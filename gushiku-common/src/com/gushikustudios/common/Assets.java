package com.gushikustudios.common;

import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.scenes.scene2d.ui.List.ListStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class Assets
{
   /*
    * Fonts
    */
   
   /*
    * Texture atlas
    */
   public static TextureAtlas mGameAtlas;
   
   /*
    * Atlas regions
    */
   public static AtlasRegion mLogo;
   
   /*
    * HUD art
    */
   public static AtlasRegion mBarTick;
   public static AtlasRegion mCrossHair;
   public static AtlasRegion mHealthBarTick;
   public static AtlasRegion mHudBg;
   public static AtlasRegion mMinimapBg;
   public static AtlasRegion mStatusBar;
   
   /*
    * Arena art
    */
   public static AtlasRegion mTile;
   public static AtlasRegion mCrate;
   public static AtlasRegion mWall;

   /*
    * Tank art
    */
   public static AtlasRegion mBullet;
   public static AtlasRegion mTracks;
   public static AtlasRegion mTankTower;
   
   public static AtlasRegion mMammoth;
   public static AtlasRegion mTankBarrels;
   public static AtlasRegion mTankBody;
   public static AtlasRegion mRearTracks;
   public static AtlasRegion mFrontTrack;
   public static AtlasRegion mLeftFrontTrackCover;
   public static AtlasRegion mRightFrontTrackCover;
   public static AtlasRegion mShadow;
   
   /*
    * Sounds
    */
   public static GameSound mSndTickFlimbo;
   public static GameSound mSndSilence;
   public static GameSound mSndShoot;
   
   /*
    * Music
    */
   
   /*
    * Skins & fonts
    */
   public static Skin mSkin;
   
   /*
    * Particle effects
    */
//   public static ParticleEffect mExplosion1;
   
   /**
    * Creates a new texture instance of the passed file...
    * 
    * @param file
    * @return
    */
   public static Texture loadTexture(String file)
   {
      return new Texture(Gdx.files.internal(file));
   }


   /**
    * Loads in all game assets.
    * 
    */
   public static void load()
   {
      // 
      // Game sounds
      //
      {
         mSndTickFlimbo = new GameSound(Gdx.audio.newSound(Gdx.files.internal("data/TickFlimbo.ogg")));
         mSndSilence = new GameSound(Gdx.audio.newSound(Gdx.files.internal("data/Silence.ogg")));
         mSndShoot = new GameSound(Gdx.audio.newSound(Gdx.files.internal("data/shoot.wav")));
      }
      
      //
      // Fonts & UI Skin
      //
      {
         mSkin = new Skin(Gdx.files.internal("data/uiskin.json"), Gdx.files.internal("data/uiskin.png"));
         
//         ListStyle listStyle = new ListStyle(mSkin.getStyle("default-white", ListStyle.class));
//         listStyle.selectedPatch.setColor(GameConstants.mSubHeaderColor);
//         mSkin.addStyle("default-lightblue", listStyle);
         
      }
      
      //
      // Game textures
      //
      {
         TextureAtlas atlas;
         atlas = new TextureAtlas(Gdx.files.internal("data/pack"));
         mGameAtlas = atlas;
         
         mLogo = atlas.findRegion("logo");
         
         mBarTick = atlas.findRegion("bar");
         mCrossHair = atlas.findRegion("crosshair");
         mHealthBarTick = atlas.findRegion("healthBar");
         mHudBg = atlas.findRegion("hudBg");
         mMinimapBg = atlas.findRegion("minimapBg");
         mStatusBar = atlas.findRegion("statusBar");
         
         mTile = atlas.findRegion("tile");
         mCrate = atlas.findRegion("crate");
         mWall = atlas.findRegion("wall");
         
         mBullet = atlas.findRegion("bullet");
         mTracks = atlas.findRegion("tracks");
         mTankTower = atlas.findRegion("tower");
         mMammoth = atlas.findRegion("mammoth");
         mTankBarrels = atlas.findRegion("barrels");
         mTankBody = atlas.findRegion("base");
         mRearTracks = atlas.findRegion("rearTracks");
         mFrontTrack = atlas.findRegion("frontTrack");
         mLeftFrontTrackCover = atlas.findRegion("leftFrontTrackCover");
         mRightFrontTrackCover = atlas.findRegion("rightFrontTrackCover");
         mShadow = atlas.findRegion("shadow");
      }
      
      //
      // Particle Effects
      //
      {
//         mExplosion1 = new ParticleEffect();
//         mExplosion1.load(Gdx.files.internal("data/explosion1.p"), Assets.mGameAtlas);
      }
   }
}
