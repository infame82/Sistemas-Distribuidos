package com.uag.sd.weathermonitor.gui.models;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import javax.swing.table.AbstractTableModel;

import com.uag.sd.weathermonitor.model.router.ZigBeeRouter;

public class RouterTableModel extends AbstractTableModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2810225090066450282L;
	private List<ZigBeeRouter> zigBeeRouters;
	private String[] columnNames = { "ID", "Location", "Coverage",
    "Status" };
	private ThreadPoolExecutor service;
	
	public RouterTableModel() {
		zigBeeRouters = new ArrayList<ZigBeeRouter>();
		service = (ThreadPoolExecutor) Executors.newFixedThreadPool(50);
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
			return "X: "+zigBeeRouter.getLocation().getX()+", Y: "+zigBeeRouter.getLocation().getY();
		case 2:
			return zigBeeRouter.getCoverage();
		case 3:
			return zigBeeRouter.isActive()?"Active":"Inactive";
		}
		return null;
	}
	
	public void addRouter(ZigBeeRouter zigBeeRouter) {
		int rowCount = zigBeeRouters.size();
		zigBeeRouters.add(zigBeeRouter);
		fireTableRowsInserted(rowCount, rowCount);
	}
	
	public ZigBeeRouter getRouter(int rowIndex) {
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
	
	public ZigBeeRouter removeEndpoint(int rowIndex) {
		fireTableRowsDeleted(rowIndex, rowIndex);
		return zigBeeRouters.remove(rowIndex);
	}
	
	public void startAllRouters() {
		for(ZigBeeRouter zigBeeRouter:zigBeeRouters) {
			if(!zigBeeRouter.isActive()) {
				service.execute(zigBeeRouter);
			}
		}
		fireTableDataChanged();
	}
	
	public void startRouter(ZigBeeRouter zigBeeRouter) {
		service.execute(zigBeeRouter);
	}
	
	public void stopAllRouters() {
		for(ZigBeeRouter zigBeeRouter:zigBeeRouters) {
			if(zigBeeRouter.isActive()) {
				zigBeeRouter.stop();
				service.remove(zigBeeRouter);
			}
		}
		fireTableDataChanged();
	}
}
