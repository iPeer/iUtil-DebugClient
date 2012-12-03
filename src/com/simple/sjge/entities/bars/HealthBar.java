package com.simple.sjge.entities.bars;

import java.awt.Color;
import java.awt.Graphics2D;

import com.simple.sjge.engine.Engine;
import com.simple.sjge.entities.Entity;
import com.simple.sjge.gfx.Colour;

public class HealthBar {

	protected Entity owner;
	public int length = 30;
	public int height = 10;
	private int maxHealth = 100;
	private int[] colours;

	public HealthBar(Entity owner, int maxHealth) {
		this.owner = owner;
		if (owner.getHealth() < maxHealth)
			owner.setHealth(maxHealth);
		this.maxHealth = maxHealth;
		this.colours = generateColours(maxHealth);
	}
	
	private int[] generateColours(int hp) {
		float step = (1.f/hp)*2;
		int[] colour = new int[hp];
		boolean redDone = false;
		boolean greenDone = false;
		float green = 1.f;
		float red = 0.f;
		colour[hp-1] = new Colour(0.f, green, 0.f).getRGB();
		for (int x = hp-1; x > 0; x--) {
			if (!redDone) {
				red += step;
				if (red >= 1.f) {
					red = 1.f;
					redDone = true;
				}
			}
			else if (redDone && !greenDone) {
				green -= step;
				if (green <= 0.f) {
					green = 0.f;
					greenDone = true;
				}
			}
			colour[x] = new Colour(red, green, 0.f).getRGB();
			
		}
		return colour;
	}

	public void render() {
		Graphics2D g = Engine.getGraphicsInstance();
		g.setColor(Colour.WHITE);
		g.drawRect((owner.x + owner.level.xOffset) + (owner.w - (this.length + 2)) / 2, (owner.y + owner.level.yOffset) - this.height - 2, this.length + 2, this.height - 2);
		Color c = Colour.GREEN;
//		
//		if (red < 255)
//			red += increment;
//		if (red >= 255)
//			red = 255;
//		if (red <= 0)
//			red = 0;
//		if (red == 255) {
//			green -= increment;
//			if (green >= 255)
//				green = 255;
//			if (green <= 0)
//				green = 0;
//		}
//		c = new Colour(red, green, 0);
		int hp = (int)owner.getHealth()-1;
		if (hp < 0)
			hp = 0;
		g.setColor(new Colour(colours[hp]));
		g.fillRect((owner.x + owner.level.xOffset) + (owner.w - (this.length - 2)) / 2, (owner.y + owner.level.yOffset) - this.height, (this.length*owner.getHealth()/maxHealth) - 1, this.height - 5);
		if (Engine.DEBUG_ENABLED)
			g.drawString(Integer.toString(owner.getHealth()), (owner.x + owner.level.xOffset) + this.length - 3, (owner.y + owner.level.yOffset) - 4);
	}

}
