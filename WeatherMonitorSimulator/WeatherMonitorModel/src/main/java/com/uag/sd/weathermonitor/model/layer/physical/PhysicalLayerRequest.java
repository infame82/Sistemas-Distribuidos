package com.uag.sd.weathermonitor.model.layer.physical;

import java.io.Serializable;

import com.uag.sd.weathermonitor.model.device.Device;
import com.uag.sd.weathermonitor.model.device.Beacon;
import com.uag.sd.weathermonitor.model.layer.physical.channel.RFChannel.RF_CHANNEL;

public class PhysicalLayerRequest implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6697321423558373595L;
	
	public enum PRIMITIVE { REQUEST_PHYSICAL_NODE(
				"Physical Node"),REQUEST_GET_CHANNELS("GET Channels"),INCREASE_ENERGY_LEVEL("Increase Energy Level");
		public String description;

		private PRIMITIVE(String description) {
			this.description = description;
		}
	};
	
	private PRIMITIVE primitive;
	private Beacon device;
	private long id;
	private RF_CHANNEL selectedChannel;
	private boolean responseRequired;
	
	public PRIMITIVE getPrimitive() {
		return primitive;
	}
	
	public PhysicalLayerRequest() {
		responseRequired = true;
		id = System.currentTimeMillis();
	}

	public PhysicalLayerRequest(PRIMITIVE primitive, Device device) {
		this();
		this.primitive = primitive;
		this.device = device;
		
	}
	
	public void setPrimitive(PRIMITIVE primitive) {
		this.primitive = primitive;
	}
	public Beacon getDevice() {
		return device;
	}
	public void setDevice(Beacon device) {
		this.device = device;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}

	public RF_CHANNEL getSelectedChannel() {
		return selectedChannel;
	}

	public void setSelectedChannel(RF_CHANNEL selectedChannel) {
		this.selectedChannel = selectedChannel;
	}

	public boolean isResponseRequired() {
		return responseRequired;
	}

	public void setResponseRequired(boolean responseRequired) {
		this.responseRequired = responseRequired;
	}
	
	

}
