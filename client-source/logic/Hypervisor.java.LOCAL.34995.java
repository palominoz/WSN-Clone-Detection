/*
* Hypervisor
* 
* Zironda Andrea & Guerra Luca -- PCD 2011/2012
* 
* Wireless Sensor Networks - Clone Detection Simulator
* 
* */
package logic;

import exceptions.*;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.Random;
import java.util.Vector;

import stats.SimStat;
import utilities.Log;
import utilities.Monitor;
import utilities.StopWatch;
import enums.*;
import java.rmi.*;
import commonInterface.*;
import gui.AmbientPanel;
import gui.ControlPanel;
import gui.ResultsPanel;
import gui.UserInterface;
import parser.*;

/*
 * This class is responsible for the managing of the simulation.It creates and starts the nodes as it is required in the
 * configuration file.
 * In order to work it other modules of the software must set up static variables of this class calling the setup method.
 * This class approaches the problem with a singleton pattern and its methods are publicly available.
 * 
 * */

public class Hypervisor extends Thread{
/***CLASS MEMBERS***/
		//current simulation running
		private static int currentSimulation;
		//check wether the simulation is finished
		static int idleNodes=0;
		//time to wait with idle status of all node to make sure the simulation is finished.
		static int simulationTerminationDelay=500;
		
		public static boolean stopsAfterCompletingEverySimulation=false;
		public static boolean stopsAfterDetectingClone=false;

/***INSTANCE MEMBERS***/
		private long waitForDeadNodeTime=500;
		private Monitor monitor = new Monitor(false);
	
/***CONSTRUCTORS***/
	Hypervisor(){}
/***CLASS METHODS***/
	// singleton implementation
	static Hypervisor singleton=null;
	
	public static Hypervisor hypervisor(){
		if (singleton==null) singleton=new Hypervisor();
		return singleton;
	}
/***INSTANCE METHODS***/
	public static void setup() throws TooManyNodes, SettingsAreNotReady{
		Log.write("Hypervisor setup started", "logic.Hypervisor", "FINE");
		
		currentSimulation=0;
		//
	
		Log.write("Additional protocol setup..", "logic.Hypervisor", "FINE");
		//eventual additional protocol settings
		switch (Settings.protocol){
		case RED:
			Random generator=new Random();
			REDNode.randomNumber=generator.nextInt(10000);
			break;
		case LSM:
			break;
		case NONE:
			throw new SettingsAreNotReady("Hypervisor doesn't know which protocol to simulate");
		}
	}
	
	private static void createNodes() throws TooManyNodes{
		//some errors may occour in creating random nodes.if that is the case the operation is repeated.
		Log.write("Creating nodes..", "logic.Hypervisor", "FINE");
		for (int i=0;i<Settings.numberOfNodes;i++){
			creation:
			try {
				 Ambient.addNode(NodeFactory.buildNode(Settings.protocol, Position.random()));
			} catch (PositionNotAvailable e) {
				break creation;
			} catch (NidOverflow e) {
				throw new TooManyNodes("Hypervisor cannot manage that number of nodes.");
			}
		}
		
		/*DEBUG*/
		try{
			Ambient.addCloned();
		}
		catch (NoNodesAvailable e){}
	}
	
	
	
	
	public static void runSimulation(){
		hypervisor().start();
	}
	public static void notifyIdle(Node idleNode){
		idleNode.isIdle=true;
		synchronized(simulationChecker){
			UserInterface.setIdleNode(idleNode);
				idleNodes++;
				simulationChecker.notify();
			Log.write(idleNodes+ " Nodes out of "+Settings.numberOfNodes+ " are currently idle", "logic.Hypervisor", "SIMSTATUS");
		}
	}
	
	public static void notifySimulating(Node simulatingNode){
		simulatingNode.isIdle=false;
		synchronized(simulationChecker){
			simulationChecker.activity();
			UserInterface.setIdleNode(simulatingNode);
			idleNodes--;
			simulationChecker.notify();
			Node n=(Node)Thread.currentThread();
			Log.write("Node "+n.nid+" is idle", "logic.Hypervisor", "VERBOSE");
			Log.write(idleNodes+ " Nodes out of "+Settings.numberOfNodes+ " are currently idle", "logic.Hypervisor", "SIMSTATUS");
		}
	}
	
	private static EndOfSimulationChecker simulationChecker=new EndOfSimulationChecker();
	
	private static class EndOfSimulationChecker extends Thread{
		
		{
			setDaemon(true);
		}
		
		private void stopHypervisor(){
			synchronized(hypervisor()){
				hypervisor().notify();
			}
		}
		
		public boolean activity=false;
		
		public void activity(){activity=true;}
		
		public void run(){
			boolean hypervisorHasNotBeenStopped=true;
			while (hypervisorHasNotBeenStopped){
				synchronized(this){
					try{
						if (idleNodes >= Settings.numberOfNodes +1) {
							Log.write("1ST");
							activity=false;
							wait(simulationTerminationDelay);
							if (idleNodes >= Settings.numberOfNodes +1 && activity==false){
								Log.write("OK");
								stopHypervisor();
								hypervisorHasNotBeenStopped=false;
							}
						}
						else{
							activity=true;
							wait(2000);
						}
					} catch (InterruptedException e){
						Log.write("Hypervisor checker has been interrupted", "logic.Hypervisor", "HIGH");
					}
				}
			}
		}
	}; 
	
	
	
	public static void notifyClone(Position p, Node detector){
		UserInterface.setClonedNode(p);
		UserInterface.setDetectorNode(detector);
		cloneWasFound=true;
		if (stopsAfterDetectingClone){
			Ambient.pause();
			ControlPanel.controlPanel().pauseButton.setActionCommand("unpause");
			ControlPanel.controlPanel().pauseButton.setText("Unpause");
		}
	}
	
	private static boolean cloneWasFound=false;
	
	/*COMPLETARE.*/
	public void run(){
		/*debug*/
		try {
			//setup(SupportedProtocol.LSM, 1, 100);
			setup();
		} catch (TooManyNodes e) {
			
		} catch (SettingsAreNotReady e) {
			UserInterface.showError(null, e.getMessage());
		}
		/*debug*/
		for (currentSimulation=0;currentSimulation<Settings.numberOfSimulations;currentSimulation++){
			synchronized(this){
				try{
					pauseFlag();
					Log.write("Simulation "+currentSimulation+" is starting.", "logic.Hypervisor", "FLOW");
					UserInterface.notifyStartOfSimulation(currentSimulation);
					resetForNewSimulation();
					StopWatch.start();
					Ambient.start();
					this.wait();
					double time = StopWatch.stop();
					DecimalFormat format = new DecimalFormat("#.#");
					Log.write("Simulation "+currentSimulation+" has finished in " + format.format(time / 1000) + " seconds", "logic.Hypervisor", "FLOW");
					pauseFlag();
					if (cloneWasFound) Log.write("A clone was found", "logic.Hypervisor", "CLONE");
					Ambient.kill();
					sleep(waitForDeadNodeTime);
					UserInterface.notifyEndOfSimulation(currentSimulation);
					collectAndDeliver(currentSimulation);
					if (stopsAfterCompletingEverySimulation) {
						pause();
						ControlPanel.controlPanel().pauseButton.setActionCommand("unpause");
						ControlPanel.controlPanel().pauseButton.setText("Unpause");
						ControlPanel.notifyPause();
					}
				}
				catch (InterruptedException e){
					Log.write("Hypervisor was interrupted", "logic.Hypervisor", "CRITICAL");
				}
				catch (NoNodesAvailable e){
					Log.write("There was an error in collecting the results", "logic.Hypervisor", "CRITICAL");
				}
				
			}
		}
		UserInterface.showMessage("Simulations have finished.");
	}

	private static void pauseFlag(){
		if (hypervisor().monitor.paused) {
			synchronized(hypervisor().monitor){
				try {
					hypervisor().monitor.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private void sendSettings(RemoteServer server){
		Vector<String> data = Settings.orderedSettings();
		Iterator<String> it = data.iterator();
		while(it.hasNext()){
			try {
				server.push(it.next()+" ");
			} catch (RemoteException e) {
				UserInterface.showError("There was a network problem");
			}
		}
	}
	
	private void sendStatistics(SimStat stats, RemoteServer server) throws RemoteException{
		//Min, max, average, e standard deviation
		/*numero di messaggi spediti, 
		 * numero di messaggi ricevuti, 
		 * numero di verifche di frme crittografche, 
		 * totale dell'energia consumata,
		 *  numero di messaggi memorizzati nella memoria del nodo. */
		try{
			DecimalFormat format = new DecimalFormat("#.###");
			server.push(format.format(stats.minimum(SimStat.ValueType.SENT))+" ");
			server.push(format.format(stats.maximum(SimStat.ValueType.SENT))+" ");
			server.push(format.format(stats.average(SimStat.ValueType.SENT))+" ");
			server.push(format.format(stats.standardDeviation(SimStat.ValueType.SENT))+" ");
			
			server.push(format.format(stats.minimum(SimStat.ValueType.RECEIVED))+" ");
			server.push(format.format(stats.maximum(SimStat.ValueType.RECEIVED))+" ");
			server.push(format.format(stats.average(SimStat.ValueType.RECEIVED))+" ");
			server.push(format.format(stats.standardDeviation(SimStat.ValueType.RECEIVED))+" ");
			
			server.push(format.format(stats.minimum(SimStat.ValueType.SIGNATURES))+" ");
			server.push(format.format(stats.maximum(SimStat.ValueType.SIGNATURES))+" ");
			server.push(format.format(stats.average(SimStat.ValueType.SIGNATURES))+" ");
			server.push(format.format(stats.standardDeviation(SimStat.ValueType.SIGNATURES))+" ");
			
			server.push(format.format(stats.minimum(SimStat.ValueType.ENERGY))+" ");
			server.push(format.format(stats.maximum(SimStat.ValueType.ENERGY))+" ");
			server.push(format.format(stats.average(SimStat.ValueType.ENERGY))+" ");
			server.push(format.format(stats.standardDeviation(SimStat.ValueType.ENERGY))+" ");
			
			server.push(format.format(stats.minimum(SimStat.ValueType.STORED))+" ");
			server.push(format.format(stats.maximum(SimStat.ValueType.STORED))+" ");
			server.push(format.format(stats.average(SimStat.ValueType.STORED))+" ");
			server.push(format.format(stats.standardDeviation(SimStat.ValueType.STORED))+" ");
			
			server.push(new Boolean(stats.cloneWasFound()).toString());
			
			server.push("\n");
			
		} catch (BadValue e) {
			UserInterface.showError("There was a problem with the system.");
		}
		finally{
			
		}
		
		
		
		
	}
	
	private void collectAndDeliver(int currentSimulation) throws NoNodesAvailable{
		SimStat simulationStatistics=new SimStat(Ambient.getStats());
		Log.write(simulationStatistics.toString());
		cloneWasFound=false;
		try{
		RemoteServer server=(RemoteServer) Naming.lookup("rmi://"+Settings.server+"/RemoteServer");
		//ref.pushData(simulationStatistics);
		Log.write("Delivering statistics for the last simulation", "logic.Hypervisor", "FINE");
		
		sendSettings(server);
		sendStatistics(simulationStatistics, server);
		server.push("\n");
		
		UserInterface.notifySentSimulation(currentSimulation);
		}
		catch(Exception e){
			Log.write("Error while connecting to RMI server..\n","logic.Hypervisor","CRITICAL");
			e.printStackTrace();
		}
		
		ResultsPanel.showStatistics(simulationStatistics);
	}
	
	private void resetForNewSimulation(){
		try{
			System.gc();
			cloneWasFound=false;
			idleNodes=0;
			Ambient.clear();
			Log.write("Preparing for a new simulation.." ,"logic.Hypervisor","FLOW");
			simulationChecker=new EndOfSimulationChecker();
			simulationChecker.start();
			createNodes();
		}
		catch (TooManyNodes e){}
		
	}
	public static void pause() {
		synchronized(hypervisor().monitor){
			hypervisor().monitor.paused = true;
		}
	}
	
	public static void unpause(){
		synchronized(hypervisor().monitor){
			hypervisor().monitor.paused = false;
			hypervisor().monitor.notify();
		}
	}
}
