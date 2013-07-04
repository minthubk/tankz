package com.gushikustudios.common;

import com.badlogic.gdx.graphics.Color;


public class GameConstants 
{
   
   public static final int BUTTON_VIBRATE_LENGTH = 25;
   
   public static final String HOW_TO_PLAY =
      "Overview:\n \n";
   
   public static final String CREDITS_TEXT = 
      "Gushiku Studios LLC\n" +
      "www.gushikustudios.com\n \n" +
      "Contact us at\ngushikustudios@gmail.com\n \n" +
      "Lead Programmer & Artist:\nTim Scott\n \n" +
      "Game Engine:\nlibgdx\n \n" +
      "Animations:\nUniversal Tween Engine\n \n";
      
   public static final String [] MUSIC_TRACK_LIST =
   {
      "data/FeelGood.ogg",
      "data/FluffDuck.ogg",
      "data/PlainLoafer.ogg",
   };
   
   /*
    * Colors
    */
   public static final Color mMainHeaderColorText = Color.YELLOW;
   public static final Color mMainHeaderColor = new Color(57f/256,116f/256,172f/256,1);
   public static final Color mSubHeaderColorText = Color.YELLOW;
   public static final Color mSubHeaderColor = new Color(133f/256,175f/256,214f/256,1);
   
   /*
    * File dialog strings
    */
   public static final String HELP_SCREEN_TITLE = "HOW TO PLAY";
   
   public static final String EXIT_FROM_GAME = "EXIT FROM GAME?";
   public static final String CONFIRM = "Please confirm you wish to quit.";
   
   public static final String HELP_TEXT_RULES_HEADER = 
      "GAME RULES";
   
   public static final String[] HELP_TEXT_RULES =
   {
      "Other rules:\n"
   };
   
   public static final String HELP_DIFFICULTY_HEADER =
      "Difficulty Levels";
   public static final String HELP_DIFFICULTY_LEVELS =
      "Easy\n"+
      "Medium\n"+
      "Hard\n";

}
