package othello;

import java.util.ArrayList;
import java.util.HashMap;

import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class Game {
	private Pane _gamePane;
	private ModSquare[][] _board;
	private HashMap<Integer, ArrayList<Integer>> _legalMoves;
	
	private Label _scoreW;
	private Label _scoreB;
	
	private Label _fin;
	private Label _winner;
	
	private Player _white;
	private Player _black;
	private Referee _referee;
	
	private int _move;
	private ArrayList<Integer> _keys;
	
	private int[][] _dummy;
	
	private boolean _validp1 = false;
	private boolean _validp2 = false;
	private boolean _over = false;
	
	public Game() {
		
		//Create the initial state of the game
		_gamePane = new Pane();
		_gamePane.setPrefSize(Constants.GAME_WIDTH, Constants.WINDOW_HEIGHT);
		_gamePane.setFocusTraversable(true);

		this.buildBoard();
		this.scoreBoard(_gamePane);
		_legalMoves = new HashMap<Integer, ArrayList<Integer>>(64);
		
		//In the initial state, both players are set to human
		_white = new HumanPlayer(this);
		_black = new HumanPlayer(this);
		_black.setColor(-1);
		
		//Instantiate the referee, if one the current player at start is computer, have the player start playing
		
		_referee = new Referee(_white, _black,this);
		if(_referee.getCurrent().isBot() == 1) {
			_referee.getCurrent().makeMove();
		} else { //If the current player is not a computer, graphically show the moves
			_referee.show();	
		}
	}
	
	
	//Reinstantiates the hashmap to clear it
	public void clearHash() {
		_legalMoves = new HashMap<Integer, ArrayList<Integer>>(64);
	}
	
	//Tells the referee to trigger the timeline handle to pause for 2 seconds.
	public void pause() {
		_referee.getAnim().play();
	}
	
	//Computer player equivalent of play and flip methods, used on the dummy board in order for the MiniMax algorithm to make moves on copy boards and evaluate them
	public void cpuPlay(int index,int[][] dummy,ArrayList<Integer> list, int color) {
		int[] coords = this.toCoords(index);
		int row = coords[0];
		int col = coords[1];
		dummy[row][col] = color;
		this.flip(list);
	}
	
	public void cpuFlip(ArrayList<Integer> list, int[][] dummy) {
		for (int i = 0; i < list.size(); i++) {
			int[] flipCoords = this.toCoords(list.get(i));
			if (dummy[flipCoords[0]][flipCoords[1]] == 1) {
				dummy[flipCoords[0]][flipCoords[1]] = -1;
			} else {
				dummy[flipCoords[0]][flipCoords[1]] = 1;
			}
		}
	}
	
	//This method creates labels for game over and winning player if neither player has a valid move, else switch turns
	public void finish() {
			if(_validp1 == false && _validp2 == false) {
				_over = true;
				this.overLabel(_gamePane);
				int white = this.count(1);
				int black = this.count(-1);
				this.winnerLabel(black, white);
			} else {
				this.turn();
			}
	}
	
	
	//Based on the amount of pieces for white and black, creates a label to notify players who won.
	public void winnerLabel(int b, int w) {
		if(w > b) { //If white wins
			_gamePane.getChildren().remove(_winner);
			_winner = new Label("WHITE WINS");
			_winner.setTextFill(Color.BLUE);
			_winner.setFont(Font.font("Courier New", FontWeight.BOLD, 40));
			_winner.setVisible(true);
			_winner.setLayoutX(150);
			_winner.setLayoutY(250);
			_gamePane.getChildren().add(_winner);
		} else if(b > w) { //If black wins
			_gamePane.getChildren().remove(_winner);
			_winner = new Label("BLACK WINS");
			_winner.setTextFill(Color.BLUE);
			_winner.setFont(Font.font("Courier New", FontWeight.BOLD, 40));
			_winner.setLayoutX(150);
			_winner.setLayoutY(250);
			_winner.setVisible(true);
			_gamePane.getChildren().add(_winner);
		} else if(b == w) { //If there is a tie
			_gamePane.getChildren().remove(_winner);
			_winner = new Label("TIE");
			_winner.setTextFill(Color.BLUE);
			_winner.setFont(Font.font("Courier New", FontWeight.BOLD, 40));
			_winner.setLayoutX(150);
			_winner.setLayoutY(250);
			_winner.setVisible(true);
			_gamePane.getChildren().add(_winner);
		}
	}
	
	
	//Accessor for the game over boolean
	public boolean gameOver() {
		return _over;
	}
	
	//This methods copies the main game board to a copy board, returning it at the end.
	public int[][] dummyBoard() {
		int[][] dummy = new int[10][10];
		for (int row = 0; row < 10; row++) {
			for (int col = 0; col < 10; col++) {
				if(_board[row][col] != null && _board[row][col].state() == 2) {
					dummy[row][col] = 2;
				} 
				if(_board[row][col] != null && _board[row][col].state() == 1) {
					dummy[row][col] = 1;
				}
				if(_board[row][col] != null && _board[row][col].state() == -1) {
					dummy[row][col] = -1;
				}
			}
		}
		return dummy;
	}
	
	
	//Accessor for the dummy board
	public int[][] getDummy() {
		return _dummy;
	}
	
	
	//Count the amount of pieces of a color: 1 for white, -1 for black pieces
	public int count(int color) {
		int count = 0;
		for (int row = 1; row < 9; row++) {
			for (int col = 1; col < 9; col++) {
				if (_board[row][col] != null && _board[row][col].state() == color) {
					count++;
				} 
			}
		}
		return count;
	}
	
	//Checks the hashmap for emptiness. If empty, no valid moves; else, the player has valid moves
	public boolean hasValid() {
		if(_legalMoves.isEmpty()) {
			return false;
		}else {
			return true;
		}
	}
	
	public void init(int p1, int p2) {
		
		//Clear every item in the board, create a new game board and score board
		this.clear(1);
		this.clear(-1);
		this.clear(2);
		this.clear(3);
		this.buildBoard();
		this.scoreBoard(_gamePane);
		
		//Set valid move and game over booleans to false
		_validp1 = false;
		_validp2 = false;
		_over = false;
		
		if(_winner != null) { //If we have a label to indicate the winner, set it to invisible
			_winner.setVisible(false);
		}
		_legalMoves = new HashMap<Integer, ArrayList<Integer>>(64); //Reinitialize the hashmap 
		if(p1 == 0) {
			_white = new HumanPlayer(this);
		} else {
			_white = new ComputerPlayer(this,p1);
		}
		if(p2 == 0) {
			_black = new HumanPlayer(this);
			_black.setColor(-1);
		} else {
			_black = new ComputerPlayer(this,p2);
			_black.setColor(-1);
		}
		_referee = new Referee(_white, _black,this);
		if(_referee.getCurrent().isBot() == 1) {
			_referee.getCurrent().makeMove();
		} else {
			_referee.show();
		}
	}

	//Return the current player
	public Player getCurrent() {
		return _referee.getCurrent();
	}

	//Clears the hashmap and tells the referee to change players
	public void turn() {
		this.clearHash();
		_referee.changePlayer();
	}
	
	
	//Creates a label to indicate game over
	public void overLabel(Pane pane) { 
		_fin = new Label("GAME OVER");
		_fin.setTextFill(Color.RED);
		_fin.setFont(Font.font("Courier New", FontWeight.BOLD, 50));
		_fin.setVisible(true);
		_fin.setLayoutX(125);
		_fin.setLayoutY(150);
		pane.getChildren().add(_fin);
	}
	
	//Instantiates the scoreboard for the game
	public void scoreBoard(Pane pane) {
		_scoreW = new Label("White pieces: " + 2);
		_scoreB = new Label("Black pieces: " + 2);
		_scoreW.setTextFill(Color.WHITE);
		_scoreB.setTextFill(Color.WHITE);
		
		_scoreW.setLayoutX(50);
		_scoreW.setLayoutY(25);
		
		_scoreB.setLayoutX(200);
		_scoreB.setLayoutY(25);
		
		pane.getChildren().addAll(_scoreW,_scoreB);
	}
	
	//Creates a score label based on the count of black and white pieces
	public void setScore(int w, int b) {
		_gamePane.getChildren().removeAll(_scoreW,_scoreB);
		_scoreW = new Label("White pieces: " + w);
		_scoreB = new Label("Black pieces: " + b);
		_scoreW.setTextFill(Color.WHITE);
		_scoreB.setTextFill(Color.WHITE);
		
		_scoreW.setLayoutX(50);
		_scoreW.setLayoutY(25);
		
		_scoreB.setLayoutX(200);
		_scoreB.setLayoutY(25);
		_gamePane.getChildren().addAll(_scoreW,_scoreB);
	}
	
	
	//Accessor for the game pane
	public Pane getRoot() {
		return _gamePane;
	}
	
	
	//Accessor for the game board
	public ModSquare[][] getBoard() {
		return _board;
	}

	
	//Accessor for the hashmap containing legal moves
	public HashMap<Integer, ArrayList<Integer>> validMoves() {
		return _legalMoves;
	}

	//This method creates slightly transparent red squares on board indices where a valid move can be made. Used to visualize moves for human players
	public void showMoves() {
		Object[] indices = _legalMoves.keySet().toArray(); //Get all indices for valid moves
		for (int i = 0; i < indices.length; i++) {
			int[] duo = this.toCoords((int) indices[i]); //Convert from index number to row/column coordinates
			
			
			//Create a modsquare and add it to the board
			ModSquare valid = new ModSquare(_gamePane,3);
			valid.posY(duo[0] * Constants.RECT_SIZE);
			valid.posX(duo[1] * Constants.RECT_SIZE);
			valid.setSandwich(_legalMoves.get(indices[i]));
			valid.setIndex((int) indices[i]);

			if (_board[duo[0]][duo[1]] == null) {
				_board[duo[0]][duo[1]] = valid;
			}
		}
	}

	
	/*
	 * This method either clears walls, pieces or valid squares depending on the integer provided:
	 * 2 for walls, 1 for white and -1 for black pieces, 3 for valid squares
	 * 
	 * The modsquares are removed from the game pane and deleted from the board
	 */
	public void clear(int state) {
		for (int row = 1; row < 9; row++) {
			for (int col = 1; col < 9; col++) {
				if (_board[row][col] != null && _board[row][col].state() == state) {
					_gamePane.getChildren().remove((_board[row][col]).getRoot());
					_board[row][col] = null;
				}
			}
		}
	}
	
	//Sets the index of the chosen move
	public void setMove(int index) {
		_move = index;
	}
	
	//Returns a list of keys for the hashmap
	public ArrayList<Integer> keys() {
		return _keys;
	}
	
	/*
	 * This method plays the move chosen by a player. The human or computer players determine the move, which contains an index value. This value is used to extract 
	 * the row and column index of the move so that it can be placed graphically on the board. The index is also the key in my hashmap, enabling access to the pieces that
	 * can be sandwiched by that move. First, we clear the squares marked as valid moves, then make place the piece. Finally, we flip the opponent's pieces, clear the hashmap 
	 * by reinitializing it and update the score.
	 */

	public void play(int index) {
		int[] coords = this.toCoords(index);
		int row = coords[0];
		int col = coords[1];
		this.clear(3);
		_board[row][col] = this.makePiece(_gamePane, this.getCurrent().getColor(), row, col);
		this.flip(_legalMoves.get(index));
		_legalMoves = new HashMap<Integer, ArrayList<Integer>>(64);
		this.setScore(this.count(_white.getColor()),this.count(_black.getColor())); //Update score by counting black and white pieces
	}

	
	//Given a list of sandwichable pieces, flip them to the opposite color.
	public void flip(ArrayList<Integer> flipList) {
		for (int i = 0; i < flipList.size(); i++) {
			int[] flipCoords = this.toCoords(flipList.get(i));
			if (_board[flipCoords[0]][flipCoords[1]].state() == 1) {
				_board[flipCoords[0]][flipCoords[1]].setBlack();
			} else {
				_board[flipCoords[0]][flipCoords[1]].setWhite();
			}
		}
	}
	
	/*
	 * This method makes use of findSandwich to first generate the list of all sandwichable pieces and does it for each empty space in the board.
	 * If the list has a size greater than 1, this means that it contains valid pieces and can then be mapped to the index of the move that can will placed at the empty spot.
	 * This method uses a hashmap that uses the valid move square's index as a key and stores a list sandwichable piece indices as its value. This allows either player to
	 * easily access a move. Essentially, the hashmap keys represent all spaces on the board where a valid move can be made. The key's value is all the opponent's pieces that
	 * can be sandwiched by that move.
	 */

	public void moveSet(int color) {
		if(!_legalMoves.isEmpty()) {
			this.clearHash();
		}
		_keys = new ArrayList<Integer>(); //List to store the keys in the hashmap, making it cleaner to access them for other classes
		for (int row = 1; row < 9; row++) {
			for (int col = 1; col < 9; col++) {
				if (_board[row][col] == null) {
					ArrayList<Integer> sandwichables = this.findSandwich(row, col, color);
					if (sandwichables.size() > 0) {
						_legalMoves.put(this.toIndex(row, col), sandwichables);
						_keys.add(this.toIndex(row, col));
					}
				}
			}
		}
		if(!_legalMoves.isEmpty()) {
			if(_referee.getCurrent().getColor() == 1) {
				_validp1 = true;
			} else if(_referee.getCurrent().getColor() == -1) {
				_validp2 = true;
			}
		} else {
			if(_referee.getCurrent().getColor() == 1) {
				_validp1 = false;
				this.finish();
			} else if(_referee.getCurrent().getColor() == -1) {
				_validp2 = false;
				this.finish();
			}
		}
	}
	
	/*
	 * This method calls find in all eight directions and combines their piece list into one array list. The piece lists contains a singular index number that represents
	 * the row and column index of that piece. The resultant legal pieces array becomes the collection of all valid sandwiches.
	 */

	public ArrayList<Integer> findSandwich(int row, int col, int color) {
		int[][] dirs = Constants.DIRECTIONS;
		ArrayList<Integer> legalPieces = new ArrayList<Integer>();
		if (_board[row][col] == null) { // row != 0 || row != 9 || col != 0 || col != 9
			for (int i = 0; i < dirs.length; i++) {
				int y = dirs[i][0];
				int x = dirs[i][1];
				legalPieces.addAll(this.find(row, col, y, x, color));
			}
		}
		return legalPieces;
	}

	
	/*
	 * This method traverses in a single direction, beginning at a desired row column index and player color (black or white)
	 * dirY and dirX come from the Directions array in constants. Each direction has a defined row(Y) and column(X) displacement.
	 */
	private ArrayList<Integer> find(int row, int col, int dirY, int dirX, int color) {
//		int i = 0;
//		boolean flag = true;
//		ArrayList<Integer> index_list = new ArrayList<Integer>();
		ArrayList<Integer> pieces = new ArrayList<Integer>();
		while (true) { //Loop indefinitely until we hit a case that either returns or breaks.
			
			//If the next piece in direction is null or a wall, reinitialize the arraylist for sandwichable pieces because direction does yield a valid sandwich, then break
			if (_board[row + dirY][col + dirX] == null || _board[row + dirY][col + dirX].state() == 2) { 
				pieces = new ArrayList<Integer>();
//				flag = false;
				break;
//				continue;
			} 
			//If piece in direction is the opponents color, update row and col to in that direction and add the piece to the list
			else if (_board[row + dirY][col + dirX].state() == -color) {
				row += dirY;
				col += dirX;
//				index_list.add(_board[row][col].getIndex());
//				i++;
				pieces.add(_board[row][col].getIndex());
			} else if (_board[row + dirY][col + dirX].state() == color) { //If we hit a piece of our own color, the sandwich is valid and we can break to return the list
				break;
			}
		}
//		if(flag == true) {
//			for(int j = 0;j<index_list.size();j++) {
//				pieces.add(index_list.get(j));
//			}
//		}
		return pieces;
	}

	// Builds the game board; places empty squares and walls
	private void buildBoard() {
		_board = new ModSquare[10][10];
		for (int row = 0; row < 10; row++) {
			for (int col = 0; col < 10; col++) {
				if (row == 0 || row == 9 || col == 0 || col == 9) {
					//Create a modsquare that will be configured to a wall
					ModSquare wall = new ModSquare(_gamePane,2);
					wall.posX(col * Constants.RECT_SIZE);
					wall.posY(row * Constants.RECT_SIZE);
					wall.setIndex(this.toIndex(row, col));
					_board[row][col] = wall;
					
				} else {
					//Anything other than a piece or a wall is an empty spot, so we do not need to add it to the board.
					Rectangle empty = new Rectangle(Constants.RECT_WIDTH, Constants.RECT_HEIGHT);
					_gamePane.getChildren().add(empty);
					empty.setX(col * Constants.RECT_SIZE);
					empty.setY(row * Constants.RECT_SIZE);
					empty.setStroke(Color.BLACK);
					empty.setFill(Color.GREEN);
				}
			}
		}
		
		//Inititalize the initial pieces
		_board[4][4] = this.makePiece(_gamePane, -1, 4, 4);
		_board[5][5] = this.makePiece(_gamePane, -1, 5, 5);
		_board[4][5] = this.makePiece(_gamePane, 1, 4, 5);
		_board[5][4] = this.makePiece(_gamePane, 1, 5, 4);
	}

	// Creates a piece based on a binary integer: white if 1, black if -1
	private ModSquare makePiece(Pane pane, int color, int row, int col) {
		ModSquare pizza = new ModSquare(pane,color); //Instantiate a new modsquare with the desired color
		pizza.posY(row * Constants.RECT_SIZE + 25); //Set the position
		pizza.posX(col * Constants.RECT_SIZE + 25);
		pizza.setIndex(this.toIndex(row, col)); //Concert its row and column coordinates into a single index number
		return pizza;
	}

	/*
	 * The two methods below help convert the row and column positions of a given point in the board to a single integer and vice versa.
	 * I am using a 10x10 board; if we number the upper left square to be 0, then the row is numbered from 0,...,9. When we go a row down, we have a range 
	 * of 10,....,19. Thus, the number representing where the square increases by 10 when we go one row below and by 1 when we go one column across. From here,
	 * we can multiply the row by 10 and add the column to get a single number. 
	 * 
	 * To convert back to row and column coordinates, we can use mod 10. The modulus 10 of the index results in the column number. If we subtract the column from the index
	 * number and divide the result by 10, we get the row number.
	 */ 
	
	//Turns the row and column numbers on the board to a single index
	public int toIndex(int row, int col) {
		return row * 10 + col;
	}

	/*
	 * This method makes use of mod 10 and arithmetics to convert back to coordinates.
	 * i.e: a piece at index 25 would be at row 2, column 5. 
	 */
	public int[] toCoords(int index) {
		int[] coordlist = new int[2];
		int col = index % 10; //Extract the column number
		int row = (index - col) / 10; //Extract the row number
		coordlist[0] = row;
		coordlist[1] = col;
		return coordlist;
	}

}
