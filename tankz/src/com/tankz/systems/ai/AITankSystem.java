package com.tankz.systems.ai;

import com.artemis.ComponentType;
import com.artemis.ComponentTypeManager;
import com.artemis.Entity;
import com.badlogic.gdx.utils.Array;
import com.gushikustudios.tankz.behaviors.Behavior;
import com.tankz.components.BrainAI;
import com.tankz.systems.player.PlayerTankMovementSystem;

public class AITankSystem extends PlayerTankMovementSystem
{
   private static ComponentType BRAIN_AI_TYPE = ComponentTypeManager.getTypeFor(BrainAI.class);
   
   public AITankSystem()
   {
      super(BrainAI.class);
   }
   
   @Override
   protected void process(Entity e)
   {
      processBehaviors(e);
      updatePlayer(e);
   }

   private void processBehaviors(Entity e)
   {
      BrainAI brainAI = (BrainAI)e.getComponent(BRAIN_AI_TYPE);
      
      if (brainAI != null)
      {
         Array<Behavior> behaviors = brainAI.getBehaviors();
         
         if (behaviors != null)
         {
            Behavior activeBehavior = brainAI.getActiveBehavior();
            for (int i = 0; i < behaviors.size ; i++)
            {
               Behavior b = behaviors.get(i);
               
               if (b.active(e))
               {
                  // if this is the previously active behavior...
                  if (b == activeBehavior)
                  {
                     // run its action and quit...
                     activeBehavior.action(e);
                     return;
                  }
                  else 
                  {
                     // suppress any previously active behaviors...
                     if (activeBehavior != null)
                     {
                        activeBehavior.suppress(e);
                     }
                     activeBehavior = b;
                     activeBehavior.action(e);
                     brainAI.setActiveBehavior(activeBehavior);
                     return;
                  }
               }
            }
            
            // operation will only get to here if no behaviors are active..
            if (activeBehavior != null)
            {
               activeBehavior.suppress(e);
               brainAI.setActiveBehavior(null);
            }
         }
      }
      else
      {
         System.out.println("Internal error detected in processBehaviors!!!");
      }
   }
}
