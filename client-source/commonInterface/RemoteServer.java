package commonInterface;



import java.rmi.*;
import stats.*;

//this interface is owned by client and server
public interface RemoteServer extends Remote{
	
	public static final String NEWLINE = "NEW_LINE";
	
	public void push(String simulationStatistics) throws RemoteException; //method invoked by client to push a simulation data 
	
	
}


/*
al momento ho messo un solo metodo,cioè il client finisce una simulazione e invoca pushData
(passando come parametro un SimStat) sull'oggetto remoto,ma è da rivedere
*/

