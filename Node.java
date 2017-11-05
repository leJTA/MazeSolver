public class Node {
	private State state;
	private Action action;
	public int f;	// estimation
	public int g;	// cout
	public int h;	// valeur de l'heuristique
	
	public Node(State st, Action act, int cost, int heur){
		state = st;
		action = act;
		g = cost;
		h = heur;
	}
	
	public State state(){
		return state;
	}
}
