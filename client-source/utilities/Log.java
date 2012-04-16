/*
* Log
* 
* Zironda Andrea & Guerra Luca -- PCD 2011/2012
* 
* Wireless Sensor Networks - Clone Detection Simulator
* 
* */
package utilities;

import gui.UserInterface;

import java.awt.Color;

public class Log{
	
	public static void write(String what, String domain, String severity){
		Thread t=Thread.currentThread();
		//if (/*"50".equals(domain) &&*/ "CRITICAL".equals(severity)){
		
		//if (/*"50".equals(domain) &&*/ "TEST".equals(severity)){
		//if (/*"50".equals(domain) &&*/ "SIMSTATUS".equals(severity)){
		
			
		//if (/*"50".equals(domain) &&*/ "TEST".equals(severity) && t.getId()==30){
		//if("BUG".equals(severity) || "CRITICAL".equals(severity)){
		if("BUG".equals(severity) || "CRITICAL".equals(severity)){
		//if("BUG".equals(severity) || "CRITICAL".equals(severity) || "HIGH".equals(severity) || "SIMSTATUS".equals(severity)){
	
		//if(!"USELESS".equals(severity)){
		//if (/*"50".equals(domain) &&*/ "BUG".equals(severity)){
		//if ("messages.Message".equals(domain) && "DEBUG".equals(severity)){
		
		System.out.println(t.getId()+"|---|"+severity+"|---|"+domain+"|---|"+what);
		UserInterface.log(what, Color.red);
		}
		
		if ("FLOW".equals(severity)){
			UserInterface.log(what, Color.gray);
		}
		
		
		
	}
	
	
	
	public static void write(String what){
		System.out.println(what);
	}
	
	
	
}
