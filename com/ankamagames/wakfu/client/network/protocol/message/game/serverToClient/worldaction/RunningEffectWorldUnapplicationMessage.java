package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.worldaction;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import org.apache.log4j.*;
import java.nio.*;

public class RunningEffectWorldUnapplicationMessage extends InputOnlyProxyMessage
{
    protected static final Logger m_logger;
    private long m_targetId;
    private long m_runningEffectUid;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer bb = ByteBuffer.wrap(rawDatas);
        this.m_targetId = bb.getLong();
        this.m_runningEffectUid = bb.getLong();
        return true;
    }
    
    @Override
    public int getId() {
        return 6324;
    }
    
    public long getTargetId() {
        return this.m_targetId;
    }
    
    public long getRunningEffectUid() {
        return this.m_runningEffectUid;
    }
    
    static {
        m_logger = Logger.getLogger((Class)RunningEffectWorldUnapplicationMessage.class);
    }
}
