package enums;

public enum SupportedProtocol {
	LSM,RED, NONE;
	
	SupportedProtocol(){}
	
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
