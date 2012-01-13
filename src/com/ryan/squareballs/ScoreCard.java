package com.ryan.squareballs;

import android.graphics.Canvas;

public class ScoreCard {
	
	public Player[] players;
	
	public ScoreCard(Player[] players) {
		this.players = players;
	}
	
	public void tallyPoints() {
		Player p;
		
		for (int i=0; i<players.length; i++) {
			int points = 0;			
			p = players[i];
			
			points = Math.max(5 - p.getTurnsTaken(), 0);
			
			if (p.getBall().isWinner())
				points += 2;
			
			p.addPoints(points);
		}
	}
	
	public void draw(Canvas canvas) {
		
	}
}