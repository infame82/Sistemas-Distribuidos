package com.uag.sd.weathermonitor.gui.models;

import javax.swing.table.AbstractTableModel;

import com.uag.sd.weathermonitor.model.device.ZigBeeEndpoint;
import com.uag.sd.weathermonitor.model.sensor.HumiditySensor;
import com.uag.sd.weathermonitor.model.sensor.Sensor;
import com.uag.sd.weathermonitor.model.sensor.TemperatureSensor;

public class SensorTableModel extends AbstractTableModel{
	
	//private List<Sensor> sensors;
	private ZigBeeEndpoint zigBeeEndpoint;
	private String[] columnNames = { "ID", "Type", "Lapse","Value",
	        "Status" };

	/**
	 * 
	 */
	private static final long serialVersionUID = 829922993812147530L;
	


	@Override
	public int getRowCount() {
		if(zigBeeEndpoint==null || zigBeeEndpoint.getSensors()==null) {
			return 0;
		}
		return zigBeeEndpoint.getSensors().size();
	}

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}
	@Override
	public String getColumnName(int col) {
		return columnNames[col];
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		if(zigBeeEndpoint==null || zigBeeEndpoint.getSensors()==null || zigBeeEndpoint.getSensors().isEmpty()) {
			return null;
		}
		Sensor sensor = zigBeeEndpoint.getSensors().get(rowIndex);
		switch(columnIndex) {
		case 0:
			return sensor.getId();
		case 1:
			if(sensor instanceof TemperatureSensor) {
				return "Temperature";
			}else if(sensor instanceof HumiditySensor) {
				return "Humidity";
			}
		case 2:
			return (sensor.getLapse()/1000)+" seg";
		case 3:
			return sensor.detect();
		case 4:
			return sensor.isActive()?"Active":"Inactive";
		}
		return null;
	}
	
	public void setEndpoint(ZigBeeEndpoint zigBeeEndpoint) {
		this.zigBeeEndpoint = zigBeeEndpoint;
		fireTableDataChanged();
	}
	
	public ZigBeeEndpoint getEndpoint() {
		return zigBeeEndpoint;
	}
	

	
	public int getIndexOf(Sensor sensor) {
		int index = -1;
		for(int i=0;i<zigBeeEndpoint.getSensors().size();i++) {
			if(zigBeeEndpoint.getSensors().get(i).equals(sensor)) {
				index = i;
				break;
			}
		}
		return index;
	}
	

	
	public void clear() {
		zigBeeEndpoint.getSensors().clear();
		fireTableDataChanged();
	}

}
