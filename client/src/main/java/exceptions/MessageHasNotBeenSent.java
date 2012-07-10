package exceptions;

@SuppressWarnings("serial")
public class MessageHasNotBeenSent extends Exception {
	public MessageHasNotBeenSent(String reason){
		super(reason);
	}
	public String getReason(){
		return this.getReason();
	}
}
