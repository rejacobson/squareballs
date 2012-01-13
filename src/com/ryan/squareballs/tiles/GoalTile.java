package com.ryan.squareballs.tiles;

import android.graphics.Bitmap;
import android.graphics.Color;

import com.ryan.squareballs.Ball;
import com.ryan.squareballs.Tile;

public class GoalTile extends Tile {

	public GoalTile(Bitmap bitmap) {
		super(bitmap);
		color = Color.WHITE;
		setSolid(false);
	}
	
	public void collisionResponse(Ball ball) {
		ball.setActive(false);
		ball.getPosition().set(0, 0);
		ball.getVelocity().set(0, 0);
		ball.setWinner(true);
	}
	
}