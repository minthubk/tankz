package com.gushikustudios.common;

/**
 * This interface is implemented by the UI thread.  It allows libgdx to post requests / messages to the UI
 * thread.
 * 
 * @author tescott
 *
 */
public interface ActivityRequestHandler
{
   public void showAds(boolean show);
   public void reportError();
   public void launchIfEnabled();
   public void launchDashboard();
   public void postScoreToLeaderboard(int score);
}
