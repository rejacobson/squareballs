package com.ryan.squareballs;

import android.graphics.Canvas;
import android.util.Log;

public class ScoreCard {
	
	private static final String TAG = ScoreCard.class.getSimpleName();
	
	public Player[] players;
	public int[] ranks;
	
	public ScoreCard(Player[] players) {
		this.players = players;
		this.ranks = new int[players.length];
		
		for (int i=0; i<players.length; i++) {
			this.ranks[i] = i;
		}
	}
	
	public void tallyPoints() {
		Player p;
		
		int topPlayerIndex = 0;
		
		for (int i=0; i<players.length; i++) {
			int points = 0;			
			p = players[i];
			
			points = Math.max(5 - p.getTurnsTaken(), 0);
			
			if (p.getBall().isWinner())
				points += 2;
			
			p.addPoints(points);
			
			Log.d(TAG, "Player "+(i+1)+" points: "+p.getPoints());
		}
	}
	
	public void setTopPlayer(int index) {
		
	}
	
	public int[] getRanks() {
		return ranks;
	}
	
	public void draw(Canvas canvas) {
		
	}
}