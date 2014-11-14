package com.uag.sd.weathermonitor.model.layer.mac;

import java.io.Serializable;
import java.util.List;

import com.uag.sd.weathermonitor.model.device.Traceable;
import com.uag.sd.weathermonitor.model.layer.physical.channel.RFChannel.RF_CHANNEL;

public class MacLayerRequest implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8704870493936367528L;
	
	public enum PRIMITIVE{REQUEST_MAC_NODE("MAC Node"),ENERGY_DETECTION_SCAN("Energy Detection Scan"),
		ACTIVE_SCAN("Active Scan"),SET_PAN_ID("Set PAN ID"),START("Start"),INVALID_REQUEST("Invalid Request"),
		REQUEST_REGISTERED_DEVICES("Get Registered Devices");
			public String description;
			private PRIMITIVE(String description) {
				this.description = description;
			}
		};
	
	private PRIMITIVE primitive;
	private Traceable device;
	private long id;
	
	private List<RF_CHANNEL> activeChannels;
	
	public MacLayerRequest(){}
	public MacLayerRequest(PRIMITIVE primitive,Traceable device) {
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
	public List<RF_CHANNEL> getActiveChannels() {
		return activeChannels;
	}
	public void setActiveChannels(List<RF_CHANNEL> activeChannels) {
		this.activeChannels = activeChannels;
	}
	
	
}
