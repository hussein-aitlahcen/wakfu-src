package com.ankamagames.baseImpl.common.clientAndServer.core;

import com.ankamagames.framework.kernel.core.translator.*;
import org.jetbrains.annotations.*;

public class GameContentTranslator extends Translator
{
    private static final String GAME_CONTENT_TRANSLATOR_PREFIX = "content.";
    private static final char GAME_CONTENT_TRANSLATOR_SEPARATOR = '.';
    
    public static GameContentTranslator getInstance() {
        return (GameContentTranslator)GameContentTranslator.m_instance;
    }
    
    @NotNull
    public String getStringWithoutFormat(final int contentType, final int contentId) {
        final String stringKey = "content." + contentType + '.' + contentId;
        return this.getStringWithoutFormat(stringKey);
    }
    
    @NotNull
    public String getString(final int contentType, final int contentId, final Object... args) {
        final String stringKey = "content." + contentType + '.' + contentId;
        return this.getString(stringKey, args);
    }
    
    public boolean containsContentKey(final int contentType, final int contentId) {
        final String stringKey = "content." + contentType + '.' + contentId;
        return this.containsKey(stringKey);
    }
}
