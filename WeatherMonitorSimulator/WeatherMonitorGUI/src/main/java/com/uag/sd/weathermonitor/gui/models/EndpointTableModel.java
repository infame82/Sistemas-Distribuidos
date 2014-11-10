package com.uag.sd.weathermonitor.gui.models;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import javax.swing.table.AbstractTableModel;

import com.uag.sd.weathermonitor.model.endpoint.ZigBeeDevice;

public class EndpointTableModel extends AbstractTableModel{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2378293237871201278L;
	private List<ZigBeeDevice> zigBeeDevices;
	private String[] columnNames = { "ID", "Location", "Coverage",
	        "Status" };
	private ThreadPoolExecutor service;
	
	public EndpointTableModel() {
		zigBeeDevices = new ArrayList<ZigBeeDevice>();
		service = (ThreadPoolExecutor) Executors.newFixedThreadPool(50);
	}

	@Override
	public int getRowCount() {
		return zigBeeDevices.size();
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
		ZigBeeDevice zigBeeDevice = zigBeeDevices.get(rowIndex);
		switch(columnIndex) {
		case 0:
			return zigBeeDevice.getId();
		case 1:
			return "X: "+zigBeeDevice.getLocation().getX()+", Y: "+zigBeeDevice.getLocation().getY();
		case 2:
			return zigBeeDevice.getCoverage();
		case 3:
			return zigBeeDevice.isActive()?"Active":"Inactive";
		}
		return null;
	}
	
	public void addEndpoint(ZigBeeDevice zigBeeDevice) {
		int rowCount = zigBeeDevices.size();
		zigBeeDevices.add(zigBeeDevice);
		fireTableRowsInserted(rowCount, rowCount);
	}
	
	public ZigBeeDevice getEndpoint(int rowIndex) {
		return zigBeeDevices.get(rowIndex);
	}
	
	public int getIndexOf(ZigBeeDevice zigBeeDevice) {
		int index = -1;
		for(int i=0;i<zigBeeDevices.size();i++) {
			if(zigBeeDevices.get(i).equals(zigBeeDevice)) {
				index = i;
				break;
			}
		}
		return index;
	}
	
	public ZigBeeDevice removeEndpoint(int rowIndex) {
		fireTableRowsDeleted(rowIndex, rowIndex);
		return zigBeeDevices.remove(rowIndex);
	}
	
	public void startAllEnpoints() {
		for(ZigBeeDevice zigBeeDevice:zigBeeDevices) {
			if(!zigBeeDevice.isActive()) {
				service.execute(zigBeeDevice);
			}
		}
		fireTableDataChanged();
	}
	
	public void startEndpoint(ZigBeeDevice zigBeeDevice) {
		service.execute(zigBeeDevice);
	}
	
	public void stopAllEndpoints() {
		for(ZigBeeDevice zigBeeDevice:zigBeeDevices) {
			if(zigBeeDevice.isActive()) {
				zigBeeDevice.stop();
				service.remove(zigBeeDevice);
			}
		}
		fireTableDataChanged();
	}

}
