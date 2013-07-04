package com.gushikustudios.tankz.screen;

import java.util.Random;

import aurelienribon.tweenengine.TweenManager;

import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.SystemManager;
import com.artemis.World;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ClickListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.utils.Array;
import com.gushikustudios.common.Assets;
import com.gushikustudios.common.Prefs;
import com.gushikustudios.common.Settings;
import com.gushikustudios.common.TwoStageRender;
import com.gushikustudios.tankz.GameMain;
import com.gushikustudios.tankz.behaviors.Behavior;
import com.gushikustudios.tankz.behaviors.Pursue;
import com.tankz.EntityFactory;
import com.tankz.components.BrainAI;
import com.tankz.components.BrainPlayer;
import com.tankz.components.Maneuver;
import com.tankz.managers.Player;
import com.tankz.managers.PlayerManager;
import com.tankz.systems.ai.AITankSystem;
import com.tankz.systems.camera.CameraSystem;
import com.tankz.systems.misc.AmmoRegenerationSystem;
import com.tankz.systems.misc.BoundarySystem;
import com.tankz.systems.misc.ExpirationSystem;
import com.tankz.systems.misc.HealthSystem;
import com.tankz.systems.misc.SoundSystem;
import com.tankz.systems.physics.PhysicsSystem;
import com.tankz.systems.player.PlayerTankMovementSystem;
import com.tankz.systems.player.PlayerTankTowerSystem;
import com.tankz.systems.rendering.PhysicsRenderSystem;
import com.tankz.systems.rendering.RenderSystem;
import com.tankz.systems.rendering.TerrainRenderSystem;

public class TankzScreen implements Screen, TwoStageRender
{
   private SpriteBatch mBatch;
   private Stage mStage;

   private int mState;
   private int mNewState = START;

   private TweenManager mTweenManager;
   
   private InputMultiplexer mInputMux;

   private World mEntityWorld;

   private Random rand;

   private EntitySystem renderSystem;
   private EntitySystem physicsSystem;
   private EntitySystem expirationSystem;
   private EntitySystem playerTankTowerSystem;
   private EntitySystem hudRenderSystem;
   private EntitySystem soundSystem;
   private EntitySystem playerTankMovementSystem;
   private EntitySystem healthRenderSystem;
   private EntitySystem healthSystem;
   private EntitySystem boundarySystem;
   private EntitySystem cameraSystem;
   private EntitySystem aiSystem;
   private EntitySystem mPhysicsRenderSystem;

   private EntitySystem terrainRenderSystem;

   private EntitySystem ammoRegenerationSystem;

   // private EntitySystem clientNetworkSystem;

   private EntitySystem crosshairRenderSystem;
   
   public TankzScreen()
   {

      mBatch = GameMain.getSpriteBatch();
      mStage = new Stage(Settings.NATIVE_WIDTH, Settings.NATIVE_HEIGHT, true, mBatch);
      mInputMux = new InputMultiplexer();
      
      Gdx.input.setInputProcessor(mInputMux);
      Gdx.input.setCatchBackKey(true);

      GameMain.getUIHandler().showAds(true);

      mTweenManager = new TweenManager();
      
      rand = new Random(2); //FIXME: Make random.

      Button btn;

      if (Settings.mDebugOn)
      {
         btn = new TextButton("DEBUG", Assets.mSkin.getStyle("default-round", TextButtonStyle.class), "debug");
         btn.x = 0;
         btn.y = 300;
         btn.setClickListener(new ClickListener()
         {

            @Override
            public void click(Actor actor, float x, float y)
            {
               Prefs.writeOnDirty();
               // GameMain.getGame().setScreen(new DebugScreen());
            }
         });
         mStage.addActor(btn);
      }

      Settings.mSkipMainMenuTweens = true; // next iteration will skip tweening

      /*
       * Configure Entity System
       */
      // Instantiate new world for the Entity System
      mEntityWorld = new World();

      // TODO: Not sure how this is utilized...?
      mEntityWorld.setManager(new PlayerManager());

      // Add game systems to the world...
      SystemManager systemManager = mEntityWorld.getSystemManager();

      // The following systems have their .process() method executed during the update() step
      boundarySystem = systemManager.setSystem(new BoundarySystem(0, 0, 4096, 4096)); // EntityProcessingSystem
      physicsSystem = systemManager.setSystem(new PhysicsSystem()); // IntervalEntitySystem
      healthSystem = systemManager.setSystem(new HealthSystem()); // EntityProcessingSystem
      soundSystem = systemManager.setSystem(new SoundSystem()); // EntityProcessingSystem
      expirationSystem = systemManager.setSystem(new ExpirationSystem()); // DelayedEntityProcessingSystem
      playerTankTowerSystem = systemManager.setSystem(new PlayerTankTowerSystem()); // EntitySystem
      playerTankMovementSystem = systemManager.setSystem(new PlayerTankMovementSystem()); // EntitySystem
      cameraSystem = systemManager.setSystem(new CameraSystem()); // EntitySystem
      ammoRegenerationSystem = systemManager.setSystem(new AmmoRegenerationSystem()); // IntervalEntityProcessingSystem

      aiSystem = systemManager.setSystem(new AITankSystem());

      // clientNetworkSystem = systemManager.setSystem(new ClientNetworkSystem(10, ""));

      // The following systems have their .process() method executed during the render() step
      terrainRenderSystem = systemManager.setSystem(new TerrainRenderSystem()); // EntitySystem
      renderSystem = systemManager.setSystem(new RenderSystem()); // EntityProcessingSystem
      // hudRenderSystem = systemManager.setSystem(new HudRenderSystem(container)); // EntitySystem
      // healthRenderSystem = systemManager.setSystem(new HealthRenderSystem(container)); // EntityProcessingSystem
      // crosshairRenderSystem = systemManager.setSystem(new CrosshairRenderSystem(container)); // EntitySystem
      mPhysicsRenderSystem = systemManager.setSystem(new PhysicsRenderSystem());

      systemManager.initializeAll();

      // create the playerTank and the computer tanks..
      Entity playerTank = EntityFactory.createMammothTank(mEntityWorld, 300f / 20, 400f / 20);
      playerTank.addComponent(new BrainPlayer());
      playerTank.addComponent(new Maneuver());
      mEntityWorld.getManager(PlayerManager.class).setPlayer(playerTank,
            new Player(new Color(51f / 255, 204f / 255, 69f / 255, 1), "PLAYER"));
      playerTank.setTag("PLAYER");
      playerTank.refresh(); // need to do this after adding or removing components to the entity

      {
         Entity e = EntityFactory.createMammothTank(mEntityWorld, 600f / 20, 800f / 20);
         Array<Behavior> list = new Array<Behavior>();
//         list.add(new Separation(mEntityWorld));
         list.add(new Pursue(mEntityWorld));
         e.addComponent(new BrainAI(list));
         e.addComponent(new Maneuver());
         mEntityWorld.getManager(PlayerManager.class).setPlayer(e,new Player(new Color(Color.ORANGE), "COMPUTER 1"));
         e.setGroup("ENEMY");
         e.refresh();
      }
//      {
//         Entity e = EntityFactory.createMammothTank(mEntityWorld, 1000f / 20, 200f / 20);
//         Array<Behavior> list = new Array<Behavior>();
////         list.add(new Separation(mEntityWorld));
//         list.add(new Pursue(mEntityWorld));
//         e.addComponent(new BrainAI(list));
//         e.addComponent(new Maneuver());
//         mEntityWorld.getManager(PlayerManager.class).setPlayer(e,new Player(new Color(Color.BLUE), "COMPUTER 2"));
//         e.setGroup("ENEMY");
//         e.refresh();
//      }

      // create the six obstacles that are near the starting position
//      EntityFactory.createWall(mEntityWorld, 756f / 20, 540f / 20);
//      EntityFactory.createWall(mEntityWorld, (756f + (5f * 108f)) / 20, 540f/20);
//      EntityFactory.createWall(mEntityWorld, (756f + (10f * 108f)) / 20, 540f/20);
//
//      EntityFactory.createWall(mEntityWorld, 756f / 20, (540f + (10f * 108f)) / 20);
//      EntityFactory.createWall(mEntityWorld, (756f + (5f * 108f)) / 20, (540f + (10f * 108f)) / 20);
//      EntityFactory.createWall(mEntityWorld, (756f + (10f * 108f)) / 20, (540f + (10f * 108f)) / 20);

//      EntityFactory.createWall(mEntityWorld, (0 - 5f) / 20f, 2048f / 20, 5, 4096f / 20, false); // left hand wall
//      EntityFactory.createWall(mEntityWorld, 2048f / 20, (0 - 5f) / 20, 4096f / 20, 5, false); // top edge
//      EntityFactory.createWall(mEntityWorld, (4096f + 5f) / 20, 2048f / 20, 5, 4096f / 20, false); // right edge
//      EntityFactory.createWall(mEntityWorld, 2048f / 20, (4096f + 5f) / 20, 4096f / 20, 5, false); // bot edge

      // create clusters of crates...
      for (int i = 0; i < 50; i++)
      {
         float gx = rand.nextFloat()*4000f / 20 + 40f/20f;
         float gy = rand.nextFloat()*4000f / 20 + 40f/20f;
         int g = (int)(rand.nextInt(6));
         
         for (int a = 0; a < g; a++)
         {
            EntityFactory.createCrate(mEntityWorld, gx + rand.nextFloat()*50f / 20,
                  gy + rand.nextFloat() * 50f / 20, rand.nextFloat()*360);
         }
      }
      
      mInputMux.addProcessor((InputProcessor) playerTankMovementSystem);
      mInputMux.addProcessor((InputProcessor)cameraSystem);
   }

   @Override
   public void render(float delta)
   {
      update(delta);
      present(delta);
      mState = mNewState;
   }

   @Override
   public void resize(int width, int height)
   {
      // TODO Auto-generated method stub
   }

   @Override
   public void show()
   {
      // Gdx.app.log(Settings.APP_LOG_ID, "MainMenu: show");
   }

   @Override
   public void hide()
   {
      // Gdx.app.log(Settings.APP_LOG_ID,"MainMenu: hide");
   }

   @Override
   public void pause()
   {
      // TODO Auto-generated method stub

   }

   @Override
   public void resume()
   {
      // Gdx.app.log(Settings.APP_LOG_ID, "MainMenu: resume");
   }

   @Override
   public void dispose()
   {
      // Gdx.app.log(Settings.APP_LOG_ID,"MainMenu: dispose");
   }

   @Override
   public void update(float delta)
   {
      mTweenManager.update(delta);

      mEntityWorld.loopStart();

      mEntityWorld.setDelta(delta);

      soundSystem.process();
      healthSystem.process();
      physicsSystem.process();
      expirationSystem.process();
      playerTankTowerSystem.process();
      playerTankMovementSystem.process();
      // boundarySystem.process();
      cameraSystem.process();
      ammoRegenerationSystem.process();
      aiSystem.process();
      // clientNetworkSystem.process();
   }

   @Override
   public void present(float delta)
   {
      Gdx.gl.glClearColor(0, 0, 0, 1);
      Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
      
      mBatch.setProjectionMatrix(((CameraSystem)cameraSystem).getCombinedMatrix());
      mBatch.begin();
//      terrainRenderSystem.process();
      renderSystem.process();
      // healthRenderSystem.process();
      // crosshairRenderSystem.process();
      // hudRenderSystem.process();
      mBatch.end();
      mPhysicsRenderSystem.process(); // this utilizes its own spritebatch.
      //
      // case RUNNING:
      // mBatch.setProjectionMatrix(mCam.combined);
      // mBatch.begin();
      // mBatch.draw(Assets.mLogo, 0, 0);
      // mBatch.end();
      // mStage.draw();
      // break;
      //
      // case DONE:
      // break;
      // }
   }

   private class ImageAndTextEntry extends Button
   {
      public ImageAndTextEntry(TextureRegion region, String string, Color color)
      {
         super(Assets.mSkin.getStyle("default-round-white", ButtonStyle.class));
         Label label;
         this.touchable = false;
         this.color.set(color);
         this.row();
         this.add(new Image(region)).size(region.getRegionWidth(), region.getRegionHeight());
         this.add(label = new Label(string, Assets.mSkin.getStyle("default-white", LabelStyle.class))).fill().expandX();
         label.setWrap(true);
      }
   }
}
