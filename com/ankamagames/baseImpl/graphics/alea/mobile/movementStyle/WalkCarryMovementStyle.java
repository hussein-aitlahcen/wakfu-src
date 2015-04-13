package com.ankamagames.baseImpl.graphics.alea.mobile.movementStyle;

import com.ankamagames.baseImpl.graphics.alea.mobile.*;

public class WalkCarryMovementStyle extends WalkMovementStyle
{
    private static final WalkCarryMovementStyle m_instance;
    
    public static WalkCarryMovementStyle getInstance() {
        return WalkCarryMovementStyle.m_instance;
    }
    
    @Override
    public void onMovingOnGround(final StyleMobile actor, final int remainPathLength) {
        actor.setAnimation("AnimMarche");
    }
    
    static {
        m_instance = new WalkCarryMovementStyle();
    }
}
