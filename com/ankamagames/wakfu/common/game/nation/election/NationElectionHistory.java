package com.ankamagames.wakfu.common.game.nation.election;

import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.*;
import com.ankamagames.wakfu.common.game.nation.*;
import com.ankamagames.wakfu.common.constants.*;
import java.util.*;
import java.nio.*;
import gnu.trove.*;

public class NationElectionHistory
{
    private final GameDate m_electionDate;
    protected ArrayList<CandidateInfo> m_candidates;
    protected int m_ballotCount;
    
    public NationElectionHistory() {
        super();
        this.m_candidates = new ArrayList<CandidateInfo>();
        this.m_ballotCount = 0;
        this.m_electionDate = GameDate.fromLong(0L);
    }
    
    public int getNbCandidates() {
        return this.m_candidates.size();
    }
    
    public int getNbCandidateBallots() {
        return this.m_ballotCount;
    }
    
    public GameDate getElectionStartDate() {
        return this.m_electionDate;
    }
    
    public void getCandidates(final int offset, final int needed, final ArrayList<CandidateInfo> listToFill) {
        for (int max = offset + Math.min(needed, this.m_candidates.size() - offset), i = offset; i < max; ++i) {
            listToFill.add(this.m_candidates.get(i));
        }
    }
    
    public void buildHistory(final Nation nation) {
        this.m_electionDate.set(nation.getVoteStartDate().toLong());
        this.m_ballotCount = 0;
        final TLongObjectIterator<CandidateInfo> it = nation.getCandidateIterator();
        while (it.hasNext()) {
            it.advance();
            final CandidateInfo candidate = it.value();
            this.m_candidates.add(candidate);
            this.m_ballotCount += candidate.getBallotCount();
        }
        Collections.sort(this.m_candidates, NationConstants.CANDIDATES_ELECTION_COMPARATOR);
    }
    
    public void serialize(final ByteBuffer buffer) {
        buffer.putLong(this.m_electionDate.toLong());
        buffer.putInt(this.m_ballotCount);
        buffer.putShort((short)this.m_candidates.size());
        for (int i = 0; i < this.m_candidates.size(); ++i) {
            this.m_candidates.get(i).serialize(buffer);
        }
    }
    
    public void unserialize(final ByteBuffer buffer) {
        this.m_candidates.clear();
        this.m_electionDate.set(buffer.getLong());
        this.m_ballotCount = buffer.getInt();
        final short size = buffer.getShort();
        for (int i = 0; i < size; ++i) {
            this.m_candidates.add(CandidateInfo.fromBuild(buffer));
        }
    }
    
    public int serializedSize() {
        int size = 14;
        for (int i = 0; i < this.m_candidates.size(); ++i) {
            size += this.m_candidates.get(i).serializedSize();
        }
        return size;
    }
    
    public void clear() {
        this.m_electionDate.set(0L);
        this.m_candidates.clear();
        this.m_ballotCount = 0;
    }
}
