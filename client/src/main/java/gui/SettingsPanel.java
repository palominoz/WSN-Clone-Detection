/**
* SettingsPanel
* 
* Zironda Andrea -- PCD 2011/2012
* 
* Wireless Sensor Networks - Clone Detection Simulator
* 
* */

package gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import enums.SupportedProtocol;

import logic.Settings;

/**
 * This class provides a panel in which settings can be overridden.
 * 
 * */
@SuppressWarnings("serial")
public class SettingsPanel extends JFrame implements ActionListener{
	private static SettingsPanel singleton = null;
	
	public static SettingsPanel settingsPanel(){
		if (singleton == null) singleton = new SettingsPanel();
		return singleton;
	}
	
	JTextField numberOfSimulations = new JTextField(new Integer(Settings.numberOfSimulations).toString());
	JTextField numberOfNodes = new JTextField(new String(new Integer(Settings.numberOfNodes).toString()));
	JTextField claimForwardProbability = new JTextField(new Double(Settings.claimForwardProbability).toString());
	JTextField transmissionRange = new JTextField(new Double(Settings.transmissionRange).toString());
	JTextField defaultMaxEnergy = new JTextField(new Double(Settings.defaultMaxEnergy).toString());
	JTextField transmitConsumption = new JTextField(new Double(Settings.transmitConsumption).toString());
	JTextField receiveConsumption = new JTextField(new Double(Settings.receiveConsumption).toString());
	JTextField signatureConsumption = new JTextField(new Double(Settings.signatureConsumption).toString());
	JTextField numberOfForwards = new JTextField(new Integer(Settings.numberOfForwards).toString());
	JTextField bufferSize = new JTextField(new Integer(Settings.bufferSize).toString());
	JTextField timeToWait = new JTextField(new Integer(Settings.timeToWait).toString());
	
	JComboBox protocol = new JComboBox();
	
	
	private SettingsPanel(){
		super("Settings Panel");
		Container container=getContentPane();
		
		JPanel group=new JPanel(new GridLayout(12,2));
		group.setBorder(BorderFactory.createTitledBorder("Settings to override"));
		group.add(new JLabel("Protocol"));
		group.add(protocol);
		protocol.addItem(SupportedProtocol.LSM);
		protocol.addItem(SupportedProtocol.RED);
		group.add(new JLabel("Number of simulations"));
		group.add(numberOfSimulations);
		group.add(new JLabel("Number of nodes"));
		group.add(numberOfNodes);
		group.add(new JLabel("Claim forward probability"));
		group.add(claimForwardProbability);
		group.add(new JLabel("Transmission range"));
		group.add(transmissionRange);
		group.add(new JLabel("Available node energy"));
		group.add(defaultMaxEnergy);
		group.add(new JLabel("Transmit consuption"));
		group.add(transmitConsumption);
		group.add(new JLabel("Receive consumption"));
		group.add(receiveConsumption);
		group.add(new JLabel("Signature consumption"));
		group.add(signatureConsumption);
		group.add(new JLabel("Number of forwards"));
		group.add(numberOfForwards);
		group.add(new JLabel("Message buffer size"));
		group.add(bufferSize);
		group.add(new JLabel("Node delay"));
		group.add(timeToWait);
		
		
		setSize(400,430);
		container.add(group, BorderLayout.NORTH);
		JButton commit = new JButton("Commit changes");
		commit.setActionCommand("commit");
		commit.addActionListener(this);
		container.add(commit, BorderLayout.SOUTH);
		setVisible(true);
	}

	private static void updateSettings(){
		Settings.protocol = (SupportedProtocol)settingsPanel().protocol.getSelectedItem();
		Settings.numberOfNodes = Integer.parseInt(settingsPanel().numberOfNodes.getText());
		Settings.numberOfSimulations = Integer.parseInt(settingsPanel().numberOfSimulations.getText()); 
		Settings.claimForwardProbability = Double.parseDouble(settingsPanel().claimForwardProbability.getText());
		Settings.transmissionRange = Double.parseDouble(settingsPanel().transmissionRange.getText());
		Settings.defaultMaxEnergy = Double.parseDouble(settingsPanel().defaultMaxEnergy.getText());
		Settings.transmitConsumption = Double.parseDouble(settingsPanel().transmitConsumption.getText());
		Settings.receiveConsumption	= Double.parseDouble(settingsPanel().receiveConsumption.getText());
		Settings.signatureConsumption = Double.parseDouble(settingsPanel().signatureConsumption.getText());
		Settings.numberOfForwards = Integer.parseInt(settingsPanel().numberOfForwards.getText());
		Settings.bufferSize	= Integer.parseInt(settingsPanel().bufferSize.getText());
		Settings.timeToWait	= Integer.parseInt(settingsPanel().timeToWait.getText());
	}
	
	private static void fetchSettings(){
		settingsPanel().protocol.setSelectedItem(Settings.protocol);
		settingsPanel().numberOfNodes.setText(new Integer(Settings.numberOfNodes).toString());
		settingsPanel().numberOfSimulations.setText(new Integer(Settings.numberOfSimulations).toString());
		settingsPanel().claimForwardProbability.setText(new Double(Settings.claimForwardProbability).toString());
		settingsPanel().transmissionRange.setText(new Double(Settings.transmissionRange).toString());
		settingsPanel().defaultMaxEnergy.setText(new Double(Settings.defaultMaxEnergy).toString());
		settingsPanel().transmitConsumption.setText(new Double(Settings.transmitConsumption).toString());
		settingsPanel().receiveConsumption.setText(new Double(Settings.receiveConsumption).toString());
		settingsPanel().signatureConsumption.setText(new Double(Settings.signatureConsumption).toString());
		settingsPanel().numberOfForwards.setText(new Integer(Settings.numberOfForwards).toString());
		settingsPanel().bufferSize.setText(new Integer(Settings.bufferSize).toString());
		settingsPanel().timeToWait.setText(new Integer(Settings.timeToWait).toString());
	}
	
	public static void wake(){
		fetchSettings();
		settingsPanel().setVisible(true);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		if ("commit".equals(e.getActionCommand())){
			try{
				updateSettings();
				UserInterface.showMessage("Settings have been overridden");
				ControlPanel.update();
			}
			catch(Exception exc){
				UserInterface.showError("The value you inserted was not valid");
			}
			
		}
	}
	
}
