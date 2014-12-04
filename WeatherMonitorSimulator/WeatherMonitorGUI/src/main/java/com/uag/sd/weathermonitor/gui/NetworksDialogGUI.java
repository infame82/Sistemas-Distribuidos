package com.uag.sd.weathermonitor.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.List;
import java.util.Map;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import com.uag.sd.weathermonitor.gui.models.NetworkTableModel;
import com.uag.sd.weathermonitor.gui.models.NetworkValue;
import com.uag.sd.weathermonitor.model.device.Beacon;
import com.uag.sd.weathermonitor.model.device.Device;
import com.uag.sd.weathermonitor.model.device.ZigBeeCoordinator;
import com.uag.sd.weathermonitor.model.device.ZigBeeRouter;
import com.uag.sd.weathermonitor.model.layer.physical.channel.RFChannel;

public class NetworksDialogGUI extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5595849259428628640L;
	private final JPanel contentPanel = new JPanel();
	private JTable ntwTable;
	private Device device;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			NetworksDialogGUI dialog = new NetworksDialogGUI();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public NetworksDialogGUI(Device device,Map<RFChannel, List<Beacon>> availableNetworks) {
		this.device = device;
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);

		JScrollPane scrollPane = new JScrollPane();
		GroupLayout gl_contentPanel = new GroupLayout(contentPanel);
		gl_contentPanel.setHorizontalGroup(gl_contentPanel.createParallelGroup(
				Alignment.LEADING).addGroup(
				gl_contentPanel
						.createSequentialGroup()
						.addContainerGap()
						.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE,
								428, Short.MAX_VALUE).addContainerGap()));
		gl_contentPanel.setVerticalGroup(gl_contentPanel.createParallelGroup(
				Alignment.LEADING).addGroup(
				gl_contentPanel
						.createSequentialGroup()
						.addContainerGap()
						.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE,
								217, Short.MAX_VALUE).addContainerGap()));

		NetworkTableModel tableModel = new NetworkTableModel(availableNetworks);
		ntwTable = new JTable(tableModel);
		scrollPane.setViewportView(ntwTable);
		JButton joinBtn = new JButton("Join");
		contentPanel.setLayout(gl_contentPanel);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{

				joinBtn.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						NetworkValue value = tableModel.get(ntwTable.getSelectedRow());
						if(!device.networkJoin(value.getChannel(), value.getBeacon())) {
							JOptionPane.showMessageDialog(NetworksDialogGUI.this, "INVALID REQUEST: NETWORK JOIN", "Error", JOptionPane.ERROR_MESSAGE);
						}
						NetworksDialogGUI.this.dispose();
					}
				});
				joinBtn.setEnabled(false);
				joinBtn.setActionCommand("OK");
				buttonPane.add(joinBtn);
				getRootPane().setDefaultButton(joinBtn);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						NetworksDialogGUI.this.dispose();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
		ntwTable.getSelectionModel().addListSelectionListener(
				new ListSelectionListener() {
					public void valueChanged(ListSelectionEvent event) {
						if (ntwTable.getSelectedRow() == -1) {
							return;
						}
						joinBtn.setEnabled(true);
					}
				});
	}

	/**
	 * Create the dialog.
	 */
	public NetworksDialogGUI() {
		setAlwaysOnTop(true);
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);

		JScrollPane scrollPane = new JScrollPane();
		GroupLayout gl_contentPanel = new GroupLayout(contentPanel);
		gl_contentPanel.setHorizontalGroup(gl_contentPanel.createParallelGroup(
				Alignment.LEADING).addGroup(
				gl_contentPanel
						.createSequentialGroup()
						.addContainerGap()
						.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE,
								428, Short.MAX_VALUE).addContainerGap()));
		gl_contentPanel.setVerticalGroup(gl_contentPanel.createParallelGroup(
				Alignment.LEADING).addGroup(
				gl_contentPanel
						.createSequentialGroup()
						.addContainerGap()
						.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE,
								217, Short.MAX_VALUE).addContainerGap()));

		ntwTable = new JTable(new NetworkTableModel());
		scrollPane.setViewportView(ntwTable);
		JButton joinBtn = new JButton("Join");
		contentPanel.setLayout(gl_contentPanel);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{

				joinBtn.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						
					}
				});
				joinBtn.setEnabled(false);
				joinBtn.setActionCommand("OK");
				buttonPane.add(joinBtn);
				getRootPane().setDefaultButton(joinBtn);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						NetworksDialogGUI.this.dispose();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
		ntwTable.getSelectionModel().addListSelectionListener(
				new ListSelectionListener() {
					public void valueChanged(ListSelectionEvent event) {
						if (ntwTable.getSelectedRow() == -1) {
							return;
						}
						joinBtn.setEnabled(true);
					}
				});
	}
}
