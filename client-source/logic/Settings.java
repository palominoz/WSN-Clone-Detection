/*
* NodeSettings
* 
* Zironda Andrea & Guerra Luca -- PCD 2011/2012
* 
* Wireless Sensor Networks - Clone Detection Simulator
* 
* */

package logic;

import java.rmi.Naming;

import utilities.Log;

import enums.SupportedProtocol;


/*
 * This singleton manages overall settings for the nodes.Used to make code more legible in the class Node.
 * 
 * */
public class Settings {
	
	private static boolean serverIsValid(){
		try{
			Naming.lookup("rmi://"+server+"/RemoteSimData");
		}
		catch (Exception e){
			return false;
		}
		return true;
	}
	public static String server = "localhost";
	
	public static SupportedProtocol protocol=SupportedProtocol.NONE;
	
	public static int numberOfNodes=0;
	
	public static int numberOfSimulations=0;
	//public static int currentSimulation=0;
	/*prefer easy access for quick implementation other than information hiding for this class purposes */
	//probability of a reiceved location claim to be forwarded
	public static double claimForwardProbability=0;
	
	//transmission range of the nodes
	public static double transmissionRange=0;
	
	//default energy a node starts with.
	public static double defaultMaxEnergy=0;
	
	//static values representing the loss of energy from actions of the node.
	public static double transmitConsumption=0;
	public static double receiveConsumption=0;
	public static double signatureConsumption=0;
	
	//number of numberOfForwards simulating LSM protocol.
	public static int numberOfForwards=0;
	
	//buffer size, useful for real world simulations, expressed in number of messages.
	public static int bufferSize=300;
	
	//action delay from change of status, to make sure all the other nodes are switched on(msec).
	//public static int status_change_delay;
	
	
	public static String status(){
		String status = new String("<html>");
		if (server.equals("") || server == null){
			status += "RMI server is missing<br />";
		}
		if (serverIsValid() == false){
			status += "RMI server is not valid<br />";
		}
		
		if (areReady() == false){
			status += "Some settings are missing/not valid<br />";
		}
		
		if (status.equals("<html>")){
			status = "<html><font color='green'>Ready</font></html>";
		}
		
		status += "</html>";
		
		
		return status;
	}
	
	public static boolean areReady(){
		return
			numberOfNodes			>0 &&
			numberOfSimulations		>0 &&
			claimForwardProbability >0 &&
			transmissionRange 		>0 &&
			defaultMaxEnergy 		>0 &&
			transmitConsumption 	>=0 &&
			receiveConsumption		>=0 &&
			signatureConsumption	>=0 &&
			numberOfForwards		>=0 &&
			bufferSize				>0 &&
			serverIsValid();
		
	}	
	
}
