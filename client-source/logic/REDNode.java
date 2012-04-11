/*
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
/*
 * Implementation of a sensor which works using the RED protocol.
 * 
 * 
 * */

public class REDNode extends Node {
	//random provided by hypervisor
	public static int randomNumber;
	
	
	
	public Node clonedNode(Position p) {
		return new REDNode(p, nid);
	}

	public void manageMessage(Message m) throws MessageNotSupportedByNode, NullPointerException, NotEnoughEnergy, CloneHasBeenDetected, NodeNotFound{
		Random generator=new Random();
		try{
			if (m.type()=="LocationClaim"){
				if (generator.nextDouble()<Settings.claimForwardProbability){
					Log.write("LSM GOOD THROW "+ info().toString(), "logic.LSMNode", "VERBOSE");
					Log.write("Node "+nid+" is forwarding a LC","logic.LSMNode","FINE");
					for(int i=0;i<Settings.numberOfForwards;i++){
						//RED PROTO HASHED POSITION
						
						Position global=Position.hashed(m.senderInfo().nid, randomNumber, i);
						Position local=neighbourForDestination(global);
						ControlMessage cm=new ControlMessage(m.sender(), global, local);
						cm.updatePath(this);
						sendMessage(cm);
					}
				}
				else Log.write("RED BAD THROW "+ info().toString(), "logic.LSMNode", "VERBOSE");
			}
			else if(m.type()=="ControlMessage"){
				ControlMessage cm=(ControlMessage)m;
				if (isLocalDestinationOf(cm)){
					Log.write("Node "+nid+" is forwarding a CM","logic.LSMNode","FINE");
					forwardMessage(cm);
				}
				else{
					Log.write("Node "+ info() +" refused a message", "logic.LSMNode", "DEBUG");
				}
			}
			else throw new MessageNotSupportedByNode("This node reiceved a not supported type of message.");
		}
		catch(NoNeighboursAvailable e){	
			Log.write("Node "+ nid + " received a message without having neighbours", "logic.LSMNode", "BUG");
		}
		catch(NoCloserDestinatary e){
			decodeSignature(m.senderInfo());
		} 
		catch (BadNeighbour e) {		
			Log.write("Node "+nid+" doesn't have neighbours", "logic.Node", "HIGH");
		} 
		catch (NodeNotFound e) {
			Log.write("Node "+nid+", while forwarding a LocationClaim, didnt find the sender to complete the new ControlMessage", "logic.Node", "BUG");
		} 
		catch (NodeIsTooFar e) {
			Log.write("Node "+nid+" detected a bug, is forwarding a message from a too far node from himself.", "logic.Node", "BUG");
		} catch (ErrorCalulatingAnHashedPosition e) {
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
