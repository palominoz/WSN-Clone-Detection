package gui;

import javax.swing.JOptionPane;

public class UserInterface {
	
	public static void showError(String text){
		JOptionPane.showMessageDialog(null,
			    text,
			    "Error",
			    JOptionPane.ERROR_MESSAGE);
	}
	
	public static void showMessage(String text){
		JOptionPane.showMessageDialog(null,
			    text,
			    "System Message",
			    JOptionPane.PLAIN_MESSAGE);
	}
}
