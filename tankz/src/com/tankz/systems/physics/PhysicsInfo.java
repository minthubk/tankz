package com.tankz.systems.physics;

public class PhysicsInfo
{
   /*
    * Box2d Collision category filtering:
    * 
    * Quick synopsis of how category bits and and mask bits work for collision detection on fixtures:
    * - A category bit defines a "IsA" type relationship
    * - A mask bit defines a "CollidesWithA" type relationship
    *
    * Let's say you have four bodies: player, player bullets, enemy, and enemy bullets.  You want the following
    * bullet behaviors:
    * 
    * - collisions between player's bullets and enemy bullets
    * - collisions between player's bullets and enemy
    * - no collisions between player's bullets and player
    * 
    * Do the following:
    * 
    * 1) Set the Category bits to uniquely identify the various categories
    * 2) Set the related mask bit position to report a collision
    * 3) Clear the related mask bit position to suppress a collision report
    * 
    * Example: Setting up player's bullets collision masking.
    * 
    * PlayerBullet.categoryBits = PLAYER_BULLET_CATEGORY;
    * PlayerBullet.maskBits = (ALL_CATEGORIES & (~(PLAYER_CATEGORY|PLAYER_BULLET_CATEGORY));
    * 
    * This will cause player bullet collisions to activate for everything, except when they collide with the
    * player or other player bullets.
    * 
    */
   public static final short PLAYER_CATEGORY =  (1 << 0);
   public static final short ENEMY_CATEGORY =   (1 << 1);
   public static final short BULLET_CATEGORY =  (1 << 2);
   public static final short ALL_CATEGORIES = (short)0xFFFF;
   
   /*
    * Box2d Collision group filtering:
    * 
    * Fixtures that belong to the same positive group index will always collide.  Fixtures that belong
    * to the same negative group index will never collide.  Note that group filtering has higher precedence than
    * category filtering.  If the group indices do not match, that is when category filtering kicks in.
    * 
    * Use a body's negated group index to prevent collision detection.
    *  
    */
   public static final short PLAYER_GROUP = 1;
   public static final short ENEMY_GROUP = 2; 
}
