package com.uag.sd.weathermonitor.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import com.uag.sd.weathermonitor.gui.models.CoordinatorTableModel;
import com.uag.sd.weathermonitor.model.device.DeviceLog;
import com.uag.sd.weathermonitor.model.device.ZigBeeCoordinator;

public class CoordinatorDialogGUI extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3229426320105901026L;
	private final JPanel contentPanel = new JPanel();
	private JTextField idField;
	private JSpinner coverageField;
	private JSpinner positionXField;
	private JSpinner positionYField;
	private ZigBeeCoordinator coordinator;
	private CoordinatorTableModel tableModel;
	private DeviceLog deviceLog;
	private JCheckBox activeBox;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			CoordinatorDialogGUI dialog = new CoordinatorDialogGUI();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public CoordinatorDialogGUI() {
		setTitle("Coordinator");
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setType(Type.POPUP);
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
						
						if(coordinator==null) {
							try {
								coordinator = new ZigBeeCoordinator(idField.getText(),deviceLog);
							} catch ( IOException e1) {
								JOptionPane.showMessageDialog(CoordinatorDialogGUI.this, e1.getMessage());
								return;
							}
							isNew = true;
							
						}
						boolean prevState = coordinator.isActive();
						coordinator.setId(idField.getText());
						coordinator.setPotency((int)coverageField.getModel().getValue());
						coordinator.setLocation((int)positionXField.getModel().getValue(),
								(int)positionYField.getModel().getValue());
						coordinator.setActive(activeBox.isSelected());
						if(isNew) {
							tableModel.add(coordinator);
							if(coordinator.isActive()) {
								tableModel.start(coordinator);
							}
						}else {
							if(prevState != coordinator.isActive()) {
								if(coordinator.isActive()) {
									tableModel.start(coordinator);
								}else {
									coordinator.stop();
								}
							}
							int rowIndex = tableModel.getIndexOf(coordinator);
							tableModel.fireTableRowsUpdated(rowIndex, rowIndex);
						}
						CoordinatorDialogGUI.this.dispose();
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
						CoordinatorDialogGUI.this.dispose();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}
	
	public CoordinatorDialogGUI(ZigBeeCoordinator coordinator,CoordinatorTableModel tableModel,DeviceLog deviceLog) {
		this();
		this.coordinator = coordinator;
		this.tableModel = tableModel;
		this.deviceLog = deviceLog;
		if(coordinator!=null) {
			idField.setText(coordinator.getId());
			idField.setEnabled(false);
			coverageField.getModel().setValue(coordinator.getPotency());
			positionXField.getModel().setValue( new Double(coordinator.getLocation().getX()).intValue());
			positionYField.getModel().setValue( new Double(coordinator.getLocation().getY()).intValue());
			activeBox.setSelected(coordinator.isActive());
		}
	}
	
	public void reset() {
		idField.setText("");
		coverageField.getModel().setValue(1);
		positionXField.getModel().setValue(0);
		positionYField.getModel().setValue(0);
		activeBox.setSelected(false);
	}

}
