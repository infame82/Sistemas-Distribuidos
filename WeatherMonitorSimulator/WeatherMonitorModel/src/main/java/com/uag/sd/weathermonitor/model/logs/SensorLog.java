package com.uag.sd.weathermonitor.model.logs;

import com.uag.sd.weathermonitor.model.sensor.SensorData;

public interface SensorLog {

	void info(SensorData data);
	
	void debug(String sensorId,String msg);
}
