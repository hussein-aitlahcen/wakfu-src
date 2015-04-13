package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.worldaction;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import org.apache.log4j.*;
import java.nio.*;

public class RunningEffectWorldApplicationMessage extends InputOnlyProxyMessage
{
    protected static final Logger m_logger;
    private long m_targetId;
    private int m_runningEffectId;
    private byte[] m_serializedRunningEffect;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer bb = ByteBuffer.wrap(rawDatas);
        this.m_targetId = bb.getLong();
        this.m_runningEffectId = bb.getInt();
        bb.get(this.m_serializedRunningEffect = new byte[bb.getShort()]);
        return true;
    }
    
    @Override
    public int getId() {
        return 6322;
    }
    
    public long getTargetId() {
        return this.m_targetId;
    }
    
    public int getRunningEffectId() {
        return this.m_runningEffectId;
    }
    
    public byte[] getSerializedRunningEffect() {
        return this.m_serializedRunningEffect;
    }
    
    static {
        m_logger = Logger.getLogger((Class)RunningEffectWorldApplicationMessage.class);
    }
}
