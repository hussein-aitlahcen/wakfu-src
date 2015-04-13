package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.item;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.wakfu.common.game.item.elements.*;
import java.nio.*;
import com.ankamagames.baseImpl.common.clientAndServer.rawData.*;

public class RollRandomElementsResultMessage extends InputOnlyProxyMessage
{
    private long m_itemId;
    private MultiElementsInfo m_multiElementsInfo;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer buffer = ByteBuffer.wrap(rawDatas);
        this.m_itemId = buffer.getLong();
        if (!buffer.hasRemaining()) {
            return true;
        }
        final RawItemElements rawItemElements = new RawItemElements();
        rawItemElements.unserialize(buffer);
        this.m_multiElementsInfo = MultiElementsInfo.unserialize(rawItemElements);
        return true;
    }
    
    public MultiElementsInfo getMultiElementsInfo() {
        return this.m_multiElementsInfo;
    }
    
    public long getItemId() {
        return this.m_itemId;
    }
    
    @Override
    public int getId() {
        return 13010;
    }
}
