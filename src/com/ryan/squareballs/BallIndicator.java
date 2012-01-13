package com.ryan.squareballs;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

public class BallIndicator {
	
	private Paint paint;
	private Vector2f position;
	private int alpha;
	private float strokeWidth;
	private float width;
	private float height;
	private float currentWidth;
	private float currentHeight;
	
	public BallIndicator() {
		position = new Vector2f();
		
		alpha = 200;
		strokeWidth = 1.0F;
		
		paint = new Paint();
		paint.setColor(Color.WHITE);
		paint.setStyle(Paint.Style.STROKE);
		paint.setAntiAlias(true);
	}
	
	public void targetEntity(Entity entity) {
		width = entity.getBitmap().getWidth();
		height = entity.getBitmap().getHeight();
		position.x = entity.position.x;
		position.y = entity.position.y;
	}
	
	public RectF getRect() {
		return new RectF(
				position.x - currentWidth/2,   // left
				position.y - currentHeight/2,  // top
				position.x + currentWidth/2,   // right
				position.y + currentHeight/2); // bottom
	}
	
	public void update() {
		if (currentWidth > width + 40 || currentHeight > height + 40) {
			currentWidth = width;
			currentHeight = height;
			strokeWidth = 1.0F;
			alpha = 200;
		}
		
		currentWidth += 0.25;
		currentHeight += 0.25;
		strokeWidth += 0.25;
		
		if (alpha > 0) alpha -= 5;
	}
	
	public void draw(Canvas canvas) {
		paint.setAlpha(alpha);
		paint.setStrokeWidth(strokeWidth);
		canvas.drawRect(getRect(), paint);
	}
}