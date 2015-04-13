package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.game.interactiveElement.serverToClient.*;
import com.ankamagames.framework.script.action.*;
import java.nio.*;

public class InteractiveElementUpdateActionMessage extends InteractiveElementUpdateMessage implements ActionMessage
{
    private long m_contextId;
    private byte m_contextType;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer buffer = ByteBuffer.wrap(rawDatas);
        this.m_contextType = buffer.get();
        this.m_contextId = buffer.getLong();
        this.decode(buffer);
        return true;
    }
    
    @Override
    public int getId() {
        return 4300;
    }
    
    @Override
    public byte getActionContextType() {
        return this.m_contextType;
    }
    
    @Override
    public long getActionContextUniqueId() {
        return this.m_contextId;
    }
}
