package com.gushikustudios.common;

import aurelienribon.tweenengine.TweenAccessor;

import com.badlogic.gdx.scenes.scene2d.Actor;

public class ActorAccessor implements TweenAccessor<Actor>
{
   public static final int POSITION_X = 1;
   public static final int POSITION_Y = 2;
   public static final int POSITION_XY = 3;
   public static final int POSITION_XY_ROTATION = 4;
   public static final int ALPHA_LEVEL = 5;
   public static final int SCALE_AMOUNT = 6;

   @Override
   public int getValues(Actor target, int tweenType, float[] returnValues)
   {
      switch (tweenType)
      {
         case POSITION_X: returnValues[0] = target.x; return 1;
         case POSITION_Y: returnValues[0] = target.y; return 1;
         case POSITION_XY:
            returnValues[0] = target.x;
            returnValues[1] = target.y;
            return 2;
            
         case POSITION_XY_ROTATION:
            returnValues[0] = target.x;
            returnValues[1] = target.y;
            returnValues[2] = target.rotation;
            return 3;
            
         case ALPHA_LEVEL:
            returnValues[0] = target.color.a;   
            return 1;
            
         case SCALE_AMOUNT:
            returnValues[0] = target.scaleX;
            returnValues[1] = target.scaleY;
            return 2;
            
         default:
               assert false; return -1;
      }
   }

   @Override
   public void setValues(Actor target, int tweenType, float[] newValues)
   {
      switch (tweenType)
      {
         case POSITION_X: target.x = (int)newValues[0]; break;
         case POSITION_Y: target.y = (int)newValues[0]; break;
         case POSITION_XY:
            target.x = (int)newValues[0];
            target.y = (int)newValues[1];
            break;
            
         case POSITION_XY_ROTATION:
            target.x = (int)newValues[0];
            target.y = (int)newValues[1];
            target.rotation = newValues[2];
            break;
            
         case ALPHA_LEVEL:
            target.color.a = newValues[0];
            break;
            
         case SCALE_AMOUNT:
            target.scaleX = newValues[0];
            target.scaleY = newValues[1];
            break;
            
         default:
            assert false;
            break;
      }
   }
}
