package com.ryan.squareballs;

import java.util.HashMap;

import android.content.res.Resources;
import android.graphics.BitmapFactory;

import com.ryan.squareballs.tiles.FloorTile;
import com.ryan.squareballs.tiles.GoalTile;
import com.ryan.squareballs.tiles.WallTile;

public class TileList {
	
	private HashMap<Integer, Tile> tileMap;
	
	public int tileWidth;
	public int tileHeight;
	
	private Resources resources;
	
	public TileList(Resources resources, int tileWidth, int tileHeight) {
		this.resources = resources;
		this.tileWidth = tileWidth;
		this.tileHeight = tileHeight;
		loadTileData();
	}
	
	private void loadTileData() {
		tileMap = new HashMap<Integer, Tile>();
		
		//FloorTile floor = new FloorTile();
		WallTile wall   = new WallTile(BitmapFactory.decodeResource(resources, R.drawable.tile_wall));
		GoalTile goal   = new GoalTile(BitmapFactory.decodeResource(resources, R.drawable.tile_goal));
		
		//tileMap.put(floor.color, floor);
		tileMap.put(wall.color, wall);
		tileMap.put(goal.color, goal);
	}
	
	public int getTileWidth() {
		return tileWidth;
	}
	public int getTileHeight() {
		return tileHeight;
	}
	
	public Tile getTileByColor(int color) {
		if (tileMap.containsKey(color)) {
			return tileMap.get(color);
		}
		
		return null;
	}
}