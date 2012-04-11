/*
* NodeFactory
* 
* Zironda Andrea & Guerra Luca -- PCD 2011/2012
* 
* Wireless Sensor Networks - Clone Detection Simulator
* 
* */
package logic;
import enums.*;
import logic.LSMNode;
import logic.REDNode;
import exceptions.*;

/*
 * 
 * Factory class to build different nodes switching proto enum.This class improves the code of other classes.
 * It would be a good idea to hide constructors in Node hierarcy.
 * 
 * */

public class NodeFactory {
	
/***CLASS MEMBERS***/
	//static public function to build nodes dinamically switching the protocol needed.
	static public Node buildNode(SupportedProtocol pr, Position p) throws NidOverflow{
		if(pr==SupportedProtocol.LSM){
			return new LSMNode(p);
		}
		else if(pr==SupportedProtocol.RED){
			return new REDNode(p);
		}
		else return null;
	}
	
	
}
