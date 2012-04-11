/*
* NodeID
* 
* Zironda Andrea & Guerra Luca -- PCD 2011/2012
* 
* Wireless Sensor Networks -Clone-Detection Simulator
* 
* */


package logic;

import exceptions.*;

/*
 * 
 * This class permits automatic generation of new NodeIDs avoiding the user of the class Node in doing it.
 * Considering the purpose of the simulation it must be possible to use different IDs.It is possible to bypass 
 * the automatic generation of new ids using the constructor with int parameter.This parameter must sit in the range
 * of already used ids in the current simulation.The reset method restarts the generation of new ids.It is responsibility
 * of the user of the class to reset at the appropriate moment.
 * 
 * */

public class NodeID {
	
/***CLASS MEMBERS***/
	//used to start a new session of node ids.
	static int flag=0;
	
/***INSTANCE MEMBERS***/
	//stores the id.
	private Integer id;
	//meant for the generation of new ids.

/***CONSTRUCTORS***/
	//default generation of new ids.
	NodeID() throws NidOverflow{
		if (flag > Integer.MAX_VALUE) throw new NidOverflow("NodeID default constructor cannot use more ids.Please reset.");
		id=flag++;
	}
	
/***CLASS METHODS***/
/***INSTANCE METHODS***/
	
	/** utils **/
	public String toString(){
		return "["+id.toString()+"]";
	}
	
	public boolean equals(NodeID another){
		return id.equals(another.id);
	}
	
	public boolean equals(String string){
		return string.equals(toString());
	}
	
	/** getters **/
	public int number(){
		return id;
	}
	
	/** misc **/
	//starts a new session of ids.
	public void reset(){
		//flag=Integer.MIN_VALUE;
		flag=0;
	}
	
	/*
	public static void main(String args[]){
		Integer a=new Integer(2);
		Integer b=new Integer(2);
		System.out.println(new Boolean(a==b).toString());
	}*/
	
}
