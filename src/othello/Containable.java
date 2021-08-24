package othello;

import javafx.scene.Node;

public interface Containable {
	public void posX(double x);
	public void posY(double y);
	public int getX();
	public int getY();
	public int state();
	public int getIndex();
	public void setIndex(int num);
	public Node getRoot();
}