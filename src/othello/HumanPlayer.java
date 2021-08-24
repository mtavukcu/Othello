package othello;

import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;

public class HumanPlayer implements Player {
	private Game _g;
	private int _color;
	private int _moveIndex;
	private int _cpu = 0; //Set to 0 for human players

	public HumanPlayer(Game game) { 
		// 1 represents white, like chess, the default player (a.k.a player to make first move) is set to white
		// -1 represents black
		_color = 1;
		_g = game; 
	}
	 
	@Override
	public int isBot() { //Accessor for the human/bot status
		return _cpu;
	}
	
	@Override
	public boolean hasValid() { //Method to return true if player has valid moves, checks if the hashmap is empty for a given player
		return _g.hasValid();
	}
	
	public void select(int i) { //Set the index value for a chosen move
		 _moveIndex = i;
	}
	
	public void setListener(Node node) { //Listener for mouse clicks, added to all squares marked as valid moves.
		node.addEventHandler(MouseEvent.MOUSE_CLICKED, new MouseHandler());
	}
	
	
	//Accessor and mutator for the player's color
	@Override
	public int getColor() { 
		return _color;
	}
	
	@Override
	public int setColor(int color) {
		return _color = color;
	}
	
	//Communicates with the game class to show all valid moves to the player
	@Override
	public void seePlay() {
		_g.moveSet(_color); //Generate a moveset
		if(this.hasValid()) { //If we have valid moves, show them.  
			_g.showMoves();
			for (int row = 1; row < 9; row++) {
				for (int col = 1; col < 9; col++) {
					if (_g.getBoard()[row][col] != null && _g.getBoard()[row][col].state() == 3) { //Add a mouselistener to all valid squares
						//NOTE: The mouselistener will be garbage collected once we make a move and the game board clears the valid squares
						this.setListener(_g.getBoard()[row][col].getRoot());
					}
				}
			}
		}
	}

	@Override
	public void makeMove() { //Makes the desired move on the board through the game class
			_g.play(_moveIndex);
			_g.turn(); //Switch turns
	}
	
	private class MouseHandler implements EventHandler<MouseEvent> { //Mousehandler for chosing a move on the board
		@Override
		public void handle(MouseEvent e) {
			if(HumanPlayer.this.hasValid()) { 
				int index = _g.toIndex(((int)e.getY()/Constants.RECT_SIZE), (int)(e.getX()/Constants.RECT_SIZE));
				HumanPlayer.this.select(index);
				HumanPlayer.this.makeMove();
			}
			e.consume();
		}

	}
}