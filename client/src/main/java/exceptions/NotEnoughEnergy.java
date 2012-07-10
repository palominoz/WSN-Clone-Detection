/*
* NotEnoughEnergy
* 
* Zironda Andrea & Guerra Luca -- PCD 2011/2012
* 
* Wireless Sensor Networks - Clone Detection Simulator
* 
* */
package exceptions;

@SuppressWarnings("serial")
public class NotEnoughEnergy extends Exception {
	public NotEnoughEnergy(){
		super("There was not enough energy on the node to perform actions.");
	}
	public String getReason(){
		return this.getReason();
	}
}
