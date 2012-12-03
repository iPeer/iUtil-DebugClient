package com.simple.sjge.entities;

import com.simple.sjge.ai.packages.PathAI;
import com.simple.sjge.level.Level;

public class PathAITestEntity extends Entity {

	public PathAITestEntity(int x, int y, int w, int h, Level level) {
		super(x, y, w, h, level);
		setAIPackage(new PathAI(this));
	}

	public void tick() { 
		super.tick();
	}

	public void render() {
		super.render();
	}

}
