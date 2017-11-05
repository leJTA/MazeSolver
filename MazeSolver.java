
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
	}
}
