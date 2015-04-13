package com.ankamagames.baseImpl.graphics.core;

import com.ankamagames.framework.preferences.*;

public enum KeyPreferenceStoreEnum implements KeyInterface
{
    LANGUAGE_PREFERENCE_KEY("language", PreferenceStoreEnum.DEFAULT_PREFERENCE_STORE), 
    APPLICATION_RESOLUTION_KEY("resolution", PreferenceStoreEnum.DEFAULT_PREFERENCE_STORE), 
    MUSIC_VOLUME_PREFERENCE_KEY("musicVolume", PreferenceStoreEnum.DEFAULT_PREFERENCE_STORE), 
    AMBIANCE_SOUNDS_VOLUME_PREFERENCE_KEY("ambianceSoundsVolume", PreferenceStoreEnum.DEFAULT_PREFERENCE_STORE), 
    UI_SOUNDS_VOLUME_PREFERENCE_KEY("uiSoundsVolume", PreferenceStoreEnum.DEFAULT_PREFERENCE_STORE), 
    MUSIC_MUTE_PREFERENCE_KEY("musicMute", PreferenceStoreEnum.DEFAULT_PREFERENCE_STORE), 
    AMBIANCE_SOUNDS_MUTE_PREFERENCE_KEY("ambianceSoundsMute", PreferenceStoreEnum.DEFAULT_PREFERENCE_STORE), 
    UI_SOUNDS_MUTE_PREFERENCE_KEY("uiSoundsMute", PreferenceStoreEnum.DEFAULT_PREFERENCE_STORE), 
    MUSIC_CONTINUOUS_MODE_PREFERENCE_KEY("musicContinuousMode", PreferenceStoreEnum.DEFAULT_PREFERENCE_STORE), 
    TOOLTIPS_DURATION("tooltipsDuration", PreferenceStoreEnum.DEFAULT_PREFERENCE_STORE), 
    TOOLTIPS_DISPLAY("tooltipsDisplay", PreferenceStoreEnum.DEFAULT_PREFERENCE_STORE), 
    LOD_LEVEL_KEY("LODLevel", PreferenceStoreEnum.DEFAULT_PREFERENCE_STORE), 
    FIGHT_LOD_LEVEL_KEY("FightLODLevel", PreferenceStoreEnum.DEFAULT_PREFERENCE_STORE);
    
    private final String m_key;
    private final PreferenceStoreEnum m_preferenceStore;
    
    private KeyPreferenceStoreEnum(final String key, final PreferenceStoreEnum preferenceStore) {
        this.m_key = key;
        this.m_preferenceStore = preferenceStore;
    }
    
    @Override
    public String getKey() {
        return this.m_key;
    }
    
    @Override
    public PreferenceStore getPreferenceStore() {
        return GamePreferences.getPreferenceStore(this.m_preferenceStore);
    }
    
    public static KeyInterface getFromKey(final String key) {
        for (final KeyInterface keyInterface : values()) {
            if (keyInterface.getKey().equals(key)) {
                return keyInterface;
            }
        }
        return null;
    }
}
