package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.actor;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;

public class PlayerUnstuckMessage extends InputOnlyProxyMessage
{
    @Override
    public boolean decode(final byte[] rawDatas) {
        return rawDatas.length == 0;
    }
    
    @Override
    public int getId() {
        return 4106;
    }
}
