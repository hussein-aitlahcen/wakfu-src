package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.craft;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;
import com.ankamagames.wakfu.common.game.craft.*;

public class CraftOccupationResultMessage extends InputOnlyProxyMessage
{
    private int result;
    private int m_resultItemRefId;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer bb = ByteBuffer.wrap(rawDatas);
        this.result = bb.get();
        this.m_resultItemRefId = bb.getInt();
        return true;
    }
    
    @Override
    public int getId() {
        return 15718;
    }
    
    public CraftResult getResult() {
        return CraftResult.values()[this.result];
    }
    
    public int getResultItemRefId() {
        return this.m_resultItemRefId;
    }
}
