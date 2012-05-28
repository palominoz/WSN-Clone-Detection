/*
* Node
* 
* Zironda Andrea & Guerra Luca -- PCD 2011/2012
* 
* Wireless Sensor Networks - Clone Detection Simulator
* 
* */
package logic;

import java.util.Vector;

import java.lang.String;
import exceptions.*;

import java.util.Iterator;
import messages.*;
import stats.NodeStat;
import utilities.Log;
import utilities.Monitor;
import enums.*;
import gui.UserInterface;


/*
 * Istances of this class simulate the behaviour of real WSN member nodes.
 * As the documentation provided said, the nodes know other nodes in range by default.It was not implemented any 
 * algorithm to reach that objective.Neighbourhood is updated when adding a node to the system.
 * Messages are simulated by method calls.In fact concurrency is not a big part of this class.The only real concurrent action 
 * is the initiation of the simulation: the forwarding of a message are sequential actions and does not need open threads to
 * have the message reach its destination.
 * 
 * */
abstract public class Node extends Thread{

/***CLASS MEMBERS***/
/***INSTANCE MEMBERS***/
	//The position is not intended to be modified.
	private final Position position;
	
	//every node has a life represented by its energy.
	double energyPool;
	
	public boolean nodeShouldStayActive=true;
	
	//an id for the node.Clones are spotted checking these ids.
	NodeID nid;
	
	//every node knows reachable nodes in their range.Must be sync externally.
	public Vector<Node> neighbourhood;
	
	//every node stores the ids they reiceve from other nodes and their position.
	Vector<NodeInfo> storedNodes;
	// each node has one buffer.
	MessageBuffer buffer;
	// prevents multiple notifications to the hypervisor
	private boolean idle = false;
	
	// pause implementative
	private Monitor monitor=new Monitor(false);
	
	//simulation stats
	NodeStat stats;
	/***CONSTRUCTORS***/
	//create new node with position.
		public Node(Position p) throws NidOverflow{
			turnoff();
			setDaemon(true);
			nid=new NodeID();
			position=p;
			stats=new NodeStat();
			neighbourhood=new Vector<Node>();
			buffer=new MessageBuffer();
			energyPool=Settings.defaultMaxEnergy;
			storedNodes=new Vector<NodeInfo>();
			Log.write("Node created", "logic.Node", "FINE");
			setName(info().toString());
		}
		
		//private implementation constructor to clone node.
		protected Node(Position p, NodeID id){
			turnoff();
			setDaemon(true);
			nid=id;
			position=p;
			stats=new NodeStat();
			neighbourhood=new Vector<Node>();
			buffer=new MessageBuffer();
			energyPool=Settings.defaultMaxEnergy;
			storedNodes=new Vector<NodeInfo>();
			Log.write("Node created", "logic.Node", "FINE");
			setName(info().toString());
		}
	/***CLASS METHODS***/
	/***INSTANCE METHODS***/
	
	// notifies hypervisor about idle status
	private void idle(){
		if (idle == false){
			Hypervisor.notifyIdle(this);
			idle =true;
		}
	}
	// notifies hypervisor about simulating status
	private void simulating(){
		if (idle && nodeShouldStayActive){
			Hypervisor.notifySimulating(this);
		}
		idle = false;
	}
	
	// get wrapped node information
	public NodeInfo info(){
		return new NodeInfo(this);
	}
	// get string status about the buffer
	public String bufferStatus(){
		return buffer.bufferStatus();
	}
	// get the size of the buffer
	public int bufferSize(){
		return buffer.size();
	}
	
	//creates and returns cloned node in a different position.
	abstract public Node clonedNode(Position p);
	
	
	
	//any finishing job before transmitting should be here.
	public final void sendMessage(Message m) throws BadNeighbour, NotEnoughEnergy{
		try{
			useEnergy(Settings.transmitConsumption);
			Ambient.sendMessage(m, this);
			//update stats
			stats.sent();
		}
		catch (NodeIsTooFar e){
			throw new BadNeighbour("This node tried to send a message to a too far neighbour");
		}
	}
	
	//forwarding of a message forecasts the change of the local destination.
	public void forwardMessage(ControlMessage m) throws NoNeighboursAvailable, NoCloserDestinatary, BadNeighbour, NotEnoughEnergy{
		m.local_destination=neighbourForDestination(m.global_destination);
		sendMessage(m);
	}
	
	//message goes in buffer
	public void receiveMessage(Message message) throws NullPointerException, MessageHasNotBeenSent, NotEnoughEnergy, BufferIsFull {
		Log.write("Node "+nid+" is reiceving a message", "logic.Node", "VERBOSE");
		useEnergy(Settings.receiveConsumption);
		buffer.pushBuffer(message);
		//update stats
		stats.reiceved();
		
	}
	
	// checks if this node is the destination of the control message
	public boolean isLocalDestinationOf(ControlMessage message){
		Double myDistance=Position.distance(position, message.local_destination);
		synchronized (neighbourhood){
			Iterator<Node> i=neighbourhood.iterator();
			while (i.hasNext()){
				if (Position.distance(i.next().position, message.local_destination)<myDistance) return false;
			}
			return true;
		}
	}
	
	
	//subclass specialized method.
	abstract protected void manageMessage(Message m) throws MessageNotSupportedByNode, NullPointerException, NotEnoughEnergy, CloneHasBeenDetected, NodeNotFound;
	
	//choice of neighbour based on the overall minimum distance from the destination.
	protected Position neighbourForDestination(Position global) throws NoNeighboursAvailable, NoCloserDestinatary, NullPointerException{
		if (global==null) throw new NullPointerException("Tried to calculate the neighbour for a null global destination");
		if (neighbourhood == null) throw new NullPointerException("Tried to dereference a null neighbouhood vector");
		if (neighbourhood.size()==0) throw new NoNeighboursAvailable("This node does not have neighbours at range");
		Iterator<Node> i=neighbourhood.iterator();
		double minDistance=Double.MAX_VALUE;
		Position local=null;
		while(i.hasNext()){
			Node neighbour=i.next();
			double currentDistance=Position.distance(global, neighbour.position);
			if (currentDistance<minDistance){
				minDistance=currentDistance;
				local=neighbour.position;
			}
		}
		if (Position.distance(global, position)<minDistance){
			throw new NoCloserDestinatary("Node failed to forward message because there are no closer nodes than himself.");
		}
		if (local!=null){return local;}
		else throw new NoNeighboursAvailable("This node got a big big error.");
	}
	
	// no implementation yet. just energy
	protected void encodeSignature() throws NotEnoughEnergy{
		useEnergy(Settings.signatureConsumption);
	}
	
	//this function will be used in subclasses.
	protected void decodeSignature(NodeInfo potentialClone) throws NotEnoughEnergy, CloneHasBeenDetected{
		useEnergy(Settings.signatureConsumption);
		stats.decoded();
		Iterator<NodeInfo> i=storedNodes.iterator();
		boolean toAdd=true;
		
		while(i.hasNext()){
			NodeInfo storedInfo=i.next();
			if (storedInfo.nid.equals(potentialClone.nid) ){
				//Log.write("CHECK: " + storedInfo+ " && "+ potentialClone);
				if (storedInfo.position.equals(potentialClone.position)){
					Log.write("Potential clone", "logic.Node", "HIGH");
					toAdd=false;
				}
				else{
					Log.write("FOUND CLONE DETAILS: id+pos 1)" +storedInfo+" 2) "+potentialClone, "logic.Node", "MEDIUM");
					throw new CloneHasBeenDetected(potentialClone.position, this);
				}
			}
		}
		if (toAdd) {
			storedNodes.add(potentialClone);
			stats.stored();
		}
	}
	
	
	//this method is used to use energy from the pool.
	//WARN:It can be upgraded building another nested class.
	private void useEnergy(double e) throws NotEnoughEnergy{
		if (e>energyPool) throw new NotEnoughEnergy();
		energyPool=energyPool-e;
		stats.consumed(e);
	}
	
	
	// getter
	public synchronized Position position(){
		return position;
	} 
	
	
	//switch off node
	public void turnoff(){
		interrupt();
	}
	
	//switch on node
	public void turnon(){
		start();
	}
	
	public void pause(){
		synchronized(monitor){
			monitor.paused=true;
		}
	}
	
	public void unpause(){
		synchronized(monitor){
			monitor.paused=false;
			monitor.notify();
		}
	}
	
	public NodeStat getStats(){
		return stats;
	}
	
	private void listen() throws NodeIsNotActive, NullPointerException, InterruptedException, MessageNotSupportedByNode, NotEnoughEnergy, CloneHasBeenDetected, NodeNotFound{
		Message message=buffer.popBuffer();//qui entra in wait
		Log.write("Node "+nid+" is managing message", "logic.Node", "VERBOSE");
		manageMessage(message);
	}
	
	
	
	
	/*
	 * this function is core activity of the node.A node listens to incoming messages.When status changes to simulating a
	 * location claim is sent.Then the node restarts listening to incoming messages.
	 * 
	 * */
	public void run(){
		try{
			encodeSignature();
			sendMessage(new LocationClaim(this));
			while (nodeShouldStayActive){ //while the switch is on
				if (!monitor.paused) {
					listen();
				}
				else {
					Log.write("Node "+ nid + " is paused", "logic.Node", "LOW");
					synchronized(monitor){
						monitor.wait();
					}
				} 
			}
		}
		catch (InterruptedException e){
			if (bufferSize() != 0){
				Log.write("Node "+nid+" was interrupted with something to do", "logic.Node", "BUG");
			}
		}
		catch (BadNeighbour e){
			Log.write("Node "+nid+" find a neighbour too far from himself", "logic.Node", "BUG");
		} catch (NullPointerException e) {
			Log.write("Node "+nid+" "+ e.getMessage(), "logic.Node", "BUG");
		} catch (NotEnoughEnergy e) {
			UserInterface.setDeadNode(this);
			Log.write("Node "+nid+" finished its energy and will be turned off", "logic.Node", "CRITICAL");
			idle();
		} catch (NodeIsNotActive e) {
			Log.write("Node is going to be turned off..", "logic.Node", "HIGH");
		} catch (CloneHasBeenDetected e){
			Hypervisor.notifyClone(e.clonePosition, e.detectorNode);
			stats.foundClone();
		} catch (MessageNotSupportedByNode e){
			Log.write("Node "+nid+" reiceved a not supported message", "logic.Node", "CRITICAL");
		} catch (NodeNotFound e) {
			Log.write("Node "+nid+" didnt find the sender of a message while trying to deconde signature", "logic.Node", "BUG");
		} 
		finally{
			Log.write("Node "+nid+" has been turned off.", "logic.Node", "LOW");
			idle();
		}
	}
	
	
	// wakes up the node
	public void wake(){
		synchronized(buffer){
			buffer.notifyAll();
		}
	}
	
	
	//util methods
	public String toString(){
		String s= "|***|\nNODE "+info()+"\n NEIGHBOURHOOD:\n";
		Iterator<Node> i=neighbourhood.iterator();
		while(i.hasNext()){
			s+="\t"+i.next().info()+"\n";
		}
		s+="\n";
		return s;
	}
	
	
	public synchronized boolean equals(Node n){
		synchronized(n){
			Log.write("CHECKING NODE: id "+this.nid+ " pos:" +this.position+" WITH NODE: id"+ n.nid+ " pos: "+n.position+ " RESULT: "+ new Boolean(nid==n.nid && position==n.position).toString(), "logic.Node", "USELESS");
		}
		return nid.equals(n.nid) && position.equals(n.position);
	}
	
	/**
	 * A message buffer contains the messages of the node to manage. It handles concurrency between node threads.
	 */
	class MessageBuffer{
		//message buffer.
		private Vector<Message> buffer=new Vector<Message>();
		
		MessageBuffer(){}
		
		// lauched on the first node, ensures all the nodes present with the iterator have an empty buffer at the same time
		public synchronized boolean recursiveLock(Iterator<Node> it){
			if (it.hasNext()){
				if (size() != 0){
					return false;
				}
				else{
					return it.next().buffer.recursiveLock(it);
				}
			}
			else{
				if (size()==0){
					return true;
				}
				else return false;
			}
		}

		public synchronized int size() throws NullPointerException{
			if (buffer==null) throw new NullPointerException("Asked for size in a MessageBuffer which has null buffer.");
			return buffer.size();
		}
		
		public synchronized String bufferStatus(){
			if (size()!=0){return new String("Node "+ nid + " Buffer: "+ size() + "\n");}
			else return new String("Empty buffer.");
		}
		
		public synchronized void pushBuffer(Message m) throws NullPointerException, BufferIsFull{
			if (buffer.size()==Settings.bufferSize) throw new BufferIsFull("This node doesnt have enough space to reiceve messages.");
			if (buffer==null) throw new NullPointerException("Tried to push a MessageBuffer which has null buffer");
			buffer.add(m);
			notifyAll();
		}
		
		public synchronized Message popBuffer() throws NullPointerException, InterruptedException, NodeIsNotActive{
			while(buffer.size()==0){
				idle();
				wait();
				simulating();
			}
			if (nodeShouldStayActive==false) throw new NodeIsNotActive("This node must be killed.");
			sleep(Settings.timeToWait);
			return buffer.remove(0);
		}
		
	}
	
	/** 
	 * Nodeinfo is just a wrapper class to make the storage of the information of a node easier.
	 * 
	 * */
	public class NodeInfo{
		//copied members
		public NodeID nid;
		public Position position;
		//one constructor
		NodeInfo(Node n){
			nid=n.nid;
			position=n.position;
		}
		
		public String toString(){
			return nid+"@"+ position;
		}
	
	}
		
}
