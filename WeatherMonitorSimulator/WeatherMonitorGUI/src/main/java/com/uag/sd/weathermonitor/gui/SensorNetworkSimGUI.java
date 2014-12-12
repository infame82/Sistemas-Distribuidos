package com.uag.sd.weathermonitor.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
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

import com.uag.sd.weathermonitor.gui.models.CoordinatorTableModel;
import com.uag.sd.weathermonitor.gui.models.EndpointRouterTableModel;
import com.uag.sd.weathermonitor.gui.models.EndpointTableModel;
import com.uag.sd.weathermonitor.gui.models.RouterTableModel;
import com.uag.sd.weathermonitor.gui.models.SensorTableModel;
import com.uag.sd.weathermonitor.model.device.Beacon;
import com.uag.sd.weathermonitor.model.device.DeviceData;
import com.uag.sd.weathermonitor.model.device.DeviceLog;
import com.uag.sd.weathermonitor.model.device.ZigBeeCoordinator;
import com.uag.sd.weathermonitor.model.device.ZigBeeEndpoint;
import com.uag.sd.weathermonitor.model.device.ZigBeeRouter;
import com.uag.sd.weathermonitor.model.layer.physical.channel.RFChannel;
import com.uag.sd.weathermonitor.model.sensor.Sensor;


public class SensorNetworkSimGUI {

	class EndpointLog implements DeviceLog {

		@Override
		public void info(DeviceData msg) {
			StringBuilder builder = new StringBuilder();
			builder.append(dateFormatter.format(new Date(System
					.currentTimeMillis())));
			builder.append(" - ID ");
			builder.append(msg.getDeviceId());
			builder.append(", Value: ");
			builder.append(msg.getData());
			builder.append(System.lineSeparator());
			endpointInfoConsole.append(builder.toString());
			endpointInfoConsole.setCaretPosition(endpointInfoConsole
					.getDocument().getLength());
		}

		@Override
		public void debug(DeviceData msg) {
			StringBuilder builder = new StringBuilder();
			builder.append(dateFormatter.format(new Date(System
					.currentTimeMillis())));
			builder.append(" - ID ");
			builder.append(msg.getDeviceId());
			builder.append(", Message: ");
			builder.append(msg.getData());
			builder.append(System.lineSeparator());
			endpointDebugConsole.append(builder.toString());
			endpointDebugConsole.setCaretPosition(endpointDebugConsole
					.getDocument().getLength());
		}

	}

	class SensorLog implements DeviceLog {
		@Override
		public void info(DeviceData msg) {
			StringBuilder builder = new StringBuilder();
			builder.append(dateFormatter.format(new Date(System
					.currentTimeMillis())));
			builder.append(" - ID ");
			builder.append(msg.getDeviceId());
			builder.append(", Value: ");
			builder.append(msg.getData());
			builder.append(System.lineSeparator());
			sensorInfoConsole.append(builder.toString());
			sensorInfoConsole.setCaretPosition(sensorInfoConsole.getDocument()
					.getLength());
		}

		@Override
		public void debug(DeviceData msg) {
			StringBuilder builder = new StringBuilder();
			builder.append(dateFormatter.format(new Date(System
					.currentTimeMillis())));
			builder.append(" - ID ");
			builder.append(msg.getDeviceId());
			builder.append(", Message: ");
			builder.append(msg.getData());
			builder.append(System.lineSeparator());
			sensorDebugConsole.append(builder.toString());
			sensorDebugConsole.setCaretPosition(sensorDebugConsole
					.getDocument().getLength());
		}

	}

	class RouterLog implements DeviceLog {

		@Override
		public void info(DeviceData msg) {
			StringBuilder builder = new StringBuilder();
			builder.append(dateFormatter.format(new Date(System
					.currentTimeMillis())));
			builder.append(" - ID ");
			builder.append(msg.getDeviceId());
			builder.append(", Value: ");
			builder.append(msg.getData());
			builder.append(System.lineSeparator());
			routerInfoConsole.append(builder.toString());
			routerInfoConsole.setCaretPosition(routerInfoConsole.getDocument()
					.getLength());
		}

		@Override
		public void debug(DeviceData msg) {
			StringBuilder builder = new StringBuilder();
			builder.append(dateFormatter.format(new Date(System
					.currentTimeMillis())));
			builder.append(" - ID ");
			builder.append(msg.getDeviceId());
			builder.append(", Message: ");
			builder.append(msg.getData());
			builder.append(System.lineSeparator());
			routerDebugConsole.append(builder.toString());
			routerDebugConsole.setCaretPosition(routerDebugConsole
					.getDocument().getLength());
		}

	}

	class CoordinatorLog implements DeviceLog{

		@Override
		public void info(DeviceData msg) {
			StringBuilder builder = new StringBuilder();
			builder.append(dateFormatter.format(new Date(System
					.currentTimeMillis())));
			builder.append(" - ID ");
			builder.append(msg.getDeviceId());
			builder.append(", Value: ");
			builder.append(msg.getData());
			builder.append(System.lineSeparator());
			coordinatorInfoConsole.append(builder.toString());
			coordinatorInfoConsole.setCaretPosition(coordinatorInfoConsole.getDocument()
					.getLength());
		}

		@Override
		public void debug(DeviceData msg) {
			StringBuilder builder = new StringBuilder();
			builder.append(dateFormatter.format(new Date(System
					.currentTimeMillis())));
			builder.append(" - ID ");
			builder.append(msg.getDeviceId());
			builder.append(", Message: ");
			builder.append(msg.getData());
			builder.append(System.lineSeparator());
			coordinatorDebugConsole.append(builder.toString());
			coordinatorDebugConsole.setCaretPosition(coordinatorDebugConsole
					.getDocument().getLength());
		}
		
	}
	
	private JFrame frmSensorNetworkSimulation;
	private JTable endPointsTable;
	private JTable sensorsTable;
	private CoordinatorTableModel coordinatorTableModel;
	private RouterTableModel routerTableModel;
	private EndpointTableModel endpointTableModel;
	private SensorTableModel sensorTableModel;
	private EndpointRouterTableModel endpointRouterTableModel;
	private JTextArea sensorInfoConsole;
	private JTextArea sensorDebugConsole;
	private JTextArea endpointInfoConsole;
	private JTextArea endpointDebugConsole;
	private JTextArea routerInfoConsole;
	private JTextArea routerDebugConsole;

	private SimpleDateFormat dateFormatter;
	private SensorLog sensorLog;
	private EndpointLog endpointLog;
	private RouterLog routerLog;
	private CoordinatorLog coordinatorLog;
	private JTable endpointRoutersTable;
	private JTable routersTable;
	private JTable routerEndpointTable;
	private JTable routerRoutersTable;
	private JTable coordinatorsTable;
	private JTextArea coordinatorInfoConsole;
	private JTextArea coordinatorDebugConsole;

	/**
	 * Create the application.
	 */
	public SensorNetworkSimGUI() {
		dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		sensorLog = new SensorLog();
		endpointLog = new EndpointLog();
		routerLog = new RouterLog();
		coordinatorLog = new CoordinatorLog();
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmSensorNetworkSimulation = new JFrame();
		frmSensorNetworkSimulation.addWindowListener(new WindowListener() {

			@Override
			public void windowOpened(WindowEvent e) {
			}

			@Override
			public void windowIconified(WindowEvent e) {
			}

			@Override
			public void windowDeiconified(WindowEvent e) {
			}

			@Override
			public void windowDeactivated(WindowEvent e) {
			}

			@Override
			public void windowClosing(WindowEvent e) {
				endpointTableModel.stopAll();
			}

			@Override
			public void windowClosed(WindowEvent e) {
			}

			@Override
			public void windowActivated(WindowEvent e) {
			}
		});
		frmSensorNetworkSimulation.setTitle("Sensor Network Simulation");
		frmSensorNetworkSimulation.setBounds(100, 100, 939, 832);
		frmSensorNetworkSimulation
				.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		sensorTableModel = new SensorTableModel();
		endpointTableModel = new EndpointTableModel();
		endpointRouterTableModel = new EndpointRouterTableModel();
		routerTableModel = new RouterTableModel();
		coordinatorTableModel = new CoordinatorTableModel();
		try {
			//ZigBeeEndpoint e1 = new ZigBeeEndpoint("E1", sensorLog, endpointLog);
			//e1.addSensor(new TemperatureSensor("T1"));
			//e1.addSensor(new HumiditySensor("H1"));
			//e1.addRouter(new ZigBeeRouter("R1", null));
			//endpointTableModel.addEndpoint(e1);
			//endpointTableModel.addEndpoint(new ZigBeeEndpoint("E2", sensorLog,
			//		endpointLog));
		} catch (Exception e) {
			JOptionPane.showMessageDialog(frmSensorNetworkSimulation,
					"Unable initialize defualt values: " + e.getMessage());
		}

		

		JTabbedPane appTabbedPane = new JTabbedPane(JTabbedPane.TOP);

		JPanel endpointsPanel = new JPanel();
		appTabbedPane.addTab("Endpoints", null, endpointsPanel, null);

		JButton addEndpointButton = new JButton("Add");
		addEndpointButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					EndpointDialogGUI dialog = new EndpointDialogGUI(null,
							endpointTableModel, sensorLog,endpointLog);
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
					ZigBeeEndpoint zigBeeEndpoint = endpointTableModel
							.get(endPointsTable.getSelectedRow());
					EndpointDialogGUI dialog = new EndpointDialogGUI(
							zigBeeEndpoint, endpointTableModel, sensorLog,endpointLog);
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

		JTabbedPane tabbedPane_1 = new JTabbedPane(JTabbedPane.TOP);
		
		JButton ntwDiscoveryEndpointBtn = new JButton("Network Discovery");
		ntwDiscoveryEndpointBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ZigBeeEndpoint endpoint = endpointTableModel
						.get(endPointsTable.getSelectedRow());
				Map<RFChannel, List<Beacon>>  networksMap = endpoint.networkDiscovery();
				NetworksDialogGUI gui = new NetworksDialogGUI(endpoint,networksMap);
				gui.setTitle("Networks for "+endpoint.getId());
				gui.setVisible(true);
			}
		});
		ntwDiscoveryEndpointBtn.setEnabled(false);
		GroupLayout gl_endpointsPanel = new GroupLayout(endpointsPanel);
		gl_endpointsPanel.setHorizontalGroup(
			gl_endpointsPanel.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_endpointsPanel.createSequentialGroup()
					.addGroup(gl_endpointsPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_endpointsPanel.createSequentialGroup()
							.addContainerGap()
							.addComponent(tabbedPane_1, GroupLayout.DEFAULT_SIZE, 900, Short.MAX_VALUE))
						.addGroup(gl_endpointsPanel.createSequentialGroup()
							.addContainerGap()
							.addComponent(addEndpointButton)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(editEndpointButton)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(removeEndpointButton)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(ntwDiscoveryEndpointBtn))
						.addGroup(gl_endpointsPanel.createSequentialGroup()
							.addContainerGap()
							.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 900, Short.MAX_VALUE))
						.addComponent(tabbedPane, GroupLayout.DEFAULT_SIZE, 906, Short.MAX_VALUE)
						.addGroup(gl_endpointsPanel.createSequentialGroup()
							.addContainerGap()
							.addComponent(separator, GroupLayout.DEFAULT_SIZE, 900, Short.MAX_VALUE)))
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
						.addComponent(ntwDiscoveryEndpointBtn))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 74, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(tabbedPane_1)
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
		gl_panel_1
				.setHorizontalGroup(gl_panel_1
						.createParallelGroup(Alignment.LEADING)
						.addGroup(
								gl_panel_1
										.createSequentialGroup()
										.addGroup(
												gl_panel_1
														.createParallelGroup(
																Alignment.LEADING)
														.addComponent(
																endpointInfoConsoleClearBtn)
														.addGroup(
																Alignment.TRAILING,
																gl_panel_1
																		.createSequentialGroup()
																		.addContainerGap()
																		.addComponent(
																				scrollPane_4,
																				GroupLayout.DEFAULT_SIZE,
																				547,
																				Short.MAX_VALUE)))
										.addContainerGap()));
		gl_panel_1.setVerticalGroup(gl_panel_1.createParallelGroup(
				Alignment.LEADING).addGroup(
				gl_panel_1
						.createSequentialGroup()
						.addContainerGap()
						.addComponent(endpointInfoConsoleClearBtn)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(scrollPane_4, GroupLayout.DEFAULT_SIZE,
								126, Short.MAX_VALUE).addContainerGap()));

		endpointInfoConsole = new JTextArea();
		endpointInfoConsole.setFont(new Font("Lucida Sans Typewriter",
				Font.PLAIN, 11));
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
		gl_panel_2
				.setHorizontalGroup(gl_panel_2
						.createParallelGroup(Alignment.LEADING)
						.addGroup(
								gl_panel_2
										.createSequentialGroup()
										.addGroup(
												gl_panel_2
														.createParallelGroup(
																Alignment.LEADING)
														.addComponent(
																endpointDebugClearBtn)
														.addGroup(
																gl_panel_2
																		.createSequentialGroup()
																		.addGap(6)
																		.addComponent(
																				scrollPane_5,
																				GroupLayout.DEFAULT_SIZE,
																				547,
																				Short.MAX_VALUE)))
										.addContainerGap()));
		gl_panel_2.setVerticalGroup(gl_panel_2.createParallelGroup(
				Alignment.LEADING).addGroup(
				gl_panel_2
						.createSequentialGroup()
						.addContainerGap()
						.addComponent(endpointDebugClearBtn)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(scrollPane_5, GroupLayout.DEFAULT_SIZE,
								126, Short.MAX_VALUE).addContainerGap()));

		endpointDebugConsole = new JTextArea();
		endpointDebugConsole.setFont(new Font("Lucida Sans Typewriter",
				Font.PLAIN, 11));
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
					ZigBeeEndpoint zigBeeEndpoint = endpointTableModel
							.get(endPointsTable.getSelectedRow());
					SensorDialogGUI dialog = new SensorDialogGUI(zigBeeEndpoint,
							null, sensorTableModel);
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
					ZigBeeEndpoint zigBeeEndpoint = endpointTableModel
							.get(endPointsTable.getSelectedRow());
					Sensor sensor = zigBeeEndpoint.getSensors().get(
							sensorsTable.getSelectedRow());
					SensorDialogGUI dialog = new SensorDialogGUI(zigBeeEndpoint,
							sensor, sensorTableModel);
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
		gl_panel.setHorizontalGroup(gl_panel
				.createParallelGroup(Alignment.LEADING)
				.addGroup(
						gl_panel.createSequentialGroup()
								.addContainerGap()
								.addGroup(
										gl_panel.createParallelGroup(
												Alignment.LEADING)
												.addComponent(
														consoleTabbedPane,
														Alignment.TRAILING,
														GroupLayout.DEFAULT_SIZE,
														444, Short.MAX_VALUE)
												.addGroup(
														Alignment.TRAILING,
														gl_panel.createSequentialGroup()
																.addComponent(
																		scrollPane_1,
																		GroupLayout.DEFAULT_SIZE,
																		438,
																		Short.MAX_VALUE)
																.addContainerGap())))
				.addGroup(
						gl_panel.createSequentialGroup()
								.addComponent(addSensorButton,
										GroupLayout.PREFERRED_SIZE, 98,
										Short.MAX_VALUE)
								.addPreferredGap(ComponentPlacement.UNRELATED)
								.addComponent(editSensorButton,
										GroupLayout.PREFERRED_SIZE, 99,
										Short.MAX_VALUE)
								.addPreferredGap(ComponentPlacement.RELATED)
								.addComponent(deleteSensorButton,
										GroupLayout.PREFERRED_SIZE, 106,
										Short.MAX_VALUE).addGap(129)));
		gl_panel.setVerticalGroup(gl_panel
				.createParallelGroup(Alignment.LEADING)
				.addGroup(
						Alignment.TRAILING,
						gl_panel.createSequentialGroup()
								.addContainerGap(66, Short.MAX_VALUE)
								.addGroup(
										gl_panel.createParallelGroup(
												Alignment.BASELINE)
												.addComponent(addSensorButton)
												.addComponent(
														deleteSensorButton)
												.addComponent(editSensorButton))
								.addPreferredGap(ComponentPlacement.RELATED)
								.addComponent(scrollPane_1,
										GroupLayout.PREFERRED_SIZE, 71,
										GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(ComponentPlacement.RELATED)
								.addComponent(consoleTabbedPane,
										GroupLayout.PREFERRED_SIZE, 216,
										GroupLayout.PREFERRED_SIZE)
								.addContainerGap()));

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
		gl_sensorConsolePane
				.setHorizontalGroup(gl_sensorConsolePane
						.createParallelGroup(Alignment.LEADING)
						.addGroup(
								gl_sensorConsolePane
										.createSequentialGroup()
										.addContainerGap()
										.addGroup(
												gl_sensorConsolePane
														.createParallelGroup(
																Alignment.LEADING)
														.addComponent(
																scrollPane_2,
																GroupLayout.DEFAULT_SIZE,
																411,
																Short.MAX_VALUE)
														.addComponent(
																clearSensorInfoButton))
										.addContainerGap()));
		gl_sensorConsolePane.setVerticalGroup(gl_sensorConsolePane
				.createParallelGroup(Alignment.TRAILING).addGroup(
						gl_sensorConsolePane
								.createSequentialGroup()
								.addContainerGap(55, Short.MAX_VALUE)
								.addComponent(clearSensorInfoButton)
								.addPreferredGap(ComponentPlacement.RELATED)
								.addComponent(scrollPane_2,
										GroupLayout.PREFERRED_SIZE, 116,
										GroupLayout.PREFERRED_SIZE)
								.addContainerGap()));

		JScrollPane scrollPane_3 = new JScrollPane();
		GroupLayout gl_sensorConsolePane_2 = new GroupLayout(
				sensorDebugConsolePane);
		gl_sensorConsolePane_2
				.setHorizontalGroup(gl_sensorConsolePane_2
						.createParallelGroup(Alignment.LEADING)
						.addGroup(
								gl_sensorConsolePane_2
										.createSequentialGroup()
										.addContainerGap()
										.addGroup(
												gl_sensorConsolePane_2
														.createParallelGroup(
																Alignment.LEADING)
														.addGroup(
																gl_sensorConsolePane_2
																		.createSequentialGroup()
																		.addComponent(
																				scrollPane_3,
																				GroupLayout.DEFAULT_SIZE,
																				411,
																				Short.MAX_VALUE)
																		.addContainerGap())
														.addGroup(
																gl_sensorConsolePane_2
																		.createSequentialGroup()
																		.addComponent(
																				clearSensorDebugButton)
																		.addGap(15)))));
		gl_sensorConsolePane_2.setVerticalGroup(gl_sensorConsolePane_2
				.createParallelGroup(Alignment.LEADING).addGroup(
						gl_sensorConsolePane_2
								.createSequentialGroup()
								.addContainerGap()
								.addComponent(clearSensorDebugButton)
								.addPreferredGap(ComponentPlacement.RELATED,
										55, Short.MAX_VALUE)
								.addComponent(scrollPane_3,
										GroupLayout.PREFERRED_SIZE, 116,
										GroupLayout.PREFERRED_SIZE)
								.addContainerGap()));

		sensorInfoConsole = new JTextArea();
		sensorInfoConsole.setColumns(1);
		sensorInfoConsole.setForeground(Color.GREEN);
		sensorInfoConsole.setBackground(Color.BLACK);
		sensorInfoConsole.setFont(new Font("Lucida Sans Typewriter",
				Font.PLAIN, 11));
		sensorInfoConsole.setEditable(false);
		scrollPane_2.setViewportView(sensorInfoConsole);
		sensorConsolePane.setLayout(gl_sensorConsolePane);
		((DefaultCaret) sensorInfoConsole.getCaret())
				.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

		sensorDebugConsole = new JTextArea();
		sensorDebugConsole.setColumns(1);
		sensorDebugConsole.setForeground(Color.CYAN);
		sensorDebugConsole.setBackground(Color.BLACK);
		sensorDebugConsole.setFont(new Font("Lucida Sans Typewriter",
				Font.PLAIN, 11));
		sensorDebugConsole.setEditable(false);
		scrollPane_3.setViewportView(sensorDebugConsole);
		sensorDebugConsolePane.setLayout(gl_sensorConsolePane_2);
		((DefaultCaret) sensorDebugConsole.getCaret())
				.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		sensorsTable = new JTable(sensorTableModel);
		sensorsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scrollPane_1.setViewportView(sensorsTable);
		panel.setLayout(gl_panel);

		JPanel panel_4 = new JPanel();
		tabbedPane.addTab("Routers", null, panel_4, null);

		JScrollPane scrollPane_6 = new JScrollPane();
		GroupLayout gl_panel_4 = new GroupLayout(panel_4);
		gl_panel_4.setHorizontalGroup(gl_panel_4.createParallelGroup(
				Alignment.LEADING).addGroup(
				gl_panel_4
						.createSequentialGroup()
						.addContainerGap()
						.addComponent(scrollPane_6, GroupLayout.DEFAULT_SIZE,
								553, Short.MAX_VALUE).addContainerGap()));
		gl_panel_4.setVerticalGroup(gl_panel_4.createParallelGroup(
				Alignment.LEADING).addGroup(
				gl_panel_4
						.createSequentialGroup()
						.addContainerGap()
						.addComponent(scrollPane_6, GroupLayout.PREFERRED_SIZE,
								328, GroupLayout.PREFERRED_SIZE)
						.addContainerGap(14, Short.MAX_VALUE)));

		endpointRoutersTable = new JTable(endpointRouterTableModel);
		scrollPane_6.setViewportView(endpointRoutersTable);
		panel_4.setLayout(gl_panel_4);
		endPointsTable = new JTable(endpointTableModel);

		endPointsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scrollPane.setViewportView(endPointsTable);
		endpointsPanel.setLayout(gl_endpointsPanel);
		GroupLayout groupLayout = new GroupLayout(
				frmSensorNetworkSimulation.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addComponent(appTabbedPane, GroupLayout.DEFAULT_SIZE, 933, Short.MAX_VALUE)
					.addContainerGap())
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(appTabbedPane, GroupLayout.DEFAULT_SIZE, 804, Short.MAX_VALUE))
		);

		JPanel routersPanel = new JPanel();
		appTabbedPane.addTab("Routers", null, routersPanel, null);

		JButton addRouterBtn = new JButton("Add");
		addRouterBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					RouterDialogGUI dialog = new RouterDialogGUI(null,
							routerTableModel,routerLog);
					dialog.setTitle("Add Router");
					dialog.setVisible(true);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});

		JButton editRouterBtn = new JButton("Edit");
		editRouterBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					ZigBeeRouter router = routerTableModel
							.get(routersTable.getSelectedRow());
					RouterDialogGUI dialog = new RouterDialogGUI(
							router, routerTableModel,routerLog);
					dialog.setTitle("Edit Router");
					dialog.setVisible(true);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		editRouterBtn.setEnabled(false);
		JButton ntwDiscoveryBtn = new JButton("Network Discovery");
		JButton removeRouterBtn = new JButton("Remove");
		removeRouterBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ZigBeeRouter router = routerTableModel
						.remove(routersTable.getSelectedRow());
				router.stop();
				routersTable.getSelectionModel().clearSelection();
				removeRouterBtn.setEnabled(false);
				editRouterBtn.setEnabled(false);
				ntwDiscoveryBtn.setEnabled(false);
			}
		});
		removeRouterBtn.setEnabled(false);

		JScrollPane scrollPane_7 = new JScrollPane();

		JTabbedPane tabbedPane_2 = new JTabbedPane(JTabbedPane.TOP);

		JSeparator separator_1 = new JSeparator();

		JTabbedPane tabbedPane_3 = new JTabbedPane(JTabbedPane.TOP);
		
		
		ntwDiscoveryBtn.setEnabled(false);
		ntwDiscoveryBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ZigBeeRouter router = routerTableModel
						.get(routersTable.getSelectedRow());
				Map<RFChannel, List<Beacon>>  networksMap = router.networkDiscovery();
				NetworksDialogGUI gui = new NetworksDialogGUI(router,networksMap);
				gui.setTitle("Networks for "+router.getId());
				gui.setVisible(true);
			}
		});
		GroupLayout gl_routersPanel = new GroupLayout(routersPanel);
		gl_routersPanel.setHorizontalGroup(
			gl_routersPanel.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_routersPanel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_routersPanel.createParallelGroup(Alignment.LEADING)
						.addComponent(tabbedPane_3, GroupLayout.DEFAULT_SIZE, 900, Short.MAX_VALUE)
						.addComponent(separator_1, GroupLayout.DEFAULT_SIZE, 900, Short.MAX_VALUE)
						.addComponent(tabbedPane_2, GroupLayout.DEFAULT_SIZE, 900, Short.MAX_VALUE)
						.addComponent(scrollPane_7, GroupLayout.DEFAULT_SIZE, 900, Short.MAX_VALUE)
						.addGroup(gl_routersPanel.createSequentialGroup()
							.addComponent(addRouterBtn)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(editRouterBtn)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(removeRouterBtn)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(ntwDiscoveryBtn)))
					.addContainerGap())
		);
		gl_routersPanel.setVerticalGroup(
			gl_routersPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_routersPanel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_routersPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(addRouterBtn)
						.addComponent(editRouterBtn)
						.addComponent(removeRouterBtn)
						.addComponent(ntwDiscoveryBtn))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(scrollPane_7, GroupLayout.PREFERRED_SIZE, 72, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(tabbedPane_2, GroupLayout.PREFERRED_SIZE, 213, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(separator_1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(tabbedPane_3, GroupLayout.PREFERRED_SIZE, 333, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(69, Short.MAX_VALUE))
		);

		JPanel panel_7 = new JPanel();
		tabbedPane_3.addTab("Endpoints", null, panel_7, null);

		JScrollPane scrollPane_10 = new JScrollPane();
		GroupLayout gl_panel_7 = new GroupLayout(panel_7);
		gl_panel_7.setHorizontalGroup(gl_panel_7.createParallelGroup(
				Alignment.LEADING).addGroup(
				gl_panel_7
						.createSequentialGroup()
						.addContainerGap()
						.addComponent(scrollPane_10, GroupLayout.DEFAULT_SIZE,
								547, Short.MAX_VALUE).addContainerGap()));
		gl_panel_7.setVerticalGroup(gl_panel_7.createParallelGroup(
				Alignment.LEADING).addGroup(
				gl_panel_7
						.createSequentialGroup()
						.addContainerGap()
						.addComponent(scrollPane_10, GroupLayout.DEFAULT_SIZE,
								275, Short.MAX_VALUE).addContainerGap()));

		routerEndpointTable = new JTable();
		scrollPane_10.setViewportView(routerEndpointTable);
		panel_7.setLayout(gl_panel_7);

		JPanel panel_8 = new JPanel();
		tabbedPane_3.addTab("Routers", null, panel_8, null);

		JScrollPane scrollPane_11 = new JScrollPane();
		GroupLayout gl_panel_8 = new GroupLayout(panel_8);
		gl_panel_8.setHorizontalGroup(gl_panel_8.createParallelGroup(
				Alignment.LEADING).addGroup(
				gl_panel_8
						.createSequentialGroup()
						.addContainerGap()
						.addComponent(scrollPane_11, GroupLayout.DEFAULT_SIZE,
								547, Short.MAX_VALUE).addContainerGap()));
		gl_panel_8.setVerticalGroup(gl_panel_8.createParallelGroup(
				Alignment.LEADING).addGroup(
				gl_panel_8
						.createSequentialGroup()
						.addContainerGap()
						.addComponent(scrollPane_11, GroupLayout.DEFAULT_SIZE,
								275, Short.MAX_VALUE).addContainerGap()));

		routerRoutersTable = new JTable();
		scrollPane_11.setViewportView(routerRoutersTable);
		panel_8.setLayout(gl_panel_8);

		JPanel panel_5 = new JPanel();
		tabbedPane_2.addTab("Info", null, panel_5, null);

		JButton btnClear = new JButton("Clear");

		JScrollPane scrollPane_8 = new JScrollPane();
		GroupLayout gl_panel_5 = new GroupLayout(panel_5);
		gl_panel_5.setHorizontalGroup(gl_panel_5.createParallelGroup(
				Alignment.LEADING).addGroup(
				gl_panel_5
						.createSequentialGroup()
						.addContainerGap()
						.addGroup(
								gl_panel_5
										.createParallelGroup(Alignment.LEADING)
										.addComponent(scrollPane_8,
												GroupLayout.DEFAULT_SIZE, 547,
												Short.MAX_VALUE)
										.addComponent(btnClear))
						.addContainerGap()));
		gl_panel_5.setVerticalGroup(gl_panel_5.createParallelGroup(
				Alignment.LEADING).addGroup(
				gl_panel_5
						.createSequentialGroup()
						.addContainerGap()
						.addComponent(btnClear)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(scrollPane_8, GroupLayout.DEFAULT_SIZE,
								120, Short.MAX_VALUE).addContainerGap()));

		routerInfoConsole = new JTextArea();
		routerInfoConsole.setEditable(false);
		routerInfoConsole.setBackground(Color.BLACK);
		routerInfoConsole.setForeground(Color.GREEN);
		routerInfoConsole.setFont(new Font("Lucida Sans Typewriter",
				Font.PLAIN, 11));
		scrollPane_8.setViewportView(routerInfoConsole);
		panel_5.setLayout(gl_panel_5);

		JPanel panel_6 = new JPanel();
		tabbedPane_2.addTab("Debug", null, panel_6, null);

		JButton btnClear_1 = new JButton("Clear");

		JScrollPane scrollPane_9 = new JScrollPane();
		GroupLayout gl_panel_6 = new GroupLayout(panel_6);
		gl_panel_6.setHorizontalGroup(gl_panel_6.createParallelGroup(
				Alignment.LEADING).addGroup(
				gl_panel_6
						.createSequentialGroup()
						.addContainerGap()
						.addGroup(
								gl_panel_6
										.createParallelGroup(Alignment.LEADING)
										.addComponent(scrollPane_9,
												GroupLayout.DEFAULT_SIZE, 547,
												Short.MAX_VALUE)
										.addComponent(btnClear_1))
						.addContainerGap()));
		gl_panel_6.setVerticalGroup(gl_panel_6.createParallelGroup(
				Alignment.LEADING).addGroup(
				gl_panel_6
						.createSequentialGroup()
						.addContainerGap()
						.addComponent(btnClear_1)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(scrollPane_9, GroupLayout.DEFAULT_SIZE,
								120, Short.MAX_VALUE).addContainerGap()));

		routerDebugConsole = new JTextArea();
		routerDebugConsole.setBackground(Color.BLACK);
		routerDebugConsole.setEditable(false);
		routerDebugConsole.setForeground(Color.GREEN);
		routerDebugConsole.setFont(new Font("Lucida Sans Typewriter",
				Font.PLAIN, 11));
		scrollPane_9.setViewportView(routerDebugConsole);
		panel_6.setLayout(gl_panel_6);

		routersTable = new JTable(routerTableModel);
		scrollPane_7.setViewportView(routersTable);
		routersPanel.setLayout(gl_routersPanel);
		
		JPanel coordinatorsPanel = new JPanel();
		appTabbedPane.addTab("Coordinators", null, coordinatorsPanel, null);
		
		JButton addCoordinatorBtn = new JButton("Add");
		addCoordinatorBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					CoordinatorDialogGUI dialog = new CoordinatorDialogGUI(null,
							coordinatorTableModel,coordinatorLog);
					dialog.setTitle("Add Coordinator");
					dialog.setVisible(true);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		
		JButton editCoordinatorBtn = new JButton("Edit");
		editCoordinatorBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					ZigBeeCoordinator coordinator = coordinatorTableModel
							.get(coordinatorsTable.getSelectedRow());
					CoordinatorDialogGUI dialog = new CoordinatorDialogGUI(
							coordinator, coordinatorTableModel,coordinatorLog);
					dialog.setTitle("Edit Coordinator");
					dialog.setVisible(true);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		editCoordinatorBtn.setEnabled(false);
		
		JButton removeCoordinatorBtn = new JButton("Remove");
		
		removeCoordinatorBtn.setEnabled(false);
		
		JButton ntwFormationBtn = new JButton("Network Formation");
		ntwFormationBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ZigBeeCoordinator coordinator = coordinatorTableModel
						.get(coordinatorsTable.getSelectedRow());
				if(coordinator.networkFormation()) {
					int rowIndex = coordinatorTableModel.getIndexOf(coordinator);
					coordinatorTableModel.fireTableRowsUpdated(rowIndex, rowIndex);
				}else {
					JOptionPane.showMessageDialog(frmSensorNetworkSimulation, "INVALID REQUEST: NETWORK FORMATION", "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		ntwFormationBtn.setEnabled(false);
		
		JScrollPane scrollPane_12 = new JScrollPane();
		
		JTabbedPane tabbedPane_4 = new JTabbedPane(JTabbedPane.TOP);
		GroupLayout gl_coordinatorsPanel = new GroupLayout(coordinatorsPanel);
		gl_coordinatorsPanel.setHorizontalGroup(
			gl_coordinatorsPanel.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_coordinatorsPanel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_coordinatorsPanel.createParallelGroup(Alignment.TRAILING)
						.addComponent(tabbedPane_4, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 580, Short.MAX_VALUE)
						.addComponent(scrollPane_12, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 580, Short.MAX_VALUE)
						.addGroup(Alignment.LEADING, gl_coordinatorsPanel.createSequentialGroup()
							.addComponent(addCoordinatorBtn)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(editCoordinatorBtn)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(removeCoordinatorBtn)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(ntwFormationBtn)))
					.addContainerGap())
		);
		gl_coordinatorsPanel.setVerticalGroup(
			gl_coordinatorsPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_coordinatorsPanel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_coordinatorsPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(addCoordinatorBtn)
						.addComponent(editCoordinatorBtn)
						.addComponent(removeCoordinatorBtn)
						.addComponent(ntwFormationBtn))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(scrollPane_12, GroupLayout.PREFERRED_SIZE, 106, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(tabbedPane_4, GroupLayout.PREFERRED_SIZE, 272, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(333, Short.MAX_VALUE))
		);
		
		JPanel panel_3 = new JPanel();
		tabbedPane_4.addTab("Info", null, panel_3, null);
		
		JButton cleanCoordinatorInfo = new JButton("Clean");
		
		JScrollPane scrollPane_13 = new JScrollPane();
		GroupLayout gl_panel_3 = new GroupLayout(panel_3);
		gl_panel_3.setHorizontalGroup(
			gl_panel_3.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_3.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel_3.createParallelGroup(Alignment.LEADING)
						.addComponent(scrollPane_13, GroupLayout.DEFAULT_SIZE, 547, Short.MAX_VALUE)
						.addComponent(cleanCoordinatorInfo))
					.addContainerGap())
		);
		gl_panel_3.setVerticalGroup(
			gl_panel_3.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_3.createSequentialGroup()
					.addContainerGap()
					.addComponent(cleanCoordinatorInfo)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(scrollPane_13, GroupLayout.DEFAULT_SIZE, 108, Short.MAX_VALUE)
					.addContainerGap())
		);
		
		coordinatorInfoConsole = new JTextArea();
		coordinatorInfoConsole.setFont(new Font("Lucida Sans Typewriter", Font.PLAIN, 11));
		coordinatorInfoConsole.setForeground(Color.GREEN);
		coordinatorInfoConsole.setBackground(Color.BLACK);
		coordinatorInfoConsole.setEditable(false);
		scrollPane_13.setViewportView(coordinatorInfoConsole);
		panel_3.setLayout(gl_panel_3);
		
		JPanel panel_9 = new JPanel();
		tabbedPane_4.addTab("Debug", null, panel_9, null);
		
		JButton cleanCoordinatorDebug = new JButton("Clean");
		
		JScrollPane scrollPane_14 = new JScrollPane();
		GroupLayout gl_panel_9 = new GroupLayout(panel_9);
		gl_panel_9.setHorizontalGroup(
			gl_panel_9.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_9.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel_9.createParallelGroup(Alignment.LEADING)
						.addComponent(scrollPane_14, GroupLayout.DEFAULT_SIZE, 547, Short.MAX_VALUE)
						.addComponent(cleanCoordinatorDebug))
					.addContainerGap())
		);
		gl_panel_9.setVerticalGroup(
			gl_panel_9.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_9.createSequentialGroup()
					.addContainerGap()
					.addComponent(cleanCoordinatorDebug)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(scrollPane_14, GroupLayout.DEFAULT_SIZE, 108, Short.MAX_VALUE)
					.addContainerGap())
		);
		
		coordinatorDebugConsole = new JTextArea();
		coordinatorDebugConsole.setForeground(Color.ORANGE);
		coordinatorDebugConsole.setBackground(Color.BLACK);
		coordinatorDebugConsole.setFont(new Font("Lucida Sans Typewriter", Font.PLAIN, 11));
		coordinatorDebugConsole.setEditable(false);
		scrollPane_14.setViewportView(coordinatorDebugConsole);
		coordinatorDebugConsole.setColumns(10);
		panel_9.setLayout(gl_panel_9);
		
		coordinatorsTable = new JTable(coordinatorTableModel);
		scrollPane_12.setViewportView(coordinatorsTable);
		coordinatorsPanel.setLayout(gl_coordinatorsPanel);
		frmSensorNetworkSimulation.getContentPane().setLayout(groupLayout);
		
		coordinatorsTable.getSelectionModel().addListSelectionListener(
				new ListSelectionListener() {
					public void valueChanged(ListSelectionEvent event) {
						if (coordinatorsTable.getSelectedRow() == -1) {
							return;
						}
						editCoordinatorBtn.setEnabled(true);
						removeCoordinatorBtn.setEnabled(true);
						ntwFormationBtn.setEnabled(true);
					}
				});
		
		routersTable.getSelectionModel().addListSelectionListener(
				new ListSelectionListener() {
					public void valueChanged(ListSelectionEvent event) {
						if (routersTable.getSelectedRow() == -1) {
							return;
						}
						editRouterBtn.setEnabled(true);
						removeRouterBtn.setEnabled(true);
						ntwDiscoveryBtn.setEnabled(true);
					}
				});

		endPointsTable.getSelectionModel().addListSelectionListener(
				new ListSelectionListener() {
					public void valueChanged(ListSelectionEvent event) {
						if (endPointsTable.getSelectedRow() == -1) {
							return;
						}
						ZigBeeEndpoint zigBeeEndpoint = endpointTableModel
								.get(endPointsTable.getSelectedRow());
						sensorTableModel.setEndpoint(zigBeeEndpoint);
						endpointRouterTableModel.setEndpoint(zigBeeEndpoint);
						editEndpointButton.setEnabled(true);
						removeEndpointButton.setEnabled(true);
						addSensorButton.setEnabled(true);
						ntwDiscoveryEndpointBtn.setEnabled(true);
					}
				});
		sensorsTable.getSelectionModel().addListSelectionListener(
				new ListSelectionListener() {
					public void valueChanged(ListSelectionEvent event) {
						if (sensorsTable.getSelectedRow() == -1) {
							return;
						}
						deleteSensorButton.setEnabled(true);
						editSensorButton.setEnabled(true);
					}
				});
		removeCoordinatorBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ZigBeeCoordinator coordinator = coordinatorTableModel
						.remove(coordinatorsTable.getSelectedRow());
				coordinator.stop();
				coordinatorsTable.getSelectionModel().clearSelection();
				removeCoordinatorBtn.setEnabled(false);
				editCoordinatorBtn.setEnabled(false);
				ntwFormationBtn.setEnabled(false);
			}
		});

		removeEndpointButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ZigBeeEndpoint zigBeeEndpoint = endpointTableModel
						.remove(endPointsTable.getSelectedRow());
				zigBeeEndpoint.stop();
				endPointsTable.getSelectionModel().clearSelection();
				removeEndpointButton.setEnabled(false);
				editEndpointButton.setEnabled(false);
				addSensorButton.setEnabled(false);
				ntwDiscoveryEndpointBtn.setEnabled(false);
				sensorTableModel.clear();
				endpointRouterTableModel.clear();
			}
		});

		deleteSensorButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ZigBeeEndpoint zigBeeEndpoint = sensorTableModel.getEndpoint();
				zigBeeEndpoint.removeSensor(sensorsTable.getSelectedRow());
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
