package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.scenario;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public class TerminateScenarioMessage extends InputOnlyProxyMessage
{
    private int m_scenarioId;
    private int m_reason;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer buff = ByteBuffer.wrap(rawDatas);
        this.m_scenarioId = buff.getInt();
        this.m_reason = buff.getInt();
        return true;
    }
    
    @Override
    public int getId() {
        return 11200;
    }
    
    public int getReason() {
        return this.m_reason;
    }
    
    public int getScenarioId() {
        return this.m_scenarioId;
    }
}
