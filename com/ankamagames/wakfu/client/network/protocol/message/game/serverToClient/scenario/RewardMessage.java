package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.scenario;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public class RewardMessage extends InputOnlyProxyMessage
{
    private int m_scenarioId;
    private byte m_type;
    private int m_reward;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer buff = ByteBuffer.wrap(rawDatas);
        this.m_scenarioId = buff.getInt();
        this.m_type = buff.get();
        this.m_reward = buff.getInt();
        return true;
    }
    
    @Override
    public int getId() {
        return 11208;
    }
    
    public int getScenarioId() {
        return this.m_scenarioId;
    }
    
    public byte getType() {
        return this.m_type;
    }
    
    public int getReward() {
        return this.m_reward;
    }
}
