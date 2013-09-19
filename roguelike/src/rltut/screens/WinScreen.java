package rltut.screens;

import java.awt.event.KeyEvent;

import rltut.World.WorldType;
import asciiPanel.AsciiPanel;

public class WinScreen implements Screen {

	@Override
	public void displayOutput(AsciiPanel terminal) {
		terminal.write("You won.", 1, 1);
		terminal.writeCenter("a) start in dungeon", 22);
		terminal.writeCenter("b) start in caves", 23);
	}

	@Override
	public Screen respondToUserInput(KeyEvent key) {
		WorldType type = WorldType.UNKNOWN;

		switch (key.getKeyCode()) {
		case KeyEvent.VK_A:
			type = WorldType.DUNGEON;
			break;
		case KeyEvent.VK_B:
			type = WorldType.CAVES;
			break;
		default:
			return this;
		}

		return new PlayScreen(type);
	}

	@Override
	public Screen respondToMouseMove(int x, int y) {
		return this;
	}
}
