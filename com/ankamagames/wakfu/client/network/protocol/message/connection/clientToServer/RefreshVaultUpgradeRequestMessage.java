package com.ankamagames.wakfu.client.network.protocol.message.connection.clientToServer;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.framework.kernel.core.common.collections.*;

public final class RefreshVaultUpgradeRequestMessage extends OutputOnlyProxyMessage
{
    @Override
    public byte[] encode() {
        return this.addClientHeader((byte)1, PrimitiveArrays.EMPTY_BYTE_ARRAY);
    }
    
    @Override
    public int getId() {
        return 1206;
    }
}
