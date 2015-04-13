package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.fight.action;

import java.nio.*;
import com.ankamagames.wakfu.common.game.fight.*;

public class FighterTurnBeginMessage extends AbstractFightActionMessage
{
    private long m_fighterId;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer buffer = ByteBuffer.wrap(rawDatas);
        this.decodeFightActionHeader(buffer);
        this.m_fighterId = buffer.getLong();
        return true;
    }
    
    @Override
    public int getId() {
        return 8104;
    }
    
    public long getFighterId() {
        return this.m_fighterId;
    }
    
    @Override
    public int getActionId() {
        return 0;
    }
    
    @Override
    public FightActionType getFightActionType() {
        return FightActionType.TURN_START;
    }
}
