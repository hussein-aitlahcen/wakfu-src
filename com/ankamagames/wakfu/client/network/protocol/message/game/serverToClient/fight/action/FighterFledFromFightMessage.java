package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.fight.action;

import java.nio.*;
import com.ankamagames.wakfu.common.game.fight.*;

public class FighterFledFromFightMessage extends AbstractFightActionMessage
{
    private long m_fighterId;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        if (!this.checkMessageSize(rawDatas.length, 13, false)) {
            return false;
        }
        final ByteBuffer buffer = ByteBuffer.wrap(rawDatas);
        this.decodeFightActionHeader(buffer);
        this.m_fighterId = buffer.getLong();
        return true;
    }
    
    @Override
    public int getId() {
        return 8302;
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
        return FightActionType.FIGHT_END;
    }
}
