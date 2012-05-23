package commonInterface;

/*
 * The rmi interface should allow the client application to push a string representing the 
 * results of a single simulation. There comes a really simple interface with only one method 
 * to achieve the objective.
 */


import java.rmi.*;


public interface RemoteServer extends Remote{
	
	/*
	 * This methods will serialize and unmarshall automatically the string of the parameter.  
	 */
	public void push(String simulationStatistics) throws RemoteException; 
	
	
}

