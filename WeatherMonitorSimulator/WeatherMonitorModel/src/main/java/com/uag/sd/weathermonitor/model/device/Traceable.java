package com.uag.sd.weathermonitor.model.device;

import java.awt.Point;
import java.io.Serializable;

public interface Traceable extends Serializable {

	String getId();
	int getPanId();
	void setPanId(int panId);
	Point getLocation();
	boolean isCoordinator();
	
	public boolean isStarted();


	public void setStarted(boolean started);
}
