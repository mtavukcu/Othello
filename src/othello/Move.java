package othello;

import java.util.ArrayList;

public class Move {
	private int _coordIndex;
	private int _score;
	private int _value;
	private ArrayList<Integer> _sandwichables;
	
	public Move(int index, ArrayList<Integer> list,int value) {
		_coordIndex = index; //Set the move's index
		_sandwichables = list; //Set the move's list of sandwichable pieces
		if(list != null) { //Set the score for the move
	 		_score = _sandwichables.size() + 1;   
		}
		_value = value; //Set the move's value
	} 
	
	//Accessor and mutator for the move's value
	
	public int getValue() {
		return _value;
	}
	
	public void setValue(int value) {
		_value = value;
	}
	
	
	//Accessor for the move's index
	public int getIndex() {
		return _coordIndex;
	}
	
	//Mutator for the move's score
	public int getScore() {
		return _score;
	}
	
	//Accessor for the move's list
	public ArrayList<Integer> toFlip() {
		return _sandwichables;
	}
}