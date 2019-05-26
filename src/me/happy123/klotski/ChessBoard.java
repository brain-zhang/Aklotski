package me.happy123.klotski;

import java.util.*;

import me.happy123.klotski.exception.BorderException;

/*
 * chess board, you should create new board for each game
 */
public class ChessBoard extends ChessBaseItem {

	//contains all pieces on the board
	ArrayList<ChessPiece> onlinePieces = new ArrayList<ChessPiece>();
	
	//description of the war situation by chess pieces, `1` means there has a chess piece on the position, `0` mean none 
	private int boardArray[][] = new int[this.height][this.width];
	
	/*
	 * construct function,just need width and height, nonsupport minus
	 */
	public ChessBoard(int width, int height) {
		super(width, height);
	}
	
	/*
	 * add a chess piece, return false if you set the piece to invalid position
	 */
	public boolean  addPiece(ChessPiece piece) {
		onlinePieces.add(piece);
		if(!setLayout()) {
			removePiece(piece.getId());
			return false; 
		}
		return true;
	}
	
	/*
	 * remove piece, return false if you remove a non exist piece
	 */
	public boolean removePiece(String id) {
		for(int i=0; i<onlinePieces.size(); i++) {
			if(onlinePieces.get(i).getId() == id) {
				onlinePieces.remove(i);
				setLayout();
				return true;
			}
		}
		return false;
	}
	
	/*
	 * move piece to another postion
	 */
	public Position movePiece(String id, Position pos) {
		ChessPiece piece = getPiece(id);
		if(!piece.move(pos)) {
			piece.undo();
			return null;
		}
		
		if(!setLayout()) {
			piece.undo();
			return null;
		}
		return pos;
	}
	
	/*
	 * auto find the next valid position for the piece and move it
	 * return null if don't find valid position
	 * return the dst position if success
	 */
	public Position movePiece(String id) {
		ArrayList<Position> useablePositions = new ArrayList<Position>();
		ArrayList<Position> allPositions = new ArrayList<Position>();
		
		ChessPiece piece = getPiece(id);
		if(piece==null) {
			return null;
		}

		Position curr_pos = piece.getPosition();
		allPositions.add(new Position(curr_pos.getPosx(), curr_pos.getPosy()-1));
		allPositions.add(new Position(curr_pos.getPosx(), curr_pos.getPosy()+1));
		allPositions.add(new Position(curr_pos.getPosx()-1, curr_pos.getPosy()));
		allPositions.add(new Position(curr_pos.getPosx()+1, curr_pos.getPosy()));
		
		if(piece.getPrevPosition()!=null) {

			for(int i=0; i<allPositions.size(); i++) {
				if(!allPositions.get(i).equals(piece.getPrevPosition())) {
					useablePositions.add(allPositions.get(i));
				}
			}
			useablePositions.add(piece.getPrevPosition());
		} else {
			useablePositions = allPositions;
		}
		
		for(int i=0; i<useablePositions.size(); i++) {
			if(movePiece(id, useablePositions.get(i)) != null) {
				return useablePositions.get(i);
			}
		}
		
		return null;
	}
	
	/*
	 * get all pieces online
	 */
	public ArrayList<ChessPiece> getAllPieces() {
		return this.onlinePieces;
	}
	
	/*
	 * print ascii img of the chess board, just for test
	 */
	public void draw() {
		System.out.printf("draw board:%d X %d\n", this.width, this.height);
		
		for(int i=0; i<boardArray.length; i++) {
			for(int j=0; j<boardArray[0].length; j++) {
				System.out.printf("%d\t", boardArray[i][j]);
			}
			System.out.print("\n");
		}
		
		for(int i=0; i<onlinePieces.size(); i++) {
			ChessPiece piece = onlinePieces.get(i);
			System.out.println(piece.toString());
		}
	}
	
	/*
	 * relayout if the someone move the piece
	 * return false if don't find valid position
	 */
	@Override
	public boolean setLayout() {
		//check if all pieces layout valid
		
		int[][] prevBoardArray = Utils.deepCopyInt(this.boardArray);
		this.boardArray = new int[this.height][this.width];
		
		for(int i=0; i<onlinePieces.size(); i++) {
			ChessPiece piece = onlinePieces.get(i);
			
			if((piece.getPosition().getPosx() + piece.getWidth()) > width || 
			   (piece.getPosition().getPosy()+ piece.getHeight()) > height ||
			   piece.getPosition().getPosx() < 0 ||
			   piece.getPosition().getPosy() < 0) {
				return false;
			}
			
			for(int posy=piece.getPosition().getPosy(); posy<piece.getPosition().getPosy()+piece.getHeight(); posy++) {
				for(int posx=piece.getPosition().getPosx(); posx<piece.getPosition().getPosx()+piece.getWidth(); posx++) {
					if(this.boardArray[posy][posx] != 0) {
						boardArray = prevBoardArray;
						return false;
					}
					this.boardArray[posy][posx] = 1;
				}
			}

		}
		return true;
	}
	
	/*
	 * get description of the war situation by chess pieces, just for test
	 */
	public int[][] getLayoutArray() {
		return Utils.deepCopyInt(this.boardArray);
	}

	/*
	 * get piece object by piece id
	 * return null if not find
	 */
	private ChessPiece getPiece(String id) {
		for(int i=0; i<onlinePieces.size(); i++) {
			if(onlinePieces.get(i).getId() == id) {
				return onlinePieces.get(i);
			}
		}
		return null;
	}
}
