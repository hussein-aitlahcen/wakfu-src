package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.util.*;
import java.nio.*;
import com.ankamagames.framework.kernel.utils.*;

public final class ActorPlayEmoteAnswerMessage extends InputOnlyProxyMessage
{
    private long m_characterId;
    private int m_emoteId;
    private final HashMap<String, Object> m_variables;
    
    public ActorPlayEmoteAnswerMessage() {
        super();
        this.m_variables = new HashMap<String, Object>();
    }
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer bb = ByteBuffer.wrap(rawDatas);
        this.m_characterId = bb.getLong();
        this.m_emoteId = bb.getInt();
        for (int varSize = bb.getInt(), i = 0; i < varSize; ++i) {
            final byte[] data = new byte[bb.getInt()];
            bb.get(data);
            this.m_variables.put(StringUtils.fromUTF8(data), bb.getLong());
        }
        return true;
    }
    
    @Override
    public int getId() {
        return 15402;
    }
    
    public long getCharacterId() {
        return this.m_characterId;
    }
    
    public int getEmoteId() {
        return this.m_emoteId;
    }
    
    public HashMap<String, Object> getVariables() {
        return this.m_variables;
    }
}
