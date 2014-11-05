package com.uag.sd.weathermonitor.model.sensor;

public class SensorData {

	private String sensorId;
	private String value;
	
	public SensorData(String sensorId,String value) {
		this.sensorId = sensorId;
		this.value = value;
	}
	
	public String getSensorId() {
		return sensorId;
	}
	public void setSensorId(String sensorId) {
		this.sensorId = sensorId;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
	
}
