package com.uag.sd.weathermonitor.model.layer.mac;

import java.io.Serializable;
import java.util.List;

import com.uag.sd.weathermonitor.model.device.Traceable;
import com.uag.sd.weathermonitor.model.layer.physical.channel.RFChannel;

public class MacLayerRequest implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8704870493936367528L;
	
	public enum PRIMITIVE{REQUEST_MAC_NODE("MAC Node"),ENERGY_DETECTION_SCAN("Energy Detection Scan"),
		ACTIVE_SCAN("Active Scan"),SET_PAN_ID("Set PAN ID"),START("Start"),INVALID_REQUEST("Invalid Request"),
		REQUEST_REGISTERED_DEVICES("Get Registered Devices"),REQUEST_EXTENED_ADDRESS("Get Extended Address");
			public String description;
			private PRIMITIVE(String description) {
				this.description = description;
			}
		};
	
	private PRIMITIVE primitive;
	private Traceable device;
	private long id;
	private List<RFChannel> activeChannels;
	private RFChannel channel;
	private boolean responseRequired;
	
	public MacLayerRequest(){
		responseRequired = true;
	}
	public MacLayerRequest(PRIMITIVE primitive,Traceable device) {
		responseRequired = true;
		this.primitive = primitive;
		this.device = device;
		id= System.currentTimeMillis();
	}
	
	public Traceable getDevice() {
		return device;
	}
	public void setDevice(Traceable device) {
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
	
	
}
