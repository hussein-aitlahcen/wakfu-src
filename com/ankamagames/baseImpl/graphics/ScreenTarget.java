package com.ankamagames.baseImpl.graphics;

import com.ankamagames.framework.ai.targetfinder.*;

public interface ScreenTarget extends WorldPositionable
{
    public static final int INVALID_POSITION = Integer.MIN_VALUE;
    
    void setScreenX(int p0);
    
    void setScreenY(int p0);
    
    void setScreenTargetHeight(int p0);
    
    int getScreenX();
    
    int getScreenY();
    
    int getScreenTargetHeight();
    
    void addWatcher(ScreenTargetWatcher p0);
    
    void removeWatcher(ScreenTargetWatcher p0);
    
    boolean isPositionComputed();
}
