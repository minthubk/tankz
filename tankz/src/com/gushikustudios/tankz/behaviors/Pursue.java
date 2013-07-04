package com.gushikustudios.tankz.behaviors;

import com.artemis.ComponentType;
import com.artemis.ComponentTypeManager;
import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.tankz.components.Maneuver;
import com.tankz.components.Physics;

public class Pursue implements Behavior
{
   private static ComponentType PHYSICS_TYPE = ComponentTypeManager.getTypeFor(Physics.class);
   private static ComponentType MANEUVER_TYPE = ComponentTypeManager.getTypeFor(Maneuver.class);
   
   private static final Vector2 mTmp1 = new Vector2();
   
   private Entity mTarget;
   private World mEntityWorld;
   
   public Pursue(World world)
   {
      mEntityWorld = world;
   }
   
   @Override
   public boolean active(Entity e)
   {
      return true;
   }

   @Override
   public void action(Entity e)
   {
      acquireTarget(e);
      if (mTarget != null)
      {
         Physics targetPhysics = (Physics) mTarget.getComponent(PHYSICS_TYPE);
         Physics selfPhysics = (Physics) e.getComponent(PHYSICS_TYPE);
         Maneuver maneuver = (Maneuver) e.getComponent(MANEUVER_TYPE);
         
         Body targetBody = targetPhysics.getBody();
         Body selfBody = selfPhysics.getBody();

         // simplistic approach -- move forward and turn towards target
         mTmp1.set(targetBody.getPosition()).sub(selfBody.getPosition());
         
         boolean reverse, forward, turnLeft, turnRight;
         
         reverse = maneuver.getReverse();
         forward = maneuver.getForward();
         turnLeft = maneuver.getLeft();
         turnRight = maneuver.getRight();

         // if the distance is great enough...
         if (mTmp1.len() > 20)
         {
            float angleToTarget = mTmp1.angle();
            float selfAngle = (selfBody.getAngle() * MathUtils.radiansToDegrees); // don't forget that this winds up
            if (selfAngle < 0)
            {
               selfAngle = (selfAngle % 360f) + 360f;
            }
            else if (selfAngle >= 360)
            {
               selfAngle = selfAngle % 360f;
            }
            float deltaAngle = angleToTarget - selfAngle;

            if (deltaAngle < 0)
            {
               // normalize
               deltaAngle += 360;
            }

            float arcAngle = 45;
            
            if (reverse == true)
            {
               arcAngle = -45;
            }
            
            reverse = forward = turnLeft = turnRight = false;

            // small angle... drive straight
            if (deltaAngle < 10)
            {
               forward = true;
            }
            else if (deltaAngle < 90 + arcAngle)
            {
               forward = true;
               turnLeft = true;
            }
            else if (deltaAngle < 180)
            {
               reverse = true;
               turnRight = true;
            }
            else if (deltaAngle < 270 - arcAngle)
            {
               reverse = true;
               turnLeft = true;
            }
            else
            {
               forward = true;
               turnRight = true;
            }
         }
         else
         {
            forward = false;
         }
         
         maneuver.setReverse(reverse);
         maneuver.setForward(forward );
         maneuver.setLeft(turnLeft);
         maneuver.setRight(turnRight);
      }
   }

   @Override
   public void suppress(Entity e)
   {

   }
   
   private void acquireTarget(Entity e)
   {
      //TODO: Target nearest tank.  For now, just target the player tank.
      if ((mTarget == null) || (!mTarget.isActive()))
      {
         mTarget = mEntityWorld.getTagManager().getEntity("PLAYER");
      }
   }

}
