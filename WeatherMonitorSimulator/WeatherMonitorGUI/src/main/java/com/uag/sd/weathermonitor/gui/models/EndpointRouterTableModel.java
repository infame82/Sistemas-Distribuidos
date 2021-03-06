package com.uag.sd.weathermonitor.gui.models;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import com.uag.sd.weathermonitor.model.device.ZigBeeEndpoint;
import com.uag.sd.weathermonitor.model.device.ZigBeeRouter;

public class EndpointRouterTableModel extends AbstractTableModel{
	

	/**
	 * 
	 */
	private static final long serialVersionUID = -3087621825478973814L;
	
	private ZigBeeEndpoint zigBeeEndpoint;
	private String[] columnNames = { "ID", "Location","Coverage"};

	@Override
	public int getRowCount() {
		if(zigBeeEndpoint==null || zigBeeEndpoint.getRouters()==null) {
			return 0;
		}
		return zigBeeEndpoint.getRouters().size();
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
		if(zigBeeEndpoint.getRouters()==null || zigBeeEndpoint.getRouters().isEmpty()
				|| rowIndex>=zigBeeEndpoint.getRouters().size()) {
			return null;
		}
		List<ZigBeeRouter> auxList = new ArrayList<ZigBeeRouter>(zigBeeEndpoint.getRouters().values());
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
	
	public void setEndpoint(ZigBeeEndpoint zigBeeEndpoint) {
		this.zigBeeEndpoint = zigBeeEndpoint;
		fireTableDataChanged();
	}
	
	public ZigBeeEndpoint getEndpoint() {
		return zigBeeEndpoint;
	}
	
	public void clear() {
		zigBeeEndpoint.getRouters().clear();
		fireTableDataChanged();
	}

}
