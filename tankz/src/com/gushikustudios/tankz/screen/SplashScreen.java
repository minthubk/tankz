package com.gushikustudios.tankz.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.GLCommon;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.gushikustudios.common.Assets;
import com.gushikustudios.common.Settings;
import com.gushikustudios.tankz.GameMain;

public class SplashScreen implements Screen
{
   private SpriteBatch mBatch;
   
   private Texture mSplashBackground;
   private TextureRegion mSplashTextureRegion;
   
   private OrthographicCamera mCam;
   private Vector3 mCamPos;
   private eSplashState mState;
   private float mCumulativeDeltaTime;
   
   private enum eSplashState { PRE_ASSETS_LOAD, ASSETS_LOAD, POST_ASSETS_LOAD, SPLASH_END };
   
   private static final float SPLASH_SCREEN_DISPLAY_TIME = 1.0f;
   
   public SplashScreen()
   {
      mBatch = GameMain.getSpriteBatch();
      
      // create the camera
      mCam = new OrthographicCamera(Settings.NATIVE_WIDTH,Settings.NATIVE_HEIGHT);
      mCamPos = new Vector3(mCam.viewportWidth/2,mCam.viewportHeight/2,0);
      mCam.position.set(mCamPos);
      mCam.update();

      // other variables (state, etc.)
      mState = eSplashState.PRE_ASSETS_LOAD;
      
      // load in the background wallpaper
      mSplashBackground = Assets.loadTexture("data/logo.png");
      mSplashTextureRegion = new TextureRegion(mSplashBackground);
      
      mCumulativeDeltaTime = 0;
      
      Settings.mSkipMainMenuTweens = false;
      
   }

   @Override
   public void render(float delta)
   {
      mCumulativeDeltaTime += delta;
      
      switch (mState)
      {
         case PRE_ASSETS_LOAD:
            mState = eSplashState.ASSETS_LOAD;
            break;
            
         case ASSETS_LOAD: // at this point, the company game screen should be up
            Assets.load();
            mState = eSplashState.POST_ASSETS_LOAD;
            break;
            
         case POST_ASSETS_LOAD:
            if (mCumulativeDeltaTime > SPLASH_SCREEN_DISPLAY_TIME)
            {
               mCumulativeDeltaTime = 0;
               mState = eSplashState.SPLASH_END;
            }
            break;
            
         case SPLASH_END:
//            if (Settings.mSharedData != null)
//            {
//               Settings.mEditMode = true;
//               GameMain.getGame().setScreen(new PlayScreen());
//            }
//            else
            {
               GameMain.getGame().setScreen(new TankzScreen());
            }
            break;
            
         default:
            mState = eSplashState.PRE_ASSETS_LOAD;
            break;
      }
      
      
      if (mState != eSplashState.SPLASH_END)
      {
         GLCommon gl = Gdx.gl;
         gl.glClearColor(0,0,0,1);
         gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
         mCam.update();  // updates camera after any manipulations

         mBatch.setProjectionMatrix(mCam.combined);
         mBatch.enableBlending();
         mBatch.begin();
         mBatch.draw(mSplashTextureRegion, (mCam.viewportWidth - mSplashTextureRegion.getRegionWidth())/2, (mCam.viewportHeight - mSplashTextureRegion.getRegionHeight())/2);
         mBatch.end();
      }
   }

   @Override
   public void resize(int width, int height)
   {

   }

   @Override
   public void show()
   {
   }

   @Override
   public void hide()
   {
      dispose();
   }

   @Override
   public void pause()
   {

   }

   @Override
   public void resume()
   {

   }

   @Override
   public void dispose()
   {
      if (mSplashBackground != null)
      {
         mSplashBackground.dispose();
         mSplashBackground = null;
      }
      else
      {
//         Gdx.app.log("matchcard", "SplashScreen: trying to dispose a null texture.");
      }
   }

}
