package com.uag.sd.weathermonitor.model.layer.mac;

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
import com.uag.sd.weathermonitor.model.layer.mac.MacLayerRequest.PRIMITIVE;
import com.uag.sd.weathermonitor.model.layer.mac.MacLayerResponse.CONFIRM;
import com.uag.sd.weathermonitor.model.utils.ObjectSerializer;

public class MacLayerInterfaceClient implements MacLayerInterface {

	private InetAddress group;
	private DeviceLog log;
	private Beacon device;

	public MacLayerInterfaceClient(Beacon device, DeviceLog log)
			throws SocketException, UnknownHostException {
		this.log = log;
		this.device = device;
		group = InetAddress.getByName(MAC_LAYER_ADDRESS);
	}

	public DeviceLog getLog() {
		return log;
	}

	public void setLog(DeviceLog log) {
		this.log = log;
	}

	@Override
	public MacLayerResponse requestMacLayerNode(MacLayerRequest request) {
		MacLayerResponse response = new MacLayerResponse();
		response.setConfirm(CONFIRM.INVALID_REQUEST);
		response.setMessage("Unknown");
		request.setPrimitive(PRIMITIVE.REQUEST_MAC_NODE);
		request.setId(System.currentTimeMillis());
		int counter = 0;
		
		boolean availableNode = false;
		DatagramSocket socket = null;
		try {
			socket = new DatagramSocket();
			byte[] requestContent = ObjectSerializer.serialize(request);
			DatagramPacket packet = new DatagramPacket(requestContent,
					requestContent.length, group, MAC_LAYER_PORT);
			mainLoop: while (!availableNode) {
				counter++;
				socket.send(packet);
				socket.setSoTimeout(REQUEST_TIME_OUT);
				DatagramPacket reply = null;
				try {
					while (true) {
						reply = new DatagramPacket(new byte[BUFFER_SIZE], BUFFER_SIZE);
						socket.receive(reply);
						response = (MacLayerResponse) ObjectSerializer
								.unserialize(reply.getData());
						if (response.getConfirm() == CONFIRM.SUCCESS) {
							break mainLoop;
						}
					}
				} catch (SocketTimeoutException ste) {					
					log.debug(new DeviceData(device.getId(), "MAC layer node not available ("+counter+")"));
					
				}
				if(counter==MAX_REQUEST) {
					response.setMessage("Not able to find an available MAC node");
					break;
				}
			}
		} catch (IOException e) {
			response.setMessage("Not able to serialize MAC Layer Request");
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			response.setMessage("Not able to unserialize MAC Layer Response");
			e.printStackTrace();
		}finally {
			if(socket!=null) {
				socket.close();
			}
		}
		
		return response;
	}
	
	private Socket getMacLayerSocket(Beacon device) throws NumberFormatException, UnknownHostException, IOException {
		MacLayerRequest requestNode = new MacLayerRequest();
		requestNode.setDevice(device);
		MacLayerResponse response = requestMacLayerNode(requestNode);
		if(response.getConfirm() != CONFIRM.SUCCESS) {
			return null;
		}
		String[] address = response.getMessage().split(":");
		return new Socket(address[0], Integer.parseInt(address[1]));
	}
	
	private MacLayerResponse sendRequest(MacLayerRequest request,PRIMITIVE primitive) {
		MacLayerResponse response = new MacLayerResponse();
		response.setConfirm(CONFIRM.INVALID_REQUEST);
		response.setMessage("Unknown");
		request.setPrimitive(primitive);
		request.setId(System.currentTimeMillis());
		Socket socket = null;
		try {
			socket = getMacLayerSocket(request.getDevice());
			if(socket==null) {
				response.setMessage("Unable to establish connection with a Mac Layer Node");
				return response;
			}
			log.debug(new DeviceData(device.getId(), "Requesting "+primitive.description+" "+socket.getInetAddress()+":"+socket.getPort()));
			ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
			out.writeObject(request);
			out.flush();
			ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
			MacLayerResponse aux = (MacLayerResponse) in.readObject();
			response = aux;
		} catch (NumberFormatException | IOException e) {
			response.setMessage("Unable to connect to Mac Layer to request "+primitive.description);
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			response.setMessage("Unable to unserialize Mac Layer Response");
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
	public MacLayerResponse energyDetectionScan(MacLayerRequest request) {
		return sendRequest(request, PRIMITIVE.ENERGY_DETECTION_SCAN);
	}

	@Override
	public MacLayerResponse activeScan(MacLayerRequest request) {
		return sendRequest(request, PRIMITIVE.ACTIVE_SCAN);
	}

	@Override
	public MacLayerResponse setPANId(MacLayerRequest request) {
		MacLayerResponse response = new MacLayerResponse();
		response.setConfirm(CONFIRM.SUCCESS);
		request.setResponseRequired(false);
		request.setId(System.currentTimeMillis());
		request.setPrimitive(PRIMITIVE.SET_PAN_ID);
		DatagramSocket socket = null;
		try {
			socket = new DatagramSocket();
			byte[] requestContent = ObjectSerializer.serialize(request);
			DatagramPacket packet = new DatagramPacket(requestContent,
					requestContent.length, group, MAC_LAYER_PORT);
			socket.send(packet);
		}catch(Exception e) {
			response.setConfirm(CONFIRM.INVALID_REQUEST);
			response.setMessage("Unable to SET PAN ID");
		} finally {
			if (socket != null) {
				socket.close();
			}
		}
		
		return response;
	}

	@Override
	public MacLayerResponse start(MacLayerRequest request) {
		MacLayerResponse response = new MacLayerResponse();
		response.setConfirm(CONFIRM.SUCCESS);
		request.setResponseRequired(false);
		request.setId(System.currentTimeMillis());
		//request.setDevice(device);
		request.setPrimitive(PRIMITIVE.START);
		DatagramSocket socket = null;
		try {
			socket = new DatagramSocket();
			byte[] requestContent = ObjectSerializer.serialize(request);
			DatagramPacket packet = new DatagramPacket(requestContent,
					requestContent.length, group, MAC_LAYER_PORT);
			socket.send(packet);
		}catch(Exception e) {
			response.setConfirm(CONFIRM.INVALID_REQUEST);
			response.setMessage("Unable to START NETWORK");
		} finally {
			if (socket != null) {
				socket.close();
			}
		}
		
		return response;
	}

	@Override
	public MacLayerResponse getRegisteredNetworks(MacLayerRequest request) {
		return sendRequest(request, PRIMITIVE.REQUEST_REGISTERED_NETWORKS);
	}

	@Override
	public MacLayerResponse getExtendedAddress(MacLayerRequest request) {
		return sendRequest(request, PRIMITIVE.REQUEST_EXTENED_ADDRESS);
	}

	@Override
	public MacLayerResponse association(MacLayerRequest request) {
		return sendRequest(request, PRIMITIVE.ASSOCIATION);
	}

	@Override
	public MacLayerResponse getRegisteredDevices(MacLayerRequest request) {
		return sendRequest(request, PRIMITIVE.REQUEST_REGISTERED_DEVICES);
	}

	@Override
	public MacLayerResponse registerDevice(MacLayerRequest request) {
		MacLayerResponse response = new MacLayerResponse();
		response.setConfirm(CONFIRM.SUCCESS);
		request.setResponseRequired(false);
		request.setId(System.currentTimeMillis());
		request.setPrimitive(PRIMITIVE.REGISTER_DEVICE);
		DatagramSocket socket = null;
		try {
			socket = new DatagramSocket();
			byte[] requestContent = ObjectSerializer.serialize(request);
			DatagramPacket packet = new DatagramPacket(requestContent,
					requestContent.length, group, MAC_LAYER_PORT);
			socket.send(packet);
		}catch(Exception e) {
			response.setConfirm(CONFIRM.INVALID_REQUEST);
			response.setMessage("Unable to REGISTER");
		} finally {
			if (socket != null) {
				socket.close();
			}
		}
		
		return response;
	}

	@Override
	public MacLayerResponse transmission(MacLayerRequest request) {
		return sendRequest(request, PRIMITIVE.TRANSMISSION);
	}
	
	
	
}
