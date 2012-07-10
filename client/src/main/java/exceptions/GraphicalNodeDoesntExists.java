package exceptions;

@SuppressWarnings("serial")
public class GraphicalNodeDoesntExists extends Exception {
	public GraphicalNodeDoesntExists(String reason){
		super(reason);
	}
	public String getReason(){
		return this.getReason();
	}
}
