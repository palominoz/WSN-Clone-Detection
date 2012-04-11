/*
* SimulationResults
* 
* Zironda Andrea & Guerra Luca -- PCD 2011/2012
* 
* Wireless Sensor Networks - Clone Detection Simulator
* 
* */
package logic;

import java.util.Vector;
import stats.*;

public class SimulationResult {
	
	//singleton implementation
	private static SimulationResult singleton=null;
	
	public static synchronized SimulationResult  simulationResult(){
		if (singleton==null) singleton=new SimulationResult();
		return singleton;
	}
	
	private SimulationResult(){
		buffer=new Vector<SimStat>();
	}
	
	//class methods
	public static void addSim(SimStat sim){
		simulationResult().buffer.add(sim);
	}
	
	//not yet sent simulations
	Vector<SimStat> buffer;
}
