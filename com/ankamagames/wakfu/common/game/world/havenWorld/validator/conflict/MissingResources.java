package com.ankamagames.wakfu.common.game.world.havenWorld.validator.conflict;

import org.apache.log4j.*;
import com.ankamagames.wakfu.common.game.world.havenWorld.items.*;

public class MissingResources extends ConstructionError
{
    private static final Logger m_logger;
    private final long m_missingResources;
    
    public MissingResources(final ModificationItem item, final long missingResources) {
        super(item);
        this.m_missingResources = missingResources;
    }
    
    public long getMissingResources() {
        return this.m_missingResources;
    }
    
    @Override
    public final Type getType() {
        return Type.MissingResources;
    }
    
    static {
        m_logger = Logger.getLogger((Class)MissingResources.class);
    }
}
