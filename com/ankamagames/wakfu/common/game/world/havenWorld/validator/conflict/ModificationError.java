package com.ankamagames.wakfu.common.game.world.havenWorld.validator.conflict;

import org.apache.log4j.*;
import com.ankamagames.wakfu.common.game.world.havenWorld.items.*;

public class ModificationError extends ConstructionError
{
    private static final Logger m_logger;
    
    public ModificationError(final ModificationItem item) {
        super(item);
    }
    
    @Override
    public Type getType() {
        return Type.Conflict;
    }
    
    static {
        m_logger = Logger.getLogger((Class)ModificationError.class);
    }
}
