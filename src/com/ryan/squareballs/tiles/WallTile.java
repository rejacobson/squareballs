package com.ryan.squareballs.tiles;

import com.ryan.squareballs.Tile;

import android.graphics.Bitmap;
import android.graphics.Color;

public class WallTile extends Tile {

	public WallTile(Bitmap bitmap) {
		super(bitmap);
		color = Color.YELLOW;
		setSolid(true);
	}
	
}