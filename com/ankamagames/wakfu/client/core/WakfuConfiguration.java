package com.ankamagames.wakfu.client.core;

import org.apache.log4j.*;
import gnu.trove.*;
import com.ankamagames.framework.fileFormat.properties.*;
import com.ankamagames.framework.fileFormat.io.*;
import org.jetbrains.annotations.*;
import com.ankamagames.framework.java.util.*;
import java.net.*;
import java.io.*;

public class WakfuConfiguration extends PropertiesReaderWriter
{
    private static final float PERCENT_FLOAT = 100.0f;
    private static final Logger m_logger;
    public static final String CONFIGURATION_FILE = "config.properties";
    private static final String WORLD_TRANSFORM_PATH = "worldTransformPath";
    private static final String FILE_INDEXERS_PATH = "fileIndexers";
    private static final String INDEXED_CONTENT_PREFIX = "indexedContentsPrefix";
    public static final String ANM_EQUIPMENT_PATH = "ANMEquipmentPath";
    public static final String ANM_RESOURCE_PATH = "ANMResourcePath";
    public static final String PLAYER_GFX_PATH = "playerGfxPath";
    public static final String NPC_GFX_PATH = "npcGfxPath";
    public static final String PET_GFX_PATH = "petGfxPath";
    public static final String GFX_CONFIG_FILE = "gfxConfigFile";
    public static final String ANM_INTERACTIVE_PATH = "ANMInteractiveElementPath";
    public static final String ANM_DYNAMIC_PATH = "ANMDynamicElementPath";
    public static final String ANM_INDEX_FILE = "ANMIndexFile";
    public static final String ANM_GUI_PATH = "ANMGUIPath";
    public static final String I18N_PATH = "i18nPath";
    public static final String DIALOGS_PATH = "dialogsPath";
    public static final String USE_XML_THEME = "useXmlTheme";
    public static final String USE_COMPILED_LUA = "useCompiledLua";
    public static final String LANG_ICONS_PATH = "langIconsPath";
    public static final String WORLD_POSITION_MARKER_APS_PATH = "worldPositionMarkerApsPath";
    public static final String LAND_MARK_MAP_PATH = "mapsPoiPath";
    public static final String NEW_LAND_MARK_PATH = "newPoiPath";
    public static final String ZAAP_LAND_MARK_PATH = "zaapPoiPath";
    public static final String DRAGO_LAND_MARK_PATH = "dragoPoiPath";
    public static final String CANNON_LAND_MARK_PATH = "cannonPoiPath";
    public static final String BOAT_LAND_MARK_PATH = "boatPoiPath";
    public static final String MAPS_TPLG_COORD_DEF = "mapsTplgCoord";
    public static final String MAPS_GFX_COORD_DEF = "mapsGfxCoord";
    public static final String MAPS_GFX_PATH = "mapsGfxPath";
    public static final String MAPS_LIGHT_PATH = "mapsLightPath";
    public static final String MAPS_TOPOLOGY_PATH = "mapsTopologyPath";
    public static final String MAPS_ENVIRONMENT_PATH = "mapsEnvironmentPath";
    public static final String MAPS_AMBIENCE_DATA_PATH = "mapsAmbienceDataPath";
    public static final String WORLD_INFO_FILE = "worldInfoFile";
    public static final String AMBIENCE_BANK_FILE = "ambienceBankFile";
    public static final String GRAPHICAL_AMBIENCE_FILE = "graphicalAmbienceFile";
    public static final String GRAPHICAL_AMBIENCE_EFFECT_FILE = "graphicalAmbienceEffectFile";
    public static final String GFX_PATH = "gfxPath";
    public static final String PLAY_LIST_BANK_FILE = "playListBankFile";
    public static final String SOUND_SOURCE_FLAVOR = "soundSourceFlavor";
    public static final String USE_LUA_AUDIO = "useLuaAudio";
    public static final String PARTICLES_AUDIO_FILE = "particlesAudioFile";
    public static final String ANIMATED_ELEMENTS_AUDIO_FILE = "animatedElementsAudioFile";
    public static final String DYNAMIC_SOUND_AMBIANCE_FILE = "dynamicSoundAmbianceFile";
    public static final String SFX_SOUND_PATH = "sfxSoundPath";
    public static final String AMB2D_PATH = "amb2DPath";
    public static final String AMB3D_PATH = "amb3DPath";
    public static final String MUSIC_PATH = "musicPath";
    public static final String VOICES_SOUND_PATH = "voicesPath";
    public static final String FIGHT_SOUND_PATH = "fightSoundPath";
    public static final String GUI_SFX_PATH = "guiSoundPath";
    public static final String FOLEYS_SOUND_PATH = "foleysSoundPath";
    public static final String PARTICLES_SOUND_PATH = "particlesSoundPath";
    public static final String SHADERS_PATH = "shadersPath";
    public static final String VIDEOS_PATH = "videosPath";
    public static final String HIGHLIGHT_GFX_PATH = "highLightGfxPath";
    public static final String PARTICLE_PATH = "particlePath";
    public static final String SCRIPT_PATH = "scriptPath";
    public static final String SCRIPT_FUNCTION_LIB_FILE = "scriptFunctionLibraryFile";
    public static final String DEFAULT_ICON_PATH = "defaultIconPath";
    public static final String SPELLS_ICONS_PATH = "spellsIconsPath";
    public static final String GROUP_DIFFICULTY_ICONS_PATH = "groupDifficultyIconsPath";
    public static final String ECOSYSTEM_DIFFICULTY_ICONS_PATH = "ecosystemDifficultyIconsPath";
    public static final String ECOSYSTEM_PROTECTED_ICONS_PATH = "ecosystemProtectedIconPath";
    public static final String OSAMODAS_MONSTER_ICON_PATH = "osamodasMonsterIconPath";
    public static final String GROUP_DIFFICULTY_CHALLENGE_ICON_PATH = "groupDifficultyChallengeIconPath";
    public static final String FRESCO_PATH = "frescoPath";
    public static final String ITEMS_ICONS_PATH = "itemsIconsPath";
    public static final String ELEMENTS_ICONS_PATH = "elementsIconsPath";
    public static final String ELEMENTS_SMALL_ICONS_PATH = "elementsSmallIconsPath";
    public static final String TARGET_EFFECT_ICONS_PATH = "targetEffectIconsPath";
    public static final String AREAS_ICONS_PATH = "areasIconsPath";
    public static final String AREAS_BIG_ICONS_PATH = "areasBigIconsPath";
    public static final String STATES_ICONS_PATH = "statesIconsPath";
    public static final String TIME_POINT_BONUS_ICONS_PATH = "timePointBonusIconsPath";
    public static final String EFFECT_AREAS_ICONS_PATH = "effectAreasIconsPath";
    public static final String BREED_SMALL_BACKGROUNDS_PATH = "breedSmallBackgroundsPath";
    public static final String BREED_BIG_BACKGROUNDS_PATH = "breedBigBackgroundsPath";
    public static final String BREED_ICON_PATH = "breedIconPath";
    public static final String POPUP_ICON_PATH = "popupIconPath";
    public static final String BREED_SMALL_ICON_PATH = "breedSmallIconPath";
    public static final String BREED_CONTACT_LIST_ILLUSTRATION_ICON_PATH = "breedContactListIllustrationPath";
    public static final String BREED_ILLUSTRATION_PATH = "breedIllustrationPath";
    public static final String BREED_PORTRAIT_ILLUSTRATION_PATH = "breedPortraitIllustrationPath";
    public static final String BREED_CHARACTER_CHOICE_ILLUSTRATION_PATH = "breedCharacterChoiceIllustrationPath";
    public static final String MONSTER_ILLUSTRATION_PATH = "monsterIllustrationPath";
    public static final String DEFAULT_MONSTER_ILLUSTRATION_PATH = "defaultMonsterIllustrationPath";
    public static final String DEFAULT_SMALL_MONSTER_ILLUSTRATION_PATH = "defaultSmallMonsterIllustrationPath";
    public static final String BACKGROUND_TYPE_ICON_PATH = "shortcutBackgroundPath";
    public static final String SKILLS_ICONS_PATH = "skillsIconsPath";
    public static final String CHALLENGE_CATEGORY_ICONS_PATH = "challengeCategoryIconsPath";
    public static final String CHALLENGE_USER_TYPE_ICONS_PATH = "challengeUserTypeIconsPath";
    public static final String CHALLENGE_RESULT_QUALITY_ICONS_PATH = "challengeResultQualityIconsPath";
    public static final String COMPASS_ICONS_PATH = "compassIconsPath";
    public static final String GUILD_BLAZON_BACKGROUND_PART_PATH = "guildBlazonBackgroundPartPath";
    public static final String GUILD_BLAZON_FOREGROUND_PART_PATH = "guildBlazonForegroundPartPath";
    public static final String GUILD_RANK_ICONS_PATH = "guildRankIconsPath";
    public static final String APTITUDE_ICONS_PATH = "aptitudeIconsPath";
    public static final String DIMENSIONAL_BAG_PRIMARY_GEM_PATH = "dimensionalBagPrimaryGemPath";
    public static final String DIMENSIONAL_BAG_SECONDARY_GEM_PATH = "dimensionalBagSecondaryGemPath";
    public static final String CALENDAR_EVENT_PATH = "calendarEventPath";
    public static final String LOOT_TYPE_ICONS_PATH = "lootTypeIconsPath";
    public static final String WEATHER_ICONS_PATH = "weatherIconsPath";
    public static final String WIND_FORCE_ICONS_PATH = "windForceIconsPath";
    public static final String PROTECTOR_BUFFS_ICON_PATH = "protectorBuffsIconsPath";
    public static final String NATION_FLAG_ICON_PATH = "nationFlagIconsPath";
    public static final String NATION_SELECTION_ICON_PATH = "nationSelectionIconsPath";
    public static final String CHALLENGE_FLYING_IMAGE_PATH = "challengeFlyingImagePath";
    public static final String LAW_FLYING_IMAGE_PATH = "lawFlyingImagePath";
    public static final String CRAFT_PASSPORT_ICONS_PATH = "craftPassportIconsPath";
    public static final String PASSPORT_STAMP_ICONS_PATH = "passportStampIconsPath";
    public static final String EMOTE_ICONS_PATH = "emoteIconsPath";
    public static final String BREED_EMOTE_ICONS_PATH = "breedEmoteIconsPath";
    public static final String EMOTE_PATH = "emotePath";
    public static final String MONSTER_FAMILY_PATH = "monstersFamily";
    public static final String ACHIEVEMENT_CATEGORY_PATH = "achievementCategoryPath";
    public static final String ACHIEVEMENT_PATH = "achievementPath";
    public static final String TITLE_PATH = "titlePath";
    public static final String CURRENCY_ICON_URL = "currencyIconUrl";
    public static final String BACKGROUND_DISPLAY_PATH = "backgroundDisplayPath";
    public static final String BACKGROUND_DISPLAY_BACKGROUND_PATH = "backgroundDisplayBackgroundPath";
    public static final String INTERACTIVE_DIALOG_PORTRAIT_PATH = "interactiveDialogPortraitPath";
    public static final String TEMPERATURE_INFLUENCE_ICON_URL = "temperatureInfluenceIconUrl";
    public static final String PROTECTOR_SECRET_ICON_URL = "protectorSecretIconUrl";
    public static final String EFFECT_DESC_PLOT_ICON_URL = "effectDescPlotIconUrl";
    public static final String GIFT_TYPE_ICON_PATH = "giftTypeIconPath";
    public static final String ZAAP_TYPE_ICON_PATH = "zaapTypeIconPath";
    public static final String GOVERNMENT_RANK_ICON_PATH = "governmentRankIconPath";
    public static final String ITEM_TYPE_ICON_PATH = "itemTypeIconPath";
    public static final String MESSAGE_BOX_ICONS_PATH = "messageBoxIconsPath";
    public static final String GUILD_STORAGE_TYPE_ICONS_PATH = "guildStorageTypeIconsPath";
    public static final String ANTI_ADDICTION_ICONS_PATH = "antiAddictionIconsPath";
    public static final String PVP_RANK_ICONS_PATH = "pvpRankIconsPath";
    public static final String PVP_RANK_PASSPORT_ICONS_PATH = "pvpRankPassportIconsPath";
    public static final String HW_BUILDING_ICON_GREEN_PATH = "hwBuidingIconGreenPath";
    public static final String HW_BUILDING_ICON_ORANGE_PATH = "hwBuidingIconOrangePath";
    public static final String HW_BUILDING_ICON_RED_PATH = "hwBuidingIconRedPath";
    public static final String TEXT_ICONS_PATH = "textIconsPath";
    public static final String ACTIVATE_MAP_PARTICLES = "activateMapParticles";
    public static final String APPLICATION_SKIN_PATH = "appSkinPath";
    public static final String THEME_DIRECTORY = "themeDirectory";
    public static final String THEME_FILE = "themeFile";
    public static final String SHORTCUTS_FILE = "shortcutsFile";
    public static final String TUTORIAL_FILE = "tutorialFile";
    public static final String DAYLIGHT_FILE = "dayLightFile";
    public static final String DEFAULT_SHORTCUTS_FILE = "defaultShortcutsFile";
    public static final String DEFAULT_CHAT_FILE = "defaultChatFile";
    public static final String DEFAULT_DAYLIGHT_FILE = "defaultDayLightFile";
    public static final String SOUND_BANK_FILE = "soundBankFile";
    public static final String REVERB_PRESET_FILE = "reverbPresetFile";
    public static final String ROLLOFF_PRESET_FILE = "rollOffPresetFile";
    public static final String LOWPASS_PRESET_FILE = "lowPassPresetFile";
    public static final String BARKS_FILE = "barksFile";
    public static final String GROUNDS_FILE = "groundsFile";
    public static final String ELEMENTS_FILE = "elementsFile";
    public static final String GROUPS_FILE = "groupsFile";
    public static final String BUILDING_FILE = "buildingFile";
    public static final String BUILDING_IMAGE_PATH = "buildingImagePath";
    public static final String BUILDING_IMAGE_OFFSET_FILE = "buildingImageOffsetFile";
    public static final String BUILDING_MINI_IMAGE_PATH = "buildingMiniImagePath";
    public static final String PARTITION_PATCH_FILE = "partitionPatchFile";
    public static final String PATCH_IMAGE_PATH = "patchImagePath";
    public static final String PATCH_IMAGE_OFFSET_FILE = "patchImageOffsetFile";
    public static final String PATCH_MINI_IMAGE_PATH = "patchMiniImagePath";
    public static final String MAP_DEFINITION_PATH = "mapDefinitionPath";
    public static final String MAP_SCROLL_DECORATOR_PATH = "mapScrollDecoratorPath";
    public static final String FULL_SUB_MAP_PATH = "fullSubMapPath";
    public static final String FULL_MAP_PATH = "fullMapPath";
    public static final String COMPLETE_MAP_PATH = "completeMapPath";
    public static final String COMPLETE_MAP_COORDS_PATH = "completeMapCoordsPath";
    public static final String MINI_MAP_POINT_FILE = "miniMapPointFile";
    public static final String MINI_MAP_POINT_BIG_FILE = "miniMapPointBigFile";
    public static final String COMPASS_POINT_FILE = "compassPointFile";
    public static final String FIGHT_CHALLENGE_ICONS_PATH = "fightChallengeIconsPath";
    public static final String POINTS_OF_INTEREST_ICON_PATH = "pointsOfInterestIconPath";
    public static final String POINTS_OF_INTEREST_DEFAULT_SMALL_ICON_PATH = "pointsOfInterestDefaultSmallIconPath";
    public static final String POINT_OF_INTEREST_PROTECTOR_ICON_PATH = "pointsOfInterestProtectorIconPath";
    public static final String POINT_OF_INTEREST_PROTECTOR_IN_CHAOS_ICON_PATH = "pointsOfInterestProtectorinChaosIconPath";
    public static final String PARTY_MEMBER_POI_PATH = "partyMemberPoiPath";
    public static final String HIGHLIGHT_GFX_DEFAULT_FILE = "highLightGfxDefaultFile";
    public static final String STATIC_DATA_BINARY_STORAGE_DIRECTORY = "contentStaticDataStorageDirectory";
    public static final String STATIC_DATA_BINARY_STORAGE_FILE = "binaryDataFile";
    public static final String MERCHANT_DISPLAY_ICON_PATH = "merchantDisplayIconPath";
    public static final String SOUND_DEVICE = "soundDevice";
    public static final String SOUND_ENABLE = "soundEnable";
    public static final String SOUND_AMBIANCE_ENABLE = "soundAmbianceEnable";
    public static final String AMB2D_MIX_VOLUME = "amb2DMix";
    public static final String AMB3D_MIX_VOLUME = "amb3DMix";
    public static final String GUI_MIX_VOLUME = "guiMix";
    public static final String MUSIC_MIX_VOLUME = "musicMix";
    public static final String SFX_MIX_VOLUME = "sfxMix";
    public static final String FIGHTS_MIX_VOLUME = "fightsMix";
    public static final String VOICES_MIX_VOLUME = "voicesMix";
    public static final String FOLEYS_MIX_VOLUME = "foleysMix";
    public static final String PARTICLES_MIX_VOLUME = "particlesMix";
    private static final String FIGHT_MIX_FADEOUT = "dynamicSpellMixFadeOut";
    private static final String FIGHT_MIX_SPELL_VOLUME = "dynamicSpellMix";
    public static final String CONNECTION_RETRY_COUNT = "connectionRetryCount";
    public static final String CONNECTION_RETRY_DELAY = "connectionRetryDelay";
    public static final String DISPATCH_ADDRESSES = "dispatchAddresses";
    public static final String BUG_REPORT_ACTIVATE = "bugReport.enable";
    public static final String BUG_REPORT_URL = "bugReport.url";
    public static final String WAKFU_WEBSITE_URL = "wakfuWebsiteUrl";
    public static final String WAKFU_WEBSITE_URL_DISPLAY = "wakfuWebsiteUrlDisplay";
    public static final String LINK_STEAM_ACCOUNT_URL = "linkSteamAccountUrl";
    public static final String ACCOUNT_CREATION_URL = "accountCreationUrl";
    public static final String ACCOUNT_VALIDATION_URL = "accountValidationUrl";
    public static final String ANTI_ADDICTION_ACCOUNT_CHECK_URL = "antiAddictionAccountCheckURL";
    public static final String LIKEVN_LOGIN_WEBSITE_URL = "likevnLoginWebsiteUrl";
    public static final String BIGPOINT_LOGIN_WEBSITE_URL = "bigpointLoginWebsiteUrl";
    public static final String BIGPOINT_SHOP_BUY_OGRINES_URL = "bigpointShopBuyOgrinesUrl";
    public static final String RSS_URL = "rssURL";
    public static final String LOGIN_NEWS_URL = "loginNewsURL";
    private static final String DEFAULT_LOGIN_NEWS_URL = "http://www.google.fr";
    public static final String CACHE_DIRECTORY = "cacheDirectory";
    public static final String AUTOLOGIN = "autoLogin";
    public static final String AUTOLOGIN_LOGIN = "autoLogin_login";
    public static final String AUTOLOGIN_PASSWORD = "autoLogin_password";
    public static final String AUTOLOGIN_PROXYGROUP = "autoLogin_proxyGroup";
    public static final String AUTOLOGIN_SELECTCHARACTER = "autoLogin_selectCharacter";
    public static final String CLIMATE_BONUS_ICONS_PATH = "climateBonusIconsPath";
    public static final String WORLD_MAP_ANM_FILE_PATH = "worldMapAnmFilePath";
    public static final String BANNER_ANM_FILE_PATH = "bannerAnmFilePath";
    public static final String BANNER_IMAGE_FILE_PATH = "bannerImageFilePath";
    public static final String FIGHT_BANNER_ANM_FILE_PATH = "fightBannerAnmFilePath";
    public static final String COMPASS_TYPE_PATH = "compassTypePath";
    public static final String DIALOG_CHOICE_TYPE_PATH = "dialogChoiceTypePath";
    public static final String REWARD_TYPE_ICONS_PATH = "rewardTypeIconsPath";
    public static final String LANGUAGE_FORCE = "language.force";
    public static final String KROSMOZ_SOAP_URL = "krosmozSOAPUrl";
    public static final String SHOP_BUY_OGRINES_URL = "shopBuyOgrinesUrl";
    public static final String KROSMOZ_GAME_URL_PREFIX = "krosmoz.gameUrl.";
    public static final String KROSMOZ_GAME_RESOLUTION_PREFIX = "krosmoz.gameResolution.";
    public static final String KROSMOZ_FIGURE_ICONS_PATH = "krosmoz.figureIconsPath";
    public static final String TUTORIAL_ICONS_PATH = "tutorialIconsPath";
    public static final String NATION_LAWS_ICONS_PATH = "nationLawsIconsPath";
    public static final String CAMERA_MIN_ZOOM = "cameraMinZoom";
    public static final String CAMERA_MAX_ZOOM = "cameraMaxZoom";
    public static final String CHARACTER_CREATION_TUTO_FORCE = "characterCreation.tuto.force";
    public static final String RESOLUTION_MIN_WIDTH = "resolution.min.width";
    public static final String RESOLUTION_MIN_HEIGHT = "resolution.min.height";
    public static final String ENABLE_RANDOM_CHARACTER_NAME = "enableRandomCharacterName";
    public static final String ACTIVATE_STUFF_PREVIEW = "activateStuffPreview";
    public static final String RANDOM_NAME_PATH = "randomNamePath";
    public static final String COMPANION_CHARACTER_SHEET_ILLUSTRATION_ICON_PATH = "companionCharacterSheetIllustrationPath";
    public static final String COMPANION_ICON_PATH = "companionIconsPath";
    public static final String COMPANION_LIST_ILLUSTRATION_PATH = "companionListIllustrationsPath";
    public static final String COMPANION_SPELL_INVENTORY_ILLUSTRATION_PATH = "companionSpellInventoryIllustrationsPath";
    public static final String UPDATER_COMMUNICATION_PORT = "UPDATER_COMMUNICATION_PORT";
    public static final String UPDATER_INITIAL_STATE = "UPDATER_INITIAL_STATE";
    private static final WakfuConfiguration m_instance;
    public static final String PARTICLE_EXTENSION = ".xps";
    private final THashSet<String> m_urlNotFound;
    
    public WakfuConfiguration() {
        super();
        this.m_urlNotFound = new THashSet<String>();
    }
    
    public static WakfuConfiguration getInstance() {
        return WakfuConfiguration.m_instance;
    }
    
    public String getDialogsPath() {
        return this.getString("dialogsPath", "");
    }
    
    public boolean load() {
        return this.load("");
    }
    
    @Override
    public boolean load(final String fileName) {
        WakfuConfiguration.m_logger.info((Object)String.format("Chargement de la configuration depuis le fichier : '%s'", fileName));
        final boolean load = super.load((fileName == null || fileName.length() == 0) ? "config.properties" : fileName);
        final boolean multiJarLoaded = this.prepareMultiJarIndex();
        final boolean worldIndexLoaded = this.prepareWorldIndex();
        return load && multiJarLoaded && worldIndexLoaded;
    }
    
    private boolean prepareMultiJarIndex() {
        try {
            return ContentFileHelper.prepare(this.getString("fileIndexers"), this.getString("indexedContentsPrefix"));
        }
        catch (PropertyException e) {
            WakfuConfiguration.m_logger.error((Object)"", (Throwable)e);
            return false;
        }
    }
    
    private boolean prepareWorldIndex() {
        try {
            final String path = this.getString("worldTransformPath");
            return WorldMapFileHelper.prepare(path);
        }
        catch (PropertyException e) {
            WakfuConfiguration.m_logger.warn((Object)(e.getMessage() + "\n(no problemo si lanc\u00e9 depuis les sources)"));
            return true;
        }
    }
    
    public float getFoleysMix() {
        return Math.min(1.0f, Math.max(0.0f, this.getFloat("foleysMix", 100.0f) / 100.0f));
    }
    
    public float getParticleMix() {
        return Math.min(1.0f, Math.max(0.0f, this.getFloat("particlesMix", 100.0f) / 100.0f));
    }
    
    public float getSfxMix() {
        return Math.min(1.0f, Math.max(0.0f, this.getFloat("sfxMix", 100.0f) / 100.0f));
    }
    
    public float getAmb2dMix() {
        return Math.min(1.0f, Math.max(0.0f, this.getFloat("amb2DMix", 100.0f) / 100.0f));
    }
    
    public float getAmb3dMix() {
        return Math.min(1.0f, Math.max(0.0f, this.getFloat("amb3DMix", 100.0f) / 100.0f));
    }
    
    public float getFightMix() {
        return Math.min(1.0f, Math.max(0.0f, this.getFloat("fightsMix", 100.0f) / 100.0f));
    }
    
    public float getGuiMix() {
        return Math.min(1.0f, Math.max(0.0f, this.getFloat("guiMix", 100.0f) / 100.0f));
    }
    
    public float getMusicMix() {
        return Math.min(1.0f, Math.max(0.0f, this.getFloat("musicMix", 100.0f) / 100.0f));
    }
    
    public float getVoicesMix() {
        return Math.min(1.0f, Math.max(0.0f, this.getFloat("voicesMix", 100.0f) / 100.0f));
    }
    
    public float getFightMixFadeOutTime() {
        return this.getFloat("dynamicSpellMixFadeOut", 1.0f);
    }
    
    public float getFightMixTargetGain() {
        return Math.min(1.0f, Math.max(0.0f, this.getFloat("dynamicSpellMix", 100.0f) / 100.0f));
    }
    
    @Nullable
    public String getParticlePath(final int particleId) {
        try {
            return this.getString("particlePath") + particleId + ".xps";
        }
        catch (PropertyException e) {
            WakfuConfiguration.m_logger.warn((Object)e);
            return null;
        }
    }
    
    @Nullable
    public String getIconUrl(final String key, final String defaultKey, final Object... args) {
        try {
            final String url = ContentFileHelper.getPath(this.getString(key), args);
            if (url != null && URLUtils.urlExists(url)) {
                return url;
            }
            if (!this.m_urlNotFound.contains(url)) {
                WakfuConfiguration.m_logger.warn((Object)("Impossible de trouver l'icone d'URL " + url));
                this.m_urlNotFound.add(url);
            }
            if (defaultKey != null) {
                return getContentPath(defaultKey);
            }
            return null;
        }
        catch (PropertyException e) {
            WakfuConfiguration.m_logger.warn((Object)e.getMessage());
            return null;
        }
    }
    
    public String getMonsterIllustrationIconUrl(final int id) {
        return this.getIconUrl("monsterIllustrationPath", "defaultMonsterIllustrationPath", id);
    }
    
    public String getItemSmallIconUrl(final int id) {
        return this.getIconUrl("itemsIconsPath", "defaultIconPath", id);
    }
    
    public String getItemBigIconUrl(final int id) {
        return this.getIconUrl("itemsIconsPath", "defaultIconPath", id);
    }
    
    public String getSpellSmallIcon(final int id) {
        return this.getIconUrl("spellsIconsPath", "defaultIconPath", id);
    }
    
    public String getSpellBigIcon(final int id) {
        return this.getIconUrl("spellsIconsPath", "defaultIconPath", id);
    }
    
    public String getCollectSkillSmallIcon(final int id) {
        return this.getIconUrl("skillsIconsPath", "defaultIconPath", "c" + id);
    }
    
    public String getCollectSkillBigIcon(final int id) {
        return this.getCollectSkillSmallIcon(id);
    }
    
    public String getSkillSmallIcon(final int id) {
        return this.getIconUrl("skillsIconsPath", "defaultIconPath", id);
    }
    
    public String getAptitudeIcon(final int id) {
        return this.getIconUrl("aptitudeIconsPath", "defaultIconPath", id);
    }
    
    public String getCalendarEventIcon(final byte id) {
        return this.getIconUrl("calendarEventPath", "defaultIconPath", id);
    }
    
    public String getFlagIconUrl(final int id) {
        return this.getIconUrl("nationFlagIconsPath", "defaultIconPath", id);
    }
    
    public String getNationSelectionIconUrl(final int id) {
        return this.getIconUrl("nationSelectionIconsPath", "defaultIconPath", id);
    }
    
    public String getDialogChoiceIconUrl(final int id) {
        return this.getIconUrl("dialogChoiceTypePath", "defaultIconPath", id);
    }
    
    public String getEmoteIconUrl(final int id) {
        return this.getIconUrl("emoteIconsPath", null, id);
    }
    
    public String getBreedEmoteIconUrl(final int id) {
        return this.getIconUrl("breedEmoteIconsPath", null, id);
    }
    
    public String getEmoteUrl(final int id) {
        return this.getIconUrl("emotePath", null, id);
    }
    
    public String getAchievementRootCategoryIconUrl(final String id) {
        return this.getIconUrl("achievementCategoryPath", null, id);
    }
    
    public String getAchievementCategoryIconUrl(final int id) {
        return this.getIconUrl("achievementCategoryPath", null, id);
    }
    
    public String getAchievementIconUrl(final int gfxId) {
        return this.getIconUrl("achievementPath", null, gfxId);
    }
    
    public String getTitleIconUrl(final int id) {
        return this.getIconUrl("titlePath", null, new Object[0]);
    }
    
    public String getDisplayBackgroundImage(final int gfxId) {
        return this.getIconUrl("backgroundDisplayPath", null, gfxId);
    }
    
    public String getDisplayBackgroundBackgroundImage(final int gfxId) {
        return this.getIconUrl("backgroundDisplayBackgroundPath", null, gfxId);
    }
    
    public String getInteractiveDialogPortraitIconUrl(final String gfxId) {
        return this.getIconUrl("interactiveDialogPortraitPath", null, gfxId);
    }
    
    public String getTutorialIconUrl(final String gfxId) {
        return this.getIconUrl("tutorialIconsPath", null, gfxId);
    }
    
    public String getNationLawsIconUrl(final long gfxId) {
        return this.getIconUrl("nationLawsIconsPath", null, gfxId);
    }
    
    public String getItemTypeIconUrl(final short id) {
        return this.getIconUrl("itemTypeIconPath", null, id);
    }
    
    @Nullable
    public URL getLoginNewsUrl() {
        String strUrl;
        try {
            strUrl = this.getString("loginNewsURL");
        }
        catch (PropertyException ignored) {
            strUrl = "http://www.google.fr";
        }
        if (strUrl == null) {
            return null;
        }
        final String language = WakfuTranslator.getInstance().getLanguage().getActualLocale().getLanguage().toLowerCase();
        final String formattedStrUrl = String.format(strUrl, language);
        try {
            return ContentFileHelper.getURL(formattedStrUrl);
        }
        catch (MalformedURLException e) {
            WakfuConfiguration.m_logger.error((Object)"While get loginNewsUrl", (Throwable)e);
            return null;
        }
    }
    
    public String getCachePath(final String subPath) {
        String directory;
        try {
            directory = this.getString("cacheDirectory") + File.separatorChar + subPath;
        }
        catch (PropertyException ignored) {
            directory = "./cache/" + subPath;
        }
        return directory;
    }
    
    public static String getContentPath(final String key) throws PropertyException {
        return ContentFileHelper.transformFileName(getInstance().getString(key));
    }
    
    public static String getContentPath(final String key, final Object id) throws PropertyException {
        final String filename = String.format(getInstance().getString(key), id);
        return ContentFileHelper.transformFileName(filename);
    }
    
    public boolean initialUpdateStateIsFullyInstalled() {
        final String initialState = this.getString("UPDATER_INITIAL_STATE", null);
        return "uptodate".equalsIgnoreCase(initialState);
    }
    
    static {
        m_logger = Logger.getLogger((Class)WakfuConfiguration.class);
        m_instance = new WakfuConfiguration();
    }
}
