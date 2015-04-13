package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.fight.action;

import com.ankamagames.wakfu.common.game.fight.*;
import java.nio.*;

public class FighterUnsummonedMessage extends AbstractFightActionMessage
{
    private long m_fighterId;
    
    @Override
    public int getActionId() {
        return 0;
    }
    
    @Override
    public FightActionType getFightActionType() {
        return FightActionType.FIGHTER_UNSUMMONED;
    }
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer bb = ByteBuffer.wrap(rawDatas);
        this.decodeFightActionHeader(bb);
        this.m_fighterId = bb.getLong();
        return true;
    }
    
    @Override
    public int getId() {
        return 8005;
    }
    
    public long getFighterId() {
        return this.m_fighterId;
    }
}
