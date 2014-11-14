package com.uag.sd.weathermonitor.model.device;

import java.awt.Point;
import java.io.Serializable;

public interface Traceable extends Serializable {

	String getId();
	Point getLocation();
}
