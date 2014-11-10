package com.uag.sd.weathermonitor.model.device;

public class DeviceData {

	private String deviceId;
	private Object data;
	
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
	
	
}
