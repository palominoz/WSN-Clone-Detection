package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import logic.Settings;

public class SettingsPanel extends JFrame {
	private static SettingsPanel singleton = null;
	
	public static SettingsPanel settingsPanel(){
		if (singleton == null) singleton = new SettingsPanel();
		return singleton;
	}
	
	JTextField nsim = new JTextField(Settings.numberOfSimulations);
	JTextField numberOfNodes = new JTextField(Settings.numberOfNodes);
	
	private SettingsPanel(){
		super("Settings Panel");
		Container container=getContentPane();
		
		JPanel group=new JPanel(new GridLayout(11,2));
		group.setBorder(BorderFactory.createTitledBorder("Settings to override"));
		group.add(new JLabel("sdd"));
		group.add(nsim);
		group.add(new JLabel("sds"));
		group.add(numberOfNodes);
		
		container.add(group, BorderLayout.NORTH);
		setVisible(true);
	}
	
}
