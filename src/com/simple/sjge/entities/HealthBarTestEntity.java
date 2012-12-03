package com.simple.sjge.entities;

import com.simple.sjge.entities.bars.HealthBar;
import com.simple.sjge.level.Level;

public class HealthBarTestEntity extends Entity {
	
	HealthBar healthbar;
	private int dir = 0;

	public HealthBarTestEntity(int x, int y, int w, int h, Level level) {
		super(x, y, w, h, level);
		health = 100;
		healthbar = new HealthBar(this, health);
	}
	
	public void tick() {
		if (dir == 0) {
			this.health--;
			if (this.health <= 0)
				dir = 1;
		}
		if (dir == 1) {
			this.health++;
			if (this.health >= 100)
				dir = 0;
		}
		super.tick();
		
	}
	
	public void render() {
		healthbar.render();
		super.render();
	}
	
	

}
