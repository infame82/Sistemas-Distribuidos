package com.uag.sd.weathermonitor.model.device;

import java.io.Serializable;

public class DeviceLayerRequest implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3224998591505882682L;

	public enum PRIMITIVE {
		ADD_NEIGHBORD("Add Neighbord");
		public String description;

		private PRIMITIVE(String description) {
			this.description = description;
		}
	};
	
	private PRIMITIVE primitive;
	private Beacon neighbord;

	
	public DeviceLayerRequest(PRIMITIVE primitive) {
		this.primitive = primitive;
	}
	
	
	public PRIMITIVE getPrimitive() {
		return primitive;
	}
	public void setPrimitive(PRIMITIVE primitive) {
		this.primitive = primitive;
	}
	public Beacon getNeighbord() {
		return neighbord;
	}
	public void setNeighbord(Beacon neighbord) {
		this.neighbord = neighbord;
	}
	
	

}
