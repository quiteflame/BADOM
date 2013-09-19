package rltut.screens;

import java.awt.event.KeyEvent;

import rltut.Creature;
import rltut.Item;
import rltut.Tile;
import asciiPanel.AsciiPanel;

public class MouseLookScreen extends TargetBasedScreen {

	public MouseLookScreen(Creature player, String caption, int sx, int sy) {
		super(player, caption, sx, sy);
	}
	
	public void enterWorldCoordinate(int x, int y, int screenX, int screenY) {
		Creature creature = player.creature(x, y, player.z);
		if (creature != null){
			caption = creature.glyph() + " "  + creature.name() + creature.details();
			return;
		}
		
		Item item = player.item(x, y, player.z);
		if (item != null){
			caption = item.glyph() + " "  + player.nameOf(item) + item.details();
			return;
		}
		System.out.println("x="+x+", y="+y);
		Tile tile = player.tile(x, y, player.z);
		caption = tile.glyph() + " " + tile.details();
		System.out.println(caption);
	}
	
	@Override
	public void displayOutput(AsciiPanel terminal) {
		terminal.clear(' ', 0, 23, 80, 1);
		terminal.write(caption, 0, 23);
	}
	
	@Override
	public Screen respondToUserInput(KeyEvent key) {
		return null;
	}

}
