package com.gushikustudios.common;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;

public class MusicRenderer
{
   private static int mState;
   
   private int mTrackIndex;
   
   private Music mMusic;
   
   private final static int DISABLED = 0;
   private final static int ENABLED = 1;
   
   private final static float DEFAULT_VOLUME = 0.5f;
   
   public MusicRenderer()
   {
      mTrackIndex = 0;
      mState = ENABLED;
   }
   
   private Music getNextTrack()
   {
      mTrackIndex++;
      
      // if walked off of track list
      if (mTrackIndex >= GameConstants.MUSIC_TRACK_LIST.length)
      {
         mTrackIndex = 0;
      }
      
      return Gdx.audio.newMusic(Gdx.files.internal(GameConstants.MUSIC_TRACK_LIST[mTrackIndex]));
   }

   public void play()
   {
      if (Prefs.mMusicIsOn)
      {
         if (mMusic != null)
         {
            if (mMusic.isPlaying())
            {
               return;
            }
            else
            {
               mMusic.dispose();
               mMusic = getNextTrack();
               if (mMusic != null)
               {
                  mMusic.setVolume(DEFAULT_VOLUME);
                  mMusic.play();
               }
            }
         }
         else
         {
            mMusic = getNextTrack();
            
            if (mMusic != null)
            {
               mMusic.setVolume(DEFAULT_VOLUME);
               mMusic.play();
            }
         }
      }
      else
      {
         stop();
      }
   }
   
   public void stop()
   {
      if (mMusic != null)
      {
         if (mMusic.isPlaying())
         {
            mMusic.stop();
         }
         mMusic.dispose();
         mMusic = null;
      }
   }
   
   public void disable()
   {
      stop();
      mState = DISABLED;
   }
   
   public void enable()
   {
      mState = ENABLED;
   }
   
   public void render(float delta)
   {
      if (mState == ENABLED)
      {
         play();
      }
   }
}
