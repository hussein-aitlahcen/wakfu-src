package com.ankamagames.wakfu.client.network.protocol.message.global.clientToServer.pvp;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.framework.kernel.core.common.collections.*;

public class NationPvpLadderEntryByIdRequest extends OutputOnlyProxyMessage
{
    private final long m_characterId;
    
    public NationPvpLadderEntryByIdRequest(final long characterId) {
        super();
        this.m_characterId = characterId;
    }
    
    @Override
    public byte[] encode() {
        final ByteArray ba = new ByteArray();
        ba.putLong(this.m_characterId);
        return this.addClientHeader((byte)6, ba.toArray());
    }
    
    @Override
    public int getId() {
        return 20405;
    }
}
