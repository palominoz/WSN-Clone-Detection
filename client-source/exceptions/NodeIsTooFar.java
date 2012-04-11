package exceptions;

@SuppressWarnings("serial")
public class NodeIsTooFar extends Exception {
	public NodeIsTooFar(String reason){
		super(reason);
	}
	public String getReason(){
		return this.getReason();
	}
}
