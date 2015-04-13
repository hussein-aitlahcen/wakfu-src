package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.fight;

import gnu.trove.*;
import com.ankamagames.wakfu.common.game.fight.reconnection.*;
import java.util.*;
import java.nio.*;

public final class ReconnectionInFightMessage extends AbstractFightMessage
{
    private byte m_fightStatus;
    private int m_turnRemainingSeconds;
    private long m_serverSendTime;
    private byte[] m_serializedScoreGauges;
    private final THashSet<CarryInfoForReconnection> m_carriedInfos;
    private final List<byte[]> m_serializedEffectArea;
    private byte[] m_serializedTimelineEvents;
    private byte[] m_serializedFightersStates;
    
    public ReconnectionInFightMessage() {
        super();
        this.m_carriedInfos = new THashSet<CarryInfoForReconnection>();
        this.m_serializedEffectArea = new ArrayList<byte[]>();
    }
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer bb = ByteBuffer.wrap(rawDatas);
        this.decodeFightHeader(bb);
        this.m_fightStatus = bb.get();
        this.m_turnRemainingSeconds = bb.getInt();
        this.m_serverSendTime = bb.getLong();
        bb.get(this.m_serializedScoreGauges = new byte[bb.getInt()]);
        final byte carriedInfoSize = bb.get();
        for (int i = 0; i < carriedInfoSize; ++i) {
            this.m_carriedInfos.add(new CarryInfoForReconnection(bb.getLong(), bb.getLong(), bb.getInt()));
        }
        for (int effectAreaSize = bb.getInt(), j = 0; j < effectAreaSize; ++j) {
            final byte[] bytes = new byte[bb.getInt()];
            bb.get(bytes);
            this.m_serializedEffectArea.add(bytes);
        }
        final int eventsSerializedSize = bb.getInt();
        bb.get(this.m_serializedTimelineEvents = new byte[eventsSerializedSize]);
        final int fighterStatesSerializedSize = bb.getInt();
        bb.get(this.m_serializedFightersStates = new byte[fighterStatesSerializedSize]);
        return false;
    }
    
    public byte[] getSerializedScoreGauges() {
        return this.m_serializedScoreGauges;
    }
    
    public byte getFightStatus() {
        return this.m_fightStatus;
    }
    
    public int getTurnRemainingSeconds() {
        return this.m_turnRemainingSeconds;
    }
    
    public long getServerSendTime() {
        return this.m_serverSendTime;
    }
    
    public THashSet<CarryInfoForReconnection> getCarriedInfos() {
        return this.m_carriedInfos;
    }
    
    public List<byte[]> getSerializedEffectArea() {
        return this.m_serializedEffectArea;
    }
    
    public byte[] getSerializedTimelineEvents() {
        return this.m_serializedTimelineEvents;
    }
    
    public byte[] getSerializedFightersStates() {
        return this.m_serializedFightersStates;
    }
    
    @Override
    public int getId() {
        return 8042;
    }
}
