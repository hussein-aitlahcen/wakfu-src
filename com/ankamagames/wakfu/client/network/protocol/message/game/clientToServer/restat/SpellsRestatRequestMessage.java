package com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.restat;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import gnu.trove.*;
import java.nio.*;

public class SpellsRestatRequestMessage extends OutputOnlyProxyMessage
{
    private final TIntArrayList m_referenceIds;
    private final TLongArrayList m_xps;
    
    public SpellsRestatRequestMessage() {
        super();
        this.m_referenceIds = new TIntArrayList();
        this.m_xps = new TLongArrayList();
    }
    
    @Override
    public byte[] encode() {
        final int spellsCount = this.m_referenceIds.size();
        final ByteBuffer buffer = ByteBuffer.allocate(1 + 12 * spellsCount);
        buffer.put((byte)spellsCount);
        for (int i = 0; i < spellsCount; ++i) {
            buffer.putInt(this.m_referenceIds.get(i));
            buffer.putLong(this.m_xps.get(i));
        }
        return this.addClientHeader((byte)3, buffer.array());
    }
    
    public void addXpToSpell(final int spellReferenceId, final long xp) {
        this.m_referenceIds.add(spellReferenceId);
        this.m_xps.add(xp);
    }
    
    @Override
    public int getId() {
        return 13201;
    }
}
