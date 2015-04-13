package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.fight.action;

import java.nio.*;
import com.ankamagames.wakfu.common.game.fight.*;

public class FighterTurnEndMessage extends AbstractFightActionMessage
{
    private long m_fighterId;
    private int m_timeScoreGain;
    private int m_addedRemainingSeconds;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer bb = ByteBuffer.wrap(rawDatas);
        this.decodeFightActionHeader(bb);
        this.m_fighterId = bb.getLong();
        this.m_timeScoreGain = bb.getInt();
        this.m_addedRemainingSeconds = bb.getInt();
        return true;
    }
    
    @Override
    public int getId() {
        return 8106;
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
        return FightActionType.TURN_END;
    }
    
    public int getTimeScoreGain() {
        return this.m_timeScoreGain;
    }
    
    public int getAddedRemainingSeconds() {
        return this.m_addedRemainingSeconds;
    }
}
