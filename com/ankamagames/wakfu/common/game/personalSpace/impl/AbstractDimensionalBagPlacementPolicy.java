package com.ankamagames.wakfu.common.game.personalSpace.impl;

import org.apache.log4j.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.interactiveElement.*;

public abstract class AbstractDimensionalBagPlacementPolicy
{
    protected static final Logger m_logger;
    public static final int MINIMUM_SPACE_BETWEEN_ELEMENTS = 1;
    public static final int MINIMUM_SPACE_BETWEEN_ELEMENTS_ON_MARKET = 0;
    
    protected static boolean checkCompatibilityWithElement(final Point3 bagPosition, final MapInteractiveElement existingElement, final boolean onMarket) {
        final int minSpace = onMarket ? 0 : 1;
        final Point3 elementPos = existingElement.getPosition();
        return Math.abs(elementPos.getX() - bagPosition.getX()) > minSpace || Math.abs(elementPos.getY() - bagPosition.getY()) > minSpace;
    }
    
    static {
        m_logger = Logger.getLogger((Class)AbstractDimensionalBagPlacementPolicy.class);
    }
}
