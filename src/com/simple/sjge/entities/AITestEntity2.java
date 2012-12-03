package com.simple.sjge.entities;

import java.util.Random;

import com.simple.sjge.ai.packages.UpDownAI;
import com.simple.sjge.level.Level;

public class AITestEntity2 extends Entity {

	public AITestEntity2(int x, int y, int w, int h, Level level) {
		super(x, y, w, h, level);
		up = new Random().nextBoolean();
		setAIPackage(new UpDownAI(this));
	}
	
	
	public void tick() {
		super.tick();
	}
	

}
