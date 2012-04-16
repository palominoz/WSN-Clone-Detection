package parser;

import java.io.*;
import java.net.*;

import utilities.Log;
import logic.*;
import enums.SupportedProtocol;
import gui.UserInterface;

public class Parser{
	private  URL url;
	public Parser(String address){
		try{ 
				url = new URL(address); 
				InputStream stream=url.openStream ();
				BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
				String document; 
				while ((document = reader.readLine()) != null) { 
					document=document.replace(" ","");
					document=document.replace("\n","");
					document=document.replace("\r","");
					document=document.replace("\t","");
						if(document.contains("PROTO="))
							Settings.protocol=SupportedProtocol.protocolWithString(document.substring(6));
							//Settings.protocol=SupportedProtocol.LSM;
						else if(document.contains("NSIM="))
							Settings.numberOfSimulations=Integer.parseInt(document.substring(5));
						else if(document.contains("p="))
							Settings.claimForwardProbability=Double.parseDouble(document.substring(2));
						else if(document.contains("g="))
							Settings.numberOfForwards=Integer.parseInt(document.substring(2));
						else if(document.contains("n="))
							Settings.numberOfNodes=Integer.parseInt(document.substring(2));
							//Settings.numberOfNodes=100;
						else if(document.contains("r="))
							Settings.transmissionRange=Double.parseDouble(document.substring(2));
							//Settings.transmissionRange=0.3;
						else if(document.contains("E="))
							Settings.defaultMaxEnergy=Integer.parseInt(document.substring(2));
						else if(document.contains("E_send="))
							Settings.transmitConsumption=Integer.parseInt(document.substring(7));
						else if(document.contains("E_receive="))
							Settings.receiveConsumption=Integer.parseInt(document.substring(10));
						else if(document.contains("E_signature="))
							Settings.signatureConsumption=Integer.parseInt(document.substring(12));	
					}
				Log.write("Settings were loaded from: "+ url, "parser.Parser", "FLOW");
					//set.print();
	}  catch (MalformedURLException mue) {
		Log.write("Inserted URL is not valid.", "parser.Parser", "HIGH");
		UserInterface.showError(null, "Inserted URL is not valid.");
	}  catch (IOException ioe) {
		Log.write("There was a network error", "parser.Parser", "HIGH");
		UserInterface.showError(null, "Please check your network connection.");
	} 
  }
	
}

