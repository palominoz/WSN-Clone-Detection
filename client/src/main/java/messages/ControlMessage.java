/**
* ControlMessage
* 
* Zironda Andrea -- PCD 2011/2012
* 
* Wireless Sensor Networks - Clone Detection Simulator
* 
* */
package messages;

import exceptions.NodeNotFound;
import logic.Position;
import logic.Node.NodeInfo;
import logic.Node;
/**
 *  ControlMessage is the message instantiated when a LocationClaim throws a successfull forward. It needs some
 *  specialization as it has a global and local destination. Global destination never changes, local destination
 *  is used to know which node is the correct receiver on each jump.
 *
 */

public class ControlMessage extends Message implements Cloneable{
	
	//control messages got a signed nid of the sender and a destination.
	public Position global_destination=null;
	
	public Position local_destination=null;
	
	//signature
	public NodeInfo senderInfo() throws NullPointerException, NodeNotFound{
		return sender().info();
	}
	
	// could have used reflection. too late atm.
	public String type(){
		return "ControlMessage";
	}
	
	public ControlMessage clone() throws NullPointerException{
		ControlMessage clone=(ControlMessage)super.clone();
		if (global_destination==null) throw new NullPointerException("Tried to clone a ControlMessage which has null global destination");
		if (local_destination==null) throw new NullPointerException("Tried to clone a ControlMessage which has null local destination");
		clone.global_destination=global_destination.clone();
		clone.local_destination=local_destination.clone();
		return clone;
	}
	
	
	
	//constructor
	public ControlMessage(Node sender, Position global, Position local) throws NullPointerException{
		super(sender);
		if (global==null) throw new NullPointerException("Tried to initialize a ControlMessage which has null global destination parameter");
		if (local==null) throw new NullPointerException("Tried to initialize a ControlMessage which has null local destination parameter");
		global_destination=global;
		local_destination=local;
	}
	
	
}
