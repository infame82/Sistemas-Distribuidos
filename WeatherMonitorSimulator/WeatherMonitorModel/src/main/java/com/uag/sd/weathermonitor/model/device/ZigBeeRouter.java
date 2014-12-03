package com.uag.sd.weathermonitor.model.device;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.uag.sd.weathermonitor.model.layer.network.NetworlLayerRequest;

@Component("zigBeeRouter")
@Scope("prototype")
public class ZigBeeRouter extends Device {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6061007998656566549L;
	protected transient Map<String, Device> zigBeeDevices;
	protected transient Map<String, Device> zigBeeRouters;


	public ZigBeeRouter(String id, DeviceLog log) throws IOException  {
		super(id,log);
		router = true;
		allowJoin = true;
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

	public boolean addEndpoint(ZigBeeEndpoint zigBeeEndpoint) {
		if (!zigBeeDevices.containsKey(zigBeeEndpoint.getId())) {
			zigBeeDevices.put(zigBeeEndpoint.getId(), zigBeeEndpoint);
		}
		return false;
	}

	public void removeEndpoint(String id) {
		zigBeeDevices.remove(id);
	}

	@Override
	protected void init() {}

	@Override
	protected void execute(DataMessage msg) {
		NetworlLayerRequest request = new NetworlLayerRequest();
		
		List<Beacon> parents = new ArrayList<Beacon>();
		if(!neighbors.get(TYPE.COORDINATOR).isEmpty()) {
			parents.addAll(neighbors.get(TYPE.COORDINATOR));
		}else {
			parents.addAll(neighbors.get(TYPE.ROUTER));
		}
		//System.out.println("-->"+this.id+"-->"+neighbors);
		request.setDevice(msg.getBeacon());
		request.setData(msg);
		request.setAssociateBeacons(parents);
		networkInterfaceClient.retransmitData(request);
	}
}
