package com.uag.sd.weathermonitor.model.endpoint;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.uag.sd.weathermonitor.model.logs.SensorLog;
import com.uag.sd.weathermonitor.model.sensor.Sensor;
import com.uag.sd.weathermonitor.model.sensor.SensorData;
import com.uag.sd.weathermonitor.model.sensor.SensorMonitor;
import com.uag.sd.weathermonitor.model.traceability.Traceable;

@Component("endpoint")
@Scope("prototype")
public class Endpoint implements SensorMonitor,Runnable,Traceable {
	private String id;
	private List<Sensor> sensors;
	private boolean active;
	private ThreadPoolExecutor executorService;
	private Integer threadPoolSize;
	private int coverage;
	private Point location;
	private SensorLog sensorLog;
	
	public Endpoint() {
		threadPoolSize = 50;
		executorService = (ThreadPoolExecutor) Executors.newFixedThreadPool(threadPoolSize);
		sensors = new ArrayList<Sensor>();
		active = false;
		coverage = 5;
		location = new Point();
	}
	
	public Endpoint(String id,SensorLog sensorLog) {
		this();
		this.id = id;
		this.sensorLog = sensorLog;
	}
	
	@Override
	public void run() {
		active = true;
		start();
		while(active) {
			try {
				Thread.sleep(0);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<Sensor> getSensors() {
		return sensors;
	}

	public void setSensors(List<Sensor> sensors) {
		this.sensors = sensors;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	private void start() {
		for(Sensor sensor:sensors) {
			startSensor(sensor);
		}
	}
	
	public void startSensor(Sensor sensor) {
		if(!sensor.isActive()) {
			sensor.setActive(true);
			executorService.execute(sensor);
		}
	}
	
	public void stop() {
		for(Sensor sensor:sensors) {
			stopSensor(sensor);
		}
		active = false;
	}
	
	public void stopSensor(Sensor sensor) {
		sensor.stop();
		executorService.remove(sensor);
	}
	
	public void stopSensor(int index) {
		Sensor sensor = sensors.get(index);
		sensor.setActive(false);
		executorService.remove(sensor);
	}
	
	public void addSensor(Sensor sensor) {
		sensor.setParent(this);
		sensor.setMonitor(this);
		sensor.setLog(sensorLog);
		sensors.add(sensor);
	}
	
	public int getSensorIndex(String id) {
		int index = -1;
		for(Sensor sensor:sensors) {
			if(sensor.getId().equals(id)) {
				return index+1;
			}
			index++;
		}
		return index;
	}
	
	public void removeSensor(String sensorId) {
		Sensor sensor = sensors.remove(getSensorIndex(sensorId));
		stopSensor(sensor);
	}
	public void removeSensor(int index) {
		Sensor sensor = sensors.remove(index);
		stopSensor(sensor);
	}

	@Override
	public synchronized void nofity(SensorData data) {
		if(!active) {
			return;
		}
		if(sensorLog!=null) {
			sensorLog.info(data);
		}
	}

	@Override
	public int getCoverage() {
		return coverage;
	}

	@Override
	public Point getLocation() {
		return location;
	}

	public void setCoverage(int coverage) {
		this.coverage = coverage;
	}

	public void setLocation(int x, int y) {
		this.location = new Point(x,y);
	}

	public boolean equals(Object o) {
		if(o==null || !(o instanceof Endpoint)) {
			return false;
		}
		Endpoint endpoint = (Endpoint)o;
		return endpoint.id.equals(id);
	}

	public SensorLog getSensorLog() {
		return sensorLog;
	}

	public void setSensorLog(SensorLog sensorLog) {
		this.sensorLog = sensorLog;
	}
	
	

}
