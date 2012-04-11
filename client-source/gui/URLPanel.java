package gui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import parser.*;

import logic.Hypervisor;

public class URLPanel extends JFrame implements ActionListener{
	
	private JLabel URLLabel=new JLabel("Please insert a valid url pointing to the configuration file");
	private JTextField URLTextField = new JTextField("http://www.math.unipd.it/~conti/teaching/PCD1112/project_config.txt");
	private JButton confirmButton=new JButton("Done");
	
	
	private URLPanel(){
		super("Please insert a valid URL");
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
	
	public static void showModalPanel(){
		new URLPanel();
	}
	
	public void actionPerformed(ActionEvent e){
		try{
			Parser parser=new Parser(URLTextField.getText());	
		}
		finally{}
		ControlPanel.setOverallStatusText("READY");
		setVisible(false);

	}
	
	
}
