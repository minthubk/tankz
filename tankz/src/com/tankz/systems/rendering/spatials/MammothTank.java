package com.tankz.systems.rendering.spatials;

import com.artemis.Entity;
import com.artemis.World;
import com.artemis.utils.Utils;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.physics.box2d.Body;
import com.gushikustudios.common.Assets;
import com.gushikustudios.tankz.GameMain;
import com.tankz.components.Physics;
import com.tankz.components.Tower;
import com.tankz.components.TurnFactor;
import com.tankz.components.Velocity;
import com.tankz.managers.Player;
import com.tankz.managers.PlayerManager;
import com.tankz.systems.camera.CameraSystem;

public class MammothTank extends Spatial
{
   private static AtlasRegion shadow = null;
   private static AtlasRegion rearTracks = null;
   private static AtlasRegion trackTile;
   private static AtlasRegion base = null;
   private static AtlasRegion rightFront = null;
   private static AtlasRegion leftFront = null;
   private static AtlasRegion frontTrack = null;
   private static AtlasRegion barrels;
   private static AtlasRegion towerImage = null;
   
   private static Vector2 mTmpVec = new Vector2(); // only need one of these.  It's defined at the top of the render() method
   private static Vector3 [] mAABB;
   private static BoundingBox mBBox;

   static
   {
      shadow = Assets.mShadow;
      trackTile = Assets.mTracks;
      rearTracks = Assets.mRearTracks;
      base = Assets.mTankBody;
      frontTrack = Assets.mFrontTrack;
      rightFront = Assets.mRightFrontTrackCover;
      leftFront = Assets.mLeftFrontTrackCover;
      barrels = Assets.mTankBarrels;
      towerImage = Assets.mTankTower;
   }

   private Physics physics;
   private Velocity velocity;
   private TurnFactor turnFactor;

   private float trackTileWidth;
   private float trackTileOffsetX;

   private boolean prevVelForward;
   private float previousX;
   private float previousY;
   private float trackOffset;
   private Tower tower;
   private Color color;
   
   private SpriteBatch mBatch;
   
   /**
    * Units are meters per pixel.  Reference point is the size of the tank at 6.25m wide x 5.2m tall
    */
   private float mXMPP;
   /**
    * Units are meters per pixel.  Reference point is the size of the tank at 6.25m wide x 5.2m tall
    */
   private float mYMPP;
   
   private static final float TRACK_SCROLL_FACTOR = 5; // used to scale how fast the tracks turn when velocity is non-zero

   public MammothTank(World world, Entity owner)
   {
      super(world, owner);
   }

   @Override
   public void initalize()
   {
      mBatch = GameMain.getSpriteBatch();
      physics = owner.getComponent(Physics.class);
      velocity = owner.getComponent(Velocity.class);
      turnFactor = owner.getComponent(TurnFactor.class);
      tower = owner.getComponent(Tower.class);
      Player player = world.getManager(PlayerManager.class).getPlayer(owner);
      color = player.getColor();

      trackTileWidth = trackTile.getRegionWidth();
      trackTileOffsetX = trackTile.getRegionX();

      previousX = 0f;
      previousY = 0f;
      
      mXMPP = 6.25f / Assets.mTankBody.getRegionWidth();
      mYMPP = 5.2f / Assets.mTankBody.getRegionHeight();
      
      mAABB = new Vector3[2];
      mAABB[0] = new Vector3(-6.25f/2,-6.25f,0);
      mAABB[1] = new Vector3(6.25f/2,6.25f/2,0);
      
      mBBox = new BoundingBox();
      
   }

   @Override
   public void render()
   {
      mBatch.setColor(color);
      Body b = physics.getBody();
      mTmpVec.set(b.getPosition());
      
      OrthographicCamera camPos = CameraSystem.getCam();
      
      // check to see if we should render this object...
      if (!camPos.frustum.boundsInFrustum(mBBox.set(mAABB[0].tmp().add(mTmpVec.x,mTmpVec.y,0),mAABB[1].tmp2().add(mTmpVec.x,mTmpVec.y,0))))
      {
         return;
      }
      
      // maintain previous velocity direction to correct when velocity hits 0, but momentum keeps things going...
      if (velocity.getVelocity() < 0)
      {
         prevVelForward = true;
         trackOffset += Utils.euclideanDistance(mTmpVec.x, mTmpVec.y, previousX, previousY);
      }
      else if (velocity.getVelocity() > 0)
      {
         prevVelForward = false;
         trackOffset -= Utils.euclideanDistance(mTmpVec.x, mTmpVec.y, previousX, previousY);
      }
      else
      {
         // else, velocity is 0 and momentum is carrying the vehicle along...
         if (prevVelForward)
         {
            trackOffset += Utils.euclideanDistance(mTmpVec.x, mTmpVec.y, previousX, previousY);
         }
         else
         {
            trackOffset -= Utils.euclideanDistance(mTmpVec.x, mTmpVec.y, previousX, previousY);
         }
      }
      
      // clamp the track offset
      if (trackOffset > trackTileWidth/(2* TRACK_SCROLL_FACTOR))
      {
         trackOffset = 0;
      }
      else if (trackOffset < 0)
      {
         trackOffset = trackTileWidth/(2 * TRACK_SCROLL_FACTOR);
      }

      previousX = mTmpVec.x;
      previousY = mTmpVec.y;

      float turnFactorAngle = turnFactor.getFactor() * 255f;
      
      float tankAngle = physics.getRotation();
      
      // Draw shadow underneath the tank.
//      mBatch.draw(shadow, physics.getX(), physics.getY(), 0, 0, shadow.getRegionWidth(), shadow.getRegionWidth(), 1, 1, physics.getRotation());

      // Draw the background under the rear tracks.
      mBatch.draw(rearTracks,
            mTmpVec.x - rearTracks.getRegionWidth() * mXMPP + 0.2f, mTmpVec.y - rearTracks.getRegionHeight() * mYMPP / 2, 
            rearTracks.getRegionWidth() * mXMPP - 0.2f, rearTracks.getRegionHeight() * mYMPP / 2, 
            rearTracks.getRegionWidth() * mXMPP, rearTracks.getRegionHeight() * mYMPP, 
            1, 1, 
            tankAngle);

      // Draw rear track tiles.
      trackTile.setRegionX((int)(trackTileOffsetX + trackOffset * TRACK_SCROLL_FACTOR));
      trackTile.setRegionWidth(70);
      mBatch.draw(trackTile, 
            mTmpVec.x - trackTile.getRegionWidth() * mXMPP + 0.3f, mTmpVec.y - trackTile.getRegionHeight() * mYMPP + 2.13f, 
            trackTile.getRegionWidth() * mXMPP - 0.3f, trackTile.getRegionHeight() * mYMPP - 2.13f, 
            trackTile.getRegionWidth() * mXMPP, trackTile.getRegionHeight() * mYMPP, 
            1, 1, 
            tankAngle);
      mBatch.draw(trackTile, 
            mTmpVec.x - trackTile.getRegionWidth() * mXMPP + 0.3f, mTmpVec.y - trackTile.getRegionHeight() * mYMPP - 1.61f, 
            trackTile.getRegionWidth() * mXMPP - 0.3f, trackTile.getRegionHeight() * mYMPP + 1.61f, 
            trackTile.getRegionWidth() * mXMPP, trackTile.getRegionHeight() * mYMPP, 
            1, 1, 
            tankAngle);

      // Draw base.
      mBatch.draw(base, 
            mTmpVec.x - 6.25f/2, mTmpVec.y - 5.2f/2, 
            6.25f/2, 5.2f/2, 
            6.25f, 5.2f, 
            1, 1, 
            physics.getRotation());
//
//         // Draw left front tracks.
//         g.rotate(physics.getX() + 43, physics.getY() - 40, turnFactorAngle);
//         {
//            frontTrack.draw(physics.getX() - frontTrack.getCenterOfRotationX() + 41,
//                  physics.getY() - frontTrack.getCenterOfRotationY() - 39, color);
//            g.fillRect(physics.getX() + 22, physics.getY() - 44, 38, 11, trackTile, -trackOffset % trackTileWidth, 0);
//            g.fillRect(physics.getX() + 22, physics.getY() - 44, 38, 11, trackTile, (-trackOffset % trackTileWidth) +
//                  trackTileWidth, 0);
//            leftFront.draw(physics.getX() - leftFront.getCenterOfRotationX() + 41,
//                  physics.getY() - leftFront.getCenterOfRotationY() - 39, color);
//         }
//         g.rotate(physics.getX() + 43, physics.getY() - 40, -turnFactorAngle);
//
//         // Draw right front tracks.
//         g.rotate(physics.getX() + 43, physics.getY() + 40, turnFactorAngle);
//         {
//            frontTrack.draw(physics.getX() - frontTrack.getCenterOfRotationX() + 41,
//                  physics.getY() - frontTrack.getCenterOfRotationY() + 39, color);
//            g.fillRect(physics.getX() + 22, physics.getY() + 34, 38, 11, trackTile, -trackOffset % trackTileWidth, 0);
//            g.fillRect(physics.getX() + 22, physics.getY() + 34, 38, 11, trackTile, (-trackOffset % trackTileWidth) +
//                  trackTileWidth, 0);
//            rightFront.draw(physics.getX() - rightFront.getCenterOfRotationX() + 41,
//                  physics.getY() - rightFront.getCenterOfRotationY() + 39, color);
//         }
//         g.rotate(physics.getX() + 43, physics.getY() + 40, -turnFactorAngle);
//
//      }
//      g.rotate(physics.getX(), physics.getY(), -physics.getRotation());
//
//      // Draw turret.
//      g.rotate(physics.getX(), physics.getY(), tower.getRotation());
//      {
//         barrels.draw(physics.getX() - tower.getRecoil() - barrels.getCenterOfRotationX(),
//               physics.getY() - barrels.getCenterOfRotationY(), color);
//         towerImage.draw(physics.getX() - towerImage.getCenterOfRotationX(),
//               physics.getY() - towerImage.getCenterOfRotationY(), color);
//      }
//      g.rotate(physics.getX(), physics.getY(), -tower.getRotation());
   }

}
