package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.fight.action;

import com.ankamagames.framework.kernel.core.maths.*;
import java.nio.*;
import com.ankamagames.wakfu.common.game.fight.*;

public class FighterChangeDirectionMessage extends AbstractFightActionMessage
{
    private long m_fighterId;
    private Direction8 m_direction;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer buffer = ByteBuffer.wrap(rawDatas);
        this.decodeFightActionHeader(buffer);
        this.m_fighterId = buffer.getLong();
        this.m_direction = Direction8.getDirectionFromIndex(buffer.get());
        return true;
    }
    
    @Override
    public int getId() {
        return 4522;
    }
    
    public long getFighterId() {
        return this.m_fighterId;
    }
    
    public Direction8 getDirection() {
        return this.m_direction;
    }
    
    @Override
    public int getActionId() {
        return 0;
    }
    
    @Override
    public FightActionType getFightActionType() {
        return FightActionType.CHANGE_DIRECTION;
    }
}
