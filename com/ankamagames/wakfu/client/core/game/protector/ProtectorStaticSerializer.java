package com.ankamagames.wakfu.client.core.game.protector;

import com.ankamagames.wakfu.common.game.protector.*;

public class ProtectorStaticSerializer extends ProtectorSerializer
{
    public static final ProtectorStaticSerializer INSTANCE;
    
    protected ProtectorStaticSerializer() {
        super();
        this.setProtectorFactory(new ProtectorStaticFactory());
    }
    
    static {
        INSTANCE = new ProtectorStaticSerializer();
    }
}
