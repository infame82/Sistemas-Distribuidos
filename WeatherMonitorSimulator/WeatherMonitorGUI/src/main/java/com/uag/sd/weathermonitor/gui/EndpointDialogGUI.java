package com.uag.sd.weathermonitor.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import com.uag.sd.weathermonitor.model.endpoint.Endpoint;
import com.uag.sd.weathermonitor.model.logs.SensorLog;

public class EndpointDialogGUI extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6684257466299046068L;
	private final JPanel contentPanel = new JPanel();
	private JTextField idField;
	private JSpinner coverageField;
	private JSpinner positionXField;
	private JSpinner positionYField;
	private JCheckBox activeBox;
	private Endpoint endpoint;
	private EndpointTableModel tableModel;
	private EndpointRefresher endpointRefresher;
	private SensorLog sensorLog;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			EndpointDialogGUI dialog = new EndpointDialogGUI();
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public EndpointDialogGUI(Endpoint endpoint,EndpointTableModel tableModel,EndpointRefresher endpointRefresher,SensorLog sensorLog) {
		this();
		this.endpoint = endpoint;
		this.tableModel = tableModel;
		this.endpointRefresher = endpointRefresher;
		this.sensorLog = sensorLog;
		if(endpoint!=null) {
			idField.setText(endpoint.getId());
			idField.setEnabled(false);
			coverageField.getModel().setValue(endpoint.getCoverage());
			positionXField.getModel().setValue( new Double(endpoint.getLocation().getX()).intValue());
			positionYField.getModel().setValue( new Double(endpoint.getLocation().getY()).intValue());
			activeBox.setSelected(endpoint.isActive());
		}
	}
	
	public void reset() {
		idField.setText("");
		coverageField.getModel().setValue(1);
		positionXField.getModel().setValue(0);
		positionYField.getModel().setValue(0);
		activeBox.setSelected(false);
	}

	/**
	 * Create the dialog.
	 */
	public EndpointDialogGUI() {
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setType(Type.POPUP);
		setModal(true);
		setResizable(false);
		setAlwaysOnTop(true);
		setBounds(100, 100, 222, 274);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);

		JLabel lblId = new JLabel("ID:");

		idField = new JTextField();
		idField.setColumns(10);

		JLabel lblCoverage = new JLabel("Coverage:");
		SpinnerModel coverageModel = new SpinnerNumberModel(1, 1, 10, 1);
		SpinnerModel locationXModel = new SpinnerNumberModel(0, 0, 10000, 1);
		SpinnerModel locationYModel = new SpinnerNumberModel(0, 0, 10000, 1);
		coverageField = new JSpinner(coverageModel);

		JPanel locationPanel = new JPanel();
		locationPanel.setBorder(new TitledBorder(null, "Location",
				TitledBorder.LEADING, TitledBorder.TOP, null, null));
		
		activeBox = new JCheckBox("Active");
		GroupLayout gl_contentPanel = new GroupLayout(contentPanel);
		gl_contentPanel.setHorizontalGroup(
			gl_contentPanel.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_contentPanel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPanel.createSequentialGroup()
							.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_contentPanel.createSequentialGroup()
									.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
										.addComponent(lblCoverage)
										.addComponent(lblId))
									.addPreferredGap(ComponentPlacement.RELATED)
									.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
										.addComponent(idField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
										.addComponent(coverageField, GroupLayout.DEFAULT_SIZE, 134, Short.MAX_VALUE)))
								.addComponent(locationPanel, GroupLayout.DEFAULT_SIZE, 202, Short.MAX_VALUE))
							.addGap(248))
						.addGroup(gl_contentPanel.createSequentialGroup()
							.addComponent(activeBox)
							.addContainerGap(135, Short.MAX_VALUE))))
		);
		gl_contentPanel.setVerticalGroup(
			gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPanel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.TRAILING)
						.addComponent(lblId)
						.addComponent(idField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblCoverage)
						.addComponent(coverageField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(locationPanel, GroupLayout.PREFERRED_SIZE, 94, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(activeBox)
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);

		JLabel lblX = new JLabel("X:");

		positionXField = new JSpinner(locationXModel);

		positionYField = new JSpinner(locationYModel);

		JLabel lblY = new JLabel("Y:");
		GroupLayout gl_locationPanel = new GroupLayout(locationPanel);
		gl_locationPanel
				.setHorizontalGroup(gl_locationPanel
						.createParallelGroup(Alignment.LEADING)
						.addGroup(
								gl_locationPanel
										.createSequentialGroup()
										.addGap(36)
										.addGroup(
												gl_locationPanel
														.createParallelGroup(
																Alignment.TRAILING)
														.addComponent(lblY)
														.addComponent(lblX))
										.addPreferredGap(
												ComponentPlacement.RELATED)
										.addGroup(
												gl_locationPanel
														.createParallelGroup(
																Alignment.TRAILING,
																false)
														.addComponent(
																positionYField,
																Alignment.LEADING)
														.addComponent(
																positionXField,
																Alignment.LEADING,
																GroupLayout.PREFERRED_SIZE,
																85,
																GroupLayout.PREFERRED_SIZE))
										.addGap(19)));
		gl_locationPanel
				.setVerticalGroup(gl_locationPanel
						.createParallelGroup(Alignment.LEADING)
						.addGroup(
								gl_locationPanel
										.createSequentialGroup()
										.addContainerGap()
										.addGroup(
												gl_locationPanel
														.createParallelGroup(
																Alignment.BASELINE)
														.addComponent(
																positionXField,
																GroupLayout.PREFERRED_SIZE,
																GroupLayout.DEFAULT_SIZE,
																GroupLayout.PREFERRED_SIZE)
														.addComponent(lblX))
										.addPreferredGap(
												ComponentPlacement.RELATED,
												GroupLayout.DEFAULT_SIZE,
												Short.MAX_VALUE)
										.addGroup(
												gl_locationPanel
														.createParallelGroup(
																Alignment.BASELINE)
														.addComponent(
																positionYField,
																GroupLayout.PREFERRED_SIZE,
																GroupLayout.DEFAULT_SIZE,
																GroupLayout.PREFERRED_SIZE)
														.addComponent(lblY))));
		locationPanel.setLayout(gl_locationPanel);
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
						
						if(endpoint==null) {
							endpoint = new Endpoint();
							endpoint.setSensorLog(sensorLog);
							isNew = true;
							
						}
						boolean prevState = endpoint.isActive();
						endpoint.setId(idField.getText());
						endpoint.setCoverage((int)coverageField.getModel().getValue());
						endpoint.setLocation((int)positionXField.getModel().getValue(),
								(int)positionYField.getModel().getValue());
						endpoint.setActive(activeBox.isSelected());
						if(isNew) {
							tableModel.addEndpoint(endpoint);
							if(endpoint.isActive()) {
								tableModel.startEndpoint(endpoint);
							}
						}else {
							if(prevState != endpoint.isActive()) {
								if(endpoint.isActive()) {
									tableModel.startEndpoint(endpoint);
								}else {
									endpoint.stop();
								}
							}
							int rowIndex = tableModel.getIndexOf(endpoint);
							tableModel.fireTableRowsUpdated(rowIndex, rowIndex);
							endpointRefresher.refreshEndpointDetails(endpoint);
						}
						EndpointDialogGUI.this.dispose();
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
						EndpointDialogGUI.this.dispose();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}

	}
}
