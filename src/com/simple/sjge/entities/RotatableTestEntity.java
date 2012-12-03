package com.simple.sjge.entities;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import com.simple.sjge.engine.Engine;
import com.simple.sjge.gfx.Colour;
import com.simple.sjge.level.Level;

public class RotatableTestEntity extends Entity {

	public RotatableTestEntity(int x, int y, int w, int h, Level level) {
		super(x, y, w, h, level);
	}

	public void tick() { super.tick(); }

	public void render() {
		Graphics2D g = Engine.getGraphicsInstance();
		AffineTransform old = g.getTransform();
//		if (Engine.DEBUG_ENABLED) {
//			g.drawLine((this.x + level.xOffset)  + (this.w / 2), (this.y + level.yOffset) + (this.h / 2), Engine.Mouse.x, Engine.Mouse.y);
//		}
		try {
			double angle = getAngle(Engine.Mouse.x, Engine.Mouse.y);
			g.rotate(angle, (this.x + (this.w / 2)) + level.xOffset, (this.y + (this.h / 2) + level.yOffset));
		}
		catch (NullPointerException e) { }
		g.setColor(Colour.CYAN);
		g.drawRect(this.x + level.xOffset, this.y + level.yOffset, this.w, this.h);
		g.drawLine((this.x + (this.w / 2) + level.xOffset), (this.y + (this.h / 2) + level.yOffset), (this.x + (this.w / 2) + level.xOffset), this.y + level.yOffset);
		g.setTransform(old);
	}

}
