package com.uag.sd.weathermonitor.gui.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.table.AbstractTableModel;

import com.uag.sd.weathermonitor.model.device.Beacon;
import com.uag.sd.weathermonitor.model.layer.physical.channel.RFChannel;

public class NetworkTableModel extends AbstractTableModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3490851868110856742L;
	private String[] columnNames = { "Channel","PANID","ExtendedPANID"};
	private List<NetworkValue> networks;
	
	public NetworkTableModel() {
		networks = new ArrayList<NetworkValue>();
	}
	
	public NetworkTableModel(Map<RFChannel, List<Beacon>> availableNetworks) {
		this();
		for(RFChannel ch:availableNetworks.keySet()) {
			for(Beacon beacon:availableNetworks.get(ch)) {
				NetworkValue value = new NetworkValue();
				value.setBeacon(beacon);
				value.setChannel(ch);
				networks.add(value);
			}
		}
	}

	@Override
	public int getRowCount() {
		return networks.size();
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
		NetworkValue network = networks.get(rowIndex);
		switch(columnIndex) {
		case 0:
			return network.getChannel().getChannel();
		case 1:
			return network.getBeacon().getPanId();
		case 2:
			return network.getBeacon().getExtendedPanID();
		}
		return null;
	}
	
	public NetworkValue get(int rowIndex) {
		return networks.get(rowIndex);
	}

}
