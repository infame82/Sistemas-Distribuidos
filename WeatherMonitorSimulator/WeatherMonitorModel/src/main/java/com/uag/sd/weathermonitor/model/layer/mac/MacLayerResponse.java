package com.uag.sd.weathermonitor.model.layer.mac;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.uag.sd.weathermonitor.model.device.Traceable;
import com.uag.sd.weathermonitor.model.layer.physical.channel.RFChannel;
import com.uag.sd.weathermonitor.model.layer.physical.channel.RFChannel.RF_CHANNEL;

public class MacLayerResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1828424982555084019L;
	
	public enum CONFIRM{INVALID_REQUEST,SUCCESS};
	
	
	private CONFIRM confirm;
	private String message;
	private Map<RF_CHANNEL,RFChannel> channels;
	private Map<RF_CHANNEL,List<Traceable>> registeredDevices;
	
	public CONFIRM getConfirm() {
		return confirm;
	}
	public void setConfirm(CONFIRM confirm) {
		this.confirm = confirm;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Map<RF_CHANNEL, RFChannel> getChannels() {
		return channels;
	}
	public void setChannels(Map<RF_CHANNEL, RFChannel> channels) {
		this.channels = channels;
	}
	public Map<RF_CHANNEL, List<Traceable>> getRegisteredDevices() {
		return registeredDevices;
	}
	public void setRegisteredDevices(Map<RF_CHANNEL, List<Traceable>> registeredDevices) {
		this.registeredDevices = registeredDevices;
	}
	
	
}
