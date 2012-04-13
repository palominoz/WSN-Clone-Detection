package gui;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;


public class ServerPanel extends JFrame implements ActionListener{
	
	private static ServerPanel singleton=null;
	
	public static ServerPanel serverPanel(){
		if (singleton==null) singleton=new ServerPanel();
		return singleton;
	}
	
	public static void setVisibity(boolean b){serverPanel().setVisible(b);}
	

	private static JTextArea resultsTextArea = new JTextArea("SIMULATION RESULTS:");

	private JComboBox simulationsCombo=new JComboBox();
	
	private Vector<String> statistics=new Vector<String>();
	
	
	public void actionPerformed(ActionEvent event){
		if ("selected".equals(event.getActionCommand())){
			resultsTextArea.setText(statistics.elementAt(simulationsCombo.getSelectedIndex()));
		}
	}
	
	private ServerPanel(){
		super("Server Panel");
		
		JPanel layout=new JPanel();
		
		simulationsCombo.setActionCommand("selected");
		//Container container=getContentPane();
		//add(layout);
		add(simulationsCombo,BorderLayout.NORTH);
		add(resultsTextArea,BorderLayout.CENTER);
		//container.add(layout);
		
		setSize(new Dimension(600,100));
		setVisible(true);
		
	}
	
	private void update(){
		resultsTextArea.setText(statistics.lastElement());
	}
	
	public static void pushStats(String stats){
		serverPanel().statistics.add(stats);
		singleton.update();
	}
	
	
		
}

