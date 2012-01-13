package com.ryan.squareballs;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

public class ShootIndicator {
	
	private Paint paint;
	private Vector2f position;
	private float magnitude;
	private Vector2f unitDirection;
	private int alpha;
	private float strokeWidth;	
	
	public ShootIndicator() {
		position = new Vector2f();
		
		alpha = 200;
		strokeWidth = 1.0F;
		
		paint = new Paint();
		paint.setColor(Color.WHITE);
		paint.setStyle(Paint.Style.STROKE);
		paint.setAntiAlias(true);
		paint.setStrokeWidth(3.0F);
		paint.setAlpha(100);
	}
	
	public void targetEntity(Entity entity) {
		position.x = entity.position.x;
		position.y = entity.position.y;
	}
	
	public void setDirection(Vector2f unitDirection) {
		this.unitDirection = unitDirection;
	}
	public void setMagnitude(float magnitude) {
		this.magnitude = magnitude;
	}
	
	public void update() {
		
	}
	
	public void draw(Canvas canvas) {
		Vector2f startPosition = Vector2f.add(position, Vector2f.multiply(unitDirection, 20.0f));
		Vector2f endPosition = Vector2f.add(startPosition,  unitDirection.multiply(magnitude));
		
		canvas.drawLine(startPosition.x, startPosition.y, endPosition.x, endPosition.y, paint);
	}
}