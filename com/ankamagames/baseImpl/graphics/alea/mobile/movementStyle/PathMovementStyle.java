package com.ankamagames.baseImpl.graphics.alea.mobile.movementStyle;

import com.ankamagames.baseImpl.graphics.alea.mobile.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.baseImpl.graphics.alea.mobile.movementStyle.jump.*;

public interface PathMovementStyle
{
    public static final boolean DEBUG_LOCAL_PLAYER_PATH_ANIMATIONS = false;
    
    int getCellSpeed(StyleMobile p0);
    
    JumpTrajectory getJumpDown();
    
    JumpTrajectory getJumpUp();
    
    void onStandingOnLastCell(StyleMobile p0);
    
    void onStandingOnFirstCell(StyleMobile p0, Direction8 p1);
    
    int getDelayOnFirstCell();
    
    void onMovingOnAir(StyleMobile p0, int p1, Phase p2);
    
    void onMovingOnGround(StyleMobile p0, int p1);
    
    void onMovingSpecialJump(StyleMobile p0);
    
    void onDirectionChanged(StyleMobile p0, Direction8 p1);
    
    boolean createPathOnSetPosition(StyleMobile p0);
    
    boolean isAirImpulsionNeeded(StyleMobile p0, int p1);
    
    String getStyleName();
}
