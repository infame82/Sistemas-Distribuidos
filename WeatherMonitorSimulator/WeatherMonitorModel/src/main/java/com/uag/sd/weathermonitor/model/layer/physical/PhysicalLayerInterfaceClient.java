package com.uag.sd.weathermonitor.model.layer.physical;

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
import com.uag.sd.weathermonitor.model.layer.physical.PhysicalLayerRequest.PRIMITIVE;
import com.uag.sd.weathermonitor.model.layer.physical.PhysicalLayerResponse.CONFIRM;
import com.uag.sd.weathermonitor.model.utils.ObjectSerializer;

public class PhysicalLayerInterfaceClient implements PhysicalLayerInterface{
	
	private InetAddress group;
	private DeviceLog log;
	private Traceable device;

	public PhysicalLayerInterfaceClient(Traceable device, DeviceLog log)
			throws SocketException, UnknownHostException {
		this.log = log;
		this.device = device;
		group = InetAddress.getByName(PHYSICAL_LAYER_ADDRESS);
	}

	public DeviceLog getLog() {
		return log;
	}

	public void setLog(DeviceLog log) {
		this.log = log;
	}


	@Override
	public PhysicalLayerResponse requestPhysicalLayerNode(
			PhysicalLayerRequest request) {
		PhysicalLayerResponse response = new PhysicalLayerResponse();
		response.setConfirm(CONFIRM.INVALID_REQUEST);
		response.setMessage("Unknown");
		request.setPrimitive(PRIMITIVE.REQUEST_PHYSICAL_NODE);
		request.setId(System.currentTimeMillis());
		int counter = 0;

		boolean availableNode = false;
		DatagramSocket socket = null;
		try {
			socket = new DatagramSocket();
			byte[] requestContent = ObjectSerializer.serialize(request);
			DatagramPacket packet = new DatagramPacket(requestContent,
					requestContent.length, group, PHYSICAL_LAYER_PORT);
			mainLoop: while (!availableNode) {
				log.debug(new DeviceData(device.getId(),
						request.getId()+": Requesting physical layer node (" + counter + ")"));
				socket.send(packet);
				socket.setSoTimeout(REQUEST_TIME_OUT);
				DatagramPacket reply = null;
				try {
					//while (true) {
						reply = new DatagramPacket(new byte[BUFFER_SIZE],
								BUFFER_SIZE);
						socket.receive(reply);
						response = (PhysicalLayerResponse) ObjectSerializer
								.unserialize(reply.getData());
						availableNode = response.getConfirm() == CONFIRM.SUCCESS;
						if (availableNode) {
							break mainLoop;
						}
				//	}
				} catch (SocketTimeoutException ste) {
					log.debug(new DeviceData(device.getId(),
							"Physical layer node not available (" + counter + ")"));
					counter++;

				}
				if (counter == MAX_REQUEST) {
					response.setMessage("Not able to find an available node");
					break;
				}
			}
		} catch (IOException e) {
			response.setMessage("Not able to serialize Physical Layer Request");
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			response.setMessage("Not able to unserialize Physical Layer Response");
			e.printStackTrace();
		} finally {
			if (socket != null) {
				socket.close();
			}
		}
		if (availableNode) {
			log.debug(new DeviceData(device.getId(),
					"Available Physical layer node: " + response.getMessage()));
		}
		return response;
	}
	
	private Socket getPhysicalLayerSocket(Traceable device)
			throws NumberFormatException, UnknownHostException, IOException {
		PhysicalLayerRequest requestNode = new PhysicalLayerRequest();
		requestNode.setDevice(device);
		PhysicalLayerResponse response = requestPhysicalLayerNode(requestNode);
		if (response.getConfirm() != CONFIRM.SUCCESS) {
			return null;
		}
		String[] address = response.getMessage().split(":");
		return new Socket(address[0], Integer.parseInt(address[1]));
	}

	private PhysicalLayerResponse sendRequest(PhysicalLayerRequest request,
			PRIMITIVE primitive) {
		PhysicalLayerResponse response = new PhysicalLayerResponse();
		response.setConfirm(CONFIRM.INVALID_REQUEST);
		response.setMessage("Unknown");
		request.setPrimitive(primitive);
		request.setId(System.currentTimeMillis());
		Socket socket = null;
		try {
			socket = getPhysicalLayerSocket(request.getDevice());
			if (socket == null) {
				response.setMessage("Unable to establish connection with a Physical Layer Node");
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
			PhysicalLayerResponse aux = (PhysicalLayerResponse) in.readObject();
			response = aux;
		} catch (NumberFormatException | IOException e) {
			response.setMessage("Unable to connect to Physical Layer to request "
					+ primitive.description);
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			response.setMessage("Unable to unserialize Physical Layer Response");
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
	public PhysicalLayerResponse getChannels(PhysicalLayerRequest request) {
		return sendRequest(request, PRIMITIVE.REQUEST_GET_CHANNELS);
	}

	@Override
	public PhysicalLayerResponse increaseEnergyLevel(
			PhysicalLayerRequest request) {
		PhysicalLayerResponse response = new PhysicalLayerResponse();
		response.setConfirm(CONFIRM.SUCCESS);
		request.setResponseRequired(false);
		request.setId(System.currentTimeMillis());
		request.setDevice(device);
		request.setPrimitive(PRIMITIVE.INCREASE_ENERGY_LEVEL);
		DatagramSocket socket = null;
		try {
			socket = new DatagramSocket();
			byte[] requestContent = ObjectSerializer.serialize(request);
			DatagramPacket packet = new DatagramPacket(requestContent,
					requestContent.length, group, PHYSICAL_LAYER_PORT);
			socket.send(packet);
		}catch(Exception e) {
			response.setConfirm(CONFIRM.INVALID_REQUEST);
			response.setMessage("Unable to increase energy");
		} finally {
			if (socket != null) {
				socket.close();
			}
		}
		return response;
	}

}
