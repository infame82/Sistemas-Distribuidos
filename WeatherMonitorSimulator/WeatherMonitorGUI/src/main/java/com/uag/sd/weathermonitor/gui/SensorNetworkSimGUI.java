package com.uag.sd.weathermonitor.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JToggleButton;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.DefaultCaret;

import org.springframework.stereotype.Component;

import com.uag.sd.weathermonitor.model.endpoint.Endpoint;
import com.uag.sd.weathermonitor.model.logs.SensorLog;
import com.uag.sd.weathermonitor.model.sensor.HumiditySensor;
import com.uag.sd.weathermonitor.model.sensor.Sensor;
import com.uag.sd.weathermonitor.model.sensor.SensorData;
import com.uag.sd.weathermonitor.model.sensor.TemperatureSensor;

@Component("sensorNetworkSimulationGUI")
public class SensorNetworkSimGUI implements EndpointRefresher,SensorLog{

	private JFrame frmSensorNetworkSimulation;
	private JTable endPointsTable;
	private JTable sensorsTable;
	private EndpointTableModel endpointTableModel;
	private SensorTableModel sensorTableModel;
	private JLabel endpointIdLabel;
	private JTextArea sensorInfoConsole;
	private JTextArea sensorDebugConsole;
	private SimpleDateFormat dateFormatter;

	/**
	 * Create the application.
	 */
	public SensorNetworkSimGUI() {
		dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmSensorNetworkSimulation = new JFrame();
		frmSensorNetworkSimulation.addWindowListener(new WindowListener() {
			
			@Override
			public void windowOpened(WindowEvent e) {}
			
			@Override
			public void windowIconified(WindowEvent e) {}
			
			@Override
			public void windowDeiconified(WindowEvent e) {}
			
			@Override
			public void windowDeactivated(WindowEvent e) {}
			
			@Override
			public void windowClosing(WindowEvent e) {
				endpointTableModel.stopAllEndpoints();
			}
			
			@Override
			public void windowClosed(WindowEvent e) {}
			
			@Override
			public void windowActivated(WindowEvent e) {}
		});
		frmSensorNetworkSimulation.setTitle("Sensor Network Simulation");
		frmSensorNetworkSimulation.setBounds(100, 100, 613, 693);
		frmSensorNetworkSimulation.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmSensorNetworkSimulation.getContentPane().setLayout(new BorderLayout(0, 0));

		JSplitPane splitPane = new JSplitPane();
		frmSensorNetworkSimulation.getContentPane().add(splitPane, BorderLayout.CENTER);

		JPanel categoriesPanel = new JPanel();
		splitPane.setLeftComponent(categoriesPanel);

		JToggleButton endpointToggleButton = new JToggleButton("Endpoints");
		endpointToggleButton.setSelected(true);
		GroupLayout gl_categoriesPanel = new GroupLayout(categoriesPanel);
		gl_categoriesPanel.setHorizontalGroup(gl_categoriesPanel
				.createParallelGroup(Alignment.LEADING).addGroup(
						gl_categoriesPanel.createSequentialGroup()
								.addContainerGap()
								.addComponent(endpointToggleButton)
								.addContainerGap(10, Short.MAX_VALUE)));
		gl_categoriesPanel.setVerticalGroup(gl_categoriesPanel
				.createParallelGroup(Alignment.LEADING).addGroup(
						gl_categoriesPanel.createSequentialGroup().addGap(28)
								.addComponent(endpointToggleButton)
								.addContainerGap(356, Short.MAX_VALUE)));
		categoriesPanel.setLayout(gl_categoriesPanel);

		JPanel endpointsPanel = new JPanel();
		splitPane.setRightComponent(endpointsPanel);

		JLabel lblNewLabel = new JLabel("Endpoints");
		lblNewLabel.setFont(new Font("Lucida Grande", Font.BOLD, 18));

		JButton addEndpointButton = new JButton("Add");
		addEndpointButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					EndpointDialogGUI dialog = new EndpointDialogGUI(null,endpointTableModel,SensorNetworkSimGUI.this,SensorNetworkSimGUI.this);
					dialog.setTitle("Add Endpoint");
					dialog.setVisible(true);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});

		JButton editEndpointButton = new JButton("Edit");
		editEndpointButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Endpoint endpoint = endpointTableModel.getEndpoint(endPointsTable.getSelectedRow());
					EndpointDialogGUI dialog = new EndpointDialogGUI(endpoint,endpointTableModel,SensorNetworkSimGUI.this,SensorNetworkSimGUI.this);
					dialog.setTitle("Edit Endpoint");
					dialog.setVisible(true);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		editEndpointButton.setEnabled(false);

		JButton removeEndpointButton = new JButton("Remove");
		
		removeEndpointButton.setEnabled(false);

		JScrollPane scrollPane = new JScrollPane();
		
		JSeparator separator = new JSeparator();
		
		JLabel lblEndpointId = new JLabel("Endpoint ID:");
		lblEndpointId.setFont(new Font("Lucida Grande", Font.BOLD, 12));
		
		endpointIdLabel = new JLabel("");
		endpointIdLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		
		JButton startEndpointsButton = new JButton("Start All");
		startEndpointsButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				endpointTableModel.startAllEnpoints();
				sensorTableModel.fireTableDataChanged();
			}
		});
		
		JButton btnStopAll = new JButton("Stop All");
		btnStopAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				endpointTableModel.stopAllEndpoints();
				sensorTableModel.fireTableDataChanged();
			}
		});
		GroupLayout gl_endpointsPanel = new GroupLayout(endpointsPanel);
		gl_endpointsPanel.setHorizontalGroup(
			gl_endpointsPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_endpointsPanel.createSequentialGroup()
					.addGap(16)
					.addGroup(gl_endpointsPanel.createParallelGroup(Alignment.LEADING)
						.addComponent(lblNewLabel)
						.addGroup(gl_endpointsPanel.createSequentialGroup()
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(addEndpointButton)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(editEndpointButton)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(removeEndpointButton)
							.addGap(6)
							.addComponent(startEndpointsButton)
							.addGap(6)
							.addComponent(btnStopAll)))
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
				.addComponent(separator, GroupLayout.DEFAULT_SIZE, 404, Short.MAX_VALUE)
				.addGroup(gl_endpointsPanel.createSequentialGroup()
					.addGap(20)
					.addComponent(lblEndpointId)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(endpointIdLabel)
					.addContainerGap(301, Short.MAX_VALUE))
				.addGroup(gl_endpointsPanel.createSequentialGroup()
					.addComponent(tabbedPane, GroupLayout.DEFAULT_SIZE, 398, Short.MAX_VALUE)
					.addContainerGap())
				.addGroup(gl_endpointsPanel.createSequentialGroup()
					.addContainerGap()
					.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 392, Short.MAX_VALUE)
					.addContainerGap())
		);
		gl_endpointsPanel.setVerticalGroup(
			gl_endpointsPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_endpointsPanel.createSequentialGroup()
					.addGap(15)
					.addComponent(lblNewLabel)
					.addGap(18)
					.addGroup(gl_endpointsPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(addEndpointButton)
						.addComponent(editEndpointButton)
						.addComponent(removeEndpointButton)
						.addComponent(startEndpointsButton)
						.addComponent(btnStopAll))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 74, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(separator, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_endpointsPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblEndpointId)
						.addComponent(endpointIdLabel))
					.addGap(12)
					.addComponent(tabbedPane, GroupLayout.DEFAULT_SIZE, 295, Short.MAX_VALUE))
		);
		
		JPanel panel = new JPanel();
		tabbedPane.addTab("Sensors", null, panel, null);
		
		JButton addSensorButton = new JButton("Add");
		addSensorButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Endpoint endpoint = endpointTableModel.getEndpoint(endPointsTable.getSelectedRow());
					SensorDialogGUI dialog = new SensorDialogGUI(endpoint,null,sensorTableModel);
					dialog.setTitle("Add Sensor");
					dialog.setVisible(true);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		addSensorButton.setEnabled(false);
		
		JButton editSensorButton = new JButton("Edit");
		editSensorButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Endpoint endpoint = endpointTableModel.getEndpoint(endPointsTable.getSelectedRow());
					Sensor sensor = endpoint.getSensors().get(sensorsTable.getSelectedRow());
					SensorDialogGUI dialog = new SensorDialogGUI(endpoint,sensor,sensorTableModel);
					dialog.setTitle("Edit Sensor");
					dialog.setVisible(true);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		editSensorButton.setEnabled(false);
		
		JButton deleteSensorButton = new JButton("Remove");
		
		deleteSensorButton.setEnabled(false);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		
		JSeparator separator_1 = new JSeparator();
		
		JTabbedPane consoleTabbedPane = new JTabbedPane(JTabbedPane.TOP);
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addComponent(consoleTabbedPane, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 444, Short.MAX_VALUE)
						.addGroup(Alignment.TRAILING, gl_panel.createSequentialGroup()
							.addComponent(scrollPane_1, GroupLayout.DEFAULT_SIZE, 438, Short.MAX_VALUE)
							.addContainerGap())
						.addGroup(Alignment.TRAILING, gl_panel.createSequentialGroup()
							.addComponent(addSensorButton, GroupLayout.PREFERRED_SIZE, 98, Short.MAX_VALUE)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(editSensorButton, GroupLayout.PREFERRED_SIZE, 99, Short.MAX_VALUE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(deleteSensorButton, GroupLayout.PREFERRED_SIZE, 106, Short.MAX_VALUE)
							.addGap(123))
						.addGroup(Alignment.TRAILING, gl_panel.createSequentialGroup()
							.addComponent(separator_1, GroupLayout.DEFAULT_SIZE, 438, Short.MAX_VALUE)
							.addContainerGap())))
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(addSensorButton)
						.addComponent(deleteSensorButton)
						.addComponent(editSensorButton))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(scrollPane_1, GroupLayout.PREFERRED_SIZE, 71, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(separator_1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(consoleTabbedPane, GroupLayout.DEFAULT_SIZE, 193, Short.MAX_VALUE)
					.addContainerGap())
		);
		
		JPanel sensorConsolePane = new JPanel();
		JPanel sensorDebugConsolePane = new JPanel();
		consoleTabbedPane.addTab("Info", null, sensorConsolePane, null);
		consoleTabbedPane.addTab("Debug", null, sensorDebugConsolePane, null);
		
		JButton clearSensorInfoButton = new JButton("Clear");
		clearSensorInfoButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sensorInfoConsole.setText("");
			}
		});
		
		JButton clearSensorDebugButton = new JButton("Clear");
		clearSensorDebugButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sensorDebugConsole.setText("");
			}
		});
		
		JScrollPane scrollPane_2 = new JScrollPane();
		GroupLayout gl_sensorConsolePane = new GroupLayout(sensorConsolePane);
		gl_sensorConsolePane.setHorizontalGroup(
			gl_sensorConsolePane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_sensorConsolePane.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_sensorConsolePane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_sensorConsolePane.createSequentialGroup()
							.addComponent(clearSensorInfoButton)
							.addGap(15))
						.addGroup(Alignment.TRAILING, gl_sensorConsolePane.createSequentialGroup()
							.addComponent(scrollPane_2, GroupLayout.DEFAULT_SIZE, 411, Short.MAX_VALUE)
							.addContainerGap())))
		);
		gl_sensorConsolePane.setVerticalGroup(
			gl_sensorConsolePane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_sensorConsolePane.createSequentialGroup()
					.addContainerGap()
					.addComponent(clearSensorInfoButton)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(scrollPane_2, GroupLayout.DEFAULT_SIZE, 165, Short.MAX_VALUE)
					.addContainerGap())
		);
		
		
		JScrollPane scrollPane_3 = new JScrollPane();
		GroupLayout gl_sensorConsolePane_2 = new GroupLayout(sensorDebugConsolePane);
		gl_sensorConsolePane_2.setHorizontalGroup(
				gl_sensorConsolePane_2.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_sensorConsolePane_2.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_sensorConsolePane_2.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_sensorConsolePane_2.createSequentialGroup()
							.addComponent(clearSensorDebugButton)
							.addGap(15))
						.addGroup(Alignment.TRAILING, gl_sensorConsolePane_2.createSequentialGroup()
							.addComponent(scrollPane_3, GroupLayout.DEFAULT_SIZE, 411, Short.MAX_VALUE)
							.addContainerGap())))
		);
		gl_sensorConsolePane_2.setVerticalGroup(
				gl_sensorConsolePane_2.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_sensorConsolePane_2.createSequentialGroup()
					.addContainerGap()
					.addComponent(clearSensorDebugButton)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(scrollPane_3, GroupLayout.DEFAULT_SIZE, 165, Short.MAX_VALUE)
					.addContainerGap())
		);
		
		
		sensorInfoConsole = new JTextArea();
		sensorInfoConsole.setColumns(1);
		sensorInfoConsole.setForeground(Color.GREEN);
		sensorInfoConsole.setBackground(Color.BLACK);
		sensorInfoConsole.setFont(new Font("Lucida Sans Typewriter", Font.PLAIN, 11));
		sensorInfoConsole.setEditable(false);
		scrollPane_2.setViewportView(sensorInfoConsole);
		sensorConsolePane.setLayout(gl_sensorConsolePane);
		((DefaultCaret) sensorInfoConsole.getCaret()).setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		
		sensorDebugConsole = new JTextArea();
		sensorDebugConsole.setColumns(1);
		sensorDebugConsole.setForeground(Color.CYAN);
		sensorDebugConsole.setBackground(Color.BLACK);
		sensorDebugConsole.setFont(new Font("Lucida Sans Typewriter", Font.PLAIN, 11));
		sensorDebugConsole.setEditable(false);
		scrollPane_3.setViewportView(sensorDebugConsole);
		sensorDebugConsolePane.setLayout(gl_sensorConsolePane_2);
		((DefaultCaret) sensorDebugConsole.getCaret()).setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		
		Endpoint e1 = new Endpoint("E1",this);
		e1.addSensor(new TemperatureSensor("T1"));
		e1.addSensor(new HumiditySensor("H1"));
		
		sensorTableModel = new SensorTableModel();
		sensorsTable = new JTable(sensorTableModel);
		sensorsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scrollPane_1.setViewportView(sensorsTable);
		panel.setLayout(gl_panel);

		endpointTableModel = new EndpointTableModel();
		endpointTableModel.addEndpoint(e1);
		endpointTableModel.addEndpoint(new Endpoint("E2",this));
		endPointsTable = new JTable(endpointTableModel);
		
		
		endPointsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scrollPane.setViewportView(endPointsTable);
		endpointsPanel.setLayout(gl_endpointsPanel);
		

		endPointsTable.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
	        public void valueChanged(ListSelectionEvent event) {
	        	if(endPointsTable.getSelectedRow()==-1) {
	        		return;
	        	}
	            Endpoint endpoint = endpointTableModel.getEndpoint(endPointsTable.getSelectedRow());
	            sensorTableModel.setEndpoint(endpoint);
	            refreshEndpointDetails(endpoint);
	            editEndpointButton.setEnabled(true);
	            removeEndpointButton.setEnabled(true);
	            addSensorButton.setEnabled(true);
	        }
	    });
		sensorsTable.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
	        public void valueChanged(ListSelectionEvent event) {
	        	if(sensorsTable.getSelectedRow()==-1) {
	        		return;
	        	}
	        	deleteSensorButton.setEnabled(true);
				editSensorButton.setEnabled(true);
	        }
		});
		
		removeEndpointButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Endpoint endpoint = endpointTableModel.removeEndpoint(endPointsTable.getSelectedRow());
				endpoint.stop();
				endPointsTable.getSelectionModel().clearSelection();
				removeEndpointButton.setEnabled(false);
				editEndpointButton.setEnabled(false);
				addSensorButton.setEnabled(false);
				endpointIdLabel.setText("");
				sensorTableModel.clear();
			}
		});
		
		deleteSensorButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Endpoint endpoint = sensorTableModel.getEndpoint();
				endpoint.removeSensor(sensorsTable.getSelectedRow());
				sensorsTable.getSelectionModel().clearSelection();
				deleteSensorButton.setEnabled(false);
				editSensorButton.setEnabled(false);
				
			}
		});
	}
	
	public void refreshEndpointDetails(Endpoint endpoint) {
		endpointIdLabel.setText(endpoint.getId());
	}

	public JFrame getFrame() {
		return frmSensorNetworkSimulation;
	}

	@Override
	public void info(SensorData data) {
		StringBuilder builder = new StringBuilder();
		builder.append(dateFormatter.format(new Date(System.currentTimeMillis())) );
		builder.append(" - ID ");
		builder.append(data.getSensorId());
		builder.append(", Value: ");
		builder.append(data.getValue());
		builder.append(System.lineSeparator());
		sensorInfoConsole.append(builder.toString());
		sensorInfoConsole.setCaretPosition(sensorInfoConsole.getDocument().getLength());
	}

	@Override
	public void debug(String sensorId,String msg) {
		StringBuilder builder = new StringBuilder();
		builder.append(dateFormatter.format(new Date(System.currentTimeMillis())) );
		builder.append(" - ID ");
		builder.append(sensorId);
		builder.append(", Message: ");
		builder.append(msg);
		builder.append(System.lineSeparator());
		sensorDebugConsole.append(builder.toString());
		sensorDebugConsole.setCaretPosition(sensorDebugConsole.getDocument().getLength());
	}
}
