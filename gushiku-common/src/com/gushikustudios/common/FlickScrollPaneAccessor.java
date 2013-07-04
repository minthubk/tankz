package com.gushikustudios.common;

import aurelienribon.tweenengine.TweenAccessor;

import com.badlogic.gdx.scenes.scene2d.ui.FlickScrollPane;

public class FlickScrollPaneAccessor implements TweenAccessor<FlickScrollPane>
{
   public static final int VERTICAL_SCROLL = 1;
   
   @Override
   public int getValues(FlickScrollPane target, int tweenType, float[] returnValues)
   {
      switch (tweenType)
      {
         case VERTICAL_SCROLL:
            returnValues[0] = target.getScrollY();
            return 1;
      }
      return 0;
   }

   @Override
   public void setValues(FlickScrollPane target, int tweenType, float[] newValues)
   {
      switch (tweenType)
      {
         case VERTICAL_SCROLL:
            if (newValues[0] < target.getMaxY())
            {
               target.setScrollY(newValues[0]);
            }
            else
            {
               target.setScrollY(target.getMaxY());
            }
            break;
            
         default:
            assert false;
            break;
      }
   }
}
