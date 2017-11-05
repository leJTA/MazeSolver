import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Comparator;

public class MazeSolver {
	public static void main(String [] args){
		char[][] map = {	
			{'0', '#', '#', '#', '#', '#'},
			{'0', '0', 'X', '0', '0', '0'},
			{'#', '0', '#', '0', '#', '0'},
			{'#', '0', '#', '0', '#', '0'},
			{'0', '0', '0', '#', '0', 'G'},
			{'#', '#', '0', '0', '0', '#'}
		};
		Maze maze = new Maze(map);
		System.out.println("Actions : " + maze.actions(maze.initialState()));
		System.out.println("h(n0) = " + maze.h1(maze.initialState()));
		RBFS(maze, maze.root());
	}
	
	//  Recursive Best-First Search
	public static ArrayList<Node> RBFS(Maze maze, Node node){
		
		if(maze.goalTest(node.state())) return node;
		
		// Priority queue to keep node ordered by f
		PriorityQueue<Node> successors = new PriorityQueue<Node>(1, 
			new Comparator<Node>(){
				public int compare(Node n1, Node n2){
					if (n1.f < n2.f) return -1;
					else if (n1.f == n2.f) return 0;
					else return 1;
				}
			});
		for (Action action : maze.actions(node.state())) {
			successors.add(maze.childNode(node, action));
		}
		
		if (successors.isEmpty()) return null;
		for (Node s : successors) {
			if (node.f > s.f) s.f = node.f;
		}
		
		// Forever loop
		while (true) {
			Node best = successors.poll();
			
		}
		
		return null;
	}
}
