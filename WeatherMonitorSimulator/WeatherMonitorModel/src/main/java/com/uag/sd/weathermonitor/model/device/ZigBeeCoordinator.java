package com.uag.sd.weathermonitor.model.device;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class ZigBeeCoordinator extends Device {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7156658081587428861L;
	private Map<String, DeviceData> valuesMap;
	private transient Connection con;
	private transient Channel channel;
	private transient String queueMsgServer;

	public ZigBeeCoordinator(String id, DeviceLog log, String queueMsgServer)
			throws IOException {
		super(id, log);
		coordinator = true;
		allowJoin = true;
		this.queueMsgServer = queueMsgServer;
	}

	@Override
	protected void init() {
		valuesMap = new HashMap<String, DeviceData>();

	}

	@Override
	public void stop() {
		super.stop();
		try {
			channel.close();
			con.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void execute(DataMessage msg) {

		String mapKey = msg.getBeacon().getId() + ":"
				+ msg.getBeacon().getLocation();
		// DeviceData prevData = valuesMap.get(mapKey);
		// if(prevData==null ||
		// !prevData.getData().equals(msg.getData().getData())) {
		log.info(new DeviceData(this.id, "Msg ID:" + msg.getId() + ", from "
				+ msg.getBeacon().getId() + "[" + msg.getBeacon().getLocation()
				+ "]" + ", data:" + msg.getData().getData() + ", Type:"
				+ msg.getData().getType()));
		valuesMap.put(mapKey, msg.getData());
		try {
			ConnectionFactory factory = new ConnectionFactory();
			factory.setHost(queueMsgServer);
			con = factory.newConnection();
			channel = con.createChannel();
			if (channel != null) {
				channel.basicPublish("", "tempNetworkQueue", false, false,
						null, buildMessage(msg.getBeacon(), msg.getData())
								.getBytes());
			}
			con.close();
		} catch (IOException e) {
		}
		// }
	}

	private String buildMessage(Beacon beacon, DeviceData data) {
		JSONObject obj = new JSONObject();
		obj.put("id", beacon.getId());
		obj.put("x", beacon.getLocation().x);
		obj.put("y", beacon.getLocation().y);
		obj.put("temp", data.getData());
		return obj.toString();
	}

}
