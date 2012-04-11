package exceptions;

@SuppressWarnings("serial")
public class SettingsAreNotReady extends Exception {
	public SettingsAreNotReady(String reason){
		super(reason);
	}
	public String getReason(){
		return this.getReason();
	}
}
