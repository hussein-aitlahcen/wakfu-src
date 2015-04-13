package com.ankamagames.baseImpl.common.clientAndServer.utils.wordsModerator;

import com.ankamagames.framework.external.*;
import com.ankamagames.framework.kernel.core.translator.*;

public enum CensoredLanguageEnum implements ExportableEnum
{
    ALL((short)0, "Toutes", (Language)null), 
    FRENCH((short)1, "Fran\u00e7ais", Language.FR), 
    ENGLISH((short)2, "Anglais", Language.EN), 
    DEUTCH((short)3, "Allemand", Language.DE), 
    ESPAGNOL((short)4, "Espagnol", Language.ES), 
    ITALIAN((short)5, "Italien", Language.IT), 
    PORTUGUESE((short)6, "Portugais", Language.PT), 
    CHINESE((short)7, "Chinois", Language.CH, true), 
    TAGALOG((short)8, "Tagalog", Language.TL), 
    MALAY((short)9, "Malay", Language.MS), 
    THAI((short)10, "Thai", Language.THA), 
    TAIWAN((short)11, "Taiwan", Language.TW);
    
    private final short m_id;
    private final String m_label;
    private final Language m_language;
    private final boolean m_spaceLess;
    
    private CensoredLanguageEnum(final short id, final String label, final Language language) {
        this(id, label, language, false);
    }
    
    private CensoredLanguageEnum(final short id, final String label, final Language language, final boolean spaceless) {
        this.m_id = id;
        this.m_label = label;
        this.m_language = language;
        this.m_spaceLess = spaceless;
    }
    
    public short getId() {
        return this.m_id;
    }
    
    public boolean isSpaceLess() {
        return this.m_spaceLess;
    }
    
    @Override
    public String getEnumId() {
        return Integer.toString(this.m_id);
    }
    
    @Override
    public String getEnumLabel() {
        return this.m_label;
    }
    
    @Override
    public String getEnumComment() {
        return null;
    }
    
    public static CensoredLanguageEnum getById(final short id) {
        for (final CensoredLanguageEnum b : values()) {
            if (b.m_id == id) {
                return b;
            }
        }
        return null;
    }
    
    public static CensoredLanguageEnum getByLanguage(final Language language) {
        for (final CensoredLanguageEnum b : values()) {
            if (b.m_language == language) {
                return b;
            }
        }
        return CensoredLanguageEnum.ALL;
    }
}
