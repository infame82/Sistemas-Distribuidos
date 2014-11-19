package com.uag.sd.weathermonitor.model.layer.physical;

import java.io.Serializable;
import java.util.List;

import com.uag.sd.weathermonitor.model.layer.physical.channel.RFChannel;


public class PhysicalLayerResponse implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5138691053979203050L;
	public enum CONFIRM{INVALID_REQUEST,SUCCESS};
	
	
	private CONFIRM confirm;
	private String message;
	private List<RFChannel> channels;
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
	public void setChannels(List<RFChannel> channels) {
		this.channels = channels;
	}
	
	
}
