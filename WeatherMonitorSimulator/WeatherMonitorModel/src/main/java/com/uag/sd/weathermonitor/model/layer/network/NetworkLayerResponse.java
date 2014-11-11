package com.uag.sd.weathermonitor.model.layer.network;

import java.io.Serializable;

public class NetworkLayerResponse implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5958884861229323221L;
	
	public enum CONFIRM{INVALID_REQUEST,SUCCESS,STARTUP_FAILURE};
	
	
	private CONFIRM confirm;
	private String message;
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
	
	

}
