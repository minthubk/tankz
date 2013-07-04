package com.tankz.systems.camera;

import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.utils.ImmutableBag;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.gushikustudios.common.Settings;
import com.tankz.components.Physics;
import com.tankz.systems.misc.BoundarySystem;

public class CameraSystem extends EntitySystem implements InputProcessor
{

   private BoundarySystem boundarySystem;

   private Entity player;

   private ComponentMapper<Physics> physicsMapper;

   private float lookAtX;
   private float lookAtY;

   private float targetZoom;
   private float zoom;

   private int screenWidth;
   private int screenHeight;

   private static OrthographicCamera mCam;
   private Vector3 mCamPos;
   
   private static final float MIN_CAMERA_ZOOM = 0.01f;

   public CameraSystem()
   {
      super();
   }

   @Override
   public void initialize()
   {
      physicsMapper = new ComponentMapper<Physics>(Physics.class, world);

      ensurePlayerEntity();

      boundarySystem = world.getSystemManager().getSystem(BoundarySystem.class);

      zoom = targetZoom = 0.1f;

      screenWidth = Gdx.app.getGraphics().getWidth();
      screenHeight = Gdx.app.getGraphics().getHeight();

//      mCam = new OrthographicCamera(Settings.NATIVE_WIDTH, Settings.NATIVE_HEIGHT);
      
      /*
       * This approach does not scale the view to the device.  Instead, it maintains a constant 
       */
      mCam = new OrthographicCamera(screenWidth, screenHeight);
      mCamPos = new Vector3(mCam.viewportWidth / 2, mCam.viewportHeight / 2, 0);
      mCam.position.set(mCamPos);
      mCam.zoom = 0.1f;
      mCam.update();
   }
   
   public static OrthographicCamera getCam()
   {
      return mCam;
   }

   @Override
   protected void processEntities(ImmutableBag<Entity> entities)
   {
      ensurePlayerEntity();

      if (player != null)
      {
         updatePosition();
         updateZoom();
//         constrainToBoundaries();
         
         mCamPos.x = lookAtX;
         mCamPos.y = lookAtY;
         
         mCam.position.set(mCamPos);
         mCam.update();
      }
   }

   private void updatePosition()
   {
      Physics physics = physicsMapper.get(player);
      lookAtX = physics.getX();
      lookAtY = physics.getY();
   }

   private void updateZoom()
   {
//      if (targetZoom != zoom)
//      {
//         if (targetZoom > zoom)
//         {
//            zoom += 0.0005f * (float) world.getDelta();
//            if (zoom > targetZoom)
//            {
//               zoom = targetZoom;
//            }
//         }
//         else
//         {
//            zoom -= 0.0005f * (float) world.getDelta();
//            if (zoom < targetZoom)
//            {
//               zoom = targetZoom;
//            }
//         }

         // FIXME: Correct zoom scaling here..
         // input.setScale(1f / zoom, 1f / zoom);
//      }
   }

   private void constrainToBoundaries()
   {
      if (getEndX() > boundarySystem.getBoundaryWidth())
      {
         lookAtX -= getEndX() - boundarySystem.getBoundaryWidth();
      }
      else if (getStartX() < 0)
      {
         lookAtX -= getStartX();
      }

      if (getEndY() > boundarySystem.getBoundaryHeight())
      {
         lookAtY -= getEndY() - boundarySystem.getBoundaryHeight();
      }
      else if (getStartY() < 0)
      {
         lookAtY -= getStartY();
      }
   }

   @Override
   protected boolean checkProcessing()
   {
      return true;
   }

   private float getOffsetX()
   {
      return ((1f / zoom) * (float) screenWidth) / 2f;
   }

   private float getOffsetY()
   {
      return ((1f / zoom) * (float) screenHeight) / 2f;
   }

   public float getStartX()
   {
      return lookAtX - getOffsetX();
   }

   public float getStartY()
   {
      return lookAtY - getOffsetY();
   }

   public float getEndX()
   {
      return lookAtX + getOffsetX();
   }

   public float getEndY()
   {
      return lookAtY + getOffsetY();
   }

   public float getWidth()
   {
      return getEndX() - getStartX();
   }

   public float getHeight()
   {
      return getEndY() - getStartY();
   }

   public float getZoom()
   {
      return zoom;
   }
   
   public Matrix4 getCombinedMatrix()
   {
      return mCam.combined;
   }

   private void ensurePlayerEntity()
   {
      if (player == null || !player.isActive())
         player = world.getTagManager().getEntity("PLAYER");
   }

   @Override
   public boolean keyDown(int keycode)
   {
      // TODO Auto-generated method stub
      return false;
   }

   @Override
   public boolean keyUp(int keycode)
   {
      // TODO Auto-generated method stub
      return false;
   }

   @Override
   public boolean keyTyped(char character)
   {
      // TODO Auto-generated method stub
      return false;
   }

   @Override
   public boolean touchDown(int x, int y, int pointer, int button)
   {
      // TODO Auto-generated method stub
      return false;
   }

   @Override
   public boolean touchUp(int x, int y, int pointer, int button)
   {
      // TODO Auto-generated method stub
      return false;
   }

   @Override
   public boolean touchDragged(int x, int y, int pointer)
   {
      // TODO Auto-generated method stub
      return false;
   }

   @Override
   public boolean touchMoved(int x, int y)
   {
      // TODO Auto-generated method stub
      return false;
   }

   @Override
   public boolean scrolled(int amount)
   {
      mCam.zoom += amount * 0.01f;
      
      zoom = mCam.zoom = Math.max(mCam.zoom, MIN_CAMERA_ZOOM);

      return true;
   }
}
