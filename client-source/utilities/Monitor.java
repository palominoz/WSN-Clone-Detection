/**
* Monitor
* 
* Zironda Andrea -- PCD 2011/2012
* 
* Wireless Sensor Networks - Clone Detection Simulator
* 
* */
package utilities;


/**
 * This class is responsible for the managing of the simulation.It creates and starts the nodes as it is required in the
 * configuration file.
 * In order to work it other modules of the software must set up static variables of this class calling the setup method.
 * This class approaches the problem with a singleton pattern and its methods are publicly available.
 * 
 * */
public class Monitor {
	public boolean paused;
	public Monitor(boolean b){
		paused = b;
	}
}
