package com.ankamagames.wakfu.client.core.preferences;

import com.ankamagames.baseImpl.graphics.ui.*;
import com.ankamagames.framework.preferences.*;
import com.ankamagames.baseImpl.graphics.core.*;

public class WakfuUserDefinedAdapter implements UserDefinedAdapter
{
    @Override
    public PreferenceStore getPreferenceStoreForDialog(final String dialogId) {
        if (dialogId.equals("consoleDialog")) {
            return GamePreferences.getDefaultPreferenceStore();
        }
        if (dialogId.startsWith("webShopArticleDialog")) {
            return GamePreferences.getDefaultPreferenceStore();
        }
        return GamePreferences.getCharacterPreferenceStore();
    }
}
