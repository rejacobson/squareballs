package com.ryan.squareballs;

public class Player {
	
	private int points;
	private int turnsTaken;
	private Ball ball;
	
	public Player(Ball ball) {
		this.ball = ball;
		this.turnsTaken = 0;
		this.points = 0;
	}
	
	public Ball getBall() {
		return ball;
	}
	
	public int getPoints() {
		return points;
	}
	public void setPoints(int points) {
		this.points = points;
	}
	public void addPoints(int points) {
		this.points += points;
	}
	
	public int getTurnsTaken() {
		return turnsTaken;
	}
	public void setTurnsTaken(int turnsTaken) {
		this.turnsTaken = turnsTaken;
	}
	public void incrementTurnsTaken(int number) {
		this.turnsTaken++;
	}
}