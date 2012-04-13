package gui;

import java.awt.Component;

import javax.swing.JOptionPane;

import utilities.Log;
import exceptions.GraphicalNodeDoesntExists;
import logic.Node;
import logic.NodeID;
import logic.Position;
import messages.Message;

public class UserInterface {
	private UserInterface singleton=null;
	
	private UserInterface userInterface(){
		synchronized(singleton){
			if (singleton==null) singleton=new UserInterface();
			return singleton;
		}
	}
	
	UserInterface(){}
	
	public static void addNode(Node node){
		AmbientPanel.addNode(node);
		ControlPanel.controlPanel().IDsCombo.addItem(node.info().nid);
	}
	
	public static void clearSimulation(){
		AmbientPanel.clearSimulation();
	}
	
	public static void setIdleNode(Node node){
		try {
			AmbientPanel.setIdleNode(node);
		} catch (GraphicalNodeDoesntExists e) {
			Log.write("User Interface cannot find the node requested to be set as idle", "gui.UserInterface", "HIGH");
		}
	}
	
	public static void setAttackerNode(Node node){
		try {
			AmbientPanel.setAttackerNode(node);
		} catch (GraphicalNodeDoesntExists e) {
			Log.write("User Interface cannot find the node requested to be set as attacker", "gui.UserInterface", "HIGH");
		}
	}
	public static void setAttackedNode(Node node){
		try {
			AmbientPanel.setAttackedNode(node);
		} catch (GraphicalNodeDoesntExists e) {
			Log.write("User Interface cannot find the node requested to be set as attacked", "gui.UserInterface", "HIGH");
		}
	}
	public static void setClonedNode(Position position){
		try {
			AmbientPanel.setClonedNode(position);
		} catch (GraphicalNodeDoesntExists e) {
			Log.write("User Interface cannot find the node requested to be set as cloned", "gui.UserInterface", "HIGH");
		}
	}
	
	public static void addMessage(Message message){
		AmbientPanel.addMessage(message);
	}
	
	public static void showError(Component parent, String error){
		JOptionPane.showMessageDialog(parent, error, "Dialog",JOptionPane.ERROR_MESSAGE);
	}
	
	
	
	public static void wakeUI(){
		ControlPanel.controlPanel();
	}
	
	public static void main(String args[]){
		wakeUI();
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

	public static void setDeadNode(Node node) {
		try {
			AmbientPanel.setDeadNode(node);
		} catch (GraphicalNodeDoesntExists e) {
			Log.write("User Interface cannot find the node requested to be set as attacked", "gui.UserInterface", "HIGH");
		}
	}
	
}
