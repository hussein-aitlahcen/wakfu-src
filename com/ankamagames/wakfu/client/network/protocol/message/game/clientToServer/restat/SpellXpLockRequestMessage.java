package com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.restat;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public class SpellXpLockRequestMessage extends OutputOnlyProxyMessage
{
    private boolean m_hasSpellId;
    private int m_spellId;
    
    @Override
    public byte[] encode() {
        final ByteBuffer buffer = ByteBuffer.allocate(5);
        buffer.put((byte)(this.m_hasSpellId ? 1 : 0));
        buffer.putInt(this.m_spellId);
        return this.addClientHeader((byte)3, buffer.array());
    }
    
    public void setNoSpell() {
        this.m_hasSpellId = false;
        this.m_spellId = 0;
    }
    
    public void setSpellId(final int spellId) {
        this.m_hasSpellId = true;
        this.m_spellId = spellId;
    }
    
    @Override
    public int getId() {
        return 13203;
    }
}
