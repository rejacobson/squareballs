package com.ryan.squareballs;

import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceHolder;

public class MainThread extends Thread {
	
	private static final String TAG = MainThread.class.getSimpleName();
	
	private boolean running;
	private boolean paused;
	private SurfaceHolder surfaceHolder;
	private MainGamePanel gamePanel;
	
	public MainThread(SurfaceHolder surfaceHolder, MainGamePanel gamePanel) {
		super();
		this.surfaceHolder = surfaceHolder;
		this.gamePanel = gamePanel;
		this.paused = false;
	}
	
	public void SetRunning(boolean running) {
		this.running = running;
	}
	
	public void Pause() {
		this.paused = true;
	}
	public void Resume() {
		this.paused = false;
	}
	
	@Override
	public void run() {
		Canvas canvas;
		long tickCount = 0L;
		
		Log.d(TAG, "Starting Game Loop");
		
		while (running) {
			if (paused) continue;
			
			tickCount++;
			canvas = null;
			
			// try locking the canvas for exclusive pixel editing on the surface
			try {
				canvas = this.surfaceHolder.lockCanvas();
				synchronized(surfaceHolder) {
					// update game state
					this.gamePanel.update(tickCount);
					
					// draws the canvas on the panel
					this.gamePanel.onDraw(canvas);
				}
				
			} finally {
				// in case of an exception the surface is not left in
				// an inconsistent state
				if (canvas != null) {
					surfaceHolder.unlockCanvasAndPost(canvas);
				}
			}
		}
		
		Log.d(TAG, "Game loop ran, "+ tickCount +" times.");
	}
	
	public void run2() {
		int TICKS_PER_SECOND = 25;
		int SKIP_TICKS = 1000 / TICKS_PER_SECOND;
		int MAX_FRAMESKIP = 5;
		
		float beginTime = System.currentTimeMillis();
		long tickCount = 0L;
		
		
	}
}