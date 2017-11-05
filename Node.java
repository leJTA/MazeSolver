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
		f = g + h;
	}
	
	public State state(){
		return state;
	}
	
	@Override
	public String toString(){
		return "[" + state.position().toString() + ", " + f + ", " + g + ", " + h + "]";
	}
}
