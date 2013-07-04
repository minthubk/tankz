package com.tankz.systems.misc;

import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntityProcessingSystem;
import com.tankz.components.Health;

public class HealthSystem extends EntityProcessingSystem {
	private ComponentMapper<Health> healthMapper;

	public HealthSystem() {
		super(Health.class);
	}

	@Override
	public void initialize() {
		healthMapper = new ComponentMapper<Health>(Health.class, world);
	}

	@Override
	protected void process(Entity e) {
		// get the health component from this entity...
		Health health = e.getComponent(Health.class);
		
		// if the health indicates that this component is not alive...
		if(!health.isAlive()) {
			// this entity is destroyed...
			world.deleteEntity(e);
		}
		
	}


}
