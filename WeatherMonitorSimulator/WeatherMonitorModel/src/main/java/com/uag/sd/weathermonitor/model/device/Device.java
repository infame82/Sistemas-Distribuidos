package com.uag.sd.weathermonitor.model.device;

import java.awt.Point;
import java.io.IOException;
import java.io.Serializable;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import com.uag.sd.weathermonitor.model.layer.mac.MacLayerNode;
import com.uag.sd.weathermonitor.model.layer.network.NerworkLayerInterfaceClient;
import com.uag.sd.weathermonitor.model.layer.network.NetworkLayerNode;
import com.uag.sd.weathermonitor.model.layer.network.NetworkLayerResponse;
import com.uag.sd.weathermonitor.model.layer.network.NetworlLayerRequest;
import com.uag.sd.weathermonitor.model.layer.network.NetworkLayerResponse.CONFIRM;
import com.uag.sd.weathermonitor.model.layer.network.NetworlLayerRequest.PRIMITIVE;

public abstract class Device implements Serializable,Runnable,Traceable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4733757119551961583L;

	
	protected String id;
	protected int coverage;
	protected int operatingChannel;
	protected boolean coordinator;
	protected boolean active;
	protected Point location;
	
	protected transient DeviceLog log;
	protected transient NetworkLayerNode networkLayerNode;
	protected transient MacLayerNode macLayerNode;
	protected transient NerworkLayerInterfaceClient networkInterfaceClient;
	private transient ThreadPoolExecutor layerPoolExecutor;
	
	
	public Device() throws SocketException, UnknownHostException {
		coordinator = false;
		active = false;
		coverage = 5;
		location = new Point();
		log = new DefaultDeviceLog();
		layerPoolExecutor = (ThreadPoolExecutor) Executors
				.newFixedThreadPool(10);
		networkInterfaceClient = new NerworkLayerInterfaceClient(this,log);
	}
	
	public Device(String id) throws SocketException, UnknownHostException {
		this();
		this.id = id;
	}
	
	public Device(String id,DeviceLog log) throws SocketException, UnknownHostException  {
		this(id);
		this.log = log;
		networkInterfaceClient.setLog(log);
	}
	
	
	@Override
	public void run() {
		active = true;
		log.debug(new DeviceData(id, "STARTED"));
		try {
			networkLayerNode = new NetworkLayerNode(this,log);
			macLayerNode = new MacLayerNode(this,log);
			layerPoolExecutor.execute(networkLayerNode);
			layerPoolExecutor.execute(macLayerNode);
			init();
			while (active) {
				execute();
				Thread.sleep(0);
			}
		} catch (InterruptedException | IOException e) {
			if(active) {
				e.printStackTrace();
			}
		}finally {
			log.debug(new DeviceData(id, "STOPPED"));
		}
		
	}
	
	public void stop() {
		log.debug(new DeviceData(id, "STOPPING..."));
		networkLayerNode.stop();
		macLayerNode.stop();
		active = false;
	}
	
	public boolean establishNetwork() {
		NetworlLayerRequest request = new NetworlLayerRequest(PRIMITIVE.REQUEST_NETWORK_FORMATION,this);
		NetworkLayerResponse response = networkInterfaceClient.requestNetworkFormation(request);
		if(response.getConfirm() == CONFIRM.INVALID_REQUEST) {
			log.debug(new DeviceData(id,response.getMessage()));
			return false;
		}
		return true;
	}
	
	protected abstract void init();
	
	protected abstract void execute();
	
	
	public int getOperatingChannel() {
		return operatingChannel;
	}
	public void setOperatingChannel(int operatingChannel) {
		this.operatingChannel = operatingChannel;
	}
	public boolean isCoordinator() {
		return coordinator;
	}
	public void setCoordinator(boolean coordinator) {
		this.coordinator = coordinator;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
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

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public void setLocation(Point location) {
		this.location = location;
	}
	
	public void setLocation(int x, int y) {
		this.location = new Point(x, y);
	}

	public DeviceLog getLog() {
		return log;
	}

	public void setLog(DeviceLog log) {
		this.log = log;
	}

	
	

		
}
