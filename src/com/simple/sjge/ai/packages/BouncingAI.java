package com.simple.sjge.ai.packages;

import com.simple.sjge.entities.Entity;

public class BouncingAI extends AI {

	public BouncingAI(Entity e) {
		super(e);
	}
	
	public void tick() {
		if (entity.up)
			entity.move(entity.x, entity.y + 1, entity.w, entity.h);
		else
			entity.move(entity.x, entity.y - 1, entity.w, entity.h);
		
		if (entity.left)
			entity.move(entity.x - 1, entity.y, entity.w, entity.h);
		else
			entity.move(entity.x + 1, entity.y, entity.w, entity.h);
	}
	
	public void collidedY() {
		if (entity.up)
			entity.move(entity.x, entity.y - 1, entity.w, entity.h, false);
		else
			entity.move(entity.x, entity.y + 1, entity.w, entity.h, false);
		boolean a = entity.up;
		entity.up = !a;
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
