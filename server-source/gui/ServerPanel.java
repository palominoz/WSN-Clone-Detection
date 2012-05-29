package gui;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


@SuppressWarnings("serial")
public class ServerPanel extends JFrame implements ActionListener{
	
	private static ServerPanel singleton=null;
	
	public static ServerPanel serverPanel(){
		if (singleton==null) singleton=new ServerPanel();
		return singleton;
	}
	
	public static void setVisibity(boolean b){serverPanel().setVisible(b);}
	

	private static JTextArea resultsTextArea = new JTextArea();

	private JComboBox simulationsCombo=new JComboBox();
	
	private String statistics=new String();
	
	
	public void actionPerformed(ActionEvent event){
		
	}
	
	private ServerPanel(){
		super("Server Panel");
		
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
		resultsTextArea.setText(statistics);
	}
	
	public static void pushStats(String stats){
		if (!stats.equals("\n"))  serverPanel().statistics += stats + " ";
		else serverPanel().statistics += stats;
		singleton.update();
	}
	
	
		
}

