package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.item;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public class ChangeBagPositionResultMessage extends InputOnlyProxyMessage
{
    private long m_bagUid1;
    private byte m_position1;
    private long m_bagUid2;
    private byte m_position2;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer buffer = ByteBuffer.wrap(rawDatas);
        this.m_bagUid1 = buffer.getLong();
        this.m_position1 = buffer.get();
        if (buffer.get() == 1) {
            this.m_bagUid2 = buffer.getLong();
            this.m_position2 = buffer.get();
        }
        else {
            this.m_bagUid2 = -1L;
            this.m_position2 = -1;
        }
        return true;
    }
    
    public long getBagUid1() {
        return this.m_bagUid1;
    }
    
    public byte getPosition1() {
        return this.m_position1;
    }
    
    public boolean isSwapBags() {
        return this.m_bagUid2 != -1L && this.m_position2 != -1;
    }
    
    public long getBagUid2() {
        return this.m_bagUid2;
    }
    
    public byte getPosition2() {
        return this.m_position2;
    }
    
    @Override
    public int getId() {
        return 5228;
    }
}
