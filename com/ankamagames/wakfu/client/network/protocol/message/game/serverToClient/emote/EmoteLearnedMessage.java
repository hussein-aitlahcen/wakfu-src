package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.emote;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public class EmoteLearnedMessage extends InputOnlyProxyMessage
{
    private long m_characterId;
    private int m_emoteId;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer bb = ByteBuffer.wrap(rawDatas);
        this.m_characterId = bb.getLong();
        this.m_emoteId = bb.getInt();
        return true;
    }
    
    @Override
    public int getId() {
        return 15722;
    }
    
    public int getEmoteId() {
        return this.m_emoteId;
    }
    
    public long getCharacterId() {
        return this.m_characterId;
    }
}
