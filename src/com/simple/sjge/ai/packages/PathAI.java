package com.simple.sjge.ai.packages;

import com.simple.sjge.ai.AIPath;
import com.simple.sjge.ai.AIPoint;
import com.simple.sjge.entities.Entity;
import com.simple.sjge.level.Level;

public class PathAI extends AI {
	
	private int dir = -1;
	private int path = 0;
	private int stepInterval = 1;
	private int step = 0;

	public PathAI(Entity e) {
		super(e);
	}
	
	public void tick() { 
		if (step >= stepInterval) {
			int x = entity.x;
			int y = entity.y;
			int w = entity.w;
			int h = entity.h;
			step = 0;
			if (path > -1) {
				AIPath a = level.getAIPath();
				AIPoint b = a.getPaths().get(path);
				int x1 = x + (w / 2);
				int y1 = y + (h / 2);
				if (x1 < b.x)
					dir = 1;
				else if (x1 > b.x)
					dir = 2;
				else if (y1 < b.y)
					dir = 0;
				else if (y1 > b.y)
					dir = 3;
				if (x1 == b.x && y1 == b.y) {
					path++;
				}
				if (path >= level.getAIPath().getPaths().size())
					path = -1;
				switch(dir) {
				case 0:
					entity.move(x, y + 1, w, h);
					break;
				case 1:
					entity.move(x + 1, y, w, h);
					break;
				case 2:
					entity.move(x - 1, y, w, h);
					break;
				case 3:
					entity.move(x, y - 1, w, h);
					break;

				}
			}
		}
		else
			step++;
	}
	
	public Level getLevel() {
		return level;
	}
	
	

}
