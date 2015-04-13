package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.fight;

import java.nio.*;

public class FightCreationForSpectatorMessage extends FightCreationMessage
{
    private byte m_fightStatus;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer bb = ByteBuffer.wrap(rawDatas);
        this.decodeFightCreation(bb);
        this.m_fightStatus = bb.get();
        return true;
    }
    
    @Override
    public int getId() {
        return 8038;
    }
    
    public byte getFightStatus() {
        return this.m_fightStatus;
    }
}
