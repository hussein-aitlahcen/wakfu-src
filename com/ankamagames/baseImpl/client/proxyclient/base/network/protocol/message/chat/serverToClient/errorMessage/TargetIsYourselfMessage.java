package com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.chat.serverToClient.errorMessage;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;

public class TargetIsYourselfMessage extends InputOnlyProxyMessage
{
    @Override
    public boolean decode(final byte[] rawDatas) {
        return true;
    }
    
    @Override
    public int getId() {
        return 3214;
    }
}
