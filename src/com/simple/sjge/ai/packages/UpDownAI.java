package com.simple.sjge.ai.packages;

import com.simple.sjge.entities.Entity;

public class UpDownAI extends AI {

	public UpDownAI(Entity e) {
		super(e);
	}
	
	public void tick() {
		if (entity.up)
			entity.move(entity.x, entity.y + 1, entity.w, entity.h);
		else
			entity.move(entity.x, entity.y - 1, entity.w, entity.h);
	}
	
	public void collidedY() {
		if (entity.up)
			entity.move(entity.x, entity.y - 1, entity.w, entity.h, false);
		else
			entity.move(entity.x, entity.y + 1, entity.w, entity.h, false);
		boolean a = entity.up;
		entity.up = !a;
	}

}
