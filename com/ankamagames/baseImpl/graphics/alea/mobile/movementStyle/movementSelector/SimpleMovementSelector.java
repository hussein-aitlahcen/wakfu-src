package com.ankamagames.baseImpl.graphics.alea.mobile.movementStyle.movementSelector;

import com.ankamagames.baseImpl.graphics.alea.mobile.*;
import com.ankamagames.baseImpl.graphics.alea.mobile.movementStyle.*;

public class SimpleMovementSelector implements MovementSelector
{
    private static final SimpleMovementSelector m_instance;
    
    public static SimpleMovementSelector getInstance() {
        return SimpleMovementSelector.m_instance;
    }
    
    @Override
    public PathMovementStyle selectMovementStyle(final StyleMobile mobile, final int pathLength) {
        return (pathLength < mobile.getMaxWalkDistance()) ? WalkMovementStyle.getInstance() : RunMovementStyle.getInstance();
    }
    
    @Override
    public void onMovementEnded(final StyleMobile mobile) {
    }
    
    @Override
    public void resetMovementSelector(final StyleMobile mobile) {
    }
    
    static {
        m_instance = new SimpleMovementSelector();
    }
}
