package com.tankz.systems.rendering.spatials;

import com.artemis.ComponentType;
import com.artemis.ComponentTypeManager;
import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.gushikustudios.common.Assets;
import com.gushikustudios.tankz.GameMain;
import com.tankz.components.Physics;

public class Bullet extends Spatial
{
   private static Color color = new Color(51f / 255, 204f / 255, 69f / 255, 1);
   private static ComponentType type = ComponentTypeManager.getTypeFor(Physics.class);

   private Physics physics;

   public Bullet(World world, Entity owner)
   {
      super(world, owner);
   }

   @Override
   public void initalize()
   {
      physics = (Physics) owner.getComponent(type);
   }

   @Override
   public void render()
   {
      AtlasRegion img = Assets.mBullet;
      
      GameMain.getSpriteBatch().setColor(color);
      GameMain.getSpriteBatch().draw(img, physics.getX(), physics.getY(), 0, 0,
            img.getRegionWidth(), img.getRegionHeight(), 1, 1, physics.getRotation());
   }

}
