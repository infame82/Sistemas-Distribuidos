package com.uag.sd.weathermonitor.model.endpoint;

import java.awt.Point;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.uag.sd.weathermonitor.model.logs.DeviceLog;
import com.uag.sd.weathermonitor.model.router.Router;
import com.uag.sd.weathermonitor.model.sensor.Sensor;
import com.uag.sd.weathermonitor.model.sensor.SensorData;
import com.uag.sd.weathermonitor.model.sensor.SensorMonitor;
import com.uag.sd.weathermonitor.model.traceability.Traceable;

@Component("endpoint")
@Scope("prototype")
public class Endpoint implements SensorMonitor,Runnable,Traceable {
	private String id;
	private List<Sensor> sensors;
	private Map<String,Router> routers;
	private boolean active;
	private ThreadPoolExecutor executorService;
	private Integer threadPoolSize;
	private int coverage;
	private Point location;
	private DeviceLog<SensorData> sensorLog;
	private DeviceLog<EndpointData> endpointLog;
	
	public Endpoint() {
		threadPoolSize = 50;
		executorService = (ThreadPoolExecutor) Executors.newFixedThreadPool(threadPoolSize);
		sensors = new ArrayList<Sensor>();
		routers = new LinkedHashMap<String, Router>();
		active = false;
		coverage = 5;
		location = new Point();
	}
	
	public Endpoint(String id,DeviceLog<SensorData> sensorLog,DeviceLog<EndpointData> endpointLog) {
		this();
		this.id = id;
		this.sensorLog = sensorLog;
		this.endpointLog = endpointLog;
	}
	
	@Override
	public void run() {
		active = true;
		if(endpointLog!=null) {
			endpointLog.debug(new EndpointData(id, "STARTED"));
		}
		start();
		while(active) {
			try {
				Thread.sleep(0);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		if(endpointLog!=null) {
			endpointLog.debug(new EndpointData(id, "STOPPED"));
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
		if(endpointLog!=null) {
			endpointLog.debug(new EndpointData(id, "STARTING SENSORS..."));
		}
		int counter = 0;
		for(Sensor sensor:sensors) {
			if(startSensor(sensor)) {
				counter++;
			}
		}
		if(endpointLog!=null) {
			endpointLog.debug(new EndpointData(id, counter+ " SENSORS STARTED"));
		}
	}
	
	public boolean startSensor(Sensor sensor) {
		
		if(!sensor.isActive()) {
			sensor.setActive(true);
			executorService.execute(sensor);
			return true;
		}
		return false;
	}
	
	public void stop() {
		if(endpointLog!=null) {
			endpointLog.debug(new EndpointData(id, "STOPPING ENDPOINT..."));
		}
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

	public DeviceLog<SensorData> getSensorLog() {
		return sensorLog;
	}

	public void setSensorLog(DeviceLog<SensorData> sensorLog) {
		this.sensorLog = sensorLog;
	}

	public DeviceLog<EndpointData> getEndpointLog() {
		return endpointLog;
	}

	public void setEndpointLog(DeviceLog<EndpointData> endpointLog) {
		this.endpointLog = endpointLog;
	}

	public Map<String, Router> getRouters() {
		return routers;
	}
	
	public boolean addRouter(Router router) {
		if(!routers.containsKey(router.getId())) {
			routers.put(router.getId(), router);
		}
		return false;
	}
	
	public void removeRouter(String id) {
		routers.remove(id);
	}

}
