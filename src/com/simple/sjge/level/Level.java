package com.simple.sjge.level;

import java.util.ArrayList;

import com.simple.sjge.ai.AIPath;
import com.simple.sjge.collision.BBox;
import com.simple.sjge.engine.Engine;
import com.simple.sjge.entities.Entity;
import com.simple.sjge.gfx.Colour;
import com.simple.sjge.gfx.Screen;

public class Level {

	public int width, height;
	private Screen screen;
	public ArrayList<BBox> BBoxes;
	public ArrayList<Entity> entities;
	public ArrayList<Entity> removedEntities;
	public ArrayList<Entity> queuedEntities;
	protected Engine engine;
	public int xOffset = 0;
	public int yOffset = 0;
	private AIPath aiPath;
	private boolean allowXDragging = true;
	private boolean allowYDragging = true;
	private int maxXDrag = 0;
	private int maxYDrag = 0;
	private int lastOnScreenEntities, onScreenEntities;

	public Level(int width, int height, Screen screen) {
		this.width = width;
		this.height = height;
		this.screen = screen;
		this.BBoxes = new ArrayList<BBox>();
		this.entities = new ArrayList<Entity>();
		this.removedEntities = new ArrayList<Entity>();
		this.queuedEntities = new ArrayList<Entity>();
		this.engine = Engine.getInstance();
		int x = 0;
		int y = 0;
		if (width < screen.width) {
			allowXDragging = false;
			x = maxXDrag = (screen.width - width) / 2;
		}
		if (height < screen.height) {
			allowYDragging = false;
			y = maxYDrag = (screen.height - height) / 2;
			
		}
		System.err.println(x+", "+maxXDrag+" | "+y+", "+maxYDrag);
		setOffset(x, y);
	}

	public void addBBox(BBox a) {
		int x = a.x + a.w;
		int y = a.y + a.h;
		if (a.x > width || x < 0 || a.y > height || y < 0)
			return;
		BBoxes.add(a);
	}
	
	public void queueEntity(Entity a) {
		for (BBox b : BBoxes)
			if (b.collidesX(a.xbbox) || b.collidesY(a.ybbox)) {
				System.err.println("WARNING: entity "+a.getID()+" @ "+a.x+", "+a.y+" collides with level, spawning was bailed.");
				return;
			}

		queuedEntities.add(a);
	}

	public void addEntity(Entity a) {
		for (BBox b : BBoxes)
			if (b.collidesX(a.xbbox) || b.collidesY(a.ybbox)) {
				System.err.println("WARNING: entity "+a.getID()+" @ "+a.x+", "+a.y+" collides with level, spawning was bailed.");
				return;
			}

		entities.add(a);
	}

	public void setOffset(int x, int y) {
		int x1 = (width - screen.width);
		int y1 = (height - screen.height);
		if (!Engine.ALLOW_OVERDRAGGING) {
			if (x < -x1)
				x = -x1;
			else if (x > 0)
				x = 0;
			if (y < -y1)
				y = -y1;
			else if (y > 0)
				y = 0;
			
			if (!allowXDragging)
				x = maxXDrag;
			if (!allowYDragging)
				y = maxYDrag;
			
		}
		
		this.xOffset = x;
		this.yOffset = y;
		
	}


	public void setAIPath(AIPath aiPath) {
		this.aiPath = aiPath;
	}

	public void removeColliding() {
		ArrayList<Entity> remove = new ArrayList<Entity>();
		for (BBox b : BBoxes)
			for (Entity e : entities)
				if (b.collidesX(e.xbbox) || b.collidesY(e.ybbox)) {
					System.err.println("WARNING: entity "+e.getID()+" @ "+e.x+", "+e.y+" collides with level, forcing removal.");
					remove.add(e);
				}
		entities.removeAll(remove);
	}

	public void tick() {
		if (!queuedEntities.isEmpty()) {
			entities.addAll(queuedEntities);
			queuedEntities.clear();
		}
		if (!removedEntities.isEmpty()) {
			entities.removeAll(removedEntities);
			removedEntities.clear();
		}
		for (Entity e : entities) 
			e.tick();
	}

	public void render() {
		
		for (BBox a : BBoxes)
			screen.drawRect((a.x + xOffset), (a.y + yOffset), a.w, a.h, Colour.YELLOW);
		for (Entity b : entities) 
			if (b.isOnScreen()) {
				onScreenEntities++;
				b.render();
			}
		lastOnScreenEntities = onScreenEntities;
		onScreenEntities = 0;
		
		if (Engine.DEBUG_ENABLED) {
			int a = screen.getGraphics().getFontMetrics().stringWidth(this.toString().split("@")[0]+" ("+this.toString().split("@")[1]+")");
			int b = (screen.width - a) / 2;
			screen.drawString(this.toString().split("@")[0]+" ("+this.toString().split("@")[1]+")", b, 12, Colour.WHITE);
			screen.drawString("SiMPLE Engine "+engine.getVersion()+" "+(Engine.DEBUG_ENABLED ? "[DEBUG MODE]" : ""), 2, 12);
			screen.drawString("E: "+lastOnScreenEntities+"/"+entities.size()+" B: "+BBoxes.size(), 2, 32);
			screen.drawString("TO: "+Engine.totalTime+"ms TI: "+Engine.tickTime+"ms R: "+Engine.renderTime+"ms", 2, 42);
//			int y = 42;
//			int entity = 0;
//			for (Entity e : entities) {
//				entity++;
//				if (y + 24 > engine.getHeight()) {
//					screen.drawString((entities.size() + 1) - entity+" more...", 2, y+=12);
//					break;
//				}
//				else
//					screen.drawString((isOffLevel(e) ? "\247c" : (!e.isOnScreen() ? "\247e" : ""))+e.getID()+" | X: "+e.x+", Y: "+e.y+", W: "+e.w+", H: "+e.h, 2, y+=12, (isOffLevel(e) ? Colour.RED : Colour.WHITE));
//			}
			if (aiPath != null) {
				aiPath.render();
			}
		}

	}

	public AIPath getAIPath() { 
		return aiPath; 
	}

	public void removeEntity(Entity entity) {
		removedEntities.add(entity);
	}

	public boolean isOffLevel(Entity entity) {
		int x = entity.x;
		int y = entity.y;
		int w = entity.w;
		int h = entity.h;
		return ((x + w) < 0 || x > width || (y + h) < 0 || y > height);

	}

	public Screen getScreen() {
		return this.screen;
	}

	public void generate() { }

}
