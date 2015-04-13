package com.ankamagames.wakfu.client.network.protocol.message.global.serverToClient.guild;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;
import com.ankamagames.framework.kernel.utils.*;

public class GuildInfoAnswerMessage extends InputOnlyProxyMessage
{
    private String m_guildName;
    private long m_guildId;
    private long m_blazonId;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer bb = ByteBuffer.wrap(rawDatas);
        final byte[] name = new byte[bb.get()];
        bb.get(name);
        this.m_guildName = StringUtils.fromUTF8(name);
        this.m_guildId = bb.getLong();
        this.m_blazonId = bb.getLong();
        return false;
    }
    
    @Override
    public int getId() {
        return 526;
    }
    
    public String getGuildName() {
        return this.m_guildName;
    }
    
    public long getGuildId() {
        return this.m_guildId;
    }
    
    public long getBlazonId() {
        return this.m_blazonId;
    }
}
