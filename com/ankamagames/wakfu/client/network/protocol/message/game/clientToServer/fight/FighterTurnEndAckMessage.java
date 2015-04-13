package com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.fight;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public final class FighterTurnEndAckMessage extends OutputOnlyProxyMessage
{
    private int m_acknowledgedTurn;
    
    public FighterTurnEndAckMessage(final int acknowledgedTurn) {
        super();
        this.m_acknowledgedTurn = acknowledgedTurn;
    }
    
    @Override
    public byte[] encode() {
        final ByteBuffer bb = ByteBuffer.allocate(4);
        bb.putInt(this.m_acknowledgedTurn);
        return this.addClientHeader((byte)3, bb.array());
    }
    
    @Override
    public int getId() {
        return 8112;
    }
}
