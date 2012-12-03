package com.simple.sjge.entities;

import java.util.Random;

import com.simple.sjge.level.Level;

public class DamagingTestEntity extends AITestEntity3 {

	public DamagingTestEntity(int x, int y, int w, int h, Level level) {
		super(x, y, w, h, level);
	}
	
	public DamagingTestEntity(int i, int j, int k, int l, Level level, int m, int n) {
		super(i, j, k, l, level, m, n);
	}
	
	public DamagingTestEntity(int i, int j, int k, int l, Level level, int m, int n, int o) {
		super(i, j, k, l, level, m, n, 50);
	}
	
	public DamagingTestEntity(int i, int j, int k, int l, Level level, int m, int n, int o, int p) {
		super(i, j, k, l, level, m, n, o, p);
	}

	public void tick() {
		super.tick();
		if (damageTick >= damageDelay) {
			damageTick = 0;
			for (Entity e : targets)
				e.damage(new Random().nextInt(20), this);
		}
		else
			damageTick++;
	}

}
