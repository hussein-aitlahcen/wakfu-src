package com.ankamagames.framework.graphics.engine.text;

import java.util.regex.*;
import com.ankamagames.framework.kernel.core.translator.*;
import java.util.*;

class SubFontDefinition
{
    private static final Pattern LANGUAGE_LIST_PATTERN;
    public static final String DEFAULT_LANGUAGES = "default";
    private final ArrayList<Language> m_languages;
    private String m_path;
    private int m_fontSizeModificator;
    private int m_deltaX;
    private int m_deltaY;
    
    SubFontDefinition() {
        super();
        this.m_languages = new ArrayList<Language>();
    }
    
    void setPath(final String path) {
        this.m_path = path;
    }
    
    void setFontSizeModificator(final int fontSizeModificator) {
        this.m_fontSizeModificator = fontSizeModificator;
    }
    
    void setDeltaX(final int deltaX) {
        this.m_deltaX = deltaX;
    }
    
    void setDeltaY(final int deltaY) {
        this.m_deltaY = deltaY;
    }
    
    void setLanguages(final CharSequence languagesString) {
        final Collection<Language> list = new ArrayList<Language>();
        if (languagesString != null) {
            final String[] languages = SubFontDefinition.LANGUAGE_LIST_PATTERN.split(languagesString);
            for (int i = 0, size = languages.length; i < size; ++i) {
                final String lang = languages[i];
                final Language language = Language.getLanguage(lang, true);
                if (language != null) {
                    list.add(language);
                }
            }
        }
        if (list.isEmpty()) {
            this.setToDefault();
        }
        else {
            this.m_languages.clear();
            this.m_languages.addAll(list);
        }
    }
    
    private void setToDefault() {
        this.m_languages.clear();
        this.m_languages.addAll(Arrays.asList(Language.values()));
    }
    
    boolean contains(final Language lang) {
        return this.m_languages.contains(lang);
    }
    
    String getPath() {
        return this.m_path;
    }
    
    int getFontSizeModificator() {
        return this.m_fontSizeModificator;
    }
    
    int getDeltaX() {
        return this.m_deltaX;
    }
    
    int getDeltaY() {
        return this.m_deltaY;
    }
    
    static {
        LANGUAGE_LIST_PATTERN = Pattern.compile(",");
    }
}
