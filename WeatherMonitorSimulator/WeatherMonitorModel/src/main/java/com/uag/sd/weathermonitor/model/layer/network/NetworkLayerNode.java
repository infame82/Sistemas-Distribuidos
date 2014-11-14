package com.uag.sd.weathermonitor.model.layer.network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import net.jini.core.entry.UnusableEntryException;
import net.jini.core.transaction.TransactionException;

import com.uag.sd.weathermonitor.model.device.DeviceData;
import com.uag.sd.weathermonitor.model.device.DeviceLog;
import com.uag.sd.weathermonitor.model.device.Traceable;
import com.uag.sd.weathermonitor.model.layer.mac.MacLayerInterfaceClient;
import com.uag.sd.weathermonitor.model.layer.mac.MacLayerNode;
import com.uag.sd.weathermonitor.model.layer.mac.MacLayerRequest;
import com.uag.sd.weathermonitor.model.layer.mac.MacLayerResponse;
import com.uag.sd.weathermonitor.model.layer.network.NetworkLayerResponse.CONFIRM;
import com.uag.sd.weathermonitor.model.layer.physical.channel.RFChannel;
import com.uag.sd.weathermonitor.model.layer.physical.channel.RFChannel.RF_CHANNEL;
import com.uag.sd.weathermonitor.model.utils.ObjectSerializer;

public class NetworkLayerNode implements Runnable, NetworkLayerInterface {

	private DeviceLog log;
	private Traceable traceableDevice;

	private MulticastSocket socket;
	private InetAddress group;
	private boolean isListening;
	private TcpNetworkRequestConnection tcpNetworkRequestConnection;
	private ThreadPoolExecutor requestExecutor;
	
	
	private MacLayerNode macLayerNode;
	private MacLayerInterfaceClient macInterfaceClient;

	private class NetworkRequestResolver implements Runnable {
		private final byte[] requestContent;
		private final InetAddress requestorAddress;
		private final int requestorPort;

		public NetworkRequestResolver(byte[] requestContent,
				InetAddress requestorAddress, int requestorPort) {
			this.requestContent = requestContent;
			this.requestorAddress = requestorAddress;
			this.requestorPort = requestorPort;
		}

		@Override
		public void run() {
			try {
				Object obj = ObjectSerializer.unserialize(requestContent);
				if (obj instanceof NetworlLayerRequest) {
					NetworlLayerRequest request = (NetworlLayerRequest) obj;
					resolveRequest(request);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		private void resolveRequest(NetworlLayerRequest request)
				throws IOException, UnusableEntryException,
				TransactionException, InterruptedException {
			/*
			 * Lookup spaceFinder = new Lookup(JavaSpace.class); JavaSpace space
			 * = (JavaSpace) spaceFinder.getService();
			 * 
			 * NetworlLayerRequest entry = (NetworlLayerRequest)
			 * space.readIfExists(request, null, JavaSpace.NO_WAIT);
			 * if(entry==null) { Lease lease = space.write(request,null,30000);
			 * 
			 * }
			 */
			NetworkLayerResponse response = new NetworkLayerResponse();
			response.setConfirm(CONFIRM.INVALID_REQUEST);
			response.setMessage("Invalid Primitive");
			if (request.getPrimitive() == NetworlLayerRequest.PRIMITIVE.REQUEST_NETWORK_NODE) {
				response = requestNetworkLayerNode(request);
			}
			
			byte[] responseContent = ObjectSerializer.serialize(response);
			DatagramSocket socket = new DatagramSocket();
			DatagramPacket packet = new DatagramPacket(responseContent,
					responseContent.length, requestorAddress, requestorPort);
			socket.send(packet);
			socket.close();
		}

	}

	private class TcpNetworkRequestConnection implements Runnable {

		private ServerSocket socket;
		private boolean active;
		private ThreadPoolExecutor requestExecutor;
		
		private class TcpNetworkRequesResolver implements Runnable{
			private Socket socket;
			private ObjectInputStream in;
			private ObjectOutputStream out;
			
			public TcpNetworkRequesResolver(Socket socket) throws IOException {
				this.socket = socket;
				in = new ObjectInputStream(socket.getInputStream());
				out = new ObjectOutputStream(socket.getOutputStream());
			}

			@Override
			public void run() {
				NetworkLayerResponse response = new NetworkLayerResponse();
				response.setConfirm(CONFIRM.INVALID_REQUEST);
				response.setMessage("Unknown request");
				try {
					NetworlLayerRequest request = (NetworlLayerRequest) in.readObject();
					if (request.getPrimitive() == NetworlLayerRequest.PRIMITIVE.REQUEST_NETWORK_FORMATION) {
						
						response = requestNetworkFormation(request);
					}
				} catch (ClassNotFoundException | IOException e) {
					e.printStackTrace();
				}finally {
					try {
						out.writeObject(response);
						out.flush();
						socket.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
					
				}
			}
			
		}

		public TcpNetworkRequestConnection() throws IOException {
			socket = new ServerSocket(0);
			log.debug(new DeviceData(traceableDevice.getId(),"TCP Network Node Socket("+socket.getLocalPort()+") opened."));
			active = false;
			requestExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(10);
		}

		@Override
		public void run() {
			active = true;
			try {
				while (active) {
					requestExecutor.execute(new TcpNetworkRequesResolver(socket.accept()));
				}
			} catch (IOException e) {
				if(active) {
					e.printStackTrace();
				}
			}finally {
				log.debug(new DeviceData(traceableDevice.getId(),"TCP Network Node Socket("+socket.getLocalPort()+") closed."));
			}

		}

		public void stop() {
			active = false;
			try {
				socket.close();
				requestExecutor.shutdownNow();
			} catch (IOException e) {
			}
		}
		
		public ServerSocket getSocket() {
			return socket;
		}

	}

	public NetworkLayerNode(Traceable traceableDevice) throws IOException {
		this.traceableDevice = traceableDevice;
		isListening = false;
		requestExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(50);
		macInterfaceClient = new MacLayerInterfaceClient(traceableDevice,log);
	}

	public NetworkLayerNode(Traceable traceableDevice, DeviceLog log) throws IOException {
		this(traceableDevice);
		this.log = log;
		macInterfaceClient.setLog(log);
	}

	@Override
	public void run() {
		DatagramPacket packet = null;
		byte[] buf = null;
		isListening = true;
		try {
			macLayerNode = new MacLayerNode(traceableDevice,log);
			requestExecutor.execute(macLayerNode);
			socket = new MulticastSocket(NETWORK_LAYER_PORT);
			group = InetAddress.getByName(NETWORK_LAYER_ADDRESS);
			socket.joinGroup(group);
			log.debug(new DeviceData(traceableDevice.getId(),
					"Network Layer Node has started to listen Multicast Socket on" + NETWORK_LAYER_ADDRESS
							+ ":" + NETWORK_LAYER_PORT));
			tcpNetworkRequestConnection = new TcpNetworkRequestConnection();
			requestExecutor.execute(tcpNetworkRequestConnection);
			while (isListening) {
				buf = new byte[1024];
				packet = new DatagramPacket(buf, buf.length);
				socket.receive(packet);
				requestExecutor.execute(new NetworkRequestResolver(packet
						.getData(), packet.getAddress(), packet.getPort()));
			}
			
		} catch (IOException e1) {
			if (isListening) {
				log.debug(new DeviceData(traceableDevice.getId(), e1.getMessage()));
				e1.printStackTrace();
			}
		}finally {
			log.debug(new DeviceData(traceableDevice.getId(),
					"Network Layer Node stopped on " + NETWORK_LAYER_ADDRESS
							+ ":" + NETWORK_LAYER_PORT));
		}
	}

	public void stop() {
		log.debug(new DeviceData(traceableDevice.getId(),
				"Stopping Network Layer Node on " + NETWORK_LAYER_ADDRESS + ":"
						+ NETWORK_LAYER_PORT));
		macLayerNode.stop();
		tcpNetworkRequestConnection.stop();
		requestExecutor.shutdownNow();
		isListening = false;
		socket.close();
	}

	@Override
	public synchronized NetworkLayerResponse requestNetworkFormation(
			NetworlLayerRequest request) {
		log.info(new DeviceData(traceableDevice.getId(),
				"Request ID ('" + request.getId()
						+ "'), Device ("+request.getDevice().getId()+") is requesting Network Formation"));
		NetworkLayerResponse response = new NetworkLayerResponse();
		response.setConfirm(CONFIRM.INVALID_REQUEST);
		if(!request.getDevice().isCoordinator()) {
			response.setMessage("Device("+request.getDevice().getId()+") is not a coordinator");
			return response;
		}
		
		MacLayerRequest macRequest = new MacLayerRequest();
		macRequest.setDevice(request.getDevice());
		MacLayerResponse macResponse = macInterfaceClient.energyDetectionScan(macRequest);
		if(macResponse.getConfirm() != MacLayerResponse.CONFIRM.SUCCESS) {
			response.setMessage(macResponse.getMessage());
			return response;
		}
		
		Map<RF_CHANNEL, RFChannel> acceptableChannels = macResponse.getChannels();
		macRequest.setActiveChannels(new ArrayList<RFChannel.RF_CHANNEL>(acceptableChannels.keySet()));
		macResponse = macInterfaceClient.activeScan(macRequest);
		
		RF_CHANNEL selectedChannel = null;
		int minDevices = 0;
		Map<RF_CHANNEL, List<Traceable>> registeredDevices = macResponse.getRegisteredDevices();
		for(RF_CHANNEL channel:registeredDevices.keySet()) {
			if(selectedChannel==null) {
				selectedChannel = channel;
			}else {
				List<Traceable> devices = registeredDevices.get(channel);
				if((devices==null && minDevices>0) || (devices!=null && (devices.size()<minDevices))) {
					selectedChannel = channel;
				}
			}	
		}
		
		response.setMessage("Not implemented");
		return response;
	}

	@Override
	public synchronized NetworkLayerResponse requestNetworkLayerNode(
			NetworlLayerRequest request) {
		log.info(new DeviceData(traceableDevice.getId(),
				"Request ID ('" + request.getId()
						+ "'), Device ("+request.getDevice().getId()+") is requesting a Network Layer Node"));
		NetworkLayerResponse response = new NetworkLayerResponse();
		response.setConfirm(CONFIRM.SUCCESS);
		StringBuilder builder = new StringBuilder();
		ServerSocket socket = tcpNetworkRequestConnection.getSocket();
		try {
			builder.append(InetAddress.getLocalHost().getHostAddress());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		builder.append(":");
		builder.append(socket.getLocalPort());
		response.setMessage(builder.toString());
		log.info(new DeviceData(traceableDevice.getId(),
				"Request ID ('" + request.getId()
						+ "'), available Network Node"));
		return response;
	}

}
