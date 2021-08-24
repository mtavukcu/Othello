package othello;

import javafx.scene.shape.Rectangle;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class Wall implements Containable {
	private Rectangle _square;
	private int _state = 2;
	private int _index = 0;

	public Wall(Pane pane) {
		_square = new Rectangle(Constants.RECT_WIDTH, Constants.RECT_HEIGHT);
		_square.setStroke(Color.BLACK);
		pane.getChildren().add(_square);
	}

	@Override
	public Node getRoot() {
		return _square;
	}
	
	public Rectangle getRect() {
		return _square;
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
}
