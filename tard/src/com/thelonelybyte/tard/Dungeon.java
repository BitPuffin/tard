package com.thelonelybyte.tard;

import com.badlogic.gdx.utils.Array;

public class Dungeon {
	// Structure, first array = x, second array = y
	private Array<Array<Block>> blocks;
	
	public Dungeon(Array<Array<Block>> blocks) {
		this.blocks = blocks;
	}
	
	
}
