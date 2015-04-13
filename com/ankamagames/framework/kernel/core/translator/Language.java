package com.ankamagames.framework.kernel.core.translator;

import org.apache.log4j.*;
import java.util.*;
import com.ankamagames.framework.kernel.core.common.collections.lightweight.*;

public enum Language
{
    FR("fr", "Fran\u00e7ais", "fr", true, true, true), 
    EN("en", "Anglais", "en", true, true, true), 
    DE("de", "Allemand", "de", true, true, true), 
    ES("es", "Espagnol", "es", true, true, true), 
    IT("it", "Italien", "it", true, true, true), 
    NL("nl", "Neerlandais", "nl", true, false, true), 
    JP("jp", "Japonais", "ja", false, true, false), 
    JA("ja", "Japonais", "ja", false, false, false), 
    RU("ru", "Russe", "ru", true, false, true), 
    PT("pt", "Portugais", "pt", true, true, true), 
    KOR("ko", "Coreen", "ko", false, true, false), 
    THA("th", "Thailandais", "th", false, true, true), 
    VIE("vi", "Vietnamien", "vi", false, true, true), 
    MS("ms", "Malay", "ms", true, false, true), 
    TL("tl", "Tagalog", "tl", true, false, true), 
    CH("ch", "Chinois", "zh", false, true, false), 
    ZH("zh", "Chinois", "zh", false, false, false), 
    TW("tw", "Chinois Traditionnel", "zh-TW", false, true, false), 
    ZH_TW("zh-TW", "Chinois Traditionnel", "zh-TW", false, false, false);
    
    private static final Logger m_logger;
    private final Locale m_locale;
    private final Locale m_actualLocale;
    private final String m_code;
    private final String m_name;
    private final boolean m_usesStandardCharacters;
    private final boolean m_translated;
    private final boolean m_separatesWords;
    private static final LightWeightMap<String, Language> LANGS_BY_CODE;
    
    private Language(final String code, final String name, final String langCode, final boolean usesStandardCharacters, final boolean translated, final boolean separatesWords) {
        this.m_locale = new Locale(code);
        this.m_actualLocale = new Locale(langCode);
        this.m_code = code;
        this.m_name = name;
        this.m_usesStandardCharacters = usesStandardCharacters;
        this.m_translated = translated;
        this.m_separatesWords = separatesWords;
    }
    
    public Locale getLocale() {
        return this.m_locale;
    }
    
    public Locale getActualLocale() {
        return this.m_actualLocale;
    }
    
    public static Language getLanguage(final String languageCode) {
        return getLanguage(languageCode, false);
    }
    
    public static Language getLanguage(final String languageCode, final boolean enableNulls) {
        if (languageCode == null) {
            return Language.EN;
        }
        final Language l = Language.LANGS_BY_CODE.get(languageCode.toLowerCase());
        if (l != null || enableNulls) {
            return l;
        }
        Language.m_logger.warn((Object)("code inconnu " + languageCode));
        return Language.EN;
    }
    
    public static boolean containsLanguage(final Language[] languages, final Language language) {
        if (languages == null || language == null) {
            return false;
        }
        for (final Language language2 : languages) {
            if (language2 == language) {
                return true;
            }
        }
        return false;
    }
    
    public String getName() {
        return this.m_name;
    }
    
    public boolean isUsesStandardCharacters() {
        return this.m_usesStandardCharacters;
    }
    
    public boolean isTranslated() {
        return this.m_translated;
    }
    
    public boolean separatesWords() {
        return this.m_separatesWords;
    }
    
    public String getCode() {
        return this.m_code;
    }
    
    static {
        m_logger = Logger.getLogger((Class)Language.class);
        LANGS_BY_CODE = new LightWeightMap<String, Language>();
        for (final Language l : values()) {
            Language.LANGS_BY_CODE.put(l.m_code.toLowerCase(), l);
        }
    }
}
