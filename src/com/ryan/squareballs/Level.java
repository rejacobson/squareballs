package com.ryan.squareballs;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.Log;

public class Level {
	
	private static final String TAG = Level.class.getSimpleName();
	
	private Bitmap bitmap;
	private int width;
	private int height;
	private Tile[][] tileMap;
	private TileList tileList;
	private Vector2f startPosition;
	
	public Level(Bitmap bitmap, TileList tileList) {
		Log.d(TAG, "Creating Level");
		
		this.tileList = tileList;
		loadBitmap(bitmap);
		
		Log.d(TAG, "Tile size == "+tileList.getTileWidth()+":"+tileList.getTileHeight());
		
		int[] index = getIndexByPosition(200.0F, 400.0F);
		Log.d(TAG, "getIndexByPosition test -- "+index[0]+":"+index[1] );
		
		//Tile tile = getTileByPosition((float)15*20, (float)23*20);
		//Log.d(TAG, "getTileByPosition test -- "+tile.position.x+":"+tile.position.y );
	}
	
	void loadBitmap(Bitmap bitmap) {
		Log.d(TAG, "Loading Level Bitmap");
		
		this.bitmap = bitmap;
		this.width = bitmap.getWidth();
		this.height = bitmap.getHeight();
		
		Log.d(TAG, "Level bitmap dimensions == "+width+":"+height);
		
		tileMap = new Tile[width][height];
		int color;
		
		for ( int x = 0; x < width; x++ ) {
			for ( int y = 0; y < height; y++ ) {
				color = bitmap.getPixel(x, y);
				
				if (color == Color.MAGENTA) {
					startPosition = new Vector2f(x*tileList.getTileWidth() + tileList.getTileWidth()/2, 
							y*tileList.getTileHeight() + tileList.getTileHeight()/2);
					continue;
				}
				
				tileMap[x][y] = tileList.getTileByColor(color);
			}
		}
		
		int nullCount = 0;
		int count = 0;
		for ( int x = 0; x < width; x++ ) {
			for ( int y = 0; y < height; y++ ) {
				if (tileMap[x][y] == null) {
					nullCount++;
				}
				count++;
			}
		}
		
		Log.d(TAG, "tile bitmap width:height == "+width+":"+height);
		Log.d(TAG, "Tile NULL count == "+nullCount);
		Log.d(TAG, "Tile count == "+count);
	}
	
	public Vector2f getStartPosition() {
		return startPosition;
	}
	
	public Tile getTileByIndex(int x, int y) {
		// Bounds check the coordinates
		if (x >= 0 && x < width && y >=0 && y < height) {
			Tile tile = tileMap[x][y];
			
			if (tile == null) return null;
			
			int tileWidth = tileList.getTileWidth();
			int tileHeight = tileList.getTileHeight();
			
			tile.position.set(x*tileWidth + tileWidth/2, y*tileList.getTileHeight() + tileHeight/2);
			return tile;
		}
		
		return null;
	}
	
	public Tile getTileByPosition(float positionX, float positionY) {
		int[] index = getIndexByPosition(positionX, positionY);
		return getTileByIndex(index[0], index[1]);
	}
	
	public int[] getIndexByPosition(float positionX, float positionY) {
		int x = (int)Math.floor(positionX / tileList.getTileWidth());
		int y = (int)Math.floor(positionY / tileList.getTileHeight());
		int[] index = {x, y};
		return index;
	}
	
	public Tile[][] getSurroundingTiles(float positionX, float positionY) {
		Tile[][] tiles = new Tile[3][3];
		int x = (int)Math.floor(positionX / tileList.getTileWidth()) - 1;
		int y = (int)Math.floor(positionY / tileList.getTileHeight()) - 1;
		
		for ( ; x < 3; x++ ) {
			for ( ; y < 3; y++) {
				tiles[x][y] = getTileByIndex(x, y);
			}
		}
		
		return tiles;
	}
	
	public Tile getCollidingTile(Entity entity) {
		Tile tile;
		int[] index = getIndexByPosition(entity.getPosition().x, entity.getPosition().y);
		int x = index[0];
		int y = index[1];
		
		// Check 0,0 tile
		if ((tile = checkCollisionByIndex(entity, x, y)) != null) return tile;
		
		// Check adjacent tiles
		if ((tile = checkCollisionByIndex(entity, x-1, y  )) != null) return tile;
		if ((tile = checkCollisionByIndex(entity, x,   y-1)) != null) return tile;
		if ((tile = checkCollisionByIndex(entity, x+1, y  )) != null) return tile;
		if ((tile = checkCollisionByIndex(entity, x,   y+1)) != null) return tile;
		
		// Check diagonal tiles
		if ((tile = checkCollisionByIndex(entity, x-1, y-1)) != null) return tile;
		if ((tile = checkCollisionByIndex(entity, x+1, y-1)) != null) return tile;
		if ((tile = checkCollisionByIndex(entity, x+1, y+1)) != null) return tile;
		if ((tile = checkCollisionByIndex(entity, x-1, y+1)) != null) return tile;
		
		return null;
	}
	
	public Tile checkCollisionByIndex(Entity entity, int x, int y) {
		Tile tile = getTileByIndex(x, y);
		if (tile == null) return null;
		if (tile.collidesWith(entity)) return tile;		
		return null;
	}
	
	public void draw(Canvas canvas) {
		Tile tile;
		int offset = 10;
		int tileWidth = tileList.getTileWidth();
		int tileHeight = tileList.getTileHeight();
		
		for ( int x = 0; x < width; x++ ) {
			for ( int y = 0; y < height; y++ ) {				
				tile = getTileByIndex(x, y);
				
				if (tile == null) continue;
				
				tile.draw(canvas);
			}
		}
	}
}