/**
* Hypervisor
* 
* Zironda Andrea -- PCD 2011/2012
* 
* Wireless Sensor Networks - Clone Detection Simulator
* 
* */
package logic;

import exceptions.*;

import java.rmi.RemoteException;
import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.Random;
import java.util.Vector;

import stats.SimStat;
import utilities.Log;
import utilities.Monitor;
import utilities.StopWatch;

import commonInterface.*;
import gui.ControlPanel;
import gui.UserInterface;

/**
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
		
		static int clones = 0;
		
		public static boolean stopsAfterCompletingEverySimulation=false;
		public static boolean stopsAfterDetectingClone=false;

/***INSTANCE MEMBERS***/
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
	
	
	
	
	public static void startSimulations(){
		synchronized(hypervisor().monitor){
			hypervisor().startSignal = true;
			hypervisor().monitor.notifyAll();
		}
	}
	public static void wake(){
		hypervisor().start();
	}
	
	public static void notifyIdle(Node idleNode){
		synchronized(hypervisor()){
			idleNodes++;
			UserInterface.setIdleNode(idleNode);
			//Log.write("I :idle: " + idleNodes , "logic.Hypervisor", "SIMSTATUS");
		}
	}
	
	
	public static void notifySimulating(Node simulatingNode){
		synchronized(hypervisor()){
			idleNodes--;
			//Log.write("S: idle: " + idleNodes , "logic.Hypervisor", "SIMSTATUS");
			UserInterface.setIdleNode(simulatingNode);
		}
	}
	
	private static boolean loggedClone = false;
	
	private static boolean stopFlag = false;
	
	public static void notifyClone(Position p, Node detector){
		if (loggedClone == false) {
			Log.write("A clone was found", "logic.Hypervisor", "CLONE");
			loggedClone = true;
		}
		UserInterface.setClonedNode(p);
		UserInterface.setDetectorNode(detector);
		clones++;
		if (stopsAfterDetectingClone) {
			stopFlag = true;
		}
	}
	
	private boolean startSignal = false;
	
	private void prepareSimulation(){
		Log.write("Simulation "+currentSimulation+" is starting.", "logic.Hypervisor", "FLOW");
		UserInterface.notifyStartOfSimulation(currentSimulation);
		resetForNewSimulation();
		
		Ambient.start();
	}
	
	private void terminateSimulation() throws InterruptedException{
		
		boolean simulating = true;
		boolean notEmptyBuffers = true;
		
		while(simulating && notEmptyBuffers){
			synchronized(monitor){
				if (monitor.paused) monitor.wait();
			}
			simulating = true;
			notEmptyBuffers = true;
			sleep(100);
			Log.write("HH: idle: " + idleNodes , "logic.Hypervisor", "SIMSTATUS");
			if (idleNodes == Settings.numberOfNodes +1){
				
				Iterator<Node> it = Ambient.ambient().nodes.iterator();
				while(it.hasNext()){
					Node current = it.next();
					if (current.getState() != Thread.State.WAITING && current.getState() != Thread.State.TERMINATED) {
						break;
					}
					
					if (it.hasNext() == false){
						simulating = false;
					}
				}
				it = Ambient.ambient().nodes.iterator();
				Node first = it.next();
				if (simulating == false && first.buffer.recursiveLock(it)){
					notEmptyBuffers = false;
				}
			}
		}
	}
	
	
	private void ensureNodesAreKilled() throws InterruptedException{
		Ambient.kill();
		Iterator<Node> it = Ambient.ambient().nodes.iterator();
		while(it.hasNext()){
			it.next().join();
		}
	}
	
	private void runSimulations(){
		try {
			
			setup();
			
			for (currentSimulation=0;currentSimulation<Settings.numberOfSimulations;currentSimulation++){
				
				if (stopFlag){
					Log.write("WWWWWW", "ads", "FLOW");
					synchronized(monitor){
						monitor.wait();
					}
					if (stopsAfterCompletingEverySimulation == false) stopFlag = false;
				}
				
				prepareSimulation();
				
				StopWatch.start();
				
				terminateSimulation();
				
				
				double time = StopWatch.stop();
				
				
				DecimalFormat format = new DecimalFormat("#.#");
				Log.write("Simulation "+currentSimulation+" has finished in " + format.format(time / 1000) + " seconds", "logic.Hypervisor", "FLOW");
				
				
				ensureNodesAreKilled();
				
			
				UserInterface.notifyEndOfSimulation(currentSimulation);
				
				collectAndDeliver(currentSimulation);
				
				if (stopsAfterCompletingEverySimulation) {
					pause();
					synchronized(monitor) {monitor.wait();}
					ControlPanel.controlPanel().pauseButton.setActionCommand("unpause");
					ControlPanel.controlPanel().pauseButton.setText("Unpause");
					ControlPanel.notifyPause();
				}
				
				
				
			}
			
		} catch (TooManyNodes e) {
			
		} catch (SettingsAreNotReady e) {
			UserInterface.showError(null, e.getMessage());
		} catch (InterruptedException e){
			Log.write("Hypervisor was interrupted", "logic.Hypervisor", "CRITICAL");
		} catch (NoNodesAvailable e){
			Log.write("There was an error in collecting the results", "logic.Hypervisor", "CRITICAL");
		}

		UserInterface.showMessage("Simulations have finished.("+clones+" clones found)");
	}
	
	/*COMPLETARE.*/
	public void run(){
		try{
			while(true){
				synchronized(monitor){
					monitor.wait();
				}
				if (startSignal){
					runSimulations();
					startSignal = false;
				}
			}
		} catch (InterruptedException e){	}
	}
	
	private void sendSettings(RemoteServer server) throws RemoteException{
		Vector<String> data = Settings.orderedSettings();
		Iterator<String> it = data.iterator();
		while(it.hasNext()){
			try {
				server.push(it.next()+" ");
			} catch (RemoteException e) {
				throw e;
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
			
			if (stats.cloneWasFound()){
				server.push("1");
			}
			else{
				server.push("0");
			}
			
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
		try{
		RemoteServer server=Settings._server;
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
	}
	
	private void resetForNewSimulation(){
		try{
			Log.write("Preparing for a new simulation.." ,"logic.Hypervisor","FLOW");
			Ambient.clear();
			System.gc();
			clones = 0;
			loggedClone = false;
			idleNodes=0;
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
