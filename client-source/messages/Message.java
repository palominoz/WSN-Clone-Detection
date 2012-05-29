/**
* Message
* 
* Zironda Andrea -- PCD 2011/2012
* 
* Wireless Sensor Networks - Clone Detection Simulator
* 
* */
package messages;
import utilities.Log;
import logic.Node;
import logic.Node.NodeInfo;
import exceptions.NodeNotFound;

/**
 *  This class models the messages between nodes. It guarantees a basic messaging service between nodes. 
 *  Supposed to be subclassed to specialize custom behaviours. 
 *  It implements the Cloneable interface because every message is splitted to all of the neighbours each time it jumps 
 *  from a node to another.
 *
 */
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
