package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.havenworld;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.wakfu.common.game.havenWorld.action.*;
import java.nio.*;
import com.ankamagames.framework.kernel.utils.*;

public class HavenWorldAuctionInfoResultMessage extends InputOnlyProxyMessage
{
    private HavenWorldError m_error;
    private long m_guildId;
    private String m_guildName;
    private int m_bidValue;
    private long m_startDate;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer buffer = ByteBuffer.wrap(rawDatas);
        this.m_error = HavenWorldError.valueOf(buffer.getInt());
        this.m_guildId = buffer.getLong();
        final byte[] utfName = new byte[buffer.getInt()];
        buffer.get(utfName);
        this.m_guildName = StringUtils.fromUTF8(utfName);
        this.m_bidValue = buffer.getInt();
        this.m_startDate = buffer.getLong();
        return true;
    }
    
    public HavenWorldError getError() {
        return this.m_error;
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
    
    public long getStartDate() {
        return this.m_startDate;
    }
    
    @Override
    public int getId() {
        return 20094;
    }
}
