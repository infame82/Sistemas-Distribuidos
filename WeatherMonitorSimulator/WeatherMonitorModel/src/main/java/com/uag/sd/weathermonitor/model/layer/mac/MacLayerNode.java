package com.uag.sd.weathermonitor.model.layer.mac;

import com.uag.sd.weathermonitor.model.device.DeviceLog;
import com.uag.sd.weathermonitor.model.device.Traceable;


public class MacLayerNode implements Runnable,MacLayerInterface {

	private DeviceLog log;
	private Traceable traceableDevice;
	private boolean active;
	
	public MacLayerNode(Traceable traceableDevice, DeviceLog log) {
		this.traceableDevice = traceableDevice;
		this.log = log;
		active = false;
	}
	
	@Override
	public void run() {
		active = true;
		while(active) {
			try {
				Thread.sleep(0);
			} catch (InterruptedException e) {
			}
		}
	}
	
	public void stop() {
		active = false;
	}

	public DeviceLog getLog() {
		return log;
	}

	public void setLog(DeviceLog log) {
		this.log = log;
	}

	public Traceable getTraceableDevice() {
		return traceableDevice;
	}

	public void setTraceableDevice(Traceable traceableDevice) {
		this.traceableDevice = traceableDevice;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}
	
	

}
