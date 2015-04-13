package com.ankamagames.wakfu.common.game.group.partySearch;

import com.ankamagames.framework.kernel.core.maths.*;

public enum PartySearchFeedbackEnum
{
    NO_ERROR(0, false), 
    REMOVED_FOR_FULL_GROUP(1, true), 
    REMOVED_FOR_VOID_OCCUPATION(2, true), 
    REGISTRATION_OK(3, false), 
    UNREGISTER_OK(4, false), 
    REMOVED_FOR_INVALID_OCCUPATION(5, true), 
    YOU_ARE_NOT_PARTY_LEADER(6, true), 
    INVALID_ROLE(7, true), 
    TOO_LONG_DESC(8, true), 
    BAD_LEVEL(9, true), 
    UPDATE_OK(10, false), 
    BAD_USER_TRY_INVITE(11, true), 
    FLOOD(12, true), 
    TOO_MUCH_OCCUPATIONS(13, true);
    
    private final byte m_id;
    private final boolean m_error;
    
    private PartySearchFeedbackEnum(final int i, final boolean isError) {
        this.m_id = MathHelper.ensureByte(i);
        this.m_error = isError;
    }
    
    public byte getId() {
        return this.m_id;
    }
    
    public static PartySearchFeedbackEnum getFromId(final byte id) {
        for (final PartySearchFeedbackEnum feedback : values()) {
            if (id == feedback.m_id) {
                return feedback;
            }
        }
        return PartySearchFeedbackEnum.NO_ERROR;
    }
    
    public boolean isError() {
        return this.m_error;
    }
}
