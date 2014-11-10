package com.uag.sd.weathermonitor.model.layer.mac;

import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import com.uag.sd.weathermonitor.model.device.DeviceLog;
import com.uag.sd.weathermonitor.model.device.Traceable;

public class MacLayerInterfaceClient implements MacLayerInterface {

	private InetAddress group;
	private DeviceLog log;
	private Traceable device;

	public MacLayerInterfaceClient(Traceable device, DeviceLog log)
			throws SocketException, UnknownHostException {
		this.log = log;
		this.device = device;
		group = InetAddress.getByName(MAC_LAYER_ADDRESS);
	}

	public DeviceLog getLog() {
		return log;
	}

	public void setLog(DeviceLog log) {
		this.log = log;
	}
	
	
	
}
