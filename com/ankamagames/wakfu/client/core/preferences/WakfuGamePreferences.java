package com.ankamagames.wakfu.client.core.preferences;

import org.apache.log4j.*;
import java.util.regex.*;
import com.ankamagames.baseImpl.graphics.core.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.framework.fileFormat.properties.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.framework.kernel.core.translator.*;
import com.ankamagames.wakfu.common.game.shortcut.*;
import com.ankamagames.wakfu.client.chat.*;
import com.ankamagames.framework.preferences.*;
import com.ankamagames.wakfu.client.core.preferences.checker.*;
import java.io.*;
import java.net.*;

public class WakfuGamePreferences extends GamePreferences implements PreferenceStoreLoadListener
{
    public static final String LANGUAGE_ICON = "languageIcon";
    protected static final Logger m_logger;
    private static final short VERSION = 11;
    public static final String CHAT_WINDOW_MODULATION_COLOR_FIELD = "chatWindowModulationColor";
    private static final String[] FIELDS;
    final FileFilter m_fileFilter;
    
    public WakfuGamePreferences() {
        super();
        this.m_fileFilter = new FileFilter() {
            private final Pattern m_pattern = Pattern.compile("(accountPreferences|characterPreferences).*(.properties)");
            
            private boolean isDeletablePrefFile(final String pathName) {
                return pathName.length() > 0 && this.m_pattern.matcher(pathName).matches();
            }
            
            @Override
            public boolean accept(final File pathname) {
                return pathname.isDirectory() || this.isDeletablePrefFile(pathname.getName());
            }
        };
    }
    
    @Override
    public KeyInterface getKeyInterface(final String key) {
        final KeyInterface keyInterface = WakfuKeyPreferenceStoreEnum.getFromKey(key);
        return (keyInterface != null) ? keyInterface : super.getKeyInterface(key);
    }
    
    @Override
    public String[] getFields() {
        final WakfuKeyPreferenceStoreEnum[] wakfuPreferences = WakfuKeyPreferenceStoreEnum.values();
        final KeyPreferenceStoreEnum[] generalPreferences = KeyPreferenceStoreEnum.values();
        final String[] fields = new String[wakfuPreferences.length + generalPreferences.length];
        int i = 0;
        for (final WakfuKeyPreferenceStoreEnum key : wakfuPreferences) {
            fields[i] = key.getKey();
            ++i;
        }
        for (final KeyPreferenceStoreEnum key2 : generalPreferences) {
            fields[i] = key2.getKey();
            ++i;
        }
        final String[] result = new String[fields.length + WakfuGamePreferences.FIELDS.length];
        System.arraycopy(fields, 0, result, 0, fields.length);
        System.arraycopy(WakfuGamePreferences.FIELDS, 0, result, fields.length, WakfuGamePreferences.FIELDS.length);
        return result;
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("languageIcon")) {
            final Language language = WakfuTranslator.getInstance().getLanguage();
            try {
                return String.format(WakfuConfiguration.getInstance().getString("langIconsPath"), language.getActualLocale().getLanguage().toUpperCase());
            }
            catch (PropertyException e) {
                WakfuGamePreferences.m_logger.warn((Object)e.getMessage());
                return null;
            }
        }
        if (fieldName.equals(KeyPreferenceStoreEnum.LANGUAGE_PREFERENCE_KEY.getKey())) {
            final Language language = WakfuTranslator.getInstance().getLanguage();
            return (language != null) ? language.getActualLocale().getLanguage() : null;
        }
        if (fieldName.equals(WakfuKeyPreferenceStoreEnum.REMEMBER_LAST_LOGIN_PREFERENCE_KEY.getKey())) {
            return this.getBooleanValue(WakfuKeyPreferenceStoreEnum.REMEMBER_LAST_LOGIN_PREFERENCE_KEY);
        }
        if (fieldName.equals(WakfuKeyPreferenceStoreEnum.LAST_LOGIN_PREFERENCE_KEY.getKey())) {
            return this.getStringValue(WakfuKeyPreferenceStoreEnum.LAST_LOGIN_PREFERENCE_KEY);
        }
        if (fieldName.equals(WakfuKeyPreferenceStoreEnum.LAST_PROXY_GROUP_INDEX_PREFERENCE_KEY.getKey())) {
            return this.getIntValue(WakfuKeyPreferenceStoreEnum.LAST_PROXY_GROUP_INDEX_PREFERENCE_KEY);
        }
        if (fieldName.equals(WakfuKeyPreferenceStoreEnum.SHADERS_ACTIVATED_KEY.getKey())) {
            return this.getBooleanValue(WakfuKeyPreferenceStoreEnum.SHADERS_ACTIVATED_KEY);
        }
        if (fieldName.equals(WakfuKeyPreferenceStoreEnum.METEO_EFFECT_ACTIVATED_KEY.getKey())) {
            return this.getBooleanValue(WakfuKeyPreferenceStoreEnum.METEO_EFFECT_ACTIVATED_KEY);
        }
        if (fieldName.equals(WakfuKeyPreferenceStoreEnum.GRAPHICAL_PRESETS_KEY.getKey())) {
            return WakfuGraphicalPresets.Level.getFromId((byte)this.getIntValue(WakfuKeyPreferenceStoreEnum.GRAPHICAL_PRESETS_KEY));
        }
        if (fieldName.equals(WakfuKeyPreferenceStoreEnum.ALPHA_MASK_ACTIVATED_KEY.getKey())) {
            return this.getBooleanValue(WakfuKeyPreferenceStoreEnum.ALPHA_MASK_ACTIVATED_KEY);
        }
        if (fieldName.equals(WakfuKeyPreferenceStoreEnum.HIDE_FIGHT_OCCLUDERS_ACTIVATED_KEY.getKey())) {
            return this.getBooleanValue(WakfuKeyPreferenceStoreEnum.HIDE_FIGHT_OCCLUDERS_ACTIVATED_KEY);
        }
        if (fieldName.equals(WakfuKeyPreferenceStoreEnum.VSYNC_ACTIVATED_KEY.getKey())) {
            return this.getBooleanValue(WakfuKeyPreferenceStoreEnum.VSYNC_ACTIVATED_KEY);
        }
        if (fieldName.equals(WakfuKeyPreferenceStoreEnum.ENABLE_RUNNING_RADIUS.getKey())) {
            return this.getBooleanValue(WakfuKeyPreferenceStoreEnum.ENABLE_RUNNING_RADIUS);
        }
        if (fieldName.equals(WakfuKeyPreferenceStoreEnum.AREA_CHALLENGES_ACTIVATED_KEY.getKey())) {
            return this.getBooleanValue(WakfuKeyPreferenceStoreEnum.AREA_CHALLENGES_ACTIVATED_KEY);
        }
        if (fieldName.equals(WakfuKeyPreferenceStoreEnum.AUTO_LOCK_FIGHTS_KEY.getKey())) {
            return this.getBooleanValue(WakfuKeyPreferenceStoreEnum.AUTO_LOCK_FIGHTS_KEY);
        }
        if (fieldName.equals(WakfuKeyPreferenceStoreEnum.TACTICAL_VIEW_KEY.getKey())) {
            return this.getBooleanValue(WakfuKeyPreferenceStoreEnum.TACTICAL_VIEW_KEY);
        }
        if (fieldName.equals(WakfuKeyPreferenceStoreEnum.CHAT_FADE_KEY.getKey())) {
            return this.getBooleanValue(WakfuKeyPreferenceStoreEnum.CHAT_FADE_KEY);
        }
        if (fieldName.equals(WakfuKeyPreferenceStoreEnum.INTERACTION_ON_LEFT_CLICK_KEY.getKey())) {
            return this.getBooleanValue(WakfuKeyPreferenceStoreEnum.INTERACTION_ON_LEFT_CLICK_KEY);
        }
        if (fieldName.equals(WakfuKeyPreferenceStoreEnum.HP_DISPLAY_TYPE_KEY.getKey())) {
            return ProgressText.ProgressBarDisplayValue.valueFromOrdinal(this.getIntValue(WakfuKeyPreferenceStoreEnum.HP_DISPLAY_TYPE_KEY));
        }
        if (fieldName.equals(WakfuKeyPreferenceStoreEnum.DEFAULT_SPLIT_MODE_KEY.getKey())) {
            return this.getBooleanValue(WakfuKeyPreferenceStoreEnum.DEFAULT_SPLIT_MODE_KEY);
        }
        if (fieldName.equals(WakfuKeyPreferenceStoreEnum.OVER_HEAD_DELAY_KEY.getKey())) {
            return this.getFloatValue(WakfuKeyPreferenceStoreEnum.OVER_HEAD_DELAY_KEY) * 2.0f;
        }
        if (fieldName.equals(WakfuKeyPreferenceStoreEnum.AUTO_SWITCH_BARS_MODE_KEY.getKey())) {
            return this.getBooleanValue(WakfuKeyPreferenceStoreEnum.AUTO_SWITCH_BARS_MODE_KEY);
        }
        if (fieldName.equals(WakfuKeyPreferenceStoreEnum.GUILD_DISPLAY_DISCONNECTED_MEMBERS_KEY.getKey())) {
            return this.getBooleanValue(WakfuKeyPreferenceStoreEnum.GUILD_DISPLAY_DISCONNECTED_MEMBERS_KEY);
        }
        if (fieldName.equals(WakfuKeyPreferenceStoreEnum.CHAT_TEXT_FONT_SIZE_KEY.getKey())) {
            return this.getIntValue(WakfuKeyPreferenceStoreEnum.CHAT_TEXT_FONT_SIZE_KEY);
        }
        if (fieldName.equals(WakfuKeyPreferenceStoreEnum.EMOTE_ICONS_ACTIVATED.getKey())) {
            return this.getBooleanValue(WakfuKeyPreferenceStoreEnum.EMOTE_ICONS_ACTIVATED);
        }
        if (fieldName.equals(WakfuKeyPreferenceStoreEnum.PET_CAN_BE_PACKED.getKey())) {
            return this.getBooleanValue(WakfuKeyPreferenceStoreEnum.PET_CAN_BE_PACKED);
        }
        if (fieldName.equals(WakfuKeyPreferenceStoreEnum.TIPS_ACTIVATED.getKey())) {
            return this.getBooleanValue(WakfuKeyPreferenceStoreEnum.TIPS_ACTIVATED);
        }
        if (fieldName.equals(WakfuKeyPreferenceStoreEnum.CHAT_AUTOMATIC_DISACTIVATION.getKey())) {
            return this.getBooleanValue(WakfuKeyPreferenceStoreEnum.CHAT_AUTOMATIC_DISACTIVATION);
        }
        if (fieldName.equals(WakfuKeyPreferenceStoreEnum.CENSOR_ACTIVATED.getKey())) {
            return this.getBooleanValue(WakfuKeyPreferenceStoreEnum.CENSOR_ACTIVATED);
        }
        if (fieldName.equals(WakfuKeyPreferenceStoreEnum.MINIMAP_ENABLE.getKey())) {
            return this.getBooleanValue(WakfuKeyPreferenceStoreEnum.MINIMAP_ENABLE);
        }
        if (fieldName.equals(WakfuKeyPreferenceStoreEnum.DISPLAY_STATE_BAR.getKey())) {
            return this.getBooleanValue(WakfuKeyPreferenceStoreEnum.DISPLAY_STATE_BAR);
        }
        if (fieldName.equals(WakfuKeyPreferenceStoreEnum.CHAT_TIME.getKey())) {
            return this.getBooleanValue(WakfuKeyPreferenceStoreEnum.CHAT_TIME);
        }
        if (fieldName.equals(WakfuKeyPreferenceStoreEnum.DISPLAY_HP_BAR_KEY.getKey())) {
            return this.getBooleanValue(WakfuKeyPreferenceStoreEnum.DISPLAY_HP_BAR_KEY);
        }
        if (fieldName.equals(WakfuKeyPreferenceStoreEnum.DISPLAY_TERRITORIES_KEY.getKey())) {
            return this.getBooleanValue(WakfuKeyPreferenceStoreEnum.DISPLAY_TERRITORIES_KEY);
        }
        if (fieldName.equals(WakfuKeyPreferenceStoreEnum.DISPLAY_XP_BAR.getKey())) {
            return this.getBooleanValue(WakfuKeyPreferenceStoreEnum.DISPLAY_XP_BAR);
        }
        if (fieldName.equals(WakfuKeyPreferenceStoreEnum.ADD_SEEDS_TO_SHORTCUT_BAR.getKey())) {
            return this.getBooleanValue(WakfuKeyPreferenceStoreEnum.ADD_SEEDS_TO_SHORTCUT_BAR);
        }
        return super.getFieldValue(fieldName);
    }
    
    @Override
    public void setFieldValue(final String fieldName, final Object value) {
        super.setFieldValue(fieldName, value);
    }
    
    @Override
    public boolean isFieldSynchronisable(final String fieldName) {
        return super.isFieldSynchronisable(fieldName);
    }
    
    @Override
    protected void initializeCharacterDefaultValues() {
        super.initializeCharacterDefaultValues();
        this.setDefault(WakfuKeyPreferenceStoreEnum.NEED_FIRST_LAUNCH_CHARACTER_UPDATE, true);
        this.setDefault(WakfuKeyPreferenceStoreEnum.OPENED_SHORTCUT_BARS_KEY, 0);
        this.setDefault(WakfuKeyPreferenceStoreEnum.DEFAULT_SHORTCUT_BAR_TYPE, ShortCutBarType.WORLD.name());
        this.setDefault(WakfuKeyPreferenceStoreEnum.CURRENT_SPELL_SHORTCUT_BAR_INDEX, 0);
        this.setDefault(WakfuKeyPreferenceStoreEnum.CURRENT_ITEM_SHORTCUT_BAR_INDEX, 0);
        this.setDefault(WakfuKeyPreferenceStoreEnum.BARS_LOCKED_MODE_KEY, true);
        this.setDefault(WakfuKeyPreferenceStoreEnum.SELECTED_LANDMARK_FILTERS, "0;1;2;3;4;5;6;7;8");
        this.setDefault(WakfuKeyPreferenceStoreEnum.DISPLAY_TERRITORIES_KEY, false);
        this.setDefault(WakfuKeyPreferenceStoreEnum.GUILD_DISPLAY_DISCONNECTED_MEMBERS_KEY, true);
        this.setDefault(WakfuKeyPreferenceStoreEnum.CHAT_FADE_KEY, true);
        this.setDefault(WakfuKeyPreferenceStoreEnum.CHAT_AUTOMATIC_DISACTIVATION, false);
        this.setDefault(WakfuKeyPreferenceStoreEnum.CHAT_TEXT_FONT_SIZE_KEY, ChatWindowManager.FontSize.LITTLE.ordinal());
        this.setDefault(WakfuKeyPreferenceStoreEnum.EMOTE_ICONS_ACTIVATED, true);
        this.setDefault(WakfuKeyPreferenceStoreEnum.PET_CAN_BE_PACKED, true);
        this.setDefault(WakfuKeyPreferenceStoreEnum.LAST_CRAFT_SEEN, -1);
        this.setDefault(WakfuKeyPreferenceStoreEnum.HIDE_FIGHT_OCCLUDERS_ACTIVATED_KEY, false);
    }
    
    @Override
    protected void initializeAccountDefaultValues() {
        super.initializeAccountDefaultValues();
        this.setDefault(WakfuKeyPreferenceStoreEnum.RADAR_ZOOM_SCALE_PREFERENCE_KEY, 0.5);
        this.setDefault(WakfuKeyPreferenceStoreEnum.AREA_CHALLENGES_ACTIVATED_KEY, true);
        this.setDefault(WakfuKeyPreferenceStoreEnum.AUTO_LOCK_FIGHTS_KEY, false);
        this.setDefault(WakfuKeyPreferenceStoreEnum.INTERACTION_ON_LEFT_CLICK_KEY, false);
        this.setDefault(WakfuKeyPreferenceStoreEnum.HP_DISPLAY_TYPE_KEY, ProgressText.ProgressBarDisplayValue.CURRENT_VALUE.ordinal());
        this.setDefault(WakfuKeyPreferenceStoreEnum.DEFAULT_SPLIT_MODE_KEY, true);
        this.setDefault(WakfuKeyPreferenceStoreEnum.OVER_HEAD_DELAY_KEY, 0);
        this.setDefault(WakfuKeyPreferenceStoreEnum.AUTO_SWITCH_BARS_MODE_KEY, true);
        this.setDefault(WakfuKeyPreferenceStoreEnum.DISPLAY_HP_BAR_KEY, false);
        this.setDefault(WakfuKeyPreferenceStoreEnum.CENSOR_ACTIVATED, true);
        this.setDefault(WakfuKeyPreferenceStoreEnum.LAST_SELECTED_CHARACTER_NAME, "");
        this.setDefault(WakfuKeyPreferenceStoreEnum.CHAT_TIME, false);
        this.setDefault(WakfuKeyPreferenceStoreEnum.MINIMAP_ENABLE, false);
        this.setDefault(WakfuKeyPreferenceStoreEnum.DISPLAY_STATE_BAR, false);
        this.setDefault(WakfuKeyPreferenceStoreEnum.FOLLOWED_QUESTS_DISPLAY, true);
        this.setDefault(WakfuKeyPreferenceStoreEnum.TIPS_ACTIVATED, true);
        this.setDefault(WakfuKeyPreferenceStoreEnum.DISPLAY_XP_BAR, true);
        this.setDefault(WakfuKeyPreferenceStoreEnum.ADD_SEEDS_TO_SHORTCUT_BAR, true);
    }
    
    @Override
    public void setDefaultPreferenceStore(final PreferenceStore store) {
        super.setDefaultPreferenceStore(store);
        if (store != null) {
            store.addPreferenceStoreLoadListener(this);
        }
    }
    
    @Override
    public void setAccountPreferenceStore(final PreferenceStore store) {
        super.setAccountPreferenceStore(store);
        if (store != null) {
            store.addPreferenceStoreLoadListener(this);
        }
    }
    
    @Override
    public void setCharacterPreferenceStore(final PreferenceStore store) {
        super.setCharacterPreferenceStore(store);
        if (store != null) {
            store.addPreferenceStoreLoadListener(this);
        }
    }
    
    @Override
    protected void initializeDefaultValues() {
        super.initializeDefaultValues();
        this.setDefault(WakfuKeyPreferenceStoreEnum.LAST_LOGIN_PREFERENCE_KEY, "");
        this.setDefault(WakfuKeyPreferenceStoreEnum.LAST_PROXY_GROUP_INDEX_PREFERENCE_KEY, 1);
        this.setDefault(WakfuKeyPreferenceStoreEnum.REMEMBER_LAST_LOGIN_PREFERENCE_KEY, true);
        this.setDefault(WakfuKeyPreferenceStoreEnum.VSYNC_ACTIVATED_KEY, true);
        this.setDefault(WakfuKeyPreferenceStoreEnum.ENABLE_RUNNING_RADIUS, true);
        this.setDefault(WakfuKeyPreferenceStoreEnum.GRAPHICAL_PRESETS_KEY, WakfuGraphicalPresets.Level.CUSTOM.getId());
        this.setDefault(WakfuKeyPreferenceStoreEnum.ALPHA_MASK_ACTIVATED_KEY, true);
        this.setDefault(WakfuKeyPreferenceStoreEnum.METEO_EFFECT_ACTIVATED_KEY, true);
        this.setDefault(KeyPreferenceStoreEnum.LOD_LEVEL_KEY, 2);
        this.setDefault(KeyPreferenceStoreEnum.FIGHT_LOD_LEVEL_KEY, 2);
    }
    
    public void restoreChatOptions() {
        this.setValue(WakfuKeyPreferenceStoreEnum.CHAT_FADE_KEY, this.getDefaultBoolean(WakfuKeyPreferenceStoreEnum.CHAT_FADE_KEY));
        this.setValue(WakfuKeyPreferenceStoreEnum.CHAT_TEXT_FONT_SIZE_KEY, this.getIntValue(WakfuKeyPreferenceStoreEnum.CHAT_TEXT_FONT_SIZE_KEY));
    }
    
    @Override
    public void onPreferenceStoreLoaded(final PreferenceStore store) {
        this.checkVersion(store);
    }
    
    private void checkVersion(final PreferenceStore store) {
        final int previousVersion = store.getInt(WakfuKeyPreferenceStoreEnum.VERSION_PREFERENCE_KEY.getKey());
        if (previousVersion < 11) {
            final URL resource = this.getClass().getResource("/com/ankamagames/wakfu/client/resources/wakfuPreferencesActions.xml");
            WakfuGamesPreferencesChecker.check(resource, this, previousVersion);
            store.setValue(WakfuKeyPreferenceStoreEnum.VERSION_PREFERENCE_KEY.getKey(), 11);
            try {
                store.save();
            }
            catch (IOException e) {
                WakfuGamePreferences.m_logger.warn((Object)"Probl\u00e8me \u00e0 la sauvegarde", (Throwable)e);
            }
        }
    }
    
    private void deleteUserFiles(final String path) {
        final File file = new File(path);
        final File[] arr$;
        final File[] files = arr$ = file.listFiles(this.m_fileFilter);
        for (final File f : arr$) {
            if (f.isDirectory()) {
                this.deleteUserFiles(f.getPath());
            }
            else {
                f.delete();
            }
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)WakfuGamePreferences.class);
        FIELDS = new String[] { "chatWindowModulationColor" };
    }
}
