package com.thelonelybyte.tard;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;

public class Room {
	private Array<Array<Block>> roomblocks;
	private int[] connectedto;
	
	public Room(Array<Array<Block>> blocks, int[] connectedto) {
		roomblocks = blocks;
		this.connectedto = connectedto;
	}
	
	public int[] getDimensions() {
		int[] dimensions = new int[2];
		dimensions[0] = getWidth();
		dimensions[1] = getHeight();
		return dimensions;
	}
	
	public int getWidth() {
		return roomblocks.size;
	}
	
	public int getHeight() {
		return roomblocks.get(0).size;
	}
	
	public Block getBlockTypeAt(int x, int y) {
		return roomblocks.get(x).get(y);
	}
	
	public int getNextRoomIndex(int playerdirection) {
		return connectedto[playerdirection];
	}
	
	public int[] getEntranceCoordinates(int playerdirection) {
		int[] coordinates = new int[2]; // x and y index of the entrance block
		// Both can not be 0 to be considered a success
		coordinates[0] = 0; // x = 0
		coordinates[1] = 0; // y = 0
		
		if (playerdirection == DungeonGenerator.up) {
			// Search the bottom row
			for (int x = 1; x < getWidth()-1; x++) {
				if (roomblocks.get(x).get(0).getType().getType().equals("ground")) {
					coordinates[0] = x;
					break;
				}
			}
		}
		else if (playerdirection == DungeonGenerator.right) {
			// Search the left row
			for (int y = 1; y < getHeight()-1; y++) {
				if (roomblocks.get(0).get(y).getType().getType().equals("ground")) {
					coordinates[1] = y;
					break;
				}
			}
		}
		else if (playerdirection == DungeonGenerator.down) {
			// Search the top row
			for (int x = 1; x < getWidth()-1; x++) {
				if (roomblocks.get(x).get(getHeight()-1).getType().getType().equals("ground")) {
					coordinates[0] = x;
					break;
				}
			}
		}
		else if (playerdirection == DungeonGenerator.left) {
			// Search the right row
			for (int y = 1; y < getHeight()-1; y++) {
				if (roomblocks.get(getWidth()-1).get(y).getType().getType().equals("ground")) {
					coordinates[1] = y;
					break;
				}
			}
		}
		
		if (coordinates[0] == 0 && coordinates[1] == 0) {
			Gdx.app.log("Room", "There is no fucking entrance here");
			Gdx.app.exit();
		}
		
		return coordinates;
	}

}
