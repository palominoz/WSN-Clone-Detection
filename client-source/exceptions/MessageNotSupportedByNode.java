/*
* MessageNotSupportedByNode
* 
* Zironda Andrea & Guerra Luca -- PCD 2011/2012
* 
* Wireless Sensor Networks - Clone Detection Simulator
* 
* */
package exceptions;

@SuppressWarnings("serial")
public class MessageNotSupportedByNode extends Exception {
	public MessageNotSupportedByNode(String reason){
		super(reason);
	}
	public String getReason(){
		return this.getReason();
	}
}
