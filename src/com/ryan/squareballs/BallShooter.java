package com.ryan.squareballs;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

public class BallShooter {
	
	private static final String TAG = BallShooter.class.getSimpleName();
	
	private float power;
	private float maxPower;
	private float incrementCounter;
	private Vector2f unitDirection;
	private Entity target;
	private Vector2f touchPosition;
	
	// Animation vars
	private Resources res;
	private Bitmap powerBarBitmap;
	private int alpha;
	private float strokeWidth;
	private Paint paint;
	private Paint powerPaint;
	
	public BallShooter(Resources res) {
		this.res = res;
		this.powerBarBitmap = BitmapFactory.decodeResource(res, R.drawable.powerbar);
		
		touchPosition = new Vector2f();
		
		power = 0;
		maxPower = 8.0F;
		incrementCounter = 0.05F;
		
		alpha = 200;
		strokeWidth = 1.0F;
		
		paint = new Paint();
		paint.setColor(Color.WHITE);
		paint.setStyle(Paint.Style.STROKE);
		paint.setAntiAlias(true);
		paint.setStrokeWidth(3.0F);
		paint.setAlpha(100);
		
		powerPaint = new Paint();
		powerPaint.setColor(Color.BLUE);
		powerPaint.setStyle(Paint.Style.STROKE);
		powerPaint.setAntiAlias(true);
		powerPaint.setStrokeWidth(6.0F);
		powerPaint.setAlpha(200);
		
	}
	
	public void targetEntity(Entity target) {
		this.target = target;
	}
	
	public Vector2f getUnitDirection() {
		return Vector2f.subtract(target.position, touchPosition).getUnit();
	}
	public float getPower() {
		return power;
	}
	public Vector2f getForce() {
		return getUnitDirection().multiply(getPower());
	}
	
	public void setTouchPosition(Vector2f touchPosition) {
		this.touchPosition = touchPosition;
	}
	
	public void update() {
		if (power > maxPower) {
			incrementCounter *= -1;
		} else if (power < 0) {
			incrementCounter *= -1;
		}
		
		power += incrementCounter;
	}
	
	public void draw(Canvas canvas) {
		Log.d(TAG, "Drawing ballShooter -- power == "+getPower());
		Vector2f direction = Vector2f.subtract(target.position, touchPosition).getUnit();
		
		Vector2f startPosition = Vector2f.add(target.position, Vector2f.multiply(direction, 20.0f));
		Vector2f endPosition = Vector2f.add(startPosition, Vector2f.multiply(direction, 200));
		
		//Log.d(TAG, "---------------------- start == "+startPosition.toString());
		//Log.d(TAG, "---------------------- end == "+endPosition.toString());
		//Log.d(TAG, "-------------------------------------------------");
		
		canvas.drawLine(startPosition.x, startPosition.y, endPosition.x, endPosition.y, paint);
		
		endPosition = Vector2f.add(startPosition, Vector2f.multiply(direction, getPower()*10.0F));
		
		canvas.drawLine(startPosition.x, startPosition.y, endPosition.x, endPosition.y, powerPaint);
		
		/*canvas.save();
		
		Matrix matrix = new Matrix();
		matrix.postTranslate(position.x, position.y);
		matrix.postScale(1, 1);
		matrix.postRotate(0);
		
		canvas.drawBitmap(powerBarBitmap, matrix, null);
		
		canvas.restore();
		*/
	}
}