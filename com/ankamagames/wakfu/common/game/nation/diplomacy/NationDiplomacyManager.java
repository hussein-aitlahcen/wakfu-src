package com.ankamagames.wakfu.common.game.nation.diplomacy;

import org.apache.log4j.*;
import com.ankamagames.wakfu.common.game.nation.crime.constants.*;
import com.ankamagames.wakfu.common.game.nation.*;
import com.ankamagames.wakfu.common.rawData.*;
import gnu.trove.*;

public class NationDiplomacyManager
{
    protected static final Logger m_logger;
    protected final Nation m_nation;
    protected NationDiplomacyListener m_listener;
    private final TIntObjectHashMap<SharedAlignment> m_alignments;
    private final TIntObjectHashMap<NationAlignement> m_alignmentRequests;
    
    public NationDiplomacyManager(final Nation nation) {
        super();
        this.m_alignments = new TIntObjectHashMap<SharedAlignment>();
        this.m_alignmentRequests = new TIntObjectHashMap<NationAlignement>();
        this.m_nation = nation;
    }
    
    public void setListener(final NationDiplomacyListener listener) {
        this.m_listener = listener;
    }
    
    protected void setRequest(final int targetNationId, final NationAlignement alignment) {
        this.m_alignmentRequests.put(targetNationId, alignment);
    }
    
    protected void removeRequest(final int targetNationId) {
        this.m_alignmentRequests.remove(targetNationId);
    }
    
    public void removeRequests() {
        this.m_alignmentRequests.clear();
    }
    
    public NationAlignement getRequest(final int targetNationId) {
        return this.m_alignmentRequests.get(targetNationId);
    }
    
    public NationAlignement getAlignment(final int nationId) {
        return this.getSharedAlignment(nationId).alignment;
    }
    
    protected void setAlignment(final int targetNationId, final NationAlignement alignment) {
        this.getSharedAlignment(targetNationId).alignment = alignment;
    }
    
    private SharedAlignment getSharedAlignment(final int nationId) {
        final Nation targetNation = NationManager.INSTANCE.getNationById(nationId);
        SharedAlignment share = this.m_alignments.get(nationId);
        if (share != null) {
            return share;
        }
        if (targetNation != null) {
            this.m_alignments.put(nationId, share = targetNation.getDiplomacyManager().m_alignments.get(this.m_nation.getNationId()));
        }
        if (share != null) {
            return share;
        }
        this.m_alignments.put(nationId, share = new SharedAlignment((this.m_nation.getNationId() != nationId && nationId != 0 && this.m_nation.getNationId() != 0) ? NationAlignement.ENEMY : NationAlignement.ALLIED));
        return share;
    }
    
    public void clear() {
        this.m_alignments.clear();
        this.m_alignmentRequests.clear();
    }
    
    private void notifyAlignmentDataChanged() {
        if (this.m_listener != null) {
            this.m_listener.onAlignmentDataChanged();
        }
    }
    
    public void toRaw(final RawNationDiplomacy raw) {
        final TIntObjectIterator<SharedAlignment> it = this.m_alignments.iterator();
        while (it.hasNext()) {
            it.advance();
            final RawNationDiplomacy.Alignment rawAlignment = new RawNationDiplomacy.Alignment();
            rawAlignment.nationId = it.key();
            rawAlignment.alignment = it.value().alignment.getId();
            raw.alignments.add(rawAlignment);
        }
        final TIntObjectIterator<NationAlignement> it2 = this.m_alignmentRequests.iterator();
        while (it2.hasNext()) {
            it2.advance();
            final RawNationDiplomacy.Request rawRequest = new RawNationDiplomacy.Request();
            rawRequest.nationId = it2.key();
            rawRequest.alignment = it2.value().getId();
            raw.alignmentRequests.add(rawRequest);
        }
    }
    
    public void fromRaw(final RawNationDiplomacy raw) {
        for (int i = 0, size = raw.alignments.size(); i < size; ++i) {
            final RawNationDiplomacy.Alignment rawAlignement = raw.alignments.get(i);
            this.setAlignment(rawAlignement.nationId, NationAlignement.getFromId(rawAlignement.alignment));
        }
        for (int i = 0, size = raw.alignmentRequests.size(); i < size; ++i) {
            final RawNationDiplomacy.Request rawRequest = raw.alignmentRequests.get(i);
            this.m_alignmentRequests.put(rawRequest.nationId, NationAlignement.getFromId(rawRequest.alignment));
        }
        this.notifyAlignmentDataChanged();
    }
    
    static {
        m_logger = Logger.getLogger((Class)NationDiplomacyManager.class);
    }
    
    private static class SharedAlignment
    {
        public NationAlignement alignment;
        
        private SharedAlignment(final NationAlignement alignement) {
            super();
            this.alignment = alignement;
        }
        
        @Override
        public String toString() {
            return "SharedAlignment{alignment=" + this.alignment + '}';
        }
    }
}
