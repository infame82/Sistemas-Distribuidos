package com.uag.sd.weathermonitor.gui.models;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import javax.swing.table.AbstractTableModel;

import com.uag.sd.weathermonitor.model.router.Router;

public class RouterTableModel extends AbstractTableModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2810225090066450282L;
	private List<Router> routers;
	private String[] columnNames = { "ID", "Location", "Coverage",
    "Status" };
	private ThreadPoolExecutor service;
	
	public RouterTableModel() {
		routers = new ArrayList<Router>();
		service = (ThreadPoolExecutor) Executors.newFixedThreadPool(50);
	}

	@Override
	public int getRowCount() {
		return routers.size();
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
		Router router = routers.get(rowIndex);
		switch(columnIndex) {
		case 0:
			return router.getId();
		case 1:
			return "X: "+router.getLocation().getX()+", Y: "+router.getLocation().getY();
		case 2:
			return router.getCoverage();
		case 3:
			return router.isActive()?"Active":"Inactive";
		}
		return null;
	}
	
	public void addRouter(Router router) {
		int rowCount = routers.size();
		routers.add(router);
		fireTableRowsInserted(rowCount, rowCount);
	}
	
	public Router getRouter(int rowIndex) {
		return routers.get(rowIndex);
	}
	
	public int getIndexOf(Router router) {
		int index = -1;
		for(int i=0;i<routers.size();i++) {
			if(routers.get(i).equals(router)) {
				index = i;
				break;
			}
		}
		return index;
	}
	
	public Router removeEndpoint(int rowIndex) {
		fireTableRowsDeleted(rowIndex, rowIndex);
		return routers.remove(rowIndex);
	}
	
	public void startAllRouters() {
		for(Router router:routers) {
			if(!router.isActive()) {
				service.execute(router);
			}
		}
		fireTableDataChanged();
	}
	
	public void startRouter(Router router) {
		service.execute(router);
	}
	
	public void stopAllRouters() {
		for(Router router:routers) {
			if(router.isActive()) {
				router.stop();
				service.remove(router);
			}
		}
		fireTableDataChanged();
	}
}
