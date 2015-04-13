package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.fight;

import gnu.trove.*;
import java.util.*;
import java.nio.*;

public class FightCreationMessage extends AbstractFightMessage
{
    private byte m_fightType;
    private int m_worldId;
    private long[] m_fightersId;
    private byte[] m_fightersType;
    private byte[] m_teamsId;
    private byte[] m_playState;
    private byte[] m_serializedMap;
    private byte[][] m_serializedFightersDatas;
    private byte[][] m_serializedEffectUsersDatas;
    private byte[] m_timelineSerialized;
    private long m_battlegroundBorderEffectArea;
    private long m_battlegroundBorderEffectAreaUID;
    private EffectUserID[] m_additionnalEffectUsers;
    private long m_attackerId;
    private long m_defenderId;
    private final List<byte[]> m_serializedEffectArea;
    private boolean m_forReconnection;
    private TByteHashSet m_lockedTeams;
    
    public FightCreationMessage() {
        super();
        this.m_serializedEffectArea = new ArrayList<byte[]>();
        this.m_lockedTeams = new TByteHashSet();
    }
    
    protected void decodeFightCreation(final ByteBuffer bb) {
        this.decodeFightHeader(bb);
        this.m_fightType = bb.get();
        this.m_worldId = bb.getInt();
        this.m_battlegroundBorderEffectArea = bb.getLong();
        this.m_battlegroundBorderEffectAreaUID = bb.getLong();
        final int serializedMapSize = bb.getInt();
        bb.get(this.m_serializedMap = new byte[serializedMapSize]);
        bb.get(this.m_timelineSerialized = new byte[bb.getShort()]);
        this.m_attackerId = bb.getLong();
        this.m_defenderId = bb.getLong();
        this.m_forReconnection = (bb.get() == 1);
        final int nbFighters = bb.get();
        this.m_fightersId = new long[nbFighters];
        this.m_fightersType = new byte[nbFighters];
        this.m_teamsId = new byte[nbFighters];
        this.m_playState = new byte[nbFighters];
        this.m_serializedFightersDatas = new byte[nbFighters][];
        this.m_serializedEffectUsersDatas = new byte[nbFighters][];
        for (int i = 0; i < nbFighters; ++i) {
            this.m_fightersId[i] = bb.getLong();
            this.m_fightersType[i] = bb.get();
            this.m_teamsId[i] = bb.get();
            this.m_playState[i] = bb.get();
            if (!this.m_forReconnection) {
                bb.get(this.m_serializedFightersDatas[i] = new byte[bb.getShort()]);
                bb.get(this.m_serializedEffectUsersDatas[i] = new byte[bb.getShort()]);
            }
        }
        final int effectUserCount = bb.get();
        this.m_additionnalEffectUsers = new EffectUserID[effectUserCount];
        for (int j = 0; j < effectUserCount; ++j) {
            final byte typeId = bb.get();
            final long id = bb.getLong();
            this.m_additionnalEffectUsers[j] = new EffectUserID(typeId, id);
        }
        final byte nbTeams = bb.get();
        for (int k = 0; k < nbTeams; ++k) {
            this.m_lockedTeams.add(bb.get());
        }
        for (int effectAreaSize = bb.getInt(), l = 0; l < effectAreaSize; ++l) {
            final byte[] bytes = new byte[bb.getInt()];
            bb.get(bytes);
            this.m_serializedEffectArea.add(bytes);
        }
    }
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer bb = ByteBuffer.wrap(rawDatas);
        this.decodeFightCreation(bb);
        return true;
    }
    
    public int getFightersCount() {
        return this.m_fightersId.length;
    }
    
    public long getFighterId(final int fighterIndex) {
        return this.m_fightersId[fighterIndex];
    }
    
    public long[] getFightersId() {
        return this.m_fightersId;
    }
    
    public byte getFighterType(final int fighterIndex) {
        return this.m_fightersType[fighterIndex];
    }
    
    public byte getTeamId(final int fighterIndex) {
        return this.m_teamsId[fighterIndex];
    }
    
    public byte getPlayStateId(final int fighterIndex) {
        return this.m_playState[fighterIndex];
    }
    
    public byte[] getSerializedFighterDatas(final int fighterIndex) {
        return this.m_serializedFightersDatas[fighterIndex];
    }
    
    public byte[] getSerializedEffectUsersDatas(final int fighterIndex) {
        return this.m_serializedEffectUsersDatas[fighterIndex];
    }
    
    public byte[] getTimelineSerialized() {
        return this.m_timelineSerialized;
    }
    
    @Override
    public int getId() {
        return 8000;
    }
    
    public byte[] getSerializedMap() {
        return this.m_serializedMap;
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
    
    public long getAttackerId() {
        return this.m_attackerId;
    }
    
    public long getDefenderId() {
        return this.m_defenderId;
    }
    
    public EffectUserID[] getAdditionnalEffectUsers() {
        return this.m_additionnalEffectUsers;
    }
    
    public TByteHashSet getLockedTeams() {
        return this.m_lockedTeams;
    }
    
    public List<byte[]> getSerializedEffectArea() {
        return this.m_serializedEffectArea;
    }
    
    public boolean isForReconnection() {
        return this.m_forReconnection;
    }
    
    public static class EffectUserID
    {
        public final byte m_typeId;
        public final long m_id;
        
        private EffectUserID(final byte typeId, final long id) {
            super();
            this.m_typeId = typeId;
            this.m_id = id;
        }
    }
}
