package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.companion;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public final class RemoveItemFromCompanionEquipmentResultMessage extends InputOnlyProxyMessage
{
    private long m_companionId;
    private long m_itemUid;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer bb = ByteBuffer.wrap(rawDatas);
        this.m_companionId = bb.getLong();
        this.m_itemUid = bb.getLong();
        return false;
    }
    
    public long getCompanionId() {
        return this.m_companionId;
    }
    
    public long getItemUid() {
        return this.m_itemUid;
    }
    
    @Override
    public int getId() {
        return 5558;
    }
    
    @Override
    public String toString() {
        return "AddItemToCompanionEquipmentResultMessage{m_itemUid=" + this.m_itemUid + '}';
    }
}
