package com.ankamagames.wakfu.common.game.world.havenWorld.validator.conflict;

import org.apache.log4j.*;
import com.ankamagames.wakfu.common.game.world.havenWorld.items.*;

public class BuildMaxQuantityReached extends ConstructionError
{
    private static final Logger m_logger;
    
    public BuildMaxQuantityReached(final ModificationItem item) {
        super(item);
    }
    
    @Override
    public final Type getType() {
        return Type.BuildingMaxQuantityReached;
    }
    
    static {
        m_logger = Logger.getLogger((Class)BuildMaxQuantityReached.class);
    }
}
