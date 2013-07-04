package com.tankz.systems.rendering;

import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.utils.ImmutableBag;
import com.badlogic.gdx.Graphics;
import com.gushikustudios.common.Assets;
import com.gushikustudios.tankz.GameMain;
import com.tankz.systems.camera.CameraSystem;

public class TerrainRenderSystem extends EntitySystem
{
   private Graphics g;
   private CameraSystem cs;

   public TerrainRenderSystem()
   {
      super();
   }

   @Override
   public void initialize()
   {
      cs = world.getSystemManager().getSystem(CameraSystem.class);
   }

   @Override
   protected void processEntities(ImmutableBag<Entity> entities)
   {
      // FIXME: Dig in and understand how this is working..
      
      float offsetX = cs.getStartX() % Assets.mTile.getRegionWidth();
      float offsetY = cs.getStartY() % Assets.mTile.getRegionHeight();

      // FIXME: Update the graphics rendering below. Need to dig in and see what this is doing...

      // I believe this is culling the terrain draw according to camera system view
      int tilesWidth = (int) Math.ceil(cs.getWidth() / Assets.mTile.getRegionWidth()) + 1;
      int tilesHeight = (int) Math.ceil(cs.getHeight() / Assets.mTile.getRegionHeight()) + 1;

      // camera should already be centered on the player, right?
      // g.scale(cs.getZoom(), cs.getZoom());
      //
      // g.translate(-offsetX, -offsetY);

      for (int x = -1; tilesWidth > x; x++)
      {
         for (int y = -1; tilesHeight > y; y++)
         {
            GameMain.getSpriteBatch().draw(Assets.mTile, x * Assets.mTile.getRegionWidth(),
                  y * Assets.mTile.getRegionHeight());
         }
      }

      // g.resetTransform();

   }

   @Override
   protected boolean checkProcessing()
   {
      return true;
   }

}
