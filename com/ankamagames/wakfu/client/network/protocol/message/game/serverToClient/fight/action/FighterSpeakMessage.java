package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.fight.action;

import java.nio.*;
import com.ankamagames.wakfu.common.game.fight.*;

public class FighterSpeakMessage extends AbstractFightActionMessage
{
    private long m_fighterId;
    private int m_translationId;
    private boolean m_blocking;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer buffer = ByteBuffer.wrap(rawDatas);
        this.decodeFightActionHeader(buffer);
        this.m_fighterId = buffer.getLong();
        this.m_translationId = buffer.getInt();
        this.m_blocking = (buffer.get() == 1);
        return true;
    }
    
    @Override
    public int getId() {
        return 8410;
    }
    
    public long getFighterId() {
        return this.m_fighterId;
    }
    
    public int getTranslationId() {
        return this.m_translationId;
    }
    
    public boolean isBlocking() {
        return this.m_blocking;
    }
    
    @Override
    public int getActionId() {
        return 0;
    }
    
    @Override
    public FightActionType getFightActionType() {
        return FightActionType.FIGHTER_SPEAK;
    }
}
