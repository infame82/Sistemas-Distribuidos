package com.uag.sd.weathermonitor.model.layer.mac;

import java.io.Serializable;
import java.util.List;

import com.uag.sd.weathermonitor.model.device.Beacon;
import com.uag.sd.weathermonitor.model.layer.physical.channel.RFChannel;

public class MacLayerRequest implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8704870493936367528L;
	
	public enum PRIMITIVE{REQUEST_MAC_NODE("MAC Node"),ENERGY_DETECTION_SCAN("Energy Detection Scan"),
		ACTIVE_SCAN("Active Scan"),SET_PAN_ID("Set PAN ID"),START("Start"),INVALID_REQUEST("Invalid Request"),
		REQUEST_REGISTERED_NETWORKS("Get Registered Networks"),REQUEST_EXTENED_ADDRESS("Get Extended Address"),
		ASSOCIATION("Association"),REQUEST_REGISTERED_DEVICES("Get Registered Devices"),REGISTER_DEVICE("Register Device"),
		TRANSMISSION("Transmission");
			public String description;
			private PRIMITIVE(String description) {
				this.description = description;
			}
		};
	
	private PRIMITIVE primitive;
	private Beacon device;
	private Beacon joinBeacon;
	private long id;
	private List<RFChannel> activeChannels;
	private RFChannel channel;
	private boolean responseRequired;
	
	public MacLayerRequest(){
		responseRequired = true;
	}
	public MacLayerRequest(PRIMITIVE primitive,Beacon device) {
		responseRequired = true;
		this.primitive = primitive;
		this.device = device;
		id= System.currentTimeMillis();
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
	public PRIMITIVE getPrimitive() {
		return primitive;
	}
	public void setPrimitive(PRIMITIVE primitive) {
		this.primitive = primitive;
	}
	
	@Override
	public boolean equals(Object o) {
		if(o==null || o instanceof MacLayerRequest) {
			return false;
		}
		
		MacLayerRequest auxLayerRequest = (MacLayerRequest) o;
		return auxLayerRequest.id == id;
	}
	public List<RFChannel> getActiveChannels() {
		return activeChannels;
	}
	public void setActiveChannels(List<RFChannel> activeChannels) {
		this.activeChannels = activeChannels;
	}
	public RFChannel getChannel() {
		return channel;
	}
	public void setChannel(RFChannel channel) {
		this.channel = channel;
	}
	public boolean isResponseRequired() {
		return responseRequired;
	}
	public void setResponseRequired(boolean responseRequired) {
		this.responseRequired = responseRequired;
	}
	public Beacon getJoinBeacon() {
		return joinBeacon;
	}
	public void setJoinBeacon(Beacon joinBeacon) {
		this.joinBeacon = joinBeacon;
	}
	
	
	
}
