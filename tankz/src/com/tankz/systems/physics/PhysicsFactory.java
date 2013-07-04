package com.tankz.systems.physics;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.PolygonShape;

public class PhysicsFactory
{

   /**
    * This factory method creates a dynamic physics body with a box shaped fixture.
    * 
    * Note: things like user data, position, and items relating to the fixture such
    * as restitution, friction, mask / category bits are NOT defined here and are left
    * to their default values!
    * 
    * @param physicsWorld - related physics world
    * @param hx - width extent
    * @param hy - height extent
    * @param density - density of the object
    * @return World defined body.
    */
   public static Body createBoxBody(float hx, float hy, float density)
   {
      PolygonShape shape = new PolygonShape();
      shape.setAsBox(hx, hy);
      
      BodyDef bd = new BodyDef();
      bd.type = BodyType.DynamicBody;
      bd.bullet = true;
      
      Body body = PhysicsSystem.getPhysicsWorld().createBody(bd);
      
      body.createFixture(shape, density); // attach the box shaped fixture to the body
      
      shape.dispose(); // libgdx requires that the shape is disposed when no longer needed
      
      return body;
   }
}
