package com.gushikustudios.common;

public interface TwoStageRender
{
   public static final int START = 0;
   public static final int RUNNING = 1;
   public static final int DONE = 2;
   
   public void update(float delta);
   public void present(float delta);
}
