package com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.fight;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public class SpellLevelCastRequestMessage extends OutputOnlyProxyMessage
{
    private long m_fighterId;
    private long m_spellId;
    private int m_castPositionX;
    private int m_castPositionY;
    private short m_castPositionZ;
    
    @Override
    public byte[] encode() {
        final ByteBuffer bb = ByteBuffer.allocate(26);
        bb.putLong(this.m_fighterId);
        bb.putLong(this.m_spellId);
        bb.putInt(this.m_castPositionX);
        bb.putInt(this.m_castPositionY);
        bb.putShort(this.m_castPositionZ);
        return this.addClientHeader((byte)3, bb.array());
    }
    
    @Override
    public int getId() {
        return 8109;
    }
    
    public void setFighterId(final long fighterId) {
        this.m_fighterId = fighterId;
    }
    
    public void setSpellId(final long spellId) {
        this.m_spellId = spellId;
    }
    
    public void setCastPosition(final int x, final int y, final short z) {
        this.m_castPositionX = x;
        this.m_castPositionY = y;
        this.m_castPositionZ = z;
    }
}
