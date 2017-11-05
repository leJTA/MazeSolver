public class Position {
	public int i;
	public int j;
	
	public Position(int line, int col){
		i = line;
		j = col;
	}
	
	public String toString(){
		return "[" + i + ", " + j + "]";
	}
	
	public boolean equals(Position other){
		return (this.i == other.i && this.j == other.j);
	}
}
