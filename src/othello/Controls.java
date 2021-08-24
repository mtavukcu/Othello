package othello;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class Controls {
	private VBox _settings;
	private Game _game;
	private int _p1 = 0;
	private int _p2 = 0;

	public Controls(Game game) {
		_game = game;
		_settings = new VBox(); 
		
		Button quit = new Button("QUIT"); //Create a quit button
		quit.setFocusTraversable(false);
		quit.setOnAction(new quitHandler());
		
		Label points = new Label("Welcome");
		points.setTextFill(Color.BLACK);
		
		
		_settings.getChildren().add(points);
		this.gameSettings(_settings);
		_settings.getChildren().add(quit); 
	}
	
	/*
	 * Create 8 radio buttons, 4 for white player, 4 for black player. Create title
	 * for white and black. Create apply settings and reset button.
	 * 
	 */
	public void gameSettings(VBox box) {
		Button apply = new Button("Apply Settings");
		apply.setFocusTraversable(false);
		Button reset = new Button("Reset");
		reset.setFocusTraversable(false);
		reset.setOnAction(e -> {
			 _game.init(_p1,_p2);
		});

		RadioButton white1, white2, white3, white4; 
		white1 = new RadioButton("Human");
		white2 = new RadioButton("Computer, Intel 1");
		white3 = new RadioButton("Computer, Intel 2");
		white4 = new RadioButton("Computer, Intel 3");

		ToggleGroup whitePlayer = new ToggleGroup(); //Toggle group for white player buttons
		white1.setToggleGroup(whitePlayer);
		white2.setToggleGroup(whitePlayer);
		white3.setToggleGroup(whitePlayer);
		white4.setToggleGroup(whitePlayer);

		RadioButton black1, black2, black3, black4;
		black1 = new RadioButton("Human");
		black2 = new RadioButton("Computer, Intel 1");
		black3 = new RadioButton("Computer, Intel 2");
		black4 = new RadioButton("Computer, Intel 3");

		ToggleGroup blackPlayer = new ToggleGroup(); //Toggle group for black player buttons
		black1.setToggleGroup(blackPlayer);
		black2.setToggleGroup(blackPlayer);
		black3.setToggleGroup(blackPlayer);
		black4.setToggleGroup(blackPlayer);

		VBox left = new VBox(); //VBox to contain button for white
		left.setPrefSize(250, Constants.WINDOW_HEIGHT / 20);
		left.setStyle("-fx-background-color: white;");
		left.setSpacing(10);
		left.getChildren().addAll(white1, white2, white3, white4);

		VBox right = new VBox(); //VBox to contain button for black
		right.setPrefSize(250, Constants.WINDOW_HEIGHT / 20);
		right.setStyle("-fx-background-color: white;");
		right.setSpacing(10);
		right.getChildren().addAll(black1, black2, black3, black4);

		HBox settings = new HBox(); //HBox to contain all buttons
		settings.setPrefSize(Constants.CONTROL_WIDTH, Constants.WINDOW_HEIGHT / 20);
		settings.setStyle("-fx-background-color: white;");
		settings.setSpacing(10);

		settings.getChildren().addAll(left, right);
		box.getChildren().addAll(settings, apply, reset);

		apply.setOnAction(e -> {
			if (white1.isSelected()) {
				_p1 = 0;
			} else if(white2.isSelected()){
				_p1 = 1;
			} else if(white3.isSelected()) {
				_p1 = 2;
			} else if(white4.isSelected()) {
				_p1 = 3;
			}
			
			if(black1.isSelected()) {
				_p2 = 0;
			}else if(black2.isSelected()){
				_p2 = 1;
			} else if(black3.isSelected()) {
				_p2 = 2;
			} else if(black4.isSelected()) {
				_p2 = 3;
			}
			_game.init(_p1,_p2);
		});
	}

	//VBox accessor for game settings
	public VBox getBox() {
		return _settings;
	}
	
	//Exit handler
	private class quitHandler implements EventHandler<ActionEvent> {

		@Override
		public void handle(ActionEvent e) {
			Platform.exit();
		}
	}
}
