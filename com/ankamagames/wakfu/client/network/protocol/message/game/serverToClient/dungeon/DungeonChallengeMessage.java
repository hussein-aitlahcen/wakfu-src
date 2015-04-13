package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.dungeon;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.wakfu.common.game.dungeon.challenge.*;
import java.nio.*;

public class DungeonChallengeMessage extends InputOnlyProxyMessage
{
    private int m_challengeId;
    private ChallengeStatus m_status;
    
    @Override
    public boolean decode(final byte... rawDatas) {
        final ByteBuffer bb = ByteBuffer.wrap(rawDatas);
        this.m_challengeId = bb.getInt();
        this.m_status = ChallengeStatus.values()[bb.getInt()];
        return true;
    }
    
    public int getChallengeId() {
        return this.m_challengeId;
    }
    
    public ChallengeStatus getStatus() {
        return this.m_status;
    }
    
    @Override
    public int getId() {
        return 15960;
    }
}
