package com.thelonelybyte.tard;

import java.util.Random;

// @Author: Isak Andersson

import com.badlogic.gdx.utils.Array;

public class Dungeon {
	// Structure, first array = x, second array = y
	private Array<Room> rooms;
	
	public Dungeon(Array<Room> rooms) {
		this.rooms = rooms;
	}
	
	public Room getRoom(int index) {
		return rooms.get(index);
	}
	
	public int roomCount() {
		return rooms.size;
	}
	
	public Room getRandomRoom() {
		Random r = new Random();
		return rooms.get(r.nextInt(rooms.size));
	}
	
}
