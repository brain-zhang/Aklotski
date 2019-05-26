package me.happy123.klotski;

import java.util.*;
import me.happy123.klotski.exception.BorderException;

/*
 * description of chess piece
 */
public class ChessPiece extends ChessBaseItem {

	private Position curr_pos;
	private String id;
	private Stack<Position> stepStack;
	
	/*
	 * construct piece, need position(x, y), size(width,height) and id
	 * Notify: i don't check if id is unique, please assure it by yourself
	 */
	public ChessPiece(int posx, int posy, int width, int height, String id) {
		super(width, height);
		this.curr_pos = new Position(posx, posy);
		this.id = id;
		this.stepStack = new Stack<Position>();
	}
	
	/*
	 * change the piece position when someone move it
	 * return false if the position is invalid
	 */
	public boolean move(Position new_pos) {
		stepStack.push(this.curr_pos);
		this.curr_pos = new_pos;
		
		if(!setLayout()) {
			return false;
		}
		
		return true;
	}
	
	/*
	 * rollback if someone set an invalid move action
	 */
	public Position undo() {
		this.curr_pos = stepStack.pop();
		return this.curr_pos;
	}
	
	/*
	 * get the position of the piece
	 */
	public Position getPosition() {
		return this.curr_pos;
	}
	
	/*
	 * get last position of the piece, for rollback
	 */
	public Position getPrevPosition() {
		if(!stepStack.empty()) {
			return stepStack.lastElement();
		}
		return null;
	}
	
	/*
	 * get id
	 */
	public String getId() {
		return this.id;
	}
	
	/*
	 * print ascii img, for test
	 */
	public void draw() {
		System.out.println(toString());
	}
	
	/*
	 * check if the position is valid
	 */
	@Override
	public boolean setLayout() {
		if(this.curr_pos.getPosx() >=0 && this.curr_pos.getPosy() >= 0) {
			return true;
		} else {
			return false;
		}
	}
	
	/*
	 * just descript the piece, for test
	 */
	public String toString() {
		return String.format("Piece id:%s, posx:%d, posy:%d, width:%d, height:%d", id, curr_pos.getPosx(), curr_pos.getPosy(), width, height);
	}
}
