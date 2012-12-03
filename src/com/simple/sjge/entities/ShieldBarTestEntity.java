package com.simple.sjge.entities;

import com.simple.sjge.ai.packages.UpDownAI;
import com.simple.sjge.entities.bars.HealthBar;
import com.simple.sjge.entities.bars.ShieldBar;
import com.simple.sjge.entities.extensions.Shield;
import com.simple.sjge.level.Level;

public class ShieldBarTestEntity extends Entity {

	HealthBar healthbar;
	ShieldBar shieldBar;
	private int dir = 0;
	Shield shield;

	public ShieldBarTestEntity(int x, int y, int w, int h, Level level) {
		super(x, y, w, h, level);
		healthbar = new HealthBar(this, 1000);
		shield = new Shield(this, 100, 0, 120);
		shieldBar = new ShieldBar(shield);
		setAIPackage(new UpDownAI(this));
	}


	public void tick() {
		shield.tick();
		if (dir == 0) {
			this.damage(1, this);
			if (this.health <= 0)
				dir = 1;
		}
		if (dir == 1) {
			this.health++;
			if (this.health >= 1000)
				dir = 0;
		}
		super.tick();

	}

	public Shield getShield() {
		return shield;
	}

	public void render() {
		if (mouseOver()) {
			healthbar.render();
			shieldBar.render();
		}
		super.render();
	}



}
