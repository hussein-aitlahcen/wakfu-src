package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.item;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public class ItemInventoryResetMessage extends InputOnlyProxyMessage
{
    private byte[] m_seralizedInventories;
    private long m_characterId;
    private boolean m_needAck;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer bb = ByteBuffer.wrap(rawDatas);
        this.m_needAck = (bb.get() == 1);
        this.m_characterId = bb.getLong();
        bb.get(this.m_seralizedInventories = new byte[bb.remaining()]);
        return true;
    }
    
    @Override
    public int getId() {
        return 5200;
    }
    
    public boolean isNeedAck() {
        return this.m_needAck;
    }
    
    public byte[] getSerializedInventories() {
        return this.m_seralizedInventories;
    }
    
    public long getCharacterId() {
        return this.m_characterId;
    }
}
