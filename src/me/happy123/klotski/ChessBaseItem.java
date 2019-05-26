package me.happy123.klotski;

/*
 * chess piece and board base class
 */
public abstract class ChessBaseItem {
	protected int width;
	protected int height;
	
	public ChessBaseItem(int width, int height) {
		this.width = width;
		this.height = height;
	}
	
	public int getWidth() {
		return this.width;
	}
	
	public int getHeight() {
		return this.height;
	}
	
	public abstract boolean setLayout();
}
