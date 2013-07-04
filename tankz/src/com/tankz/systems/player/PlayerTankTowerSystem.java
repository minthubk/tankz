package com.tankz.systems.player;

import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.utils.ImmutableBag;
import com.artemis.utils.TrigLUT;
import com.artemis.utils.Utils;
import com.gushikustudios.common.Assets;
import com.tankz.EntityFactory;
import com.tankz.components.Ammo;
import com.tankz.components.Physics;
import com.tankz.components.Tower;
import com.tankz.components.Transform;
import com.tankz.components.TurnFactor;
import com.tankz.components.Velocity;

public class PlayerTankTowerSystem extends EntitySystem {
	private static final float MAX_VELOCITY = 0.16f;
	private static final float THRUST = 0.00012f;

	private static final float TURN_THRUST = 0.0002f;
	private static final float MAX_TURN_VELOCITY = 0.05f;

	private static float RECOIL_SPEED = 0.2f;
	private static float RECOIL_RECOVER_SPEED = 0.02f;
	private static int RECOIL_TARGET_OFFSET = 20;

	private boolean forward;
	private boolean reverse;
	private boolean turnRight;
	private boolean turnLeft;

	private boolean moving;
	private boolean turning;

	private boolean recoil;
	private boolean shoot;

	private ComponentMapper<Transform> transformMapper;
	private ComponentMapper<Velocity> velocityMapper;
	private ComponentMapper<TurnFactor> turnFactorMapper;
	private ComponentMapper<Tower> towerMapper;
	private ComponentMapper<Physics> physicsMapper;
	private ComponentMapper<Ammo> ammoMapper;
	private Entity player;

	public PlayerTankTowerSystem() {
		super();
	}

	@Override
	public void initialize() {
		transformMapper = new ComponentMapper<Transform>(Transform.class, world);
		velocityMapper = new ComponentMapper<Velocity>(Velocity.class, world);
		turnFactorMapper = new ComponentMapper<TurnFactor>(TurnFactor.class, world);
		towerMapper = new ComponentMapper<Tower>(Tower.class, world);
		physicsMapper = new ComponentMapper<Physics>(Physics.class, world);
		ammoMapper = new ComponentMapper<Ammo>(Ammo.class, world);

		ensurePlayerEntity();
	}

	@Override
	protected void processEntities(ImmutableBag<Entity> entities) {
		ensurePlayerEntity();

		if (player != null) {
			updatePlayer(player);
		}
	}
	
	@Override
	protected boolean checkProcessing() {
		return true;
	}

	private void ensurePlayerEntity() {
		if (player == null || !player.isActive())
			player = world.getTagManager().getEntity("PLAYER");
	}

	protected void updatePlayer(Entity e) {
		Velocity v = velocityMapper.get(e);
		TurnFactor tf = turnFactorMapper.get(e);
		Physics physics = physicsMapper.get(e);

		Tower tower = towerMapper.get(e);
		Ammo ammo = ammoMapper.get(e);

		updateTowerRotation(tower, physics);

		updateRecoil(tower);

		if (shoot && ammo.hasAmmo(10)) {
			shoot = false;
			recoil = true;

			// Create bullets for both barrels.
			{
				float x = Utils.getRotatedX(physics.getX() + 75, physics.getY() - 10, physics.getX(), physics.getY(), tower.getRotation());
				float y = Utils.getRotatedY(physics.getX() + 75, physics.getY() - 10, physics.getX(), physics.getY(), tower.getRotation());
				EntityFactory.createBullet(world, x, y, tower.getRotation(), e);
			}
			{
				float x = Utils.getRotatedX(physics.getX() + 75, physics.getY() + 10, physics.getX(), physics.getY(), tower.getRotation());
				float y = Utils.getRotatedY(physics.getX() + 75, physics.getY() + 10, physics.getX(), physics.getY(), tower.getRotation());
				EntityFactory.createBullet(world, x, y, tower.getRotation(), e);
			}

			// apply force to moving tank.
			float counterRotation = tower.getRotation()+180;
			float force = 2000f;
			physics.setForce(force*TrigLUT.cosDeg(counterRotation), force*TrigLUT.sinDeg(counterRotation));

			// add sound.
			EntityFactory.createSound(world, Assets.mSndShoot);

			ammo.reduceBy(10);
		}
	}

	/**
	 * Tower tracks body direction.
	 * 
	 * @param tower
	 * @param p
	 */
	private void updateTowerRotation(Tower tower, Physics p) {
		tower.setRotation(p.getBody().getAngle());
	}

	private void updateRecoil(Tower tower) {
		float recoilOffset = tower.getRecoil();
		if (recoil) {
			recoilOffset += world.getDelta() * RECOIL_SPEED;

			if (recoilOffset > RECOIL_TARGET_OFFSET) {
				recoilOffset = RECOIL_TARGET_OFFSET;
				recoil = false;
			}

			tower.setRecoil(recoilOffset);
		} else if (recoilOffset > 0) {
			recoilOffset -= (float) world.getDelta() * RECOIL_RECOVER_SPEED;
			if (recoilOffset < 0)
				recoilOffset = 0;
			tower.setRecoil(recoilOffset);
		}
	}
}
