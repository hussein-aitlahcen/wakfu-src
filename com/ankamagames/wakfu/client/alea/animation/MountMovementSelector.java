package com.ankamagames.wakfu.client.alea.animation;

import com.ankamagames.baseImpl.graphics.alea.mobile.movementStyle.movementSelector.*;
import com.ankamagames.baseImpl.graphics.alea.mobile.*;
import com.ankamagames.baseImpl.graphics.alea.mobile.movementStyle.*;

public class MountMovementSelector implements MovementSelector
{
    private final MountMovementStyle m_mountMovementStyle;
    
    public MountMovementSelector(final MountMovementStyle mountMovementStyle) {
        super();
        this.m_mountMovementStyle = mountMovementStyle;
    }
    
    @Override
    public PathMovementStyle selectMovementStyle(final StyleMobile mobile, final int pathLength) {
        return this.m_mountMovementStyle;
    }
    
    @Override
    public void onMovementEnded(final StyleMobile mobile) {
    }
    
    @Override
    public void resetMovementSelector(final StyleMobile mobile) {
    }
}
