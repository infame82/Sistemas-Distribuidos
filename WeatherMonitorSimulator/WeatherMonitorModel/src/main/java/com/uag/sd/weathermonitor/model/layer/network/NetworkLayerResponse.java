package com.uag.sd.weathermonitor.model.layer.network;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.uag.sd.weathermonitor.model.device.Traceable;
import com.uag.sd.weathermonitor.model.layer.physical.channel.RFChannel;

public class NetworkLayerResponse implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5958884861229323221L;
	
	public enum CONFIRM{INVALID_REQUEST,SUCCESS,STARTUP_FAILURE};
	
	
	private CONFIRM confirm;
	private String message;
	private long extendedPANID;
	private Map<RFChannel, List<Traceable>>  availableNetworks;
	
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
	public long getExtendedPANID() {
		return extendedPANID;
	}
	public void setExtendedPANID(long extendedPANID) {
		this.extendedPANID = extendedPANID;
	}
	public Map<RFChannel, List<Traceable>> getAvailableNetworks() {
		return availableNetworks;
	}
	public void setAvailableNetworks(
			Map<RFChannel, List<Traceable>> availableNetworks) {
		this.availableNetworks = availableNetworks;
	}
	
	

}
