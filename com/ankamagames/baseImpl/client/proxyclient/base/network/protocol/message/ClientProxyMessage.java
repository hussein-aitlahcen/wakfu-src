package com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message;

import com.ankamagames.framework.kernel.core.common.message.*;
import io.netty.buffer.*;

public abstract class ClientProxyMessage extends Message
{
    public static final int HEADER_SIZE = 5;
    
    public byte[] addClientHeader(final byte architectureTarget, final byte[] datas) {
        final int msgSize = 5 + datas.length;
        final ByteBuf buf = Unpooled.buffer(msgSize);
        buf.writeShort(msgSize);
        buf.writeByte((int)architectureTarget);
        buf.writeShort(this.getId());
        buf.writeBytes(datas);
        return buf.array();
    }
}
