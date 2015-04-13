package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.fight.action;

import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.fight.*;
import java.nio.*;

public class FightChangeTimePointGapMessage extends AbstractFightMessage
{
    private int m_newGap;
    
    public FightChangeTimePointGapMessage() {
        super();
        this.m_newGap = -1;
    }
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer buffer = ByteBuffer.wrap(rawDatas);
        this.decodeFightHeader(buffer);
        this.m_newGap = buffer.getInt();
        return true;
    }
    
    @Override
    public int getId() {
        return 8014;
    }
    
    public int getNewGap() {
        return this.m_newGap;
    }
}
