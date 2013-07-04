package com.gushikustudios.common;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class Prefs
{
   private static Preferences mPrefs;
   private static boolean mDirtySettings;
   
   public static float mMusicVolume;
   public static float mFXVolume;
   
   public static boolean mMusicIsOn;
   public static boolean mFXIsOn;
   
   public static int mHighScore;
   
   public static boolean mVibrate;
   
   public static void load()
   {
      mPrefs = Gdx.app.getPreferences(".tankz");
      
      if (mPrefs != null)
      {
         mFXIsOn = mPrefs.getBoolean("mFXIsOn",true);
         mFXVolume = mPrefs.getFloat("mFXVolume",1.0f);
         mMusicIsOn = mPrefs.getBoolean("mMusicIsOn",true);
         mMusicVolume = mPrefs.getFloat("mMusicVolume",1.0f);
         mHighScore = mPrefs.getInteger("mHighScore",5);
         mVibrate = mPrefs.getBoolean("mVibrate",true);
      }
      else
      {
         setDefaults();
      }
   }
   
   public static void setDirty()
   {
      mDirtySettings = true;
   }
   
   public static void writeOnDirty()
   {
      if (mDirtySettings)
      {
         mDirtySettings = false;
         save();
      }
   }
   
   public static void save()
   {
      if (mPrefs != null)
      {
          mPrefs.putBoolean("mFXIsOn",mFXIsOn);
          mPrefs.putFloat("mFXVolume",mFXVolume);
          mPrefs.putBoolean("mMusicIsOn",mMusicIsOn);
          mPrefs.putFloat("mMusicVolume",mMusicVolume);
          mPrefs.putInteger("mHighScore", mHighScore);
          mPrefs.putBoolean("mVibrate", mVibrate);
          
          mPrefs.flush();
      }
   }

   private static void setDefaults()
   {
      // set up defaults...
      mFXIsOn = true;
      mFXVolume = 1;
      
      mMusicIsOn = true;
      mMusicVolume = 1;
      mVibrate = true;
      
      mHighScore = 0;
   }
   
}
