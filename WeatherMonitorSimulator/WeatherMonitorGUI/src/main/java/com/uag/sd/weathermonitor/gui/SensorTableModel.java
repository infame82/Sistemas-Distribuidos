package com.uag.sd.weathermonitor.gui;

import javax.swing.table.AbstractTableModel;

import com.uag.sd.weathermonitor.model.endpoint.Endpoint;
import com.uag.sd.weathermonitor.model.sensor.HumiditySensor;
import com.uag.sd.weathermonitor.model.sensor.Sensor;
import com.uag.sd.weathermonitor.model.sensor.TemperatureSensor;

public class SensorTableModel extends AbstractTableModel{
	
	//private List<Sensor> sensors;
	private Endpoint endpoint;
	private String[] columnNames = { "ID", "Type", "Lapse","Value",
	        "Status" };

	/**
	 * 
	 */
	private static final long serialVersionUID = 829922993812147530L;
	


	@Override
	public int getRowCount() {
		if(endpoint==null || endpoint.getSensors()==null) {
			return 0;
		}
		return endpoint.getSensors().size();
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
		if(endpoint==null || endpoint.getSensors()==null || endpoint.getSensors().isEmpty()) {
			return null;
		}
		Sensor sensor = endpoint.getSensors().get(rowIndex);
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
	
	public void setEndpoint(Endpoint endpoint) {
		this.endpoint = endpoint;
		fireTableDataChanged();
	}
	
	public Endpoint getEndpoint() {
		return endpoint;
	}
	

	
	public int getIndexOf(Sensor sensor) {
		int index = -1;
		for(int i=0;i<endpoint.getSensors().size();i++) {
			if(endpoint.getSensors().get(i).equals(sensor)) {
				index = i;
				break;
			}
		}
		return index;
	}
	

	
	public void clear() {
		endpoint.getSensors().clear();
		fireTableDataChanged();
	}

}
