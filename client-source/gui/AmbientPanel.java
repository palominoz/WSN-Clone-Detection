/*
* AmbientPanel
* 
* Zironda Andrea -- PCD 2011/2012
* 
* Wireless Sensor Networks - Clone Detection Simulator
* 
* */
package gui;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.border.Border;

import exceptions.GraphicalNodeDoesntExists;

import logic.Node;
import logic.Position;
import java.util.Iterator;
import java.util.Vector;

/*
 * This class manages external request for drawing purposes and contains an instance of AmbientDrawing. The repaint
 * is managed each time something changes within the method calls.
 * 
 * */

public class AmbientPanel extends JFrame{
	
	/*** SINGLETON IMPLEMENTATION ***/
	private static AmbientPanel singleton=null;
	
	public static AmbientPanel ambientPanel(){
		if (singleton==null) singleton=new AmbientPanel();
		return singleton;
	}
	
	
	
	/***CLASS MEMBERS***/
	// flags if the paths are shown
	public static boolean allMessagesToBeShown=false;
	// flags if the ranges are shown 
	public static boolean rangesToBeShown=false;
	// flags if the ids are shown
	public static boolean IDsToBeShown=false;
	// flags if idle nodes should be highlighted
	public static boolean IdleNodesToBeShown=false;
	// field
	private static AmbientDrawing field=new AmbientDrawing();
	
	
	/***INSTANCE MEMBERS***/
	// records where the nodes are
	public Vector<GraphicalNode> nodes=new Vector<GraphicalNode>();
	// records where the message jumps have occurred
	public Vector<GraphicalJump> messageList=new Vector<GraphicalJump>();
	
	/***CONSTRUCTORS***/
	AmbientPanel(){
		super("Ambient panel");
		Container content=getContentPane();
		
		content.setLayout(new GridBagLayout());
		
		Border border=BorderFactory.createLineBorder(Color.BLACK);
		field.setBorder(BorderFactory.createTitledBorder(border, "Simulation field"));
		field.setBorder(border);
		//field.setBorder(BorderFactory.createTitledBorder("Simulation Field"));
		
		//content.add(field, BorderLayout.NORTH);
		
		//margine
		GridBagConstraints constraints=new GridBagConstraints();
		constraints.insets = new Insets( 4, 4, 4, 4);
		constraints.gridx = constraints.gridy = 0;  
	    constraints.gridwidth = constraints.gridheight = 1;  
	    constraints.fill = GridBagConstraints.BOTH;  
	    constraints.anchor = GridBagConstraints.NORTHWEST;  
	    constraints.weightx = constraints.weighty = 100;  
		
		content.add(field,constraints);
		
		setSize(new Dimension(700,700));
		setVisible(true);
	}
	
	/***CLASS METHODS***
	 */
	
	// adds a node
	public static void addNode(Node n){
		synchronized (ambientPanel().nodes){
			ambientPanel().nodes.add(new GraphicalNode(n));
		}
		field.repaint();
	}
	// set node as attacked
	public static void setAttackedNode(Node n) throws GraphicalNodeDoesntExists{
		synchronized(ambientPanel().nodes){
			Iterator<GraphicalNode> i=ambientPanel().nodes.iterator();
			while (i.hasNext()){
				GraphicalNode gn=i.next();
				if (gn.equals(n)){
					gn.isAttacked=true;
					return;
				}
			}
			throw new GraphicalNodeDoesntExists("Tried to set attacked node but it doesnt exists.");
		}
	}
	
	// set node as attacker
	public static void setAttackerNode(Node n) throws GraphicalNodeDoesntExists{
		synchronized(ambientPanel().nodes){
			Iterator<GraphicalNode> i=ambientPanel().nodes.iterator();
			while (i.hasNext()){
				GraphicalNode gn=i.next();
				if (gn.equals(n)){
					gn.isAttacker=true;
					field.repaint();
					return;
				}
			}
			throw new GraphicalNodeDoesntExists("Tried to set attacked node but it doesnt exists.");
		}
	}
	// set node as idle
	public static void setIdleNode(Node n) throws GraphicalNodeDoesntExists{
		synchronized(ambientPanel().nodes){
			Iterator<GraphicalNode> i=ambientPanel().nodes.iterator();
			while (i.hasNext()){
				GraphicalNode gn=i.next();
				if (gn.equals(n)){
					gn.isIdle=!gn.isIdle;
					field.repaint();
					return;
				}
			}
			throw new GraphicalNodeDoesntExists("Tried to set attacked node but it doesnt exists.");
		}
	}
	
	// clean things up
	public static void clearSimulation(){
		ambientPanel().nodes.removeAllElements();
		ambientPanel().messageList.removeAllElements();
		field.repaint();
	}
	
	// set node as highlighted
	public static void setHighlightedNode(Node n) throws GraphicalNodeDoesntExists{
		synchronized(ambientPanel().nodes){
			Iterator<GraphicalNode> i=ambientPanel().nodes.iterator();
			while (i.hasNext()){
				GraphicalNode gn=i.next();
				if (gn.equals(n)){
					gn.isHighlighted=!gn.isHighlighted;
					field.repaint();
					return;
				}
			}
			throw new GraphicalNodeDoesntExists("Tried to set attacked node but it doesnt exists.");
		}
	}
	// set node as dead(finished energy)
	public static void setDeadNode(Node n) throws GraphicalNodeDoesntExists{
		synchronized(ambientPanel().nodes){
			Iterator<GraphicalNode> i=ambientPanel().nodes.iterator();
			while (i.hasNext()){
				GraphicalNode gn=i.next();
				if (gn.equals(n)){
					gn.isDead=true;
					field.repaint();
					return;
				}
			}
			throw new GraphicalNodeDoesntExists("Tried to set attacked node but it doesnt exists.");
		}
	}
	// set node as detector
	public static void setDetectorNode(Node n) throws GraphicalNodeDoesntExists {
		synchronized(ambientPanel().nodes){
			Iterator<GraphicalNode> i=ambientPanel().nodes.iterator();
			while (i.hasNext()){
				GraphicalNode gn=i.next();
				if (gn.equals(n)){
					gn.isDetector=true;
					field.repaint();
					return;
				}
			}
			throw new GraphicalNodeDoesntExists("Tried to set attacked node but it doesnt exists.");
		}
	}
	// set node as clone
	public static void setClonedNode(Position p) throws GraphicalNodeDoesntExists{
		synchronized(ambientPanel().nodes){
			Iterator<GraphicalNode> i=ambientPanel().nodes.iterator();
			while (i.hasNext()){
				GraphicalNode gn=i.next();
				if (gn.node.position().equals(p)){
					gn.isClone=true;
					field.repaint();
					return;
				}
			}
			throw new GraphicalNodeDoesntExists("Tried to set cloned node but it doesnt exists.");
		}
	}
	
	// records a message jump
	public static void addMessage(Position s, Position e, boolean h){
		GraphicalJump jump = new GraphicalJump(s,e,h);
		synchronized(ambientPanel().messageList){
			if (ambientPanel().messageList.contains(jump) == false) {
				ambientPanel().messageList.add(jump);
			}
		}
		field.repaint();
	}
	
	// set messages to be shown
	public static void setMessagesToBeShown() {
		allMessagesToBeShown=!allMessagesToBeShown;
		field.repaint();
	}
	// set ranges to be shown
	public static void setRangesToBeShown() {
		rangesToBeShown=!rangesToBeShown;
		field.repaint();
	}
	// turn on/off drawing
	public static void setActive(boolean active){
		field.setActive(active);
		field.repaint();
	}
	// set ids to be shown
	public static void setIDsToBeShown() {
		IDsToBeShown=!IDsToBeShown;
		field.repaint();
	}
	// set idle nodes to be tagged
	public static void setIdleNodesToBeShown() {
		IdleNodesToBeShown=!IdleNodesToBeShown;
		field.repaint();
	}
	
	
	
	/**
	 * Wrapper class to record in which position a jump of a message occurred
	 * 
	 * */
	public static class GraphicalJump{
		
		
		public Position start;
		public Position end;
		public boolean highlighted;
		public int length;
		
		GraphicalJump(Position s, Position e, boolean h){
			start = s;
			end = e;
			highlighted = h;
			length=0;
		}
		
		
		public boolean equals(GraphicalJump i){
			boolean same = i.start.equals(start) && i.end.equals(end) || i.start.equals(end) && i.end.equals(start);
			if (same && i.highlighted) highlighted = true;
			return same;
		}
		
		
		public String toString(){
			return start.toString() + " " + end.toString();
		}
		
	};
	
	/**
	 * Modeling the graphical representation of a node
	 * **/
	public static class GraphicalNode{
		Node node;
		boolean isAttacked=false;
		boolean isAttacker=false;
		boolean isClone=false;
		public boolean isHighlighted=false;
		public boolean isIdle=false;
		public boolean isDead=false;
		public boolean isDetector;
		
		GraphicalNode(Node n){
			node=n;
		}
		
		public boolean equals(Node n){
			return node.equals(n);
		}
		
		public String idleString(){
			return "(IDLE)";
		}
		
		public String toString(){
			if (AmbientPanel.IdleNodesToBeShown && isIdle){
				return node.info().nid.toString()+ " "+idleString();
			}
			else{
				return node.info().nid.toString();
			}
		}
	};
	
	
}