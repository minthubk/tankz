package com.tankz.systems.rendering;

import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntityProcessingSystem;
import com.artemis.utils.Bag;
import com.tankz.components.SpatialForm;
import com.tankz.systems.camera.CameraSystem;
import com.tankz.systems.rendering.spatials.Bullet;
import com.tankz.systems.rendering.spatials.Crate;
import com.tankz.systems.rendering.spatials.Explosion;
import com.tankz.systems.rendering.spatials.MammothTank;
import com.tankz.systems.rendering.spatials.Spatial;
import com.tankz.systems.rendering.spatials.Wall;

public class RenderSystem extends EntityProcessingSystem {
	private Bag<Spatial> spatials;
	private ComponentMapper<SpatialForm> spatialFormMapper;
	private CameraSystem cameraSystem;

	public RenderSystem() {
		super(SpatialForm.class);
	}

	@Override
	public void initialize() {
		spatialFormMapper = new ComponentMapper<SpatialForm>(SpatialForm.class, world);

		cameraSystem = world.getSystemManager().getSystem(CameraSystem.class);

		spatials = new Bag<Spatial>();
	}

	@Override
	protected void begin() {
	}

	@Override
	protected void process(Entity e) {
		Spatial spatial = spatials.get(e.getId());
		spatial.render();
	}

	@Override
	protected void end() {
	}

	@Override
	protected void added(Entity e) {
		Spatial spatial = getSpatial(e);
		if (spatial != null) {
			spatial.initalize();
			spatials.set(e.getId(), spatial);
		}
	}

	@Override
	protected void removed(Entity e) {
		spatials.set(e.getId(), null);
	}

	private Spatial getSpatial(Entity e) {
		SpatialForm spatialForm = spatialFormMapper.get(e);
		String spatialFormFile = spatialForm.getSpatialFormFile();
		if (spatialFormFile.equalsIgnoreCase("crate")) {
			return new Crate(world, e);
		} else if (spatialFormFile.equalsIgnoreCase("mammothTank")) {
			return new MammothTank(world, e);
		} else if (spatialFormFile.equalsIgnoreCase("bullet")) {
			return new Bullet(world, e);
		} else if (spatialFormFile.equalsIgnoreCase("explosion")) {
			return new Explosion(world, e);
		} else if (spatialFormFile.equalsIgnoreCase("wall")) {
			return new Wall(world, e);
		}
		return null;
	}
}
