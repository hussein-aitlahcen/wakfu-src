package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.scenario;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public class RunClientEventMessage extends InputOnlyProxyMessage
{
    private boolean m_fromReward;
    private int m_eventId;
    private int m_scenarioId;
    private long[] m_parameters;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        if (rawDatas.length < 10) {
            return false;
        }
        final ByteBuffer buff = ByteBuffer.wrap(rawDatas);
        this.m_fromReward = (buff.get() == 1);
        this.m_eventId = buff.getInt();
        this.m_scenarioId = buff.getInt();
        this.m_parameters = new long[buff.get()];
        for (int i = 0; i < this.m_parameters.length; ++i) {
            this.m_parameters[i] = buff.getLong();
        }
        return true;
    }
    
    @Override
    public int getId() {
        return 11100;
    }
    
    public int getEventId() {
        return this.m_eventId;
    }
    
    public int getScenarioId() {
        return this.m_scenarioId;
    }
    
    public long[] getParameters() {
        return this.m_parameters;
    }
    
    public boolean isFromReward() {
        return this.m_fromReward;
    }
}
