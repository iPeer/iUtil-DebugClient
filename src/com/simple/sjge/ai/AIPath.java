package com.simple.sjge.ai;

import java.awt.Graphics2D;
import java.util.ArrayList;

import com.simple.sjge.engine.Engine;
import com.simple.sjge.gfx.Colour;
import com.simple.sjge.level.Level;

public class AIPath {
	
	private ArrayList<AIPoint> path;
	protected Level level;
	
	public AIPath(Level level) {
		this.level = level;
		this.path = new ArrayList<AIPoint>();
	}
	
	public void addPath(int x, int y) {
		AIPoint a = new AIPoint(x, y);
		if (path.contains(a))
			return;
		this.path.add(a);
	}
	
	public int numPaths() {
		return path.size();
	}
	
	public ArrayList<AIPoint> getPaths() {
		return path;
	}
	
	public void render() {
		Graphics2D g = Engine.getGraphicsInstance();
		g.setColor(Colour.PINK);
		int paths = numPaths();
		int it = 1;
		for (; it < paths; it++) {
			g.drawLine((path.get(it).x + level.xOffset), (path.get(it).y + level.yOffset), (path.get(it - 1).x + level.xOffset), (path.get(it - 1).y + level.yOffset));
		}
	}
	
	@Override
	public String toString() {
		String a = "PATH -> ("+this.path.get(0).x+", "+this.path.get(0).y+")";
		for (AIPoint b : this.path)
			a = a+" -> ("+b.x+", "+b.y+")";
		return a;
	}
	
}
