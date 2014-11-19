package com.uag.sd.weathermonitor.model.layer.physical;

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
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import net.jini.core.entry.UnusableEntryException;
import net.jini.core.transaction.TransactionException;

import com.uag.sd.weathermonitor.model.device.DeviceData;
import com.uag.sd.weathermonitor.model.device.DeviceLog;
import com.uag.sd.weathermonitor.model.device.Traceable;
import com.uag.sd.weathermonitor.model.layer.physical.PhysicalLayerResponse.CONFIRM;
import com.uag.sd.weathermonitor.model.layer.physical.channel.RFChannel;
import com.uag.sd.weathermonitor.model.layer.physical.channel.RFChannel.RF_CHANNEL;
import com.uag.sd.weathermonitor.model.utils.ObjectSerializer;

public class PhysicalLayerNode implements Runnable,PhysicalLayerInterface{
	
	private DeviceLog log;
	private Traceable traceableDevice;

	private MulticastSocket socket;
	private InetAddress group;
	private boolean isListening;
	private ThreadPoolExecutor requestExecutor;
	private TcpPhysicalRequestConnection tcpPhysicalRequestConnection;
	private List<RFChannel> channels;
	private PhysicalLayerInterfaceClient physicalClient;
	private EnergyLevelStabilizer energyLevelStabilizer;
	
	private class EnergyLevelStabilizer implements Runnable{

		private int timer = 10000;
		@Override
		public void run() {
			while(isListening) {
				try {
					Thread.sleep(timer);
					synchronized (channels) {
						for(RFChannel channel:channels) {
							if(channel.getEnergy()>0) {
								channel.setEnergy(channel.getEnergy()-1);
							}
						}
					}
				} catch (InterruptedException e) {}
			}
		}
	}
	
	private class PhysicalRequestResolver implements Runnable {
		private final byte[] requestContent;
		private final InetAddress requestorAddress;
		private final int requestorPort;

		public PhysicalRequestResolver(byte[] requestContent,
				InetAddress requestorAddress, int requestorPort) {
			this.requestContent = requestContent;
			this.requestorAddress = requestorAddress;
			this.requestorPort = requestorPort;
		}

		@Override
		public void run() {
			try {
				Object obj = ObjectSerializer.unserialize(requestContent);
				if (obj instanceof PhysicalLayerRequest) {
					PhysicalLayerRequest request = (PhysicalLayerRequest) obj;
					resolveRequest(request);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		private void resolveRequest(PhysicalLayerRequest request)
				throws IOException, UnusableEntryException,
				TransactionException, InterruptedException {
			PhysicalLayerResponse response = new PhysicalLayerResponse();
			response.setConfirm(CONFIRM.INVALID_REQUEST);
			response.setMessage("Invalid Primitive");
			if (request.getPrimitive() == PhysicalLayerRequest.PRIMITIVE.REQUEST_PHYSICAL_NODE) {
				response = requestPhysicalLayerNode(request);
			}else if (request.getPrimitive() == PhysicalLayerRequest.PRIMITIVE.INCREASE_ENERGY_LEVEL) {
				response = increaseEnergyLevel(request);
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
	
	
	private class TcpPhysicalRequestConnection implements Runnable {

		private ServerSocket socket;
		private boolean active;
		private ThreadPoolExecutor requestExecutor;
		
		private class TcpPhysicalRequesResolver implements Runnable{
			private Socket socket;
			private ObjectInputStream in;
			private ObjectOutputStream out;
			
			public TcpPhysicalRequesResolver(Socket socket) throws IOException {
				this.socket = socket;
				in = new ObjectInputStream(socket.getInputStream());
				out = new ObjectOutputStream(socket.getOutputStream());
			}

			@Override
			public void run() {
				PhysicalLayerResponse response = new PhysicalLayerResponse();
				response.setConfirm(CONFIRM.INVALID_REQUEST);
				response.setMessage("Unknown request");
				try {
					PhysicalLayerRequest request = (PhysicalLayerRequest) in.readObject();
					if (request.getPrimitive() == PhysicalLayerRequest.PRIMITIVE.REQUEST_GET_CHANNELS) {						
						response = getChannels(request);
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

		public TcpPhysicalRequestConnection() throws IOException {
			socket = new ServerSocket(0);
			log.debug(new DeviceData(traceableDevice.getId(),"TCP Physical Node Socket("+socket.getLocalPort()+") opened."));
			active = false;
			requestExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(10);
		}

		@Override
		public void run() {
			active = true;
			try {
				while (active) {
					requestExecutor.execute(new TcpPhysicalRequesResolver(socket.accept()));
				}
			} catch (IOException e) {
				if(active) {
					e.printStackTrace();
				}
			}finally {
				log.debug(new DeviceData(traceableDevice.getId(),"TCP Physical Node Socket("+socket.getLocalPort()+") closed."));
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
	


	public PhysicalLayerNode(Traceable traceableDevice, DeviceLog log) throws IOException {
		this.traceableDevice = traceableDevice;
		isListening = false;
		requestExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(50);
		this.log = log;
		physicalClient = new PhysicalLayerInterfaceClient(traceableDevice,log);
		PhysicalLayerRequest physicalRequest = new PhysicalLayerRequest();
		physicalRequest.setDevice(traceableDevice);
		PhysicalLayerResponse response = physicalClient.getChannels(physicalRequest);
		if(response.getConfirm() == CONFIRM.INVALID_REQUEST) {
			channels = new ArrayList<RFChannel>();
			channels.add(new RFChannel(RF_CHANNEL.CH_11));
			channels.add(new RFChannel(RF_CHANNEL.CH_12));
			channels.add(new RFChannel(RF_CHANNEL.CH_13));
			channels.add(new RFChannel(RF_CHANNEL.CH_14));
			channels.add(new RFChannel(RF_CHANNEL.CH_15));
			channels.add(new RFChannel(RF_CHANNEL.CH_16));
			channels.add(new RFChannel(RF_CHANNEL.CH_17));
			channels.add(new RFChannel(RF_CHANNEL.CH_18));
			channels.add(new RFChannel(RF_CHANNEL.CH_19));
			channels.add(new RFChannel(RF_CHANNEL.CH_20));
			channels.add(new RFChannel(RF_CHANNEL.CH_21));
			channels.add(new RFChannel(RF_CHANNEL.CH_22));
			channels.add(new RFChannel(RF_CHANNEL.CH_23));
			channels.add(new RFChannel(RF_CHANNEL.CH_24));
		}else {
			channels = response.getChannels();
		}
		
		
	}

	@Override
	public PhysicalLayerResponse requestPhysicalLayerNode(
			PhysicalLayerRequest request) {
		log.info(new DeviceData(traceableDevice.getId(),
				"Request ID ('" + request.getId()
						+ "'), Device ("+request.getDevice().getId()+") is requesting a Physical Layer Node"));
		PhysicalLayerResponse response = new PhysicalLayerResponse();
		response.setConfirm(CONFIRM.SUCCESS);
		StringBuilder builder = new StringBuilder();
		ServerSocket socket = tcpPhysicalRequestConnection.getSocket();
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
						+ "'), available Physical Node"));
		return response;
	}

	@Override
	public void run() {
		DatagramPacket packet = null;
		byte[] buf = null;
		isListening = true;
		try {
			socket = new MulticastSocket(PHYSICAL_LAYER_PORT);
			group = InetAddress.getByName(PHYSICAL_LAYER_ADDRESS);
			socket.joinGroup(group);
			log.debug(new DeviceData(traceableDevice.getId(),
					"Physical Layer Node has started to listen Multicast Socket on" + PHYSICAL_LAYER_ADDRESS
							+ ":" + PHYSICAL_LAYER_PORT));
			tcpPhysicalRequestConnection = new TcpPhysicalRequestConnection();
			energyLevelStabilizer = new EnergyLevelStabilizer();
			requestExecutor.execute(tcpPhysicalRequestConnection);
			requestExecutor.execute(energyLevelStabilizer);
			while (isListening) {
				buf = new byte[1024];
				packet = new DatagramPacket(buf, buf.length);
				socket.receive(packet);
				requestExecutor.execute(new PhysicalRequestResolver(packet
						.getData(), packet.getAddress(), packet.getPort()));
			}
			
		} catch (IOException e1) {
			if (isListening) {
				log.debug(new DeviceData(traceableDevice.getId(), e1.getMessage()));
				e1.printStackTrace();
			}
		}finally {
			log.debug(new DeviceData(traceableDevice.getId(),
					"Physical Layer Node stopped on " + PHYSICAL_LAYER_ADDRESS
							+ ":" + PHYSICAL_LAYER_PORT));
		}
	}
	
	public void stop() {
		log.debug(new DeviceData(traceableDevice.getId(),
				"Stopping Physical Layer Node on " + PHYSICAL_LAYER_ADDRESS + ":"
						+ PHYSICAL_LAYER_PORT));
		tcpPhysicalRequestConnection.stop();
		requestExecutor.shutdownNow();
		isListening = false;
		socket.close();
	}

	@Override
	public synchronized PhysicalLayerResponse getChannels(PhysicalLayerRequest request) {
		PhysicalLayerResponse response = new PhysicalLayerResponse();
		response.setConfirm(CONFIRM.SUCCESS);
		response.setMessage("");
		response.setChannels(channels);
		return response;
	}

	@Override
	public synchronized PhysicalLayerResponse increaseEnergyLevel(
			PhysicalLayerRequest request) {
		PhysicalLayerResponse response = new PhysicalLayerResponse();
		response.setConfirm(CONFIRM.SUCCESS);
		response.setMessage("");
		RFChannel channel = getChannel(request.getSelectedChannel());
		channel.setEnergy(channel.getEnergy()+1);
		return response;
	}
	
	private RFChannel getChannel(RF_CHANNEL channelNumber) {
		for(RFChannel channel:channels) {
			if(channel.getChannel()== channelNumber) {
				return channel;
			}
		}
		return null;
	}

}
