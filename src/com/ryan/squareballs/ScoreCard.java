package com.ryan.squareballs;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

public class ScoreCard {
	
	private static final String TAG = ScoreCard.class.getSimpleName();
	
	public Player[] players;
	private Player currentPlayer;
	private int currentPlayerIndex;
	
	public Bitmap bitmap;
	private Resources res;
	private Paint paint;
	
	public ScoreCard(Player[] players, Resources res) {
		this.res = res;
		this.players = players;
		
		currentPlayerIndex = 0;
		currentPlayer = players[currentPlayerIndex];
				
		this.bitmap = BitmapFactory.decodeResource(res, R.drawable.scorecard);
		
		paint = new Paint();
		paint.setColor(Color.BLACK);
		paint.setStyle(Paint.Style.FILL);
		
		//paint.setStrokeWidth(2.0F);
		paint.setTextSize(16);
	}
	public void finishLevel() {
		tallyPoints();
		
		for (int i=0; i<players.length; i++) {
			//players[i].
		}
	}
	
	public void finishTurn() {
		if (currentPlayerIndex >= players.length-1) {
			currentPlayerIndex = 0;
		} else {
			currentPlayerIndex++;
		}
		
		currentPlayer = players[currentPlayerIndex];
	}
	
	public Ball getBall() {
		return currentPlayer.getBall();
	}
	public Ball[] getBalls() {
		Ball[] balls = new Ball[players.length];
		
		for (int i=0; i<players.length; i++) {
			balls[i] = players[i].getBall();
		}
		
		//Log.d(TAG, "getBalls() -- "+balls.length);
		
		return balls;
	}
	
	public Player getPlayer() {
		return currentPlayer;
	}
	public Player[] getPlayers() {
		return players;
	}
	
	public void tallyPoints() {
		Player p;
		
		int topPlayerIndex = 0;
		
		for (int i=0; i<players.length; i++) {
			//int points = 0;			
			p = players[i];
			
			//points = Math.max(5 - p.getTurnsTaken(), 0);
			
			if (p.getBall().isWinner())
				p.addPoints(1);
			
			//p.addPoints(points);
			
			Log.d(TAG, "Player "+(i+1)+" points: "+p.getPoints());
		}
	}
	
	public void draw(Canvas canvas) {
		canvas.drawBitmap(bitmap, 0.0F, 460.0F, null);
		
		float spacing = 50;
		
		for (int i=0; i<players.length; i++) {
			canvas.drawText(""+players[i].getPoints(), 25 + (spacing*i), 476, paint);
		}
	}
}