package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.scenario;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.framework.kernel.core.maths.*;
import java.nio.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;

public class SetChallengeTarget extends InputOnlyProxyMessage
{
    private int m_challengeId;
    private Point3 m_position;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer buffer = ByteBuffer.wrap(rawDatas);
        this.m_position = PositionValue.fromLong(buffer.getLong());
        this.m_challengeId = buffer.getInt();
        return true;
    }
    
    public int getChallengeId() {
        return this.m_challengeId;
    }
    
    public Point3 getPosition() {
        return this.m_position;
    }
    
    @Override
    public int getId() {
        return 11218;
    }
}
