package com.uag.sd.weathermonitor.gui;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import com.uag.sd.weathermonitor.model.endpoint.Endpoint;
import com.uag.sd.weathermonitor.model.router.Router;

public class EndpointRouterTableModel extends AbstractTableModel{
	

	/**
	 * 
	 */
	private static final long serialVersionUID = -3087621825478973814L;
	
	private Endpoint endpoint;
	private String[] columnNames = { "ID", "Location"};

	@Override
	public int getRowCount() {
		if(endpoint==null || endpoint.getRouters()==null) {
			return 0;
		}
		return endpoint.getRouters().size();
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
		if(endpoint.getRouters()==null || endpoint.getRouters().isEmpty()
				|| rowIndex>=endpoint.getRouters().size()) {
			return null;
		}
		List<Router> auxList = new ArrayList<Router>(endpoint.getRouters().values());
		Router router = auxList.get(rowIndex);
		switch (columnIndex) {
		case 0:
			return router.getId();
		case 1:
			return  "X: "+router.getLocation().getX()+", Y: "+router.getLocation().getY();
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
	
	public void clear() {
		endpoint.getRouters().clear();
		fireTableDataChanged();
	}

}
