package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.scenario;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public class ChallengeProposalMessage extends InputOnlyProxyMessage
{
    private int m_scenarioId;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer buff = ByteBuffer.wrap(rawDatas);
        this.m_scenarioId = buff.getInt();
        return true;
    }
    
    @Override
    public int getId() {
        return 11210;
    }
    
    public int getScenarioId() {
        return this.m_scenarioId;
    }
}
