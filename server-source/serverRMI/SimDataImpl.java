package serverRMI;



import java.rmi.*;
import java.rmi.server.*;
import stats.*;
import commonInterface.*;

import java.util.*;
import gui.*;


/*This class is defined on the server side;
 UnicastRemoteObiect:support point-to-point connections (TCP)
 */
public class SimDataImpl extends UnicastRemoteObject implements RemoteSimData{
	
	public SimDataImpl() throws RemoteException {} //al momento l'ho laciato vuoto,da rivedere
	
	
	
	public void pushData(String simulationStatistics) throws RemoteException{
		
	}
	

	
}

