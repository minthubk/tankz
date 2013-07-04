package com.tankz.systems.misc;

import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntityProcessingSystem;
import com.tankz.components.SoundFile;

public class SoundSystem extends EntityProcessingSystem
{
   private ComponentMapper<SoundFile> soundMapper;

   public SoundSystem()
   {
      super(SoundFile.class);
   }

   @Override
   public void initialize()
   {
      soundMapper = new ComponentMapper<SoundFile>(SoundFile.class, world);
   }

   @Override
   protected void process(Entity e)
   {
      world.deleteEntity(e);
   }

   @Override
   protected void added(Entity e)
   {
      SoundFile soundFile = soundMapper.get(e);

      soundFile.getSoundFile().play(1, 0.3f);
   }

}
