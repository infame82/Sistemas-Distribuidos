package com.uag.sd.weathermonitor.model.device;

import java.io.IOException;
import java.net.DatagramPacket;

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
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void execute(DeviceLayerRequest request) {
		// TODO Auto-generated method stub
		
	}

}
