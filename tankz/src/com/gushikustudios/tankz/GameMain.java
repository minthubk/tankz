package com.gushikustudios.tankz;

import aurelienribon.tweenengine.Tween;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.FlickScrollPane;
import com.gushikustudios.common.Achievements;
import com.gushikustudios.common.ActivityRequestHandler;
import com.gushikustudios.common.ActorAccessor;
import com.gushikustudios.common.FlickScrollPaneAccessor;
import com.gushikustudios.common.MusicRenderer;
import com.gushikustudios.common.Prefs;
import com.gushikustudios.common.SoundRenderer;
import com.gushikustudios.common.VibeRenderer;
import com.gushikustudios.tankz.screen.SplashScreen;

public class GameMain extends Game 
{
   private static ActivityRequestHandler mUIHandler; // this is the handler that posts from libgdx -> UI thread
   private static SpriteBatch mBatch; 
   private static GameMain mGame;
   private static SoundRenderer mSoundRenderer;
   private static MusicRenderer mMusicRenderer;
   private static Achievements mAchievements;
   private static VibeRenderer mVibe;
   
   public GameMain(ActivityRequestHandler handler)
   {
      super();
      mGame = this;
      mUIHandler = handler;
      Tween.registerAccessor(Actor.class, new ActorAccessor());
      Tween.registerAccessor(FlickScrollPane.class, new FlickScrollPaneAccessor());
      Tween.setWaypointsLimit(16);
   }

   @Override
   public void create()
   {
      mBatch = new SpriteBatch();
      mSoundRenderer = new SoundRenderer();
      mMusicRenderer = new MusicRenderer();
      mAchievements = new Achievements();
      mVibe = new VibeRenderer();
      Prefs.load();
      setScreen(new SplashScreen());
   }
   
   @Override
   public void render()
   {
      // ensure minimum delta....
      float delta = Math.min(Gdx.graphics.getDeltaTime(), 1/30.0f);
      if (getScreen() != null) getScreen().render(delta);
      mSoundRenderer.render(delta); // sound hack for lame ass sound implementation.
//      mMusicRenderer.render(delta);
   }
   
   public static SpriteBatch getSpriteBatch()
   {
      return mBatch;
   }
   
   public static SoundRenderer getSound()
   {
      return mSoundRenderer;
   }
   
   public static MusicRenderer getMusic()
   {
      return mMusicRenderer;
   }
   
   public static GameMain getGame()
   {
      return mGame;
   }
   
   public static ActivityRequestHandler getUIHandler()
   {
      return mUIHandler;
   }
   
   public static Achievements getAch()
   {
      return mAchievements;
   }
   
   public static VibeRenderer getVibe()
   {
      return mVibe;
   }
}
