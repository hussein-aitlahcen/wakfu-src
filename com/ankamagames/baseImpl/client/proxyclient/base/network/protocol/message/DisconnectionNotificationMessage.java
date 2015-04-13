package com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message;

import org.apache.commons.lang3.*;

public class DisconnectionNotificationMessage extends OutputOnlyProxyMessage
{
    @Override
    public byte[] encode() {
        return this.addClientHeader((byte)0, ArrayUtils.EMPTY_BYTE_ARRAY);
    }
    
    @Override
    public int getId() {
        return 1;
    }
}
