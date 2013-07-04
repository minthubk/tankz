package com.gushikustudios.common;

import com.badlogic.gdx.audio.Sound;

public class GameSound
{
   private Sound mSound;
   private int mState;
   private float mVol;
   private long mSoundId;
   private float mPitch;
   
   public static int STOPPED = 0;
   public static int PLAYING = 1;
   public static int LOOPING = 2;
   public static int PAUSED = 3;
   
   public GameSound(Sound sound)
   {
      mSound = sound;
      mState = STOPPED;
   }
   
   public void play()
   {
      play(1);
   }
   
   public void play(float pitch, float vol)
   {
      if (mSound != null)
      {
         if ((pitch >= 0.5f) && (pitch <= 2.0f))
         {
            mSoundId = mSound.play(vol, pitch, 0);
            mVol = vol;
            mState = PLAYING;
         }
         else
         {
            play(vol); // play at default..
         }
      }
   }
   
   public void play(float vol)
   {
      if (mSound != null)
      {
         mSoundId = mSound.play(vol);
         mVol = vol;
         mState = PLAYING;
      }
   }
   
   public void stop()
   {
      if (mState != STOPPED)
      {
         if (mSound != null)
         {
            mSound.stop();
            mSoundId = 0;
         }
      }
      mState = STOPPED;
   }
   
   public void pause()
   {
      if (mState == LOOPING)
      {
         if (mSound != null)
         {
            mSound.stop();
            mSoundId = 0;
         }
         mState = PAUSED;
      }
   }
   
   public void resume()
   {
      if (mState == PAUSED)
      {
         if (mSound != null)
         {
            mSoundId = mSound.loop(mVol);
         }
         mState = LOOPING;
      }
   }
   
   public void loop(float vol)
   {
      if (mSound != null)
      {
         mSoundId = mSound.loop(vol);
         mVol = vol;
      }
      mState = LOOPING;
   }
   
   public void loop()
   {
      loop(1);
      mState = LOOPING;
   }
   
   public boolean loopSingle()
   {
      return (loopSingle(1));
   }
   
   
   /**
    * Ensures that only a singly looped sound is played.  Useful for higher level functions that repeatedly execute loop, but only desire
    * one instance of looping sound.
    * 
    * @param vol - Volume.  I'll let you guess what this does.
    * @return true if the sound was looped.  False otherwise, indicating that the sound is already looping
    */
   public boolean loopSingle(float vol)
   {
      if ((mState != LOOPING) && (mState != PAUSED))
      {
         loop(vol);
         return true; // sound was added
      }
      return false;
   }
   
   public void loopSingleSetVol(float vol)
   {
      if (mSoundId != 0)
      {
         mSound.setVolume(mSoundId, (mVol = vol));
      }
   }
   
   public void loopSingleSetPitch(float pitch)
   {
      if (mSoundId != 0)
      {
         mSound.setPitch(mSoundId, (mPitch = pitch));
      }
   }
   
   public void loopSingleSetVolAndPitch(float vol, float pitch)
   {
      if (mSoundId != 0)
      {
         mSound.setVolume(mSoundId, (mVol = vol));
         mSound.setPitch(mSoundId, (mPitch = pitch));
      }
   }
   
   public float getPitch()
   {
      return mPitch;
   }
   
   public void gracefulStop()
   {
      if (mSoundId != 0)
      {
         mSound.setLooping(mSoundId, false);
         mSoundId = 0;
      }
   }

}
