package com.ankamagames.wakfu.client.alea.graphics.tacticalView.map;

public class Block
{
    public final int minZ;
    public final int maxZ;
    public final boolean walkable;
    public final boolean blockLos;
    
    Block(final int minZ, final int maxZ, final boolean walkable, final boolean blockLos) {
        super();
        this.minZ = minZ;
        this.maxZ = maxZ;
        this.walkable = walkable;
        this.blockLos = blockLos;
    }
}
