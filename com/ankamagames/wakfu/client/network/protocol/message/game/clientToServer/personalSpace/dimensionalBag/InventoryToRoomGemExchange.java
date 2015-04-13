package com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.personalSpace.dimensionalBag;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public class InventoryToRoomGemExchange extends OutputOnlyProxyMessage
{
    private long m_gemItemUid;
    private byte m_roomlayoutPosition;
    private boolean m_primary;
    
    public void setGemItemUid(final long gemItemUid) {
        this.m_gemItemUid = gemItemUid;
    }
    
    public void setRoomlayoutPosition(final byte roomlayoutPosition) {
        this.m_roomlayoutPosition = roomlayoutPosition;
    }
    
    public void setPrimary(final boolean primary) {
        this.m_primary = primary;
    }
    
    @Override
    public byte[] encode() {
        final ByteBuffer buffer = ByteBuffer.allocate(10);
        buffer.putLong(this.m_gemItemUid);
        buffer.put(this.m_roomlayoutPosition);
        buffer.put((byte)(this.m_primary ? 1 : 0));
        return this.addClientHeader((byte)3, buffer.array());
    }
    
    @Override
    public int getId() {
        return 10035;
    }
}
