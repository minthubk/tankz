package com.gushikustudios.common;

import com.badlogic.gdx.Gdx;

public class VibeRenderer
{
   public void vibrate(int lengthInMilliseconds)
   {
      if (Prefs.mVibrate)
      {
         Gdx.input.vibrate(lengthInMilliseconds);
      }
   }
}