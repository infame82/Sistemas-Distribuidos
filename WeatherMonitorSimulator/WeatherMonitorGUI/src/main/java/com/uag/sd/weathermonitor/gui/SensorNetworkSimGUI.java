package com.uag.sd.weathermonitor.gui;

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
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.DefaultCaret;

import org.springframework.stereotype.Component;

import com.uag.sd.weathermonitor.model.endpoint.Endpoint;
import com.uag.sd.weathermonitor.model.endpoint.EndpointData;
import com.uag.sd.weathermonitor.model.logs.DeviceLog;
import com.uag.sd.weathermonitor.model.sensor.HumiditySensor;
import com.uag.sd.weathermonitor.model.sensor.Sensor;
import com.uag.sd.weathermonitor.model.sensor.SensorData;
import com.uag.sd.weathermonitor.model.sensor.TemperatureSensor;

@Component("sensorNetworkSimulationGUI")
public class SensorNetworkSimGUI {
	
	class EndpointLog implements DeviceLog<EndpointData>{

		@Override
		public void info(EndpointData msg) {
			StringBuilder builder = new StringBuilder();
			builder.append(dateFormatter.format(new Date(System.currentTimeMillis())) );
			builder.append(" - ID ");
			builder.append(msg.getEndpointId());
			builder.append(", Value: ");
			builder.append(msg.getValue());
			builder.append(System.lineSeparator());
			endpointInfoConsole.append(builder.toString());
			endpointInfoConsole.setCaretPosition(sensorInfoConsole.getDocument().getLength());
		}

		@Override
		public void debug(EndpointData msg) {
			StringBuilder builder = new StringBuilder();
			builder.append(dateFormatter.format(new Date(System.currentTimeMillis())) );
			builder.append(" - ID ");
			builder.append(msg.getEndpointId());
			builder.append(", Message: ");
			builder.append(msg.getValue());
			builder.append(System.lineSeparator());
			endpointDebugConsole.append(builder.toString());
			endpointDebugConsole.setCaretPosition(sensorDebugConsole.getDocument().getLength());
		}
		
	}
	
	class SensorLog implements DeviceLog<SensorData>{
		@Override
		public void info(SensorData msg) {
			StringBuilder builder = new StringBuilder();
			builder.append(dateFormatter.format(new Date(System.currentTimeMillis())) );
			builder.append(" - ID ");
			builder.append(msg.getSensorId());
			builder.append(", Value: ");
			builder.append(msg.getValue());
			builder.append(System.lineSeparator());
			sensorInfoConsole.append(builder.toString());
			sensorInfoConsole.setCaretPosition(sensorInfoConsole.getDocument().getLength());
		}

		@Override
		public void debug(SensorData msg) {
			StringBuilder builder = new StringBuilder();
			builder.append(dateFormatter.format(new Date(System.currentTimeMillis())) );
			builder.append(" - ID ");
			builder.append(msg.getSensorId());
			builder.append(", Message: ");
			builder.append(msg.getValue());
			builder.append(System.lineSeparator());
			sensorDebugConsole.append(builder.toString());
			sensorDebugConsole.setCaretPosition(sensorDebugConsole.getDocument().getLength());
		}
		
	}
 
	private JFrame frmSensorNetworkSimulation;
	private JTable endPointsTable;
	private JTable sensorsTable;
	private EndpointTableModel endpointTableModel;
	private SensorTableModel sensorTableModel;
	private JTextArea sensorInfoConsole;
	private JTextArea sensorDebugConsole;
	private JTextArea endpointInfoConsole;
	private JTextArea endpointDebugConsole;
	private SimpleDateFormat dateFormatter;
	private SensorLog sensorLog;
	private EndpointLog endpointLog;

	/**
	 * Create the application.
	 */
	public SensorNetworkSimGUI() {
		dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		sensorLog = new SensorLog();
		endpointLog = new EndpointLog();
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
		frmSensorNetworkSimulation.setBounds(100, 100, 612, 832);
		frmSensorNetworkSimulation.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		Endpoint e1 = new Endpoint("E1",sensorLog,endpointLog);
		e1.addSensor(new TemperatureSensor("T1"));
		e1.addSensor(new HumiditySensor("H1"));
		
		sensorTableModel = new SensorTableModel();

		endpointTableModel = new EndpointTableModel();
		endpointTableModel.addEndpoint(e1);
		endpointTableModel.addEndpoint(new Endpoint("E2",sensorLog,endpointLog));
														
														JTabbedPane appTabbedPane = new JTabbedPane(JTabbedPane.TOP);
														
																JPanel endpointsPanel = new JPanel();
																appTabbedPane.addTab("Endpoints", null, endpointsPanel, null);
																		
																				JButton addEndpointButton = new JButton("Add");
																				addEndpointButton.addActionListener(new ActionListener() {
																					public void actionPerformed(ActionEvent e) {
																						try {
																							EndpointDialogGUI dialog = new EndpointDialogGUI(null,endpointTableModel,sensorLog);
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
																									EndpointDialogGUI dialog = new EndpointDialogGUI(endpoint,endpointTableModel,sensorLog);
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
																										
																										JTabbedPane tabbedPane_1 = new JTabbedPane(JTabbedPane.TOP);
																										GroupLayout gl_endpointsPanel = new GroupLayout(endpointsPanel);
																										gl_endpointsPanel.setHorizontalGroup(
																											gl_endpointsPanel.createParallelGroup(Alignment.LEADING)
																												.addGroup(Alignment.TRAILING, gl_endpointsPanel.createSequentialGroup()
																													.addGroup(gl_endpointsPanel.createParallelGroup(Alignment.TRAILING)
																														.addGroup(Alignment.LEADING, gl_endpointsPanel.createSequentialGroup()
																															.addContainerGap()
																															.addComponent(tabbedPane_1, GroupLayout.DEFAULT_SIZE, 580, Short.MAX_VALUE))
																														.addGroup(Alignment.LEADING, gl_endpointsPanel.createSequentialGroup()
																															.addContainerGap()
																															.addComponent(addEndpointButton)
																															.addPreferredGap(ComponentPlacement.RELATED)
																															.addComponent(editEndpointButton)
																															.addPreferredGap(ComponentPlacement.RELATED)
																															.addComponent(removeEndpointButton)
																															.addGap(6)
																															.addComponent(startEndpointsButton)
																															.addGap(6)
																															.addComponent(btnStopAll))
																														.addGroup(Alignment.LEADING, gl_endpointsPanel.createSequentialGroup()
																															.addContainerGap()
																															.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 580, Short.MAX_VALUE))
																														.addComponent(tabbedPane, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 586, Short.MAX_VALUE)
																														.addGroup(gl_endpointsPanel.createSequentialGroup()
																															.addContainerGap()
																															.addComponent(separator, GroupLayout.DEFAULT_SIZE, 586, Short.MAX_VALUE)))
																													.addContainerGap())
																										);
																										gl_endpointsPanel.setVerticalGroup(
																											gl_endpointsPanel.createParallelGroup(Alignment.LEADING)
																												.addGroup(gl_endpointsPanel.createSequentialGroup()
																													.addContainerGap()
																													.addGroup(gl_endpointsPanel.createParallelGroup(Alignment.BASELINE)
																														.addComponent(addEndpointButton)
																														.addComponent(editEndpointButton)
																														.addComponent(removeEndpointButton)
																														.addComponent(startEndpointsButton)
																														.addComponent(btnStopAll))
																													.addPreferredGap(ComponentPlacement.RELATED)
																													.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 74, GroupLayout.PREFERRED_SIZE)
																													.addPreferredGap(ComponentPlacement.RELATED)
																													.addComponent(tabbedPane_1, GroupLayout.DEFAULT_SIZE, 173, Short.MAX_VALUE)
																													.addPreferredGap(ComponentPlacement.RELATED)
																													.addComponent(separator, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
																													.addPreferredGap(ComponentPlacement.RELATED)
																													.addComponent(tabbedPane, GroupLayout.PREFERRED_SIZE, 394, GroupLayout.PREFERRED_SIZE))
																										);
																										
																										JPanel panel_1 = new JPanel();
																										tabbedPane_1.addTab("Info", null, panel_1, null);
																										
																										JButton endpointInfoConsoleClearBtn = new JButton("Clear");
																										
																										JScrollPane scrollPane_4 = new JScrollPane();
																										GroupLayout gl_panel_1 = new GroupLayout(panel_1);
																										gl_panel_1.setHorizontalGroup(
																											gl_panel_1.createParallelGroup(Alignment.LEADING)
																												.addGroup(gl_panel_1.createSequentialGroup()
																													.addGroup(gl_panel_1.createParallelGroup(Alignment.LEADING)
																														.addComponent(endpointInfoConsoleClearBtn)
																														.addGroup(Alignment.TRAILING, gl_panel_1.createSequentialGroup()
																															.addContainerGap()
																															.addComponent(scrollPane_4, GroupLayout.DEFAULT_SIZE, 547, Short.MAX_VALUE)))
																													.addContainerGap())
																										);
																										gl_panel_1.setVerticalGroup(
																											gl_panel_1.createParallelGroup(Alignment.LEADING)
																												.addGroup(gl_panel_1.createSequentialGroup()
																													.addContainerGap()
																													.addComponent(endpointInfoConsoleClearBtn)
																													.addPreferredGap(ComponentPlacement.RELATED)
																													.addComponent(scrollPane_4, GroupLayout.DEFAULT_SIZE, 126, Short.MAX_VALUE)
																													.addContainerGap())
																										);
																										
																										endpointInfoConsole = new JTextArea();
																										endpointInfoConsole.setFont(new Font("Lucida Sans Typewriter", Font.PLAIN, 11));
																										endpointInfoConsole.setBackground(Color.BLACK);
																										endpointInfoConsole.setEditable(false);
																										endpointInfoConsole.setForeground(Color.GREEN);
																										scrollPane_4.setViewportView(endpointInfoConsole);
																										panel_1.setLayout(gl_panel_1);
																										
																										JPanel panel_2 = new JPanel();
																										tabbedPane_1.addTab("Debug", null, panel_2, null);
																										
																										JButton endpointDebugClearBtn = new JButton("Clear");
																										
																										JScrollPane scrollPane_5 = new JScrollPane();
																										GroupLayout gl_panel_2 = new GroupLayout(panel_2);
																										gl_panel_2.setHorizontalGroup(
																											gl_panel_2.createParallelGroup(Alignment.LEADING)
																												.addGroup(gl_panel_2.createSequentialGroup()
																													.addGroup(gl_panel_2.createParallelGroup(Alignment.LEADING)
																														.addComponent(endpointDebugClearBtn)
																														.addGroup(gl_panel_2.createSequentialGroup()
																															.addGap(6)
																															.addComponent(scrollPane_5, GroupLayout.DEFAULT_SIZE, 547, Short.MAX_VALUE)))
																													.addContainerGap())
																										);
																										gl_panel_2.setVerticalGroup(
																											gl_panel_2.createParallelGroup(Alignment.LEADING)
																												.addGroup(gl_panel_2.createSequentialGroup()
																													.addContainerGap()
																													.addComponent(endpointDebugClearBtn)
																													.addPreferredGap(ComponentPlacement.RELATED)
																													.addComponent(scrollPane_5, GroupLayout.DEFAULT_SIZE, 126, Short.MAX_VALUE)
																													.addContainerGap())
																										);
																										
																										endpointDebugConsole = new JTextArea();
																										endpointDebugConsole.setFont(new Font("Lucida Sans Typewriter", Font.PLAIN, 11));
																										endpointDebugConsole.setBackground(Color.BLACK);
																										endpointDebugConsole.setEditable(false);
																										endpointDebugConsole.setForeground(Color.CYAN);
																										scrollPane_5.setViewportView(endpointDebugConsole);
																										panel_2.setLayout(gl_panel_2);
																										
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
																															.addContainerGap())))
																												.addGroup(gl_panel.createSequentialGroup()
																													.addComponent(addSensorButton, GroupLayout.PREFERRED_SIZE, 98, Short.MAX_VALUE)
																													.addPreferredGap(ComponentPlacement.UNRELATED)
																													.addComponent(editSensorButton, GroupLayout.PREFERRED_SIZE, 99, Short.MAX_VALUE)
																													.addPreferredGap(ComponentPlacement.RELATED)
																													.addComponent(deleteSensorButton, GroupLayout.PREFERRED_SIZE, 106, Short.MAX_VALUE)
																													.addGap(129))
																										);
																										gl_panel.setVerticalGroup(
																											gl_panel.createParallelGroup(Alignment.LEADING)
																												.addGroup(Alignment.TRAILING, gl_panel.createSequentialGroup()
																													.addContainerGap(66, Short.MAX_VALUE)
																													.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
																														.addComponent(addSensorButton)
																														.addComponent(deleteSensorButton)
																														.addComponent(editSensorButton))
																													.addPreferredGap(ComponentPlacement.RELATED)
																													.addComponent(scrollPane_1, GroupLayout.PREFERRED_SIZE, 71, GroupLayout.PREFERRED_SIZE)
																													.addPreferredGap(ComponentPlacement.RELATED)
																													.addComponent(consoleTabbedPane, GroupLayout.PREFERRED_SIZE, 216, GroupLayout.PREFERRED_SIZE)
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
																														.addComponent(scrollPane_2, GroupLayout.DEFAULT_SIZE, 411, Short.MAX_VALUE)
																														.addComponent(clearSensorInfoButton))
																													.addContainerGap())
																										);
																										gl_sensorConsolePane.setVerticalGroup(
																											gl_sensorConsolePane.createParallelGroup(Alignment.TRAILING)
																												.addGroup(gl_sensorConsolePane.createSequentialGroup()
																													.addContainerGap(55, Short.MAX_VALUE)
																													.addComponent(clearSensorInfoButton)
																													.addPreferredGap(ComponentPlacement.RELATED)
																													.addComponent(scrollPane_2, GroupLayout.PREFERRED_SIZE, 116, GroupLayout.PREFERRED_SIZE)
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
																															.addComponent(scrollPane_3, GroupLayout.DEFAULT_SIZE, 411, Short.MAX_VALUE)
																															.addContainerGap())
																														.addGroup(gl_sensorConsolePane_2.createSequentialGroup()
																															.addComponent(clearSensorDebugButton)
																															.addGap(15))))
																										);
																										gl_sensorConsolePane_2.setVerticalGroup(
																											gl_sensorConsolePane_2.createParallelGroup(Alignment.LEADING)
																												.addGroup(gl_sensorConsolePane_2.createSequentialGroup()
																													.addContainerGap()
																													.addComponent(clearSensorDebugButton)
																													.addPreferredGap(ComponentPlacement.RELATED, 55, Short.MAX_VALUE)
																													.addComponent(scrollPane_3, GroupLayout.PREFERRED_SIZE, 116, GroupLayout.PREFERRED_SIZE)
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
																										sensorsTable = new JTable(sensorTableModel);
																										sensorsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
																										scrollPane_1.setViewportView(sensorsTable);
																										panel.setLayout(gl_panel);
																										endPointsTable = new JTable(endpointTableModel);
																										
																										
																										endPointsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
																										scrollPane.setViewportView(endPointsTable);
																										endpointsPanel.setLayout(gl_endpointsPanel);
																										GroupLayout groupLayout = new GroupLayout(frmSensorNetworkSimulation.getContentPane());
																										groupLayout.setHorizontalGroup(
																											groupLayout.createParallelGroup(Alignment.LEADING)
																												.addGroup(groupLayout.createSequentialGroup()
																													.addComponent(appTabbedPane, GroupLayout.PREFERRED_SIZE, 613, GroupLayout.PREFERRED_SIZE)
																													.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
																										);
																										groupLayout.setVerticalGroup(
																											groupLayout.createParallelGroup(Alignment.TRAILING)
																												.addGroup(Alignment.LEADING, groupLayout.createSequentialGroup()
																													.addContainerGap()
																													.addComponent(appTabbedPane, GroupLayout.DEFAULT_SIZE, 804, Short.MAX_VALUE))
																										);
																										frmSensorNetworkSimulation.getContentPane().setLayout(groupLayout);
		

		endPointsTable.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
	        public void valueChanged(ListSelectionEvent event) {
	        	if(endPointsTable.getSelectedRow()==-1) {
	        		return;
	        	}
	            Endpoint endpoint = endpointTableModel.getEndpoint(endPointsTable.getSelectedRow());
	            sensorTableModel.setEndpoint(endpoint);
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
	


	public JFrame getFrame() {
		return frmSensorNetworkSimulation;
	}
	
	

	
}
