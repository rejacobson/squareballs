package com.ryan.squareballs;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;

public class Tile extends Entity {

	private Bitmap bitmap;
	private boolean solid;
	public int color;
	
	public Tile(Bitmap bitmap) {
		super(bitmap);
		solid = true;
	}
	
	public boolean isSolid() {
		return solid;
	}
	public void setSolid(boolean solid) {
		this.solid = solid;
	}
	
	public void collisionResponse(Ball ball) {
		
	}
}