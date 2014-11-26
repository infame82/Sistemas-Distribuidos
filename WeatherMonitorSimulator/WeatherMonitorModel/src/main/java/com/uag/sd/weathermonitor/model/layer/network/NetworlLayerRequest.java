package com.uag.sd.weathermonitor.model.layer.network;

import java.io.Serializable;

import com.uag.sd.weathermonitor.model.device.Device;
import com.uag.sd.weathermonitor.model.device.Beacon;
import com.uag.sd.weathermonitor.model.layer.physical.channel.RFChannel;

public class NetworlLayerRequest implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5701409115994218582L;

	public enum PRIMITIVE {
		REQUEST_NETWORK_FORMATION("Network Formation"), REQUEST_NETWORK_NODE(
				"Network Node"),REQUEST_EXTENDED_PAN_ID("Extened PAN ID"),NETWORK_DISCOVERY("Network Discovery")
				,NETWORK_JOIN("Network Join");
		public String description;

		private PRIMITIVE(String description) {
			this.description = description;
		}
	};

	private PRIMITIVE primitive;
	private Beacon device;
	private Beacon joinBeacon;
	private RFChannel channel;
	private long id;

	public NetworlLayerRequest() {
	}

	public NetworlLayerRequest(PRIMITIVE primitive, Device device) {
		this.primitive = primitive;
		this.device = device;
		id = System.currentTimeMillis();
	}

	public PRIMITIVE getPrimitive() {
		return primitive;
	}

	public void setPrimitive(PRIMITIVE primitive) {
		this.primitive = primitive;
	}

	public Beacon getDevice() {
		return device;
	}

	public void setDevice(Beacon device) {
		this.device = device;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
	

	public Beacon getJoinBeacon() {
		return joinBeacon;
	}

	public void setJoinBeacon(Beacon joinBeacon) {
		this.joinBeacon = joinBeacon;
	}

	public RFChannel getChannel() {
		return channel;
	}

	public void setChannel(RFChannel channel) {
		this.channel = channel;
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || o instanceof NetworlLayerRequest) {
			return false;
		}

		NetworlLayerRequest auxLayerRequest = (NetworlLayerRequest) o;
		return auxLayerRequest.id == id;
	}

}
