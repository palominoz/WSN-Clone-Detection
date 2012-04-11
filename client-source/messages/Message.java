/*
* Message
* 
* Zironda Andrea & Guerra Luca -- PCD 2011/2012
* 
* Wireless Sensor Networks - Clone Detection Simulator
* 
* */
package messages;
import utilities.Log;
import logic.Node;
import logic.Node.NodeInfo;
import logic.Settings;
import logic.Position;

import java.util.Iterator;
import java.util.Vector;
/*
 * 
 * Message is the base class of messages and used to guarantee basic messaging services between nodes.
 * 
 * 
 * */

import exceptions.MessageHasNotBeenSent;
import exceptions.NodeIsTooFar;
import exceptions.NodeNotFound;

abstract public class Message implements Cloneable{
	
	public static class MessageJump{
		public Node origin;
		public Node destination;
		
		MessageJump(Node o, Node d) throws NullPointerException{
			if (o==null || d==null) throw new NullPointerException("Tried to initialize a MessageJump with null parameters");
			origin=o;
			destination=d;
		}
	}
	
	//the sender of the message
	Vector<Node> path=new Vector<Node>();
	
	int id;
	static int flag=0;
	
	public void updatePath(Node n) throws NodeIsTooFar, NullPointerException{
		if (path==null) throw new NullPointerException("Tried to update path in a message where is null.");
		synchronized (path){
			if (path.size()>0) {
				Log.write("JUMP from "+ path.lastElement().info() +" to "+n.info(), new Integer(id).toString(), "PATHDEBUG");
				
				if (Position.distance(path.lastElement().position(), n.position())>Settings.transmissionRange){
					Log.write("Message detected wrong jump", "messages.Message", "BUG");
					throw new NodeIsTooFar("Updating path of a message with a node too far away to reach.");
				}
			}
			path.add(n);
		}
	}
	
	public Node sender() throws NodeNotFound, NullPointerException{
		synchronized (path){
			if (path.size()==0) throw new NodeNotFound("Asked for sender of a message wich has not has been set");
			return path.firstElement();
		}
	}
	
	public NodeInfo senderInfo() throws NodeNotFound, NullPointerException{
		return sender().info();
	}
	
	
	public Message clone() throws NullPointerException{
		if (path==null) throw new NullPointerException("Tried to clone a message from another without path vector");
		try {
			Message clone = (Message)super.clone();
			clone.path=(Vector<Node>)path.clone();
			Log.write("CLONED MESSAGE: original address :"+this+" new address"+clone, "messages.Message", "DEBUG");
			return clone;
		} catch (CloneNotSupportedException e) {}
		return null;
	}
	
	abstract public String type();
	
	//This class is intended to be subclassed.
	protected Message(Node sender) throws NullPointerException{
		if (sender==null) throw new NullPointerException("Tried to initialize a message without a creator node");
		try{
			id=flag++;
			updatePath(sender);
			Log.write("A Message has been initialized.","messages.Message","VERBOSE");
		}
		catch(NodeIsTooFar e){
			Log.write("Message creation added the creator and it was too far by itself", "messages.Message", "BUG");
		}
	}
	
	
	public Node lastSender() throws MessageHasNotBeenSent{
		return lastJump().origin;
	}
	
	public MessageJump lastJump() throws MessageHasNotBeenSent{
		if (pathLength()>0) return new MessageJump(path.elementAt(path.size()-1), path.lastElement());
		else throw new MessageHasNotBeenSent("Asked for last jump of a message which hasnt been sent");
	}
	
	public Vector<MessageJump> jumps() throws MessageHasNotBeenSent, NodeIsTooFar{
		Vector<MessageJump> result=new Vector<MessageJump>();
		if (pathLength()>0){
			synchronized(path){
				Iterator<Node> i=path.iterator();
				Node first=null;
				while (i.hasNext()){
					Node current=i.next();
					if (first==null) first=current;
					else{
						if (Position.distance(first.position(), current.position())>Settings.transmissionRange){
							Log.write("Found an error with a non legal jump (distance) sender: "+ first.info() +" receiver: "+ current.info() , "messages.Message", "BUG");
							throw new NodeIsTooFar("Jumps calculation detected a non legal jump (node too far)");
						}
						result.add(new MessageJump(first, current));
						first=current;
					}
				}
			}
		}
		else throw new MessageHasNotBeenSent("Asked for jumps of a message which has not been sent");
		return result;
	}
	
	public int pathLength() throws NullPointerException{
		if (path==null) throw new NullPointerException("Path vector on this message was not initialized");
		return path.size()-1;
	}
	
}
