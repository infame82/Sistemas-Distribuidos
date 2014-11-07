package com.uag.sd.weathermonitor.model.endpoint;

public class EndpointData {

	private String endpointId;
	private String value;
	
	public EndpointData(String endpointId,String value) {
		this.endpointId = endpointId;
		this.value = value;
	}

	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}

	public String getEndpointId() {
		return endpointId;
	}

	public void setEndpointId(String endpointId) {
		this.endpointId = endpointId;
	}
	
	
}
