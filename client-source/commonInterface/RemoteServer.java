/*
* RemoteServer
* 
* Zironda Andrea -- PCD 2011/2012
* 
* Wireless Sensor Networks - Clone Detection Simulator
* 
* */

package commonInterface;

/*
 * The rmi interface should allow the client application to push a string representing the 
 * results of a single simulation. There comes a really simple interface with only one method 
 * to achieve the objective.
 */


import java.rmi.*;


public interface RemoteServer extends Remote{
	
	/*
	 * Pushes a string to the server.  
	 */
	public void push(String simulationStatistics) throws RemoteException; 
	
	
}

