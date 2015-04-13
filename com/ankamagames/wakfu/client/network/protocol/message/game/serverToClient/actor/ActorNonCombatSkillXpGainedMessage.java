package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.actor;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public class ActorNonCombatSkillXpGainedMessage extends InputOnlyProxyMessage
{
    private long m_actorId;
    private int m_skillId;
    private long m_xpGained;
    private boolean m_levelGained;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer buffer = ByteBuffer.wrap(rawDatas);
        this.m_actorId = buffer.getLong();
        this.m_skillId = buffer.getInt();
        this.m_xpGained = buffer.getLong();
        this.m_levelGained = (buffer.get() == 1);
        return true;
    }
    
    @Override
    public int getId() {
        return 4210;
    }
    
    public long getActorId() {
        return this.m_actorId;
    }
    
    public int getSkillId() {
        return this.m_skillId;
    }
    
    public long getXpGained() {
        return this.m_xpGained;
    }
    
    public boolean isLevelGained() {
        return this.m_levelGained;
    }
}
