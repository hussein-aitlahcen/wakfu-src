package com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.fight;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public final class PointEffectSelectionMessage extends OutputOnlyProxyMessage
{
    private long m_fighterId;
    private int m_effectId;
    
    public void setFighterId(final long fighterId) {
        this.m_fighterId = fighterId;
    }
    
    public void setEffectId(final int effectId) {
        this.m_effectId = effectId;
    }
    
    @Override
    public byte[] encode() {
        final ByteBuffer bb = ByteBuffer.allocate(12);
        bb.putLong(this.m_fighterId);
        bb.putInt(this.m_effectId);
        return this.addClientHeader((byte)3, bb.array());
    }
    
    @Override
    public int getId() {
        return 8307;
    }
}
