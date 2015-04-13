package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.fight;

import com.ankamagames.wakfu.common.game.fight.*;
import java.util.*;
import com.ankamagames.framework.kernel.core.common.collections.*;
import java.nio.*;

public class ExternalFightCreationMessage extends AbstractFightMessage
{
    private byte m_fightType;
    private long[] m_fightersId;
    private byte[] m_teamsId;
    private long m_battlegroundBorderEffectArea;
    private long m_battlegroundBorderEffectAreaUID;
    private AbstractFight.FightStatus m_fightStatus;
    private ArrayList<Long> m_creators;
    private ArrayList<ObjectPair<Short, Short>> m_partition;
    private byte[] m_serializedFightMap;
    
    public ExternalFightCreationMessage() {
        super();
        this.m_creators = new ArrayList<Long>();
        this.m_partition = new ArrayList<ObjectPair<Short, Short>>();
    }
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer buffer = ByteBuffer.wrap(rawDatas);
        this.decodeFightHeader(buffer);
        this.m_fightType = buffer.get();
        this.m_battlegroundBorderEffectArea = buffer.getLong();
        this.m_battlegroundBorderEffectAreaUID = buffer.getLong();
        final byte nbLeaders = buffer.get();
        for (int i = 0; i < nbLeaders; ++i) {
            this.m_creators.add(buffer.getLong());
        }
        final int nbFighters = buffer.get();
        this.m_fightersId = new long[nbFighters];
        this.m_teamsId = new byte[nbFighters];
        for (int j = 0; j < nbFighters; ++j) {
            this.m_fightersId[j] = buffer.getLong();
            this.m_teamsId[j] = buffer.get();
        }
        final byte status = buffer.get();
        this.m_fightStatus = AbstractFight.FightStatus.getStatusFromId(status);
        for (byte k = (byte)(buffer.get() - 1); k >= 0; --k) {
            final short x = buffer.getShort();
            final short y = buffer.getShort();
            this.m_partition.add(new ObjectPair<Short, Short>(x, y));
        }
        buffer.get(this.m_serializedFightMap = new byte[buffer.remaining()]);
        return true;
    }
    
    public int getFightersCount() {
        return this.m_fightersId.length;
    }
    
    public long getFighterId(final int fighterIndex) {
        return this.m_fightersId[fighterIndex];
    }
    
    public byte getTeamId(final int fighterIndex) {
        return this.m_teamsId[fighterIndex];
    }
    
    @Override
    public int getId() {
        return 8006;
    }
    
    public byte getFightType() {
        return this.m_fightType;
    }
    
    public long getBattlegroundBorderEffectAreaBaseId() {
        return this.m_battlegroundBorderEffectArea;
    }
    
    public long getBattlegroundBorderEffectAreaUID() {
        return this.m_battlegroundBorderEffectAreaUID;
    }
    
    public byte[] getSerializedFightMap() {
        return this.m_serializedFightMap;
    }
    
    public AbstractFight.FightStatus getFightStatus() {
        return this.m_fightStatus;
    }
    
    public ArrayList<Long> getCreators() {
        return this.m_creators;
    }
    
    public ArrayList<ObjectPair<Short, Short>> getPartition() {
        return this.m_partition;
    }
}
