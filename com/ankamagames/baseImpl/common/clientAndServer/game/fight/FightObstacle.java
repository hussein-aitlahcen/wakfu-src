package com.ankamagames.baseImpl.common.clientAndServer.game.fight;

import com.ankamagames.framework.ai.*;

public interface FightObstacle extends PhysicalRadiusOwner
{
    void setObstacleId(byte p0);
    
    byte getObstacleId();
    
    boolean canBlockMovementOrSight();
    
    boolean isBlockingMovement();
    
    boolean isBlockingSight();
}
