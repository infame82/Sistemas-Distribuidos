package com.uag.sd.weathermonitor.model.device;

import java.io.Serializable;

public class DeviceLayerResponse implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8028727896138823662L;
	
	public enum CONFIRM{INVALID_REQUEST,SUCCESS};
	
	private CONFIRM confirm;
	private String msg;
	public CONFIRM getConfirm() {
		return confirm;
	}
	public void setConfirm(CONFIRM confirm) {
		this.confirm = confirm;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	

}
