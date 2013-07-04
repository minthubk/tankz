package com.gushikustudios.tankz.behaviors;

import com.artemis.ComponentType;
import com.artemis.ComponentTypeManager;
import com.artemis.Entity;
import com.artemis.World;
import com.artemis.utils.ImmutableBag;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.tankz.components.Maneuver;
import com.tankz.components.Physics;

public class Separation implements Behavior
{
   private static ComponentType PHYSICS_TYPE = ComponentTypeManager.getTypeFor(Physics.class);
   private static ComponentType MANEUVER_TYPE = ComponentTypeManager.getTypeFor(Maneuver.class);
   
   private static final Vector2 mTmp1 = new Vector2();
   private static final Vector2 mTmp2 = new Vector2();
   
   private static final float MIN_DIST_SQUARED = 12*12;
   
   private World mEntityWorld;
   private Entity mNearNeighbor;
   
   public Separation(World world)
   {
      mEntityWorld = world;
   }

   @Override
   public boolean active(Entity e)
   {
      ImmutableBag<Entity> list = mEntityWorld.getGroupManager().getEntities("ENEMY");
      Physics selfPhysics = (Physics)e.getComponent(PHYSICS_TYPE);
      
      //TODO: Temporarily bypass based on delta?
      for (int i = 0; i < list.size() ; i++)
      {
         Entity target = list.get(i);
         // if not examining self...
         if (target != e)
         {
            // get the physics of the entity
            Physics targetPhysics = (Physics)target.getComponent(PHYSICS_TYPE);
            Body selfBody = selfPhysics.getBody();
            Body targetBody = targetPhysics.getBody();

            // get the distance from body to body
            mTmp1.set(selfBody.getPosition());
            mTmp2.set(targetBody.getPosition());
            mTmp1.sub(mTmp2);
            
            if (mTmp1.len2() < MIN_DIST_SQUARED)
            {
               mNearNeighbor = e;
               return true;
            }
         }
      }
      return false;
   }

   @Override
   public void action(Entity e)
   {
      Physics selfPhysics = (Physics)e.getComponent(PHYSICS_TYPE);
      Physics targetPhysics = (Physics)mNearNeighbor.getComponent(PHYSICS_TYPE);
      Body selfBody = selfPhysics.getBody();
      Body targetBody = targetPhysics.getBody();
      Maneuver m = (Maneuver)e.getComponent(MANEUVER_TYPE);
      
      mTmp1.set(targetBody.getPosition()).sub(selfBody.getPosition());
      
      float angleToTarget = mTmp1.angle();
      float targetHeading = (targetBody.getAngle() * MathUtils.radiansToDegrees);
      if (targetHeading < 0)
      {
         targetHeading = (targetHeading % 360f) + 360f;
      }
      else if (targetHeading >= 360)
      {
         targetHeading = targetHeading % 360f;
      }
      float selfHeading = (selfBody.getAngle() * MathUtils.radiansToDegrees); // don't forget that this winds up
      if (selfHeading < 0)
      {
         selfHeading = (selfHeading % 360f) + 360f;
      }
      else if (selfHeading >= 360)
      {
         selfHeading = selfHeading % 360f;
      }
      float deltaAngle = angleToTarget - selfHeading;
      float headingDelta = Math.abs(selfHeading - targetBody.getAngle());

      if (deltaAngle < 0)
      {
         // normalize
         deltaAngle += 360;
      }
      
      m.clear();
      
      if (deltaAngle < 135)
      {
         m.setForward(true);
         m.setRight(true);
      }
      else if (deltaAngle < 180)
      {
         m.setReverse(true);
         m.setLeft(true);
      }
      else if (deltaAngle < 215)
      {
         m.setReverse(true);
         m.setRight(true);
      }
      else if (deltaAngle < 360)
      {
         m.setForward(true);
         m.setLeft(true);
      }
   }

   @Override
   public void suppress(Entity e)
   {

   }

}
