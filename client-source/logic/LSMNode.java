/**
* LSMNode
* 
* Zironda Andrea -- PCD 2011/2012
* 
* Wireless Sensor Networks - Clone Detection Simulator
* 
* */
package logic;

import messages.*;

import exceptions.*;

import java.util.Random;

import utilities.Log;
/**
 * LSM protocol implementation for a node
 */
public class LSMNode extends Node {

	
	public Node clonedNode(Position p) {
		return new LSMNode(p, nid);
	}


	//LSM Protocol implementation.
	protected void manageMessage(Message m) throws MessageNotSupportedByNode, NullPointerException, NotEnoughEnergy, CloneHasBeenDetected, NodeNotFound{
		Random generator=new Random();
		try{
			// if I receive a locationclaim
			if (m.type()=="LocationClaim"){
				// and I throw correctly
				if (generator.nextDouble()<Settings.claimForwardProbability){
					Log.write("LSM GOOD THROW "+ info().toString(), "logic.LSMNode", "VERBOSE");
					Log.write("Node "+nid+" is forwarding a LC","logic.LSMNode","FINE");
					// I forward g times to a random destination
					for(int i=0;i<Settings.numberOfForwards;i++){
						Position global=Position.random();
						Position local=neighbourForDestination(global);
						ControlMessage cm=new ControlMessage(m.sender(), global, local);
						sendMessage(cm);
					}
				}
				else Log.write("LSM BAD THROW "+ info().toString(), "logic.LSMNode", "VERBOSE");
			}
			// if I receive a control message
			else if(m.type()=="ControlMessage"){
				ControlMessage cm=(ControlMessage)m;
				// and i am the local destination
				if (isLocalDestinationOf(cm)){
					Log.write("Node "+nid+" is forwarding a CM","logic.LSMNode","FINE");
					// i check for clones and forward it
					decodeSignature(m.senderInfo());
					forwardMessage(cm);
				}
				else{
					Log.write("Node "+ info() +" refused a message", "logic.LSMNode", "DEBUG");
				}
			}
			else throw new MessageNotSupportedByNode("This node reiceved a not supported type of message.");
		}
		catch(NotEnoughEnergy e){	
			Log.write("Node "+nid+" finished its energy", "logic.Node", "HIGH");
		}
		catch(NoNeighboursAvailable e){	
			Log.write("Node "+ nid + " received a message without having neighbours", "logic.LSMNode", "BUG");
		}
		catch(NoCloserDestinatary e){	
			
		} 
		catch (BadNeighbour e) {		
			Log.write("Node "+nid+" doesn't have neighbours", "logic.Node", "HIGH");
		} 
		catch (NodeNotFound e) {
			Log.write("Node "+nid+", while forwarding a LocationClaim, didnt find the sender to complete the new ControlMessage", "logic.Node", "BUG");
		} 
	}
	
	public LSMNode(Position p) throws NidOverflow{
		super(p);
	}
	
	private LSMNode(Position p, NodeID n){
		super(p, n);
	}
}
