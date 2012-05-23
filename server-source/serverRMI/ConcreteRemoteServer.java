package serverRMI;



import java.rmi.*;
import java.rmi.server.*;
import stats.*;
import commonInterface.*;
import commonInterface.RemoteServer;

import java.util.*;
import gui.*;


/*This class is defined on the server side;
 UnicastRemoteObiect:support point-to-point connections (TCP)
 */
public class ConcreteRemoteServer extends UnicastRemoteObject implements commonInterface.RemoteServer{
	
	public ConcreteRemoteServer() throws RemoteException {} //al momento l'ho laciato vuoto,da rivedere
	
	
	
	public void push(String data) throws RemoteException{
		if (data.equals(RemoteServer.NEWLINE)){
			data = "\n";
		}
		ServerPanel.pushStats(data);
		OutputFile.write(data);
	}
	

	
}

