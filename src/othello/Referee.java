package othello;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.util.Duration;

public class Referee {
	private Game _game; //Game variable to use throughout the referee
	private int _turn = 0;
	private Player _current;
	private Player _one; //Player 1 and 2 can be used throughout the class
	private Player _two;
	private Timeline _animator;
	
	public Referee(Player white, Player black, Game game) {
		_game = game;
		_one = white; 
		_two = black;
		_current = _one; //Player one goes first, set to white
		if(_one.isBot() == 1 || _two.isBot() == 1) { //If either player is a computer, start the TimeLine
			this.gameTime();
		} 
	}
	//Accessor for the the Timeline
	public Timeline getAnim() {  
		return _animator; 
	}
	
	//Allows the player to see their moves
	public void show() {
		_current.seePlay();
	}
	
	
	//Returns the current player
	public Player getCurrent() {
		return _current;
	}
	
	//Turn changing logic
	public void changePlayer() { 
		if(!_game.gameOver()) { //Change turns only if game isn't over
			if(_turn == 0) { 
				_turn = 1;
				_current = _two;
				if(_current.isBot() == 1) { //Check if the new current player is a computer
					_current.makeMove();
				} else {
					_current.seePlay();
				}	
			} else if(_turn == 1) {
				_turn = 0;
				_current = _one;
				if(_current.isBot() == 1) {
					_current.makeMove();
				} else {
					_current.seePlay();
				}
			}
		}
	}
	
	
	//TimeLine to see the computer players moves updated on the board, duration is set to 2 seconds
	public void gameTime() {
	KeyFrame tetrisFrame = new KeyFrame(Duration.seconds(Constants.DURATION), new timeHandler());
	_animator = new Timeline(tetrisFrame);
	_animator.setCycleCount(Animation.INDEFINITE);
}
	
	//TimeHandler that calls stop, pausing for 2 seconds. Then, we switch turns. This method is used for showing the computer players moves on the board

	private class timeHandler implements EventHandler<ActionEvent> {

	@Override
	public void handle(ActionEvent time) {
		_animator.stop();
		Referee.this.changePlayer();
		time.consume();
	}
  }
}