package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public class SearchTreasureResultMessage extends InputOnlyProxyMessage
{
    private int m_errorId;
    private int m_x;
    private int m_y;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer buffer = ByteBuffer.wrap(rawDatas);
        this.m_errorId = buffer.getInt();
        this.m_x = buffer.getInt();
        this.m_y = buffer.getInt();
        return true;
    }
    
    @Override
    public int getId() {
        return 9508;
    }
    
    public int getErrorId() {
        return this.m_errorId;
    }
    
    public int getX() {
        return this.m_x;
    }
    
    public int getY() {
        return this.m_y;
    }
}
