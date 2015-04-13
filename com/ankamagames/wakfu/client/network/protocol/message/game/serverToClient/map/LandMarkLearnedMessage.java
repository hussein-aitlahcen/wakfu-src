package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.map;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public class LandMarkLearnedMessage extends InputOnlyProxyMessage
{
    private long m_characterId;
    private byte m_landMarkId;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer bb = ByteBuffer.wrap(rawDatas);
        this.m_characterId = bb.getLong();
        this.m_landMarkId = bb.get();
        return true;
    }
    
    @Override
    public int getId() {
        return 15962;
    }
    
    public byte getLandMarkId() {
        return this.m_landMarkId;
    }
    
    public long getCharacterId() {
        return this.m_characterId;
    }
}
