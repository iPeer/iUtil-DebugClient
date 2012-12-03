package com.simple.sjge.engine;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Arrays;

import com.simple.sjge.util.Debug;

public class KeyboardHandler implements KeyListener {

	public class Key {

		public boolean down, pressed, pressed1;
		public int presses, multi;

		public Key() {
			keys.add(this);
		}

		public void toggle(boolean flag) {
			if (down != flag)
				down = flag;
			if (flag)
				presses++;
		}

		public void tick() {
			if (multi < presses) {
				multi++;
				pressed1 = true;
			} else {
				pressed1 = false;
			}
		}
	}

	public ArrayList<Key> keys;
	public Key left;
	public Key right;
	public Key up;
	public Key down;
	public Key jump;
	public Key debug;
	public Key rendering;
	public Key console;
	public Key quit;
	public Key pause;
	private Engine engine;

	public long lastDebugPress = 0L;

	public KeyboardHandler(Engine engine) {
		this.engine = engine;
		keys = new ArrayList<Key>();

		left = new Key();
		right = new Key();
		up = new Key();
		down = new Key();

		pause = new Key();

		debug = new Key();
		quit = new Key();
		console = new Key();
		engine.addKeyListener(this);
		Debug.p("Added Key listener.");
	}


	public void tick() {
		for (int x = 0; x < keys.size(); x++) {
			keys.get(x).tick();
		}
	}

	public void releaseAll() {
		for (int x = 0; x < keys.size(); x++) {
			keys.get(x).down = false;
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
//		if (engine.currentGui instanceof GuiConsole)
//			toggle(arg0, true);
		if (engine.currentGui != null)
			engine.currentGui.keyTyped(e);
		else
			Keyboard.toggle(e, true);
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		Keyboard.toggle(arg0, false);
	}

	@Override
	public void keyTyped(KeyEvent arg0) {

	}

	@SuppressWarnings("unused")
	private void toggle(KeyEvent e, boolean flag) {
		int k = e.getKeyCode();
		if (Arrays.asList(Keyboard.VK_Q, Keyboard.VK_ESCAPE).contains(k))
			quit.toggle(flag);
		if (k == Keyboard.VK_F3)
			debug.toggle(flag);

		if (Arrays.asList(Keyboard.VK_P).contains(k))
			pause.toggle(flag);
		
		if (Arrays.asList(Keyboard.VK_BACK_QUOTE, Keyboard.VK_DEAD_TILDE).contains(k))
			console.toggle(flag);

		if (Arrays.asList(Keyboard.VK_UP, Keyboard.VK_W, Keyboard.VK_NUMPAD8).contains(k))
			up.toggle(flag);
		if (Arrays.asList(Keyboard.VK_RIGHT, Keyboard.VK_D, Keyboard.VK_NUMPAD6).contains(k))
			right.toggle(flag);
		if (Arrays.asList(Keyboard.VK_LEFT, Keyboard.VK_A, Keyboard.VK_NUMPAD4).contains(k))
			left.toggle(flag);
		if (Arrays.asList(Keyboard.VK_DOWN, Keyboard.VK_S, Keyboard.VK_NUMPAD2).contains(k))
			down.toggle(flag);
	}

}
