package com.ankamagames.wakfu.common.game.zone;

import org.apache.log4j.*;
import gnu.trove.*;

public class ZoneBuffManager
{
    protected static final Logger m_logger;
    private static final ZoneBuffManager m_instance;
    private final TIntObjectHashMap<ZoneBuff> m_zoneBuffs;
    
    public static ZoneBuffManager getInstance() {
        return ZoneBuffManager.m_instance;
    }
    
    private ZoneBuffManager() {
        super();
        this.m_zoneBuffs = new TIntObjectHashMap<ZoneBuff>();
    }
    
    public ZoneBuff getZoneBuff(final int id) {
        final ZoneBuff zoneBuff = this.m_zoneBuffs.get(id);
        if (zoneBuff != null) {
            return zoneBuff;
        }
        return null;
    }
    
    static {
        m_logger = Logger.getLogger((Class)ZoneBuffManager.class);
        m_instance = new ZoneBuffManager();
    }
}
