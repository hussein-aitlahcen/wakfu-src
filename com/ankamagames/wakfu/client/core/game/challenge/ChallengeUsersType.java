package com.ankamagames.wakfu.client.core.game.challenge;

import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.client.core.*;

public enum ChallengeUsersType implements ExportableEnum
{
    NONE((byte)0, "Choisissez un type de challenge !", false), 
    SOLO((byte)1, "Solo", false), 
    RACE((byte)2, "Course", false), 
    COMPETITIVE((byte)3, "Comp\u00e9titif", true), 
    COLLABORATIVE((byte)4, "Collaboratif", false);
    
    private final byte m_id;
    private final String m_description;
    private final boolean m_displayRanking;
    
    private ChallengeUsersType(final byte id, final String description, final boolean displayRanking) {
        this.m_id = id;
        this.m_description = description;
        this.m_displayRanking = displayRanking;
    }
    
    @Override
    public String getEnumId() {
        return String.valueOf(this.m_id);
    }
    
    @Override
    public String getEnumLabel() {
        return this.m_description;
    }
    
    @Override
    public String getEnumComment() {
        return null;
    }
    
    public static ChallengeUsersType fromId(final byte id) {
        for (final ChallengeUsersType value : values()) {
            if (value.m_id == id) {
                return value;
            }
        }
        return null;
    }
    
    public String getTranslatedName() {
        return WakfuTranslator.getInstance().getString("challenge.userType." + this.ordinal());
    }
    
    public byte getId() {
        return this.m_id;
    }
    
    public boolean isDisplayRanking() {
        return this.m_displayRanking;
    }
}
