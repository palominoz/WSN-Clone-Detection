/*
* CloneHasBeenDetected
* 
* Zironda Andrea & Guerra Luca -- PCD 2011/2012
* 
* Wireless Sensor Networks - Clone Detection Simulator
* 
* */
package exceptions;

import logic.Node;
import logic.Position;

@SuppressWarnings("serial")
public class CloneHasBeenDetected extends Exception {
	public CloneHasBeenDetected(Position where, Node detector){
		super("A Cloned node has been found in the network.");
		detectorNode=detector;
		clonePosition=where;
	}
	
	public Node detectorNode=null;
	public Position clonePosition=null;
	
	public String getReason(){
		return this.getReason();
	}
}
