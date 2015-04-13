package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.personalSpace.dimensionalBag;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public class InventoryToRoomGemExchangeResult extends InputOnlyProxyMessage
{
    private long m_gemItemUid;
    private byte m_roomlayoutPosition;
    private boolean m_primary;
    
    public long getGemItemUid() {
        return this.m_gemItemUid;
    }
    
    public byte getRoomlayoutPosition() {
        return this.m_roomlayoutPosition;
    }
    
    public boolean isPrimary() {
        return this.m_primary;
    }
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer buffer = ByteBuffer.wrap(rawDatas);
        this.m_gemItemUid = buffer.getLong();
        this.m_roomlayoutPosition = buffer.get();
        this.m_primary = (buffer.get() == 1);
        return true;
    }
    
    @Override
    public int getId() {
        return 10036;
    }
}
