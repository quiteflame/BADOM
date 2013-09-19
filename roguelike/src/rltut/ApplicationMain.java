package rltut;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JFrame;

import rltut.screens.Screen;
import rltut.screens.StartScreen;
import asciiPanel.AsciiPanel;

public class ApplicationMain extends JFrame implements KeyListener, MouseListener, MouseMotionListener {
	private static final long serialVersionUID = 1060623638149583738L;
	
	private AsciiPanel terminal;
	private Screen screen;
	
	public ApplicationMain(){
		super();
		terminal = new AsciiPanel();
		add(terminal);
		pack();
		screen = new StartScreen();
		addKeyListener(this);
		addMouseListener(this);
		addMouseMotionListener(this);
		repaint();
	}
	
	@Override
	public void repaint(){
		terminal.clear();
		screen.displayOutput(terminal);
		super.repaint();
	}

	@Override
	public void keyPressed(KeyEvent e) {
		screen = screen.respondToUserInput(e);
		repaint();
	}

	@Override
	public void keyReleased(KeyEvent e) { }

	@Override
	public void keyTyped(KeyEvent e) { }
	
	public static void main(String[] args) {
		ApplicationMain app = new ApplicationMain();
		app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		app.setVisible(true);
	}

	@Override
	public void mouseDragged(MouseEvent e) {
				
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		screen = screen.respondToMouseMove(e.getXOnScreen() - getX(), e.getYOnScreen() - getY());
		repaint();
		System.out.println("Mouse pos: x="+(e.getXOnScreen() - getX())+", y="+(e.getYOnScreen() - getY()));
	}

	@Override
	public void mouseClicked(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	public void mousePressed(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}
}
