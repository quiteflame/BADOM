package rltut;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class DungeonBuilder {
	
	private static final int ROOM_MAX_SIZE = 10;
	private static final int ROOM_MIN_SIZE = 6;
	private static final int MAX_ROOMS = 30;
	
	private int width;
	private int height;
	private int depth;
	private Tile[][][] tiles;
	private int[][][] regions;
	private int nextRegion;
	
	public DungeonBuilder(int width, int height, int depth) {
		this.width = width;
		this.height = height;
		this.depth = depth;
		this.tiles = new Tile[width][height][depth];
		this.regions = new int[width][height][depth];
		this.nextRegion = 1;
	}

	public World build() {
		return new World(tiles);
	}
	
	private DungeonBuilder addExitStairs() {
		int x = -1;
		int y = -1;
		
		do {
			x = (int)(Math.random() * width);
			y = (int)(Math.random() * height);
		}
		while (tiles[x][y][0] != Tile.DUNGEON_FLOOR);
		
		tiles[x][y][0] = Tile.STAIRS_UP;
		return this;
	}

	public DungeonBuilder makeDungeon() {
		return make_map().createRegions().connectRegions().addExitStairs();
	}
	
	private class Rect {
	    //a rectangle on the map. used to characterize a room.
		
		private int x1;
		private int y1;
		private int x2;
		private int y2;
		
	    public Rect(int x, int y, int w, int h){
	        this.x1 = x;
	        this.y1 = y;
	        this.x2 = x + w;
	        this.y2 = y + h;
	    }
	 
	    public Point center(){
	        int center_x = (this.x1 + this.x2) / 2;
	        int center_y = (this.y1 + this.y2) / 2;
	        return new Point(center_x, center_y);
	    }
	 
	    public boolean intersect(Rect other){
	        //returns true if this rectangle intersects with another one
	        return (this.x1 <= other.x2 && this.x2 >= other.x1 &&
	        		this.y1 <= other.y2 && this.y2 >= other.y1);
	    }
	}
	
	private void create_room(Rect room) {
	    //go through the tiles in the rectangle and make them passable
	    for(int x = room.x1 + 1; x < room.x2; x++){
	        for(int y = room.y1 + 1; y < room.y2; y++){
	        	for(int z = 0; z < depth; z++){
	        		tiles[x][y][z] = Tile.DUNGEON_FLOOR;
	        	}
	        }
	    }
	 }
	 
	private void create_h_tunnel(int x1, int x2, int y) {
	    //horizontal tunnel. min() and max() are used in case x1>x2
	    for(int x = Math.min(x1, x2); x < Math.max(x1, x2) + 1; x++){
	    	for(int z = 0; z < depth; z++){
        		tiles[x][y][z] = Tile.DUNGEON_FLOOR;
        	}
	    }
	}
	 
	private void create_v_tunnel(int y1, int y2, int x) {
	    //vertical tunnel
	    for(int y = Math.min(y1, y2); y < Math.max(y1, y2) + 1; y++){
	    	for(int z = 0; z < depth; z++){
        		tiles[x][y][z] = Tile.DUNGEON_FLOOR;
        	}
	    }
	}
	 
	private DungeonBuilder make_map(){
	    //fill map with "blocked" tiles
	 
	    for (int x = 0; x < this.width; x++) {
			for (int y = 0; y < this.height; y++) {
				for(int z = 0; z < depth; z++){
	        		tiles[x][y][z] = Tile.DUNGEON_WALL;
	        	}
			}
		}        		
	    
	    ArrayList<Rect> rooms = new ArrayList<Rect>();
	    int num_rooms = 0;
	    Random random = new Random();
	    for(int r = 0; r < DungeonBuilder.MAX_ROOMS; r++){
	        //random width and height
	        int w = randomInteger(DungeonBuilder.ROOM_MIN_SIZE, DungeonBuilder.ROOM_MAX_SIZE, random);
	        int h = randomInteger(DungeonBuilder.ROOM_MIN_SIZE, DungeonBuilder.ROOM_MAX_SIZE, random);
	        //random position without going out of the boundaries of the map
	        int x = randomInteger(0, this.width - w - 1, random);
	        int y = randomInteger(0, this.height - h - 1, random);
	 
	        //"Rect" class makes rectangles easier to work with
	        Rect new_room = new Rect(x, y, w, h);
	 
	        //run through the other rooms and see if they intersect with this one
	        boolean failed = false;
	        
	        for (Rect other_room : rooms) {
	        	if(new_room.intersect(other_room)){
	                failed = true;
	                break;
	        	}
			}
	 
	        if(!failed){
	            //this means there are no intersections, so this room is valid
	 
	            //"paint" it to the map's tiles
	            create_room(new_room);
	 
	            //center coordinates of new room, will be useful later
	            Point new_room_center = new_room.center();
	 
	            if(num_rooms == 0){
	                //this is the first room, where the player starts at
//	                player.x = new_x;
//	                player.y = new_y;
	            } else {
	                //all rooms after the first:
	                //connect it to the previous room with a tunnel
	 
	                //center coordinates of previous room
	                Point prev_room_center = rooms.get(num_rooms-1).center();
	 
	                //draw a coin (random number that is either 0 or 1)
	                if(randomInteger(0, 0, random) == 1){
	                    //first move horizontally, then vertically
	                    create_h_tunnel(prev_room_center.x, new_room_center.x, prev_room_center.y);
	                    create_v_tunnel(prev_room_center.y, new_room_center.y, new_room_center.x);
	                }else{
	                    //first move vertically, then horizontally
	                    create_v_tunnel(prev_room_center.y, new_room_center.y, prev_room_center.x);
	                    create_h_tunnel(prev_room_center.x, new_room_center.x, new_room_center.y);
	                }
	            }
	            //finally, append the new room to the list
	            rooms.add(new_room);
	            num_rooms += 1;
	        }
	    }
	    
	    return this;
	}
	
	private DungeonBuilder createRegions(){
		regions = new int[width][height][depth];
		
		for (int z = 0; z < depth; z++){
			for (int x = 0; x < width; x++){
				for (int y = 0; y < height; y++){
					if (tiles[x][y][z] != Tile.DUNGEON_WALL && regions[x][y][z] == 0){
						fillRegion(nextRegion++, x, y, z);
					}
				}
			}
		}
		return this;
	}
	
	private int fillRegion(int region, int x, int y, int z) {
		int size = 1;
		ArrayList<Point> open = new ArrayList<Point>();
		open.add(new Point(x,y,z));
		regions[x][y][z] = region;
		
		while (!open.isEmpty()){
			Point p = open.remove(0);

			for (Point neighbor : p.neighbors8()){
				if (neighbor.x < 0 || neighbor.y < 0 || neighbor.x >= width || neighbor.y >= height)
					continue;
				
				if (regions[neighbor.x][neighbor.y][neighbor.z] > 0
						|| tiles[neighbor.x][neighbor.y][neighbor.z] == Tile.DUNGEON_WALL)
					continue;

				size++;
				regions[neighbor.x][neighbor.y][neighbor.z] = region;
				open.add(neighbor);
			}
		}
		return size;
	}
	
	public DungeonBuilder connectRegions(){
		for (int z = 0; z < depth-1; z++){
			connectRegionsDown(z);
		}
		return this;
	}
	
	private void connectRegionsDown(int z){
		List<Integer> connected = new ArrayList<Integer>();
		
		for (int x = 0; x < width; x++){
			for (int y = 0; y < height; y++){
				int r = regions[x][y][z] * 1000 + regions[x][y][z+1];
				if (tiles[x][y][z] == Tile.DUNGEON_FLOOR
						&& tiles[x][y][z+1] == Tile.DUNGEON_FLOOR
						&& !connected.contains(r)){
					connected.add(r);
					connectRegionsDown(z, regions[x][y][z], regions[x][y][z+1]);
				}
			}
		}
	}
	
	private void connectRegionsDown(int z, int r1, int r2){
		List<Point> candidates = findRegionOverlaps(z, r1, r2);
		
		int stairs = 0;
		do{
			Point p = candidates.remove(0);
			tiles[p.x][p.y][z] = Tile.STAIRS_DOWN;
			tiles[p.x][p.y][z+1] = Tile.STAIRS_UP;
			stairs++;
		}
		while (candidates.size() / stairs > 250);
	}

	public List<Point> findRegionOverlaps(int z, int r1, int r2) {
		ArrayList<Point> candidates = new ArrayList<Point>();
		
		for (int x = 0; x < width; x++){
			for (int y = 0; y < height; y++){
				if (tiles[x][y][z] == Tile.DUNGEON_FLOOR
						&& tiles[x][y][z+1] == Tile.DUNGEON_FLOOR
						&& regions[x][y][z] == r1 
						&& regions[x][y][z+1] == r2){
					candidates.add(new Point(x,y,z));
				}
			}
		}
		
		Collections.shuffle(candidates);
		return candidates;
	}
	
	private int randomInteger(int aStart, int aEnd, Random aRandom){
	    if ( aStart > aEnd ) {
	      throw new IllegalArgumentException("Start cannot exceed End.");
	    }
	    //get the range, casting to long to avoid overflow problems
	    long range = (long)aEnd - (long)aStart + 1;
	    // compute a fraction of the range, 0 <= frac < range
	    long fraction = (long)(range * aRandom.nextDouble());
	    int randomNumber =  (int)(fraction + aStart);    
	    
	    return randomNumber;
	}
}
