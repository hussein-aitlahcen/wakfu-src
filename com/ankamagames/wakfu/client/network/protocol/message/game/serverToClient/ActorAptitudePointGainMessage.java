package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public class ActorAptitudePointGainMessage extends InputOnlyProxyMessage
{
    private byte m_aptitudeType;
    private int m_points;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer buffer = ByteBuffer.wrap(rawDatas);
        this.m_aptitudeType = buffer.get();
        this.m_points = buffer.getInt();
        return true;
    }
    
    @Override
    public int getId() {
        return 8414;
    }
    
    public byte getAptitudeType() {
        return this.m_aptitudeType;
    }
    
    public int getPoints() {
        return this.m_points;
    }
}
