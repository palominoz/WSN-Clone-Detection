package exceptions;

@SuppressWarnings("serial")
public class NodeIsNotActive extends Exception {
	public NodeIsNotActive(String reason){
		super(reason);
	}
	public String getReason(){
		return this.getReason();
	}
}
