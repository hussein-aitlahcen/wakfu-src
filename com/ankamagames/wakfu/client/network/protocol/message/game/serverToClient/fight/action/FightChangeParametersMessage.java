package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.fight.action;

import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.fight.*;
import java.nio.*;

public class FightChangeParametersMessage extends AbstractFightMessage
{
    private boolean m_changeTurnDuration;
    private int m_turnDuration;
    
    public FightChangeParametersMessage() {
        super();
        this.m_changeTurnDuration = false;
        this.m_turnDuration = -1;
    }
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer buffer = ByteBuffer.wrap(rawDatas);
        this.decodeFightHeader(buffer);
        this.m_changeTurnDuration = (buffer.get() == 1);
        if (this.m_changeTurnDuration) {
            this.m_turnDuration = buffer.getInt();
        }
        return true;
    }
    
    @Override
    public int getId() {
        return 8012;
    }
    
    public boolean mustChangeTurnDuration() {
        return this.m_changeTurnDuration;
    }
    
    public int getTurnDuration() {
        return this.m_turnDuration;
    }
}
