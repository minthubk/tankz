package com.gushikustudios.common;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Align;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;

public class TextEntry extends Button
{
   public TextEntry(String string,Color textColor,Color bgColor,boolean center)
   {
      super(Assets.mSkin.getStyle("default-round-white", ButtonStyle.class));
      Label label;
      this.touchable = false;
      this.color.set(bgColor);
      this.add(label = new Label(string,Assets.mSkin.getStyle("default-white",LabelStyle.class))).fill().expandX();
      label.setWrap(true);
      if (center)
      {
         label.setAlignment(Align.CENTER);
      }
      label.setColor(textColor);
   }
   
   public TextEntry(String [] string,Color [] textColor,Color bgColor,boolean center)
   {
      super(Assets.mSkin.getStyle("default-round-white", ButtonStyle.class));
      Label label;
      this.touchable = false;
      this.color.set(bgColor);
      for (int j = 0; j < string.length; j++)
      {
         this.row();
         this.add(label = new Label(string[j],Assets.mSkin.getStyle("default-white",LabelStyle.class))).fill().expandX();
         label.setWrap(true);
         if (center)
         {
            label.setAlignment(Align.CENTER);
         }
         label.setColor(textColor[j]);
      }
   }
}
