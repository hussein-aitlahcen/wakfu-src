package com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.chat.serverToClient.errorMessage;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;
import com.ankamagames.framework.kernel.utils.*;

public class ChannelNotFoundMessage extends InputOnlyProxyMessage
{
    private String m_channelName;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer bb = ByteBuffer.wrap(rawDatas);
        final byte[] cn = new byte[bb.get()];
        bb.get(cn);
        this.m_channelName = StringUtils.fromUTF8(cn);
        return true;
    }
    
    @Override
    public int getId() {
        return 3202;
    }
    
    public String getChannelName() {
        return this.m_channelName;
    }
}
