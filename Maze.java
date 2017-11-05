import java.util.ArrayList;
import java.lang.Math.*;

public class Maze {
	// The map of the maze : 2D array
	// '0' = open/free, 'X' = current position, '#' = wall/block, 'G' = Goal/Exit
	private char[][] map;
	private State initialState;
	private ArrayList<Position> goals;
	private final int limit;	// majoration of the value of h(n)
	
	// Constructor
	public Maze(char[][] m){
		map = m;
		limit = m.length + m[0].length;
		goals = new ArrayList<Position>(0);
		for (int i = 0; i < m.length; ++i) {
			for (int j = 0; j < m[i].length; ++j) {
				System.out.print(m[i][j]);
				if (m[i][j] == 'X')
					initialState = new State(new Position(i, j));
				else if (m[i][j] == 'G')
					goals.add(new Position(i, j));
					
			}
			System.out.println();
		}
		System.out.println("Initial position : " + initialState.position());
		System.out.print("Goal(s) : ");
		for(Position p : goals)
			System.out.print(p + " ");
		System.out.println();
	}
	
	// Getter of initialState
	public State initialState(){
		return initialState;
	}

	// Goal test
	public boolean goalTest(State state){
		for (Position p : goals) {
			// If the position of the state coincides with that of one of the goals, return true
			if(state.position().equals(p))
				return true;
		}
		return false;
	}
	
	// Actions that can be perfomed from a state
	public ArrayList<Action> actions(State state){
		int i = state.position().i;
		int j = state.position().j;
		ArrayList<Action> actions = new ArrayList<Action>(0);
		
		if (i > 0 && map[i - 1][j] != '#') 
			actions.add(Action.NORTH);
		if (i < map.length - 1 && map[i + 1][j] != '#') 
			actions.add(Action.SOUTH);
		if (j > 0 && map[i][j - 1] != '#')
			actions.add(Action.WEST);
		if (j < map[i].length - 1 && map[i][j + 1] != '#')
			actions.add(Action.EAST);
			
		return actions;
	}
	
	// Initial Node : the root
	public Node root(){
		return new Node(initialState, null, 0, h1(initialState));
	}

	// Child node created from a parent node and an action
	public Node childNode(Node parent, Action action){
		State st = null;
		if (action == Action.NORTH)
			st = new State(new Position(parent.state().position().i - 1, parent.state().position().j));
			
		if (action == Action.SOUTH)
			st = new State(new Position(parent.state().position().i + 1, parent.state().position().j));

		if (action == Action.WEST)
			st = new State(new Position(parent.state().position().i, parent.state().position().j - 1));

		if (action == Action.EAST)
			st = new State(new Position(parent.state().position().i, parent.state().position().j + 1));
		
		return new Node(st, action, parent.g + 1, h1(st));
	}

	// Heuristic : Manhattan distance
	public int h1(State state){
		int h = limit;
		for(Position p : goals){
			int min = Math.abs(p.i - state.position().i) + Math.abs(p.j - state.position().j);
			h = (h > min) ? min : h;
		}
		return h;
	}
	
	// Heuristic :  Euclidean distance
	public double h2(State state){
		double h = limit;
		for(Position p : goals){
			double min = Math.sqrt(Math.pow(p.i - state.position().i, 2) + Math.pow(p.j - state.position().j, 2));
			h = (h > min) ? min : h;
		}
		return h;
	}
}

