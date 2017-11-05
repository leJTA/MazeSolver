public class State {
	private Position position;
	
	public State(Position p){
		position = p;
	}
	public Position position(){
		return position;
	}
	/*
	public void goNorth(){
		--position.i;
	}
	public void goSouth(){
		++position.i;
	}
	public void goEast(){
		--position.j;
	}
	public void goWest(){
		++position.j;
	}
	*/
}
