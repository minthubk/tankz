package com.tankz.components;

import com.artemis.Component;
import com.badlogic.gdx.utils.Array;
import com.gushikustudios.tankz.behaviors.Behavior;

public class BrainAI extends Component
{
   private Array<Behavior> mBehaviors;
   private Behavior mActiveBehavior;
   
   public BrainAI(Array<Behavior> behaviors)
   {
      mBehaviors = behaviors;
   }
   
   public Array<Behavior> getBehaviors()
   {
      return mBehaviors;
   }
   
   public Behavior getActiveBehavior()
   {
      return mActiveBehavior;
   }
   
   public void setActiveBehavior(Behavior behavior) // TODO: Would this be better served using an index to prevent a behavior not in mBehaviors from being activated???
   {
      mActiveBehavior = behavior;
   }
}
