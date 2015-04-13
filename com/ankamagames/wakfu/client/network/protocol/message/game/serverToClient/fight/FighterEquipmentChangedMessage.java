package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.fight;

import java.nio.*;

public class FighterEquipmentChangedMessage extends AbstractFightMessage
{
    long m_fighterId;
    int m_fighterInit;
    int m_fighterHp;
    int m_fighterMaxHp;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        if (rawDatas.length != 24) {
            return false;
        }
        final ByteBuffer bb = ByteBuffer.wrap(rawDatas);
        this.decodeFightHeader(bb);
        this.m_fighterId = bb.getLong();
        this.m_fighterInit = bb.getInt();
        this.m_fighterHp = bb.getInt();
        this.m_fighterMaxHp = bb.getInt();
        return true;
    }
    
    public int getFighterHp() {
        return this.m_fighterHp;
    }
    
    public long getFighterId() {
        return this.m_fighterId;
    }
    
    public int getFighterInit() {
        return this.m_fighterInit;
    }
    
    public int getFighterMaxHp() {
        return this.m_fighterMaxHp;
    }
    
    @Override
    public int getId() {
        return 8114;
    }
}
