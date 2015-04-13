package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.scenario;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public class RemoveItemFromInventoryWithReferenceIdMessage extends InputOnlyProxyMessage
{
    private int m_referenceId;
    private int m_count;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        if (rawDatas.length < 8) {
            return false;
        }
        final ByteBuffer buff = ByteBuffer.wrap(rawDatas);
        this.m_referenceId = buff.getInt();
        this.m_count = buff.getInt();
        return true;
    }
    
    @Override
    public int getId() {
        return 11112;
    }
    
    public int getReferenceId() {
        return this.m_referenceId;
    }
    
    public int getCount() {
        return this.m_count;
    }
}
