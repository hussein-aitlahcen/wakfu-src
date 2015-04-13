package com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.chat.clientToServer;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.framework.kernel.utils.*;
import java.nio.*;

public class AddCommentaryToContactMessage extends OutputOnlyProxyMessage
{
    private String m_friendName;
    private String m_commentary;
    
    @Override
    public byte[] encode() {
        final byte[] fn = StringUtils.toUTF8(this.m_friendName);
        final byte[] commentary = StringUtils.toUTF8(this.m_commentary);
        final ByteBuffer bb = ByteBuffer.allocate(1 + fn.length + 1 + commentary.length);
        bb.put((byte)fn.length);
        bb.put(fn);
        bb.put((byte)commentary.length);
        bb.put(commentary);
        return this.addClientHeader((byte)4, bb.array());
    }
    
    @Override
    public int getId() {
        return 3157;
    }
    
    public void setFriendName(final String friendName) {
        this.m_friendName = friendName;
    }
    
    public void setCommentary(final String commentary) {
        this.m_commentary = commentary;
    }
}
