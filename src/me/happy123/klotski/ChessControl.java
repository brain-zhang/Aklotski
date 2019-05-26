package me.happy123.klotski;

import me.happy123.klotski.Position;;

public class ChessControl {

	private ChessBoard board;
	
	public ChessControl(ChessBoard board) {
		this.board = board;
	}
	
	public boolean addPiece(ChessPiece piece) {
		return this.board.addPiece(piece);
	}
	
	public boolean removePiece(String id) {
		return this.board.removePiece(id);
	}
	
	public Position movePiece(String id) {
		return this.board.movePiece(id);
	}
	
	public void draw() {
		this.board.draw();
	}
	
	public ChessBoard getBoard() {
		return this.board;
	}
	
	public int[][] getBoardLayoutArray() {
		return this.board.getLayoutArray();
	}
}
