package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public class CharacterHealthUpdateMessage extends InputOnlyProxyMessage
{
    private int m_health;
    private int m_healthRegen;
    
    public int getValue() {
        return this.m_health;
    }
    
    public int getHealthRegen() {
        return this.m_healthRegen;
    }
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer buffer = ByteBuffer.wrap(rawDatas);
        this.m_health = buffer.getInt();
        this.m_healthRegen = buffer.getInt();
        return true;
    }
    
    @Override
    public int getId() {
        return 4124;
    }
}
