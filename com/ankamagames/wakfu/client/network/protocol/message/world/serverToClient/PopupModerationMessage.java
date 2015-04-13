package com.ankamagames.wakfu.client.network.protocol.message.world.serverToClient;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;
import com.ankamagames.framework.kernel.utils.*;

public class PopupModerationMessage extends ClientProxyMessage
{
    private String m_moderator;
    private String m_message;
    
    @Override
    public byte[] encode() {
        throw new UnsupportedOperationException("Cannot encode PopupModerationMessage on the client side");
    }
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer bb = ByteBuffer.wrap(rawDatas);
        final byte[] mt = new byte[bb.get() & 0xFF];
        bb.get(mt);
        this.m_moderator = StringUtils.fromUTF8(mt);
        final byte[] mc = new byte[bb.get() & 0xFF];
        bb.get(mc);
        this.m_message = StringUtils.fromUTF8(mc);
        return true;
    }
    
    @Override
    public int getId() {
        return 15706;
    }
    
    public String getMessage() {
        return this.m_message;
    }
    
    public String getModerator() {
        return this.m_moderator;
    }
}
