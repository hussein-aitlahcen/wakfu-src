package com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.vault;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.framework.kernel.core.common.collections.*;

public class VaultConsultRequestMessage extends OutputOnlyProxyMessage
{
    @Override
    public byte[] encode() {
        return this.addClientHeader((byte)3, PrimitiveArrays.EMPTY_BYTE_ARRAY);
    }
    
    @Override
    public int getId() {
        return 15670;
    }
}
