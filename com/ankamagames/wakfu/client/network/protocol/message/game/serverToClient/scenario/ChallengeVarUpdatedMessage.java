package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.scenario;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public class ChallengeVarUpdatedMessage extends InputOnlyProxyMessage
{
    private int m_scenarioId;
    private byte m_varId;
    private long m_varNewValue;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer buff = ByteBuffer.wrap(rawDatas);
        this.m_scenarioId = buff.getInt();
        this.m_varId = buff.get();
        this.m_varNewValue = buff.getLong();
        return true;
    }
    
    @Override
    public int getId() {
        return 11204;
    }
    
    public int getScenarioId() {
        return this.m_scenarioId;
    }
    
    public byte getVarId() {
        return this.m_varId;
    }
    
    public long getVarNewValue() {
        return this.m_varNewValue;
    }
}
