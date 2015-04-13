package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.fight.action;

import java.nio.*;
import com.ankamagames.wakfu.common.game.fight.*;

public class FighterTackledMessage extends AbstractFightActionMessage
{
    private long m_tackledFighterId;
    private long m_tacklerId;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        if (!this.checkMessageSize(rawDatas.length, 28, true)) {
            return false;
        }
        final ByteBuffer bb = ByteBuffer.wrap(rawDatas);
        this.decodeFightActionHeader(bb);
        this.m_tackledFighterId = bb.getLong();
        this.m_tacklerId = bb.getLong();
        return true;
    }
    
    @Override
    public int getId() {
        return 4506;
    }
    
    public long getTackledFighterId() {
        return this.m_tackledFighterId;
    }
    
    public long getTacklerId() {
        return this.m_tacklerId;
    }
    
    @Override
    public int getActionId() {
        return 0;
    }
    
    @Override
    public FightActionType getFightActionType() {
        return FightActionType.TACKLE;
    }
}
