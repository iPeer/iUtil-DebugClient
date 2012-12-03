package com.simple.sjge.engine;

import java.awt.Component;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Keyboard extends KeyEvent implements KeyListener {

	private static final long serialVersionUID = -2669482104921708121L;
	private static boolean[] keys = new boolean[1000];
	
	public static int F3 = 114, console = 192;
	
	private Engine engine;

	public Keyboard(Component arg0, int arg1, long arg2, int arg3, int arg4,
			char arg5) {
		super(arg0, arg1, arg2, arg3, arg4, arg5);
	}

	public Keyboard(Component arg0, int arg1, long arg2, int arg3, int arg4,
			char arg5, int arg6) {
		super(arg0, arg1, arg2, arg3, arg4, arg5, arg6);
	}
	
	public void init(Engine engine) {
		this.engine = engine;
		engine.addKeyListener(this);
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}
	
	public static void toggle(KeyEvent e, boolean b) {
		int i = e.getKeyCode();
		keys[i] = b;
	}

	
	public static boolean isKeyDown(int id) {
		return keys[id];
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		toggle(e, true);
	}

	@Override
	public void keyReleased(KeyEvent e) {
		toggle(e, false);
	}

}
