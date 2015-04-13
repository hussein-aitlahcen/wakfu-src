package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public class TeleporterLoadingMessage extends InputOnlyProxyMessage
{
    private int m_parameterId;
    private int m_linkId;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer bb = ByteBuffer.wrap(rawDatas);
        this.m_parameterId = bb.getInt();
        this.m_linkId = bb.getInt();
        return true;
    }
    
    @Override
    public int getId() {
        return 15804;
    }
    
    public int getParameterId() {
        return this.m_parameterId;
    }
    
    public int getLinkId() {
        return this.m_linkId;
    }
}
