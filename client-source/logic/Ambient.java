/**
* Ambient
* 
* Zironda Andrea & Guerra Luca -- PCD 2011/2012
* 
* Wireless Sensor Networks - Clone Detection Simulator
* 
* */
package logic;

import java.text.DecimalFormat;
import java.util.Random;
import java.util.Iterator;
import java.util.Vector;


import stats.NodeStat;
import utilities.Log;
import exceptions.BufferIsFull;
import exceptions.MessageHasNotBeenSent;
import exceptions.NodeNotFound;
import exceptions.NotEnoughEnergy;
import exceptions.PositionNotAvailable;
import messages.Message;
import exceptions.*;
import gui.UserInterface;


/**
 * This class is meant to preserve references to the nodes and apply operations through all of them (messages, threads).
 * It provides its functionalities offering a singleton interface and methods to reach objectives.
 * */

public class Ambient {
	public Vector<Node> nodes;
	
	//This class permits only one instance.
	static Ambient singleton=null;

	private static NodeID clonedNodeID;
	
	//Private constructor to prevent new instances from external code.
	private Ambient(){
		nodes=new Vector<Node>();
	}
	
	//This method is used from external sources to get the only object available of this class.
	static public synchronized Ambient ambient(){
		if (singleton==null){
			singleton=new Ambient();
		}
		return singleton;
	}
	
	//This method adds a node to the ambient.It calls self class method ambient() to get the singleton dinamically.
	public static void  addNode(Node newNode) throws PositionNotAvailable{
		synchronized(ambient().nodes){
			//add node to the field.
			Iterator<Node> i=ambient().nodes.iterator();
			while (i.hasNext()){
				if (newNode.position().equals(i.next())){
					throw new PositionNotAvailable("System tried to sit a node into an occupied position.");
				}
			}
			Log.write("Adding Node to position: "+newNode.position(), "logic.Ambient", "FINE");
			ambient().nodes.add(newNode);
			
			//gui
			UserInterface.addNode(newNode);
			
			Iterator<Node> j=ambient().nodes.iterator();
			Log.write("Updating neighbours..", "logic.Ambient", "FINE");
			//update neighbours.
			while(j.hasNext()){
				Node other=j.next();
				Double distance=Position.distance(newNode.position(), other.position());
				DecimalFormat f=new DecimalFormat("#.##");
				if (distance<=Settings.transmissionRange && distance!=0){
					Log.write("Distance between "+ newNode.info()+" and " + other.info() + " is " +f.format(distance)+" Limit is: " +Settings.transmissionRange, "logic.Ambient", "DEBUG");
					synchronized(newNode.neighbourhood){
						newNode.neighbourhood.add(other);
					}
					synchronized(other.neighbourhood){
						other.neighbourhood.add(newNode);
					}
				}
			}
		}
	}
	
	//the ambient simply deliver messages to nodes in range.It is possible here to simulate some sort
	//of disturbs caused by wireless networking issues.
	public static void sendMessage(Message original, Node sender) throws NullPointerException, NodeIsTooFar{
		if (original==null || sender==null) throw new NullPointerException();
		Log.write("Node "+sender.nid+" is sending a message", "logic.Ambient", "VERBOSE");
		synchronized (sender.neighbourhood){
			Iterator<Node> i=sender.neighbourhood.iterator();
			while (i.hasNext()){
				try{
					Node receiver=i.next();
					if (Position.distance(sender.position(), receiver.position())>Settings.transmissionRange){
						Log.write("Ambient detected a non-legal message", "logic.Ambient", "CRITICAL");
					}
					Message copy=original.clone();
					receiver.receiveMessage(copy);
					if (copy.type()=="ControlMessage"){
						try {
							if (copy.sender().nid.equals(Ambient.clonedNodeID)){
								UserInterface.addMessage(sender, receiver, true);
							}
							else{
								UserInterface.addMessage(sender, receiver, false);
							}
						} catch (NodeNotFound e) {
							/* nulla di iportante*/
						}
					}
					else{
						UserInterface.addMessage(sender, receiver, false);
					}
				} catch (MessageHasNotBeenSent e) {
					Log.write("There was an error delivering a message, receiver didnt find the sender of the node.", "logic.Ambient", "BUG");
				} catch (NotEnoughEnergy e) {
					Log.write("It wasn't possible to deliver a message because the receiver was turned off.", "logic.Ambient", "HIGH");
				} catch (BufferIsFull e) {
					Log.write("It wasn't possible to deliver a message because the receiver's buffer was full", "logic.Ambient", "HIGH");
				}
			}
				
		}	
	}
	
	
	public static void addCloned() throws NoNodesAvailable{
		Log.write("Cloning node..", "logic.Ambient", "FINE");
		if (ambient().nodes.size()==0) throw new NoNodesAvailable("Tried to clone but there weren't nodes");
		Random generator=new Random();
		int i=generator.nextInt(ambient().nodes.size());
		creation:
		try{
			Node attacked=ambient().nodes.elementAt(i);
			Node attacker=attacked.clonedNode(Position.random());
			addNode(attacker);
			UserInterface.setAttackerNode(attacker);
			UserInterface.setAttackedNode(attacked);
			Ambient.clonedNodeID = attacked.nid;
		}
		catch(PositionNotAvailable e){
			break creation;
		}
	}
	
	public static void start(){
		checkNeighbours();
		Log.write("Broadcasting simulation start message..", "logic.Ambient", "FLOW");
		synchronized(ambient().nodes){
			Iterator<Node> i=ambient().nodes.iterator();
			//completeNeighbours();
			while (i.hasNext()){
				i.next().start();
			}
		}
		Log.write("Simulation started", "logic.Ambient", "FLOW");
	}
	
	
	public static void print(){
		Iterator<Node> i=ambient().nodes.iterator();
		String s=new String("PRINTING AMBIENT NODES..:\n");
		while (i.hasNext()){
			s+=i.next();
		}
		Log.write(s);
	}
	
	public static synchronized void kill(){
		Log.write("Killing nodes..", "logic.Ambient", "HIGH");
		synchronized(ambient().nodes){
			Iterator<Node> i=ambient().nodes.iterator();
			while(i.hasNext()){
				Node node=i.next();
				node.interrupt();
			}
			/*deb*/
			Log.write("ACTIVE THREADS "+ new Integer(Thread.activeCount()).toString(), "", "SIMSTATUS");
			/*deb*/
		}
	}
	public static void pause(){
		Hypervisor.pause();
		synchronized(ambient().nodes){
			Iterator<Node> i=ambient().nodes.iterator();
			//completeNeighbours();
			while (i.hasNext()){
				i.next().pause();
			}	
		}
	}
	
	public static void unpause(){
		Hypervisor.unpause();
		synchronized(ambient().nodes){
			Iterator<Node> i=ambient().nodes.iterator();
			//completeNeighbours();
			while (i.hasNext()){
				i.next().unpause();
			}	
		}
	}
	
	
	public static void clear(){
		ambient().nodes.removeAllElements();
		UserInterface.clearSimulation();
	}
	
	public static void printBufferStatus(){
		synchronized(ambient().nodes){
			Iterator<Node> i=ambient().nodes.iterator();
			//completeNeighbours();
			String s=new String("PRINTING BUFFER STATUSES:\n");
			while (i.hasNext()){
				s+=i.next().bufferStatus();
			}
			Log.write(s);
		}
	}	
	
	private static void checkNeighbours(){
		Iterator<Node> i=ambient().nodes.iterator();
		while (i.hasNext()){
			Node current=i.next();
			Iterator<Node> j=current.neighbourhood.iterator();
			while (j.hasNext()){
				Node neighbour=j.next();
				if (Position.distance(current.position(), neighbour.position())>Settings.transmissionRange){
					Log.write("Wrong neighbour! Node "+ neighbour.info() +" in Node "+current.info()+" neighbourhood!", "logic.Ambient", "CRITICAL");
				}
			}
		}
	}

	public static Node nodeFromID(NodeID id) throws NodeNotFound{
		Iterator<Node> i=ambient().nodes.iterator();
		while(i.hasNext()){
			Node current=i.next();
			if (id.equals(current.nid)){
				return current;
			}
		}
		throw new NodeNotFound("Ambient didnt find any node with provided id.");
	}

	public static Vector<NodeStat> getStats() throws NoNodesAvailable{
		
		if (ambient().nodes.size()==0 || ambient().nodes==null) throw new NoNodesAvailable("Tried to get stats when there were no available nodes.");
		else {
			Vector<NodeStat> results=new Vector<NodeStat>();
			Iterator<Node> i=ambient().nodes.iterator();
			while (i.hasNext()){
				results.add(i.next().getStats());
			}
			return results;
		}
	}
	
	
	
}
