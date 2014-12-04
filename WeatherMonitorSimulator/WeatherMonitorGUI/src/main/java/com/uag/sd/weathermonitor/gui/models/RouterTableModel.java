package com.uag.sd.weathermonitor.gui.models;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import javax.swing.table.AbstractTableModel;

import com.uag.sd.weathermonitor.model.device.ZigBeeRouter;

public class RouterTableModel extends AbstractTableModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2810225090066450282L;
	private List<ZigBeeRouter> zigBeeRouters;
	private String[] columnNames = { "ID","PANID","ExtendedPANID", "Location", "Coverage",
    "Status" };
	private ThreadPoolExecutor service;
	
	public RouterTableModel() {
		zigBeeRouters = new ArrayList<ZigBeeRouter>();
		service = (ThreadPoolExecutor) Executors.newFixedThreadPool(20);
	}

	@Override
	public int getRowCount() {
		return zigBeeRouters.size();
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
		ZigBeeRouter zigBeeRouter = zigBeeRouters.get(rowIndex);
		switch(columnIndex) {
		case 0:
			return zigBeeRouter.getId();
		case 1:
			return zigBeeRouter.getPanId();
		case 2:
			return zigBeeRouter.getExtendedPanID();
		case 3:
			return "X: "+zigBeeRouter.getLocation().getX()+", Y: "+zigBeeRouter.getLocation().getY();
		case 4:
			return zigBeeRouter.getPotency();
		case 5:
			return zigBeeRouter.isActive()?"Active":"Inactive";
		}
		return null;
	}
	
	public void add(ZigBeeRouter zigBeeRouter) {
		int rowCount = zigBeeRouters.size();
		zigBeeRouters.add(zigBeeRouter);
		fireTableRowsInserted(rowCount, rowCount);
	}
	
	public ZigBeeRouter get(int rowIndex) {
		return zigBeeRouters.get(rowIndex);
	}
	
	public int getIndexOf(ZigBeeRouter zigBeeRouter) {
		int index = -1;
		for(int i=0;i<zigBeeRouters.size();i++) {
			if(zigBeeRouters.get(i).equals(zigBeeRouter)) {
				index = i;
				break;
			}
		}
		return index;
	}
	
	public ZigBeeRouter remove(int rowIndex) {
		fireTableRowsDeleted(rowIndex, rowIndex);
		return zigBeeRouters.remove(rowIndex);
	}
	
	public void startAll() {
		for(ZigBeeRouter zigBeeRouter:zigBeeRouters) {
			if(!zigBeeRouter.isActive()) {
				service.execute(zigBeeRouter);
			}
		}
		fireTableDataChanged();
	}
	
	public void start(ZigBeeRouter zigBeeRouter) {
		service.execute(zigBeeRouter);
	}
	
	public void stopAll() {
		for(ZigBeeRouter zigBeeRouter:zigBeeRouters) {
			if(zigBeeRouter.isActive()) {
				zigBeeRouter.stop();
				service.remove(zigBeeRouter);
			}
		}
		fireTableDataChanged();
	}
}
