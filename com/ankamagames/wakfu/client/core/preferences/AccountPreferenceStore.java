package com.ankamagames.wakfu.client.core.preferences;

import com.ankamagames.framework.preferences.*;

public class AccountPreferenceStore extends PreferenceStore
{
    private static final String USER_PREFERENCES_FILE_PATTERN = "accountPreferences%d.properties";
    
    public AccountPreferenceStore(final long accountId) {
        super(String.format("accountPreferences%d.properties", accountId));
        this.setIgnoreNoneDefaultDefinedProperties(false);
        this.setAutoSave(true);
    }
}
