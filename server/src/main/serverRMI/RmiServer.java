package serverRMI;

import java.net.MalformedURLException;
import java.rmi.*;
import java.rmi.registry.LocateRegistry;

import gui.*;


public class RmiServer{
	
	private static final String HOST="localhost";//server address
	
	public static void main(String[] args) throws Exception{
		ServerPanel.setVisibity(true);
		String rmiObjName= "rmi://"+HOST+"/RemoteServer";
		try{
			//System.setSecurityManager(new java.rmi.RMISecurityManager());
			System.setProperty("java.security.policy", "rmi.policy");
			LocateRegistry.createRegistry(1099);
			//initialized remote object
			ConcreteRemoteServer server=new ConcreteRemoteServer();
			//remoteObj registered into rmi registry
			Naming.rebind(rmiObjName, server);
		}
		catch(AccessException e){
			UserInterface.showError("No permission to start the server");
		}
		catch(MalformedURLException e2){
			UserInterface.showError("There was a problem setting up server (malformed url)");
		}
		catch(RemoteException e3){
			UserInterface.showError("There was a problem setting up server\n"+ e3.getMessage());
		}
	}
	
	
}