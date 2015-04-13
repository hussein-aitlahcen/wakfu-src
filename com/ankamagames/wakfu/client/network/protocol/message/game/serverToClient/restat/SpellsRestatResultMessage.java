package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.restat;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.wakfu.common.rawData.*;
import java.nio.*;

public class SpellsRestatResultMessage extends InputOnlyProxyMessage
{
    private boolean m_success;
    private boolean m_globalRestat;
    private RawSpellLevelInventory m_rawInventory;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer bb = ByteBuffer.wrap(rawDatas);
        this.m_success = (bb.get() == 1);
        this.m_globalRestat = (bb.get() == 1);
        if (this.m_success) {
            (this.m_rawInventory = new RawSpellLevelInventory()).unserialize(bb);
        }
        return true;
    }
    
    public boolean isSuccess() {
        return this.m_success;
    }
    
    public boolean isGlobalRestat() {
        return this.m_globalRestat;
    }
    
    public RawSpellLevelInventory getRawInventory() {
        return this.m_rawInventory;
    }
    
    @Override
    public int getId() {
        return 13202;
    }
}
