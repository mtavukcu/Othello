package othello;

import java.util.ArrayList;

public class ComputerPlayer implements Player {
	private Game _game;
	private int _color;
	private int _intel;
	private int _cpu = 1;
	
	public ComputerPlayer(Game game, int intel) {
		_game = game;
		_color = 1;
		_intel = intel;		
	}
	
	@Override
	public int isBot() {
		return _cpu;
	}
	
	public int evaluateBoard(int currPlayer, int[][] board) {
		int curr = 0;
		int opp = 0;
		for(int row = 1;row<9;row++) {
			for(int col = 1;col<9;col++) {
				if(board[row][col] == currPlayer) {
					curr += Constants.WEIGHTS[row][col];
				} else if(board[row][col] == -currPlayer){
					opp += Constants.WEIGHTS[row][col];
				}
			}
		}
		return curr - opp;
	}
	
	public Move getBestMove(int[][] board, int intelligence, int currPlayer) {
		if(_game.gameOver()) { //If game is over, we check who wins
			int curr = _game.count(currPlayer);
			int opp = _game.count(-currPlayer); 
			if(curr > opp) { //If current player wins, return high value move
				Move win = new Move(0,null,(int)1e99);
				return win;
			} else if(curr < opp) { //If opponent wins, return low value move
				Move loss = new Move(0,null,(int)-1e99);
				return loss;
			} else if(curr == opp) { //If game is a tie, return neutral move
				Move neutral = new Move(0,null,0);
				return neutral;
			}
		}
		_game.moveSet(currPlayer); //Generate all possible moves for this player
		if(this.hasValid() == false) { //Case for no valid moves
			if(intelligence == 1) {
				Move m = new Move(0,null,-1000); //Return low value move
				return m;
			} else {
				Move m = getBestMove(board, intelligence - 1, -currPlayer); //Return move from opponent's perspective, its value negated
				m.setValue(-(m.getValue()));
				return m;
			}
		} else { //If current player has moves
			ArrayList<Integer> list = _game.keys(); //A list of keys that represent the index number of valid moves for this player
			
			//Keep track of the best value and corresponding best move
			int best = (int)-1e99;
			Move best_move = null;
			
			for(int i = 0;i<list.size();i++) {
				int curr = 0; //Current value for the move
				
				Move curr_move = null; //Current move
				
				int[][] dummy = _game.dummyBoard(); //Create a dummy board
				
				_game.cpuPlay(list.get(i), dummy, _game.validMoves().get(list.get(i)), currPlayer); //Make a move on the dummy board
				
				if(intelligence == 1) { //If intelligence is 1, the value is found by evaluating board weights
					curr = evaluateBoard(currPlayer,dummy);
					curr_move = new Move(list.get(i),_game.validMoves().get(list.get(i)),curr);
				} else {
					curr_move = getBestMove(dummy,intelligence-1,-currPlayer); //If intelligence is higher than 1, recurse on the enemy player
					curr_move.setValue(-(curr_move.getValue()));
					curr = curr_move.getValue(); //Negate the opponents move
				}
				if(curr > best) { //Check if we have the best move
					best = curr;
					best_move = curr_move;
				}
			}
			return best_move; //Return the best move
		}
	}
	
	//Makes the move based on what is chosen in getBestMove
	@Override
	public void makeMove() {
		if(!_game.gameOver()) {
			Move play = this.getBestMove(null, _intel, _color);
			if(play.toFlip() == null) { //If the move is invalid, clear the hashmap (moves has no flippable pieces)
				_game.clearHash(); 
			} else { //If move is valid, play
				_game.play(play.getIndex()); 
			}
			_game.pause(); //Pause stops the timeline for 2 seconds, and then referee calls changePlayer to switch turns
		}
	}
	
	//Generate a moveset, since this not the human player, we do not need to graphically show moves
	@Override
	public void seePlay() {
		_game.moveSet(_color);
	}
	
	//Accessor and mutator for the computer's color
	@Override
	public int getColor() {
		return _color;
	}
	
	@Override
	public int setColor(int color) {
		return _color = color;
	}
	
	//Check if this player has valid moves
	@Override
	public boolean hasValid() {
		return !_game.validMoves().isEmpty();
	}
}