package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.fight.action;

import java.nio.*;
import com.ankamagames.wakfu.common.game.fight.*;

public class TableTurnBeginMessage extends AbstractFightActionMessage
{
    private short m_numTurns;
    private byte[] m_shortTimelineSerialize;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer buffer = ByteBuffer.wrap(rawDatas);
        this.decodeFightActionHeader(buffer);
        this.m_numTurns = buffer.getShort();
        if (buffer.remaining() > 0) {
            buffer.get(this.m_shortTimelineSerialize = new byte[buffer.getShort()]);
        }
        return true;
    }
    
    public byte[] getShortTimelineSerialize() {
        return this.m_shortTimelineSerialize;
    }
    
    @Override
    public int getId() {
        return 8100;
    }
    
    public short getNumTurns() {
        return this.m_numTurns;
    }
    
    @Override
    public int getActionId() {
        return 0;
    }
    
    @Override
    public FightActionType getFightActionType() {
        return FightActionType.NEW_TABLE_TURN;
    }
}
