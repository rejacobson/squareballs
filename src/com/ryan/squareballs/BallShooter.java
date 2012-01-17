package com.ryan.squareballs;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

public class BallShooter {
	
	public enum State {WAITING, ADJUSTING_METER, SHOOTING_BALL}
	
	public class Meter {
		private float power;
		private float maxPower;
		private float incrementCounter;
		private Paint meterPaint;
		
		public Meter() {
			power = 0;
			maxPower = 8.0F;
			incrementCounter = 0.05F;
			
			meterPaint = new Paint();
			meterPaint.setColor(Color.BLUE);
			meterPaint.setStyle(Paint.Style.STROKE);
			meterPaint.setAntiAlias(true);
			meterPaint.setStrokeWidth(6.0F);
			meterPaint.setAlpha(200);
		}
		
		public void reset() {
			power = 0;
			incrementCounter = 0.05F;
		}
		
		public float getPower() {
			return power;
		}
		
		public Vector2f getForce(Vector2f unitDirection) {
			return unitDirection.multiply(getPower());
		}
		
		public void update() {
			if (power > maxPower) {
				incrementCounter *= -1;
			} else if (power < 0) {
				incrementCounter *= -1;
			}
			
			power += incrementCounter;
		}
		
		public void draw(Canvas canvas, Vector2f position, Vector2f unitDirection) {
			Vector2f endPosition = Vector2f.add(position, Vector2f.multiply(unitDirection, getPower()*10));			
			canvas.drawLine(position.x, position.y, endPosition.x, endPosition.y, meterPaint);
		}
	}
	
	private static final String TAG = BallShooter.class.getSimpleName();
	
	private BallIndicator ballIndicator;
	
	private Entity ball;
	private Meter meter;
	private State state;
	
	//private Vector2f unitDirection;
	private Vector2f touchPosition;
	
	private boolean holdingTarget;	
	
	// Animation vars
	private Resources res;
	private Bitmap powerBarBitmap;
	private int alpha;
	private float strokeWidth;
	private Paint paint;
	
	public BallShooter(Resources res) {
		this.meter = new Meter();		
		this.res = res;
		this.powerBarBitmap = BitmapFactory.decodeResource(res, R.drawable.powerbar);
		this.ballIndicator = new BallIndicator();
		
		touchPosition = new Vector2f();		
		
		alpha = 200;
		strokeWidth = 1.0F;
		
		paint = new Paint();
		paint.setColor(Color.WHITE);
		paint.setStyle(Paint.Style.STROKE);
		paint.setAntiAlias(true);
		paint.setStrokeWidth(3.0F);
		paint.setAlpha(100);
		
	}
	
	public void attachToBall(Entity target) {
		this.state = State.WAITING;
		this.ball = target;		
		this.meter.reset();
		
		ballIndicator.targetEntity(ball);
	}
	
	public boolean isFired() {
		return state == State.SHOOTING_BALL;
	}
	public boolean isWaiting() {
		return state == State.WAITING;
	}
	
	public void setTouchPosition(Vector2f touchPosition) {
		this.touchPosition = touchPosition;
	}
	
	public void actionDown(float x, float y) {		
		Vector2f touchPoint = new Vector2f(x, y);
		float distance = touchPoint.getDistance(ball.getPosition());
		
		if ( distance <= 75 && (state == State.WAITING || state == State.ADJUSTING_METER) ) {
			state = State.ADJUSTING_METER;
			touchPosition.set(x, y);
		} else {
			if (state == State.ADJUSTING_METER) {
				state = State.SHOOTING_BALL;
				launchBall();
			}
		}
	}
	public void actionMove(float x, float y) {		
		if (state == State.ADJUSTING_METER) {
			touchPosition.set(x, y);
		}
	}
	public void actionUp(float x, float y) {
	}
	
	public void launchBall() {
		Vector2f unitDirection = Vector2f.subtract(ball.position, touchPosition).getUnit();
		Vector2f velocity = meter.getForce(unitDirection); //Vector2f.subtract(currentBall.position, touchUpPosition).multiply(dampening);
		
		ball.setVelocity(velocity);
	}
	
	public void update() {
		ballIndicator.update();
		
		if (state != State.ADJUSTING_METER) return;
		meter.update();
	}
	
	public void draw(Canvas canvas) {
		Log.d(TAG, "State == "+ state);
		
		if (state == State.WAITING) {
			Log.d(TAG, "Drawing the BallShooter indicator ----------------");
			ballIndicator.draw(canvas);
		}
		
		if (state != State.ADJUSTING_METER) return;
		
		
		Vector2f unitDirection = Vector2f.subtract(ball.position, touchPosition).getUnit();
		
		Vector2f startPosition = Vector2f.add(ball.position, Vector2f.multiply(unitDirection, 20.0f));
		Vector2f endPosition = Vector2f.add(startPosition, Vector2f.multiply(unitDirection, 200));
		
		//Log.d(TAG, "---------------------- start == "+startPosition.toString());
		//Log.d(TAG, "---------------------- end == "+endPosition.toString());
		//Log.d(TAG, "-------------------------------------------------");
		
		canvas.drawLine(startPosition.x, startPosition.y, endPosition.x, endPosition.y, paint);
		
		meter.draw(canvas, startPosition, unitDirection);
		
		
		
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