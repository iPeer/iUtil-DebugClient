package com.simple.sjge.entities;

import java.util.Random;

import com.simple.sjge.ai.packages.LeftRightAI;
import com.simple.sjge.level.Level;

public class AITestEntity extends Entity {

	public AITestEntity(int x, int y, int w, int h, Level level) {
		super(x, y, w, h, level);
		left = new Random().nextBoolean();
		setAIPackage(new LeftRightAI(this));
	}
	
	public void tick() {
		super.tick();
	}

}
