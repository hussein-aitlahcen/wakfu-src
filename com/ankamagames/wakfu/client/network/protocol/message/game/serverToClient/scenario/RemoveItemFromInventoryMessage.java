package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.scenario;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public class RemoveItemFromInventoryMessage extends InputOnlyProxyMessage
{
    private long m_uid;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        if (rawDatas.length < 8) {
            return false;
        }
        final ByteBuffer buff = ByteBuffer.wrap(rawDatas);
        this.m_uid = buff.getLong();
        return true;
    }
    
    @Override
    public int getId() {
        return 11110;
    }
    
    public long getUid() {
        return this.m_uid;
    }
}
