package com.simple.sjge.entities;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.util.Random;

import com.simple.sjge.engine.Engine;
import com.simple.sjge.gfx.Colour;
import com.simple.sjge.level.Level;

public class RotatingDamagingTestEntity extends DamagingTestEntity {
	
	public RotatingDamagingTestEntity(int x, int y, int w, int h, Level level) {
		super(x, y, w, h, level);
	}
	
	public RotatingDamagingTestEntity(int i, int j, int k, int l, Level level, int m, int n) {
		super(i, j, k, l, level, m, n);
	}
	
	public RotatingDamagingTestEntity(int i, int j, int k, int l, Level level, int m, int n, int o) {
		super(i, j, k, l, level, m, n, 50);
	}
	
	public RotatingDamagingTestEntity(int i, int j, int k, int l, Level level, int m, int n, int o, int p) {
		super(i, j, k, l, level, m, n, o, p);
	}

	public void tick() {
		super.tick();
	}
	
	public void render() {
		Graphics2D g = Engine.getGraphicsInstance();
		AffineTransform old = g.getTransform();
		super.render();
		if (!targets.isEmpty()) {
			double angle = getAngle(targets.get(0));
			g.rotate(angle, (this.x + (this.w / 2)) + level.xOffset, (this.y + (this.h / 2) + level.yOffset));
		}
		g.setColor(Colour.CYAN);
		g.drawRect(this.x + level.xOffset, this.y + level.yOffset, this.w, this.h);
		g.drawLine((this.x + (this.w / 2) + level.xOffset), (this.y + (this.h / 2) + level.yOffset), (this.x + (this.w / 2) + level.xOffset), this.y + level.yOffset);
		g.setTransform(old);
	}

}
