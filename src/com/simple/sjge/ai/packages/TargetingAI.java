package com.simple.sjge.ai.packages;

import java.util.ArrayList;

import com.simple.sjge.entities.AITestEntity3;
import com.simple.sjge.entities.Entity;

public class TargetingAI extends AI {

	public TargetingAI(Entity e) {
		super(e);
	}
	
	public void tick() {
		for (Entity target : level.entities) {
			if (target.getBBox().canBeTargetedBy(entity.aof)) {
				if (!(target instanceof AITestEntity3)) {
					if (!entity.targets.contains(target) && entity.targets.size() < entity.numTargets) {
						entity.targets.add(target);
						break;
					}
				}
			}
			else
				entity.targets.remove(target);
		}
		
		// Check if all the entities that are targeted still exist
		ArrayList<Entity> a = new ArrayList<Entity>();
		for (Entity b : entity.targets)
			if (!level.entities.contains(b))
				a.add(b);
		entity.targets.removeAll(a);
}

}
