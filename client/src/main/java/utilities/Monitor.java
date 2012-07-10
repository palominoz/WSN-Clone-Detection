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
 * A Monitor keeps track if something is paused. An object that can be paused will have one monitor as
 * instance member and wait on it if it has to pause.
 * 
 * */
public class Monitor {
	public boolean paused;
	public Monitor(boolean b){
		paused = b;
	}
}
