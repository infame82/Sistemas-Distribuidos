package com.uag.sd.weathermonitor.gui.models;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import javax.swing.table.AbstractTableModel;

import com.uag.sd.weathermonitor.model.device.ZigBeeCoordinator;

public class CoordinatorTableModel extends AbstractTableModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7126129425772272472L;
	private List<ZigBeeCoordinator> zigBeeCoordinators;
	private String[] columnNames = { "ID","PANID","ExtendedPANID", "Location", "Coverage",
    "Status" };
	private ThreadPoolExecutor service;
	
	public CoordinatorTableModel() {
		zigBeeCoordinators = new ArrayList<ZigBeeCoordinator>();
		service = (ThreadPoolExecutor) Executors.newFixedThreadPool(20);
	}

	@Override
	public int getRowCount() {
		return zigBeeCoordinators.size();
		
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
		ZigBeeCoordinator coordinator = zigBeeCoordinators.get(rowIndex);
		switch(columnIndex) {
		case 0:
			return coordinator.getId();
		case 1:
			return coordinator.getPanId();
		case 2:
			return coordinator.getExtendedPanID();
		case 3:
			return "X: "+coordinator.getLocation().getX()+", Y: "+coordinator.getLocation().getY();
		case 4:
			return coordinator.getPotency();
		case 5:
			return coordinator.isActive()?"Active":"Inactive";
		}
		return null;
	}
	
	public void add(ZigBeeCoordinator coordinator) {
		int rowCount = zigBeeCoordinators.size();
		zigBeeCoordinators.add(coordinator);
		fireTableRowsInserted(rowCount, rowCount);
	}
	
	public ZigBeeCoordinator get(int rowIndex) {
		return zigBeeCoordinators.get(rowIndex);
	}
	
	public int getIndexOf(ZigBeeCoordinator coordinator) {
		int index = -1;
		for(int i=0;i<zigBeeCoordinators.size();i++) {
			if(zigBeeCoordinators.get(i).getId().equals(coordinator.getId())) {
				index = i;
				break;
			}
		}
		return index;
	}
	
	public ZigBeeCoordinator remove(int rowIndex) {
		fireTableRowsDeleted(rowIndex, rowIndex);
		return zigBeeCoordinators.remove(rowIndex);
	}
	
	public void start(ZigBeeCoordinator coordinator) {
		service.execute(coordinator);
	}
	
	public void stop(ZigBeeCoordinator coordinator) {
		coordinator.stop();
	}

}
