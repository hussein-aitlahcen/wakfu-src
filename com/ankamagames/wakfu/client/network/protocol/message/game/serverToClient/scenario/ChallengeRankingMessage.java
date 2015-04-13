package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.scenario;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public class ChallengeRankingMessage extends InputOnlyProxyMessage
{
    private short m_ranking;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer buff = ByteBuffer.wrap(rawDatas);
        this.m_ranking = buff.getShort();
        return true;
    }
    
    @Override
    public int getId() {
        return 11228;
    }
    
    public short getRanking() {
        return this.m_ranking;
    }
}
