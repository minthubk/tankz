package com.tankz.systems.physics;

import com.artemis.Entity;
import com.artemis.IntervalEntitySystem;
import com.artemis.utils.ImmutableBag;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.tankz.EntityFactory;
import com.tankz.components.Health;
import com.tankz.components.Physics;

/**
 * This system creates the physics world, implements the world step, and performs
 * collision handling.
 * 
 * @author tescott
 * 
 */
public class PhysicsSystem extends IntervalEntitySystem implements ContactListener
{
   // private ComponentMapper<Physics> physicsMapper;
   // private ComponentMapper<Transform> transformMapper;
   private static World physicsWorld;
   private float mAcc;
   
   private static final float MAX_DELTA_TIME = 0.25f;
   private static final int VELOCITY_ITER = 10;
   private static final int POSITION_ITER = 10;
   private static final float MINIMUM_TIME_STEP = 1/60f;
   private static final float INV_MINIMUM_TIME_STEP = 1.0f/MINIMUM_TIME_STEP;

   @SuppressWarnings("unchecked")
   public PhysicsSystem()
   {
      super(20, Physics.class);
   }

   // TODO: is it better to follow the pattern for the TerrainRenderSystem for this?
   // TODO: proper disposal of the world and all of its contents???
   public static World getPhysicsWorld()
   {
      return physicsWorld;
   }

   @Override
   public void initialize()
   {
      // physicsMapper = new ComponentMapper<Physics>(Physics.class, world);
      // transformMapper = new ComponentMapper<Transform>(Transform.class, world);

      physicsWorld = new World(new Vector2(0, 0), true);
      physicsWorld.setContactListener(this);
   }

   @Override
   protected void processEntities(ImmutableBag<Entity> entities)
   {
      //TODO: Change this delta to a float!
      float delta = (float)world.getDeltaInSecs();

      if (delta > MAX_DELTA_TIME)
      {
         delta = MAX_DELTA_TIME;
      }

      mAcc += delta;
      while (mAcc >= MINIMUM_TIME_STEP)
      {
         physicsWorld.step(MINIMUM_TIME_STEP, VELOCITY_ITER, POSITION_ITER);
         mAcc -= MINIMUM_TIME_STEP;
      }
   }

   @Override
   protected void added(Entity e)
   {
      Physics collidable = e.getComponent(Physics.class);
      Body body = collidable.getBody();
      body.setUserData(e);
//      physicsWorld.add(body); TODO: Can this be safely removed?  Once a body is created, it's automatically inserted into the world.
   }

   @Override
   protected void removed(Entity e)
   {
      Physics collidable = e.getComponent(Physics.class);
      physicsWorld.destroyBody(collidable.getBody());
   }

   private void handleBulletHittingTarget(Entity bullet, Entity target)
   {
      addDamageToTarget(target);

      Physics bp = bullet.getComponent(Physics.class);
      EntityFactory.createExplosion(world, bp.getX(), bp.getY());

      world.deleteEntity(bullet);
   }

   private void addDamageToTarget(Entity e)
   {
      Health h = e.getComponent(Health.class);

      // if this entity has health...
      if (h != null)
         h.addDamage(10f);
   }

   @Override
   public void beginContact(Contact contact)
   {
      Body bodyA = contact.getFixtureA().getBody();
      Body bodyB = contact.getFixtureB().getBody();

      Entity entityA = Entity.class.cast(bodyA.getUserData());
      Entity entityB = Entity.class.cast(bodyB.getUserData());

      String groupA = world.getGroupManager().getGroupOf(entityA);
      String groupB = world.getGroupManager().getGroupOf(entityB);

      // if a bullet impacts with a crate, tank or wall...
      if ("crates".equalsIgnoreCase(groupA) && "bullets".equalsIgnoreCase(groupB))
      {
         handleBulletHittingTarget(entityB, entityA);
      }
      else if ("crates".equalsIgnoreCase(groupB) && "bullets".equalsIgnoreCase(groupA))
      {
         handleBulletHittingTarget(entityA, entityB);
      }
      else if ("tanks".equalsIgnoreCase(groupB) && "bullets".equalsIgnoreCase(groupA))
      {
         handleBulletHittingTarget(entityA, entityB);
      }
      else if ("bullets".equalsIgnoreCase(groupB) && "tanks".equalsIgnoreCase(groupA))
      {
         handleBulletHittingTarget(entityB, entityA);
      }
      else if ("bullets".equalsIgnoreCase(groupB) && "walls".equalsIgnoreCase(groupA))
      {
         handleBulletHittingTarget(entityB, entityA);
      }
      else if ("walls".equalsIgnoreCase(groupB) && "bullets".equalsIgnoreCase(groupA))
      {
         handleBulletHittingTarget(entityA, entityB);
      }      
   }

   @Override
   public void endContact(Contact contact)
   {
      
   }

   @Override
   public void preSolve(Contact contact, Manifold oldManifold)
   {
      
   }

   @Override
   public void postSolve(Contact contact, ContactImpulse impulse)
   {
      
   }

}
