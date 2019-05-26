package me.happy123.klotski;

import org.junit.*;
import static org.junit.Assert.*;

public class TestChessPiece {
	
	private ChessBoard board;
	private ChessControl chessControl;

	@Before
	public void setUp() {
		this.board = new ChessBoard(4 ,5);
		this.chessControl = new ChessControl(board);
	}

	@Test public void Test1AddChessPiece() {
		this.chessControl.addPiece(new ChessPiece(1,1,2,2, "zhang"));
		assertFalse(this.chessControl.addPiece(new ChessPiece(1,1,2,2, "guan")));
		assertFalse(this.chessControl.addPiece(new ChessPiece(1,1,1,1, "guan")));
		assertFalse(this.chessControl.addPiece(new ChessPiece(2,2,1,1, "guan")));
		
		int[][] expectLayoutArray = new int[][] {
				{0,0,0,0},
				{0,1,1,0},
				{0,1,1,0},
				{0,0,0,0},
				{0,0,0,0},
		};
		
		assertTrue(java.util.Arrays.deepEquals(expectLayoutArray, this.chessControl.getBoardLayoutArray()));
		this.chessControl.draw();
		
		assertTrue(this.chessControl.addPiece(new ChessPiece(3,3,1,1, "guan")));
		expectLayoutArray = new int[][] {
				{0,0,0,0},
				{0,1,1,0},
				{0,1,1,0},
				{0,0,0,1},
				{0,0,0,0},
		};
		assertTrue(java.util.Arrays.deepEquals(expectLayoutArray, this.chessControl.getBoardLayoutArray()));
		this.chessControl.draw();
	}
	
	@Test public void Test2RemoveChessPiece() {
		this.chessControl.addPiece(new ChessPiece(1,1,2,2, "zhang"));
		this.chessControl.removePiece("zhang");
		
		int[][] expectLayoutArray = new int[][] {
				{0,0,0,0},
				{0,0,0,0},
				{0,0,0,0},
				{0,0,0,0},
				{0,0,0,0},
		};
		assertTrue(java.util.Arrays.deepEquals(expectLayoutArray, this.chessControl.getBoardLayoutArray()));
		this.chessControl.draw();
	}
	
	@Test public void Test3MoveChessPiece() {
		this.chessControl.addPiece(new ChessPiece(1,1,2,2, "zhang"));
		this.chessControl.addPiece(new ChessPiece(0,0,1,1, "guan"));

		int[][] expectLayoutArray = new int[][] {
				{1,0,0,0},
				{0,1,1,0},
				{0,1,1,0},
				{0,0,0,0},
				{0,0,0,0},
		};
		assertTrue(java.util.Arrays.deepEquals(expectLayoutArray, this.chessControl.getBoardLayoutArray()));
		this.chessControl.draw();
		
		this.chessControl.movePiece("zhang");
		expectLayoutArray = new int[][] {
				{1,1,1,0},
				{0,1,1,0},
				{0,0,0,0},
				{0,0,0,0},
				{0,0,0,0},
		};
		assertTrue(java.util.Arrays.deepEquals(expectLayoutArray, this.chessControl.getBoardLayoutArray()));
		this.chessControl.draw();
		
		this.chessControl.movePiece("zhang");
		expectLayoutArray = new int[][] {
				{1,0,1,1},
				{0,0,1,1},
				{0,0,0,0},
				{0,0,0,0},
				{0,0,0,0},
		};
		assertTrue(java.util.Arrays.deepEquals(expectLayoutArray, this.chessControl.getBoardLayoutArray()));
		this.chessControl.draw();
		
		this.chessControl.movePiece("zhang");
		expectLayoutArray = new int[][] {
				{1,0,0,0},
				{0,0,1,1},
				{0,0,1,1},
				{0,0,0,0},
				{0,0,0,0},
		};
		assertTrue(java.util.Arrays.deepEquals(expectLayoutArray, this.chessControl.getBoardLayoutArray()));
		this.chessControl.draw();
	}
}
