import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Comparator;
import java.lang.Math.*;
import java.io.*;

public class MazeSolver {
	public static void main(String [] args){
		char[][] map = readMapFromFile("map.txt");
		
		Maze maze = new Maze(map);
		RecursiveBestFirstSearch(maze);
	}
	
	// Simplified Memory-Bounded A*
	public static ArrayList<Node> SMA_stars(Maze maze){
		
		return null;
	}
	
	//  Recursive Best-First Search
	public static void RecursiveBestFirstSearch(Maze maze){
		ResultAndCost result = RBFS(maze, maze.root(), MAX);
		System.out.println(result.status + " cost = " + result.cost);
	}
	
	// RBFS
	public static ResultAndCost RBFS(Maze maze, Node node, int f_limit){
		if(maze.goalTest(node.state())){
			System.out.println("Exit found at " + node.state().position() + "\n");
			return new ResultAndCost(ResultAndCost.Status.SUCCESS, node.g);
		} 
		
		// Priority queue to keep node ordered by f
		PriorityQueue<Node> successors = new PriorityQueue<Node>(2, 
			new Comparator<Node>(){
				public int compare(Node n1, Node n2){
					if (n1.f < n2.f) return -1;
					else if (n1.f == n2.f) return 0;
					else return 1;
				}
			});
			
		// for all available actions from the current state
		for (Action action : maze.actions(node.state())) {
			
			// Do not add the rollback actions!!
			if(action != reverse(node.action()))
				successors.add(maze.childNode(node, action));
		}
		
		// No successors => dead end
		if (successors.isEmpty()) {
			System.out.println("dead end " + node.state().position());
			return (new ResultAndCost(ResultAndCost.Status.FAILURE, MAX));
		}
		
		for (Node s : successors) {
			s.f = Math.max(node.f, s.g + s.h); // s.f = max(s.g + s.h, node.f)
		}

		// Forever loop
		while (true) {
			Node best = successors.poll();
			
			if (best.f > f_limit) 
				return new ResultAndCost(ResultAndCost.Status.FAILURE, best.f);
				
			Node alternative = successors.peek();
			ResultAndCost r;
			if(alternative != null){
				r = RBFS(maze, best, Math.min(f_limit, alternative.f));
			}
			else
				r = RBFS(maze, best, f_limit);
			best.f = r.cost;
			successors.add(best);
			if (r.status != ResultAndCost.Status.FAILURE) {
				System.out.println(node.state().position());
				return r;
			}
		}
	}
	
	// Reverse actions
	public static Action reverse(Action action){
		if(action == Action.NORTH)
			return Action.SOUTH;
		if(action == Action.SOUTH)
			return Action.NORTH;
		if(action == Action.EAST)
			return Action.WEST;
		if(action == Action.WEST)
			return Action.EAST;
		else
			return null;
	}
	
	// Read map from the txt file
	public static char[][] readMapFromFile(String filename){
		int nbCols = 0, nbLines = 0;
		char[][] map = null;
		try{
			BufferedReader fichier = new BufferedReader(new FileReader(filename));
			String line = "";
			
			// First, we count the number of lines and columns to determine de dim. of the map
			while ((line = fichier.readLine()) != null) {
				nbCols = line.length();
				++nbLines;
			}
			fichier.close();
			map = new char[nbLines][nbCols];
			
			line = "";
			int i = 0;
			fichier = new BufferedReader(new FileReader(filename));
			while ((line = fichier.readLine()) != null){
				for(int j = 0; j < nbCols; ++j)
					map[i][j] = line.charAt(j);
				++i;
			}
			fichier.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
		return map;
	}
	
	public static final int MAX = Integer.MAX_VALUE;
}
