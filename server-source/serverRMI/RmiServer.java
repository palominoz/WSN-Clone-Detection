package serverRMI;

import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import gui.*;


public class RmiServer{
	
	private static final String HOST="localhost";//server adress
	
	public static void main(String[] args) throws Exception{
		ServerPanel.setVisibity(true);
		String rmiObjName= "rmi://"+HOST+"/RemoteSimData";//adress RemoteObj
		try{
			//System.setSecurityManager(new java.rmi.RMISecurityManager());
			System.setProperty("java.security.policy", "rmi.policy");
			Registry registry = LocateRegistry.createRegistry(1099);
			//initialized remote object
			SimDataImpl remoteObj=new SimDataImpl();
			//remoteObj registered into rmi registry
			Naming.rebind(rmiObjName,remoteObj);
		}
		
		catch(RemoteException e){e.printStackTrace();}
		catch(Exception e){e.printStackTrace();}
	}
	
	
}