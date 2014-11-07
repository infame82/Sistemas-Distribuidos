package com.uag.sd.weathermonitor.model.logs;

public interface DeviceLog<M> {

	void info(M msg);
	
	void debug(M msg);
}
