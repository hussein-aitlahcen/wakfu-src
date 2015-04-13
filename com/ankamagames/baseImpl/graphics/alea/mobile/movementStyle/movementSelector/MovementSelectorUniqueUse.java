package com.ankamagames.baseImpl.graphics.alea.mobile.movementStyle.movementSelector;

import org.apache.log4j.*;
import com.ankamagames.baseImpl.graphics.alea.mobile.movementStyle.*;
import com.ankamagames.baseImpl.graphics.alea.mobile.*;

public class MovementSelectorUniqueUse extends CustomMovementSelector
{
    private static final Logger m_logger;
    
    public MovementSelectorUniqueUse(final MovementSelector lastMovementSelector, final PathMovementStyle walk, final PathMovementStyle run) {
        super(lastMovementSelector, walk, run);
    }
    
    @Override
    public void onMovementEnded(final StyleMobile mobile) {
        this.resetMovementSelector(mobile);
    }
    
    static {
        m_logger = Logger.getLogger((Class)MovementSelectorUniqueUse.class);
    }
}
