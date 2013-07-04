package com.tankz.components;

import com.artemis.Component;
import com.badlogic.gdx.math.MathUtils;

public class Tower extends Component {
	private float rotationInRadians;
	private float recoil;
	
	public Tower() {
	}
	
	/**
	 * @return Rotation in radians
	 */
	public float getRotation() {
		return rotationInRadians;
	}
	
	/**
	 * @param rotation in radians
	 */
	public void setRotation(float rotation) {
		this.rotationInRadians = rotation;
	}

	public void setRecoil(float recoil) {
		this.recoil = recoil;
	}

	public float getRecoil() {
		return recoil;
	}
	
	/**
	 * Adds the given angle in radians to the current angle.  Constrains to 2*PI
	 * 
	 * @param angle in radians.
	 */
	public void addRotation(float angle) {
		rotationInRadians = (rotationInRadians + angle) % MathUtils.PI*2;
	}

}
