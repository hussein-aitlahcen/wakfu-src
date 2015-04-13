package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.companion;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;
import com.ankamagames.wakfu.common.game.companion.*;

public final class CompanionAddedMessage extends InputOnlyProxyMessage
{
    private CompanionModel m_companion;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer bb = ByteBuffer.wrap(rawDatas);
        this.m_companion = CompanionModelSerializer.unserialize(bb);
        return true;
    }
    
    public CompanionModel getCompanion() {
        return this.m_companion;
    }
    
    @Override
    public int getId() {
        return 5550;
    }
}
