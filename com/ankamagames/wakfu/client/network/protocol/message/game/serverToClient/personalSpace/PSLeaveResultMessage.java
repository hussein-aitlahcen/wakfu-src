package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.personalSpace;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;

public class PSLeaveResultMessage extends InputOnlyProxyMessage
{
    @Override
    public boolean decode(final byte[] rawDatas) {
        return true;
    }
    
    @Override
    public int getId() {
        return 10006;
    }
}
