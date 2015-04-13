package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.scenario;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import gnu.trove.*;
import java.nio.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.*;

public class ChallengeInformationMessage extends InputOnlyProxyMessage
{
    private int m_currentChallengeId;
    private int m_timeBeforeCurrentChallenge;
    private int m_timeBeforeNextChallenge;
    private int m_entityCount;
    private GameDateConst m_startDate;
    private TIntArrayList m_currentChallengeActions;
    private byte m_state;
    private int m_protectorId;
    private boolean m_isRegisteredToChallenge;
    
    public ChallengeInformationMessage() {
        super();
        this.m_currentChallengeActions = new TIntArrayList();
    }
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer buff = ByteBuffer.wrap(rawDatas);
        this.m_state = buff.get();
        this.m_currentChallengeId = buff.getInt();
        this.m_timeBeforeCurrentChallenge = buff.getInt();
        this.m_timeBeforeNextChallenge = buff.getInt();
        this.m_startDate = GameDate.fromLong(buff.getLong());
        this.m_protectorId = buff.getInt();
        this.m_entityCount = buff.getInt();
        this.m_isRegisteredToChallenge = (buff.get() == 1);
        while (buff.hasRemaining()) {
            this.m_currentChallengeActions.add(buff.getInt());
        }
        return true;
    }
    
    @Override
    public int getId() {
        return 11214;
    }
    
    public TIntArrayList getCurrentChallengeActions() {
        return this.m_currentChallengeActions;
    }
    
    public int getCurrentChallengeId() {
        return this.m_currentChallengeId;
    }
    
    public int getTimeBeforeCurrentChallenge() {
        return this.m_timeBeforeCurrentChallenge;
    }
    
    public int getTimeBeforeNextChallenge() {
        return this.m_timeBeforeNextChallenge;
    }
    
    public GameDateConst getStartDate() {
        return this.m_startDate;
    }
    
    public byte getState() {
        return this.m_state;
    }
    
    public int getProtectorId() {
        return this.m_protectorId;
    }
    
    public int getEntityCount() {
        return this.m_entityCount;
    }
    
    public boolean isRegisteredToChallenge() {
        return this.m_isRegisteredToChallenge;
    }
}
