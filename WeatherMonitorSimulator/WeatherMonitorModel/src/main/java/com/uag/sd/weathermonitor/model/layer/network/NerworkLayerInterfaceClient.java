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
import com.uag.sd.weathermonitor.model.device.Beacon;
import com.uag.sd.weathermonitor.model.layer.network.NetworkLayerResponse.CONFIRM;
import com.uag.sd.weathermonitor.model.layer.network.NetworlLayerRequest.PRIMITIVE;
import com.uag.sd.weathermonitor.model.utils.ObjectSerializer;

public class NerworkLayerInterfaceClient implements NetworkLayerInterface {

	private InetAddress group;
	private DeviceLog log;
	private Beacon device;

	public NerworkLayerInterfaceClient(Beacon device, DeviceLog log)
			throws SocketException, UnknownHostException {
		this.log = log;
		this.device = device;
		group = InetAddress.getByName(NETWORK_LAYER_ADDRESS);
	}

	public DeviceLog getLog() {
		return log;
	}

	public void setLog(DeviceLog log) {
		this.log = log;
	}

	@Override
	public NetworkLayerResponse requestNetworkLayerNode(
			NetworlLayerRequest request) {
		NetworkLayerResponse response = new NetworkLayerResponse();
		response.setConfirm(CONFIRM.INVALID_REQUEST);
		response.setMessage("Unknown");
		request.setPrimitive(PRIMITIVE.REQUEST_NETWORK_NODE);
		request.setId(System.currentTimeMillis());
		int counter = 0;

		boolean availableNode = false;
		DatagramSocket socket = null;
		try {
			socket = new DatagramSocket();
			byte[] requestContent = ObjectSerializer.serialize(request);
			DatagramPacket packet = new DatagramPacket(requestContent,
					requestContent.length, group, NETWORK_LAYER_PORT);
			mainLoop: while (!availableNode) {
				counter++;
				log.debug(new DeviceData(device.getId(),
						"Requesting network layer node (" + counter + ")"));
				socket.send(packet);
				socket.setSoTimeout(REQUEST_TIME_OUT);
				DatagramPacket reply = null;
				try {
					//while (true) {
						reply = new DatagramPacket(new byte[BUFFER_SIZE],
								BUFFER_SIZE);
						socket.receive(reply);
						response = (NetworkLayerResponse) ObjectSerializer
								.unserialize(reply.getData());
						availableNode = response.getConfirm() == CONFIRM.SUCCESS;
						if (availableNode) {
							break mainLoop;
						}
					//}
				} catch (SocketTimeoutException ste) {
					log.debug(new DeviceData(device.getId(),
							"Network layer node not available (" + counter + ")"));

				}
				if(counter==MAX_REQUEST) {
					response.setMessage("Not able to find an available netowork node");
					break;
				}
			}
		} catch (IOException e) {
			response.setMessage("Not able to serialize Network Layer Request");
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			response.setMessage("Not able to unserialize Network Layer Response");
			e.printStackTrace();
		} finally {
			if (socket != null) {
				socket.close();
			}
		}
		if (availableNode) {
			log.debug(new DeviceData(device.getId(),
					"Available network layer node :" + response.getMessage()));
		}
		return response;
	}

	private Socket getNetworkLayerSocket(Beacon device)
			throws NumberFormatException, UnknownHostException, IOException {
		NetworlLayerRequest requestNode = new NetworlLayerRequest();
		requestNode.setDevice(device);
		NetworkLayerResponse response = requestNetworkLayerNode(requestNode);
		if (response.getConfirm() != CONFIRM.SUCCESS) {
			return null;
		}
		String[] address = response.getMessage().split(":");
		return new Socket(address[0], Integer.parseInt(address[1]));
	}

	private NetworkLayerResponse sendRequest(NetworlLayerRequest request,
			PRIMITIVE primitive) {
		NetworkLayerResponse response = new NetworkLayerResponse();
		response.setConfirm(CONFIRM.INVALID_REQUEST);
		response.setMessage("Unknown");
		request.setPrimitive(primitive);
		request.setId(System.currentTimeMillis());
		Socket socket = null;
		try {
			socket = getNetworkLayerSocket(request.getDevice());
			if (socket == null) {
				response.setMessage("Unable to establish connection with a Network Layer Node");
				return response;
			}
			log.debug(new DeviceData(device.getId(), "Requesting "
					+ primitive.description + " " + socket.getInetAddress()
					+ ":" + socket.getPort()));
			ObjectOutputStream out = new ObjectOutputStream(
					socket.getOutputStream());
			out.writeObject(request);
			out.flush();
			ObjectInputStream in = new ObjectInputStream(
					socket.getInputStream());
			NetworkLayerResponse aux = (NetworkLayerResponse) in.readObject();
			response = aux;
		} catch (NumberFormatException | IOException e) {
			response.setMessage("Unable to connect to Network Layer to request "
					+ primitive.description);
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			response.setMessage("Unable to unserialize Network Layer Response");
			e.printStackTrace();
		} finally {
			try {
				if (socket != null) {
					socket.close();
				}
			} catch (IOException e) {
			}
		}

		return response;
	}

	@Override
	public NetworkLayerResponse requestNetworkFormation(
			NetworlLayerRequest request) {
		return sendRequest(request, PRIMITIVE.REQUEST_NETWORK_FORMATION);
	}

	@Override
	public NetworkLayerResponse requestExtenedPanId(NetworlLayerRequest request) {
		return sendRequest(request, PRIMITIVE.REQUEST_EXTENDED_PAN_ID);
	}

	@Override
	public NetworkLayerResponse networkDiscovery(NetworlLayerRequest request) {
		return sendRequest(request, PRIMITIVE.NETWORK_DISCOVERY);
	}

	@Override
	public NetworkLayerResponse netoworkJoin(NetworlLayerRequest request) {
		return sendRequest(request, PRIMITIVE.NETWORK_JOIN);
	}

	@Override
	public NetworkLayerResponse associate(NetworlLayerRequest request) {
		return sendRequest(request, PRIMITIVE.ASSOCIATE);
	}

}
