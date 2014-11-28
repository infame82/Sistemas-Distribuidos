package com.uag.sd.weathermonitor.model.device;

import java.io.IOException;

public class ZigBeeCoordinator extends Device {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7156658081587428861L;
	
	public ZigBeeCoordinator(String id, DeviceLog log) throws IOException  {
		super(id,log);
		coordinator= true;
		allowJoin = true;
	}

	@Override
	protected void init() {
	}

	@Override
	protected void execute(DataMessage msg) {
		log.info(new DeviceData(this.id, "Msg from "+msg.getBeacon().getId()+", data:"+msg.getData()+", Type:"+msg.getType()));
	}

}
