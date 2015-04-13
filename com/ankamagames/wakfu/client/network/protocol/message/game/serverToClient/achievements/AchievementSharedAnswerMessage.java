package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.achievements;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import gnu.trove.*;
import java.nio.*;

public class AchievementSharedAnswerMessage extends InputOnlyProxyMessage
{
    private TLongArrayList m_sharedWith;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer buffer = ByteBuffer.wrap(rawDatas);
        final byte numShared = buffer.get();
        this.m_sharedWith = new TLongArrayList(numShared);
        for (int i = 0; i < numShared; ++i) {
            this.m_sharedWith.add(buffer.getLong());
        }
        return true;
    }
    
    @Override
    public int getId() {
        return 15616;
    }
    
    public TLongArrayList getSharedWith() {
        return this.m_sharedWith;
    }
}
