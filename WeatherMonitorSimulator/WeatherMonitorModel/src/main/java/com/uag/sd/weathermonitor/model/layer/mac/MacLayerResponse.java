package com.uag.sd.weathermonitor.model.layer.mac;

import java.io.Serializable;

public class MacLayerResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1828424982555084019L;
	
	public enum CONFIRM{INVALID_REQUEST,SUCCESS};
	
	
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
