package me.happy123.aklotski;

import java.util.ArrayList;
import java.util.Stack;

import android.R.string;

import me.happy123.klotski.ChessPiece;

public class WarRecord {

	private int id, level, status;
	private String name, layout, walkthrough;
	
	public WarRecord(int id, int level, int status, String name, String layout, String walkthrough) {
		// TODO Auto-generated constructor stub
		this.id = id;
		this.level = level;
		this.status = status;
		this.name = name;
		this.layout = layout;
		this.walkthrough = walkthrough;
	}
	
	public int getId() {
		return this.id;
	}
	
	public int getLevel() {
		return this.level;
	}
	
	/*
	 * -1 means lock
	 * 0 means unlock
	 * 1 means OK
	 * 2 means working
	 */
	public int getStatus() {
		return this.status;
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getLayout() {
		return this.layout;
	}
	
	public String getWalkthrough() {
		return this.walkthrough;
	}
	
	/*
	 * conver just like "2, 0, 0S4, 1, 0S2, 3, 0S2, 0, 2S3, 1, 2S2, 3, 2S1, 1, 3S1, 2, 3S1, 0, 4S1, 3, 4" to chesslayout
	 */
	public static ArrayList<ChessPiece> convertToChessLayout(String layout) {
		Stack<String> vImages = new Stack<String>();
		vImages.push("guan_v");
		vImages.push("zhang_v");
		vImages.push("zhao_v");
		vImages.push("ma_v");
		vImages.push("huang_v");
		
		Stack<String> hImages = new Stack<String>();
		hImages.push("huang_h");
		hImages.push("ma_h");
		hImages.push("zhao_h");
		hImages.push("zhang_h");
		hImages.push("guan_h");
		
		Stack<String> unitImages = new Stack<String>();
		unitImages.push("bing4");
		unitImages.push("zu4");
		unitImages.push("yong4");
		unitImages.push("shi4");
		unitImages.push("bing3");
		unitImages.push("zu3");
		unitImages.push("yong3");
		unitImages.push("shi3");
		unitImages.push("bing2");
		unitImages.push("zu2");
		unitImages.push("yong2");
		unitImages.push("shi2");
		unitImages.push("bing");
		unitImages.push("zu");
		unitImages.push("yong");
		unitImages.push("shi");
		
		ArrayList<ChessPiece> chessLayout = new ArrayList<ChessPiece>();
		String[] singleChessPiece = layout.split("S");
		
		for(int i=0; i<singleChessPiece.length; i++) {
			String[] parts = singleChessPiece[i].split(",");
			int posx = Integer.parseInt(parts[1].trim());
			int posy = Integer.parseInt(parts[2].trim());
			int chessType = Integer.parseInt(parts[0].trim());
			
			if(chessType == 1) {
				chessLayout.add(new ChessPiece(posx, posy, 1, 1, unitImages.pop()));
			} else if(Integer.parseInt(parts[0].trim()) == 2) {
				if(vImages.isEmpty()) {
					chessLayout.add(new ChessPiece(posx, posy, 1, 2, "wei_v"));
				} else {
					chessLayout.add(new ChessPiece(posx, posy, 1, 2, vImages.pop()));
				}
			} else if(Integer.parseInt(parts[0].trim()) == 3) {
				if(hImages.isEmpty()) {
					chessLayout.add(new ChessPiece(posx, posy, 2, 1, "wei_h"));
				} else {
					chessLayout.add(new ChessPiece(posx, posy, 2, 1, hImages.pop()));	
				}
			} else if(Integer.parseInt(parts[0].trim()) == 4) {
				chessLayout.add(new ChessPiece(posx, posy, 2, 2, "cao"));	
			}
		}
			
		return chessLayout;
	}
}
