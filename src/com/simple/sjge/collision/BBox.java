package com.simple.sjge.collision;

import java.awt.Point;
import java.awt.Rectangle;

import com.simple.sjge.engine.Engine;
import com.simple.sjge.level.Level;

public class BBox {

	public int x, y, w, h;
	public Rectangle box;
	
	public BBox(int x, int y, int w, int h) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		this.box = new Rectangle(x, y, w, h);
	}
		
	@Deprecated
	public boolean collidesX(int x, int y, int w, int h) {
		if (!Engine.CHECK_COLLISIONS)
			return false;
		Rectangle a = new Rectangle(x, y, w, h);
		return a.intersects(box);
	}
	
	@Deprecated
	public boolean collidesY(int x, int y, int w, int h) {
		if (!Engine.CHECK_COLLISIONS)
			return false;
		Rectangle a = new Rectangle(x, y, w, h);
		return a.intersects(box);
	}
	
	public boolean collidesX(BBox b) {
		if (!Engine.CHECK_COLLISIONS)
			return false;
		//Rectangle a = new Rectangle(this.x, this.y, this.w, this.h);
		Rectangle a = new Rectangle(b.x, b.y, b.w, b.h);
		return a.intersects(box);
		//return ((b.x + b.w) >= x && b.x <= (x + w)) && ((b.box.y + b.h) >= y && b.y <= (y + h));
	}
	
	public boolean collidesY(BBox b) {
		if (!Engine.CHECK_COLLISIONS)
			return false;
		//Rectangle a = new Rectangle(this.x, this.y, this.w, this.h);
		Rectangle a = new Rectangle(b.x, b.y, b.w, b.h);
		return a.intersects(box);
	}

	public boolean collidesNX(BBox b) {
		return ((b.x + b.w) <= x && b.x >= (x + w)) && ((b.box.y + b.h) <= y && b.y >= (y + h));
	}

	public boolean intersects(BBox other) {
		Rectangle a = new Rectangle(this.x, this.y, this.w, this.h);
		Rectangle b = new Rectangle(other.x, other.y, other.w, other.h);
		return a.intersects(b);
	}

	public boolean canBeTargetedBy(BBox other) {
		return other.getRectangle().contains(this.x, this.y, this.w, this.h);
	}

	private Rectangle getRectangle() {
		return new Rectangle(this.x, this.y ,this.w, this.h);
	}

	public void render(Level level) {
		Engine.getGraphicsInstance().drawRect(this.x + level.xOffset, this.y + level.yOffset, this.w, this.h);
	}

	public boolean contains(Point point) {
		Rectangle rec = new Rectangle(this.x, this.y ,this.w, this.h);
		return rec.contains(point);
	}
	
}
