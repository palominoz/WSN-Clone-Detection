/*
* ControlPanel
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
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import logic.Settings;

/*
 * This class represents a panel in which the user can control the simulations. It has a number of buttons, checkboxes to
 * fulfil the needs of the user.
 * 
 * */
public class ControlPanel extends JFrame{
/***SINGLETON IMPLEMENTATION***/
	private static ControlPanel singleton=null;
	
	public static ControlPanel controlPanel(){
		if (singleton==null) {
			singleton=new ControlPanel();
			update();
		}
		return singleton;
	}
/***CLASS MEMBERS***/
/***INSTANCE MEMBERS***/
	// names tells you all
	private JButton startButton = new JButton("Start");
	private JButton nextButton = new JButton("Next");
	private JButton stopButton =  new JButton("Stop");
	public JButton pauseButton = new JButton("Pause");
	private JButton	fetchButton = new JButton("Fetch Settings");
	private JButton lookupServerButton = new JButton("Lookup Server");
	private JButton overrideSettingsButton = new JButton("Override");
	private JButton showAllMessagesButton = new JButton("Paths");
	private JButton showRangesButton = new JButton("Range");
	private JButton showIDsButton = new JButton("IDs");
	private JButton showIdlesButton = new JButton("Idles");
	private JButton highlightNodeButton = new JButton("Highlight");
	
	
	public JComboBox IDsCombo=new JComboBox();
	
	public JLabel statusLabel=new JLabel(Settings.status());
	
	public JCheckBox pauseWhenDetectingClone = new JCheckBox("Pause when detecting clone");
	public JCheckBox pauseEverySimulation = new JCheckBox("Pause after each simulation");
	public JCheckBox enableGraphics = new JCheckBox("Disable graphics (~ 10x faster performance)");
/***CONSTRUCTORS***/
	ControlPanel(){
		//init
		super("Control Panel");
		Container content=getContentPane();
		//action commands
		//commands group
		startButton.setActionCommand("start");
		nextButton.setActionCommand("next");
		stopButton.setActionCommand("stop");
		pauseButton.setActionCommand("pause");
		fetchButton.setActionCommand("fetch");
		lookupServerButton.setActionCommand("lookup");
		overrideSettingsButton.setActionCommand("override");
		highlightNodeButton.setActionCommand("highlightNode");
		

		
		
		
		//GUI group
		//upper
		showAllMessagesButton.setActionCommand("showAllMessages");
		showRangesButton.setActionCommand("showRanges");
		showIDsButton.setActionCommand("showIDs");
		showIdlesButton.setActionCommand("showIdles");
		//lower
		
		pauseWhenDetectingClone.setActionCommand("ClonePause");
		pauseEverySimulation.setActionCommand("SimPause");
		enableGraphics.setActionCommand("EnableGraphics");
		
		//set listener
		startButton.addActionListener(Listener.listener());
		nextButton.addActionListener(Listener.listener());
		stopButton.addActionListener(Listener.listener());
		pauseButton.addActionListener(Listener.listener());
		fetchButton.addActionListener(Listener.listener());
		lookupServerButton.addActionListener(Listener.listener());
		overrideSettingsButton.addActionListener(Listener.listener());
		pauseWhenDetectingClone.addActionListener(Listener.listener());
		pauseEverySimulation.addActionListener(Listener.listener());
		enableGraphics.addActionListener(Listener.listener());
		
		//GUI group
		showAllMessagesButton.addActionListener(Listener.listener());
		showRangesButton.addActionListener(Listener.listener());
		showIDsButton.addActionListener(Listener.listener());
		highlightNodeButton.addActionListener(Listener.listener());
		IDsCombo.addActionListener(Listener.listener());
		showIdlesButton.addActionListener(Listener.listener());
		
		//Commands creation
		JPanel statusGroup=new JPanel(new GridLayout(2,1));
		statusGroup.setBorder(BorderFactory.createTitledBorder("Status"));
		statusGroup.add(statusLabel);
		
		
		
		//Commands creation
		JPanel controlGroup=new JPanel(new GridLayout(2,1));
		JPanel buttons = new JPanel(new GridLayout(4,2));
		JPanel checks = new JPanel(new GridLayout(3,1));
		controlGroup.setBorder(BorderFactory.createTitledBorder("Simulation controls"));
		buttons.add(startButton);
		buttons.add(nextButton);
		buttons.add(stopButton);
		buttons.add(pauseButton);
		buttons.add(fetchButton);
		buttons.add(lookupServerButton);
		buttons.add(overrideSettingsButton);
		checks.add(pauseWhenDetectingClone);
		checks.add(pauseEverySimulation);
		checks.add(enableGraphics);
		controlGroup.add(buttons);
		controlGroup.add(checks);
		
		//GUI group creation
		JPanel guiGroup=new JPanel(new GridLayout(2,3));
		guiGroup.setBorder(BorderFactory.createTitledBorder("GUI controls"));
		guiGroup.add(showAllMessagesButton);
		guiGroup.add(showRangesButton);
		guiGroup.add(showIDsButton);
		guiGroup.add(showIdlesButton);
		guiGroup.add(IDsCombo);
		guiGroup.add(highlightNodeButton);
		
		//Status creation
		JPanel simulationStatus=new JPanel(new GridLayout(1,1));
		simulationStatus.setBorder(BorderFactory.createTitledBorder("Simulation status"));
		
		//composition
		content.add(statusGroup, BorderLayout.NORTH);
		content.add(controlGroup, BorderLayout.CENTER);
		content.add(guiGroup, BorderLayout.SOUTH);
		
		//settings
		setSize(400,400);
		setVisible(true);
	}
/***CLASS METHODS***/
	public static void notifyEndOfSimulation(Integer index) {
		//controlPanel().pauseButton.setEnabled(false);
		
		controlPanel().statusLabel.setText("<html><font color='green'>Sending stats .. </font></html>");
		controlPanel().showAllMessagesButton.setEnabled(false);
		controlPanel().showRangesButton.setEnabled(false);
		controlPanel().showIDsButton.setEnabled(false);
		controlPanel().showIdlesButton.setEnabled(false);
		controlPanel().IDsCombo.setEnabled(false);
		controlPanel().highlightNodeButton.setEnabled(false);
		controlPanel().IDsCombo.removeAllItems();
	}
	
	public static void notifyPause(){
		controlPanel().statusLabel.setText("<html><font color='yellow'>Paused</font></html>");
	}
	
	public static void notifyUnpause(){
		controlPanel().statusLabel.setText("<html><font color='green'>Running</font></html>");
	}
	
	
	public static void notifyStartOfSimulation(Integer index) {
		controlPanel().pauseButton.setEnabled(true);
		
		controlPanel().statusLabel.setText("<html><font color='green'>Running simulation " + index + "</font></html>");
		controlPanel().showAllMessagesButton.setEnabled(true);
		controlPanel().showRangesButton.setEnabled(true);
		controlPanel().showIDsButton.setEnabled(true);
		controlPanel().showIdlesButton.setEnabled(true);
		controlPanel().IDsCombo.setEnabled(true);
		controlPanel().highlightNodeButton.setEnabled(true);
	}
	
	public static void update(){
		singleton.statusLabel.setText(Settings.status());
		if (Settings.areReady() == false){
			controlPanel().startButton.setEnabled(false);
			controlPanel().stopButton.setEnabled(false);
			controlPanel().pauseButton.setEnabled(false);
			controlPanel().nextButton.setEnabled(false);
			
			controlPanel().showAllMessagesButton.setEnabled(false);
			controlPanel().showRangesButton.setEnabled(false);
			controlPanel().showIDsButton.setEnabled(false);
			controlPanel().showIdlesButton.setEnabled(false);
			controlPanel().IDsCombo.setEnabled(false);
			controlPanel().highlightNodeButton.setEnabled(false);
			
		}
		else{
			controlPanel().startButton.setEnabled(true);
			controlPanel().stopButton.setEnabled(true);
			controlPanel().nextButton.setEnabled(true);
		}
	}
/***INSTANCE METHODS***/
	
	
	
	
	
	
	
}
