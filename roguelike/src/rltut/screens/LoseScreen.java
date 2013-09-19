package rltut.screens;

import java.awt.event.KeyEvent;

import rltut.Creature;
import rltut.World.WorldType;
import asciiPanel.AsciiPanel;

public class LoseScreen implements Screen {
	private Creature player;

	public LoseScreen(Creature player) {
		this.player = player;
	}

	@Override
	public void displayOutput(AsciiPanel terminal) {
		terminal.writeCenter("R.I.P.", 3);
		terminal.writeCenter(player.causeOfDeath(), 5);
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
