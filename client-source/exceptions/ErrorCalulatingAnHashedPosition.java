package exceptions;

@SuppressWarnings("serial")
public class ErrorCalulatingAnHashedPosition extends Exception {
	public ErrorCalulatingAnHashedPosition(String reason){
		super(reason);
	}
	public String getReason(){
		return this.getReason();
	}
}
