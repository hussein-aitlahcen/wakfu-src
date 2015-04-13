package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.scenario;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public class CreateGraphicalInputRequestMessage extends InputOnlyProxyMessage
{
    private int m_scenarioId;
    private byte m_varId;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer buff = ByteBuffer.wrap(rawDatas);
        this.m_scenarioId = buff.getInt();
        this.m_varId = buff.get();
        return true;
    }
    
    public int getScenarioId() {
        return this.m_scenarioId;
    }
    
    public byte getVarId() {
        return this.m_varId;
    }
    
    @Override
    public int getId() {
        return 11216;
    }
}
