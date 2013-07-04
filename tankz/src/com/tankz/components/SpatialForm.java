package com.tankz.components;

import com.artemis.Component;

/**
 * Spatial is part of the rendering system.  It's the visual representation of an entity.
 * 
 * @author tescott
 *
 */
public class SpatialForm extends Component {
	private String spatialFormFile;

	public SpatialForm(String spatialFormFile) {
		this.spatialFormFile = spatialFormFile;
	}

	public String getSpatialFormFile() {
		return spatialFormFile;
	}

}
