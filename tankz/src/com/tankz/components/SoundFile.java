package com.tankz.components;

import com.artemis.Component;
import com.gushikustudios.common.GameSound;

public class SoundFile extends Component {
	private GameSound soundFile;
	
	public SoundFile(GameSound audioFile) {
		this.soundFile = audioFile;
	}

	public GameSound getSoundFile() {
		return soundFile;
	}
	
}