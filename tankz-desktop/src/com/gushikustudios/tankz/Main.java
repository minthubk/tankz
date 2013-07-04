package com.gushikustudios.tankz;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.gushikustudios.common.ActivityRequestHandler;
import com.gushikustudios.common.Settings;

public class Main implements ActivityRequestHandler
{
   static final String GAME_NAME = "Tankz";

   static Main application;

   /**
    * @param args
    */
   public static void mainLaunch(int width, int height)
   {
      LwjglApplication lwjglA = new LwjglApplication(new GameMain(application), GAME_NAME, width, height, false);
      lwjglA.getGraphics().getConfig().resizable = false;
   }

   public static void main(String[] args)
   {

      if (application == null)
      {
         application = new Main();
      }

      SwingUtilities.invokeLater(new Runnable()
      {
         @Override
         public void run()
         {
            // -------------------------------------------------------------
            // Display mode selection
            // -------------------------------------------------------------

            // String[] modes = { "portrait", "landscape" };
            String modeResult = new String("landscape");
            // String modeResult = (String)JOptionPane.showInputDialog(
            // null,
            // "Select the display mode",
            // "Initialization",
            // JOptionPane.PLAIN_MESSAGE,
            // null,
            // modes,
            // "landscape");

            // -------------------------------------------------------------
            // Resolution selection
            // -------------------------------------------------------------

            String[] resolutions =
            {
                  "HVGA (320x480)",
                  "WVGA800 (480x800)",
                  "WVGA854 (480x854)",
                  "Galaxy Tab (600x1024)",
                  "Nook Color (565x1024)",
                  "Motorola Xoom (800x1280)" };
            String resolutionResult = (String) JOptionPane.showInputDialog(
                  null,
                  "Select your display",
                  "Initialization",
                  JOptionPane.PLAIN_MESSAGE,
                  null,
                  resolutions,
                  "WVGA800 (480x800)");
//             String resolutionResult = new String("WVGA800 (480x800)");

            // -------------------------------------------------------------
            // App launch
            // -------------------------------------------------------------

            boolean isPortrait = modeResult.equals("portrait");
            
            Settings.mVersionName = new String("X.X (Desktop)");
            Settings.mDebugOn = true;

            if (resolutionResult != null && resolutionResult.length() > 0)
            {
               Matcher m = Pattern.compile("(\\d+)x(\\d+)").matcher(resolutionResult);
               m.find();
               int w = Integer.parseInt(m.group(isPortrait ? 1 : 2));
               int h = Integer.parseInt(m.group(isPortrait ? 2 : 1));
               mainLaunch(w, h);
            }
         }
      });
}

   @Override
   public void showAds(boolean show)
   {
      // TODO Auto-generated method stub
      
   }

   @Override
   public void reportError()
   {
      // TODO Auto-generated method stub
      
   }

   @Override
   public void launchIfEnabled()
   {
      // TODO Auto-generated method stub
      
   }

   @Override
   public void launchDashboard()
   {
      // TODO Auto-generated method stub
      
   }

   @Override
   public void postScoreToLeaderboard(int score)
   {
      // TODO Auto-generated method stub
      
   }
}
