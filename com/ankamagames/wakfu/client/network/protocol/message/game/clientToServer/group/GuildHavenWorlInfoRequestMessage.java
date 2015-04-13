package com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.group;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import org.apache.commons.lang3.*;

public class GuildHavenWorlInfoRequestMessage extends OutputOnlyProxyMessage
{
    @Override
    public byte[] encode() {
        return this.addClientHeader((byte)6, ArrayUtils.EMPTY_BYTE_ARRAY);
    }
    
    @Override
    public int getId() {
        return 20089;
    }
}
