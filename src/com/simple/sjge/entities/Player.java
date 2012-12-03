package com.simple.sjge.entities;

import com.simple.sjge.level.Level;


public class Player extends Entity {

	public Player(int x, int y, int w, int h, Level level) {
		super(x, y, w, h, level);
	}
	
	public void tick() {
		super.tick();
	}
	
	public void collidedY() {
		if (engine.input.up.down)
			move(x, y+1, w, h, false);
		else
			move(x, y-1, w, h, false);
	}
	
	public void collidedX() {
		if (engine.input.left.down)
			move(x+1, y, w, h, false);
		else
			move(x-1, y, w, h, false);
	}

}
