public class ResultAndCost {
	public static enum Status {SUCCESS, FAILURE};
	public Status status;
	public int cost;
	
	public ResultAndCost(Status s, int c){
		status = s;
		cost = c;
	}
}
