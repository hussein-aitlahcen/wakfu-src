package com.ankamagames.wakfu.client.core.game.challenge;

import com.ankamagames.framework.external.*;
import org.apache.log4j.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.framework.fileFormat.properties.*;

public enum ChallengeCategory implements ExportableEnum
{
    ENVIRONMENT((byte)0), 
    ECOSYSTEM((byte)1), 
    CHAOS((byte)2), 
    NOT_A_CHALLENGE((byte)4);
    
    private static final Logger m_logger;
    private final byte m_id;
    
    public static ChallengeCategory fromId(final byte id) {
        for (final ChallengeCategory type : values()) {
            if (type.m_id == id) {
                return type;
            }
        }
        return null;
    }
    
    private ChallengeCategory(final byte id) {
        this.m_id = id;
    }
    
    public byte getId() {
        return this.m_id;
    }
    
    @Override
    public String getEnumId() {
        return Byte.toString(this.m_id);
    }
    
    @Override
    public String getEnumLabel() {
        return this.toString();
    }
    
    public String getTranslatedString() {
        return WakfuTranslator.getInstance().getString("challenge.type." + this.ordinal());
    }
    
    public String getIconURL() {
        try {
            return String.format(WakfuConfiguration.getInstance().getString("challengeCategoryIconsPath"), this.getId());
        }
        catch (PropertyException e) {
            ChallengeCategory.m_logger.error((Object)"Exception", (Throwable)e);
            return null;
        }
    }
    
    @Override
    public String getEnumComment() {
        return null;
    }
    
    static {
        m_logger = Logger.getLogger((Class)ChallengeCategory.class);
    }
}
