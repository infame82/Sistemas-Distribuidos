package com.uag.sd.weathermonitor.gui.models;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import javax.swing.table.AbstractTableModel;

import com.uag.sd.weathermonitor.model.device.ZigBeeEndpoint;

public class EndpointTableModel extends AbstractTableModel{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2378293237871201278L;
	private List<ZigBeeEndpoint> zigBeeEndpoints;
	private String[] columnNames = { "ID", "Location", "Coverage",
	        "Status" };
	private ThreadPoolExecutor service;
	
	public EndpointTableModel() {
		zigBeeEndpoints = new ArrayList<ZigBeeEndpoint>();
		service = (ThreadPoolExecutor) Executors.newFixedThreadPool(50);
	}

	@Override
	public int getRowCount() {
		return zigBeeEndpoints.size();
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
		ZigBeeEndpoint zigBeeEndpoint = zigBeeEndpoints.get(rowIndex);
		switch(columnIndex) {
		case 0:
			return zigBeeEndpoint.getId();
		case 1:
			return "X: "+zigBeeEndpoint.getLocation().getX()+", Y: "+zigBeeEndpoint.getLocation().getY();
		case 2:
			return zigBeeEndpoint.getPotency();
		case 3:
			return zigBeeEndpoint.isActive()?"Active":"Inactive";
		}
		return null;
	}
	
	public void addEndpoint(ZigBeeEndpoint zigBeeEndpoint) {
		int rowCount = zigBeeEndpoints.size();
		zigBeeEndpoints.add(zigBeeEndpoint);
		fireTableRowsInserted(rowCount, rowCount);
	}
	
	public ZigBeeEndpoint getEndpoint(int rowIndex) {
		return zigBeeEndpoints.get(rowIndex);
	}
	
	public int getIndexOf(ZigBeeEndpoint zigBeeEndpoint) {
		int index = -1;
		for(int i=0;i<zigBeeEndpoints.size();i++) {
			if(zigBeeEndpoints.get(i).equals(zigBeeEndpoint)) {
				index = i;
				break;
			}
		}
		return index;
	}
	
	public ZigBeeEndpoint removeEndpoint(int rowIndex) {
		fireTableRowsDeleted(rowIndex, rowIndex);
		return zigBeeEndpoints.remove(rowIndex);
	}
	
	public void startAllEnpoints() {
		for(ZigBeeEndpoint zigBeeEndpoint:zigBeeEndpoints) {
			if(!zigBeeEndpoint.isActive()) {
				service.execute(zigBeeEndpoint);
			}
		}
		fireTableDataChanged();
	}
	
	public void startEndpoint(ZigBeeEndpoint zigBeeEndpoint) {
		service.execute(zigBeeEndpoint);
	}
	
	public void stopAllEndpoints() {
		for(ZigBeeEndpoint zigBeeEndpoint:zigBeeEndpoints) {
			if(zigBeeEndpoint.isActive()) {
				zigBeeEndpoint.stop();
				service.remove(zigBeeEndpoint);
			}
		}
		fireTableDataChanged();
	}

}
