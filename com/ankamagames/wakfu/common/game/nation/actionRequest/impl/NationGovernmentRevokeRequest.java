package com.ankamagames.wakfu.common.game.nation.actionRequest.impl;

import org.apache.log4j.*;
import com.ankamagames.wakfu.common.game.nation.actionRequest.*;
import com.ankamagames.wakfu.common.game.nation.*;
import com.ankamagames.wakfu.common.game.nation.government.*;
import java.nio.*;

public class NationGovernmentRevokeRequest extends NationActionRequest
{
    private static final Logger m_logger;
    public static final NationActionRequestFactory FACTORY;
    private long m_requesterRankId;
    private long m_requestedRankId;
    private RevokeReason m_reason;
    
    public NationGovernmentRevokeRequest() {
        super(NationActionRequestType.GOVERNMENT_REVOKE_REQUEST);
    }
    
    public void setRequesterRankId(final long requesterRankId) {
        this.m_requesterRankId = requesterRankId;
    }
    
    public void setRequestedRankId(final long requestedRankId) {
        this.m_requestedRankId = requestedRankId;
    }
    
    public void setReason(final RevokeReason reason) {
        this.m_reason = reason;
    }
    
    @Override
    public void execute() {
        final Nation nation = this.getConcernedNation();
        if (nation == null) {
            NationGovernmentRevokeRequest.m_logger.error((Object)("Impossible d'ex\u00e9cuter l'action " + this + " : la nation " + this.m_nationId + " n'existe pas"));
            return;
        }
        nation.requestGovernmentRevoke(NationRank.getById(this.m_requesterRankId), NationRank.getById(this.m_requestedRankId), this.m_reason);
    }
    
    @Override
    public boolean authorizedFromClient(final Citizen citizen) {
        final Nation nation = citizen.getCitizenComportment().getNation();
        if (nation.getNationId() != this.m_nationId) {
            return false;
        }
        final GovernmentInfo member = nation.getGovernment().getMember(citizen.getId());
        return member != null && member.getRank().getId() == this.m_requesterRankId && NationRankRightChecker.hasRankToRevoke(member.getRank(), NationRank.getById(this.m_requesterRankId));
    }
    
    @Override
    public boolean serialize(final ByteBuffer buffer) {
        buffer.putLong(this.m_requesterRankId);
        buffer.putLong(this.m_requestedRankId);
        buffer.put(this.m_reason.getId());
        return true;
    }
    
    @Override
    public boolean unserialize(final ByteBuffer buffer) {
        this.m_requesterRankId = buffer.getLong();
        this.m_requestedRankId = buffer.getLong();
        this.m_reason = RevokeReason.byId(buffer.get());
        return true;
    }
    
    @Override
    public int serializedSize() {
        return 17;
    }
    
    @Override
    public void clear() {
        this.m_nationId = -1;
        this.m_requesterRankId = -1L;
        this.m_requestedRankId = -1L;
    }
    
    static {
        m_logger = Logger.getLogger((Class)NationGovernmentRevokeRequest.class);
        FACTORY = new NationActionRequestFactory() {
            @Override
            public NationActionRequest createNew() {
                return new NationGovernmentRevokeRequest();
            }
        };
    }
}
