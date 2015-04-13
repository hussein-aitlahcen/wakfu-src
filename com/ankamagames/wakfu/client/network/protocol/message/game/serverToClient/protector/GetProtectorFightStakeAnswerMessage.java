package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.protector;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public final class GetProtectorFightStakeAnswerMessage extends InputOnlyProxyMessage
{
    private int m_protectorId;
    private int m_fightStake;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer bb = ByteBuffer.wrap(rawDatas);
        this.m_protectorId = bb.getInt();
        this.m_fightStake = bb.getInt();
        return true;
    }
    
    @Override
    public int getId() {
        return 15317;
    }
    
    public int getFightStake() {
        return this.m_fightStake;
    }
    
    public int getProtectorId() {
        return this.m_protectorId;
    }
}
