package com.tankz.systems.rendering.spatials;

import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.gushikustudios.common.Assets;
import com.gushikustudios.tankz.GameMain;
import com.tankz.components.Physics;

public class Wall extends Spatial
{
   private static AtlasRegion img = null;

   static
   {
      img = Assets.mWall;
   }
   private Physics physics;

   public Wall(World world, Entity owner)
   {
      super(world, owner);
   }

   @Override
   public void initalize()
   {
      physics = owner.getComponent(Physics.class);
   }

   @Override
   public void render()
   {
      GameMain.getSpriteBatch().draw(img, physics.getX(), physics.getY(), 0, 0,
            img.getRegionWidth(), img.getRegionHeight(), 1, 1, physics.getRotation());
   }

}
