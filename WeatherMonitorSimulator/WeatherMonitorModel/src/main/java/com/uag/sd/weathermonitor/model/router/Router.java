package com.uag.sd.weathermonitor.model.router;

import java.awt.Point;
import java.util.LinkedHashMap;
import java.util.Map;

import com.uag.sd.weathermonitor.model.endpoint.Endpoint;
import com.uag.sd.weathermonitor.model.logs.DeviceLog;
import com.uag.sd.weathermonitor.model.traceability.Traceable;

public class Router implements Runnable,Traceable{

	private String id;
	private Map<String,Endpoint> endpoints;
	private Map<String, Router> routers;
	private int coverage;
	private Point location;
	private DeviceLog<RouterData> routerLog;
	private boolean active;

	public Router() {
		endpoints = new LinkedHashMap<String, Endpoint>();
		routers = new LinkedHashMap<String, Router>();
		active = false;
		coverage = 5;
		location = new Point();
	}
	
	public Router(String id,DeviceLog<RouterData> routerLog) {
		this();
		this.id = id;
		this.routerLog = routerLog;
	}
	
	@Override
	public void run() {
		active = true;
		if(routerLog!=null) {
			routerLog.debug(new RouterData(id, "STARTED"));
		}
		while(active) {
			try {
				Thread.sleep(0);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		if(routerLog!=null) {
			routerLog.debug(new RouterData(id, "STOPPED"));
		}
	}
	
	public void stop() {
		if(routerLog!=null) {
			routerLog.debug(new RouterData(id, "STOPPING ROUTER..."));
		}
		active = false;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Map<String, Endpoint> getEndpoints() {
		return endpoints;
	}
	public void setEndpoints(Map<String, Endpoint> endpoints) {
		this.endpoints = endpoints;
	}
	public Map<String, Router> getRouters() {
		return routers;
	}
	public void setRouters(Map<String, Router> routers) {
		this.routers = routers;
	}
	public int getCoverage() {
		return coverage;
	}
	public void setCoverage(int coverage) {
		this.coverage = coverage;
	}
	public Point getLocation() {
		return location;
	}
	public void setLocation(Point location) {
		this.location = location;
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
	
	
	public boolean addEndpoint(Endpoint endpoint) {
		if(!endpoints.containsKey(endpoint.getId())) {
			endpoints.put(endpoint.getId(), endpoint);
		}
		return false;
	}
	
	public void removeEndpoint(String id) {
		endpoints.remove(id);
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}
	
	
	
}
