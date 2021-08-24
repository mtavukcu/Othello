package othello;

import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;

import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class ModSquare {
	private Rectangle _square;
	private Ellipse _circle;
	private ArrayList<Integer> _list;
	private int _state = 0;
	private int _index = 0;
 	
	public ModSquare(Pane pane,int state) {
		_list = new ArrayList<Integer>();
		_state = state; //The state logically represents what the modsquare is: 2 for wall, 3 for valid square, 1 for white piece and -1 or black piece
		this.setup(pane); //Setup the modsquare depending on what state was chosen

	} 
	
	//If the modsquare is setup to be a wall or valid square, return the rectangle node; else, return the circle node
	public Node getRoot() {
		if(_state == 2 || _state == 3) { 
			return _square;
		} else {
			return _circle;
		}
	}
	
	
	//Changes the state of a modsquare from a white piece to a black piece
	public void setBlack() {
		if (_state == 1) {
			_state = -1;
			_circle.setFill(Color.BLACK);
			_circle.setStroke(Color.BLACK);
		}
	}

	//Changes the state of a modsquare from a black piece to a white piece
	public void setWhite() {
		if (_state == -1) {
			_state = 1;
			_circle.setFill(Color.WHITE);
			_circle.setStroke(Color.WHITE);
		}
	}
	
	//Setup either a black or white circle to model a piece, or a black square for a wall 
	public void setup(Pane pane) {
		if(_state == 1) {
			_circle = new Ellipse(Constants.PIECE_SIZE, Constants.PIECE_SIZE);
			_circle.setFill(Color.WHITE);
			_circle.setStroke(Color.WHITE);
			pane.getChildren().add(_circle);
		} else if(_state == -1) {
			_circle = new Ellipse(Constants.PIECE_SIZE, Constants.PIECE_SIZE);
			_circle.setFill(Color.BLACK);
			_circle.setStroke(Color.BLACK);
			pane.getChildren().add(_circle);
		} else if(_state == 2) {
			_square = new Rectangle(Constants.RECT_WIDTH, Constants.RECT_HEIGHT);
			_square.setStroke(Color.BLACK);
			pane.getChildren().add(_square);
		} else if(_state == 3) {
			_square = new Rectangle(Constants.RECT_WIDTH, Constants.RECT_HEIGHT);
			_square.setFill(Color.RED);
			_square.setStroke(Color.RED);
			_square.setOpacity(0.6);
			pane.getChildren().add(_square);
		}
	}

	//Accessor for the modsquare state
	public int state() {
		return _state;
	}

	//Accessor and mutator for the modsquare's index value
	public int getIndex() {
		return _index;
	}

	public void setIndex(int num) {
		_index = num;
	}
	
	
	//Accessor and mutator for the squares's list of flippable pieces, if it has any
	public ArrayList<Integer> getSandwich() {
		if(_state == 3) {
			return _list;
		}
		return null;
	}
	
	public void setSandwich(ArrayList<Integer> list) {
		if(_state == 3) {
			_list = list;
		} 
	}
	
	// Setters and getters for X and Y coordinates
	public void posX(double x) {
		if(_state == 2 || _state == 3) {
			_square.setX(x);
		} else {
			_circle.setCenterX(x);
		}
	}

	public void posY(double y) {
		if(_state == 2 || _state == 3) {
			_square.setY(y);
		} else {
			_circle.setCenterY(y);	
		}
	}

	public double getX() {
		if(_state == 2 || _state == 3) {
			return _square.getX();
		} else {
			return (int) _circle.getCenterX();
		}
	}

	public double getY() {
		if(_state == 2 || _state == 3) {
			return _square.getY();
		} else {
			return (int) _circle.getCenterY();
		}
	}
}