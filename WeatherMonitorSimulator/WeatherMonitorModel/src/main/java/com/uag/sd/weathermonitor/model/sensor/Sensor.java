package com.uag.sd.weathermonitor.model.sensor;

import com.uag.sd.weathermonitor.model.device.DeviceData;
import com.uag.sd.weathermonitor.model.device.DeviceLog;
import com.uag.sd.weathermonitor.model.device.ZigBeeEndpoint;
import com.uag.sd.weathermonitor.model.device.DeviceData.SENSOR_TYPE;

public abstract class Sensor implements Runnable{

	protected String id;
	protected long lapse;
	protected boolean active;
	protected SensorMonitor monitor;
	protected String value;
	protected ZigBeeEndpoint parent;
	protected DeviceLog log;
	
	//In milliseconds, 5000 = 5 sec
	public static final long DEFAULT_LAPSE = 30000;
	
	public Sensor() {
		active = false;
		lapse = DEFAULT_LAPSE;
	}
	
	public Sensor(String id) {
		this();
		this.id = id;
	}
	
	public void setParent(ZigBeeEndpoint parent) {
		this.parent = parent;
	}
	
	public ZigBeeEndpoint getParent() {
		return parent;
	}
	
	@Override
	public void run() {
		active = true;
		if(log!=null) {
			log.debug(new DeviceData(id, "STARTED"));
		}
		while (active) {
			if(monitor!=null) {
				DeviceData data = new DeviceData(id, detect());
				data.setType(getType());
				monitor.notify(data);
				log.info(data);
			}
			try {
				Thread.sleep(lapse);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		if(log!=null) {
			log.debug(new DeviceData(id, "STOPPED"));
		}
	}
	
	public void stop() {
		if(log!=null) {
			log.debug(new DeviceData(id, "STOPPING..."));
		}
		active = false;
	}
	
	public abstract String detect();
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

	public long getLapse() {
		return lapse;
	}
	public void setLapse(long lapse) {
		if(log!=null && lapse!=this.lapse) {
			log.debug(new DeviceData(id, "Lapse updated: "+lapse));
		}
		this.lapse = lapse;
	}
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
	public SensorMonitor getMonitor() {
		return monitor;
	}
	public void setMonitor(SensorMonitor monitor) {
		this.monitor = monitor;
	}
	
	public String getValue() {
		return value;
	}
	
	public void setValue(String value) {
		if(log!=null && !value.equals(this.value)) {
			log.debug(new DeviceData(id, "Value updated: "+value));
		}
		this.value = value;
	}

	public DeviceLog getLog() {
		return log;
	}

	public void setLog(DeviceLog log) {
		this.log = log;
	}
	
	public abstract SENSOR_TYPE getType();
	
}
