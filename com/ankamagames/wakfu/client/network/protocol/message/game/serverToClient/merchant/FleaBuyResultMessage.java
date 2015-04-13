package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.merchant;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.wakfu.common.rawData.*;
import java.nio.*;

public class FleaBuyResultMessage extends InputOnlyProxyMessage
{
    private byte m_error;
    private long m_container;
    private RawInventoryItem m_serializedItem;
    
    public byte getError() {
        return this.m_error;
    }
    
    public long getContainer() {
        return this.m_container;
    }
    
    public RawInventoryItem getSerializedItem() {
        return this.m_serializedItem;
    }
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer buffer = ByteBuffer.wrap(rawDatas);
        this.m_error = buffer.get();
        if (this.m_error == 0) {
            this.m_container = buffer.getLong();
            this.m_serializedItem = new RawInventoryItem();
            if (!this.m_serializedItem.unserialize(buffer)) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public int getId() {
        return 5234;
    }
}
