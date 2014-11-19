package com.uag.sd.weathermonitor.model.endpoint;

import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.uag.sd.weathermonitor.model.device.DefaultDeviceLog;
import com.uag.sd.weathermonitor.model.device.Device;
import com.uag.sd.weathermonitor.model.device.DeviceData;
import com.uag.sd.weathermonitor.model.device.DeviceLog;
import com.uag.sd.weathermonitor.model.router.ZigBeeRouter;
import com.uag.sd.weathermonitor.model.sensor.Sensor;
import com.uag.sd.weathermonitor.model.sensor.SensorMonitor;

@Component("zigBeeDevice")
@Scope("prototype")
public class ZigBeeDevice extends Device implements SensorMonitor {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1776039180607508567L;
	private transient List<Sensor> sensors;
	private transient LinkedHashMap<String, ZigBeeRouter> zigBeeRouters;
	private transient ThreadPoolExecutor executorService;
	private transient Integer threadPoolSize;
	private transient DeviceLog sensorLog;

	public ZigBeeDevice(String id, DeviceLog sensorLog, DeviceLog log) throws IOException  {
		super(id,log);
		executorService = (ThreadPoolExecutor) Executors.newFixedThreadPool(50);
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
		if (sensorLog != null) {
			sensorLog.info(data);
		}
	}

	public boolean equals(Object o) {
		if (o == null || !(o instanceof ZigBeeDevice)) {
			return false;
		}
		ZigBeeDevice zigBeeDevice = (ZigBeeDevice) o;
		return zigBeeDevice.id.equals(id);
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
	protected void execute() {}

}
