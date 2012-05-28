/*
* SimStat
* 
* Zironda Andrea & Guerra Luca -- PCD 2011/2012
* 
* Wireless Sensor Networks - Clone Detection Simulator
* 
* */
package stats;
import java.util.Iterator;
import java.util.Vector;
import exceptions.*;

/**
 * Records class for an entire simulation
 *
 */
public class SimStat {
	
	public enum ValueType{
		SENT, RECEIVED, SIGNATURES, ENERGY, STORED
	}
	
	Vector<NodeStat> nodesStats;
	
	public SimStat(Vector<NodeStat> n){
		nodesStats=n;
	}
	
	public String toString(){
		try{
			return	"MIN Sent Messages:"		+minimum(SimStat.ValueType.SENT)+ "\n"+ 
					"MIN Received Messages:"	+minimum(SimStat.ValueType.RECEIVED)+ "\n"+
					"MIN Number of Signatures:"	+minimum(SimStat.ValueType.SIGNATURES)+ "\n"+
					"MIN Consumed Energy:"		+minimum(SimStat.ValueType.ENERGY)+ "\n"+
					"MIN Consumed Energy:"		+minimum(SimStat.ValueType.ENERGY)+ "\n"+
					"MIN Stored Messages:"		+minimum(SimStat.ValueType.STORED)+ "\n"+
					
					"MAX Sent Messages:"		+maximum(SimStat.ValueType.SENT)+ "\n"+
					"MAX Received Messages:"	+maximum(SimStat.ValueType.RECEIVED)+ "\n"+
					"MAX Number of Signatures:"	+maximum(SimStat.ValueType.SIGNATURES)+ "\n"+
					"MAX Consumed Energy:"		+maximum(SimStat.ValueType.ENERGY)+ "\n"+
					"MAX Stored Messages:"		+maximum(SimStat.ValueType.STORED)+ "\n"+
				
					"AVG Sent Messages:"		+average(SimStat.ValueType.SENT)+ "\n"+
					"AVG Received Messages:"	+average(SimStat.ValueType.RECEIVED)+ "\n"+
					"AVG Number of Signatures:"	+average(SimStat.ValueType.SIGNATURES)+ "\n"+
					"AVG Consumed Energy:"		+average(SimStat.ValueType.ENERGY)+ "\n"+
					"AVG Stored Messages:"		+average(SimStat.ValueType.STORED)+ "\n"+
				
				
					"STANDARD DEVIATION Sent Messages:"			+standardDeviation(SimStat.ValueType.SENT)+ "\n"+
					"STANDARD DEVIATION Received Messages:"		+standardDeviation(SimStat.ValueType.RECEIVED)+ "\n"+
					"STANDARD DEVIATION Number of Signatures:"	+standardDeviation(SimStat.ValueType.SIGNATURES)+ "\n"+
					"STANDARD DEVIATION Consumed Energy:"		+standardDeviation(SimStat.ValueType.ENERGY)+ "\n"+
					"STANDARD DEVIATION Stored Messages:"		+standardDeviation(SimStat.ValueType.STORED)+ "\n"+
					"CLONE WAS FOUND:"		+cloneWasFound().toString()+ "\n";
		}
		catch (BadValue e){}
		return null;
	}
	
	private Boolean found = null;
	
	
	
	public Boolean cloneWasFound(){
		if (found != null) return found;
		else{
			Iterator<NodeStat> it = nodesStats.iterator();
			found = false;
			while(it.hasNext()){
				if (found.equals(true)) break;
				found = it.next().foundClone;
			}
		}
		return found;
	}
	
	public Double minimum(ValueType what) throws BadValue{
		synchronized(nodesStats){
			Iterator<NodeStat> i=nodesStats.iterator();
			double min=Double.MAX_VALUE;
			while(i.hasNext()){
				NodeStat singleNodeStatistics=i.next();
				switch (what){
				case SENT:
					if (singleNodeStatistics.sent<min) min=singleNodeStatistics.sent;
					break;
				case RECEIVED:
					if (singleNodeStatistics.received<min) min=singleNodeStatistics.received;
					break;
				case SIGNATURES:
					if (singleNodeStatistics.signatures<min) min=singleNodeStatistics.signatures;
					break;
				case ENERGY:
					if (singleNodeStatistics.energy<min) min=singleNodeStatistics.energy;
					break;
				case STORED:
					if (singleNodeStatistics.sent<min) min=singleNodeStatistics.sent;
					break;
				default:
					throw new BadValue("Bad choice for statistic");	
				}
			}
			return min;
		}
	}
	
	public Double maximum(ValueType what) throws BadValue{
		synchronized(nodesStats){
			Iterator<NodeStat> i=nodesStats.iterator();
			double max=Double.MIN_VALUE;
			while(i.hasNext()){
				NodeStat singleNodeStatistics=i.next();
				switch (what){
				case SENT:
					if (singleNodeStatistics.sent>max) max=singleNodeStatistics.sent;
					break;
				case RECEIVED:
					if (singleNodeStatistics.received<max) max=singleNodeStatistics.received;
					break;
				case SIGNATURES:
					if (singleNodeStatistics.signatures<max) max=singleNodeStatistics.signatures;
					break;
				case ENERGY:
					if (singleNodeStatistics.energy<max) max=singleNodeStatistics.energy;
					break;
				case STORED:
					if (singleNodeStatistics.sent<max) max=singleNodeStatistics.sent;
					break;
				default:
					throw new BadValue("Bad choice for statistic");	
				}
			}
			return max;
		}
	}
	
	public Double average(ValueType what) throws BadValue{
		synchronized(nodesStats){
			Iterator<NodeStat> i=nodesStats.iterator();
			double tot=0;
			int count=0;
			while(i.hasNext()){
				count++;
				NodeStat singleNodeStatistics=i.next();
				switch (what){
				case SENT:
					tot+=singleNodeStatistics.sent;
					break;
				case RECEIVED:
					tot+=singleNodeStatistics.received;
					break;
				case SIGNATURES:
					tot+=singleNodeStatistics.signatures;
					break;
				case ENERGY:
					tot+=singleNodeStatistics.energy;
					break;
				case STORED:
					tot+=singleNodeStatistics.messages;
					break;
				default:
					throw new BadValue("Bad choice for statistic");	
				}
			}
			return tot/count;
		}
		
	}
	
	public Double standardDeviation(ValueType what) throws BadValue{
		synchronized(nodesStats){
			double average=average(what);
			double sum=0;
			Iterator<NodeStat> i=nodesStats.iterator();
			while(i.hasNext()){
				NodeStat singleNodeStatistics=i.next();
				switch (what){
				case SENT:
					sum+=Math.pow(singleNodeStatistics.sent-average, 2);
					break;
				case RECEIVED:
					sum+=Math.pow(singleNodeStatistics.received-average, 2);
					break;
				case SIGNATURES:
					sum+=Math.pow(singleNodeStatistics.signatures-average, 2);
					break;
				case ENERGY:
					sum+=Math.pow(singleNodeStatistics.energy-average, 2);
					break;
				case STORED:
					sum+=Math.pow(singleNodeStatistics.messages-average, 2);
					break;
				default:
					throw new BadValue("Bad choice for statistic");	
				}
			}
			
			return Math.sqrt(sum/nodesStats.size());
		}
	}
		
}
