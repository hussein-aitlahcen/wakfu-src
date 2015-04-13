package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.nation;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.util.*;
import com.ankamagames.wakfu.common.game.nation.election.*;
import java.nio.*;

public class NationVoteInformationResultMessage extends InputOnlyProxyMessage
{
    private ArrayList<CandidateInfo> m_candidate;
    private int m_nbCandidates;
    private int m_nbBallots;
    
    public NationVoteInformationResultMessage() {
        super();
        this.m_candidate = new ArrayList<CandidateInfo>();
    }
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer buffer = ByteBuffer.wrap(rawDatas);
        this.m_nbCandidates = buffer.getInt();
        this.m_nbBallots = buffer.getInt();
        while (buffer.hasRemaining()) {
            this.m_candidate.add(CandidateInfo.fromBuild(buffer));
        }
        return true;
    }
    
    public ArrayList<CandidateInfo> getCandidates() {
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
        return 20012;
    }
}
