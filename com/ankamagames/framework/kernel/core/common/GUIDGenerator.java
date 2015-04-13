package com.ankamagames.framework.kernel.core.common;

import com.ankamagames.framework.kernel.core.common.message.scheduler.clock.*;
import com.ankamagames.framework.kernel.core.common.message.*;

public class GUIDGenerator implements MessageHandler, LongUIDGenerator
{
    private static final long MINUTES_TO_MILLISEC = 60000L;
    private int m_nextIntPart;
    private int m_serverId;
    private long m_fixedPart;
    private static final GUIDGenerator m_instance;
    
    public static GUIDGenerator getInstance() {
        return GUIDGenerator.m_instance;
    }
    
    private GUIDGenerator() {
        super();
        this.m_serverId = -1;
    }
    
    public static void init(final int serverId) {
        if (serverId < 0 || serverId > 255) {
            throw new IllegalArgumentException("Le num\u00e9ro de serveur doit \u00eatre compris entre 0 et 255");
        }
        GUIDGenerator.m_instance.m_serverId = serverId;
        GUIDGenerator.m_instance.m_nextIntPart = 0;
        GUIDGenerator.m_instance.initFixedPart();
        MessageScheduler.getInstance().addClock(GUIDGenerator.m_instance, 60000L, 0);
    }
    
    public static long getGUID() {
        final long uid = GUIDGenerator.m_instance.m_fixedPart + (GUIDGenerator.m_instance.m_nextIntPart & 0xFFFFFFL);
        final GUIDGenerator instance = GUIDGenerator.m_instance;
        ++instance.m_nextIntPart;
        return uid;
    }
    
    @Override
    public boolean onMessage(final Message message) {
        if (message.getId() == Integer.MIN_VALUE) {
            this.initFixedPart();
            this.m_nextIntPart = 0;
            return false;
        }
        return true;
    }
    
    private void initFixedPart() {
        if (this.m_serverId < 0 || this.m_serverId > 255) {
            throw new IllegalArgumentException("Impossible d'initialiser le GUIDGenerator : Le num\u00e9ro de serveur doit \u00eatre fix\u00e9 par la m\u00e9thode init");
        }
        this.m_fixedPart = (this.m_serverId & 0xFFL) << 48;
        this.m_fixedPart |= (System.currentTimeMillis() / 60000L & 0xFFFFFFL) << 24;
    }
    
    @Override
    public long getId() {
        return 1L;
    }
    
    @Override
    public void setId(final long id) {
    }
    
    @Override
    public long getNextUID() {
        return getGUID();
    }
    
    static {
        m_instance = new GUIDGenerator();
    }
}
