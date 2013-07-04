package com.tankz.systems.rendering;

import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.utils.ImmutableBag;
import com.tankz.systems.camera.CameraSystem;

public class CrosshairRenderSystem extends EntitySystem {
	private CameraSystem cs;

	public CrosshairRenderSystem() 
	{
		super();
	}

	@Override
	public void initialize() {
//		try {
//			crosshair = new Image("crosshair.png");
//		} catch (SlickException e) {
//			e.printStackTrace();
//		}

		cs = world.getSystemManager().getSystem(CameraSystem.class);
	}
	
	@Override
	protected void processEntities(ImmutableBag<Entity> entities) {
//		g.setDrawMode(Graphics.MODE_ADD);
//		crosshair.drawCentered(container.getInput().getAbsoluteMouseX(), container.getInput().getAbsoluteMouseY());
//		g.setDrawMode(Graphics.MODE_NORMAL);
	}
	
	@Override
	protected boolean checkProcessing() {
		return true;
	}

}
