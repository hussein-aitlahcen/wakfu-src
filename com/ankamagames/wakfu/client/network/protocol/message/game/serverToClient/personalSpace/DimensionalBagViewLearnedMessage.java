package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.personalSpace;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public class DimensionalBagViewLearnedMessage extends InputOnlyProxyMessage
{
    private int m_viewId;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer bb = ByteBuffer.wrap(rawDatas);
        this.m_viewId = bb.getInt();
        return true;
    }
    
    @Override
    public int getId() {
        return 15726;
    }
    
    public int getViewId() {
        return this.m_viewId;
    }
}
