package othello;

import javafx.scene.layout.BorderPane;

public class PaneOrganizer {
	private BorderPane _main;
	private Game _game;
	private Controls _menu;

	public PaneOrganizer() {
		_main = new BorderPane();
		_game = new Game(); 
		_menu = new Controls(_game);
		_main.setPrefSize(Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT);
		_main.setLeft(_game.getRoot());
		_main.setRight(_menu.getBox());
	}

	public BorderPane getRoot() {
		return _main;
	}
}