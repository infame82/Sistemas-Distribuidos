package com.uag.sd.weathermonitor.model.device;

import java.awt.Point;
import java.io.Serializable;

public interface Beacon extends Serializable {

	String getId();
	
	int getPort();
	String getIP();
	
	int getPanId();
	void setPanId(int panId);
	
	int getExtendedPanID();
	void setExtendedPanID(int extendedPanID);
	
	Point getLocation();
	int getPotency() ;
	
	boolean isCoordinator();
	boolean isRouter();
	boolean isEndpoint();
	boolean isAllowJoin();
	
	public boolean isStarted();
	public void setStarted(boolean started);
}
