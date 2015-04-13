package com.ankamagames.wakfu.client.core.preferences;

import com.ankamagames.framework.preferences.*;
import com.ankamagames.baseImpl.graphics.core.*;

public enum WakfuKeyPreferenceStoreEnum implements KeyInterface
{
    VERSION_PREFERENCE_KEY("version", PreferenceStoreEnum.DEFAULT_PREFERENCE_STORE), 
    NEED_FIRST_LAUNCH_CHARACTER_UPDATE("needFirstLaunchUpdate", PreferenceStoreEnum.CHARACTER_PREFERENCE_STORE), 
    CHARACTERS_NUMBER_ON_SERVER_PATTERN("charactersList", PreferenceStoreEnum.ACCOUNT_PREFERENCE_STORE), 
    LANGUAGE_PREFERENCE_KEY("language", PreferenceStoreEnum.DEFAULT_PREFERENCE_STORE), 
    REMEMBER_LAST_LOGIN_PREFERENCE_KEY("rememberLastLogin", PreferenceStoreEnum.DEFAULT_PREFERENCE_STORE), 
    LAST_LOGIN_PREFERENCE_KEY("lastLogin", PreferenceStoreEnum.DEFAULT_PREFERENCE_STORE), 
    LAST_PROXY_GROUP_INDEX_PREFERENCE_KEY("lastProxyGroup", PreferenceStoreEnum.DEFAULT_PREFERENCE_STORE), 
    RADAR_ZOOM_SCALE_PREFERENCE_KEY("radarZoomScale", PreferenceStoreEnum.ACCOUNT_PREFERENCE_STORE), 
    ENVIRONMENT_SFX_VOLUME_PREFERENCE_KEY("environmentSfxVolume", PreferenceStoreEnum.DEFAULT_PREFERENCE_STORE), 
    ENVIRONMENT_SFX_MUTE_PREFERENCE_KEY("environmentSfxVolume", PreferenceStoreEnum.DEFAULT_PREFERENCE_STORE), 
    OPENED_SHORTCUT_BARS_KEY("openedShortcutBars", PreferenceStoreEnum.CHARACTER_PREFERENCE_STORE), 
    OPENED_SHORTCUT_BAR_VERTICAL_KEY("openedShortcutBarVertical", PreferenceStoreEnum.CHARACTER_PREFERENCE_STORE), 
    DEFAULT_SHORTCUT_BAR_TYPE("defaultShortcutBarType", PreferenceStoreEnum.CHARACTER_PREFERENCE_STORE), 
    CURRENT_SPELL_SHORTCUT_BAR_INDEX("currentSpellShorctutBarIndex", PreferenceStoreEnum.CHARACTER_PREFERENCE_STORE), 
    CURRENT_ITEM_SHORTCUT_BAR_INDEX("currentItemShorctutBarIndex", PreferenceStoreEnum.CHARACTER_PREFERENCE_STORE), 
    SELECTED_LANDMARK_FILTERS("selectedLandMarksFiters", PreferenceStoreEnum.CHARACTER_PREFERENCE_STORE), 
    GRAPHICAL_PRESETS_KEY("graphicalPresets", PreferenceStoreEnum.DEFAULT_PREFERENCE_STORE), 
    SHADERS_ACTIVATED_KEY("shadersActivated", PreferenceStoreEnum.DEFAULT_PREFERENCE_STORE), 
    METEO_EFFECT_ACTIVATED_KEY("meteoEffectActivated", PreferenceStoreEnum.DEFAULT_PREFERENCE_STORE), 
    ALPHA_MASK_ACTIVATED_KEY("alphaMaskActivated", PreferenceStoreEnum.DEFAULT_PREFERENCE_STORE), 
    HIDE_FIGHT_OCCLUDERS_ACTIVATED_KEY("hideFightOccludersActivated", PreferenceStoreEnum.CHARACTER_PREFERENCE_STORE), 
    DISPLAY_HP_BAR_KEY("displayHPBar", PreferenceStoreEnum.ACCOUNT_PREFERENCE_STORE), 
    VSYNC_ACTIVATED_KEY("vsyncActivated", PreferenceStoreEnum.DEFAULT_PREFERENCE_STORE), 
    ENABLE_RUNNING_RADIUS("enableRunningRadius", PreferenceStoreEnum.DEFAULT_PREFERENCE_STORE), 
    AREA_CHALLENGES_ACTIVATED_KEY("areaChallengesActivated", PreferenceStoreEnum.ACCOUNT_PREFERENCE_STORE), 
    AUTO_LOCK_FIGHTS_KEY("autoLockFights", PreferenceStoreEnum.ACCOUNT_PREFERENCE_STORE), 
    TACTICAL_VIEW_KEY("tacticalView", PreferenceStoreEnum.ACCOUNT_PREFERENCE_STORE), 
    CHAT_FADE_KEY("chatFade", PreferenceStoreEnum.CHARACTER_PREFERENCE_STORE), 
    INTERACTION_ON_LEFT_CLICK_KEY("interactionOnLeftClick", PreferenceStoreEnum.ACCOUNT_PREFERENCE_STORE), 
    APTITUDE_DISPLAY_MODE_KEY("aptitudeDisplayMode", PreferenceStoreEnum.CHARACTER_PREFERENCE_STORE), 
    HP_DISPLAY_TYPE_KEY("hpDisplayType", PreferenceStoreEnum.ACCOUNT_PREFERENCE_STORE), 
    DEFAULT_SPLIT_MODE_KEY("defaultSplitMode", PreferenceStoreEnum.ACCOUNT_PREFERENCE_STORE), 
    OVER_HEAD_DELAY_KEY("overHeadDelay", PreferenceStoreEnum.ACCOUNT_PREFERENCE_STORE), 
    BARS_LOCKED_MODE_KEY("barsLockedMode", PreferenceStoreEnum.CHARACTER_PREFERENCE_STORE), 
    AUTO_SWITCH_BARS_MODE_KEY("autoSwitchBarsMode", PreferenceStoreEnum.ACCOUNT_PREFERENCE_STORE), 
    DISPLAY_TERRITORIES_KEY("displayTerritories", PreferenceStoreEnum.CHARACTER_PREFERENCE_STORE), 
    GUILD_DISPLAY_DISCONNECTED_MEMBERS_KEY("guildDisplayDisconnectedMembers", PreferenceStoreEnum.CHARACTER_PREFERENCE_STORE), 
    CHAT_AUTOMATIC_DISACTIVATION("chatAutomaticDisactivation", PreferenceStoreEnum.CHARACTER_PREFERENCE_STORE), 
    CHAT_TEXT_FONT_SIZE_KEY("chatTextFontSize", PreferenceStoreEnum.CHARACTER_PREFERENCE_STORE), 
    EMOTE_ICONS_ACTIVATED("emoteIconsActivated", PreferenceStoreEnum.ACCOUNT_PREFERENCE_STORE), 
    CENSOR_ACTIVATED("censorActivated", PreferenceStoreEnum.ACCOUNT_PREFERENCE_STORE), 
    PET_CAN_BE_PACKED("petCanBePacked", PreferenceStoreEnum.CHARACTER_PREFERENCE_STORE), 
    TIPS_ACTIVATED("tipsActivated", PreferenceStoreEnum.ACCOUNT_PREFERENCE_STORE), 
    LAST_CRAFT_SEEN("lastCraftSeen", PreferenceStoreEnum.CHARACTER_PREFERENCE_STORE), 
    LAST_SELECTED_CHARACTER_NAME("lastSelectedCharacterName", PreferenceStoreEnum.ACCOUNT_PREFERENCE_STORE), 
    CHAT_TIME("chatTime", PreferenceStoreEnum.ACCOUNT_PREFERENCE_STORE), 
    DISPLAY_XP_BAR("displayXpBar", PreferenceStoreEnum.ACCOUNT_PREFERENCE_STORE), 
    MINIMAP_ENABLE("minimapEnable", PreferenceStoreEnum.ACCOUNT_PREFERENCE_STORE), 
    DISPLAY_STATE_BAR("displayStateBar", PreferenceStoreEnum.ACCOUNT_PREFERENCE_STORE), 
    FOLLOWED_QUESTS_DISPLAY("followedQuestsDisplay", PreferenceStoreEnum.ACCOUNT_PREFERENCE_STORE), 
    DISPLAY_DEBUG_BAR("debugBar", PreferenceStoreEnum.DEFAULT_PREFERENCE_STORE), 
    ACTIVE_CLIENT_EVENT_LISTENERS("activeClientEventListeners", PreferenceStoreEnum.CHARACTER_PREFERENCE_STORE), 
    INACTIVE_CLIENT_EVENT_LISTENERS("inactiveClientEventListeners", PreferenceStoreEnum.CHARACTER_PREFERENCE_STORE), 
    ADD_SEEDS_TO_SHORTCUT_BAR("addSeedsToShortcutBar", PreferenceStoreEnum.ACCOUNT_PREFERENCE_STORE), 
    COMPANIONS_LIST_SAVED_SEARCH_KEY("companionsListSavedSearch", PreferenceStoreEnum.ACCOUNT_PREFERENCE_STORE), 
    LAST_SERVER_ID_KEY("lastServerId", PreferenceStoreEnum.DEFAULT_PREFERENCE_STORE);
    
    private final String m_key;
    private final PreferenceStoreEnum m_preferenceStore;
    
    private WakfuKeyPreferenceStoreEnum(final String key, final PreferenceStoreEnum preferenceStore) {
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
