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
import logic.Hypervisor;
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
	
	int id;
	static int flag=0;
	
	private Node sender = null;
	
	public Node sender() throws NodeNotFound, NullPointerException{
		if (sender == null) throw new NullPointerException();
		return sender;
	}
	
	public NodeInfo senderInfo() throws NodeNotFound, NullPointerException{
		return sender().info();
	}
	
	
	public Message clone(){
		try {
			Message clone = (Message)super.clone();
			Log.write("CLONED MESSAGE: original address :"+this+" new address"+clone, "messages.Message", "DEBUG");
			return clone;
		} catch (CloneNotSupportedException e) {}
		return null;
	}
	
	abstract public String type();
	
	//This class is intended to be subclassed.
	protected Message(Node s) throws NullPointerException{
		if (s==null) throw new NullPointerException("Tried to initialize a message without a creator node");
		id=flag++;
		sender = s;
		Log.write("A Message has been initialized.","messages.Message","VERBOSE");
	}
	
}
