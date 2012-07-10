/**
* StopWatch
* 
* Zironda Andrea -- PCD 2011/2012
* 
* Wireless Sensor Networks - Clone Detection Simulator
* 
* */
package utilities;

/**
 *  Simple stopwatch to measure how does it take to do something.
 *
 */
public class StopWatch {
	
	private static long startTime = 0;
	private static boolean running = false;
	
	public static void start(){
		if (running == false){
			startTime = System.currentTimeMillis();
			running = true;
		}
		else{
			//throw..
		}
	}
	
	public static long stop(){
		if (running==true){
			running = false;
			return (System.currentTimeMillis() - startTime);
		}
		else{
			//throw..
			return 0;
		}
	}
}
