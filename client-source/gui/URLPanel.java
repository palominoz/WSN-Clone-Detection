/**
* URLPanel
* 
* Zironda Andrea -- PCD 2011/2012
* 
* Wireless Sensor Networks - Clone Detection Simulator
* 
* */

package gui;

import java.awt.*;
import java.awt.event.*;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import javax.swing.*;

import commonInterface.RemoteServer;

import exceptions.WrongUrlPanel;
import parser.*;

import logic.Hypervisor;
import logic.Settings;
/**
 * This class provides a panel in which the server or the configuration file url can be set.
 * 
 * */
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
				RemoteServer s= (RemoteServer)Naming.lookup("rmi://"+URLTextField.getText()+"/RemoteServer");
				Settings._server = s;
				Settings.server = URLTextField.getText();
				ControlPanel.update();
			}
			setVisible(false);
		} catch (MalformedURLException e0) {
			UserInterface.showError("The url inserted is not correct");
		} catch (RemoteException e1) {
			UserInterface.showError("The server you have inserted does not respond");
		} catch (NotBoundException e2) {
			UserInterface.showError("Generic error");
		}
		
	}
	
	
}
