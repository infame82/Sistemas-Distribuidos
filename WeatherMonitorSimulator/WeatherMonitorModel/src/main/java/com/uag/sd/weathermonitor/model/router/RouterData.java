package com.uag.sd.weathermonitor.model.router;

public class RouterData {
	private String routerId;
	private String value;
	
	public RouterData(String routerId,String value) {
		this.routerId = routerId;
		this.value = value;
	}

	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}

	public String getRouterId() {
		return routerId;
	}

	public void setRouterId(String routerId) {
		this.routerId = routerId;
	}

	

}
