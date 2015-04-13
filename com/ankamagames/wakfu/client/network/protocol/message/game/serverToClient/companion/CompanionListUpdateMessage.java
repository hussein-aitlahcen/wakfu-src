package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.companion;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.util.*;
import java.nio.*;
import com.ankamagames.wakfu.common.game.companion.*;

public final class CompanionListUpdateMessage extends InputOnlyProxyMessage
{
    private List<CompanionModel> m_companions;
    
    public CompanionListUpdateMessage() {
        super();
        this.m_companions = new ArrayList<CompanionModel>();
    }
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer bb = ByteBuffer.wrap(rawDatas);
        final byte companionListSize = bb.get();
        for (int i = 0; i < companionListSize; ++i) {
            this.m_companions.add(CompanionModelSerializer.unserializeToCurrentVersion(bb));
        }
        return true;
    }
    
    public List<CompanionModel> getCompanions() {
        return this.m_companions;
    }
    
    @Override
    public int getId() {
        return 5551;
    }
}
