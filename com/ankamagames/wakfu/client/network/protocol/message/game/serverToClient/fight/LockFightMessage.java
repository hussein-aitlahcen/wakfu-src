package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.fight;

import com.ankamagames.wakfu.common.game.fight.lock.*;
import java.nio.*;

public class LockFightMessage extends AbstractFightMessage
{
    private FightLockModification m_lockModification;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer buff = ByteBuffer.wrap(rawDatas);
        this.decodeFightHeader(buff);
        this.m_lockModification = FightLockModification.deserialize(buff);
        return true;
    }
    
    @Override
    public int getId() {
        return 8158;
    }
    
    public FightLockModification getLockModification() {
        return this.m_lockModification;
    }
}
