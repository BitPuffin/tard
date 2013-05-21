package com.thelonelybyte.tard;

import com.badlogic.gdx.utils.Array;

public class DungeonManager {
	private Array<Dungeon> dungeons;
	
	public DungeonManager() {
		dungeons = new Array<Dungeon>();
	}
	
	public void add(Dungeon d) {
		dungeons.add(d);
	}
	
	public Dungeon get(int index) {
		return dungeons.get(index);
	}

}
