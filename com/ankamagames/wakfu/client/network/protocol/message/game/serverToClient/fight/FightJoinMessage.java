package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.fight;

import java.nio.*;

public class FightJoinMessage extends AbstractFightMessage
{
    private byte[] m_serializedFighterDatas;
    private byte[] m_serializedEffectUserDatas;
    private long m_fighterId;
    private byte m_teamId;
    private byte m_fighterType;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer buffer = ByteBuffer.wrap(rawDatas);
        this.decodeFightHeader(buffer);
        this.m_fighterId = buffer.getLong();
        this.m_fighterType = buffer.get();
        this.m_teamId = buffer.get();
        buffer.get(this.m_serializedFighterDatas = new byte[buffer.getShort()]);
        buffer.get(this.m_serializedEffectUserDatas = new byte[buffer.getShort()]);
        return true;
    }
    
    public byte[] getSerializedFighterDatas() {
        return this.m_serializedFighterDatas;
    }
    
    public byte[] getSerializedEffectUserDatas() {
        return this.m_serializedEffectUserDatas;
    }
    
    public long getFighterId() {
        return this.m_fighterId;
    }
    
    public byte getTeamId() {
        return this.m_teamId;
    }
    
    public byte getFighterType() {
        return this.m_fighterType;
    }
    
    @Override
    public int getId() {
        return 8002;
    }
}
