package com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.playerTitle;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public class SelectPlayerTitleRequest extends OutputOnlyProxyMessage
{
    private short m_currentTitle;
    
    @Override
    public byte[] encode() {
        final ByteBuffer bb = ByteBuffer.allocate(2);
        bb.putShort(this.m_currentTitle);
        return this.addClientHeader((byte)3, bb.array());
    }
    
    @Override
    public int getId() {
        return 5501;
    }
    
    public void setCurrentTitle(final short currentTitle) {
        this.m_currentTitle = currentTitle;
    }
}
