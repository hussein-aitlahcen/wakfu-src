package com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.companion;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.framework.kernel.core.common.collections.*;
import com.ankamagames.framework.kernel.utils.*;

public final class RenameCompanionRequestMessage extends OutputOnlyProxyMessage
{
    private final long m_companionId;
    private final String m_name;
    
    public RenameCompanionRequestMessage(final long companionId, final String name) {
        super();
        this.m_companionId = companionId;
        this.m_name = name;
    }
    
    @Override
    public byte[] encode() {
        final ByteArray ba = new ByteArray();
        ba.putLong(this.m_companionId);
        final byte[] encoded = StringUtils.toUTF8(this.m_name);
        ba.put((byte)encoded.length);
        ba.put(encoded);
        return this.addClientHeader((byte)3, ba.toArray());
    }
    
    @Override
    public int getId() {
        return 5561;
    }
    
    @Override
    public String toString() {
        return "RenameCompanionRequestMessage{m_companionId=" + this.m_companionId + ", m_name='" + this.m_name + '\'' + '}';
    }
}
