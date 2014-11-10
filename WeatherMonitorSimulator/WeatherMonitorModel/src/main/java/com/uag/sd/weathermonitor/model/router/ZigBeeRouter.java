package com.uag.sd.weathermonitor.model.router;

import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.uag.sd.weathermonitor.model.device.Device;
import com.uag.sd.weathermonitor.model.device.DeviceLog;
import com.uag.sd.weathermonitor.model.endpoint.ZigBeeDevice;

@Component("zigBeeRouter")
@Scope("prototype")
public class ZigBeeRouter extends Device {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6061007998656566549L;
	protected transient Map<String, Device> zigBeeDevices;
	protected transient Map<String, Device> zigBeeRouters;

	public ZigBeeRouter() throws SocketException, UnknownHostException {
		super();
		zigBeeDevices = new LinkedHashMap<String, Device>();
		zigBeeRouters = new LinkedHashMap<String, Device>();
	}
	
	public ZigBeeRouter(String id) throws SocketException, UnknownHostException  {
		this();
		this.id = id;
	}


	public ZigBeeRouter(String id, DeviceLog log) throws SocketException, UnknownHostException  {
		this(id);
		this.log = log;
	}

	public Map<String, Device> getEndpoints() {
		return zigBeeDevices;
	}

	public void setEndpoints(Map<String, Device> zigBeeDevices) {
		this.zigBeeDevices = zigBeeDevices;
	}

	public Map<String, Device> getRouters() {
		return zigBeeRouters;
	}

	public void setRouters(Map<String, Device> zigBeeRouters) {
		this.zigBeeRouters = zigBeeRouters;
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

	public boolean addEndpoint(ZigBeeDevice zigBeeDevice) {
		if (!zigBeeDevices.containsKey(zigBeeDevice.getId())) {
			zigBeeDevices.put(zigBeeDevice.getId(), zigBeeDevice);
		}
		return false;
	}

	public void removeEndpoint(String id) {
		zigBeeDevices.remove(id);
	}

	@Override
	protected void init() {}

	@Override
	protected void execute() {}
}
