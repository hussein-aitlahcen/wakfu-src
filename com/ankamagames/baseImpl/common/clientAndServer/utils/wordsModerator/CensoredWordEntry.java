package com.ankamagames.baseImpl.common.clientAndServer.utils.wordsModerator;

import gnu.trove.*;
import java.util.*;

public class CensoredWordEntry
{
    private final int m_id;
    private final String m_text;
    private final THashSet<CensoredLanguageEnum> m_languageTypes;
    private final CensorTypeEnum m_censorTypeId;
    private final boolean m_deepSearch;
    
    public CensoredWordEntry(final int id, final String text, final CensoredLanguageEnum languageType, final CensorTypeEnum censorTypeId, final boolean deepSearch) {
        super();
        this.m_id = id;
        this.m_text = text;
        (this.m_languageTypes = new THashSet<CensoredLanguageEnum>()).add(languageType);
        this.m_censorTypeId = censorTypeId;
        this.m_deepSearch = deepSearch;
    }
    
    public CensoredWordEntry(final int id, final String text, final Collection<CensoredLanguageEnum> languageTypes, final CensorTypeEnum censorTypeId, final boolean deepSearch) {
        super();
        this.m_id = id;
        this.m_text = text;
        (this.m_languageTypes = new THashSet<CensoredLanguageEnum>()).addAll(languageTypes);
        this.m_censorTypeId = censorTypeId;
        this.m_deepSearch = deepSearch;
    }
    
    public int getId() {
        return this.m_id;
    }
    
    public String getText() {
        return this.m_text;
    }
    
    public boolean hasLanguage(final CensoredLanguageEnum lang) {
        return this.m_languageTypes.contains(lang);
    }
    
    public CensorTypeEnum getCensorType() {
        return this.m_censorTypeId;
    }
    
    public boolean isDeepSearch() {
        return this.m_deepSearch;
    }
}
