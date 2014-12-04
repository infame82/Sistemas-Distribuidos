package com.uag.sd.weathermonitor.gui.models;

import com.uag.sd.weathermonitor.model.device.Beacon;
import com.uag.sd.weathermonitor.model.layer.physical.channel.RFChannel;

public class NetworkValue {

	private RFChannel channel;
	private Beacon beacon;
	public RFChannel getChannel() {
		return channel;
	}
	public void setChannel(RFChannel channel) {
		this.channel = channel;
	}
	public Beacon getBeacon() {
		return beacon;
	}
	public void setBeacon(Beacon beacon) {
		this.beacon = beacon;
	}
	
	
}
