package com.ankamagames.wakfu.client.network.protocol.message.connection.clientToServer;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import org.apache.commons.lang3.*;

public class ClientProxiesRequestMessage extends OutputOnlyProxyMessage
{
    @Override
    public byte[] encode() {
        return this.addClientHeader((byte)8, ArrayUtils.EMPTY_BYTE_ARRAY);
    }
    
    @Override
    public int getId() {
        return 1035;
    }
}
