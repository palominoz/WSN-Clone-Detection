/**
* SupportedProtocol
* 
* Zironda Andrea -- PCD 2011/2012
* 
* Wireless Sensor Networks - Clone Detection Simulator
* 
* */
package enums;

/**
 * An instance of this enum identifies one supported protocol
 * 
 * */

public enum SupportedProtocol {
	// currently supported protocols
	LSM,RED, NONE;
	
	SupportedProtocol(){}
	
	// convenience method
	public static SupportedProtocol protocolWithString(String string){
		if ("RED".equals(string)){
			return RED;
		}
		else if ("LSM".equals(string)){
			return LSM;
		}
		else return NONE;
	}
}
