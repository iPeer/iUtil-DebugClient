package com.simple.sjge.ai.packages;

import com.simple.sjge.entities.Entity;

public class LeftRightAI extends AI {

	public LeftRightAI(Entity e) {
		super(e);
	}

	public void tick() {
		if (entity.left)
			entity.move(entity.x - 1, entity.y, entity.w, entity.h);
		else
			entity.move(entity.x + 1, entity.y, entity.w, entity.h);
	}
	
	public void collidedX() {
		if (!entity.left)
			entity.move(entity.x - 1, entity.y, entity.w, entity.h, false);
		else
			entity.move(entity.x + 1, entity.y, entity.w, entity.h, false);
		boolean a = entity.left;
		entity.left = !a;
	}
	
	
}
