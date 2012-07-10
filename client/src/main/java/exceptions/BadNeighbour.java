package exceptions;

@SuppressWarnings("serial")
public class BadNeighbour extends Exception {
	public BadNeighbour(String reason){
		super(reason);
	}
	public String getReason(){
		return this.getReason();
	}
}
