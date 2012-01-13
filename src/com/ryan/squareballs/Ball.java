package com.ryan.squareballs;

import android.graphics.Bitmap;

public class Ball extends Entity {

	private boolean active;
	private boolean winner;
	
	public Ball(Bitmap bitmap) {
		super(bitmap);
		setActive(false);
		setWinner(false);
	}
	
	public Ball(Bitmap bitmap, float x, float y) {
		super(bitmap);
		setPosition(x, y);
		setActive(false);
	}
	
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
	
	public boolean isWinner() {
		return winner;
	}
	public void setWinner(boolean winner) {
		this.winner = winner;
	}
	
	/*public boolean collidesWith(Entity b) {
		
		// Check if entities are moving apart
		//if (b.getClass().getName() == "")
		
		return super.collidesWith(b);
	}*/
	
	
	public void detectSelection(int eventX, int eventY) {
		Vector2f touchPoint = new Vector2f(eventX, eventY);
		float distance = touchPoint.getDistance(position);
		
		if ( distance <= 75 ) {
			setTouched(true);
		} else {
			setTouched(false);
		}
	}
}