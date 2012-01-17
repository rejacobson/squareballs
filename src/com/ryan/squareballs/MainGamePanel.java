package com.ryan.squareballs;

import java.util.Random;

import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class MainGamePanel extends SurfaceView implements SurfaceHolder.Callback {

	private static final String TAG = MainGamePanel.class.getSimpleName();
	
	private MainThread thread;
	
	private int currentLevel;
	private Level level;
	private int[] levels;

	private TileList tileList;
	
	/*private Player[] players;
	private int currentPlayerIndex;
	private Player currentPlayer;
	private Ball currentBall;*/
	
	//private boolean touching;
	//private Vector2f touchPosition;
	
	//private boolean turnTaken;
	private boolean levelWon;
	
	private float friction;
	private float dampening;
	
	private ScoreCard scoreCard;
	private BallShooter ballShooter;
	
	private float scaleAmount;

	private int result;
	
	public MainGamePanel(Context context) {
		super(context);
		
		getHolder().addCallback(this);
		
		setFocusable(true);
	}
	
	private void setupGame() {
		//XmlResourceParser levelResources = getResources().getXml(R.xml.levels);
		
		Player[] players = new Player[3];
		
		players[0] = new Player(new Ball(BitmapFactory.decodeResource(getResources(), R.drawable.player1)));		
		players[1] = new Player(new Ball(BitmapFactory.decodeResource(getResources(), R.drawable.player2)));		
		players[2] = new Player(new Ball(BitmapFactory.decodeResource(getResources(), R.drawable.player3)));		
		//players[3] = new Player(new Ball(BitmapFactory.decodeResource(getResources(), R.drawable.player4)));
		
		scoreCard = new ScoreCard(players, getResources());
		ballShooter = new BallShooter(getResources());
		
		friction = 0.99F;
		dampening = 0.1f;
		
		scaleAmount = 1.0F;
					
		//touchPosition = new Vector2f();
		//touching = false;
		
		//turnTaken = false;
		
		
		
		
		tileList = new TileList(getResources(), 20, 20);
		
		levels = new int[18];
		levels[0] = R.drawable.level1;
		levels[1] = R.drawable.level2;
		levels[2] = R.drawable.level3;
		levels[3] = R.drawable.level4;
		levels[4] = R.drawable.level5;
		levels[5] = R.drawable.level6;
		levels[6] = R.drawable.level7;
		levels[7] = R.drawable.level8;
		levels[8] = R.drawable.level9;
		levels[9] = R.drawable.level10;
		levels[10] = R.drawable.level11;
		levels[11] = R.drawable.level12;
		levels[12] = R.drawable.level13;
		levels[13] = R.drawable.level14;
		levels[14] = R.drawable.level15;
		levels[15] = R.drawable.level16;
		levels[16] = R.drawable.level17;
		levels[17] = R.drawable.level18;
		currentLevel = 0;
			
		loadLevel();
	}

	public void loadLevel() {
		Random r = new Random();
		int lvl = r.nextInt(levels.length);
		
		level = new Level(BitmapFactory.decodeResource(getResources(), levels[lvl]), tileList);
		
		Ball[] balls = scoreCard.getBalls();
		for (int i=0; i<balls.length; i++) {
			balls[i].position.set(0, 0);
			balls[i].setActive(false);
			balls[i].setWinner(false);
		}
		
		levelWon = false;
		
		ballShooter.attachToBall(scoreCard.getBall());
		
		thread.Resume();
	}
	
	public void finishLevel() {
		thread.Pause();
		
		// Tally points
		//scoreCard.tallyPoints();
		scoreCard.finishLevel();
		
		if (currentLevel < levels.length-1) {
			currentLevel++;
			loadLevel();
		} else {
			// End of match
			thread.SetRunning(false);
			((Activity)getContext()).finish();
		}
	}
	
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		// TODO Auto-generated method stub
		
	}

	public void surfaceCreated(SurfaceHolder holder) {
		thread = new MainThread(getHolder(), this);
		
		setupGame();
		
		thread.SetRunning(true);
		thread.start();
		
		Log.d(TAG, "Window size == "+ getWidth() +":"+ getHeight());
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		boolean retry = true;
		while (retry) {
			try {
				thread.SetRunning(false);
				thread.join();
				thread = null;
				retry = false;
				//((Activity)getContext()).finish();
			} catch(InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		//touchPosition.set(event.getX(), event.getY());
				
		// DOWN
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			ballShooter.actionDown(event.getX(), event.getY());
		}
		
		// MOVE
		if (event.getAction() == MotionEvent.ACTION_MOVE) {
			ballShooter.actionMove(event.getX(), event.getY());
		}
		
		// UP
		if (event.getAction() == MotionEvent.ACTION_UP) {
			ballShooter.actionUp(event.getX(), event.getY());
		}		
		
		return true;
	}
	
	public void finishTurn() {		
		//turnTaken = false;
		
		scoreCard.finishTurn();
		
		ballShooter.attachToBall(scoreCard.getBall());
		
		Ball currentBall = scoreCard.getBall();
		
		if (!currentBall.isActive()) {
			currentBall.position.set(level.getStartPosition().x, level.getStartPosition().y);
			currentBall.setActive(true);
			ballShooter.attachToBall(currentBall);
		}
	}
	
	public void update(long tickCount) {
		Ball[] balls = scoreCard.getBalls();
		Ball ball;
		int movingBallsCount = 0;
		
		//ballShooter.setTouchPosition(touchPosition);
		
		ball = scoreCard.getBall();
		
		if (!ball.isActive()) {
			ball.position.set(level.getStartPosition().x, level.getStartPosition().y);
			ball.setActive(true);
			ballShooter.attachToBall(ball);
		}
		
		ballShooter.update();
		
		// Update all balls
		for (int i=0; i<balls.length; i++) {
			ball = balls[i];
			
			if (ball.isActive()) {
				ball.update();
				
				if (ball.isMoving()) {
					movingBallsCount++;
				}
			}
		}
		
		checkCollision();
		
		if (levelWon) {
			finishLevel();
			return;
		}
		
		// Next player's turn
		if (movingBallsCount == 0 && ballShooter.isFired()) {
			finishTurn();
		}
	}
	
	private void checkCollision() {
		Ball[] balls = scoreCard.getBalls();
		Ball b1, b2;
		Tile tile;
		
		// Check collision between entities and walls
		for (int i=0; i<balls.length; i++) {
			b1 = balls[i];
			
			if (!b1.isActive()) continue;
			
			tile = level.getCollidingTile(b1);
			
			// A collision happened
			if (tile != null) {
				tile.collisionResponse(b1);
				
				if (b1.isWinner()) {
					levelWon = true;
					return;
				}
				
				if (tile.isSolid()) {					
					int collisionEdge = Entity.getCollisionEdge(b1, tile, b1.velocity);
					
					// Left and Right edge						
					if (collisionEdge == 1 || collisionEdge == 3) {
						//Log.d(TAG, "collision response -- x-axis");
						b1.velocity.x *= -1;
					}
					
					// Top and Bottom edge
					if (collisionEdge == 2 || collisionEdge == 4) {
						//Log.d(TAG, "collision response -- y-axis");
						b1.velocity.y *= -1;
					}
				}
			}
			
			if (b1.velocity.x < 0 && b1.position.x <= b1.getBitmap().getWidth()/2) {
				b1.velocity.x *= -1;
			}
			if (b1.velocity.y < 0 && b1.position.y <= b1.getBitmap().getHeight()/2) {
				b1.velocity.y *= -1;
			}
			
			if (b1.velocity.x > 0 && b1.position.x >= getWidth() - b1.getBitmap().getWidth()/2) {
				b1.velocity.x *= -1;
			}
			if (b1.velocity.y > 0 && b1.position.y >= getHeight() - b1.getBitmap().getHeight()/2) {
				b1.velocity.y *= -1;
			}
			
			if ( b1.velocity.x != 0 || b1.velocity.y != 0) {
				b1.getVelocity().multiply(friction);
			}
			
			// Check winning state
			if (b1.isWinner()) {
				
			}
		}
		
			
		
		// Check collision between entities
		if (balls.length > 1) {
							
			for (int i=0; i<balls.length-1; i++) {
				b1 = balls[i];
				
				if (!b1.isActive()) continue;
				
				for (int j=(i+1); j<balls.length; j++) {
					b2 = balls[j];					
					
					if (!b2.isActive()) continue;
					
					if (b1.collidesWith(b2)) {
						Vector2f relativeVelocity = Vector2f.subtract(b1.velocity, b2.velocity);
						int collisionEdge = Entity.getCollisionEdge(b1, b2, relativeVelocity);
						
						if (collisionEdge != 0) {
							Log.d(TAG, "Collision!!!! edge == "+result);
						}
						
						// Left and Right edge						
						if (collisionEdge == 1 || collisionEdge == 3) {
							float vx = b1.velocity.x;
							b1.velocity.x = b2.velocity.x;
							b2.velocity.x = vx;
						}
						
						// Top and Bottom edge
						if (collisionEdge == 2 || collisionEdge == 4) {
							float vy = b1.velocity.y;
							b1.velocity.y = b2.velocity.y;
							b2.velocity.y = vy;
						}
						
					}
				}
			}
		
		}
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		Ball[] balls = scoreCard.getBalls();
		Ball ball;
		
		// Detect touching past the edge
		/*
		if (touching) {
			if (touchPosition.x < 15 || 
				touchPosition.x > getWidth()-15 ||
				touchPosition.y < 15 || 
				touchPosition.y > getWidth()-15) {
				if (scaleAmount > 0.5) scaleAmount -= 0.01;
			}
		}
		
		if (scaleAmount < 1) {
			canvas.scale(scaleAmount, scaleAmount, getWidth()/2, getHeight()/2);
			Vector2f center = new Vector2f(getWidth()/2, getHeight()/2);
			Vector2f diff = Vector2f.subtract(center, touchPosition);
			float distance = diff.getMagnitude();
			Vector2f diffUnit = diff.getUnit();
			touchPosition = Vector2f.subtract(center, diffUnit.multiply(-scaleAmount));
		}
		*/
		
		canvas.drawColor(Color.BLACK);
		
		level.draw(canvas);
		
		for (int i=0; i<balls.length; i++) {
			ball = balls[i];			
			if (ball.isActive()) ball.draw(canvas);
		}
		
		ballShooter.draw(canvas);
		
		scoreCard.draw(canvas);
		
	}
	
}