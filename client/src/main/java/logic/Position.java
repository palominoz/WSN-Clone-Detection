/**
* Position
* 
* Zironda Andrea -- PCD 2011/2012
* 
* Wireless Sensor Networks - Clone Detection Simulator
* 
* */
package logic;

import exceptions.*;

import java.lang.Math;
import logic.NodeID;
import java.util.Random;
import java.lang.String;
import java.text.DecimalFormat;

import utilities.Log;
/**
 * 
 * This class provides an interface to indetify a point in 2D space.
 * It forecasts bounds to check wether a new position is valid. 
 * 
 * */


public class Position implements Cloneable{

	
	//instance members.Couples identify one point.
	public Double X;
	public Double Y;
	
	//bounds to be eventually reduced on user needs.
	static final double x_lower_bound=0;
	static final double x_upper_bound=1;
	static final double y_lower_bound=0;
	static final double y_upper_bound=1;
	
	
	public Position clone() throws NullPointerException{
		if (X==null || Y==null) throw new NullPointerException("Tried to clone a Position which has null members.");
		Position clone;
		try {
			clone = (Position)super.clone();
			clone.X=new Double(X);
			clone.X=new Double(Y);
			return clone;
		} catch (CloneNotSupportedException e) {}
		return null;
	}
	
	public String toString(){
		DecimalFormat d=new DecimalFormat("#.##");
		return "("+d.format(X)+"||"+d.format(Y)+")";
	}
	
	public double distanceFrom(Position another) throws NullPointerException{
		if (another==null) throw new NullPointerException("Tried to calculate distance betwenn two null points");
		return distance(this,another);
	}
	
	public static double distance(Position p1, Position p2) throws NullPointerException{
		if (p1==null || p2==null) throw new NullPointerException("Tried to calculate distance between two null points");
		return Math.sqrt(Math.pow(p1.X-p2.X,2)+ Math.pow(p1.Y-p2.Y,2));
	}
	
	private static double normalize(double toNormalize){
		toNormalize=Math.abs(toNormalize);
		double flag=1;
		while (flag<toNormalize){
			flag=flag*10;
		}
		return toNormalize/flag;
	}
	
	//simple hash function.
	static public Position hashed(NodeID id, int rand, int counter) throws NullPointerException, ErrorCalulatingAnHashedPosition{
		if (id==null) throw new NullPointerException("Tried to hash a null NodeID");
		String stringToHashX=new String(id.toString()+rand+counter);
		String stringToHashY=new String(counter+rand+id.toString());
		
		double doubleForX=stringToHashX.hashCode();
		double doubleForY=stringToHashY.hashCode();
		
		Double normalizedDoubleForX=normalize(doubleForX);
		Double normalizedDoubleForY=normalize(doubleForY);
		
		//Log.write("NORMALIZE DEBUG: \n from " +stringToHashX +"\t"+stringToHashY +"\t\n"+"to "+normalizedDoubleForX.toString()+ "\t"+normalizedDoubleForY.toString());
		
		try{
			return new Position(normalizedDoubleForX,normalizedDoubleForY);
		}
		catch(CoordinateOutOfBounds e){
			throw new ErrorCalulatingAnHashedPosition("It was not possible to calculate correct coordinates to build a new Position");
		}	
	}
	
	public boolean equals(Position other){
		Log.write("CHECKING POS: "+this+ " WITH "+ other+ " RESULT: "+ new Boolean(X==other.X && Y==other.Y).toString(), "logic.Position", "VERBOSE");
		return X.equals(other.X) && Y.equals(other.Y);
	}
	
	//random position userful in creating new nodes.
	static public Position random(){
		Random generator=new Random();
		double x=generator.nextDouble()*x_upper_bound;
		double y=generator.nextDouble()*y_upper_bound;
		//DecimalFormat d=new DecimalFormat("#.##");
		//Log.write(d.format(x_upper_bound), d.format(y_upper_bound), "FINE");
		try{
			return new Position(x,y);
		}
		catch (CoordinateOutOfBounds e){
			return null;
		}
	}
	
	//constructor for new positions.
	public Position(double x, double y) throws CoordinateOutOfBounds{
		if (x<x_lower_bound || x>x_upper_bound || y<y_lower_bound || y>y_upper_bound) { 
			throw new CoordinateOutOfBounds("Tried to initialize a position with parameters out of bounds");
		}
		X=x;
		Y=y;
	}
}
