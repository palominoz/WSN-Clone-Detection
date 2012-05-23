package utilities;

import java.util.Timer;

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
