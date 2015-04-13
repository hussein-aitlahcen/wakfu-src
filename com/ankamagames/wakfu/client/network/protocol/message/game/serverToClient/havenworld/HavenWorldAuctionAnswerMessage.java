package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.havenworld;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;
import com.ankamagames.framework.kernel.utils.*;

public class HavenWorldAuctionAnswerMessage extends InputOnlyProxyMessage
{
    private long m_guildId;
    private String m_guildName;
    private int m_bidValue;
    
    @Override
    public boolean decode(final byte... rawDatas) {
        final ByteBuffer bb = ByteBuffer.wrap(rawDatas);
        this.m_guildId = bb.getLong();
        final byte[] utfName = new byte[bb.getInt()];
        bb.get(utfName);
        this.m_guildName = StringUtils.fromUTF8(utfName);
        this.m_bidValue = bb.getInt();
        return true;
    }
    
    public long getGuildId() {
        return this.m_guildId;
    }
    
    public String getGuildName() {
        return this.m_guildName;
    }
    
    public int getBidValue() {
        return this.m_bidValue;
    }
    
    @Override
    public int getId() {
        return 5526;
    }
}
