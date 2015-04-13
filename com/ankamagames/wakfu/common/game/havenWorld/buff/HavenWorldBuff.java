package com.ankamagames.wakfu.common.game.havenWorld.buff;

public interface HavenWorldBuff
{
    void apply(short p0, long p1);
    
    void unapply(short p0, long p1);
    
    boolean onlyOnce();
}
