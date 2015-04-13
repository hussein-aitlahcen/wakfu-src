package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.*;
import java.nio.*;

public class TimeResultMessage extends InputOnlyProxyMessage
{
    private GameDate m_date;
    private boolean m_isDay;
    private float m_phasePercentage;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer bb = ByteBuffer.wrap(rawDatas);
        this.m_date = GameDate.fromLong(bb.getLong());
        this.m_isDay = (bb.get() == 1);
        this.m_phasePercentage = bb.getFloat();
        return true;
    }
    
    @Override
    public int getId() {
        return 15001;
    }
    
    public GameDate getDate() {
        return this.m_date;
    }
    
    public boolean isDay() {
        return this.m_isDay;
    }
    
    public float getPhasePercentage() {
        return this.m_phasePercentage;
    }
}
