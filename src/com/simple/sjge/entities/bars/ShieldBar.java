package com.simple.sjge.entities.bars;

import java.awt.Graphics2D;

import com.simple.sjge.engine.Engine;
import com.simple.sjge.entities.Entity;
import com.simple.sjge.entities.extensions.Shield;
import com.simple.sjge.gfx.Colour;

public class ShieldBar {

	protected Entity owner;
	private int length = 30;
	public int height = 10;

	public ShieldBar(Entity owner) {
		this.owner = owner;
	}

	public ShieldBar(Shield shield) {
		this(shield.getOwner());
	}

	public void render() {
		Graphics2D g = Engine.getGraphicsInstance();
		g.setColor(Colour.WHITE);
		g.drawRect((owner.x + owner.level.xOffset) + (owner.w - (this.length + 2)) / 2, (owner.y + owner.level.yOffset) - this.height - 12, this.length + 2, this.height - 2);
		if (owner.getShield().getCurrent() > 0) {
			g.setColor(Colour.CYAN);
			g.fillRect((owner.x + owner.level.xOffset) + (owner.w - (this.length - 2)) / 2, (owner.y + owner.level.yOffset) - this.height - 10, (this.length*owner.getShield().getCurrent()/owner.getShield().getCapacity()) - 1, this.height - 5);
		}
		if (Engine.DEBUG_ENABLED)
			g.drawString(Integer.toString(owner.getShield().getCurrent())+" - "+owner.getShield().getTicksEmpty()+" - "+owner.getShield().isCharging(), (owner.x + owner.level.xOffset) + this.length - 3, (owner.y + owner.level.yOffset) - 14);
	}

}
