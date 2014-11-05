package com.uag.sd.weathermonitor.model.sensor;

import bsh.This;

import com.uag.sd.weathermonitor.model.endpoint.Endpoint;
import com.uag.sd.weathermonitor.model.logs.SensorLog;

public abstract class Sensor implements Runnable{

	protected String id;
	protected long lapse;
	protected boolean active;
	protected SensorMonitor monitor;
	protected String value;
	protected Endpoint parent;
	protected SensorLog log;
	
	//In milliseconds, 5000 = 5 sec
	public static final long DEFAULT_LAPSE = 5000;
	
	public Sensor() {
		active = false;
		lapse = DEFAULT_LAPSE;
	}
	
	public Sensor(String id) {
		this();
		this.id = id;
	}
	
	public void setParent(Endpoint parent) {
		this.parent = parent;
	}
	
	public Endpoint getParent() {
		return parent;
	}
	
	@Override
	public void run() {
		active = true;
		if(log!=null) {
			log.debug(id, "STARTED");
		}
		while (active) {
			if(monitor!=null) {
				monitor.nofity(new SensorData(id, detect()));
			}
			try {
				Thread.sleep(lapse);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		if(log!=null) {
			log.debug(id,"STOPPED");
		}
	}
	
	public void stop() {
		if(log!=null) {
			log.debug(id, "STOPPING...");
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
			log.debug(id,"Lapse updated: "+lapse);
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
			log.debug(id,"Value updated: "+value);
		}
		this.value = value;
	}

	public SensorLog getLog() {
		return log;
	}

	public void setLog(SensorLog log) {
		this.log = log;
	}
	
	
}
