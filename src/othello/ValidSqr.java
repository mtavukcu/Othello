package othello;

import javafx.scene.shape.Rectangle;

import java.util.ArrayList;

import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class ValidSqr implements Containable {
	private Rectangle _square;
	private int _state = 3;
	private int _index = 0;
	private ArrayList<Integer> _list;
//	private Game _game;

	public ValidSqr(Pane pane) {
		_square = new Rectangle(Constants.RECT_WIDTH, Constants.RECT_HEIGHT);
		_square.setFill(Color.RED); 
		_square.setStroke(Color.RED);
		_square.setOpacity(0.6); 
		pane.getChildren().add(_square);
//		_square.addEventHandler(MouseEvent.MOUSE_CLICKED, new MouseHandler());
	}

	@Override
	public Node getRoot() { 
		return _square; 
	}
	
	public Rectangle getRect() {
		return _square;
	}

	public void notifyMove(Game game) {
		game.setMove(this.getIndex());
		// System.out.println("Told game the move!");
	}

	@Override
	public int state() {
		return _state;
	}

	@Override
	public int getIndex() {
		return _index;
	}

	@Override
	public void setIndex(int num) {
		_index = num;
	}

	@Override
	public int getX() {
		return (int) _square.getX();
	}

	@Override
	public int getY() {
		return (int) _square.getY();
	}

	@Override
	public void posX(double x) {
		_square.setX(x);
	}

	@Override
	public void posY(double y) {
		_square.setY(y);
	}

	public void setSandwich(ArrayList<Integer> list) {
		_list = list;
	}

	public ArrayList<Integer> getSandwich() {
		return _list;
	}

}
