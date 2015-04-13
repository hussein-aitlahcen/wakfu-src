package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.chaos;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import gnu.trove.*;
import java.nio.*;

public final class InteractiveElementChaosMessage extends InputOnlyProxyMessage
{
    private TShortIntHashMap m_ieTypes;
    
    public InteractiveElementChaosMessage() {
        super();
        this.m_ieTypes = new TShortIntHashMap();
    }
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer buff = ByteBuffer.wrap(rawDatas);
        while (buff.hasRemaining()) {
            this.m_ieTypes.put(buff.getShort(), buff.getInt());
        }
        return true;
    }
    
    @Override
    public int getId() {
        return 9305;
    }
    
    public TShortIntHashMap getIeTypes() {
        return this.m_ieTypes;
    }
}
