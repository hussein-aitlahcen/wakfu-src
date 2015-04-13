package com.ankamagames.baseImpl.graphics.alea.mobile.movementStyle.movementSelector;

import org.apache.log4j.*;
import com.ankamagames.baseImpl.graphics.alea.mobile.movementStyle.*;
import com.ankamagames.baseImpl.graphics.alea.mobile.*;

public class CustomMovementSelector implements MovementSelector
{
    private static final Logger m_logger;
    protected final MovementSelector m_lastMovementSelector;
    private final PathMovementStyle m_walkStyle;
    private final PathMovementStyle m_runStyle;
    
    public CustomMovementSelector(final MovementSelector lastMovementSelector, final PathMovementStyle walk, final PathMovementStyle run) {
        super();
        this.m_lastMovementSelector = lastMovementSelector;
        this.m_walkStyle = walk;
        this.m_runStyle = run;
    }
    
    @Override
    public PathMovementStyle selectMovementStyle(final StyleMobile mobile, final int pathLength) {
        return (pathLength < mobile.getMaxWalkDistance()) ? this.m_walkStyle : this.m_runStyle;
    }
    
    @Override
    public void onMovementEnded(final StyleMobile mobile) {
    }
    
    @Override
    public void resetMovementSelector(final StyleMobile mobile) {
        mobile.setMovementSelector(this.m_lastMovementSelector);
    }
    
    public static MovementSelector create(final boolean justOnce, final PathMobile mobile, PathMovementStyle walkStyle, PathMovementStyle runStyle) {
        if (walkStyle == null && runStyle == null) {
            return SimpleMovementSelector.getInstance();
        }
        if (walkStyle == null) {
            CustomMovementSelector.m_logger.warn((Object)"style inconnu pour la marche");
            walkStyle = runStyle;
        }
        if (runStyle == null) {
            CustomMovementSelector.m_logger.warn((Object)"style inconnu pour la course");
            runStyle = walkStyle;
        }
        if (justOnce) {
            return new MovementSelectorUniqueUse(mobile.getMovementSelector(), walkStyle, runStyle);
        }
        return new CustomMovementSelector(mobile.getMovementSelector(), walkStyle, runStyle);
    }
    
    static {
        m_logger = Logger.getLogger((Class)CustomMovementSelector.class);
    }
}
