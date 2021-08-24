package othello;

public class Constants {
	public final static int RECT_WIDTH = 50;
	public final static int RECT_HEIGHT = 50;
	public final static int RECT_SIZE = 50;
	public final static int PIECE_SIZE = 20;
	public final static int WINDOW_WIDTH = 800; 
	public final static int WINDOW_HEIGHT = 500;
	public final static int GAME_WIDTH = 500; 
	public final static int CONTROL_WIDTH = 300;
	public final static double DURATION = 5; 
	
	//An array containing row and column displacements that represent the eight directions around a board square
	public final static int[][] DIRECTIONS = {{1,0},{-1,0},{0,1},{0,-1},{1,1},{-1,1},{1,-1},{-1,-1}}; 
	
	//Board weights as suggested by the othello handouts
	public final static int[][] WEIGHTS = {{0,0,0,0,0,0,0,0,0,0},
										 {0,200,-70,30,25,25,30,-70,200,0},
										 {0,-70,-100,-10,-10,-10,-10,-100,-70,0},
										 {0,30,-10,2,2,2,2,-10,30,0},
										 {0,25,-10,2,2,2,2,-10,25,0},
										 {0,25,-10,2,2,2,2,-10,25,0},
										 {0,30,-10,2,2,2,2,-10,30,0},
										 {0,-70,-100,-10,-10,-10,-10,-100,-70,0},
										 {0,200,-70,30,25,25,30,-70,200,0},
										 {0,0,0,0,0,0,0,0,0,0}};

} 