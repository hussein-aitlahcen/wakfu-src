package com.ankamagames.wakfu.common.game.world.havenWorld.validator.conflict;

import org.apache.log4j.*;
import com.ankamagames.wakfu.common.game.world.havenWorld.items.*;

public class MissingWorker extends ConstructionError
{
    private static final Logger m_logger;
    private final int m_missingWorker;
    
    public MissingWorker(final ModificationItem item, final int missingWorker) {
        super(item);
        this.m_missingWorker = missingWorker;
    }
    
    public int getMissingWorker() {
        return this.m_missingWorker;
    }
    
    @Override
    public final Type getType() {
        return Type.MissingWorker;
    }
    
    static {
        m_logger = Logger.getLogger((Class)MissingWorker.class);
    }
}
