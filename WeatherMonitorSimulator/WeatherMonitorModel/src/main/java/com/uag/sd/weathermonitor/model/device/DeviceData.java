package com.uag.sd.weathermonitor.model.device;

import java.io.Serializable;

public class DeviceData implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2868862113403179898L;
	public enum SENSOR_TYPE{T,H};
	
	private String deviceId;
	private Object data;
	private SENSOR_TYPE type;
	
	
	public DeviceData(String deviceId,Object data) {
		this.deviceId = deviceId;
		this.data = data;
	}
	
	public String getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}

	public SENSOR_TYPE getType() {
		return type;
	}

	public void setType(SENSOR_TYPE type) {
		this.type = type;
	}
	
	
}
