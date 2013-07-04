package com.gushikustudios.tankz;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.gushikustudios.common.ActivityRequestHandler;
import com.gushikustudios.common.Settings;

public class MainActivity extends AndroidApplication implements ActivityRequestHandler 
{
   private static GameMain mGameMain;
   
   private RelativeLayout mLayout;

   private static final int HIDE_ADS = 0;
   private static final int SHOW_ADS = 1;
   private static final int SHARE_DESIGN = 2;
   private static final int OPEN_URL = 3;
   private static final int GET_GALLERY_PIC = 4;
   private static final int PUT_AD_ON_TOP = 5;
   private static final int PUT_AD_ON_BOT = 6;

   private static final int SELECT_PICTURE = 1;

   /** Called when the activity is first created. */
   @Override
   public void onCreate(Bundle savedInstanceState)
   {
      super.onCreate(savedInstanceState);

      try
      {
         Settings.mVersionName = this.getPackageManager().getPackageInfo(this.getPackageName(), 0).versionName;

         int flags = this.getPackageManager().getPackageInfo(this.getPackageName(), 0).applicationInfo.flags;
         if ((flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0)
         {
            Settings.mDebugOn = true;
         }
      }
      catch (NameNotFoundException e)
      {
         Settings.mVersionName = new String("Beta Build");
      }

      // Do the stuff that initialize() would do for you
      requestWindowFeature(Window.FEATURE_NO_TITLE);
      getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                        WindowManager.LayoutParams.FLAG_FULLSCREEN);
      getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);

      AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
      config.useCompass = false;
      config.useAccelerometer = false;

      mLayout = new RelativeLayout(this);

      mGameMain = new GameMain(this);
      View gameView = initializeForView(mGameMain, config);

      // Add the libgdx view
      mLayout.addView(gameView);

      // Add the AdMob view
      RelativeLayout.LayoutParams adParams =
                new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                                RelativeLayout.LayoutParams.WRAP_CONTENT);
      adParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
      adParams.addRule(RelativeLayout.CENTER_IN_PARENT);
      
      adParams =
         new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                         RelativeLayout.LayoutParams.WRAP_CONTENT);
      adParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
      adParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
      
      // Hook it all up
      setContentView(mLayout);

   }

   protected Handler mHandler = new Handler()
   {
      @Override
      public void handleMessage(Message msg)
      {
         switch (msg.what)
         {
            case SHOW_ADS:
               break;

            case HIDE_ADS:
               break;
         }
      }
   };


   public void showAds(boolean show)
   {
      mHandler.sendEmptyMessage(show ? SHOW_ADS : HIDE_ADS);
   }
   
   public void reportError()
   {
      // TODO Auto-generated method stub

   }

   public void launchIfEnabled()
   {
      // TODO Auto-generated method stub

   }

   public void launchDashboard()
   {
      mHandler.sendEmptyMessage(OPEN_URL);
   }

   public void postScoreToLeaderboard(int score)
   {
      // TODO Auto-generated method stub

   }
}