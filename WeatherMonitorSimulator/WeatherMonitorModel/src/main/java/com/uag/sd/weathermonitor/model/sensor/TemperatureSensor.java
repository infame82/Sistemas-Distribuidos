package com.uag.sd.weathermonitor.model.sensor;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.uag.sd.weathermonitor.model.device.DeviceData.SENSOR_TYPE;

@Component("temperatureSensor")
@Scope("prototype")
public class TemperatureSensor extends Sensor{

	public enum SCALE{CELSIUS,FARENHEIT};
	private SCALE scale;
	
	public TemperatureSensor() {
		value = "27";
		scale = SCALE.CELSIUS;
	}
	public TemperatureSensor(String id) {
		super(id);
		value = "27";
		scale = SCALE.CELSIUS;
	}
	public TemperatureSensor(String id,String temperature) {
		super(id);
		value = temperature;
		scale = SCALE.CELSIUS;
	}
	
	public TemperatureSensor(SCALE scale) {
		this();
		this.scale = scale;
	}

	@Override
	public String detect() {
		return value;
	}
	@Override
	public SENSOR_TYPE getType() {
		return SENSOR_TYPE.T;
	}

}
