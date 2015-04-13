package com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.fight;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public class FightRecoveryRequestMessage extends OutputOnlyProxyMessage
{
    private int m_fightId;
    
    public FightRecoveryRequestMessage() {
        super();
        this.m_fightId = -1;
    }
    
    @Override
    public byte[] encode() {
        final ByteBuffer buffer = ByteBuffer.allocate(4);
        buffer.putInt(this.m_fightId);
        return this.addClientHeader((byte)3, buffer.array());
    }
    
    @Override
    public int getId() {
        return 8011;
    }
    
    public void setFightId(final int fightId) {
        this.m_fightId = fightId;
    }
}
