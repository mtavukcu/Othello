package othello;

import javafx.scene.shape.Ellipse;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class Piece implements Containable {
	private Ellipse _piece;
	private int _state = 0;
	private int _index = 0;

	public Piece(Pane pane) {
		_piece = new Ellipse(Constants.PIECE_SIZE, Constants.PIECE_SIZE);
		_piece.setFill(Color.WHITE);
		_piece.setStroke(Color.WHITE); 
		pane.getChildren().add(_piece);
	}
	
	@Override
	public Node getRoot() {
		return _piece;
	}
	
//	public Ellipse getOval() {
//		return _piece;
//	}

	/*
	 * Setter and getters for the state of a piece: black or white. It is noted with
	 * an integer: 0 for white and 1 for black.
	 */

	public void setBlack() {
		if (_state == 0) {
			_state = 1;
			_piece.setFill(Color.BLACK);
			_piece.setStroke(Color.BLACK);
		}
	}

	public void setWhite() {
		if (_state == 1) {
			_state = 0;
			_piece.setFill(Color.WHITE);
			_piece.setStroke(Color.WHITE);
		}
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

	// Setters and getters for the location of the pieces
	@Override
	public int getX() {
		return (int) _piece.getCenterX();
	}

	@Override
	public int getY() {
		return (int) _piece.getCenterY();
	}

	@Override
	public void posX(double x) {
		_piece.setCenterX(x);
	}

	@Override
	public void posY(double y) {
		_piece.setCenterY(y);
	}

}