package com.simple.sjge.entities;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.util.ArrayList;

import com.simple.sjge.ai.packages.TargetingAI;
import com.simple.sjge.collision.BBox;
import com.simple.sjge.engine.Engine;
import com.simple.sjge.gfx.Colour;
import com.simple.sjge.level.Level;

public class AITestEntity3 extends Entity {

	public AITestEntity3(int x, int y, int w, int h, Level level) {
		this(x, y, w, h, level, 100, 100);
	}	
	public AITestEntity3(int x, int y, int w, int h, Level level, int rangeX) {
		this(x, y, w, h, level, rangeX, rangeX);
	}
	public AITestEntity3(int x, int y, int w, int h, Level level, int rangeX, int rangeY) {
		this(x, y, w, h, level, rangeX, rangeY, 1);
	}
	
	public AITestEntity3(int x, int y, int w, int h, Level level, int rangeX, int rangeY, int targets) {
		this(x, y, w, h, level, rangeX, rangeY, targets, 50);
	}

	public AITestEntity3(int x, int y, int w, int h, Level level, int rangeX, int rangeY, int targets, int damageTimer) {
		super(x, y, w, h, level);
		this.rangeX = rangeX;
		this.rangeY = rangeY;
		this.aof = new BBox((x + (w / 2)) - (this.rangeX / 2), ((y + (h / 2)) - (this.rangeY / 2)), this.rangeX, this.rangeY);
		this.targets = new ArrayList<Entity>();
		this.numTargets = targets;
		this.damageDelay = damageTimer;
		setAIPackage(new TargetingAI(this));
	}

	public void tick() {
		super.tick();
	}

	public void render() {

		Graphics2D g = Engine.getGraphicsInstance();

		if (Engine.DEBUG_ENABLED) {
			for (Entity target : this.targets) {
				g.setColor(Colour.GREEN);
				g.drawLine((this.x + level.xOffset)  + (this.w / 2), (this.y + level.yOffset) + (this.h / 2), (target.x + level.xOffset) + (target.w / 2), (target.y + level.yOffset) + (target.h / 2));
			}
			g.setColor(Colour.RED);
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));
			g.fillRect(((this.x + level.xOffset) + (this.w / 2)) - (this.rangeX / 2),  ((this.y + level.yOffset) + (this.h / 2)) - (this.rangeY / 2), this.rangeX, this.rangeY);
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
			g.drawRect(((this.x + level.xOffset) + (this.w / 2)) - (this.rangeX / 2),  ((this.y + level.yOffset) + (this.h / 2)) - (this.rangeY / 2), this.rangeX, this.rangeY);
		}
		super.render();
	}

}
