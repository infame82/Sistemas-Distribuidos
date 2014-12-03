package com.uag.sd.weathermonitor.model.device;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ZigBeeCoordinator extends Device {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7156658081587428861L;
	private Map<String,DeviceData> valuesMap;
	
	public ZigBeeCoordinator(String id, DeviceLog log) throws IOException  {
		super(id,log);
		coordinator= true;
		allowJoin = true;
	}

	@Override
	protected void init() {
		valuesMap = new HashMap<String, DeviceData>();
	}

	@Override
	protected void execute(DataMessage msg) {
		
		String mapKey = msg.getBeacon().getId()+":"+msg.getBeacon().getLocation();
		DeviceData prevData = valuesMap.get(mapKey);
		if(prevData==null || !prevData.getData().equals(msg.getData().getData())) {
			log.info(new DeviceData(this.id, "---->Msg ID:"+msg.getId()+", from "+msg.getBeacon().getId() +"["+msg.getBeacon().getLocation()+"]"+", data:"+msg.getData().getData()+", Type:"+msg.getData().getType()));	
			valuesMap.put(mapKey,msg.getData());
		}
	}

}
