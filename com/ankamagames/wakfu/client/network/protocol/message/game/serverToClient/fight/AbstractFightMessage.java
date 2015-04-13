package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.fight;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public abstract class AbstractFightMessage extends InputOnlyProxyMessage
{
    protected static final int FIGHT_HEADER_SIZE = 4;
    private int m_fightId;
    
    public AbstractFightMessage() {
        super();
        this.m_fightId = -1;
    }
    
    protected void decodeFightHeader(final ByteBuffer buff) {
        this.m_fightId = buff.getInt();
    }
    
    public int getFightId() {
        return this.m_fightId;
    }
}
