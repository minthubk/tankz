package com.tankz.systems.player;

import com.artemis.Component;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntityProcessingSystem;
import com.artemis.utils.TrigLUT;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.tankz.components.BrainPlayer;
import com.tankz.components.Maneuver;
import com.tankz.components.Physics;
import com.tankz.components.TurnFactor;
import com.tankz.components.Velocity;

//TODO: Move the input processor to a different system?  Make PlayerTankMovementSystem generic.
public class PlayerTankMovementSystem extends EntityProcessingSystem implements InputProcessor
{
   private static final float MAX_REV_VELOCITY = 10f;
   private static final float MAX_FWD_VELOCITY = 20f;

   private static final float TURN_THRUST = 20f;
   private static final float MAX_TURN_VELOCITY = 625f; // this affects the turning radius

   private static float FWD_ACCEL_FACTOR = 200.0f;
   private static float REV_ACCEL_FACTOR = 100.0f;

   protected ComponentMapper<Velocity> velocityMapper;
   protected ComponentMapper<TurnFactor> turnFactorMapper;
   protected ComponentMapper<Physics> physicsMapper;
   protected ComponentMapper<Maneuver> maneuverMapper;
   
   private Entity player;

   private float mVelocity;
   

   public PlayerTankMovementSystem()
   {
      super(BrainPlayer.class);
   }
   
   /**
    * This provides sub-classes the ability to indicate which components they are interested in...
    * 
    * @param requiredType
    * @param otherTypes
    */
   public PlayerTankMovementSystem(Class<? extends Component> requiredType, Class<? extends Component>... otherTypes)
   {
      super(requiredType, otherTypes);
   }
   
   @Override
   public void initialize()
   {
      velocityMapper = new ComponentMapper<Velocity>(Velocity.class, world);
      turnFactorMapper = new ComponentMapper<TurnFactor>(TurnFactor.class, world);
      physicsMapper = new ComponentMapper<Physics>(Physics.class, world);
      maneuverMapper = new ComponentMapper<Maneuver>(Maneuver.class, world);
   }

   @Override
   protected void process(Entity e)
   {
      updatePlayer(e);
   }

   @Override
   protected boolean checkProcessing()
   {
      return true;
   }

   protected void updatePlayer(Entity e)
   {
      Velocity v = velocityMapper.get(e);
      TurnFactor tf = turnFactorMapper.get(e);
      Physics c = physicsMapper.get(e);
      Maneuver m = maneuverMapper.get(e); // get the maneuvers for this entity...

      updateMoving(e,c,v,m);
      updateTurning(c, v, tf, m, world.getDelta());
   }

   private void updateMoving(Entity e, Physics physics, Velocity v, Maneuver m)
   {
      if (m.getForward())
      {
         float angle = physics.getRotationInRadians();
         float ax = (TrigLUT.cos(angle));
         float ay = (TrigLUT.sin(angle));
         
         mVelocity = physics.getBody().getLinearVelocity().len(); 
         
         if (mVelocity < MAX_FWD_VELOCITY)
         {
            float mass = physics.getBody().getMass();
            physics.getBody().applyForceToCenter(mass*ax*FWD_ACCEL_FACTOR,mass*ay*FWD_ACCEL_FACTOR);
         }
      }
      else if (mVelocity > 0)
      {
         mVelocity = 0;
      }

      if (m.getReverse())
      {
         float angle = physics.getRotationInRadians();
         float ax = (TrigLUT.cos(angle));
         float ay = (TrigLUT.sin(angle));

         mVelocity = physics.getBody().getLinearVelocity().len();
         
         if (physics.getBody().getLinearVelocity().len() < MAX_REV_VELOCITY)
         {
            float mass = physics.getBody().getMass();
            physics.getBody().applyForceToCenter(-mass*ax*REV_ACCEL_FACTOR,-mass*ay*REV_ACCEL_FACTOR);
         }
         
         mVelocity = -mVelocity; // reverse
      }
      else if (mVelocity < 0)
      {
         mVelocity += world.getDelta() * 1f;
         if (mVelocity > 0)
         {
            mVelocity = 0;
         }
      }
      v.setVelocity(mVelocity);
   }

   private void updateTurning(Physics physics, Velocity v, TurnFactor tf, Maneuver m, int delta)
   {
      float turnFactor = tf.getFactor();

      if (m.getRight())
      {
         turnFactor += delta * TURN_THRUST;
         if (turnFactor > MAX_TURN_VELOCITY)
         {
            turnFactor = MAX_TURN_VELOCITY;
         }
         m.setTurning(true);
      }
      else if (m.getLeft())
      {
         turnFactor -= delta * TURN_THRUST;
         if (turnFactor < -MAX_TURN_VELOCITY)
         {
            turnFactor = -MAX_TURN_VELOCITY;
         }
         m.setTurning(true);
      }

      if (!m.getRight() && !m.getLeft() && m.getTurning())
      {
         if (turnFactor > 0)
         {
            turnFactor -= delta * TURN_THRUST;
            if (turnFactor <= 0)
            {
               turnFactor = 0;
               m.setTurning(false);
            }
         }
         else
         {
            turnFactor += delta * TURN_THRUST;
            if (turnFactor >= 0)
            {
               turnFactor = 0;
               m.setTurning(false);
            }
         }
      }

      if (m.getTurning())
      {
         // turning factor force is proportional to speed
         Vector2 velocity = physics.getBody().getLinearVelocity();
         updateRotating(physics.getBody(),turnFactor*velocity.len2(),m);
      }
      tf.setFactor(turnFactor);
   }

   private void updateRotating(Body b, float factor, Maneuver m)
   {
      if (m.getRight() || m.getLeft())
      {
         if (m.getReverse())
         {
            b.applyTorque(factor);
         }
         else
         {
            b.applyTorque(-factor);
         }
      }
   }

   @Override
   public boolean keyDown(int key)
   {
      ensurePlayerEntity();
      
      if (player == null)
      {
         return false;
      }
      
      Maneuver m = maneuverMapper.get(player);
      
      if (key == Keys.W)
      {
         m.setForward(true);
         return true;
      }
      else if (key == Keys.S)
      {
         m.setReverse(true);
         return true;
      }
      else if (key == Keys.A)
      {
         m.setLeft(true);
         return true;
      }
      else if (key == Keys.D)
      {
         m.setRight(true);
         return true;
      }
      return false;
   }

   @Override
   public boolean keyUp(int key)
   {
      ensurePlayerEntity();
      
      if (player == null)
      {
         return false;
      }
      
      Maneuver m = maneuverMapper.get(player);
      
      if (key == Keys.W)
      {
         m.setForward(false);
         return true;
      }
      else if (key == Keys.S)
      {
         m.setReverse(false);
         return true;
      }
      else if (key == Keys.A)
      {
         m.setLeft(false);
         return true;
      }
      else if (key == Keys.D)
      {
         m.setRight(false);
         return true;
      }
      return false;
   } 
   
   private void ensurePlayerEntity() {
      if (player == null || !player.isActive())
      {
         player = world.getTagManager().getEntity("PLAYER");
      }
   }

   @Override
   public boolean keyTyped(char character)
   {
      return false;
   }

   @Override
   public boolean touchDown(int x, int y, int pointer, int button)
   {
      return false;
   }

   @Override
   public boolean touchUp(int x, int y, int pointer, int button)
   {
      return false;
   }

   @Override
   public boolean touchDragged(int x, int y, int pointer)
   {
      return false;
   }

   @Override
   public boolean touchMoved(int x, int y)
   {
      return false;
   }

   @Override
   public boolean scrolled(int amount)
   {
      return false;
   }
}
