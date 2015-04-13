package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.fight.action;

import java.nio.*;
import com.ankamagames.wakfu.common.game.fight.*;

public final class MoverHitInvisibleFighterMessage extends AbstractFightActionMessage
{
    private long m_moverId;
    private long m_hitedInvisibleFighter;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer buffer = ByteBuffer.wrap(rawDatas);
        this.decodeFightActionHeader(buffer);
        this.m_moverId = buffer.getLong();
        this.m_hitedInvisibleFighter = buffer.getLong();
        return true;
    }
    
    public long getHitedInvisibleFighter() {
        return this.m_hitedInvisibleFighter;
    }
    
    public long getMoverId() {
        return this.m_moverId;
    }
    
    @Override
    public int getActionId() {
        return this.getId();
    }
    
    @Override
    public FightActionType getFightActionType() {
        return FightActionType.MOVER_HIT_INVISIBLE_FIGHTER;
    }
    
    @Override
    public int getId() {
        return 8412;
    }
}
