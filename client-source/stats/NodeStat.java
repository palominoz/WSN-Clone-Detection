/*
* NodeStat
* 
* Zironda Andrea & Guerra Luca -- PCD 2011/2012
* 
* Wireless Sensor Networks - Clone Detection Simulator
* 
* */
package stats;

public class NodeStat {
	//class properties
	int sent;
	int received;
	double energy;
	int messages;
	int signatures;
	
	boolean foundClone = false;
	
	public NodeStat(){}
	
	public void reiceved(){
		received++;
	}
	public void sent(){
		sent++;
	}
	public void consumed(double e){
		energy=energy+e;
	}
	public void stored(){
		messages++;
	}
	public void decoded(){
		signatures++;
	}
	public void foundClone(){
		foundClone = true;
	}
}
