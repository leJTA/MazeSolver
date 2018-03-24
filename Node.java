import java.util.ArrayList;
import java.util.Stack;

public class Node {
	public State state;
	public Action previous_action;	// action that
	public ArrayList<Action> future_actions; // actions that can be perfomed from the current node
	public ArrayList<Node> successors;
	public Node parent;	// parent of the node
	public int f;	// estimation
	public int g;	// cost
	public int h;	// heuristic
	public boolean completed; // have we generated all of the successors of this node?
	public boolean blocked;
	public int iterator;	// iterator for the successors
	public int backup;
	
	public Node(Node p, State st, Action prev, ArrayList<Action> fut, int cost, int heur){
		successors = new ArrayList<Node>(0);
		parent = p;
		state = st;
		previous_action = prev;
		future_actions = fut;
		g = cost;
		h = heur;
		f = g + h;
		iterator = 0;
		backup = Integer.MAX_VALUE;
		completed = false;
		blocked = true;
	}
	
	public void removeSuccessor(Node s) {
		// Search the index of s.previous_action
		System.out.println("Removing " + s + " from " + this);
		int i = 0;
		while (future_actions.get(i) != s.previous_action) {
			++i;
		}
		
		// If it was the last one then we set the node to 'completed' = true
		if ((completed == false) && (i == future_actions.size() - 1)) {
			completed = true;
		}
		
		future_actions.remove(s.previous_action);
		successors.remove(s);
		
		if (future_actions.isEmpty()) {
			completed = false;
		}
		
		if (i < iterator) {
			iterator = (iterator - 1) % future_actions.size();
		}
	}
	
	public void updateCost() {
		if (future_actions.isEmpty()) {
			f = Integer.MAX_VALUE;
			return;
		}
		
		f = backup;
		for (Node child : successors) {
			f = Math.min(f, child.f);
		}
	}
	
	public boolean equals(Node other) {
		return (this.state == other.state && this.previous_action == other.previous_action);
	}
	
	@Override
	public String toString(){
		return "(" + state.position.toString() + future_actions + successors.size() + ",f=" + f + ",g=" + g + ",h=" + h + ",b=" + backup + ")";
	}
}
