package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.item;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public class EquipCostumeResultMessage extends InputOnlyProxyMessage
{
    private long m_characterId;
    private int m_itemRefId;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer buffer = ByteBuffer.wrap(rawDatas);
        this.m_characterId = buffer.getLong();
        this.m_itemRefId = buffer.getInt();
        return true;
    }
    
    public long getCharacterId() {
        return this.m_characterId;
    }
    
    public int getItemRefId() {
        return this.m_itemRefId;
    }
    
    @Override
    public int getId() {
        return 11130;
    }
}
