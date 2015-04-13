package com.ankamagames.baseImpl.common.clientAndServer.game.effectArea;

import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;

public class BasicEffectAreaParameters
{
    private final long id;
    private final int x;
    private final int y;
    private final short z;
    private final EffectContext context;
    private final EffectUser owner;
    
    public BasicEffectAreaParameters(final long id, final int x, final int y, final short z, final EffectContext context, final EffectUser owner) {
        super();
        this.id = id;
        this.x = x;
        this.y = y;
        this.z = z;
        this.context = context;
        this.owner = owner;
    }
    
    public long getId() {
        return this.id;
    }
    
    public int getX() {
        return this.x;
    }
    
    public int getY() {
        return this.y;
    }
    
    public short getZ() {
        return this.z;
    }
    
    public EffectContext getContext() {
        return this.context;
    }
    
    public EffectUser getOwner() {
        return this.owner;
    }
}
