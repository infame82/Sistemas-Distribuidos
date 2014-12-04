package com.uag.sd.weathermonitor.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EmptyBorder;

import com.uag.sd.weathermonitor.gui.models.SensorTableModel;
import com.uag.sd.weathermonitor.model.device.ZigBeeEndpoint;
import com.uag.sd.weathermonitor.model.sensor.HumiditySensor;
import com.uag.sd.weathermonitor.model.sensor.Sensor;
import com.uag.sd.weathermonitor.model.sensor.TemperatureSensor;

public class SensorDialogGUI extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 838713263181896725L;
	private final JPanel contentPanel = new JPanel();
	private JTextField idField;
	private JLabel lblId;
	private JComboBox<String> typeBox;
	private JTextField valueField;
	private JSpinner lapseBox;
	private JCheckBox chckbxActive;
	private ZigBeeEndpoint zigBeeEndpoint;
	private Sensor sensor;
	private SensorTableModel sensorTableModel;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			SensorDialogGUI dialog = new SensorDialogGUI();
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public SensorDialogGUI(ZigBeeEndpoint zigBeeEndpoint,Sensor sensor,SensorTableModel sensorTableModel) {
		this();
		this.zigBeeEndpoint = zigBeeEndpoint;
		this.sensor = sensor;
		this.sensorTableModel = sensorTableModel;
		if(sensor!=null) {
			idField.setText(sensor.getId());
			idField.setEnabled(false);
			typeBox.setEnabled(false);
			lapseBox.getModel().setValue(new Double(sensor.getLapse()/1000).intValue());
			valueField.setText(sensor.getValue());
			chckbxActive.setSelected(sensor.isActive());
		}
	}

	/**
	 * Create the dialog.
	 */
	public SensorDialogGUI() {
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setType(Type.POPUP);
		setModal(true);
		setResizable(false);
		setAlwaysOnTop(true);
		setBounds(100, 100, 208, 247);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		{
			lblId = new JLabel("ID:");
		}
		{
			idField = new JTextField();
			idField.setColumns(10);
		}
		
		JLabel lblType = new JLabel("Type:");
		String[] types = new String[]{ "Temperature","Humidity" };
		typeBox = new JComboBox<String>(types);
		typeBox.setSelectedIndex(0);
		
		SpinnerModel lapseModel = new SpinnerNumberModel(1,1, 3600, 1);
		lapseBox = new JSpinner(lapseModel);
		
		valueField = new JTextField();
		valueField.setColumns(10);
		
		chckbxActive = new JCheckBox("Active");
		
		JLabel lblLapse = new JLabel("Lapse:");
		
		JLabel lblValue = new JLabel("Value:");
		GroupLayout gl_contentPanel = new GroupLayout(contentPanel);
		gl_contentPanel.setHorizontalGroup(
			gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPanel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
						.addComponent(lblType)
						.addComponent(lblId)
						.addComponent(lblLapse)
						.addComponent(lblValue))
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPanel.createSequentialGroup()
							.addGap(58)
							.addComponent(chckbxActive))
						.addGroup(gl_contentPanel.createSequentialGroup()
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING, false)
								.addComponent(lapseBox, GroupLayout.PREFERRED_SIZE, 74, GroupLayout.PREFERRED_SIZE)
								.addComponent(valueField)
								.addComponent(idField)
								.addComponent(typeBox, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
					.addContainerGap(248, Short.MAX_VALUE))
		);
		gl_contentPanel.setVerticalGroup(
			gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPanel.createSequentialGroup()
					.addGap(8)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblId)
						.addComponent(idField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblType)
						.addComponent(typeBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblLapse)
						.addComponent(lapseBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblValue)
						.addComponent(valueField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(chckbxActive)
					.addContainerGap(63, Short.MAX_VALUE))
		);
		contentPanel.setLayout(gl_contentPanel);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						boolean isNew = false;
						
						if(sensor==null) {
							if(typeBox.getSelectedIndex()==0) {
								sensor = new TemperatureSensor(idField.getText());
							}else if(typeBox.getSelectedIndex()==1){
								sensor = new HumiditySensor(idField.getText());
							}
							isNew = true;
						}
						boolean prevState = sensor.isActive();
						sensor.setLapse(((int)lapseBox.getModel().getValue())*1000);
						sensor.setValue(valueField.getText());
						sensor.setActive(chckbxActive.isSelected());
						if(isNew) {
							int rowCount = zigBeeEndpoint.getSensors().size();
							zigBeeEndpoint.addSensor(sensor);
							sensorTableModel.fireTableRowsInserted(rowCount, rowCount);
							if(sensor.isActive()) {
								sensor.setActive(false);
								zigBeeEndpoint.startSensor(sensor);
							}
						}else {
							if(prevState != sensor.isActive()) {
								if(sensor.isActive()) {
									sensor.setActive(false);
									zigBeeEndpoint.startSensor(sensor);
								}else {
									zigBeeEndpoint.stopSensor(sensor);
								}
							}
							int rowIndex = sensorTableModel.getIndexOf(sensor);
							sensorTableModel.fireTableRowsUpdated(rowIndex, rowIndex);
						}
						SensorDialogGUI.this.dispose();
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						SensorDialogGUI.this.dispose();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}
}
