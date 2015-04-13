package com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.havenWorld;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.framework.kernel.core.common.collections.*;

public final class LeaveHavenWorldRequestMessage extends OutputOnlyProxyMessage
{
    @Override
    public byte[] encode() {
        final ByteArray byteArray = new ByteArray();
        return this.addClientHeader((byte)3, byteArray.toArray());
    }
    
    @Override
    public int getId() {
        return 15654;
    }
}
