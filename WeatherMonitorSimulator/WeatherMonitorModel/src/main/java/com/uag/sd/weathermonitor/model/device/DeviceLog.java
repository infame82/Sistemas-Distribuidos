package com.uag.sd.weathermonitor.model.device;

public interface DeviceLog {
	void info(DeviceData msg);
	
	void debug(DeviceData msg);
}
