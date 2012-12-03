package com.simple.sjge.entities;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import com.simple.sjge.ai.packages.AI;
import com.simple.sjge.collision.BBox;
import com.simple.sjge.engine.Engine;
import com.simple.sjge.entities.bars.HealthBar;
import com.simple.sjge.entities.bars.ShieldBar;
import com.simple.sjge.entities.extensions.Shield;
import com.simple.sjge.gfx.Colour;
import com.simple.sjge.gfx.Screen;
import com.simple.sjge.level.Level;

public class Entity {


	public int x, y, w, h;
	private Screen screen;
	private BBox bbox;
	public BBox xbbox;
	public BBox ybbox;
	public Level level;
	protected Engine engine;
	public boolean left;
	public boolean up;
	protected int health = 100;
	protected Shield shield;
	protected HealthBar healthBar;
	protected ShieldBar shieldBar;
	protected AI aiPackage;
	public BBox aof;
	public int rangeX = 100, rangeY = 100;
	public int numTargets = 5;
	public ArrayList<Entity> targets;
	protected int damageDelay = 30;
	protected int damageTick = 0;
	private boolean shouldTick = true;
	BufferedImage sprite;
	private boolean entityCollisions = false;

	public Entity(int x, int y, int w, int h, Level level) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		this.bbox = new BBox(x, y, w, h);
//		this.xbbox = new BBox(x - 1, y - 1, w + 1, h - 1);
//		this.ybbox = new BBox(x - 1, y - 1, w - 1, h + 1);
		this.xbbox = new BBox(x - 1, y, w + 2, h);
		this.ybbox = new BBox(x, y - 1, w, h + 2);
		this.level = level;
		this.engine = Engine.getInstance();
		this.screen = level.getScreen();
		try {
			this.sprite = ImageIO.read(Engine.class.getResource("missing.png"));
		}
		catch (IOException e) {
			System.err.println("Unable to set sprite for entity "+getID());
		}
		//move(x, y, w, h, false);

	}

	public String getID() {
		String[] a = this.toString().split("\\.");
		String b = a[a.length - 1].split("@")[0];
		return this.toString().split("@")[1]+" - "+b;
	}

	public BBox getBBox() {
		return this.bbox;
	}

	public void setAIPackage(AI ai) {
		this.aiPackage = ai;
	}

	public void tick() { 
		if (!this.shouldTick)
			return;
		if (aiPackage != null)
			aiPackage.tick();

		if (level.isOffLevel(this)) {
			System.err.println("WARNING: entity id "+this.getID()+" has left the level and was queued for removal.");
			level.removeEntity(this);
		}

		if (this instanceof Player) {
			if (engine.input.left.down) {
				this.left = true;
				move(this.x - 1, this.y, this.w, this.h);
			}
			if (engine.input.right.down) {
				this.left = false;
				move(this.x + 1, this.y, this.w, this.h);
			}
			if (engine.input.up.down) {
				this.up = true;
				move(this.x, this.y - 1, this.w, this.h);
			}
			if (engine.input.down.down) {
				this.up = false;
				move(this.x, this.y + 1, this.w, this.h);
			}
		}

	}

	public void collidedX() { 
		if (aiPackage != null)
			aiPackage.collidedX();
	}
	public void collidedY() { 
		if (aiPackage != null)
			aiPackage.collidedY();
	}

	public void move (int x, int y, int w, int h) {
		move(x, y, w, h, true);
	}

	public void move(int x, int y, int w, int h, boolean collisions) {
		if (collisions) {
			//			boolean collidesX = false;
			//			boolean collidesY = false;
			for (BBox a : level.BBoxes) {
				boolean b = a.collidesX(xbbox);
				boolean c = a.collidesY(ybbox);
				if (b && c) {
					this.collidedX();
					this.collidedY();
					return;
				}
				if (b) {
					//collidesX = true;
					this.collidedX();
					return;
				}
				if (c) {
					//collidesY = true;
					this.collidedY();
					return;
				}
			}
			
			if (entityCollisions && Engine.CHECK_ENTITY_COLLISIONS) {
				for (Entity e : level.entities) {
					if (e.equals(this))
						continue;
					boolean a = e.collidesWithX(this);
					boolean b = e.collidesWithY(this);
					boolean c = e.entityCollisions;			
					if ((a && b) && c) {
						e.collidedX();
						e.collidedY();
						this.collidedX();
						this.collidedY();
						return;
					}
					if (a && c) {
						e.collidedX();
						this.collidedX();
						return;
					}
					if (b && c) {
						e.collidedY();
						this.collidedY();
						return;
					}
					
				}
			}
			
		}
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		this.bbox = new BBox(x, y, w, h);
		this.xbbox = new BBox(x - 1, y, w + 2, h);
		this.ybbox = new BBox(x, y - 1, w, h + 2);
	}
	
	public boolean collidesWithX(BBox a) {
		if (!Engine.CHECK_ENTITY_COLLISIONS)
			return false;
		Rectangle check = new Rectangle(a.x, a.y, a.w, a.h);
		return xbbox.intersects(a);
		//return check.intersects(this.xbbox.box);
	}
	
	public boolean collidesWithY(BBox a) {
		if (!Engine.CHECK_ENTITY_COLLISIONS)
			return false;
		Rectangle check = new Rectangle(a.x, a.y, a.w, a.h);
		return ybbox.intersects(a);
		//return check.intersects(this.ybbox.box);
	}
	
	public boolean collidesWithX(Entity e) {
		return this.xbbox.intersects(e.xbbox);
	}
	
	public boolean collidesWithY(Entity e) {
		return this.ybbox.intersects(e.ybbox);
	}

	public void render() { 
		if (this.sprite != null)
			screen.renderSprite(sprite, x + level.xOffset, y + level.yOffset, w, h);
		else
			screen.drawRect(x + level.xOffset, y + level.yOffset, w, h);
		screen.drawRect(xbbox.x + level.xOffset, xbbox.y + level.yOffset, xbbox.w, xbbox.h, Colour.RED);
		screen.drawRect(ybbox.x + level.xOffset, ybbox.y + level.yOffset, ybbox.w, ybbox.h, Colour.RED);
	}

	public boolean isOnScreen() {
		Rectangle screen1 = new Rectangle(0, 0, screen.width, screen.height);
		Rectangle entity = new Rectangle();
		if (this.healthBar != null && this.w < this.healthBar.length)
			entity = new Rectangle((this.x + level.xOffset) + (this.healthBar.length / 2), (this.y + level.yOffset) - (this.healthBar.height + (this.shieldBar != null ? this.shieldBar.height + 2 : 0)), w, h);
		else
			entity = new Rectangle(this.x + level.xOffset, this.y + level.yOffset, w, h);
		return entity.intersects(screen1);
	}

	public int getHealth() {
		return health;
	}

	public Shield getShield() {
		return shield;
	}

	public void damage(int i, Entity e) {
		this.health -= i;
		if (this.health < 0)
			this.health = 0;
	}

	public void setShouldTick(boolean b) {
		this.shouldTick = b;
	}

	public double getAngle(int x, int y) {
		double dx = x - ((this.x + (this.w / 2)) + level.xOffset);
		double dy = y - ((this.y + (this.h / 2)) + level.yOffset);
		double a = Math.toRadians((360 + Math.toDegrees(Math.atan2(dx, dy))) % 360);
		return Math.PI-a;
	}
	
	public double getAngle(Entity entity) {
		return getAngle((entity.x + (entity.w / 2)) + level.xOffset, (entity.y + (entity.h / 2)) + level.yOffset);
	}

	public void setHealth(int maxHealth) {
		this.health = maxHealth;
	}
	
	public void setEntityCollisions(boolean c) {
		this.entityCollisions = c;
	}
	
	public boolean mouseOver() {
		Point mouse = Engine.Mouse;
		Rectangle rect = new Rectangle(this.x + level.xOffset, this.y + level.yOffset, this.w, this.h);
		return rect.contains(mouse);
	}

}
