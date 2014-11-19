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
import com.uag.sd.weathermonitor.model.layer.network.NetworkLayerResponse.CONFIRM;
import com.uag.sd.weathermonitor.model.layer.network.NetworlLayerRequest;
import com.uag.sd.weathermonitor.model.layer.network.NetworlLayerRequest.PRIMITIVE;
import com.uag.sd.weathermonitor.model.layer.physical.PhysicalLayerNode;

public abstract class Device implements Serializable,Runnable,Traceable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4733757119551961583L;

	
	protected String id;
	protected int panID;
	protected int coverage;
	protected int operatingChannel;
	protected boolean coordinator;
	protected boolean active;
	protected Point location;
	protected boolean started;
	
	protected transient DeviceLog log;
	protected transient NetworkLayerNode networkLayerNode;
	private transient MacLayerNode macLayerNode;
	private transient PhysicalLayerNode physicalNode;
	
	protected transient NerworkLayerInterfaceClient networkInterfaceClient;
	private transient ThreadPoolExecutor layerPoolExecutor;
	
		
	public Device(String id,DeviceLog log) throws IOException  {
		this.id = id;
		this.log = log;
		panID = -1;
		coordinator = false;
		active = false;
		started = false;
		coverage = 5;
		location = new Point();
		log = new DefaultDeviceLog();
		layerPoolExecutor = (ThreadPoolExecutor) Executors
				.newFixedThreadPool(10);
		
		physicalNode = new PhysicalLayerNode(this,log);
		layerPoolExecutor.execute(physicalNode);	
		
		macLayerNode = new MacLayerNode(this,log);
		layerPoolExecutor.execute(macLayerNode);
		
		networkLayerNode = new NetworkLayerNode(this,log);
		layerPoolExecutor.execute(networkLayerNode);
		
		networkInterfaceClient = new NerworkLayerInterfaceClient(this,log);
		
	}
	
	
	@Override
	public void run() {
		active = true;
		log.debug(new DeviceData(id, "STARTED"));
		try {
			
			
			init();
			while (active) {
				execute();
				Thread.sleep(0);
			}
		} catch (InterruptedException e) {
			if(active) {
				e.printStackTrace();
			}
		}finally {
			log.debug(new DeviceData(id, "STOPPED"));
		}
		
	}
	
	public void stop() {
		log.debug(new DeviceData(id, "STOPPING..."));
		physicalNode.stop();
		macLayerNode.stop();
		networkLayerNode.stop();
		
		active = false;
	}
	
	public boolean establishNetwork() {
		NetworlLayerRequest request = new NetworlLayerRequest(PRIMITIVE.REQUEST_NETWORK_FORMATION,this);
		NetworkLayerResponse response = networkInterfaceClient.requestNetworkFormation(request);
		if(response.getConfirm() == CONFIRM.INVALID_REQUEST) {
			log.debug(new DeviceData(id,response.getMessage()));
			return false;
		}
		setStarted(true);
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

	public int getPanId() {
		return panID;
	}
	
	public void setPanId(int panId) {
		this.panID = panId;
	}


	public boolean isStarted() {
		return started;
	}


	public void setStarted(boolean started) {
		this.started = started;
	}
	

		
}
