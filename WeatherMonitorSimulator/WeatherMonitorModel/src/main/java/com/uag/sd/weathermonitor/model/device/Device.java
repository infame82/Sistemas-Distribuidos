package com.uag.sd.weathermonitor.model.device;

import java.awt.Point;
import java.io.IOException;
import java.io.Serializable;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
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
import com.uag.sd.weathermonitor.model.utils.ObjectSerializer;

public abstract class Device implements Serializable,Runnable,Beacon{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4733757119551961583L;
	public enum TYPE{COORDINATOR,ROUTER,ENDPOINT};

	
	protected String id;
	protected int panID;
	protected int extendedPanID;
	
	protected int potency;
	protected int operatingChannel;
	protected boolean coordinator;
	protected boolean router;
	protected boolean endpoint;
	protected boolean active;
	protected boolean allowJoin;
	protected Point location;
	protected boolean started;
	protected Map<TYPE,List<Beacon>> neighbors;
	
	
	protected transient DeviceLog log;
	protected transient NetworkLayerNode networkLayerNode;
	private transient MacLayerNode macLayerNode;
	private transient PhysicalLayerNode physicalNode;
	
	protected transient NerworkLayerInterfaceClient networkInterfaceClient;
	private transient ThreadPoolExecutor layerPoolExecutor;
	
	protected transient ThreadPoolExecutor executorService;
	protected List<String> msgHistory;

	
	private transient DatagramSocket listener;
	public static final int BUFFER_SIZE = 2048;
	//public static final int DATA_BUFFER_SIZE = 2048;
	
	public class RequestResolver implements Runnable{
		private final byte[] requestContent;	
		public RequestResolver(byte[] requestContent) {
			this.requestContent = requestContent;
		}
		@Override
		public void run() {
			try {
				Object obj = ObjectSerializer.unserialize(requestContent);
				if (obj instanceof DeviceLayerRequest) {
					DeviceLayerResponse response = new DeviceLayerResponse();
					response.setConfirm(DeviceLayerResponse.CONFIRM.SUCCESS);
					if(obj instanceof DeviceLayerRequest) {
						DeviceLayerRequest request = (DeviceLayerRequest) obj;
						if(request.getPrimitive()==DeviceLayerRequest.PRIMITIVE.ADD_NEIGHBORD) {
							Beacon beacon = request.getNeighbord();
							log.debug(new DeviceData(Device.this.id,"Registering neighbord "+beacon.getId()+", "+beacon.getIP()+":"+beacon.getPort()));
							if(beacon.isCoordinator() && !alreadyRegistered(beacon, neighbors.get(TYPE.COORDINATOR))) {
								neighbors.get(TYPE.COORDINATOR).add(beacon);
							}else if(beacon.isRouter() && !alreadyRegistered(beacon, neighbors.get(TYPE.ROUTER))) {
								neighbors.get(TYPE.ROUTER).add(beacon);
							}else if(beacon.isEndpoint() && !alreadyRegistered(beacon, neighbors.get(TYPE.ENDPOINT))) {
								neighbors.get(TYPE.ENDPOINT).add(beacon);
							}else {
								response.setConfirm(DeviceLayerResponse.CONFIRM.INVALID_REQUEST);
								response.setMsg("Neighbord already exists");
								log.debug(new DeviceData(Device.this.id,"Neighbord already exists "+beacon.getId()+", "+beacon.getIP()+":"+beacon.getPort()));
							}
						}
					}
				}else if(obj instanceof DataMessage){
					DataMessage msg = (DataMessage)obj;
					String keyString = msg.getBeacon().getId()+":"+msg.getId();
					if(!msgHistory.contains(keyString)) {
						execute(msg);
						msgHistory.add(keyString);
					}
					
				}
			} catch (ClassNotFoundException | IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private boolean alreadyRegistered(Beacon beacon,List<Beacon> beacons) {
		for(Beacon registeredNeighbord:neighbors.get(TYPE.COORDINATOR)) {
			if(registeredNeighbord.getIP().equals(beacon.getIP()) &&
					registeredNeighbord.getPort() == beacon.getPort()) {
				return true;
			}
		}
		return false;
	}
	
		
	public Device(String id,DeviceLog log) throws IOException  {
		this.id = id;
		this.log = log;
		panID = -1;
		coordinator = false;
		active = false;
		started = false;
		potency = 5;
		location = new Point();
		msgHistory = new ArrayList<String>();
		neighbors = new HashMap<Device.TYPE, List<Beacon>>();
		neighbors.put(TYPE.COORDINATOR, new ArrayList<Beacon>());
		neighbors.put(TYPE.ROUTER, new ArrayList<Beacon>());
		neighbors.put(TYPE.ENDPOINT, new ArrayList<Beacon>());
		layerPoolExecutor = (ThreadPoolExecutor) Executors
				.newFixedThreadPool(3);
		executorService = (ThreadPoolExecutor) Executors.newFixedThreadPool(50);
		
		physicalNode = new PhysicalLayerNode(this,log);
		macLayerNode = new MacLayerNode(this,log);
		networkLayerNode = new NetworkLayerNode(this,log);
		
		
		
		networkInterfaceClient = new NerworkLayerInterfaceClient(this,log);
		ipAddress =   InetAddress.getLocalHost().getHostAddress();
		listener = new DatagramSocket();
		listenerPort = listener.getLocalPort();
		
		
		
		
		
	}
	
	protected int listenerPort;
	protected String ipAddress;

	@Override
	public void run() {
		physicalNode.init();
		macLayerNode.init();
		networkLayerNode.init();
		
		layerPoolExecutor.execute(physicalNode);
		layerPoolExecutor.execute(macLayerNode);
		layerPoolExecutor.execute(networkLayerNode);
		
		active = true;
		log.debug(new DeviceData(id, "STARTED"));
		DatagramPacket request = null;
		try {
			log.info(new DeviceData(this.id, "Device Listening on: "+ipAddress+":"+listenerPort));
			init();
			while (active) {
				request = new DatagramPacket(new byte[BUFFER_SIZE], BUFFER_SIZE);
				listener.receive(request);
				executorService.submit(new RequestResolver(request.getData()));
			}
		} catch (IOException e) {
			if(active) {
				if(listener!=null) {
					listener.close();
				}
				e.printStackTrace();
			}
		}finally {
			log.debug(new DeviceData(id, "STOPPED"));
			if(listener!=null) {
				listener.close();
			}
		}
		
	}
	
	public void stop() {
		active = false;
		log.debug(new DeviceData(id, "STOPPING..."));
		if(listener!=null) {
			listener.close();
		}
		physicalNode.stop();
		macLayerNode.stop();
		networkLayerNode.stop();
		
		
	}
	
	public boolean networkFormation() {
		NetworlLayerRequest request = new NetworlLayerRequest(PRIMITIVE.REQUEST_NETWORK_FORMATION,this);
		NetworkLayerResponse response = networkInterfaceClient.requestNetworkFormation(request);
		if(response.getConfirm() == CONFIRM.INVALID_REQUEST) {
			log.debug(new DeviceData(id,response.getMessage()));
			return false;
		}
		panID = response.getBeacon().getPanId();
		extendedPanID = response.getBeacon().getExtendedPanID();
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
		NetworkLayerResponse response = networkInterfaceClient.networkJoin(request);
		if(response.getConfirm() == CONFIRM.INVALID_REQUEST) {
			log.debug(new DeviceData(id,response.getMessage()));
			return false;
		}
		for(Beacon neighbor:response.getNeighbords()) {
			if(neighbor.isCoordinator() && !alreadyRegistered(neighbor, neighbors.get(TYPE.COORDINATOR))) {
				neighbors.get(TYPE.COORDINATOR).add(neighbor);
			}else if(neighbor.isRouter() && !alreadyRegistered(neighbor, neighbors.get(TYPE.ROUTER))) {
				neighbors.get(TYPE.ROUTER).add(neighbor);
			}else if(neighbor.isEndpoint() && !alreadyRegistered(neighbor, neighbors.get(TYPE.ENDPOINT))) {
				neighbors.get(TYPE.ENDPOINT).add(neighbor);
			}
		}
		this.panID = response.getBeacon().getPanId();
		this.extendedPanID = response.getBeacon().getExtendedPanID();
		return true;
	}
	
	protected abstract void init();
	
	protected  abstract void execute(DataMessage request);
	
	
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
	
	public int getExtendedPanID() {
		return extendedPanID;
	}
	
	public void setExtendedPanID(int extendedPanID) {
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
	
	public int getPort() {
		return listenerPort;
	}
	
	public String getIP() {
		return ipAddress;
	}
	
	
}
