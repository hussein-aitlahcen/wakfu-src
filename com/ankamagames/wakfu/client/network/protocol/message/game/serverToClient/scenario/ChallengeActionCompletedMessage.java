package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.scenario;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public class ChallengeActionCompletedMessage extends InputOnlyProxyMessage
{
    private int m_scenarioId;
    private int m_actionId;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer buff = ByteBuffer.wrap(rawDatas);
        this.m_scenarioId = buff.getInt();
        this.m_actionId = buff.getInt();
        return true;
    }
    
    @Override
    public int getId() {
        return 11206;
    }
    
    public int getScenarioId() {
        return this.m_scenarioId;
    }
    
    public int getActionId() {
        return this.m_actionId;
    }
}
