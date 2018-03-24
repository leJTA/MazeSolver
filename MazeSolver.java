import java.util.ArrayList;
import java.util.Stack;
import java.util.PriorityQueue;
import java.util.Comparator;
import java.lang.Math.*;
import java.io.*;

public class MazeSolver {
	
	public static void main(String [] args){
		if (args.length < 2) {
			System.out.println("Usage : java MazeSolver [map_file] [memory_size]");
			return;
		}
		char[][] map = readMapFromFile(args[0]);
		
		Maze maze = new Maze(map);
		Stack<Node> solution_path = SMA_stars(maze, Integer.parseInt(args[1]));
		if (solution_path == null) {
			System.out.println("\nunreachable solution or insufficient memory :( ");
		} else {
			System.out.println("Solved... Solution cost : d=" + (solution_path.size() - 1));
			while (!solution_path.isEmpty()) {
				Position p = solution_path.pop().state.position;
				if(map[p.i][p.j] != 'G')
					map[p.i][p.j] = ' ';
			}
			for (char[] t : map) {
				for(char c : t) {
					System.out.print(c);
				}
				System.out.println();
			}
		}
	}
	
	// Simplified Memory-Bounded A*
	public static Stack<Node> SMA_stars(Maze maze, int maxSize){
		
		// List of nodes from start to the goal
		Stack<Node> path = new Stack<Node>();
		
		// List of closes nodes
		ArrayList<Node> closed = new ArrayList<Node>(0); 
		
		// Priority queue to keep node ordered by f
		ArrayList<Node> open = new ArrayList<Node>(10); 
		
		open.add(maze.root());
				
		// loop do
		int nbNodesGenerated = 1;
		while (true) {
			
			++nbNodesGenerated;
			
			// if Queue is empty then return failure
			if (open.isEmpty()) {
				return null;
			}
			
			// n ← deepest least-f-cost node in Queue
			Node node = best(open);
			
			if (node.f == INFINITY) {
				System.out.println("closed " + closed);
				return null;
			}
			
			// if GOAL-TEST(n) then return success
			if (maze.goalTest(node.state)) {
				do {
					path.push(node);
					node = node.parent;
				} while (node != null);
				System.out.println(nbNodesGenerated + " nodes generated");
				System.out.println("memory : " + (open.size() + closed.size()) + "/" + maxSize );
				return path;
			}
			
			// s ← NEXT-SUCCESSOR(n)
			Node s = maze.nextSuccessor(node);
			// If node has no successors then update node's cost and those of its ancestor if necessary
			if (s == null) {
				node.f = INFINITY;
				updateAncestors(node);
				continue;
			}
			
			// if s is not a goal and is at maximum depth then
			if (!maze.goalTest(s.state) && s.g == maxSize - 1) {
				s.f = INFINITY;
			} else {
				s.f = Math.max(node.f, s.g + s.h);
			}
			
			boolean keepInMemory = true;
			if (contains(open, s) || contains(closed, s)) {
				keepInMemory = insertInto(s, open, closed);
				if (!keepInMemory) {
					node.removeSuccessor(s);
					if(node.completed) {
						node.updateCost();
						updateAncestors(node);
					}
					continue;
				}
			}
			// if all of n’s successors have been generated then
			if (node.completed) {
				
				// update n’s f-cost ...
				node.updateCost();
				
				// ... and those of its ancestors if necessary
				updateAncestors(node);
				
				if (node.future_actions.size() == node.successors.size()) {
					// ... then remove n from Queue but keep it in memory
					closed.add(node);
					open.remove(node);
				}
			}
				
			// if memory is full then
			if ((open.size() + closed.size()) >= maxSize) {
				//System.out.println("memory is full!!!");
				
				// delete the shallowest, highest-f-cost leaf's node in Queue
				Node worst = null;
				for (Node n : open) {
					// A leaf node
					if (n.successors.size() == 0) {
						if (worst == null) worst = n;
						else if (worst.f < n.f || (worst.f == n.f && worst.g > n.g)) {
							worst = n;
						}
					}
				}
				
				open.remove(worst);
				Node parent = worst.parent;
				
				if (worst.parent != null ) {
					parent.backup = Math.min(worst.f, worst.parent.backup);
					parent.successors.remove(worst);
					
					// insert its parent on Queue if necessary
					if (!contains(open, parent)) {
						open.add(parent);
						closed.remove(parent);
					}
				}
				
				if (worst.f < INFINITY) {
					node.blocked = false;
				}
			}
			
			// insert s on OPEN
			open.add(s);
			//System.out.println("open " + open);
			//System.out.println("closed " + closed);
			//System.out.println("memory : " + (open.size() + closed.size()) + "/" + maxSize );
			//System.out.println("\n======================================\n");
		}
	}
	
	public static void updateAncestors(Node node) {
		Node cursor = node;
		boolean changed = true;
		
		while (changed && cursor.parent != null && cursor.parent.completed) {
			int tmp = cursor.parent.f;
			cursor.parent.f = cursor.parent.backup;
			
			for (Node child : cursor.parent.successors) {
				cursor.parent.f = Math.min(cursor.parent.f, child.f);
			}
			if (tmp == cursor.parent.f)
				changed = false;
			cursor = cursor.parent;
		}
	}
	
	
	public static boolean insertInto(Node s, ArrayList<Node> open, ArrayList<Node> closed) {
		for (Node other : open) {
			if (other.state.position.equals(s.state.position)) {
				if(s.g >= other.g) {
					return false;
				}
				else {
					System.out.println("replacing " + other + " by " + s);
					Node parent = other.parent;
					parent.removeSuccessor(other);
					open.remove(other);
					parent.updateCost();
					
					updateAncestors(parent);
					
					if (contains(open, parent)) {
						if (parent.future_actions.size() > 0 
							&& parent.successors.size() == parent.future_actions.size()) {
							open.remove(parent);
							closed.add(parent);
						}
					} else {
						if (parent.successors.isEmpty()) {
							closed.remove(parent);
							open.add(parent);
						}
					}
					return true;
				}
			}
		}
		
		// Idem for the closed
		for (Node other : closed) {
			if (other.state.position.equals(s.state.position)) {
				if(s.g >= other.g) {
					return false;
				}
				else {
					System.out.println("replacing " + other + " by " + s);
					Node parent = other.parent;
					try {
						parent.removeSuccessor(other);
					} catch(Exception e) {
						System.out.println(parent.parent);
					}
					closed.remove(other);
					parent.updateCost();
					
					updateAncestors(parent);
					
					if (contains(open, parent)) {
						if (parent.future_actions.size() > 0  
							&& parent.successors.size() == parent.future_actions.size()) {
							open.remove(parent);
							closed.add(parent);
						}
					} else {
						if (parent.successors.isEmpty()) {
							closed.remove(parent);
							open.add(parent);
						}
					}
					return true;
				}
			}
		}
		return false;
	}
	
	// If the ArrayList contains the node
	public static boolean contains(ArrayList<Node> list, Node node){
		for (Node n : list) {
			if (node.state.position.equals(n.state.position) /*&& node.previous_action == n.previous_action*/)
				return true;
		}
		return false;
	}
	
	// The best node in the list
	public static Node best(ArrayList<Node> list) {
		Node n = null;
		for (Node other : list) {
			if (n == null) n = other;
			else if ((n.f > other.f) || (n.f == other.f && n.g < other.g)) {
				n = other;
			}
		}
		return n;
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
	
	public static final int INFINITY = Integer.MAX_VALUE;
}
