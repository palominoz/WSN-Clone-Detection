/*
* LocationClaim
* 
* Zironda Andrea & Guerra Luca -- PCD 2011/2012
* 
* Wireless Sensor Networks - Clone Detection Simulator
* 
* */
package messages;
import exceptions.NodeNotFound;
import messages.Message;
import logic.Node;
import logic.Node.NodeInfo;


public class LocationClaim extends Message implements Cloneable {

	@Override
	public String type() {
		return "LocationClaim";
	}
	
	public LocationClaim clone(){
		LocationClaim clone=(LocationClaim)super.clone();
		return clone;
	}
	
	public NodeInfo signature() throws NullPointerException, NodeNotFound{
		return sender().info();
	}
	
	public LocationClaim(Node creator) throws NullPointerException{
		super(creator);
	}

}
