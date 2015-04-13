package com.ankamagames.wakfu.client.network.protocol.message.global.clientToServer.pvp;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.framework.kernel.utils.*;
import com.ankamagames.framework.kernel.core.common.collections.*;

public class NationPvpLadderEntryByNameRequest extends OutputOnlyProxyMessage
{
    private final String m_characterName;
    
    public NationPvpLadderEntryByNameRequest(final String characterName) {
        super();
        this.m_characterName = characterName;
    }
    
    @Override
    public byte[] encode() {
        final byte[] name = StringUtils.toUTF8(this.m_characterName);
        final ByteArray ba = new ByteArray();
        ba.putInt(name.length);
        ba.put(name);
        return this.addClientHeader((byte)6, ba.toArray());
    }
    
    @Override
    public int getId() {
        return 20407;
    }
}
