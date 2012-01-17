package com.ryan.squareballs;

public class PlayerStats {
	
	public Player player;
	public int turnsTaken;
	public int points;
	
	public PlayerStats(Player player) {
		this.player = player;
		this.turnsTaken = 0;
		this.points = 0;
	}
}