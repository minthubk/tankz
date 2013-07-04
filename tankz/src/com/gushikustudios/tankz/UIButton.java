package com.gushikustudios.tankz;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.gushikustudios.common.Assets;
import com.gushikustudios.common.GameConstants;

public class UIButton extends ImageButton
{
   public String mName;
   public Image mImage;
   
   /**
    * Non-toggle image button
    * 
    * @param imageBgDown
    * @param imageBgUp
    * @param imageFg
    * @param name
    * @param noToggle
    */
   public UIButton(TextureRegion imageBgDown, TextureRegion imageBgUp, TextureRegion imageFg,String name,boolean noToggle)
   {
      super(imageBgUp,imageBgDown);
      
      mName = name;
      
      Image image;
      
//      this.width = imageBgUp.getRegionWidth();
//      this.height = imageBgUp.getRegionHeight();
      
      if (imageFg != null)
      {
         image = new Image(imageFg);
         image.width = imageFg.getRegionWidth();
         image.height = imageFg.getRegionHeight();
         image.x = this.width / 2 - image.width / 2;
         image.y = this.height / 2 - image.height / 2;
         image.touchable = false;
         mImage = image;
         this.addActor(image);
      }
   }
   
   /**
    * Non-toggle imagebutton
    * 
    * @param imageBgDown
    * @param imageBgUp
    * @param imageFg
    * @param noToggle
    */
   public UIButton(TextureRegion imageBgDown, TextureRegion imageBgUp, TextureRegion imageFg, boolean noToggle)
   {
      this(imageBgDown,imageBgUp,imageFg,null,noToggle);
   }
   
   /**
    * Toggle image button
    * 
    * @param imageBgDown
    * @param imageBgUp
    * @param imageFg
    * @param name
    */
   public UIButton(TextureRegion imageBgDown, TextureRegion imageBgUp, TextureRegion imageFg,String name)
   {
      super(imageBgUp,imageBgDown,imageBgDown);
      
      mName = name;
      
      Image image;
      
      
//      if (imageBgUp != null)
//      {
//         this.width = imageBgUp.getRegionWidth();
//         this.height = imageBgUp.getRegionHeight();
//      }
//      else
//      {
//         this.width = imageFg.getRegionWidth();
//         this.height = imageFg.getRegionHeight();
//      }
      
      if (imageFg != null)
      {
         image = new Image(imageFg);
//         image.width = imageFg.getRegionWidth();
//         image.height = imageFg.getRegionHeight();
//         image.x = this.width / 2 - image.width / 2;
//         image.y = this.height / 2 - image.height / 2;
         image.touchable = false;
         this.addActor(image);
      }
   }
   
   /**
    * Toggle image button
    * 
    * @param imageBgDown
    * @param imageBgUp
    * @param imageFg
    */
   public UIButton(TextureRegion imageBgDown, TextureRegion imageBgUp, TextureRegion imageFg)
   {
      this(imageBgDown,imageBgUp,imageFg,null);
   }
   
   public static void buttonPressAction()
   {
      GameMain.getSound().play(Assets.mSndTickFlimbo);
      GameMain.getVibe().vibrate(GameConstants.BUTTON_VIBRATE_LENGTH);
   }
}
