package com.uag.sd.weathermonitor.model.device;

import java.io.Serializable;

public class DataMessage implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -286928484182028848L;
	
	
	
	private Beacon beacon;
	private int id;
	private int expiration;
	private DeviceData data;


	public Beacon getBeacon() {
		return beacon;
	}
	public void setBeacon(Beacon beacon) {
		this.beacon = beacon;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getExpiration() {
		return expiration;
	}
	public void setExpiration(int expiration) {
		this.expiration = expiration;
	}
	public DeviceData getData() {
		return data;
	}
	public void setData(DeviceData data) {
		this.data = data;
	}
	
	
	
	
	
}
