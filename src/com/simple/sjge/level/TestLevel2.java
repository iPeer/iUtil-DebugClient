package com.simple.sjge.level;

import com.simple.sjge.collision.BBox;
import com.simple.sjge.gfx.Screen;

public class TestLevel2 extends Level {

	public TestLevel2(int width, int height, Screen screen) {
		super(width, height, screen);
		generate();
	}

	public void generate() {
		addBBox(new BBox(0, 0, width - 1, height - 1));

	}

	public void render() {
		super.render();
	}

	public void tick() {
		super.tick();
	}

}
