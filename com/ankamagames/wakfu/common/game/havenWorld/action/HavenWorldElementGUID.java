package com.ankamagames.wakfu.common.game.havenWorld.action;

public class HavenWorldElementGUID
{
    private static long m_guid;
    
    public static long getNextGUID() {
        return --HavenWorldElementGUID.m_guid;
    }
    
    public static long noUid() {
        return 0L;
    }
    
    static {
        HavenWorldElementGUID.m_guid = 0L;
    }
}
