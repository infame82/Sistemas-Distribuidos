package com.uag.sd.weathermonitor.model.sensor;

import com.uag.sd.weathermonitor.model.device.DeviceData;

public interface SensorMonitor {

	void notify(DeviceData data);
}
