package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.fight.action;

import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.fight.*;
import com.ankamagames.framework.kernel.core.maths.*;
import java.nio.*;

public class FighterPositionMessage extends AbstractFightMessage
{
    private long m_fighterId;
    private Point3 m_position;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        if (!this.checkMessageSize(rawDatas.length, 22, false)) {
            return false;
        }
        final ByteBuffer bb = ByteBuffer.wrap(rawDatas);
        this.decodeFightHeader(bb);
        this.m_fighterId = bb.getLong();
        this.m_position = new Point3(bb.getInt(), bb.getInt(), bb.getShort());
        return true;
    }
    
    @Override
    public int getId() {
        return 4528;
    }
    
    public long getFighterId() {
        return this.m_fighterId;
    }
    
    public Point3 getPosition() {
        return this.m_position;
    }
}
