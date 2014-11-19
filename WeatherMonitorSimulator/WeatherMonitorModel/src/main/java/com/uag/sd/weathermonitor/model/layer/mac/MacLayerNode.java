package com.uag.sd.weathermonitor.model.layer.mac;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import net.jini.core.entry.UnusableEntryException;
import net.jini.core.transaction.TransactionException;

import com.uag.sd.weathermonitor.model.device.DeviceData;
import com.uag.sd.weathermonitor.model.device.DeviceLog;
import com.uag.sd.weathermonitor.model.device.Traceable;
import com.uag.sd.weathermonitor.model.layer.mac.MacLayerResponse.CONFIRM;
import com.uag.sd.weathermonitor.model.layer.physical.PhysicalLayerInterfaceClient;
import com.uag.sd.weathermonitor.model.layer.physical.PhysicalLayerRequest;
import com.uag.sd.weathermonitor.model.layer.physical.PhysicalLayerResponse;
import com.uag.sd.weathermonitor.model.layer.physical.channel.RFChannel;
import com.uag.sd.weathermonitor.model.utils.ObjectSerializer;

public class MacLayerNode implements Runnable, MacLayerInterface {

	private DeviceLog log;
	private Traceable traceableDevice;
	private boolean active;

	private MulticastSocket socket;
	private InetAddress group;
	private long extendedAddress;

	private TcpMacRequestConnection tcpMacConnection;
	private ThreadPoolExecutor requestExecutor;
	
	
	private PhysicalLayerInterfaceClient physicalClient;
	private MacLayerInterfaceClient macClient;
	
	private Map<RFChannel,List<Traceable>> registeredDevices;
	
	private class MacRequestResolver implements Runnable{

		private final byte[] requestContent;
		private final InetAddress requestorAddress;
		private final int requestorPort;
		
		public MacRequestResolver(byte[] requestContent,
				InetAddress requestorAddress, int requestorPort) {
			this.requestContent = requestContent;
			this.requestorAddress = requestorAddress;
			this.requestorPort = requestorPort;
		}
		@Override
		public void run() {
			try {
				Object obj = ObjectSerializer.unserialize(requestContent);
				if (obj instanceof MacLayerRequest) {
					MacLayerRequest request = (MacLayerRequest) obj;
					resolveRequest(request);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		private void resolveRequest(MacLayerRequest request)
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
			MacLayerResponse response = new MacLayerResponse();
			response.setConfirm(CONFIRM.INVALID_REQUEST);
			response.setMessage("Invalid Primitive");
			if (request.getPrimitive() == MacLayerRequest.PRIMITIVE.REQUEST_MAC_NODE) {
				response = requestMacLayerNode(request);
			}else if (request.getPrimitive() == MacLayerRequest.PRIMITIVE.SET_PAN_ID) {
				response = setPANId(request);
			}else if (request.getPrimitive() == MacLayerRequest.PRIMITIVE.START) {
				response = start(request);
			}
			if(request.isResponseRequired()) {
				byte[] responseContent = ObjectSerializer.serialize(response);
				DatagramSocket socket = new DatagramSocket();
				DatagramPacket packet = new DatagramPacket(responseContent,
						responseContent.length, requestorAddress, requestorPort);
				socket.send(packet);
				socket.close();
			}
		}
		
	}

	private class TcpMacRequestConnection implements Runnable {

		private ServerSocket socket;
		private boolean active;
		private ThreadPoolExecutor requestExecutor;
		
		

		private class TcpMacRequestResolver implements Runnable {
			private Socket socket;
			private ObjectInputStream in;
			private ObjectOutputStream out;

			public TcpMacRequestResolver(Socket socket) throws IOException {
				this.socket = socket;
				in = new ObjectInputStream(socket.getInputStream());
				out = new ObjectOutputStream(socket.getOutputStream());
			}

			@Override
			public void run() {
				MacLayerResponse response = new MacLayerResponse();
				response.setConfirm(CONFIRM.INVALID_REQUEST);
				response.setMessage("Unknown request");
				try {
					MacLayerRequest request = (MacLayerRequest) in.readObject();
					if (request.getPrimitive() == MacLayerRequest.PRIMITIVE.ENERGY_DETECTION_SCAN) {
						response = energyDetectionScan(request);
					}else if (request.getPrimitive() == MacLayerRequest.PRIMITIVE.REQUEST_REGISTERED_DEVICES) {
						response = getRegisteredDevices(request);
					}else if (request.getPrimitive() == MacLayerRequest.PRIMITIVE.REQUEST_EXTENED_ADDRESS) {
						response = getExtendedAddress(request);
					}else if (request.getPrimitive() == MacLayerRequest.PRIMITIVE.ACTIVE_SCAN) {
						response = activeScan(request);
					}
				} catch (ClassNotFoundException | IOException e) {
					e.printStackTrace();
				} finally {
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

		public TcpMacRequestConnection() throws IOException {
			socket = new ServerSocket(0);
			log.debug(new DeviceData(traceableDevice.getId(),
					"TCP MAC Node Socket(" + socket.getLocalPort()
							+ ") opened."));
			active = false;
			requestExecutor = (ThreadPoolExecutor) Executors
					.newFixedThreadPool(10);
		}

		@Override
		public void run() {
			active = true;
			try {
				while (active) {
					requestExecutor.execute(new TcpMacRequestResolver(socket
							.accept()));
				}
			} catch (IOException e) {
				if (active) {
					e.printStackTrace();
				}
			}finally {
				log.debug(new DeviceData(traceableDevice.getId(),
						"TCP MAC Node Socket(" + socket.getLocalPort()
								+ ") closed."));
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

	public MacLayerNode(Traceable traceableDevice, DeviceLog log) throws SocketException, UnknownHostException {
		this.traceableDevice = traceableDevice;
		this.log = log;
		active = false;
		requestExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(50);
		physicalClient = new PhysicalLayerInterfaceClient(traceableDevice,log);
		macClient = new MacLayerInterfaceClient(traceableDevice, log);
		
		MacLayerRequest macRequest = new MacLayerRequest();
		macRequest.setDevice(traceableDevice);
		MacLayerResponse response = macClient.getRegisteredDevices(macRequest);
		registeredDevices = response.getRegisteredDevices();
		if(registeredDevices==null) {
			registeredDevices = new HashMap<RFChannel, List<Traceable>>();
		}
		
		response = macClient.getExtendedAddress(macRequest);
		if(response.getConfirm()==CONFIRM.INVALID_REQUEST) {
			extendedAddress = new Random().nextLong();
		}else {
			extendedAddress = response.getExtendedAddress();
		}		
	}

	@Override
	public void run() {
		DatagramPacket packet = null;
		byte[] buf = null;
		active = true;
		try {
			
			
			socket = new MulticastSocket(MAC_LAYER_PORT);
			group = InetAddress.getByName(MAC_LAYER_ADDRESS);
			socket.joinGroup(group);
			log.debug(new DeviceData(traceableDevice.getId(),
					"MAC Layer Node has started to listen Multicast Socket on " + MAC_LAYER_ADDRESS
							+ ":" + MAC_LAYER_PORT));
			tcpMacConnection = new TcpMacRequestConnection();
			requestExecutor.execute(tcpMacConnection);
			while (active) {
				buf = new byte[2048];
				packet = new DatagramPacket(buf, buf.length);
				socket.receive(packet);
				requestExecutor.execute(new MacRequestResolver(packet
						.getData(), packet.getAddress(), packet.getPort()));
			}
			
		} catch (Exception e) {
			if (active) {
				log.debug(new DeviceData(traceableDevice.getId(), e.getMessage()));
				e.printStackTrace();
			}
		}finally {
			log.debug(new DeviceData(traceableDevice.getId(),
					"MAC Layer Node stopped on " + MAC_LAYER_ADDRESS
							+ ":" + MAC_LAYER_PORT));
		}
	}

	public void stop() {
		log.debug(new DeviceData(traceableDevice.getId(),
				"Stopping MAC Layer Node on " + MAC_LAYER_ADDRESS + ":"
						+ MAC_LAYER_PORT));
		
		active = false;
		tcpMacConnection.stop();
		requestExecutor.shutdownNow();
		socket.close();
	}

	public DeviceLog getLog() {
		return log;
	}

	public void setLog(DeviceLog log) {
		this.log = log;
	}

	public Traceable getTraceableDevice() {
		return traceableDevice;
	}

	public void setTraceableDevice(Traceable traceableDevice) {
		this.traceableDevice = traceableDevice;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	@Override
	public synchronized MacLayerResponse requestMacLayerNode(MacLayerRequest request) {
		log.info(new DeviceData(traceableDevice.getId(), "Request ID ('"
				+ request.getId() + "'), Device ("
				+ request.getDevice().getId()
				+ ") is requesting a MAC Layer Node"));
		MacLayerResponse response = new MacLayerResponse();
		response.setConfirm(CONFIRM.SUCCESS);
		StringBuilder builder = new StringBuilder();
		ServerSocket socket = tcpMacConnection.getSocket();
		try {
			builder.append(InetAddress.getLocalHost().getHostAddress());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		builder.append(":");
		builder.append(socket.getLocalPort());
		response.setMessage(builder.toString());
		log.info(new DeviceData(traceableDevice.getId(), "Request ID ('"
				+ request.getId() + "'), available MAC Node"));
		return response;
	}

	@Override
	public synchronized MacLayerResponse energyDetectionScan(MacLayerRequest request) {
		log.info(new DeviceData(traceableDevice.getId(),
				"Request ID ('" + request.getId()
						+ "'), Device ("+request.getDevice().getId()+") is requesting "+request.getPrimitive().description));
		MacLayerResponse response = new MacLayerResponse();
		response.setConfirm(CONFIRM.INVALID_REQUEST);
		response.setMessage("Not implemented");
		PhysicalLayerRequest physicalRequest = new PhysicalLayerRequest();
		physicalRequest.setDevice(request.getDevice());
		PhysicalLayerResponse physicalResponse = physicalClient.getChannels(physicalRequest);
		if(physicalResponse==null || physicalResponse.getConfirm()!=com.uag.sd.weathermonitor.model.layer.physical.PhysicalLayerResponse.CONFIRM.SUCCESS) {
			response.setMessage("Unable to have available channels");
			return response;
		}
		
		List<RFChannel> acceptableChannels = new ArrayList<RFChannel>();
		for(RFChannel channel:physicalResponse.getChannels()) {
			if(channel.getEnergy()<=ACCEPTABLE_ENERGY_LEVEL) {
				acceptableChannels.add(channel);
			}
		}
		if(acceptableChannels.isEmpty()) {
			response.setMessage("Not available channels with acceptable energy: "+ACCEPTABLE_ENERGY_LEVEL);
			response.setChannels(acceptableChannels);
			return response;
		}
		
		response.setConfirm(CONFIRM.SUCCESS);
		response.setMessage("OK");
		response.setChannels(acceptableChannels);
		return response;
	}

	@Override
	public synchronized MacLayerResponse activeScan(MacLayerRequest request) {
		log.info(new DeviceData(traceableDevice.getId(),
				"Request ID ('" + request.getId()
						+ "'), Device ("+request.getDevice().getId()+") is requesting "+request.getPrimitive().description));
		MacLayerResponse response = new MacLayerResponse();
		response.setConfirm(CONFIRM.SUCCESS);
		response.setMessage("OK");
		Map<RFChannel,List<Traceable>> registeredDevices = new HashMap<RFChannel, List<Traceable>>();
		for(RFChannel channel:request.getActiveChannels()) {
			registeredDevices.put(channel,registeredDevices.get(channel));
		}
		response.setRegisteredDevices(registeredDevices);
		return response;
	}

	@Override
	public synchronized MacLayerResponse setPANId(MacLayerRequest request) {
		log.info(new DeviceData(traceableDevice.getId(),
				"Request ID ('" + request.getId()
						+ "'), Device ("+request.getDevice().getId()+") is requesting "+request.getPrimitive().description));
		MacLayerResponse response = new MacLayerResponse();
		response.setConfirm(CONFIRM.SUCCESS);
		List<Traceable> devices = registeredDevices.get(request.getChannel());
		if(devices == null) {
			devices = new ArrayList<Traceable>();
			devices.add(request.getDevice());
		}
		registeredDevices.put(request.getChannel(), devices);
		return response;
	}

	@Override
	public synchronized MacLayerResponse start(MacLayerRequest request) {
		log.info(new DeviceData(traceableDevice.getId(),
				"Request ID ('" + request.getId()
						+ "'), Device ("+request.getDevice().getId()+") is requesting "+request.getPrimitive().description));
		List<Traceable> devices = registeredDevices.get(request.getChannel());
		for(Traceable device:devices) {
			if(device.getId().equals(request.getDevice().getId()) && device.getPanId()== request.getDevice().getPanId()) {
				device.setStarted(true);
				break;
			}
		}
		
		MacLayerResponse response = new MacLayerResponse();
		response.setConfirm(CONFIRM.SUCCESS);
		return response;
	}

	@Override
	public synchronized MacLayerResponse getRegisteredDevices(MacLayerRequest request) {
		MacLayerResponse response = new MacLayerResponse();
		response.setConfirm(CONFIRM.SUCCESS);
		response.setMessage("");
		response.setRegisteredDevices(registeredDevices);
		return response;
	}

	@Override
	public MacLayerResponse getExtendedAddress(MacLayerRequest request) {
		MacLayerResponse response = new MacLayerResponse();
		response.setConfirm(CONFIRM.SUCCESS);
		response.setMessage("");
		response.setExtendedAddress(extendedAddress);
		return response;
	}

}
