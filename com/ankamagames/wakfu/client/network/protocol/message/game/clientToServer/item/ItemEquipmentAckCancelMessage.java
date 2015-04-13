package com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.item;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.framework.kernel.core.common.collections.*;

public class ItemEquipmentAckCancelMessage extends OutputOnlyProxyMessage
{
    private long m_characterId;
    
    @Override
    public byte[] encode() {
        final ByteArray ba = new ByteArray();
        ba.putLong(this.m_characterId);
        return this.addClientHeader((byte)3, ba.toArray());
    }
    
    public void setCharacterId(final long characterId) {
        this.m_characterId = characterId;
    }
    
    @Override
    public int getId() {
        return 5201;
    }
}
