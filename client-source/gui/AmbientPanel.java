package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import utilities.Log;

import exceptions.GraphicalNodeDoesntExists;
import exceptions.NodeNotFound;

import logic.Node;
import logic.Position;
import messages.Message;
import java.awt.geom.Ellipse2D;
import java.util.Iterator;
import java.util.Random;
import java.util.Vector;

public class AmbientPanel extends JFrame{
	public Vector<GraphicalNode> nodes=new Vector<GraphicalNode>();
	
	public static boolean allMessagesToBeShown=false;
	
	public static boolean rangesToBeShown=false;
	
	public static boolean IDsToBeShown=false;
	
	public static boolean IdleNodesToBeShown=false;
	
	public static class MessageListItem{
		
		static int defaultRefreshCount=1;
		
		public Message message;
		public int length;
		public int refreshCount;
		Color color;
		
		MessageListItem(Message m){
			message=m;
			length=0;
			Random generator=new Random();
			color=new Color(generator.nextInt(255),generator.nextInt(255),generator.nextInt(255));
			refreshCount=defaultRefreshCount;
		}
		
		
		public boolean equals(MessageListItem i){
			return i.message==message;
		}
		
		
		public String toString(){
			try {
				return new String("\nSENDER: "+message.sender().info().nid+"\nLENGTH: "+message.pathLength()+"\n" );
			} catch (NullPointerException e) {
				Log.write("Tried to convert to string a MessageListItem, which has some NullPointerException", "gui.AmbientPanel", "BUG");
			} catch (NodeNotFound e) {
				Log.write("Tried to convert to string a MessageListItem, which has a message without a sender", "gui.AmbientPanel", "BUG");
			}
			return new String();
		}
		
	};
	
	public static class GraphicalNode{
		Node node;
		boolean isAttacked=false;
		boolean isAttacker=false;
		boolean isClone=false;
		public boolean isHighlighted=false;
		public boolean isIdle=false;
		public boolean isDead=false;
		
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
	
	
	public Vector<MessageListItem> messageList=new Vector<MessageListItem>();
	
	private static AmbientPanel singleton=null;
	
	public static AmbientPanel ambientPanel(){
		if (singleton==null) singleton=new AmbientPanel();
		return singleton;
	}
	
	private static AmbientDrawing field=new AmbientDrawing();

	
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
	
	public static void addNode(Node n){
		synchronized (ambientPanel().nodes){
			ambientPanel().nodes.add(new GraphicalNode(n));
		}
		field.repaint();
	}
	
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
	
	
	public static void clearSimulation(){
		ambientPanel().nodes.removeAllElements();
		ambientPanel().messageList.removeAllElements();
		field.repaint();
	}
	
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

	public static void addMessage(Message m){
		MessageListItem i=new MessageListItem(m);
		synchronized(ambientPanel().messageList){
			if (ambientPanel().messageList.contains(i)) return;
			else ambientPanel().messageList.add(i);
		}
		field.repaint();
	}

	public static void setMessagesToBeShown() {
		allMessagesToBeShown=!allMessagesToBeShown;
		field.repaint();
	}

	public static void setRangesToBeShown() {
		rangesToBeShown=!rangesToBeShown;
		field.repaint();
	}

	public static void setIDsToBeShown() {
		IDsToBeShown=!IDsToBeShown;
		field.repaint();
	}
	
	public static void setIdleNodesToBeShown() {
		IdleNodesToBeShown=!IdleNodesToBeShown;
		field.repaint();
	}
	
}