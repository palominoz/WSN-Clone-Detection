/*
* LogPanel
* 
* Zironda Andrea -- PCD 2011/2012
* 
* Wireless Sensor Networks - Clone Detection Simulator
* 
* */
package gui;

import java.awt.Color;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

/*
 * This class models a panel that logs strings. 
 * 
 * */

public class LogPanel extends JFrame {
	
	private static LogPanel singleton = null;
	
	private static String html = "<html>";
	
	public static LogPanel logPanel(){
		if (singleton == null){
			singleton = new LogPanel();
		}
		return singleton;
	}
	
	private JTextPane textArea = new JTextPane();
	

	
	private LogPanel(){
		super("Log Panel");
		setSize(700,240);
		JScrollPane pane = new JScrollPane(textArea);
		textArea.setBackground(Color.black);
		textArea.setEditable(false);
		getContentPane().add(pane);
		setVisible(true);
	}
	
	
	public static void write(String what, Color color){
		try {
		    // Get the text pane's document
		    StyledDocument doc = (StyledDocument)logPanel().textArea.getDocument();

		    // Create a style object and then set the style attributes
		    Style style = doc.addStyle("StyleName", null);

		    // Italic
		    //StyleConstants.setItalic(style, true);

		    // Bold
		    StyleConstants.setBold(style, true);

		    // Font family
		    StyleConstants.setFontFamily(style, "SansSerif");

		    // Font size
		    //StyleConstants.setFontSize(style, 30);

		    // Background color
		    //StyleConstants.setBackground(style, Color.black);

		    // Foreground color
		    StyleConstants.setForeground(style, color);

		    // Append to document
		    doc.insertString(0, timestamp()+ " || ", defaultStyle());
		    doc.insertString(timestamp().length() + 4, what + "\n", style);
		} catch (BadLocationException e) {
		}
	}
	
	private static Style defaultStyle(){
		 // Get the text pane's document
	    StyledDocument doc = (StyledDocument)logPanel().textArea.getDocument();

	    // Create a style object and then set the style attributes
	    Style style = doc.addStyle("StyleName", null);
	    StyleConstants.setBold(style, true);
	    
	    StyleConstants.setForeground(style, Color.white);
	    
	    return style;
	}
	
	public static String timestamp(){
		java.util.Date date= new java.util.Date();
		Timestamp t = new Timestamp(date.getTime());
		return new SimpleDateFormat("H:mm:ss").format(t);
	}

}
