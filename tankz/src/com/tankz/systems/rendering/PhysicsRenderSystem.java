package com.tankz.systems.rendering;

import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.utils.ImmutableBag;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.gushikustudios.common.Settings;
import com.tankz.systems.camera.CameraSystem;
import com.tankz.systems.physics.PhysicsSystem;

public class PhysicsRenderSystem extends EntitySystem
{
   private World mPhysicsWorld;
   private Box2DDebugRenderer mBox2dRend;
   private CameraSystem mCam;
   
   @Override
   public void initialize()
   {
      mPhysicsWorld = PhysicsSystem.getPhysicsWorld();
      if (Settings.mDebugOn)
      {
         mBox2dRend = new Box2DDebugRenderer();
         mCam = world.getSystemManager().getSystem(CameraSystem.class);
      }
   }
   
   @Override
   protected void processEntities(ImmutableBag<Entity> entities)
   {
      mBox2dRend.render(mPhysicsWorld, mCam.getCombinedMatrix());
   }

   @Override
   protected boolean checkProcessing()
   {
      // TODO: Only if debug is enabled..
      if (Settings.mDebugOn)
      {
         return true;
      }
      return false;
   }

}
