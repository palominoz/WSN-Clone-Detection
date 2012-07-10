/**
* REDNode
* 
* Zironda Andrea & Guerra Luca -- PCD 2011/2012
* 
* Wireless Sensor Networks - Clone Detection Simulator
* 
* */
package logic;

import java.util.Random;

import utilities.Log;

import messages.ControlMessage;
import messages.Message;
import exceptions.*;

/**
 * Implementation of a sensor which works using the RED protocol.
 * */

public class REDNode extends Node {
	//random provided by hypervisor
	public static int randomNumber;
	
	
	// returns a clone
	public Node clonedNode(Position p) {
		return new REDNode(p, nid);
	}
	
	// RED protocol implementation
	public void manageMessage(Message m) throws MessageNotSupportedByNode, NullPointerException, NotEnoughEnergy, CloneHasBeenDetected, NodeNotFound{
		Random generator=new Random();
		try{
			//if I receive a location claim
			if (m.type()=="LocationClaim"){
				// and I roll correctly
				if (generator.nextDouble()<Settings.claimForwardProbability){
					Log.write("Node "+nid+" is forwarding a LC","logic.REDNode","FINE");
					// I forward the location claim g times
					for(int i=0;i<Settings.numberOfForwards;i++){
						// to one destination calculated as from documentation
						Position global=Position.hashed(m.senderInfo().nid, randomNumber, i);
						Position local=neighbourForDestination(global);
						ControlMessage cm=new ControlMessage(m.sender(), global, local);
						sendMessage(cm);
					}
				}
				//otherwise i dont do anything
				else Log.write("RED BAD THROW "+ info().toString(), "logic.REDNode", "VERBOSE");
			}
			// If i receive a ControlMessage
			else if(m.type()=="ControlMessage"){
				ControlMessage cm=(ControlMessage)m;
				// and i am the local destination of the message
				if (isLocalDestinationOf(cm)){
					Log.write("Node "+nid+" is forwarding a CM","logic.REDNode","FINE");
					// I forward it ( and detect clone if i am the last receiver)
					forwardMessage(cm);
				}
				else{
					Log.write("Node "+ info() +" refused a message", "logic.REDNode", "DEBUG");
				}
			}
			else throw new MessageNotSupportedByNode("This node reiceved a not supported type of message.");
		}
		catch(NoNeighboursAvailable e){	
			// bug tracker
			Log.write("Node "+ nid + " received a message without having neighbours", "logic.LSMNode", "BUG");
		}
		catch(NoCloserDestinatary e){
			// thrown when I am the last receiver.
			decodeSignature(m.senderInfo());
		} 
		catch (BadNeighbour e) {
			// forever alone
			Log.write("Node "+nid+" doesn't have neighbours", "logic.Node", "VERBOSE");
		} 
		catch (NodeNotFound e) {
			// 
			Log.write("Node "+nid+", while forwarding a LocationClaim, didnt find the sender to complete the new ControlMessage", "logic.Node", "BUG");
		} 
		catch (ErrorCalulatingAnHashedPosition e) {
			// bug tracker
			Log.write("Node "+ nid +" "+ e.getMessage(), "logic.REDNode", "BUG");
		}
	}
	
	public REDNode(Position p) throws NidOverflow{
		super(p);
	}
	
	protected REDNode(Position p, NodeID id){
		super(p, id);
	}

}
