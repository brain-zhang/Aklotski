package me.happy123.klotski;

public class Position {
	private int posx, posy;
	
	public Position(int x, int y) {
		this.posx = x;
		this.posy = y;
	}
	
	public int getPosx() {
		return this.posx;
	}
	
	public int getPosy() {
		return this.posy;
	}
	
	public boolean equals(Object object2) {
	    return object2 instanceof Position && posx == (((Position)object2).posx) && posy == (((Position)object2).posy);
	}
}
