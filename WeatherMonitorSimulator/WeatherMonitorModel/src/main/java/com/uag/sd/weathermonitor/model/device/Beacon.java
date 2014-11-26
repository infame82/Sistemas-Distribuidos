package com.uag.sd.weathermonitor.model.device;

import java.awt.Point;
import java.io.Serializable;

public interface Beacon extends Serializable {

	String getId();
	int getPanId();
	void setPanId(int panId);
	
	long getExtendedPanID();
	void setExtendedPanID(long extendedPanID);
	
	Point getLocation();
	int getPotency() ;
	boolean isCoordinator();
	boolean isRouter();
	boolean isEndpoint();
	boolean isAllowJoin();
	public boolean isStarted();
	public void setStarted(boolean started);
}
