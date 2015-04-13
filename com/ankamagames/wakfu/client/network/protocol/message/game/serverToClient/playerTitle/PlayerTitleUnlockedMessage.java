package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.playerTitle;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public class PlayerTitleUnlockedMessage extends InputOnlyProxyMessage
{
    private long m_characterId;
    private int m_titleId;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer buff = ByteBuffer.wrap(rawDatas);
        this.m_characterId = buff.getLong();
        this.m_titleId = buff.getInt();
        return true;
    }
    
    @Override
    public int getId() {
        return 5502;
    }
    
    public int getTitleId() {
        return this.m_titleId;
    }
    
    public long getCharacterId() {
        return this.m_characterId;
    }
}
