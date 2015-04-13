package com.ankamagames.wakfu.common.game.nation.election;

import com.ankamagames.wakfu.common.game.nation.event.*;
import java.nio.*;
import gnu.trove.*;

public class NationForbiddenCandidateHandler
{
    private static final byte NUM_FORBIDDEN_ELECTIONS = 2;
    final TLongByteHashMap m_forbiddenCandidates;
    private final NationPoliticEventHandler m_handler;
    
    public NationForbiddenCandidateHandler(final NationPoliticEventHandler handler) {
        super();
        this.m_forbiddenCandidates = new TLongByteHashMap();
        this.m_handler = handler;
    }
    
    public void putCandidate(final long citizenId) {
        this.m_forbiddenCandidates.put(citizenId, (byte)2);
        this.fireChange();
    }
    
    public boolean isCandidateForbidden(final long citizenId) {
        return this.m_forbiddenCandidates.get(citizenId) != 0;
    }
    
    public void onGovernorElected() {
        final TLongByteIterator it = this.m_forbiddenCandidates.iterator();
        while (it.hasNext()) {
            it.advance();
            final byte score = it.value();
            if (score == 1) {
                it.remove();
            }
            it.setValue((byte)(score - 1));
        }
        this.fireChange();
    }
    
    private void fireChange() {
        this.m_handler.onForbiddenCandidatesChanged();
    }
    
    public void serialize(final ByteBuffer bb) {
        bb.putShort((short)this.m_forbiddenCandidates.size());
        final TLongByteIterator it = this.m_forbiddenCandidates.iterator();
        while (it.hasNext()) {
            it.advance();
            bb.putLong(it.key());
            bb.put(it.value());
        }
    }
    
    public void unserialize(final ByteBuffer bb) {
        this.m_forbiddenCandidates.clear();
        for (short i = 0, size = bb.getShort(); i < size; ++i) {
            this.m_forbiddenCandidates.put(bb.getLong(), bb.get());
        }
    }
    
    public int serializedSize() {
        return 2 + this.m_forbiddenCandidates.size() * 9;
    }
}
