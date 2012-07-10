package exceptions;

@SuppressWarnings("serial")
public class WrongUrlPanel extends Exception {
	public WrongUrlPanel(String reason){
		super(reason);
	}
	public String getReason(){
		return this.getReason();
	}
}
