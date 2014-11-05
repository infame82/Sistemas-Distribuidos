package com.uag.sd.weathermonitor.model.sensor;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component("humiditySensor")
@Scope("prototype")
public class HumiditySensor extends Sensor{

	public HumiditySensor() {
		value = ".5";
	}
	
	public HumiditySensor(String id) {
		super(id);
		value = ".5";
	}

	@Override
	public String detect() {
		return value;
	}

}
