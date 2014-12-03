package com.uag.sd.weathermonitor.model.device;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.uag.sd.weathermonitor.model.layer.network.NetworlLayerRequest;
import com.uag.sd.weathermonitor.model.sensor.Sensor;
import com.uag.sd.weathermonitor.model.sensor.SensorMonitor;

@Component("zigBeeDevice")
@Scope("prototype")
public class ZigBeeEndpoint extends Device implements SensorMonitor {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1776039180607508567L;
	private transient List<Sensor> sensors;
	private transient LinkedHashMap<String, ZigBeeRouter> zigBeeRouters;
	
	private transient DeviceLog sensorLog;

	public ZigBeeEndpoint(String id, DeviceLog sensorLog, DeviceLog log) throws IOException  {
		super(id,log);
		endpoint=true;
		allowJoin = false;
		sensors = new ArrayList<Sensor>();
		this.id = id;
		this.sensorLog = sensorLog;
		this.log = log;
	}

	public List<Sensor> getSensors() {
		return sensors;
	}

	public void setSensors(List<Sensor> sensors) {
		this.sensors = sensors;
	}

	@Override
	protected void init() {
		log.debug(new DeviceData(id, "STARTING SENSORS..."));

		int counter = 0;
		for (Sensor sensor : sensors) {
			if (startSensor(sensor)) {
				counter++;
			}
		}
		log.debug(new DeviceData(id, counter + " SENSORS STARTED"));

	}

	public boolean startSensor(Sensor sensor) {

		if (!sensor.isActive()) {
			sensor.setActive(true);
			executorService.execute(sensor);
			return true;
		}
		return false;
	}

	@Override
	public void stop() {
		super.stop();
		for (Sensor sensor : sensors) {
			stopSensor(sensor);
		}
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
		for (Sensor sensor : sensors) {
			if (sensor.getId().equals(id)) {
				return index + 1;
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
	public synchronized void notify(DeviceData data) {
		if (!active) {
			return;
		}
		
		
		NetworlLayerRequest request = new NetworlLayerRequest();
		List<Beacon> parents = new ArrayList<Beacon>();
		if(!neighbors.get(TYPE.COORDINATOR).isEmpty()) {
			parents.addAll(neighbors.get(TYPE.COORDINATOR));
		}else {
			parents.addAll(neighbors.get(TYPE.ROUTER));
		}
		request.setDevice(this);
		DataMessage msg = new DataMessage();
		msg.setData(data);
		msg.setBeacon(this);
		request.setData(msg);
		request.setAssociateBeacons(parents);
		networkInterfaceClient.transmitData(request);
	}

	public boolean equals(Object o) {
		if (o == null || !(o instanceof ZigBeeEndpoint)) {
			return false;
		}
		ZigBeeEndpoint zigBeeEndpoint = (ZigBeeEndpoint) o;
		return zigBeeEndpoint.id.equals(id);
	}

	public LinkedHashMap<String, ZigBeeRouter> getRouters() {
		return zigBeeRouters;
	}

	public boolean addRouter(ZigBeeRouter zigBeeRouter) {
		if (!zigBeeRouters.containsKey(zigBeeRouter.getId())) {
			zigBeeRouters.put(zigBeeRouter.getId(), zigBeeRouter);
		}
		return false;
	}

	public void removeRouter(String id) {
		zigBeeRouters.remove(id);
	}

	public DeviceLog getSensorLog() {
		return sensorLog;
	}

	public void setSensorLog(DeviceLog sensorLog) {
		this.sensorLog = sensorLog;
	}

	@Override
	protected void execute(DataMessage msg) {
	}

}
