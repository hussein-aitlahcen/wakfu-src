package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.actor;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;

public class ActorLeaveInstanceMessage extends InputOnlyProxyMessage
{
    @Override
    public boolean decode(final byte[] rawDatas) {
        return this.checkMessageSize(rawDatas.length, 0, true);
    }
    
    @Override
    public int getId() {
        return 4128;
    }
}
