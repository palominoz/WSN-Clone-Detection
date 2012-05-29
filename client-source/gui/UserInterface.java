/**
* UserInterface
* 
* Zironda Andrea -- PCD 2011/2012
* 
* Wireless Sensor Networks - Clone Detection Simulator
* 
* */
package gui;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JOptionPane;

import utilities.Log;
import exceptions.GraphicalNodeDoesntExists;
import logic.Hypervisor;
import logic.Node;
import logic.Position;

/**
 * This class is the facade pattern implementation. Acts as a bridge between logic classes and graphical classes. 
 * It's method are highly representing the events occurring between the two worlds.
 * */

public class UserInterface {
	
	
	private UserInterface singleton=null;
	
	private static boolean disabled = false;
	
	
	@SuppressWarnings("unused")
	private UserInterface userInterface(){
		synchronized(singleton){
			if (singleton==null) singleton=new UserInterface();
			return singleton;
		}
	}
	
	UserInterface(){}

	public static boolean enabled(){
		return !disabled;
	}
	
	public static void enable(){
		if (disabled == false) disabled = true;
	}
	
	public static void disable(){
		if (disabled == true) disabled = false;
	}
	
	
	public static void addNode(Node node){
		if (disabled) return;
		AmbientPanel.addNode(node);
		ControlPanel.controlPanel().IDsCombo.addItem(node.info().nid);
	}
	
	public static void clearSimulation(){
		if (disabled) return;
		AmbientPanel.clearSimulation();
	}
	
	public static void setIdleNode(Node node){
		if (disabled) return;
		try {
			AmbientPanel.setIdleNode(node);
		} catch (GraphicalNodeDoesntExists e) {
			Log.write("User Interface cannot find the node requested to be set as idle", "gui.UserInterface", "HIGH");
		}
	}
	
	public static void setAttackerNode(Node node){
		if (disabled) return;
		try {
			AmbientPanel.setAttackerNode(node);
		} catch (GraphicalNodeDoesntExists e) {
			Log.write("User Interface cannot find the node requested to be set as attacker", "gui.UserInterface", "HIGH");
		}
	}
	
	public static void setDetectorNode(Node detector) {
		if (disabled) return;
		try {
			AmbientPanel.setDetectorNode(detector);
		} catch (GraphicalNodeDoesntExists e) {
			Log.write("User Interface cannot find the node requested to be set as attacker", "gui.UserInterface", "HIGH");
		}
	}
	public static void setAttackedNode(Node node){
		if (disabled) return;
		try {
			AmbientPanel.setAttackedNode(node);
		} catch (GraphicalNodeDoesntExists e) {
			Log.write("User Interface cannot find the node requested to be set as attacked", "gui.UserInterface", "HIGH");
		}
	}
	public static void setClonedNode(Position position){
		if (disabled) return;
		try {
			AmbientPanel.setClonedNode(position);
		} catch (GraphicalNodeDoesntExists e) {
			Log.write("User Interface cannot find the node requested to be set as cloned", "gui.UserInterface", "HIGH");
		}
	}
	
	public static void addMessage(Node sender, Node receiver, boolean highlighted){
		if (disabled) return;
		AmbientPanel.addMessage(sender.position(), receiver.position(), highlighted);
	}
	
	public static void showError(Component parent, String error){
		JOptionPane.showMessageDialog(parent, error, "Dialog",JOptionPane.ERROR_MESSAGE);
	}
	
	
	public static void log(String what, Color color){
		LogPanel.write(what, color);
	}
	
	public static void wakeUI(){
		ControlPanel.controlPanel();
		LogPanel.write("Graphical User Interface has been waked.", Color.orange);
	}
	
	public static void main(String args[]){
		wakeUI();
		Hypervisor.wake();
	}

	public static void notifyEndOfSimulation(Integer indexOfSimulation) {
		ControlPanel.notifyEndOfSimulation(indexOfSimulation);
	}
	
	public static void notifyStartOfSimulation(Integer indexOfSimulation) {
		ControlPanel.notifyStartOfSimulation(indexOfSimulation);
	}
	
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

	public static void setDeadNode(Node node) {
		if (disabled) return;
		try {
			AmbientPanel.setDeadNode(node);
		} catch (GraphicalNodeDoesntExists e) {
			Log.write("User Interface cannot find the node requested to be set as attacked", "gui.UserInterface", "HIGH");
		}
	}

	public static void notifySentSimulation(int currentSimulation) {
		
	}
	
}
