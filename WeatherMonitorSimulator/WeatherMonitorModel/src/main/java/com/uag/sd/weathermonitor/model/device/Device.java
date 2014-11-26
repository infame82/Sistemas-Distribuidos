package com.uag.sd.weathermonitor.model.device;

import java.awt.Point;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
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
import com.uag.sd.weathermonitor.model.layer.physical.channel.RFChannel;
import com.uag.sd.weathermonitor.model.layer.physical.channel.RFChannel.RF_CHANNEL;

public abstract class Device implements Serializable,Runnable,Beacon{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4733757119551961583L;

	
	protected String id;
	protected int panID;
	protected long extendedPanID;
	
	protected int potency;
	protected int operatingChannel;
	protected boolean coordinator;
	protected boolean router;
	protected boolean endpoint;
	protected boolean active;
	protected boolean allowJoin;
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
		potency = 5;
		location = new Point();
		//log = new DefaultDeviceLog();
		layerPoolExecutor = (ThreadPoolExecutor) Executors
				.newFixedThreadPool(10);
		
		physicalNode = new PhysicalLayerNode(this,log);
		layerPoolExecutor.execute(physicalNode);
		physicalNode.init();
		
		macLayerNode = new MacLayerNode(this,log);
		layerPoolExecutor.execute(macLayerNode);
		macLayerNode.init();
		
		networkLayerNode = new NetworkLayerNode(this,log);
		layerPoolExecutor.execute(networkLayerNode);
		networkLayerNode.init();
		
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
	
	public boolean networkFormation() {
		NetworlLayerRequest request = new NetworlLayerRequest(PRIMITIVE.REQUEST_NETWORK_FORMATION,this);
		NetworkLayerResponse response = networkInterfaceClient.requestNetworkFormation(request);
		if(response.getConfirm() == CONFIRM.INVALID_REQUEST) {
			log.debug(new DeviceData(id,response.getMessage()));
			return false;
		}
		setStarted(true);
		return true;
	}
	
	public Map<RFChannel, List<Beacon>> networkDiscovery() {
		NetworlLayerRequest request = new NetworlLayerRequest(PRIMITIVE.NETWORK_DISCOVERY,this);
		NetworkLayerResponse response = networkInterfaceClient.networkDiscovery(request);
		if(response.getConfirm() == CONFIRM.INVALID_REQUEST) {
			log.debug(new DeviceData(id,response.getMessage()));
			return null;
		}
		return response.getAvailableNetworks();
	}
	
	public boolean networkJoin(RFChannel channel,Beacon beacon) {
		NetworlLayerRequest request = new NetworlLayerRequest(PRIMITIVE.NETWORK_JOIN,this);
		request.setJoinBeacon(beacon);
		request.setChannel(channel);
		NetworkLayerResponse response = networkInterfaceClient.netoworkJoin(request);
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
	
	

	public int getPotency() {
		return potency;
	}


	public void setPotency(int potency) {
		this.potency = potency;
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
	
	public long getExtendedPanID() {
		return extendedPanID;
	}
	
	public void setExtendedPanID(long extendedPanID) {
		this.extendedPanID = extendedPanID;
	}
	

	public boolean isRouter() {
		return router;
	}
	public boolean isEndpoint() {
		return endpoint;
	}


	public void setRouter(boolean router) {
		this.router = router;
	}


	public void setEndpoint(boolean endpoint) {
		this.endpoint = endpoint;
	}


	public boolean isAllowJoin() {
		return allowJoin;
	}


	public void setAllowJoin(boolean allowJoin) {
		this.allowJoin = allowJoin;
	}
	
	
	
}
