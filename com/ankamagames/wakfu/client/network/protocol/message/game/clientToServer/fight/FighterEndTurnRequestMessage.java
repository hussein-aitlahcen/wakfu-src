package com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.fight;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public class FighterEndTurnRequestMessage extends OutputOnlyProxyMessage
{
    private long m_fighterId;
    private short m_tableTurn;
    
    public FighterEndTurnRequestMessage(final long fighterId, final short tableTurn) {
        super();
        this.m_fighterId = fighterId;
        this.m_tableTurn = tableTurn;
    }
    
    @Override
    public byte[] encode() {
        final ByteBuffer bb = ByteBuffer.allocate(10);
        bb.putLong(this.m_fighterId);
        bb.putShort(this.m_tableTurn);
        return this.addClientHeader((byte)3, bb.array());
    }
    
    @Override
    public int getId() {
        return 8105;
    }
}
