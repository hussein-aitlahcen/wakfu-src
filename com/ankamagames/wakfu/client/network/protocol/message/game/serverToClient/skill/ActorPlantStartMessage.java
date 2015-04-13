package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.skill;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import org.apache.log4j.*;
import java.nio.*;

public class ActorPlantStartMessage extends InputOnlyProxyMessage
{
    protected static final Logger m_logger;
    protected static final boolean DEBUG_MODE = false;
    private byte m_skillId;
    private long m_duration;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer buffer = ByteBuffer.wrap(rawDatas);
        this.m_skillId = buffer.get();
        this.m_duration = buffer.getInt();
        return true;
    }
    
    @Override
    public int getId() {
        return 4141;
    }
    
    public byte getSkillId() {
        return this.m_skillId;
    }
    
    public long getDuration() {
        return this.m_duration;
    }
    
    static {
        m_logger = Logger.getLogger((Class)ActorPlantStartMessage.class);
    }
}
