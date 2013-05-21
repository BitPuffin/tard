package com.thelonelybyte.tard;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Logger;

public class DungeonGenerator {
	private Random rand;
	
	private static final int up = 0;
	private static final int right = 1;
	private static final int down = 2;
	private static final int left = 3;
	
	private BlockType walltype;
	private BlockType groundtype;
	private BlockType stairsuptype;
	private BlockType stairsdowntype;
	
	public DungeonGenerator() {
		rand = new Random();
	}
	
	public Dungeon genDungeon(BlockType w, BlockType g) {
		Gdx.app.log("dungeon generator", "Generating dungeon, wall type is " + w.getType() + " and ground type is " + g.getType());
		walltype = w;
		groundtype = g;
		
		Dungeon d;
		
		int roomcount = rand.nextInt(rand.nextInt(100)+1)+1;
		Gdx.app.log("dungeon generator", "Room count: " + roomcount);
		
		Array<Array<Array<Block>>> rooms = new Array<Array<Array<Block>>>(roomcount);
		
		for(int i = 0; i < rooms.size; i++) {
			rooms.insert(i, genRoom());
			Gdx.app.log("dungeon generator", "room inserted at index " + i);
		}
		
		
		// assign something
		return d;
	}
	
	private Array<Array<Block>> genRoom() {
		Gdx.app.log("dungeon generator", "Generating room...");
		
		int doors = rand.nextInt(5) + 1;
		Gdx.app.log("dungeon generator", "Door count for this room: " + doors);
		
		int width = rand.nextInt(60);
		if (width < 6) {
			width = 6;
		}
		Gdx.app.log("dungeon generator", "Room width: " + width);
		
		int height = rand.nextInt(60);
		if (height < 6) {
			height = 6;
		}
		Gdx.app.log("dungeon generator", "Room height: " + height);
		
		Array<Array<Block>> room = new Array<Array<Block>>();
		
		Gdx.app.log("dungeon generator", "Generating the room without doors");
		// Closed room!
		for(int x = 0; x < width; x++) {
			room.insert(x, new Array<Block>());
			for (int y = 0; y < height; y++) {
				if (x == 0 || x == width-1 || y == 0 || y == height-1) {
					room.get(x).insert(y, new Block(walltype));
				} else {
					room.get(x).insert(y, new Block(groundtype));
				}
			}
		}
		Gdx.app.log("dungeon generator", "Done generating closed room!");
		
		Gdx.app.log("dungeon generator", "Adding doors...");
		int doorsadded = 0;
		
		final int mindoorchance = 10;
		final int doorhigh = 100;
		
		// Insert doors
		while (doorsadded < doors) {
			// loop around the walls of the room and randomly instert openings
			// skip at 0 and max
			for (int x = 1; x < width-1; x++) {
				if(tryInsert(room.get(x).get(0), groundtype, doorsadded, doors, mindoorchance, doorhigh)) {
					doorsadded++;
					if (doorsadded == doors) {
						break;
					}
				}
			}
			for (int x = 1; x < width-1; x++) {
				if(tryInsert(room.get(x).get(height-1), groundtype, doorsadded, doors, mindoorchance, doorhigh)) {
					doorsadded++;
					if (doorsadded == doors) {
						break;
					}
				}
			}
			for (int y = 1; y < height-1; y++) {
				if(tryInsert(room.get(0).get(y), groundtype, doorsadded, doors, mindoorchance, doorhigh)) {
					doorsadded++;
					if (doorsadded == doors) {
						break;
					}
				}
			}
			for (int y = 1; y < height-1; y++) {
				if(tryInsert(room.get(height-1).get(y), groundtype, doorsadded, doors, mindoorchance, doorhigh)) {
					doorsadded++;
					if (doorsadded == doors) {
						break;
					}
				}
			}
		}
		Gdx.app.log("dungeon generator", "Finished generating doors, thus done with generating room.");
		
		return room;
	}
	
	// Just generate a straight corridor
	private Array<Array<Block>> genCorridor(int direction) {
		Gdx.app.log("dungeon generator", "Generating a " + (direction == up || direction == down ? "vertical" : "horizontal") + "corridor...");
		int len = rand.nextInt(16);
		if (len < 2) { len = 2; }
		Gdx.app.log("dungeon generator", "The legth of the corridor is: " + len);
		
		Array<Array<Block>> corridor = new Array<Array<Block>>(len);
		
		if (direction == up || direction == down) {
			// vertical
			for (int x = 0; x < 3; x++) {
				corridor.insert(x, new Array<Block>());
				for (int y = 0; y < len; y++) {
					if(x == 0 || x == 2) {
						corridor.get(x).insert(y, new Block(walltype));
					} else {
						corridor.get(x).insert(y, new Block(groundtype));
					}
				}
			}
			
		} else if (direction == left || direction == right) {
			// horizontal
			for (int x = 0; x < len; x++) {
				corridor.insert(x, new Array<Block>());
				for (int y = 0; y < 3; y++) {
					if (y == 0 || y == 2) {
						corridor.get(x).insert(y, new Block(walltype));
					} else {
						corridor.get(x).insert(y, new Block(groundtype));
					}
				}
			}
		}
		
		Gdx.app.log("dungeon generator", "Done generating corridor!");
		
		return corridor;
	}
	
	private Array<Array<Block>> connectRooms(Array<Array<Array<Block>>> rooms) {
		Gdx.app.log("dungeon generator", "Time to connect the rooms...");
		
		boolean[][][] hasBeenConnected = new boolean[rooms.size][][];
		for(int i=0; i < hasBeenConnected.length; i++) {
			hasBeenConnected[i] = new boolean[rooms.get(i).size][];
			
			for(int j=0; j < hasBeenConnected[i].length; j++) {
				hasBeenConnected[i][j] = new boolean[rooms.get(i).get(j).size];
				for(int k=0; k < hasBeenConnected[i][j].length; k++) {
					hasBeenConnected[i][j][k] = false;
				}
			}
		}
		
		for(int i=0; i < rooms.size; i++) {
			Array<Integer[]> openings = findOpenings(rooms.get(i));
			//for()
		}
		
	}
	
	private Array<Integer[]> findOpenings(Array<Array<Block>> room) {
		Gdx.app.log("dungeon generator", "Finding the openings of this room...");
		
		Array<Integer[]> coordinates = new Array<Integer[]>();
		int coordindex = -1;
		
		for(int x = 1; x < room.size-1; x++) {
			if (room.get(x).get(0).getType().equals(groundtype)) {
				coordindex++;
				coordinates.add(new Integer[3]); // x, y, direction
				coordinates.get(coordindex)[0] = x;
				coordinates.get(coordindex)[1] = 0;
				coordinates.get(coordindex)[2] = up;
			}
		}
		int ysize = room.get(0).size;
		for(int x = 1; x < room.size-1; x++) {
			if (room.get(x).get(ysize-1).getType().equals(groundtype)) {
				coordindex++;
				coordinates.add(new Integer[3]); // x, y, direction
				coordinates.get(coordindex)[0] = x;
				coordinates.get(coordindex)[1] = ysize-1;
				coordinates.get(coordindex)[2] = down;
			}
		}
		for(int y = 1; y < ysize-1; y++) {
			if(room.get(0).get(y).getType().equals(groundtype)) {
				coordindex++;
				coordinates.add(new Integer[3]);
				coordinates.get(coordindex)[0] = 0;
				coordinates.get(coordindex)[1] = y;
				coordinates.get(coordindex)[2] = left;
			}
		}
		int xsize = room.size;
		for(int y = 1; y < ysize-1; y++) {
			if(room.get(xsize-1).get(y).getType().equals(groundtype)) {
				coordindex++;
				coordinates.add(new Integer[3]);
				coordinates.get(coordindex)[0] = 0;
				coordinates.get(coordindex)[1] = y;
				coordinates.get(coordindex)[2] = right;
			}
		}
		
		Gdx.app.log("dungeon generator", "Done searching for openings/doors");
		
		return coordinates;
	}
	
	
	private boolean tryInsert(Block current, BlockType target, int inserted, int max, int min, int high) {
		if((rand.nextInt(high) < min) && (inserted <= max) && (!current.getType().equals(target))) {
			Gdx.app.log("dungeon generator", "Inserting door!");
			current = new Block(target);
			return true;
		} else {
			return false;
		}
	}
}
