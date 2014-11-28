package com.uag.sd.weathermonitor.model.device;

import java.io.Serializable;

import com.uag.sd.weathermonitor.model.device.DeviceData.SENSOR_TYPE;

public class DataMessage implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -286928484182028848L;
	
	
	
	private Beacon beacon;
	private int id;
	private int expiration;
	private String data;
	private SENSOR_TYPE type;

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
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public SENSOR_TYPE getType() {
		return type;
	}
	public void setType(SENSOR_TYPE type) {
		this.type = type;
	}
	
	
	
	
}
