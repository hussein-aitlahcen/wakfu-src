package com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import org.apache.log4j.*;
import com.ankamagames.wakfu.common.game.world.action.*;
import java.nio.*;

public class ActorResurrectMonsterRequestMessage extends OutputOnlyProxyMessage
{
    protected static final Logger m_logger;
    private long m_corpseId;
    private MonsterResurrectionType m_resurrectionType;
    
    public ActorResurrectMonsterRequestMessage(final long corpseId, final MonsterResurrectionType resurrectionType) {
        super();
        this.m_corpseId = corpseId;
        this.m_resurrectionType = resurrectionType;
    }
    
    @Override
    public byte[] encode() {
        final int sizeDatas = 9;
        final ByteBuffer buffer = ByteBuffer.allocate(sizeDatas);
        buffer.putLong(this.m_corpseId);
        buffer.put(this.m_resurrectionType.getId());
        return this.addClientHeader((byte)3, buffer.array());
    }
    
    @Override
    public int getId() {
        return 4193;
    }
    
    static {
        m_logger = Logger.getLogger((Class)ActorResurrectMonsterRequestMessage.class);
    }
}
