package com.tankz.systems.physics;

import com.badlogic.gdx.graphics.Color;

/**
 * Helper class for setting consistent material properties for JBox2D physics objects. The values
 * are inspired by real values, but have been adjusted to play better within my game. If none of the
 * predefined materials work, you may create your own:
 *
 * <pre>Material myMaterial = new Material(1.0f, 0.3f, 0.1f);</pre>
 *
 * <br>Note: COR is half what is should be so that everything feels correct. By default, JBox2D will
 * take the highest of the 2 which can make everything really bouncy.
 *
 * <br><br>Note: Density (in kg/m3) was divided by 1000 to group them around the unofficial default
 * density of 1.0 for JBox2d. Here are the real densities for some common materials:<ul>
 * <li>Iron = 7874 kg/m3</li>
 * <li>Mild steel = 7850 kg/m3</li>
 * <li>Granite = 2750 kg/m3</li>
 * <li>Concrete = 2400 kg/m3</li>
 * <li>Dry Sand = 1780 kg/m3</li>
 * <li>Pine = 530 kg/m3</li>
 * <li>Average Glass = 2500 kg/m3</li>
 * <li>Manufactured Rubber = 1500 kg/m3</li>
 * <li>Ice = 920 kg/m3</li>
 * <li>Pumice = 250 kg/m3</li>
 * <li>Expanded version for Styrofoam density = 100 kg/m3</li>
 * <li>Cotton = 30 kg/m3 (actually 5-30)</li>
 * <li>Commercial low-density Sponge = 18 kg/m3</li>
 * <li>Air = 1.204 kg/m3 (@ 20 C at sea level)</li>
 * <li>Helium = 0.1786 kg/m3.</li></ul>
 *
 */
public class Material
{

      //                                                    Density Friction COR    Grab   Crush  Explode
      public static final Material DEFAULT     = new Material(1.00f,  0.30f, 0.1f,  false, true,  true,  Color.BLUE);
      public static final Material METAL       = new Material(7.85f,  0.20f, 0.2f,  false, false, false, Color.BLUE); // Heavy, inert.
      public static final Material STONE       = new Material(2.40f,  0.50f, 0.1f,  false, false, false, Color.BLUE); // Heavy, inert.
      public static final Material WOOD        = new Material(0.53f,  0.40f, 0.15f, false, true,  false, new Color(150, 98, 0,1)); // Medium weight, mostly inert.
      public static final Material GLASS       = new Material(2.50f,  0.10f, 0.2f,  false, true,  true,  new Color(0, 0, 220, 128)); // Heavy, transparent.
      public static final Material RUBBER      = new Material(1.50f,  0.80f, 0.4f,  false, false, false, new Color(20, 20, 20,1)); // Medium weight, inert, bouncy.
      public static final Material ICE         = new Material(0.92f,  0.01f, 0.1f,  false, true,  true,  new Color(0, 146, 220, 200)); // Medium weight, slippery surface.
      public static final Material PUMICE      = new Material(0.25f,  0.60f, 0.0f,  false, true,  true,  Color.WHITE); // Light, fragile.
      public static final Material POLYSTYRENE = new Material(0.10f,  0.60f, 0.05f, false, true,  true,  Color.WHITE); // Light, fragile.
      public static final Material FABRIC      = new Material(0.03f,  0.60f, 0.1f,  true,  true,  true,  Color.BLUE); // Medium weight, grabbable.
      public static final Material SPONGE      = new Material(0.018f, 0.90f, 0.05f, true,  true,  true,  Color.BLUE); // Light, fragile, grabbable.
      public static final Material AIR         = new Material(0.001f, 0.90f, 0.0f,  true,  true,  true,  new Color(142, 171, 255, 128)); // No gravity?
      public static final Material HELIUM      = new Material(0.0001f, 0.9f, 0.0f,  true,  true,  true,  new Color(142, 171, 255, 128)); // Negative gravity?
      public static final Material PLAYER      = new Material(1.00f,  0f,    0.0f,  false, true,  true,  Color.BLUE);

      /** Measure of mass in kg/m^3. Used to calculate the mass of a body. */
      private final float density;
      /** Measure of how easily a shape slides across a surface. Typically between 0 and 1. 0 means no friction. */
      private final float friction;
      /** Coefficient of Restitution (COR) or how much velocity a shape retains when colliding with another
       * (i.e. bounciness). COR is a ratio represented by a value between 0 and 1. 1 being more bouncy than 0. */
      private final float restitution;
      /** Game specific. Whether player can grab and hold onto the shape. */
      private final boolean isGrabbable;
      /** Game specific. Whether a shape is crushed when a strong force is applied to it. */
      private final boolean isCrushable;
      /** Game specific. Whether the material is affected by explosions (deforms the shape). */
      private final boolean isExplodable;
      /** Rendering engine specific. Color used to represent the shape when displaying.  */
      private final Color color;

      public Material(float density, float friction, float restitution) {
         this(density, friction, restitution, false, true, false, Color.BLUE);
      }

      public Material(float density, float friction, float restitution, boolean isGrabbable,
            boolean isCrushable, boolean isExplodable, Color color) {
         this.density = density;
         this.friction = friction;
         this.restitution = restitution;
         this.isGrabbable = isGrabbable;
         this.isCrushable = isCrushable;
         this.isExplodable = isExplodable;
         this.color = color;
      }

      public float density() {
         return density;
      }
      public float friction() {
         return friction;
      }
      public float restitution() {
         return restitution;
      }
      public boolean isGrabbable() {
         return isGrabbable;
      }
      public boolean isCrushable() {
         return isCrushable;
      }
      public boolean isExplodable() {
         return isExplodable;
      }
      public Color getColor() {
         return color;
      }
}
