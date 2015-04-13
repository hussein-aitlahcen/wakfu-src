package com.ankamagames.framework.graphics.engine.Anm2.actions;

import com.ankamagames.framework.sound.*;

public interface AnimatedObject
{
    SoundValidator getSoundValidator();
    
    boolean canPlaySound();
    
    boolean setAnimation(String p0);
    
    String getStaticAnimationKey();
    
    String getPreviousAnimation();
    
    int getCurrentFightId();
    
    void setRenderRadius(float p0);
}
