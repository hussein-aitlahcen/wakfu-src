package com.ankamagames.wakfu.client.network.protocol.message.global.serverToClient.guild;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public class GuildBonusLearnedMessage extends InputOnlyProxyMessage
{
    private int m_bonusId;
    
    @Override
    public boolean decode(final byte... rawDatas) {
        final ByteBuffer bb = ByteBuffer.wrap(rawDatas);
        this.m_bonusId = bb.getInt();
        return true;
    }
    
    public int getBonusId() {
        return this.m_bonusId;
    }
    
    @Override
    public int getId() {
        return 20072;
    }
    
    @Override
    public String toString() {
        return "GuildBonusLearnedMessage{m_bonusId=" + this.m_bonusId + '}';
    }
}
