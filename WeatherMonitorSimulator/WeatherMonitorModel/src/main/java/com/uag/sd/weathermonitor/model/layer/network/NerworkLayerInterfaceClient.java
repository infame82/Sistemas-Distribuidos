package com.uag.sd.weathermonitor.model.layer.network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import com.uag.sd.weathermonitor.model.device.DeviceData;
import com.uag.sd.weathermonitor.model.device.DeviceLog;
import com.uag.sd.weathermonitor.model.device.Traceable;
import com.uag.sd.weathermonitor.model.layer.network.NetworkLayerResponse.CONFIRM;
import com.uag.sd.weathermonitor.model.layer.network.NetworlLayerRequest.PRIMITIVE;
import com.uag.sd.weathermonitor.model.utils.ObjectSerializer;

public class NerworkLayerInterfaceClient implements NetworkLayerInterface {

	private InetAddress group;
	private DeviceLog log;
	private Traceable device;

	public NerworkLayerInterfaceClient(Traceable device, DeviceLog log) throws SocketException,
			UnknownHostException {
		this.log = log;
		this.device = device;
		group = InetAddress.getByName(NETWORK_LAYER_ADDRESS);
	}

	@Override
	public NetworkLayerResponse requestNetworkFormation(
			NetworlLayerRequest request) {
		NetworlLayerRequest requestNode = new NetworlLayerRequest();
		requestNode.setDevice(request.getDevice());
		NetworkLayerResponse response = requestNetworkLayerNode(requestNode);
		if(response.getConfirm() != CONFIRM.SUCCESS) {
			return response;
		}
		String[] address = response.getMessage().split(":");
		response.setConfirm(CONFIRM.INVALID_REQUEST);
		response.setMessage("Unknown");
		request.setPrimitive(PRIMITIVE.REQUEST_NETWORK_FORMATION);
		Socket socket = null;
		try {
			socket = new Socket(address[0], Integer.parseInt(address[1]));
			log.debug(new DeviceData(device.getId(), "Requesting Network Formation to "+address[0]+":"+address[1]));
			ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
			out.writeObject(request);
			out.flush();
			ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
			NetworkLayerResponse aux = (NetworkLayerResponse) in.readObject();
			response = aux;
		} catch (NumberFormatException | IOException e) {
			response.setMessage("Unable to connect to Network Layer to request formation");
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			response.setMessage("Unable to unserualize Response");
			e.printStackTrace();
		}finally {
			try {
				if(socket!=null) {
					socket.close();
				}
			} catch (IOException e) {
			}
		}
		
		return response;
	}

	@Override
	public NetworkLayerResponse requestNetworkLayerNode(
			NetworlLayerRequest request) {
		NetworkLayerResponse response = new NetworkLayerResponse();
		response.setConfirm(CONFIRM.INVALID_REQUEST);
		response.setMessage("Unknown");
		request.setPrimitive(PRIMITIVE.REQUEST_NETWORK_NODE);
		request.setId(System.currentTimeMillis());
		int counter = 1;
		int MAX_REQUEST = 6;
		boolean availableNode = false;
		DatagramSocket socket = null;
		try {
			socket = new DatagramSocket();
			byte[] requestContent = ObjectSerializer.serialize(request);
			DatagramPacket packet = new DatagramPacket(requestContent,
					requestContent.length, group, NETWORK_LAYER_PORT);
			mainLoop: while (!availableNode) {
				log.debug(new DeviceData(device.getId(), "Requesting network layer node ("+counter+")"));
				socket.send(packet);
				socket.setSoTimeout(5000);
				DatagramPacket reply = null;
				try {
					while (true) {
						reply = new DatagramPacket(new byte[1024], 1024);
						socket.receive(reply);
						response = (NetworkLayerResponse) ObjectSerializer
								.unserialize(reply.getData());
						availableNode = response.getConfirm() == CONFIRM.SUCCESS;
						if (availableNode) {
							break mainLoop;
						}
					}
				} catch (SocketTimeoutException ste) {
					log.debug(new DeviceData(device.getId(), "Network layer node not available ("+counter+""));
					counter++;
				}
				if(counter==MAX_REQUEST) {
					response.setMessage("Not able to find an available node");
					break;
				}
			}
		} catch (IOException e) {
			response.setMessage("Not able to serialize request");
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			response.setMessage("Not able to unserialize response");
			e.printStackTrace();
		}finally {
			if(socket!=null) {
				socket.close();
			}
		}
		if(availableNode) {
			log.debug(new DeviceData(device.getId(), "Available network layer node :"+response.getMessage()));
		}
		return response;
	}

	public DeviceLog getLog() {
		return log;
	}

	public void setLog(DeviceLog log) {
		this.log = log;
	}
	
	

}
