package othello;

public interface Player {
	public void makeMove();
	public void seePlay();
	public int isBot();
	public int getColor();
	public int setColor(int color);
	public boolean hasValid();
}