package com.uag.sd.weathermonitor.gui;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import javax.swing.table.AbstractTableModel;

import com.uag.sd.weathermonitor.model.endpoint.Endpoint;

public class EndpointTableModel extends AbstractTableModel{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2378293237871201278L;
	private List<Endpoint> endpoints;
	private String[] columnNames = { "ID", "Location", "Coverage",
	        "Status" };
	private ThreadPoolExecutor service;
	
	public EndpointTableModel() {
		endpoints = new ArrayList<Endpoint>();
		service = (ThreadPoolExecutor) Executors.newFixedThreadPool(50);
	}

	@Override
	public int getRowCount() {
		return endpoints.size();
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
		Endpoint endpoint = endpoints.get(rowIndex);
		switch(columnIndex) {
		case 0:
			return endpoint.getId();
		case 1:
			return "X: "+endpoint.getLocation().getX()+", Y: "+endpoint.getLocation().getY();
		case 2:
			return endpoint.getCoverage();
		case 3:
			return endpoint.isActive()?"Active":"Inactive";
		}
		return null;
	}
	
	public void addEndpoint(Endpoint endpoint) {
		int rowCount = endpoints.size();
		endpoints.add(endpoint);
		fireTableRowsInserted(rowCount, rowCount);
	}
	
	public Endpoint getEndpoint(int rowIndex) {
		return endpoints.get(rowIndex);
	}
	
	public int getIndexOf(Endpoint endpoint) {
		int index = -1;
		for(int i=0;i<endpoints.size();i++) {
			if(endpoints.get(i).equals(endpoint)) {
				index = i;
				break;
			}
		}
		return index;
	}
	
	public Endpoint removeEndpoint(int rowIndex) {
		fireTableRowsDeleted(rowIndex, rowIndex);
		return endpoints.remove(rowIndex);
	}
	
	public void startAllEnpoints() {
		for(Endpoint endpoint:endpoints) {
			if(!endpoint.isActive()) {
				service.execute(endpoint);
			}
		}
		fireTableDataChanged();
	}
	
	public void startEndpoint(Endpoint endpoint) {
		service.execute(endpoint);
	}
	
	public void stopAllEndpoints() {
		for(Endpoint endpoint:endpoints) {
			if(endpoint.isActive()) {
				endpoint.stop();
				service.remove(endpoint);
			}
		}
		fireTableDataChanged();
	}

}
