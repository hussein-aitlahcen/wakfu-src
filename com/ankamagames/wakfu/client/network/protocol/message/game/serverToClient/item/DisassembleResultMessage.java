package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.item;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public class DisassembleResultMessage extends InputOnlyProxyMessage
{
    private int m_gemsCount;
    private int m_totalDisassembleCount;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer buffer = ByteBuffer.wrap(rawDatas);
        this.m_gemsCount = buffer.getInt();
        this.m_totalDisassembleCount = buffer.getInt();
        return true;
    }
    
    public int getGemsCount() {
        return this.m_gemsCount;
    }
    
    public int getTotalDisassembleCount() {
        return this.m_totalDisassembleCount;
    }
    
    @Override
    public int getId() {
        return 4186;
    }
}
