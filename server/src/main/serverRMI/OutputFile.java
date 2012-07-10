package serverRMI;



import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class OutputFile {
	
	public static void write(String what){
		FileWriter handle;
		try {
			handle = new FileWriter("output.txt", true);
			BufferedWriter output = new BufferedWriter(handle);
			output.write(what);
			output.flush();
			output.close();
		} catch (IOException e) {
			
		}
	}
	
}
