package com.uag.sd.weathermonitor.model.layer.network;

import java.io.Serializable;

import com.uag.sd.weathermonitor.model.device.Device;



public class NetworlLayerRequest implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5701409115994218582L;
	
	public enum PRIMITIVE{REQUEST_NETWORK_FORMATION,REQUEST_NETWORK_NODE};
	
	private PRIMITIVE primitive;
	private Device device;
	private long id;
	
	public NetworlLayerRequest(){}
	public NetworlLayerRequest(PRIMITIVE primitive,Device device) {
		this.primitive = primitive;
		this.device = device;
		id= System.currentTimeMillis();
	}
	
	public PRIMITIVE getPrimitive() {
		return primitive;
	}
	public void setPrimitive(PRIMITIVE primitive) {
		this.primitive = primitive;
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
	@Override
	public boolean equals(Object o) {
		if(o==null || o instanceof NetworlLayerRequest) {
			return false;
		}
		
		NetworlLayerRequest auxLayerRequest = (NetworlLayerRequest) o;
		return auxLayerRequest.id == id;
	}

}
