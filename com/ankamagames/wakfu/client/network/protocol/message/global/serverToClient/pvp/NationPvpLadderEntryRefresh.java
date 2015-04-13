package com.ankamagames.wakfu.client.network.protocol.message.global.serverToClient.pvp;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.wakfu.common.game.pvp.*;
import java.nio.*;

public class NationPvpLadderEntryRefresh extends InputOnlyProxyMessage
{
    private PvpLadderEntry m_entry;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer bb = ByteBuffer.wrap(rawDatas);
        this.m_entry = ((bb.remaining() > 0) ? new PvpLadderEntry(bb) : null);
        return true;
    }
    
    @Override
    public int getId() {
        return 20412;
    }
    
    public PvpLadderEntry getEntry() {
        return this.m_entry;
    }
}
