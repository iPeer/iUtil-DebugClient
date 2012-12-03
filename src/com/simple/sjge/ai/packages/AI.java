package com.simple.sjge.ai.packages;

import com.simple.sjge.entities.Entity;
import com.simple.sjge.level.Level;

public class AI {
	
	Entity entity;
	Level level;

	public AI(Entity e) {
		this.entity = e;
		this.level = e.level;
	}
	
	public void tick() { }

	public void collidedX() { }
	
	public void collidedY() { }
	
}
