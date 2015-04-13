package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.fight.action;

import com.ankamagames.wakfu.client.core.game.fight.history.*;
import com.ankamagames.wakfu.common.game.fight.*;
import java.nio.*;

public class FightReportMessage extends AbstractFightActionMessage
{
    private FightHistoryReader m_history;
    
    @Override
    public int getActionId() {
        return 8304;
    }
    
    @Override
    public FightActionType getFightActionType() {
        return FightActionType.FIGHT_REPORT;
    }
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer buffer = ByteBuffer.wrap(rawDatas);
        this.decodeFightActionHeader(buffer);
        this.m_history = FightHistoryReader.deserialize(buffer);
        return true;
    }
    
    @Override
    public int getId() {
        return 8304;
    }
    
    public FightHistoryReader getHistory() {
        return this.m_history;
    }
}
