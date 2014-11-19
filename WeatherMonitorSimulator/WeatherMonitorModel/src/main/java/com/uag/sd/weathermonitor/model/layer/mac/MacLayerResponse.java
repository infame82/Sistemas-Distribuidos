package com.uag.sd.weathermonitor.model.layer.mac;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.uag.sd.weathermonitor.model.device.Traceable;
import com.uag.sd.weathermonitor.model.layer.physical.channel.RFChannel;

public class MacLayerResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1828424982555084019L;
	
	public enum CONFIRM{INVALID_REQUEST,SUCCESS,STARTUP_FAILURE};
	
	
	private CONFIRM confirm;
	private String message;
	private List<RFChannel> channels;
	private Map<RFChannel,List<Traceable>> registeredDevices;
	private long extendedAddress;
	
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
	public List<RFChannel> getChannels() {
		return channels;
	}
	public void setChannels(List<RFChannel>  channels) {
		this.channels = channels;
	}
	public Map<RFChannel, List<Traceable>> getRegisteredDevices() {
		return registeredDevices;
	}
	public void setRegisteredDevices(Map<RFChannel, List<Traceable>> registeredDevices) {
		this.registeredDevices = registeredDevices;
	}
	public long getExtendedAddress() {
		return extendedAddress;
	}
	public void setExtendedAddress(long extendedAddress) {
		this.extendedAddress = extendedAddress;
	}
	
	
}
