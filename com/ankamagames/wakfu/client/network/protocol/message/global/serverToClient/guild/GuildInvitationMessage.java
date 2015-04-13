package com.ankamagames.wakfu.client.network.protocol.message.global.serverToClient.guild;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;
import com.ankamagames.framework.kernel.utils.*;

public class GuildInvitationMessage extends InputOnlyProxyMessage
{
    private String m_inviterName;
    private String m_guildName;
    
    @Override
    public boolean decode(final byte... rawDatas) {
        final ByteBuffer bb = ByteBuffer.wrap(rawDatas);
        final byte[] inviterUtf = new byte[bb.getInt()];
        bb.get(inviterUtf);
        this.m_inviterName = StringUtils.fromUTF8(inviterUtf);
        final byte[] guildUtf = new byte[bb.getInt()];
        bb.get(guildUtf);
        this.m_guildName = StringUtils.fromUTF8(guildUtf);
        return true;
    }
    
    public String getInviterName() {
        return this.m_inviterName;
    }
    
    public String getGuildName() {
        return this.m_guildName;
    }
    
    @Override
    public int getId() {
        return 20056;
    }
    
    @Override
    public String toString() {
        return "GuildInvitationMessage{m_inviterName='" + this.m_inviterName + '\'' + ", m_guildName='" + this.m_guildName + '\'' + '}';
    }
}
