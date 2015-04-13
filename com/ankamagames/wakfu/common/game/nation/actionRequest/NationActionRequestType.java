package com.ankamagames.wakfu.common.game.nation.actionRequest;

import org.apache.log4j.*;
import com.ankamagames.wakfu.common.game.nation.actionRequest.impl.*;

public enum NationActionRequestType
{
    PROTECTOR_BUFFS_ADD(NationProtectorBuffsAddRequest.FACTORY), 
    PROTECTOR_BUFFS_REMOVE(NationProtectorBuffsRemoveRequest.FACTORY), 
    PROTECTOR_BUFFS_CLEAR(NationProtectorBuffsClearRequest.FACTORY), 
    PROTECTOR_BUFFS_REPLACE(NationProtectorBuffsReplaceRequest.FACTORY), 
    MEMBER_ADD_REQUEST(NationMemberAddRequest.FACTORY), 
    CANDIDATE_REGISTRATION_REQUEST(NationCandidateRegistrationRequest.FACTORY), 
    CANDIDATE_VOTE_REQUEST(NationCandidateVoteRequest.FACTORY), 
    GOVERNMENT_NOMINATION_CONFIRMATION_RESULT(NationGovernmentNominationConfirmationResult.FACTORY), 
    GOVERNMENT_REVOKE_REQUEST(NationGovernmentRevokeRequest.FACTORY), 
    MEMBER_ADD_NATION_JOB(NationMemberManageNationJobRequest.FACTORY), 
    CRIMINAL_VIP_REQUEST(NationCriminalVipRequest.FACTORY), 
    REVALIDATE_CANDIDATE_REQUEST(NationRevalidateCandidateRequest.FACTORY);
    
    private static final Logger m_logger;
    private final NationActionRequestFactory m_requestFactory;
    
    private NationActionRequestType(final NationActionRequestFactory requestFactory) {
        this.m_requestFactory = requestFactory;
    }
    
    public static NationActionRequest createRequestFromOrdinal(final byte ordinal) {
        final NationActionRequestType[] actionRequestTypes = values();
        if (ordinal < 0 || ordinal > actionRequestTypes.length) {
            NationActionRequestType.m_logger.error((Object)("Impossible de cr\u00e9er une requ\u00eate \u00e0 partir de son ordinal : rodinal en dehors des limites : " + values().length + " max, " + ordinal + " demand\u00e9"));
            return null;
        }
        final NationActionRequestType type = values()[ordinal];
        if (type.m_requestFactory != null) {
            return type.m_requestFactory.createNew();
        }
        return null;
    }
    
    static {
        m_logger = Logger.getLogger((Class)NationActionRequestType.class);
    }
}
