package com.thelonelybyte.tard;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;
import com.badlogic.gdx.utils.IntIntMap;
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
		
		Array<Array<Array<Block>>> rooms = new Array<Array<Array<Block>>>();
		
		for(int i = 0; i < roomcount; i++) {
			rooms.insert(i, genRoom());
			Gdx.app.log("dungeon generator", "room inserted at index " + i);
		}
		
		d = new Dungeon(connectRooms(rooms));
		rooms = null; // Yes hi I would like to be garbage collected asap
		
		
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
			Gdx.app.log("dungeon generator", "x row at y = 0");
			for (int x = 1; x < width-1; x++) {
				if(tryInsert(room.get(x).get(0), groundtype, doorsadded, doors, mindoorchance, doorhigh)) {
					doorsadded++;
					if (doorsadded == doors) {
						break;
					}
				}
			}
			
			Gdx.app.log("dungeon generator", "x row at y = height-1");
			for (int x = 1; x < width-1; x++) {
				if(tryInsert(room.get(x).get(height-1), groundtype, doorsadded, doors, mindoorchance, doorhigh)) {
					doorsadded++;
					if (doorsadded == doors) {
						break;
					}
				}
			}
			Gdx.app.log("dungeon generator", "y to height -1 row at x = 0");
			for (int y = 1; y < height-1; y++) {
				if(tryInsert(room.get(0).get(y), groundtype, doorsadded, doors, mindoorchance, doorhigh)) {
					doorsadded++;
					if (doorsadded == doors) {
						break;
					}
				}
			}
			
			Gdx.app.log("dungeon generator", "y to height -1 row at x = width-1");
			for (int y = 1; y < height-1; y++) {
				if(tryInsert(room.get(width-1).get(y), groundtype, doorsadded, doors, mindoorchance, doorhigh)) {
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
	// Not actually used though hohohohohohohohohohohohoh
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
	
	private Array<Room> connectRooms(Array<Array<Array<Block>>> rooms) {
		Gdx.app.log("dungeon generator", "Time to connect the "+ rooms.size +" rooms...");
		
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
		
		boolean connectedside[][] = new boolean[rooms.size][4];
		for(int i = 0; i < connectedside.length; i++) {
			connectedside[i][up] 	= false;
			connectedside[i][right] = false;
			connectedside[i][down] 	= false;
			connectedside[i][left] 	= false;
		}
		
		int connectedto[][] = new int[rooms.size][4]; // connectedto[roomisconnectedto][other4roomsides]
		for (int i = 0; i < connectedto.length; i++) {
			connectedto[i][up] 		= -1;
			connectedto[i][right] 	= -1;
			connectedto[i][down] 	= -1;
			connectedto[i][left] 	= -1;
		}

		// This is gonna get messy
		// And innefficient!
		// It loops through all openings of the rooms and tries to connect it to 4 other rooms
		for(int room1=0; room1 < rooms.size; room1++) {
			Array<Integer[]> openings = findOpenings(rooms.get(room1));
			
			for (int room1opening = 0; room1opening < openings.size; room1opening++) {
				
				for (int room2 = 0; room2 < rooms.size; room2++) {
					if (room2 == room1) { continue; }
					Array<Integer[]> otheropenings = findOpenings(rooms.get(room2));
					
					for (int room2opening = 0; room2opening < otheropenings.size; room2opening++) {
						if (((openings.get(room1opening)[2] == up) && (otheropenings.get(room2opening)[2] == down))) {
							if(!connectedside[room1][up] && !connectedside[room2][down]) {
								connectedside[room1][up] 	= true;
								connectedto[room1][up] 		= room2;
								connectedside[room2][down] 	= true;
								connectedto[room2][down] 	= room1;
								hasBeenConnected[room1][openings.get(room1opening)[0]][openings.get(room1opening)[1]] = true;
								hasBeenConnected[room2][openings.get(room2opening)[0]][openings.get(room2opening)[1]] = true;
							}
						}
						
						if (((openings.get(room1opening)[2] == down) && (otheropenings.get(room2opening)[2] == up))) {
							if(!connectedside[room1][down] && !connectedside[room2][up]) {
								connectedside[room1][down] 	= true;
								connectedto[room1][down] 	= room2;
								connectedside[room2][up] 	= true;
								connectedto[room2][up] 		= room1;
								hasBeenConnected[room1][openings.get(room1opening)[0]][openings.get(room1opening)[1]] = true;
								hasBeenConnected[room2][openings.get(room2opening)[0]][openings.get(room2opening)[1]] = true;
							}
						}
						
						if (((openings.get(room1opening)[2] == left) && (otheropenings.get(room2opening)[2] == right))) {
							if(!connectedside[room1][left] && !connectedside[room2][right]) {
								connectedside[room1][left] 	= true;
								connectedto[room1][left] 	= room2;
								connectedside[room2][right] = true;
								connectedto[room2][right] 	= room1;
								hasBeenConnected[room1][openings.get(room1opening)[0]][openings.get(room1opening)[1]] = true;
								hasBeenConnected[room2][openings.get(room2opening)[0]][openings.get(room2opening)[1]] = true;
							}
						}
						
						if (((openings.get(room1opening)[2] == right) && (otheropenings.get(room2opening)[2] == left))) {
							if(!connectedside[room1][right] && !connectedside[room2][left]) {
								connectedside[room1][right] = true;
								connectedto[room1][right] 	= room2;
								connectedside[room2][left] 	= true;
								connectedto[room2][left] 	= room1;
								hasBeenConnected[room1][openings.get(room1opening)[0]][openings.get(room1opening)[1]] = true;
								hasBeenConnected[room2][openings.get(room2opening)[0]][openings.get(room2opening)[1]] = true;
							}
						}
					}
					
				}
				
			}
		}
		
		int openingcount = 0;
		// And again let's get a little messy!
		// We are looping through to fill the holes that didn't get connected
		for (int roomindex = 0; roomindex < rooms.size; roomindex++) {
			Array<Integer[]> openings = findOpenings(rooms.get(roomindex));
			openingcount += openings.size;
			for(int opening = 0; opening < openings.size; opening++) {
				int x = openings.get(opening)[0];
				int y = openings.get(opening)[1];
				if (!hasBeenConnected[roomindex][x][y]) {
					rooms.get(roomindex).get(x).insert(y, new Block(walltype));
				}
			}
		}
		
		// Create y lists, one for each  x and y in each direction
//		Array<Block> x1 = new Array<Block>(); 	// +x
//		Array<Block> x2 = new Array<Block>(); 	// -x 
//		Array<Block> y1 = new Array<Block>();	// +y
//		Array<Block> y2 = new Array<Block>();	// -y
//		int currentx = 0;	// track our steps
//		int currenty = 0;	// ditto
//		
//		int openingcounter = 0;
//		for(int roomindex = 0; roomindex < rooms.size; roomindex++) {
//			if (openingcounter < openingcount) {
//				Array<Integer[]> openings = findOpenings(rooms.get(roomindex));
//				
//			}
//		}
		
		Array<Array<Array<Block>>> finalrooms = new Array<Array<Array<Block>>>();
		IntIntMap newindextoold = new IntIntMap();
		IntIntMap oldindextonew = new IntIntMap();
		
		int newindex = 0;
		for(int roomindex = 0; roomindex < rooms.size; roomindex++) {	
			if(!isConnected(connectedside[roomindex]) && !finalrooms.contains(rooms.get(roomindex), false)) {
				finalrooms.insert(newindex, rooms.get(roomindex));
				oldindextonew.put(roomindex, newindex);
				newindextoold.put(newindex, roomindex);
				
				newindex++;
			}
		}
		
		int finalconnectedto[][] = new int[finalrooms.size][4]; // connectedto[roomisconnectedto][other4roomsides]
		for (int i = 0; i < finalconnectedto.length; i++) {
			finalconnectedto[i][up] 	= -1;
			finalconnectedto[i][right] 	= -1;
			finalconnectedto[i][down] 	= -1;
			finalconnectedto[i][left] 	= -1;
		}
		
		Array<Room> returnme = new Array<Room>();
		// Helt efterblivet men orkar inte nu
		for(int roomindex = 0; roomindex < finalrooms.size; roomindex++) {
			int[] c 	= new int[4];
			c[up] 		= oldindextonew.get(connectedto[newindextoold.get(roomindex, -1)][up]	, -1);
			c[right] 	= oldindextonew.get(connectedto[newindextoold.get(roomindex, -1)][right], -1);
			c[down] 	= oldindextonew.get(connectedto[newindextoold.get(roomindex, -1)][down]	, -1);
			c[left] 	= oldindextonew.get(connectedto[newindextoold.get(roomindex, -1)][left]	, -1);
			
			Room room 	= new Room(finalrooms.get(roomindex), c);
			returnme.insert(roomindex, room);
		}
		
		Gdx.app.log("dungeon generator", "Done connecting rooms");
		
		return returnme;
		
	}
	
	private boolean isConnected(boolean[] sideconnected) {
		return sideconnected[up] || sideconnected[right] || sideconnected[down] || sideconnected[left];
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
