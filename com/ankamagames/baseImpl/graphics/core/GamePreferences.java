package com.ankamagames.baseImpl.graphics.core;

import org.apache.log4j.*;
import com.ankamagames.framework.preferences.*;
import com.ankamagames.framework.reflect.*;
import com.ankamagames.framework.kernel.core.translator.*;

public class GamePreferences implements FieldProvider
{
    private static final Logger m_logger;
    private static PreferenceStore m_defaultPreferenceStore;
    private static PreferenceStore m_accountPreferenceStore;
    private static PreferenceStore m_characterPreferenceStore;
    
    public static PreferenceStore getPreferenceStore(final PreferenceStoreEnum preferenceStoreEnum) {
        switch (preferenceStoreEnum) {
            case DEFAULT_PREFERENCE_STORE: {
                return GamePreferences.m_defaultPreferenceStore;
            }
            case ACCOUNT_PREFERENCE_STORE: {
                return GamePreferences.m_accountPreferenceStore;
            }
            case CHARACTER_PREFERENCE_STORE: {
                return GamePreferences.m_characterPreferenceStore;
            }
            default: {
                return null;
            }
        }
    }
    
    public KeyInterface getKeyInterface(final String key) {
        return KeyPreferenceStoreEnum.getFromKey(key);
    }
    
    public void setValue(final KeyInterface KeyInterface, final boolean value) {
        final PreferenceStore store = KeyInterface.getPreferenceStore();
        final String key = KeyInterface.getKey();
        if (store == null) {
            GamePreferences.m_logger.error((Object)("[CODE-Prefs] Impossible d'enregistrer la pr\u00c3©f\u00c3©rence de clef=" + key + " \u00c3  cet endroit, le preferenceStore n'est pas initialis\u00c3©."));
            return;
        }
        store.setValue(key, value);
        GlobalPropertiesProvider.getInstance().firePropertyValueChanged(this, key);
    }
    
    public void setValue(final KeyInterface KeyInterface, final double value) {
        final PreferenceStore store = KeyInterface.getPreferenceStore();
        final String key = KeyInterface.getKey();
        if (store == null) {
            GamePreferences.m_logger.error((Object)("[CODE-Prefs] Impossible d'enregistrer la pr\u00c3©f\u00c3©rence de clef=" + key + " \u00c3  cet endroit, le preferenceStore n'est pas initialis\u00c3©."));
            return;
        }
        store.setValue(key, value);
        GlobalPropertiesProvider.getInstance().firePropertyValueChanged(this, key);
    }
    
    public void setValue(final KeyInterface KeyInterface, final float value) {
        final PreferenceStore store = KeyInterface.getPreferenceStore();
        final String key = KeyInterface.getKey();
        if (store == null) {
            GamePreferences.m_logger.error((Object)("[CODE-Prefs] Impossible d'enregistrer la pr\u00c3©f\u00c3©rence de clef=" + key + " \u00c3  cet endroit, le preferenceStore n'est pas initialis\u00c3©."));
            return;
        }
        store.setValue(key, value);
        GlobalPropertiesProvider.getInstance().firePropertyValueChanged(this, key);
    }
    
    public void setValue(final KeyInterface KeyInterface, final int value) {
        final PreferenceStore store = KeyInterface.getPreferenceStore();
        final String key = KeyInterface.getKey();
        if (store == null) {
            GamePreferences.m_logger.error((Object)("[CODE-Prefs] Impossible d'enregistrer la pr\u00c3©f\u00c3©rence de clef=" + key + " \u00c3  cet endroit, le preferenceStore n'est pas initialis\u00c3©."));
            return;
        }
        store.setValue(key, value);
        GlobalPropertiesProvider.getInstance().firePropertyValueChanged(this, key);
    }
    
    public void setValue(final KeyInterface KeyInterface, final long value) {
        final PreferenceStore store = KeyInterface.getPreferenceStore();
        final String key = KeyInterface.getKey();
        if (store == null) {
            GamePreferences.m_logger.error((Object)("[CODE-Prefs] Impossible d'enregistrer la pr\u00c3©f\u00c3©rence de clef=" + key + " \u00c3  cet endroit, le preferenceStore n'est pas initialis\u00c3©."));
            return;
        }
        store.setValue(key, value);
        GlobalPropertiesProvider.getInstance().firePropertyValueChanged(this, key);
    }
    
    public void setValue(final KeyInterface KeyInterface, final String value) {
        final PreferenceStore store = KeyInterface.getPreferenceStore();
        final String key = KeyInterface.getKey();
        if (store == null) {
            GamePreferences.m_logger.error((Object)("[CODE-Prefs] Impossible d'enregistrer la pr\u00c3©f\u00c3©rence de clef=" + key + " \u00c3  cet endroit, le preferenceStore n'est pas initialis\u00c3©."));
            return;
        }
        store.setValue(key, value);
        GlobalPropertiesProvider.getInstance().firePropertyValueChanged(this, key);
    }
    
    public void setDefault(final KeyInterface KeyInterface, final boolean value) {
        final PreferenceStore store = KeyInterface.getPreferenceStore();
        final String key = KeyInterface.getKey();
        if (store == null) {
            GamePreferences.m_logger.error((Object)("[CODE-Prefs] Impossible d'enregistrer la pr\u00c3©f\u00c3©rence de clef=" + key + " \u00c3  cet endroit, le preferenceStore n'est pas initialis\u00c3©."));
            return;
        }
        store.setDefault(key, value);
        GlobalPropertiesProvider.getInstance().firePropertyValueChanged(this, key);
    }
    
    public void setDefault(final KeyInterface KeyInterface, final double value) {
        final PreferenceStore store = KeyInterface.getPreferenceStore();
        final String key = KeyInterface.getKey();
        if (store == null) {
            GamePreferences.m_logger.error((Object)("[CODE-Prefs] Impossible d'enregistrer la pr\u00c3©f\u00c3©rence de clef=" + key + " \u00c3  cet endroit, le preferenceStore n'est pas initialis\u00c3©."));
            return;
        }
        store.setDefault(key, value);
        GlobalPropertiesProvider.getInstance().firePropertyValueChanged(this, key);
    }
    
    public void setDefault(final KeyInterface KeyInterface, final float value) {
        final PreferenceStore store = KeyInterface.getPreferenceStore();
        final String key = KeyInterface.getKey();
        if (store == null) {
            GamePreferences.m_logger.error((Object)("[CODE-Prefs] Impossible d'enregistrer la pr\u00c3©f\u00c3©rence de clef=" + key + " \u00c3  cet endroit, le preferenceStore n'est pas initialis\u00c3©."));
            return;
        }
        store.setDefault(key, value);
        GlobalPropertiesProvider.getInstance().firePropertyValueChanged(this, key);
    }
    
    public void setDefault(final KeyInterface KeyInterface, final int value) {
        final PreferenceStore store = KeyInterface.getPreferenceStore();
        final String key = KeyInterface.getKey();
        if (store == null) {
            GamePreferences.m_logger.error((Object)("[CODE-Prefs] Impossible d'enregistrer la pr\u00c3©f\u00c3©rence de clef=" + key + " \u00c3  cet endroit, le preferenceStore n'est pas initialis\u00c3©."));
            return;
        }
        store.setDefault(key, value);
        GlobalPropertiesProvider.getInstance().firePropertyValueChanged(this, key);
    }
    
    public void setDefault(final KeyInterface KeyInterface, final long value) {
        final PreferenceStore store = KeyInterface.getPreferenceStore();
        final String key = KeyInterface.getKey();
        if (store == null) {
            GamePreferences.m_logger.error((Object)("[CODE-Prefs] Impossible d'enregistrer la pr\u00c3©f\u00c3©rence de clef=" + key + " \u00c3  cet endroit, le preferenceStore n'est pas initialis\u00c3©."));
            return;
        }
        store.setDefault(key, value);
        GlobalPropertiesProvider.getInstance().firePropertyValueChanged(this, key);
    }
    
    public void setDefault(final KeyInterface KeyInterface, final String value) {
        final PreferenceStore store = KeyInterface.getPreferenceStore();
        final String key = KeyInterface.getKey();
        if (store == null) {
            GamePreferences.m_logger.error((Object)("[CODE-Prefs] Impossible d'enregistrer la pr\u00c3©f\u00c3©rence de clef=" + key + " \u00c3  cet endroit, le preferenceStore n'est pas initialis\u00c3©."));
            return;
        }
        store.setDefault(key, value);
        GlobalPropertiesProvider.getInstance().firePropertyValueChanged(this, key);
    }
    
    public boolean getBooleanValue(final KeyInterface KeyInterface) {
        final PreferenceStore store = KeyInterface.getPreferenceStore();
        final String key = KeyInterface.getKey();
        if (store == null) {
            GamePreferences.m_logger.error((Object)("[CODE-Prefs] Impossible de r\u00c3©cup\u00c3©rer la pr\u00c3©f\u00c3©rence de clef=" + key + " \u00c3  cet endroit, le preferenceStore n'est pas initialis\u00c3©."));
            return false;
        }
        return store.getBoolean(key);
    }
    
    public double getDoubleValue(final KeyInterface KeyInterface) {
        final PreferenceStore store = KeyInterface.getPreferenceStore();
        final String key = KeyInterface.getKey();
        if (store == null) {
            GamePreferences.m_logger.error((Object)("[CODE-Prefs] Impossible de r\u00c3©cup\u00c3©rer la pr\u00c3©f\u00c3©rence de clef=" + key + " \u00c3  cet endroit, le preferenceStore n'est pas initialis\u00c3©."));
            return 0.0;
        }
        return store.getDouble(key);
    }
    
    public float getFloatValue(final KeyInterface KeyInterface) {
        final PreferenceStore store = KeyInterface.getPreferenceStore();
        final String key = KeyInterface.getKey();
        if (store == null) {
            GamePreferences.m_logger.error((Object)("[CODE-Prefs] Impossible de r\u00c3©cup\u00c3©rer la pr\u00c3©f\u00c3©rence de clef=" + key + " \u00c3  cet endroit, le preferenceStore n'est pas initialis\u00c3©."));
            return 0.0f;
        }
        return store.getFloat(key);
    }
    
    public int getIntValue(final KeyInterface KeyInterface) {
        final PreferenceStore store = KeyInterface.getPreferenceStore();
        final String key = KeyInterface.getKey();
        if (store == null) {
            GamePreferences.m_logger.error((Object)("[CODE-Prefs] Impossible de r\u00c3©cup\u00c3©rer la pr\u00c3©f\u00c3©rence de clef=" + key + " \u00c3  cet endroit, le preferenceStore n'est pas initialis\u00c3©."));
            return 0;
        }
        return store.getInt(key);
    }
    
    public long getLongValue(final KeyInterface KeyInterface) {
        final PreferenceStore store = KeyInterface.getPreferenceStore();
        final String key = KeyInterface.getKey();
        if (store == null) {
            GamePreferences.m_logger.error((Object)("[CODE-Prefs] Impossible de r\u00c3©cup\u00c3©rer la pr\u00c3©f\u00c3©rence de clef=" + key + " \u00c3  cet endroit, le preferenceStore n'est pas initialis\u00c3©."));
            return 0L;
        }
        return store.getLong(key);
    }
    
    public String getStringValue(final KeyInterface KeyInterface) {
        final PreferenceStore store = KeyInterface.getPreferenceStore();
        final String key = KeyInterface.getKey();
        if (store == null) {
            GamePreferences.m_logger.error((Object)("[CODE-Prefs] Impossible de r\u00c3©cup\u00c3©rer la pr\u00c3©f\u00c3©rence de clef=" + key + " \u00c3  cet endroit, le preferenceStore n'est pas initialis\u00c3©."));
            return null;
        }
        return store.getString(key);
    }
    
    public boolean getDefaultBoolean(final KeyInterface KeyInterface) {
        final PreferenceStore store = KeyInterface.getPreferenceStore();
        final String key = KeyInterface.getKey();
        if (store == null) {
            GamePreferences.m_logger.error((Object)("[CODE-Prefs] Impossible de r\u00c3©cup\u00c3©rer la pr\u00c3©f\u00c3©rence de clef=" + key + " \u00c3  cet endroit, le preferenceStore n'est pas initialis\u00c3©."));
            return false;
        }
        return store.getDefaultBoolean(key);
    }
    
    public double getDefaultDouble(final KeyInterface KeyInterface) {
        final PreferenceStore store = KeyInterface.getPreferenceStore();
        final String key = KeyInterface.getKey();
        if (store == null) {
            GamePreferences.m_logger.error((Object)("[CODE-Prefs] Impossible de r\u00c3©cup\u00c3©rer la pr\u00c3©f\u00c3©rence de clef=" + key + " \u00c3  cet endroit, le preferenceStore n'est pas initialis\u00c3©."));
            return 0.0;
        }
        return store.getDefaultDouble(key);
    }
    
    public float getDefaultFloat(final KeyInterface KeyInterface) {
        final PreferenceStore store = KeyInterface.getPreferenceStore();
        final String key = KeyInterface.getKey();
        if (store == null) {
            GamePreferences.m_logger.error((Object)("[CODE-Prefs] Impossible de r\u00c3©cup\u00c3©rer la pr\u00c3©f\u00c3©rence de clef=" + key + " \u00c3  cet endroit, le preferenceStore n'est pas initialis\u00c3©."));
            return 0.0f;
        }
        return store.getDefaultFloat(key);
    }
    
    public int getDefaultInt(final KeyInterface KeyInterface) {
        final PreferenceStore store = KeyInterface.getPreferenceStore();
        final String key = KeyInterface.getKey();
        if (store == null) {
            GamePreferences.m_logger.error((Object)("[CODE-Prefs] Impossible de r\u00c3©cup\u00c3©rer la pr\u00c3©f\u00c3©rence de clef=" + key + " \u00c3  cet endroit, le preferenceStore n'est pas initialis\u00c3©."));
            return 0;
        }
        return store.getDefaultInt(key);
    }
    
    public long getDefaultLong(final KeyInterface KeyInterface) {
        final PreferenceStore store = KeyInterface.getPreferenceStore();
        final String key = KeyInterface.getKey();
        if (store == null) {
            GamePreferences.m_logger.error((Object)("[CODE-Prefs] Impossible de r\u00c3©cup\u00c3©rer la pr\u00c3©f\u00c3©rence de clef=" + key + " \u00c3  cet endroit, le preferenceStore n'est pas initialis\u00c3©."));
            return 0L;
        }
        return store.getDefaultLong(key);
    }
    
    public String getDefaultString(final KeyInterface KeyInterface) {
        final PreferenceStore store = KeyInterface.getPreferenceStore();
        final String key = KeyInterface.getKey();
        if (store == null) {
            GamePreferences.m_logger.error((Object)("[CODE-Prefs] Impossible de r\u00c3©cup\u00c3©rer la pr\u00c3©f\u00c3©rence de clef=" + key + " \u00c3  cet endroit, le preferenceStore n'est pas initialis\u00c3©."));
            return null;
        }
        return store.getDefaultString(key);
    }
    
    protected void initializeAccountDefaultValues() {
    }
    
    protected void initializeCharacterDefaultValues() {
    }
    
    protected void initializeDefaultValues() {
        this.setDefault(KeyPreferenceStoreEnum.LANGUAGE_PREFERENCE_KEY, Translator.getDefaultLanguage().getActualLocale().getLanguage());
        this.setDefault(KeyPreferenceStoreEnum.MUSIC_VOLUME_PREFERENCE_KEY, 1);
        this.setDefault(KeyPreferenceStoreEnum.AMBIANCE_SOUNDS_VOLUME_PREFERENCE_KEY, 1);
        this.setDefault(KeyPreferenceStoreEnum.UI_SOUNDS_VOLUME_PREFERENCE_KEY, 1);
        this.setDefault(KeyPreferenceStoreEnum.MUSIC_MUTE_PREFERENCE_KEY, false);
        this.setDefault(KeyPreferenceStoreEnum.AMBIANCE_SOUNDS_MUTE_PREFERENCE_KEY, false);
        this.setDefault(KeyPreferenceStoreEnum.UI_SOUNDS_MUTE_PREFERENCE_KEY, false);
        this.setDefault(KeyPreferenceStoreEnum.MUSIC_CONTINUOUS_MODE_PREFERENCE_KEY, false);
        this.setDefault(KeyPreferenceStoreEnum.TOOLTIPS_DISPLAY, true);
        this.setDefault(KeyPreferenceStoreEnum.TOOLTIPS_DURATION, 3000);
        this.setDefault(KeyPreferenceStoreEnum.LOD_LEVEL_KEY, 2);
    }
    
    @Override
    public void appendFieldValue(final String fieldName, final Object value) {
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals(KeyPreferenceStoreEnum.LANGUAGE_PREFERENCE_KEY.getKey())) {
            return this.getStringValue(KeyPreferenceStoreEnum.LANGUAGE_PREFERENCE_KEY);
        }
        if (fieldName.equals(KeyPreferenceStoreEnum.MUSIC_VOLUME_PREFERENCE_KEY.getKey())) {
            return this.getFloatValue(KeyPreferenceStoreEnum.MUSIC_VOLUME_PREFERENCE_KEY);
        }
        if (fieldName.equals(KeyPreferenceStoreEnum.AMBIANCE_SOUNDS_VOLUME_PREFERENCE_KEY.getKey())) {
            return this.getFloatValue(KeyPreferenceStoreEnum.AMBIANCE_SOUNDS_VOLUME_PREFERENCE_KEY);
        }
        if (fieldName.equals(KeyPreferenceStoreEnum.UI_SOUNDS_VOLUME_PREFERENCE_KEY.getKey())) {
            return this.getFloatValue(KeyPreferenceStoreEnum.UI_SOUNDS_VOLUME_PREFERENCE_KEY);
        }
        if (fieldName.equals(KeyPreferenceStoreEnum.MUSIC_MUTE_PREFERENCE_KEY.getKey())) {
            return this.getBooleanValue(KeyPreferenceStoreEnum.MUSIC_MUTE_PREFERENCE_KEY);
        }
        if (fieldName.equals(KeyPreferenceStoreEnum.AMBIANCE_SOUNDS_MUTE_PREFERENCE_KEY.getKey())) {
            return this.getBooleanValue(KeyPreferenceStoreEnum.AMBIANCE_SOUNDS_MUTE_PREFERENCE_KEY);
        }
        if (fieldName.equals(KeyPreferenceStoreEnum.UI_SOUNDS_MUTE_PREFERENCE_KEY.getKey())) {
            return this.getBooleanValue(KeyPreferenceStoreEnum.UI_SOUNDS_MUTE_PREFERENCE_KEY);
        }
        if (fieldName.equals(KeyPreferenceStoreEnum.MUSIC_CONTINUOUS_MODE_PREFERENCE_KEY.getKey())) {
            return this.getBooleanValue(KeyPreferenceStoreEnum.MUSIC_CONTINUOUS_MODE_PREFERENCE_KEY);
        }
        if (fieldName.equals(KeyPreferenceStoreEnum.LOD_LEVEL_KEY.getKey())) {
            return this.getFloatValue(KeyPreferenceStoreEnum.LOD_LEVEL_KEY);
        }
        if (fieldName.equals(KeyPreferenceStoreEnum.FIGHT_LOD_LEVEL_KEY.getKey())) {
            return this.getFloatValue(KeyPreferenceStoreEnum.FIGHT_LOD_LEVEL_KEY);
        }
        return null;
    }
    
    @Override
    public String[] getFields() {
        final KeyPreferenceStoreEnum[] keyPreferenceStoreEnums = KeyPreferenceStoreEnum.values();
        final String[] fields = new String[keyPreferenceStoreEnums.length];
        int i = 0;
        for (final KeyPreferenceStoreEnum keyPreferenceStoreEnum : keyPreferenceStoreEnums) {
            fields[i] = keyPreferenceStoreEnum.getKey();
            ++i;
        }
        return fields;
    }
    
    @Override
    public boolean isFieldSynchronisable(final String fieldName) {
        return false;
    }
    
    @Override
    public void prependFieldValue(final String fieldName, final Object value) {
    }
    
    @Override
    public void setFieldValue(final String fieldName, final Object value) {
    }
    
    protected void firePropertyValueChanged(final String field) {
        GlobalPropertiesProvider.getInstance().firePropertyValueChanged(this, field);
    }
    
    public void resetToDefault() {
        if (GamePreferences.m_defaultPreferenceStore != null) {
            GamePreferences.m_defaultPreferenceStore.resetToDefault();
        }
        if (GamePreferences.m_accountPreferenceStore != null) {
            GamePreferences.m_accountPreferenceStore.resetToDefault();
        }
        if (GamePreferences.m_characterPreferenceStore != null) {
            GamePreferences.m_characterPreferenceStore.resetToDefault();
        }
    }
    
    public static PreferenceStore getCharacterPreferenceStore() {
        return GamePreferences.m_characterPreferenceStore;
    }
    
    public static PreferenceStore getAccountPreferenceStore() {
        return GamePreferences.m_accountPreferenceStore;
    }
    
    public static PreferenceStore getDefaultPreferenceStore() {
        return GamePreferences.m_defaultPreferenceStore;
    }
    
    public void setDefaultPreferenceStore(final PreferenceStore defaultPreferenceStore) {
        GamePreferences.m_defaultPreferenceStore = defaultPreferenceStore;
        if (GamePreferences.m_defaultPreferenceStore != null) {
            this.initializeDefaultValues();
        }
    }
    
    public void setAccountPreferenceStore(final PreferenceStore accountPreferenceStore) {
        GamePreferences.m_accountPreferenceStore = accountPreferenceStore;
        if (GamePreferences.m_accountPreferenceStore != null) {
            this.initializeAccountDefaultValues();
        }
    }
    
    public void setCharacterPreferenceStore(final PreferenceStore characterPreferenceStore) {
        GamePreferences.m_characterPreferenceStore = characterPreferenceStore;
        if (GamePreferences.m_characterPreferenceStore != null) {
            this.initializeCharacterDefaultValues();
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)GamePreferences.class);
    }
}
