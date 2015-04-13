package com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.chat.clientToServer;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.framework.kernel.core.common.collections.*;

public class CloseModeratorRequestMessage extends OutputOnlyProxyMessage
{
    @Override
    public byte[] encode() {
        return this.addClientHeader((byte)4, PrimitiveArrays.EMPTY_BYTE_ARRAY);
    }
    
    @Override
    public int getId() {
        return 3173;
    }
}
