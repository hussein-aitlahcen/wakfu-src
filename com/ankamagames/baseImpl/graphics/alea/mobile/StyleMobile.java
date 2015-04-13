package com.ankamagames.baseImpl.graphics.alea.mobile;

import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.baseImpl.graphics.alea.mobile.movementStyle.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.movement.*;
import com.ankamagames.baseImpl.graphics.alea.mobile.movementStyle.movementSelector.*;

public interface StyleMobile
{
    boolean setAnimation(String p0);
    
    String getAnimation();
    
    void setDirection(Direction8 p0);
    
    Direction8 getDirection();
    
    String getStaticAnimationKey();
    
    String getMoveEndAnimationKey();
    
    void setStaticAnimationKey(String p0);
    
    int getMaxWalkDistance();
    
    PathMovementStyle getMovementStyle();
    
    MovementSpeed getRunMovementSpeed();
    
    MovementSpeed getWalkMovementSpeed();
    
    void setIsInSpecialJump(boolean p0);
    
    boolean isInSpecialJump();
    
    void setMovementSelector(MovementSelector p0);
    
    MovementSelector getMovementSelector();
    
    void resetMovementSelector();
    
    boolean containsAnimation(String p0);
}
