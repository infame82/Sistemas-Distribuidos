package com.uag.sd.weathermonitor.model.layer.mac;

import java.io.Serializable;

import com.uag.sd.weathermonitor.model.device.Device;

public class MacLayerRequest implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8704870493936367528L;
	
	public enum PRIMITIVE{REQUEST_MAC_NODE("MAC Node"),ENERGY_DETECTION_SCAN("Energy Detection Scan"),
		ACTIVE_SCAN("Active Scan"),SET_PAN_ID("Set PAN ID"),START("Start"),INVALID_REQUEST("Invalid Request");
			public String description;
			private PRIMITIVE(String description) {
				this.description = description;
			}
		};
	
	private PRIMITIVE primitive;
	private Device device;
	private long id;
	
	public MacLayerRequest(){}
	public MacLayerRequest(PRIMITIVE primitive,Device device) {
		this.primitive = primitive;
		this.device = device;
		id= System.currentTimeMillis();
	}
	
	public Device getDevice() {
		return device;
	}
	public void setDevice(Device device) {
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

}
