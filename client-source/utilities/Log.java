/**
* Log
* 
* Zironda Andrea -- PCD 2011/2012
* 
* Wireless Sensor Networks - Clone Detection Simulator
* 
* */
package utilities;

import gui.UserInterface;

import java.awt.Color;
/**
 * Simple log class: [what happened || where || how does it feels]
 * 
 * I left some commented filters of how i have used this class to debug the entire application.
 * FLOW and CLONE are magic words to show logs in the user interface. Its simple to mantain and customize.
 *
 */
public class Log{
	
	public static void write(String what, String domain, String severity){
		Thread t=Thread.currentThread();
		//if (/*"50".equals(domain) &&*/ "CRITICAL".equals(severity)){
		
		//if (/*"50".equals(domain) &&*/ "TEST".equals(severity)){
		//if (/*"50".equals(domain) &&*/ "SIMSTATUS".equals(severity)){
		
			
		//if (/*"50".equals(domain) &&*/ "TEST".equals(severity) && t.getId()==30){
		//if("BUG".equals(severity) || "CRITICAL".equals(severity)){
		if("BUG".equals(severity) || "CRITICAL".equals(severity)|| "SIMSTATUS".equals(severity)){
		//if("BUG".equals(severity) || "CRITICAL".equals(severity) || "HIGH".equals(severity) || "SIMSTATUS".equals(severity)){
	
		//if(!"USELESS".equals(severity)){
		//if (/*"50".equals(domain) &&*/ "BUG".equals(severity)){
		//if ("messages.Message".equals(domain) && "DEBUG".equals(severity)){
		
		System.out.println(t.getId()+"|---|"+severity+"|---|"+domain+"|---|"+what);
		//UserInterface.log(what, Color.red);
		}
		
		if ("FLOW".equals(severity)){
			UserInterface.log(what, Color.gray);
		}
		
		if ("CLONE".equals(severity)){
			UserInterface.log(what, Color.green);
		}
		
		
		
	}
	
	
	
	public static void write(String what){
		System.out.println(what);
	}
	
	
	
}
