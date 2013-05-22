package com.thelonelybyte.tard;

import com.badlogic.gdx.utils.Array;

public class Room {
	private Array<Array<Block>> roomblocks;
	private int[] connectedto;
	
	public Room(Array<Array<Block>> blocks, int[] connectedto) {
		roomblocks = blocks;
		this.connectedto = connectedto;
	}

}
