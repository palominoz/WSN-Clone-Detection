package gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class ControlPanel extends JFrame{
	private static ControlPanel singleton=null;
	
	public static ControlPanel controlPanel(){
		if (singleton==null) singleton=new ControlPanel();
		return singleton;
	}
	
	private JButton startButton = new JButton("Start");
	private JButton nextButton = new JButton("Next");
	private JButton stopButton =  new JButton("Stop");
	public JButton pauseButton = new JButton("Pause");
	private JButton	fetchButton = new JButton("Fetch");
	private JButton showAllMessagesButton = new JButton("Paths");
	private JButton showRangesButton = new JButton("Ranges");
	private JButton showIDsButton = new JButton("IDs");
	private JButton showIdlesButton = new JButton("Idles");
	private JButton highlightNodeButton = new JButton("Highlight");
	
	public JComboBox IDsCombo=new JComboBox();
	
	public JLabel overallStatusLabel=new JLabel("Waiting for the settings file");
	
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
		highlightNodeButton.setActionCommand("highlightNode");
		
		
		
		//GUI group
		//upper
		showAllMessagesButton.setActionCommand("showAllMessages");
		showRangesButton.setActionCommand("showRanges");
		showIDsButton.setActionCommand("showIDs");
		showIdlesButton.setActionCommand("showIdles");
		//lower
		
		
		//set listener
		startButton.addActionListener(Listener.listener());
		nextButton.addActionListener(Listener.listener());
		stopButton.addActionListener(Listener.listener());
		pauseButton.addActionListener(Listener.listener());
		fetchButton.addActionListener(Listener.listener());
		//GUI group
		showAllMessagesButton.addActionListener(Listener.listener());
		showRangesButton.addActionListener(Listener.listener());
		showIDsButton.addActionListener(Listener.listener());
		highlightNodeButton.addActionListener(Listener.listener());
		IDsCombo.addActionListener(Listener.listener());
		showIdlesButton.addActionListener(Listener.listener());
		
		
		//Commands creation
		JPanel buttonGroup=new JPanel(new GridLayout(2,3));
		buttonGroup.setBorder(BorderFactory.createTitledBorder("Simulation controls"));
		buttonGroup.add(startButton);
		buttonGroup.add(nextButton);
		buttonGroup.add(stopButton);
		buttonGroup.add(pauseButton);
		buttonGroup.add(fetchButton);
		
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
		simulationStatus.add(overallStatusLabel);
		
		//composition
		content.add(buttonGroup, BorderLayout.NORTH);
		content.add(guiGroup, BorderLayout.CENTER);
		content.add(simulationStatus, BorderLayout.SOUTH);
		
		//settings
		setSize(400,240);
		setVisible(true);
	}
	
	public static void setOverallStatusText(String s){
		controlPanel().overallStatusLabel.setText(s);
	}
	
	
}
