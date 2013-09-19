package rltut;

import java.awt.Color;

import asciiPanel.AsciiPanel;

public enum Tile {
	CAVE_FLOOR((char)250, AsciiPanel.yellow, "A dirt and rock cave floor."),
	DUNGEON_FLOOR((char)250, AsciiPanel.yellow, "A stone dungeon floor."),
	CAVE_WALL('#', AsciiPanel.yellow, "A dirt and rock cave wall."),
	DUNGEON_WALL((char)177, AsciiPanel.yellow, "A solid dungeon wall."),
	BOUNDS('x', AsciiPanel.brightBlack, "Beyond the edge of the world."),
	STAIRS_DOWN('>', AsciiPanel.white, "A stone staircase that goes down."),
	STAIRS_UP('<', AsciiPanel.white, "A stone staircase that goes up."),
	UNKNOWN(' ', AsciiPanel.white, "(unknown)");
	
	private char glyph;
	public char glyph() { return glyph; }
	
	private Color color;
	public Color color() { return color; }

	private String description;
	public String details(){ return description; }
	
	Tile(char glyph, Color color, String description){
		this.glyph = glyph;
		this.color = color;
		this.description = description;
	}

	public boolean isGround() {
		return this != CAVE_WALL && this != BOUNDS && this != DUNGEON_WALL;
	}

	public boolean isDiggable() {
		return this == Tile.CAVE_WALL;
	}
}
