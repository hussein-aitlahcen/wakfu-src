package com.ankamagames.wakfu.client.core.preferences;

import com.ankamagames.framework.preferences.*;

public class CharacterPreferenceStore extends PreferenceStore
{
    private static final String USER_PREFERENCES_FILE_PATTERN = "characterPreferences-%s.properties";
    
    public CharacterPreferenceStore(final String characterName) {
        super(String.format("characterPreferences-%s.properties", characterName));
        this.setIgnoreNoneDefaultDefinedProperties(false);
        this.setAutoSave(true);
    }
}
