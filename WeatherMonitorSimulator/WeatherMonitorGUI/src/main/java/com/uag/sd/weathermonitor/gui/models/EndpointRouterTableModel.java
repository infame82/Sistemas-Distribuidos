package com.uag.sd.weathermonitor.gui.models;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import com.uag.sd.weathermonitor.model.endpoint.ZigBeeDevice;
import com.uag.sd.weathermonitor.model.router.ZigBeeRouter;

public class EndpointRouterTableModel extends AbstractTableModel{
	

	/**
	 * 
	 */
	private static final long serialVersionUID = -3087621825478973814L;
	
	private ZigBeeDevice zigBeeDevice;
	private String[] columnNames = { "ID", "Location","Coverage"};

	@Override
	public int getRowCount() {
		if(zigBeeDevice==null || zigBeeDevice.getRouters()==null) {
			return 0;
		}
		return zigBeeDevice.getRouters().size();
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
		if(zigBeeDevice.getRouters()==null || zigBeeDevice.getRouters().isEmpty()
				|| rowIndex>=zigBeeDevice.getRouters().size()) {
			return null;
		}
		List<ZigBeeRouter> auxList = new ArrayList<ZigBeeRouter>(zigBeeDevice.getRouters().values());
		ZigBeeRouter zigBeeRouter = auxList.get(rowIndex);
		switch (columnIndex) {
		case 0:
			return zigBeeRouter.getId();
		case 1:
			return  "X: "+zigBeeRouter.getLocation().getX()+", Y: "+zigBeeRouter.getLocation().getY();
		case 2:
			return zigBeeRouter.getPotency();
		}
		return null;
	}
	
	public void setEndpoint(ZigBeeDevice zigBeeDevice) {
		this.zigBeeDevice = zigBeeDevice;
		fireTableDataChanged();
	}
	
	public ZigBeeDevice getEndpoint() {
		return zigBeeDevice;
	}
	
	public void clear() {
		zigBeeDevice.getRouters().clear();
		fireTableDataChanged();
	}

}
