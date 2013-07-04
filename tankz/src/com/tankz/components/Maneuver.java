package com.tankz.components;

import com.artemis.Component;

public class Maneuver extends Component
{
   private boolean mForward;
   private boolean mReverse;
   private boolean mTurnLeft;
   private boolean mTurnRight;
   private boolean mTurning;
 
   public boolean getTurning()
   {
      return mTurning;
   }
   
   public boolean getForward()
   {
      return mForward;
   }
   
   public boolean getReverse()
   {
      return mReverse;
   }
   
   public boolean getLeft()
   {
      return mTurnLeft;
   }
   
   public boolean getRight()
   {
      return mTurnRight;
   }
   
   public void setTurning(boolean t)
   {
      mTurning = t;
   }
   
   public void setForward(boolean f)
   {
      mForward = f;
   }
   
   public void setReverse(boolean r)
   {
      mReverse = r;
   }
   
   public void setLeft(boolean l)
   {
      mTurnLeft = l;
   }
   
   public void setRight(boolean r)
   {
      mTurnRight = r;
   }
   
   public void clear()
   {
      mForward = mReverse = mTurnLeft = mTurnRight = false;
   }
}
