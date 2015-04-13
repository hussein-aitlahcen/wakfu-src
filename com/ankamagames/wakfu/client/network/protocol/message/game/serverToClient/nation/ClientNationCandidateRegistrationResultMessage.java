package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.nation;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.wakfu.common.game.nation.election.*;
import java.nio.*;

public class ClientNationCandidateRegistrationResultMessage extends InputOnlyProxyMessage
{
    private CandidateInfo m_candidate;
    private int m_nbCandidates;
    private int m_nbBallots;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer buffer = ByteBuffer.wrap(rawDatas);
        this.m_candidate = CandidateInfo.fromBuild(buffer);
        this.m_nbCandidates = buffer.getInt();
        this.m_nbBallots = buffer.getInt();
        return true;
    }
    
    public CandidateInfo getCandidate() {
        return this.m_candidate;
    }
    
    public int getNbCandidates() {
        return this.m_nbCandidates;
    }
    
    public int getNbBallots() {
        return this.m_nbBallots;
    }
    
    @Override
    public int getId() {
        return 15122;
    }
}
