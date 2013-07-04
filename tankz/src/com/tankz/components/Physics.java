package com.tankz.components;

import com.artemis.Component;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;

/**
 * This is a physics component for a body.
 * 
 * @author tescott
 *
 */
public class Physics extends Component
{
   private Body body;

   public Physics(Body body)
   {
      this.body = body;
   }

   public float getX()
   {
      return body.getPosition().x;
   }

   public float getY()
   {
      return body.getPosition().y;
   }

   /**
    * @return body rotation in degrees
    */
   public float getRotation()
   {
      return (MathUtils.radiansToDegrees * body.getAngle());
   }

   public void setLocation(float x, float y)
   {
      body.setTransform(x, y, body.getAngle());
   }

   /**
    * @return body rotation in radians
    */
   public float getRotationInRadians()
   {
      return (body.getAngle());
   }

   /**
    * Adds rotation to object by angle degrees. Constrains the rotation to 360 degrees / 2 PI
    * 
    * @param angle
    *           Angle to add in degrees.
    */
   public void addRotation(float angle)
   {
      body.setTransform(body.getPosition(), (body.getAngle() + MathUtils.degreesToRadians * angle) % MathUtils.PI * 2);
   }

   /**
    * Applies a force of the specified x & y magnitudes to the center of the body..
    * 
    * @param xf
    * @param yf
    */
   public void setForce(float xf, float yf)
   {
      body.applyForceToCenter(xf, yf); // body.setForce(xf, yf);
   }

   public Body getBody()
   {
      return body;
   }

}
