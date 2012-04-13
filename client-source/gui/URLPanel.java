package gui;

import java.awt.*;
import java.awt.event.*;
import java.rmi.Naming;

import javax.swing.*;

import exceptions.WrongUrlPanel;
import parser.*;

import logic.Hypervisor;
import logic.Settings;

public class URLPanel extends JFrame implements ActionListener{
	
	private JLabel URLLabel;
	private JTextField URLTextField;
	private JButton confirmButton=new JButton("Done");
	private final String type;
	
	
	private URLPanel(String _type){
		super("Please insert a valid URL");
		type = _type;
		if (type == "settings"){
			URLLabel = new JLabel("Please insert a valid url pointing to the configuration file");
			URLTextField  = new JTextField("http://www.math.unipd.it/~conti/teaching/PCD1112/project_config.txt");
		}
		else if (type == "server"){
			URLLabel = new JLabel("Please insert a valid url pointing to the RMI server");
			URLTextField  = new JTextField("localhost");
		}
		Container container=getContentPane();
		
		JPanel layout=new JPanel(new GridLayout(3,1));
		
		//set this as listener of the confimation button
		confirmButton.addActionListener(this);
		
		
		layout.add(URLLabel,BorderLayout.WEST);
		layout.add(URLTextField);
		layout.add(confirmButton, BorderLayout.EAST);
		
		container.add(layout);
		setSize(new Dimension(500,140));
		setVisible(true);
		
	}
	
	public static void showModalPanel(String type) throws WrongUrlPanel{
		new URLPanel(type);
	}
	
	public void actionPerformed(ActionEvent e){
		try{
			if (type == "settings"){
				new Parser(URLTextField.getText());
			}
			if (type == "server"){
				Naming.lookup("rmi://"+URLTextField.getText()+"/RemoteSimData");
				Settings.server = URLTextField.getText();
			}
		}
		catch(Exception exc){
			UserInterface.showError("The server cannot be found at that address");
		}
		finally{
			ControlPanel.update();
		}
		setVisible(false);

	}
	
	
}
