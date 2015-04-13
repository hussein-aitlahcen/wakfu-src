package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.fight.action;

import java.nio.*;
import com.ankamagames.wakfu.common.game.fight.*;

public class FighterActivityChangeMessage extends AbstractFightActionMessage
{
    private long m_fighterId;
    private byte m_activity;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        if (!this.checkMessageSize(rawDatas.length, 21, true)) {
            return false;
        }
        final ByteBuffer bb = ByteBuffer.wrap(rawDatas);
        this.decodeFightActionHeader(bb);
        this.m_fighterId = bb.getLong();
        this.m_activity = bb.get();
        return true;
    }
    
    @Override
    public int getId() {
        return 4520;
    }
    
    public long getFighterId() {
        return this.m_fighterId;
    }
    
    public byte getActivity() {
        return this.m_activity;
    }
    
    @Override
    public int getActionId() {
        return 0;
    }
    
    @Override
    public FightActionType getFightActionType() {
        return FightActionType.CHANGE_ACTIVITY;
    }
}
