package exceptions;

@SuppressWarnings("serial")
public class NodeNotFound extends Exception {
	public NodeNotFound(String reason){
		super(reason);
	}
	public String getReason(){
		return this.getReason();
	}
}
