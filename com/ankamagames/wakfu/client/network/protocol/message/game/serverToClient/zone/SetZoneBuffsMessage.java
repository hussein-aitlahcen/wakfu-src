package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.zone;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import gnu.trove.*;
import java.nio.*;

public class SetZoneBuffsMessage extends InputOnlyProxyMessage
{
    private final TIntArrayList m_buffIds;
    private final TIntArrayList m_buffRemainingTimes;
    
    public SetZoneBuffsMessage() {
        super();
        this.m_buffIds = new TIntArrayList();
        this.m_buffRemainingTimes = new TIntArrayList();
    }
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        if (rawDatas.length < 2) {
            return false;
        }
        final ByteBuffer buffer = ByteBuffer.wrap(rawDatas);
        final int nBuffs = buffer.getShort() & 0xFFFF;
        if (buffer.remaining() != nBuffs * 8) {
            return false;
        }
        for (int i = 0; i < nBuffs; ++i) {
            this.m_buffIds.add(buffer.getInt());
            this.m_buffRemainingTimes.add(buffer.getInt());
        }
        return true;
    }
    
    @Override
    public int getId() {
        return 15204;
    }
    
    public final int getBuffCount() {
        return this.m_buffIds.size();
    }
    
    public final int getBuffId(final int i) {
        return this.m_buffIds.get(i);
    }
    
    public final int getBuffRemainingTime(final int i) {
        return this.m_buffRemainingTimes.get(i);
    }
}
