package com.uag.sd.weathermonitor.model.device;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DefaultDeviceLog implements DeviceLog{
	
	private SimpleDateFormat dateFormatter;
	
	public DefaultDeviceLog() {
		dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
	}

	@Override
	public void info(DeviceData msg) {
		StringBuilder builder = new StringBuilder();
		builder.append(dateFormatter.format(new Date(System.currentTimeMillis())) );
		builder.append(" - ID ");
		builder.append(msg.getDeviceId());
		builder.append(", Value: ");
		builder.append(msg.getData());
		builder.append(System.lineSeparator());
		System.out.println(builder.toString());
	}

	@Override
	public void debug(DeviceData msg) {
		StringBuilder builder = new StringBuilder();
		builder.append(dateFormatter.format(new Date(System.currentTimeMillis())) );
		builder.append(" - ID ");
		builder.append(msg.getDeviceId());
		builder.append(", Message: ");
		builder.append(msg.getData());
		builder.append(System.lineSeparator());
		System.err.println(builder.toString());
	}

}
