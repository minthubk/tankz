package com.gushikustudios.tankz.behaviors;

import com.artemis.Entity;

public interface Behavior
{
   /**
    * @return true when this behavior should be active
    */
   boolean active(Entity e);
   
   /**
    * method executed iteratively when the behavior is active
    */
   void action(Entity e);
   
   /**
    * method executed when the behavior is suppressed (ie. another higher-priority behavior becomes active)
    */
   void suppress(Entity e);
}
