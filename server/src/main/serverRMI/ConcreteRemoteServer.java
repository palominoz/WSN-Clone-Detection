package serverRMI;



import java.rmi.*;
import java.rmi.server.*;
import commonInterface.RemoteServer;

import gui.*;


/*This class is defined on the server side;
 UnicastRemoteObiect:support point-to-point connections (TCP)
 */
@SuppressWarnings("serial")
public class ConcreteRemoteServer extends UnicastRemoteObject implements commonInterface.RemoteServer{
	
	public ConcreteRemoteServer() throws RemoteException {} 
	
	
	
	public void push(String data) throws RemoteException{
		if (data.equals(RemoteServer.NEWLINE)){
			data = "\n";
		}
		ServerPanel.pushStats(data);
		OutputFile.write(data);
	}
	

	
}

