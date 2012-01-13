package com.ryan.squareballs.tiles;

import android.graphics.Canvas;
import android.graphics.Color;

import com.ryan.squareballs.Tile;

public class FloorTile extends Tile {

	public FloorTile() {
		super(null);
		color = Color.BLACK;
		setSolid(false);
	}
		
}