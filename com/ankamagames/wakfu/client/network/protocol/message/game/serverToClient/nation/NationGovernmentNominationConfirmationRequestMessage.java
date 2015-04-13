package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.nation;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.wakfu.common.game.nation.government.*;
import java.nio.*;

public class NationGovernmentNominationConfirmationRequestMessage extends InputOnlyProxyMessage
{
    private NationRank m_rank;
    
    public NationRank getRank() {
        return this.m_rank;
    }
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer buffer = ByteBuffer.wrap(rawDatas);
        this.m_rank = NationRank.getById(buffer.getLong());
        return true;
    }
    
    @Override
    public int getId() {
        return 15124;
    }
}
