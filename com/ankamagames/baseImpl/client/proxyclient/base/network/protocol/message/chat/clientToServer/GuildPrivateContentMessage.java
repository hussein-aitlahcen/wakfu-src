package com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.chat.clientToServer;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.framework.kernel.utils.*;
import java.nio.*;

public final class GuildPrivateContentMessage extends OutputOnlyProxyMessage
{
    private long m_groupId;
    private String m_messageContent;
    
    @Override
    public byte[] encode() {
        final byte[] mc = StringUtils.toUTF8(this.m_messageContent);
        final ByteBuffer bb = ByteBuffer.allocate(9 + mc.length);
        bb.putLong(this.m_groupId);
        bb.put((byte)mc.length);
        bb.put(mc);
        return this.addClientHeader((byte)6, bb.array());
    }
    
    @Override
    public int getId() {
        return 523;
    }
    
    public void setMessageContent(final String messageContent) {
        this.m_messageContent = messageContent;
    }
    
    public void setGroupId(final long groupId) {
        this.m_groupId = groupId;
    }
}
