package com.ryan.squareballs;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;

public class Entity {
	
	private static final String TAG = Entity.class.getSimpleName();
	
	private Bitmap bitmap;
	
	public Vector2f position;
	public Vector2f velocity;
	
	private float maxVelocity;
	
	int width;
	int height;
	
	private boolean touched;
	
	public Entity(Bitmap bitmap) {
		this.bitmap = bitmap;
		this.width = 20;
		this.height = 20;
		this.position = new Vector2f();
		this.velocity = new Vector2f();
		this.maxVelocity = 8.0F;
	}
	
	//////////////////////////////////////////////////
	// Bitmap
	//////////////////////////////////////////////////
	public Bitmap getBitmap() {
		return bitmap;
	}	
	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}

	//////////////////////////////////////////////////
	// Velocity
	//////////////////////////////////////////////////
	public Vector2f getVelocity() {
		return velocity;
	}
	public void setVelocity(Vector2f velocity) {
		this.velocity = velocity;
	}
	public void setVelocity(float x, float y) {
		velocity.x = x;
		velocity.y = y;
	}
	
	//////////////////////////////////////////////////
	// Position
	//////////////////////////////////////////////////
	public Vector2f getPosition() {
		return position;
	}
	public void setPosition(Vector2f position) {
		this.position = position;
	}
	public void setPosition(float x, float y) {
		position.x = x;
		position.y = y;
	}
	
	public void setSize(int width, int height) {
		this.width = width;
		this.height = height;
	}

	public RectF getRect() {
		return new RectF(
				position.x - width/2,   // left
				position.y - height/2,  // top
				position.x + width/2,   // right
				position.y + height/2); // bottom
	}
	public RectF getDoubleRect() {
		return new RectF(
				position.x - width,   // left
				position.y - height,  // top
				position.x + width,   // right
				position.y + height); // bottom
	}
		
	public boolean isTouched() {
		return touched;
	}
	public void setTouched(boolean touched) {
		this.touched = touched;
	}
	
	public boolean isMoving() {
		return velocity.getSquaredMagnitude() > 0.0001F;
	}
	public boolean isStopped() {
		return !isMoving();
	}
	
	public boolean collidesWith(Entity entity) {
		// Check if the entities are moving away from each other
		Vector2f positionDiff = Vector2f.subtract(entity.position, this.position);
		Vector2f velocityDiff = Vector2f.subtract(entity.velocity, this.velocity);
		
		if (velocityDiff.dot(positionDiff) >= 0) return false;
		
		// test rect intersection
		RectF a = getRect();
		RectF b = entity.getRect();
		return RectF.intersects(a, b);
	}
	
	public static int getCollisionEdge(Entity a, Entity b, Vector2f movingDirection) {
		RectF b_rect = b.getDoubleRect();
		Vector2f pnt1 = a.position;
		Vector2f pnt2 = Vector2f.subtract(a.position, movingDirection);
		return Vector2f.rectLineIntersection(b_rect, pnt1, pnt2);
	}
	
	public void update() {
		// Restrict speed
		if (velocity.getMagnitude() > maxVelocity) {
			velocity.unit().multiply(maxVelocity);
		}
				
		position.add(velocity);
	}
	
	public void draw(Canvas canvas) {
		if (bitmap == null) return;
		
		// Draw only part of the bitmap
		canvas.drawBitmap(bitmap, position.x - bitmap.getWidth()/2, 
				position.y - bitmap.getHeight()/2, null);
	}
}