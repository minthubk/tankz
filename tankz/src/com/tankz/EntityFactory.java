package com.tankz;

import com.artemis.Entity;
import com.artemis.World;
import com.artemis.utils.TrigLUT;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.gushikustudios.common.GameSound;
import com.tankz.components.Ammo;
import com.tankz.components.Expiration;
import com.tankz.components.Health;
import com.tankz.components.Physics;
import com.tankz.components.SoundFile;
import com.tankz.components.SpatialForm;
import com.tankz.components.Tower;
import com.tankz.components.Transform;
import com.tankz.components.TurnFactor;
import com.tankz.components.Velocity;
import com.tankz.systems.physics.Material;
import com.tankz.systems.physics.PhysicsFactory;
import com.tankz.systems.physics.PhysicsInfo;

public class EntityFactory
{
   /**
    * Explosions consist of the following components:<br>
    * - <strong>Transform</strong>:<br>
    * - <strong>SpatialForm</strong>:<br>
    * - <strong>Expiration</strong>: How long to display the explosion
    * 
    * @param entityWorld
    * @param x
    * @param y
    * @return
    */
   public static Entity createExplosion(World entityWorld, float x, float y)
   {
      Entity e = entityWorld.createEntity();
      e.addComponent(new Transform(x, y));
      e.addComponent(new SpatialForm("explosion"));
      e.addComponent(new Expiration(200));
      e.refresh();
      return e;
   }

   /**
    * Bullets consist of the following components:<br>
    * - <strong>Transform</strong>:<br>
    * - <strong>SpatialForm</strong>:<br>
    * - <strong>Expiration</strong>:<br>
    * - <strong>Physics Body</strong>:<br>
    * 
    * @param entityWorld
    * @param x
    * @param y
    * @param angleDeg
    * @param shooter
    *           - Used to prevent tank from shooting itself
    * @return
    */
   public static Entity createBullet(World entityWorld, float x, float y, float angleDeg, Entity shooter)
   {
      Entity e = entityWorld.createEntity();
      e.setGroup("bullets");
      Transform transform = new Transform(x, y, angleDeg);
      e.addComponent(transform);
      e.addComponent(new SpatialForm("bullet"));
      e.addComponent(new Expiration(1500));

      Body b = PhysicsFactory.createBoxBody(0.5f, 0.5f, 0.2f); //FIXME- Reference materials list for density
      b.setBullet(true);
      b.setUserData(e);
      Fixture fix = b.getFixtureList().get(0);

      // Prevent bullets from colliding with the shooter!
      // b.addExcludedBody(shooter.getComponent(Physics.class).getBody());
      Body srcBody = shooter.getComponent(Physics.class).getBody();
      Fixture srcFix = srcBody.getFixtureList().get(0);
      Filter srcFilter = srcFix.getFilterData();

      // TODO: Make it so you can snipe opponents bullets.
      // Collide with everything except other bullets and the shooter of this bullet.
      Filter filter = fix.getFilterData(); // b.setBitmask(1);
      filter.categoryBits = PhysicsInfo.BULLET_CATEGORY;
      filter.maskBits = (short) (PhysicsInfo.ALL_CATEGORIES & (~(srcFilter.categoryBits | PhysicsInfo.BULLET_CATEGORY)));
      fix.setFilterData(filter);

      b.setTransform(x, y, angleDeg*MathUtils.degreesToRadians); // b.setPosition(x,y); b.setRotation(angle);
      fix.setRestitution(0); // b.setRestitution(0);
      b.setLinearDamping(0.002f);// b.setDamping(0.002f);
      fix.setFriction(10); // b.setFriction(10);

      // TODO: Make a pool of bullets to avoid new allocations!
      b.setLinearVelocity(new Vector2(1000f * TrigLUT.cosDeg(angleDeg), 1000f * TrigLUT.sinDeg(angleDeg))); // b.adjustVelocity(new
                                                                                                      // Vector2(1000f *
                                                                                                      // TrigLUT.cosDeg(angle),
                                                                                                      // 1000f *
                                                                                                      // TrigLUT.sinDeg(angle)));

      e.addComponent(new Physics(b));

      e.refresh();

      return e;
   }

   /**
    * Walls are really keep-out / indestructible items. They consist of the following components:<br>
    * - <strong>SpatialForm</strong>:<br>
    * - <strong>Physics body</strong>:<br>
    * 
    * @param entityWorld
    * @param x
    *           - center point of box shape
    * @param y
    *           - center point of box shape
    * @return entity of the new object
    */
   public static Entity createWall(World entityWorld, float x, float y)
   {
      return (createWall(entityWorld, x, y, 214f/20, 214f/20, true));
   }

   /**
    * @param entityWorld
    * @param x
    *           - center point of box shape
    * @param y
    *           - center point of box shape
    * @param sizeX
    *           - half-width extent
    * @param sizeY
    *           - half-height extent
    * @param hasSpatial
    *           - defines whether this entity has a drawn object associated with it
    * @return
    */
   public static Entity createWall(World entityWorld, float x, float y, float sizeX, float sizeY, boolean hasSpatial)
   {
      Entity e = entityWorld.createEntity();
      e.setGroup("walls");

      if (hasSpatial)
      {
         SpatialForm form = new SpatialForm("wall");
         e.addComponent(form);
      }

      Body b = PhysicsFactory.createBoxBody(sizeX/2, sizeY/2, 0.3f); //FIXME- Reference materials list for density
      // A StaticBody is immovable by definition.
      b.setType(BodyType.StaticBody); // b.setMoveable(false);
      b.setFixedRotation(true); // b.setRotatable(false);
      b.setUserData(e);
      b.setTransform(x, y, 0); // b.setPosition(x, y);
      b.setLinearDamping(0.1f); // b.setDamping(0.1f);

      b.setAngularDamping(10); // FIXME: Is this even needed, since this is a static body?  Seems redundant.
      Fixture fix = b.getFixtureList().get(0);
      fix.setRestitution(0);
      fix.setFriction(100);
      e.addComponent(new Physics(b));

      e.refresh();

      return e;
   }

   /**
    * Crates are destructible items. They consist of the following components:<br>
    * - <strong>Health</strong>: crates can be destroyed and therefore have health.<br>
    * - <strong>SpatialForm</strong>:<br>
    * - <strong>Physics Body</strong>:<br>
    * 
    * @param entityWorld
    * @param x
    * @param y
    * @param angleDeg
    * @return
    */
   
   private static final float CRATE_SIZE = 1.5f;
   private static final float CRATE_VARIANCE = 0.75f;
   
   public static Entity createCrate(World entityWorld, float x, float y, float angleDeg)
   {
      Entity e = entityWorld.createEntity();

      e.setGroup("crates");
      e.addComponent(new Health(100, 160));

      SpatialForm form = new SpatialForm("crate");
      e.addComponent(form);
      
      float dim = MathUtils.random(CRATE_SIZE, CRATE_SIZE+CRATE_VARIANCE);
      Body b = PhysicsFactory.createBoxBody(dim/2, dim/2, Material.METAL.density());
      
      b.setUserData(e); // back reference
      b.setTransform(x, y, angleDeg*MathUtils.degreesToRadians); // b.setPosition(x, y);       b.setRotation(angleDeg);
      b.setLinearDamping(10f); // b.setDamping(0.1f);
      b.setAngularDamping(10f); // b.setRotDamping(10f);
      
      Fixture fix = b.getFixtureList().get(0);
      fix.setRestitution(0); // b.setRestitution(0);
      fix.setFriction(100); // b.setFriction(100);

      e.addComponent(new Physics(b));

      e.refresh();
      
      return e;
   }

   /**
    * Mammoth tanks are destructible items. They contain the following components:<br>
    * - <strong>SpatialForm</strong>:<br>
    * - <strong>Velocity</strong>: ? Why isn't this simply part of the physics???<br>
    * - <strong>TurnFactor</strong>:<br>
    * - <strong>Tower</strong>: The turret<br>
    * - <strong>Health</strong>: How much health the tank has<br>
    * - <strong>Ammo</strong>: Current and maximum ammo<br>
    * - <strong>Physics body</strong:<br>
    * 
    * @param entityWorld
    * @param x
    * @param y
    * @return
    */
   public static Entity createMammothTank(World entityWorld, float x, float y)
   {
      Entity e = entityWorld.createEntity();
      e.setGroup("tanks");

      e.addComponent(new SpatialForm("mammothTank"));
      e.addComponent(new Velocity());
      e.addComponent(new TurnFactor());
      e.addComponent(new Tower());
      e.addComponent(new Health(110, 150));
      e.addComponent(new Ammo(78, 150));

      Body b = PhysicsFactory.createBoxBody(6.25f/2, 5.2f/2, 1f); //FIXME- Reference materials list for density
      b.setUserData(e);
      b.setTransform(x, y, 0);
      b.setLinearDamping(10f);
      b.setAngularDamping(50f);
      Fixture fix = b.getFixtureList().get(0);
      fix.setRestitution(0);
      fix.setFriction(100);
      e.addComponent(new Physics(b));

      return e;
   }

   /**
    * A sound has the following components:<br>
    * - <strong>SoundFile</strong>:<br>
    * 
    * @param entityWorld
    * @param soundFileName
    * @return
    */
   public static Entity createSound(World entityWorld, GameSound soundFileName)
   {
      //FIXME: Use a pool for creating these entities???
      Entity sound = entityWorld.createEntity();
      sound.addComponent(new SoundFile(soundFileName));
      sound.refresh();
      return sound;
   }
}
