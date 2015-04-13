package com.ankamagames.wakfu.common.game.world.havenWorld.validator.conflict;

import org.apache.log4j.*;
import com.ankamagames.wakfu.common.game.world.havenWorld.items.*;

public class MissingKama extends ConstructionError
{
    private static final Logger m_logger;
    private final long m_missingKama;
    
    public MissingKama(final ModificationItem item, final long missingKama) {
        super(item);
        this.m_missingKama = missingKama;
    }
    
    public long getMissingKama() {
        return this.m_missingKama;
    }
    
    @Override
    public final Type getType() {
        return Type.MissingKama;
    }
    
    static {
        m_logger = Logger.getLogger((Class)MissingKama.class);
    }
}
