package com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.seedSpreader;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.framework.kernel.core.common.collections.*;

public class SeedSpreaderRemoveSeedRequestMessage extends OutputOnlyProxyMessage
{
    private final long m_seedSpreaderId;
    
    public SeedSpreaderRemoveSeedRequestMessage(final long seedSpreaderId) {
        super();
        this.m_seedSpreaderId = seedSpreaderId;
    }
    
    @Override
    public byte[] encode() {
        final ByteArray byteArray = new ByteArray();
        byteArray.putLong(this.m_seedSpreaderId);
        return this.addClientHeader((byte)3, byteArray.toArray());
    }
    
    @Override
    public int getId() {
        return 15944;
    }
}
