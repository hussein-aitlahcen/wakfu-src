package com.ankamagames.wakfu.client.sound;

import com.ankamagames.baseImpl.graphics.sound.*;
import com.ankamagames.wakfu.common.game.time.calendar.*;
import com.ankamagames.baseImpl.graphics.core.partitions.*;
import com.ankamagames.baseImpl.graphics.alea.mobile.*;
import com.ankamagames.baseImpl.graphics.script.*;
import com.ankamagames.xulor2.util.sound.*;
import com.ankamagames.framework.sound.helper.*;
import gnu.trove.*;
import com.ankamagames.framework.sound.group.effects.*;
import com.ankamagames.framework.kernel.core.common.collections.lightweight.intKey.*;
import com.ankamagames.framework.sound.util.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.framework.sound.group.defaultSound.*;
import com.ankamagames.wakfu.client.core.game.fight.*;
import java.io.*;
import com.ankamagames.framework.sound.stream.*;
import com.ankamagames.framework.sound.openAL.*;
import com.ankamagames.framework.kernel.core.common.message.scheduler.process.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.core.game.ressource.*;
import com.ankamagames.wakfu.client.sound.ambiance2D.criteria.event.*;
import com.ankamagames.framework.fileFormat.properties.*;
import com.ankamagames.wakfu.client.sound.ambiance2D.criteria.ambience.*;
import com.ankamagames.wakfu.client.*;
import com.ankamagames.wakfu.client.core.world.*;
import com.ankamagames.baseImpl.graphics.alea.environment.*;
import com.ankamagames.framework.sound.group.music.*;
import org.jetbrains.annotations.*;
import com.ankamagames.baseImpl.graphics.core.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.client.core.game.wakfu.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.common.datas.specific.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import java.util.*;
import com.ankamagames.wakfu.client.core.weather.*;
import com.ankamagames.framework.sound.*;
import com.ankamagames.framework.sound.group.field.*;
import com.ankamagames.framework.sound.group.*;
import com.ankamagames.framework.fileFormat.xml.*;
import com.ankamagames.framework.fileFormat.document.*;
import com.ankamagames.wakfu.client.sound.ambiance2D.*;
import com.ankamagames.baseImpl.graphics.isometric.*;
import com.ankamagames.baseImpl.common.clientAndServer.world.*;
import org.apache.log4j.*;

public class WakfuSoundManager extends BaseSoundManager implements WakfuGameCalendar.DayChangeListener, PartitionChangedListener<PathMobile, LocalPartition>
{
    public static final int SOUND_LEVEL_PLAY_LOW_LEVEL_SOUNDS_ONLY = 0;
    public static final int SOUND_LEVEL_PLAY_ALL_SOUNDS_IN_FIGHT = 10;
    public static final int SOUND_LEVEL_PLAY_ALL_SOUNDS = 20;
    public static final float DEFAULT_GROUP_GAIN = 1.0f;
    public static final int MUSIC_INTERFACE = 200000;
    public static final int MUSIC_FIGHT = 200200;
    public static final int MUSIC_FIGHT_END_VICTORY = 290998;
    public static final int MUSIC_FIGHT_END_LOSS = 290999;
    public static final int MUSIC_CHALLENGE = 290997;
    public static final int NO_MUSIC = 0;
    public static final int SFX_DEATH_SOUND = 15384;
    public static final int GUI_LOADING = 600001;
    public static final int GUI_OK = 600002;
    public static final int GUI_GO = 600003;
    public static final int GUI_BUTTON_CLICK = 600004;
    public static final int GUI_TOGGLE_BUTTON_SELECT = 600006;
    public static final int GUI_TOGGLE_BUTTON_UNSELECT = 600007;
    public static final int GUI_CLOSE_WINDOW = 600008;
    public static final int GUI_TAB = 600009;
    public static final int GUI_ROLLOVER = 600010;
    public static final int GUI_ALERT = 600011;
    public static final int GUI_SHOW_WINDOW = 600012;
    public static final int GUI_HIDE_WINDOW = 600013;
    public static final int GUI_SCROLL_UP = 600014;
    public static final int GUI_SCROLL_DOWN = 600015;
    public static final int GUI_OPEN_DOOR = 600016;
    public static final int GUI_CLOSE_DOOR = 600017;
    public static final int GUI_WINDOW_FADE_OUT = 600018;
    public static final int GUI_WINDOW_FADE_IN = 600019;
    public static final int GUI_HEART_DISPLAY = 600020;
    public static final int GUI_SHOW_MAP = 600021;
    public static final int GUI_HIDE_MAP = 600022;
    public static final int GUI_SHOW_MENU = 600023;
    public static final int GUI_HIDE_MENU = 600024;
    public static final int GUI_SOUND_TEST = 600025;
    public static final int GUI_SHOW_EQUIPMENT = 600026;
    public static final int GUI_HIDE_EQUIPMENT = 600027;
    public static final int GUI_EQUIP_BOOTS = 600028;
    public static final int GUI_EQUIP_ARMS = 600029;
    public static final int GUI_EQUIP_CLOTH = 600030;
    public static final int GUI_EQUIP_NECK = 600036;
    public static final int GUI_EQUIP_ACCESSORY = 600037;
    public static final int GUI_EQUIP_WEAPON = 600038;
    public static final int GUI_EQUIP_HAND_OBJECT = 600039;
    public static final int GUI_STORE_OBJECT = 600040;
    public static final int GUI_DROP_OBJECT = 600043;
    public static final int GUI_ROTATE_ITEM = 600044;
    public static final int GUI_SHOW_ACHIEVEMENTS = 600045;
    public static final int GUI_HIDE_ACHIEVEMENTS = 600046;
    public static final int GUI_DROP_VALIDATION_SPELLS = 600053;
    public static final int GUI_SHOW_CHARACTER_SHEET = 600054;
    public static final int GUI_SHOW_CRAFT = 600057;
    public static final int GUI_HIDE_CRAFT = 600058;
    public static final int GUI_DRAG_VALIDATION_SPELLS = 600059;
    public static final int GUI_POPUP = 600060;
    public static final int GUI_LEVEL_UP = 600062;
    public static final int GUI_RESURRECTION = 600063;
    public static final int GUI_BIND_SOUL_TO_PHOENIX = 600064;
    public static final int GUI_DIMENSIONAL_BAG_IN = 600065;
    public static final int GUI_DIMENSIONAL_BAG_OUT = 600066;
    public static final int GUI_FIGHT_IN = 600067;
    public static final int GUI_FIGHT_OUT = 600068;
    public static final int GUI_FIGHT_END_TURN = 600069;
    public static final int GUI_FIGHT_READY = 600070;
    public static final int GUI_FIGHT_CHANGE_START_POSITION = 600071;
    public static final int GUI_MRU_DISPLAY = 600072;
    public static final int GUI_ACHIEVEMENT_SUB_OBJECTIVE_COMPLETED = 600074;
    public static final int GUI_ACHIEVEMENT_COMPLETED = 600075;
    public static final int GUI_FIGHT_START_PLAYER_TURN = 600077;
    public static final int GUI_FIGHT_START_OTHER_FIGHTER_TURN = 600078;
    public static final int GUI_MESSAGE_BOX_OK_BUTTON = 600082;
    public static final int GUI_MESSAGE_BOX_CANCEL_BUTTON = 600083;
    public static final int GUI_POLITIC_MESSAGE = 600109;
    public static final int GUI_SHOW_SPELLS = 600114;
    public static final int GUI_SHOW_TUTORIAL = 600195;
    public static final int GUI_COMMUNITY_BAR_DISPLAY = 600116;
    public static final int GUI_EXPAND = 600117;
    public static final int GUI_UNEXPAND = 600118;
    public static final int GUI_MAP_ADD_NOTE = 600119;
    public static final int GUI_BACKGROUND_DISPLAY_OPEN = 600120;
    public static final int GUI_BACKGROUND_DISPLAY_CLOSE = 600121;
    public static final int GUI_CRITICAL_HIT = 600122;
    public static final int GUI_TIME_POINT_BONUS_POPUP = 600123;
    public static final int GUI_TIME_POINT_BONUS_CHOSEN = 600124;
    public static final int GUI_FAIL = 600125;
    public static final int GUI_TIMER_LOOP = 600126;
    public static final int GUI_CHALLENGE_PROPOSED = 600127;
    public static final int GUI_CHALLENGE_COUNTDOWN = 600128;
    public static final int GUI_CHALLENGE_COUNTDOWN_END = 600129;
    public static final int GUI_CHALLENGE_VICTORY = 600130;
    public static final int GUI_CHALLENGE_DEFEAT = 600131;
    public static final int GUI_NATION_VOTE_START = 600132;
    public static final int GUI_GOVERNOR_ELECTED = 600133;
    public static final int GUI_ARCADE_DUNGEON_SURVIVAL = 600134;
    public static final int GUI_ARCADE_DUNGEON_FIGHT_START = 600136;
    public static final int GUI_ARCADE_DUNGEON_RESULT_OPEN = 600137;
    public static final int GUI_CHALLENGE_END = 600138;
    public static final int GUI_CITIZEN_POINT_GAIN = 600139;
    public static final int GUI_CITIZEN_POINT_LOSS = 600140;
    public static final int GUI_DISPLAY_ECOSYSTEM_FRAME = 600141;
    public static final int GUI_DIMENSIONAL_BAG_UNLOCK = 600142;
    public static final int GUI_DIMENSIONAL_BAG_LOCK = 600143;
    public static final int GUI_CHALLENGE_BANNER = 600144;
    public static final int GUI_PRIVATE_MESSAGE = 600145;
    public static final int GUI_SCORE_ROLL = 600146;
    public static final int GUI_ARCADE_DUNGEON_ROUND_START = 600147;
    public static final int GUI_GAZETTE_OPEN = 600148;
    public static final int GUI_GAZETTE_CLOSE = 600149;
    public static final int GUI_BLIND_BOX_ROLL = 600152;
    public static final int GUI_BLIND_BOX_ROLL_FINISH_1 = 600153;
    public static final int GUI_BLIND_BOX_ROLL_FINISH_2 = 600154;
    public static final int GUI_BLIND_BOX_ROLL_FINISH_3 = 600155;
    public static final int GUI_BLIND_BOX_ROLL_WIN = 600156;
    public static final int GUI_FLIP_IMAGES = 600158;
    public static final int GUI_HAVEN_WORLD_EDITOR_USE_PARTITION_TOOL = 600182;
    public static final int GUI_HAVEN_WORLD_EDITOR_ROLLOVER_ELEMENT = 600193;
    public static final int GUI_PAPER_MAP_AMBIANCE = 600197;
    private static final boolean MUSIC_USE_STREAM = false;
    private boolean m_enableInventoryEventSounds;
    private final SoundFunctionsLibrary.SoundFunctionsLibraryDelegate m_soundLibraryDelegate;
    private final XulorSoundManagementDelegate m_xulorDelegate;
    private SoundInitializer m_initializer;
    private boolean m_ambianceEnabled;
    private final SoundContainerManager m_soundContainerManager;
    private final WakfuBarkManager m_barkManager;
    private final WakfuGroundSoundManager m_groundSoundManager;
    private static final WakfuSoundManager m_instance;
    private PakAudioResourceHelper m_helper;
    private boolean m_inWorld;
    private boolean m_isInFight;
    private boolean m_isInForcedMusic;
    private long m_forcedMusicId;
    private long m_musicFightId;
    private float m_musicFightGain;
    private long m_musicBossFightId;
    private float m_musicBossFightGain;
    private long m_previousAmbiance;
    private float m_previousAmbianceGain;
    private short m_currentPlayListId;
    private boolean m_needsToApplyPlayList;
    private Fader m_currentMusicFader;
    private Fader m_currentAmbianceFader;
    private Fader m_currentGUIFader;
    private AudioSource m_loadingSource;
    private TIntObjectHashMap<ReverbParameter> m_reverbPresets;
    private TIntObjectHashMap<LowPassParameter> m_lowPassPresets;
    private IntObjectLightWeightMap<RollOffParameter> m_rollOffPresets;
    
    public static WakfuSoundManager getInstance() {
        return WakfuSoundManager.m_instance;
    }
    
    private WakfuSoundManager() {
        super();
        this.m_enableInventoryEventSounds = true;
        this.m_soundLibraryDelegate = new SoundFunctionsLibrary.SoundFunctionsLibraryDelegate() {
            private final ArrayList<GameSoundGroup> m_groups = new ArrayList<GameSoundGroup>();
            private final ArrayList<AudioSourceRequest> m_requests = new ArrayList<AudioSourceRequest>();
            
            {
                this.m_groups.add(GameSoundGroup.SOUND_AMB_2D);
                this.m_groups.add(GameSoundGroup.SOUND_AMB_3D);
                this.m_groups.add(GameSoundGroup.SFX);
                this.m_groups.add(GameSoundGroup.VOICES);
                this.m_groups.add(GameSoundGroup.SOUND_FIGHT);
                this.m_groups.add(GameSoundGroup.PARTICLES);
                this.m_groups.add(GameSoundGroup.FOLEYS);
                this.m_groups.add(GameSoundGroup.GUI);
            }
            
            private GameSoundGroup getGroupFromPrefix(final long soundPrefix) {
                for (int i = 0, size = this.m_groups.size(); i < size; ++i) {
                    final GameSoundGroup testGroup = this.m_groups.get(i);
                    if (testGroup.hasPrefixId(soundPrefix)) {
                        return testGroup;
                    }
                }
                return null;
            }
            
            @Override
            public AudioSourceDefinition playSound(final long soundId, float gain, final int playCount, final long endDate, final long fadeOutDate, final int fightId) {
                if (!WakfuSoundManager.this.isRunning()) {
                    return null;
                }
                final long prefix = SoundGroupUtils.getSoundPrefix(soundId);
                final GameSoundGroup group = this.getGroupFromPrefix(prefix);
                if (group == null) {
                    return null;
                }
                final DefaultSourceGroup audioSourceGroup = group.getDefaultGroup();
                if (!audioSourceGroup.canPrepareSounds()) {
                    return null;
                }
                final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
                if (localPlayer != null && group.isWorldSoundGroup()) {
                    final Fight fight = localPlayer.getCurrentOrObservedFight();
                    if (prefix != 530L) {
                        if (fight == null) {
                            gain *= ((fightId <= 0) ? 1.0f : 0.25f);
                        }
                        else {
                            if (fight.getId() != fightId) {
                                return null;
                            }
                            if (prefix == 111L) {
                                gain *= WakfuSoundManager.this.m_groundSoundManager.getFightFspGain();
                            }
                        }
                    }
                }
                gain = MathHelper.clamp(gain, 0.0f, 1.0f);
                final AudioSourceRequest request = new AudioSourceRequest(AudioSourceManager.getInstance().generateUID(), soundId, gain, playCount, endDate, fadeOutDate, fightId);
                synchronized (this.m_requests) {
                    this.m_requests.add(request);
                }
                return request;
            }
            
            public AudioSourceDefinition playSound(final long soundId, final float gain, final int playCount, final long endDate, final long fadeOutDate, final int fightId, final ObservedSource observed, final int rollOffPresetsId) {
                return this.playSound(soundId, gain, playCount, endDate, fadeOutDate, fightId, observed, rollOffPresetsId, true);
            }
            
            @Override
            public AudioSourceDefinition playSound(final long soundId, float gain, final int playCount, final long endDate, final long fadeOutDate, final int fightId, final ObservedSource observed, final int rollOffPresetsId, final boolean autoRelease) {
                if (!WakfuSoundManager.this.isRunning()) {
                    return null;
                }
                final long prefix = SoundGroupUtils.getSoundPrefix(soundId);
                final GameSoundGroup group = this.getGroupFromPrefix(prefix);
                if (group == null) {
                    return null;
                }
                final FieldSourceGroup posGroup = group.getFieldSourceGroup();
                if (!posGroup.canPrepareSounds()) {
                    return null;
                }
                final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
                if (group.isWorldSoundGroup()) {
                    final Fight fight = (localPlayer != null) ? localPlayer.getCurrentOrObservedFight() : null;
                    if (prefix != 530L) {
                        if (fight == null) {
                            gain *= ((fightId <= 0) ? 1.0f : 0.25f);
                        }
                        else {
                            if (fight.getId() != fightId) {
                                return null;
                            }
                            if (prefix == 111L) {
                                gain *= WakfuSoundManager.this.m_groundSoundManager.getFightFspGain();
                            }
                        }
                    }
                }
                gain = Math.max(Math.min(1.0f, gain), 0.0f);
                final AudioSourceRequest request = new AudioSourceRequest(AudioSourceManager.getInstance().generateUID(), soundId, gain, playCount, endDate, fadeOutDate, fightId, observed, rollOffPresetsId, autoRelease);
                synchronized (this.m_requests) {
                    this.m_requests.add(request);
                }
                return request;
            }
            
            @Override
            public void playForcedMusic(final long soundId) {
                WakfuSoundManager.getInstance().startForcedMusic(soundId);
            }
            
            @Override
            public void stopForcedMusic() {
                WakfuSoundManager.getInstance().stopForcedMusic();
            }
            
            private void handleSoundRequest(final AudioSourceRequest request) {
                final long soundId = request.getSoundId();
                final long prefix = SoundGroupUtils.getSoundPrefix(soundId);
                final GameSoundGroup group = this.getGroupFromPrefix(prefix);
                if (group == null) {
                    return;
                }
                final DefaultSourceGroup audioSourceGroup = group.getDefaultGroup();
                if (!audioSourceGroup.canPrepareSounds()) {
                    return;
                }
                AudioStreamProvider asp;
                try {
                    asp = audioSourceGroup.getHelper().fromId(soundId);
                }
                catch (IOException e) {
                    WakfuSoundManager.m_logger.debug((Object)("Impossible de charger le son d'id " + soundId));
                    return;
                }
                if (asp == null) {
                    WakfuSoundManager.m_logger.debug((Object)("Impossible de pr\u00e9parer le son d'id " + soundId));
                    return;
                }
                final AudioSource source = audioSourceGroup.prepareSound(asp, request.getSoundUID());
                if (source == null) {
                    return;
                }
                source.setGain(request.getGain());
                final int playCount = request.getPlayCount();
                if (playCount == 0) {
                    source.setLoop(true);
                }
                else if (playCount > 1) {
                    source.setRepeatCount(playCount);
                }
                final long endDate = request.getEndDate();
                if (endDate != -1L) {
                    source.setEndDate(endDate);
                }
                final long fadeOutDate = request.getFadeOutDate();
                if (fadeOutDate != -1L) {
                    source.setFadeOutDate(fadeOutDate);
                }
                audioSourceGroup.addSource(source);
            }
            
            private void handlePositionedSoundRequest(final AudioSourceRequest request) {
                final long soundId = request.getSoundId();
                final long prefix = SoundGroupUtils.getSoundPrefix(soundId);
                final GameSoundGroup group = this.getGroupFromPrefix(prefix);
                if (group == null) {
                    return;
                }
                final FieldSourceGroup posGroup = group.getFieldSourceGroup();
                if (!posGroup.canPrepareSounds()) {
                    return;
                }
                AudioStreamProvider asp;
                try {
                    asp = posGroup.getHelper().fromId(soundId);
                }
                catch (IOException e2) {
                    WakfuSoundManager.m_logger.debug((Object)("Impossible de charger le son d'id " + soundId));
                    return;
                }
                if (asp == null) {
                    WakfuSoundManager.m_logger.debug((Object)("Impossible de pr\u00e9parer le son d'id " + soundId));
                    return;
                }
                final int rollOffPresetsId = request.getRollOffPresetsId();
                final RollOffParameter rollOff = WakfuSoundManager.this.getRollOffParameter(rollOffPresetsId);
                if (rollOff == null) {
                    WakfuSoundManager.m_logger.debug((Object)"Les rollOffs n'ont pas \u00e9t\u00e9 initialis\u00e9s");
                    return;
                }
                final float gain = request.getGain();
                final ObservedSource observed = request.getObserved();
                try {
                    posGroup.addSource(asp, gain, observed, rollOff.getRefDistance(), rollOff.getMaxDistance(), rollOff.getRollOffFactor(), 0, 0, request.getPlayCount() == 0, false, request.isAutoRelease(), 0.0f, request.getSoundUID());
                }
                catch (Exception e) {
                    WakfuSoundManager.m_logger.debug((Object)"Exception lev\u00e9e lors de la cr\u00e9ation d'une source positionn\u00e9e", (Throwable)e);
                }
            }
            
            @Override
            public void processSoundRequests() {
                synchronized (this.m_requests) {
                    for (int i = 0, size = this.m_requests.size(); i < size; ++i) {
                        final AudioSourceRequest request = this.m_requests.get(i);
                        if (request.getObserved() == null) {
                            this.handleSoundRequest(request);
                        }
                        else {
                            this.handlePositionedSoundRequest(request);
                        }
                    }
                    this.m_requests.clear();
                }
            }
            
            @Override
            public void stopSound(final long soundId, final AudioSource source) {
                if (source != null) {
                    if (source.isReleased()) {
                        source.stopAndRelease();
                    }
                    else {
                        source.fade(0.0f, 300.0f);
                        source.setStopOnNullGain(true);
                    }
                }
                else {
                    synchronized (this.m_requests) {
                        for (int i = this.m_requests.size() - 1; i >= 0; --i) {
                            final AudioSourceRequest request = this.m_requests.get(i);
                            if (request.getSoundUID() == soundId) {
                                this.m_requests.remove(i);
                                break;
                            }
                        }
                    }
                }
            }
            
            @Override
            public void resetLinkerMix() {
                if (!WakfuSoundManager.this.isRunning()) {
                    return;
                }
                for (int i = 0, size = this.m_groups.size(); i < size; ++i) {
                    final DefaultSourceGroup group = this.m_groups.get(i).getDefaultGroup();
                    if (group != null) {
                        group.resetLinkerMix();
                    }
                }
            }
            
            @Override
            public void setLinkerMix(final float targetGain, final float fadeOutTime) {
                if (!WakfuSoundManager.this.isRunning()) {
                    return;
                }
                for (int i = 0, size = this.m_groups.size(); i < size; ++i) {
                    final DefaultSourceGroup group = this.m_groups.get(i).getDefaultGroup();
                    if (group != null) {
                        group.setLinkerTargetGain(targetGain);
                        if (fadeOutTime != -1.0f) {
                            group.setLinkerFadeOutTime(fadeOutTime);
                        }
                    }
                }
            }
        };
        this.m_xulorDelegate = new XulorSoundDelegate(this);
        this.m_soundContainerManager = new SoundContainerManager();
        this.m_barkManager = new WakfuBarkManager();
        this.m_groundSoundManager = new WakfuGroundSoundManager();
        this.m_helper = new PakAudioResourceHelper(new String[0]);
        this.m_musicFightId = 200200L;
        this.m_musicFightGain = 1.0f;
        this.m_musicBossFightId = 200200L;
        this.m_musicBossFightGain = 1.0f;
        this.m_currentPlayListId = -1;
        SoundManager.setInstance(this);
    }
    
    private Fader fade(final byte type, final Fader currentFader, final float ratio, final int duration) {
        float currentRatio = 1.0f;
        if (currentFader != null) {
            currentRatio = currentFader.m_currentRatio;
            ProcessScheduler.getInstance().remove(currentFader);
        }
        else {
            switch (type) {
                case 0: {
                    currentRatio = GameSoundGroup.MUSIC.getMusicGroup().getGainModRatioToDefault();
                    break;
                }
                case 1: {
                    currentRatio = GameSoundGroup.SOUND_AMB_2D.getDefaultGroup().getGainModRatioToDefault();
                    break;
                }
                case 2: {
                    currentRatio = GameSoundGroup.GUI.getDefaultGroup().getGainModRatioToDefault();
                    break;
                }
            }
        }
        final int frameDuration = 100;
        final int numFrames = Math.max(1, duration / 100);
        final Fader fader = new Fader(type, currentRatio, ratio, numFrames);
        ProcessScheduler.getInstance().schedule(fader, 100L, numFrames);
        return fader;
    }
    
    public void fadeMusic(final float gain, final int duration) {
        this.m_currentMusicFader = this.fade((byte)0, this.m_currentMusicFader, gain, duration);
    }
    
    public void fadeAmbiance(final float gain, final int duration) {
        this.m_currentAmbianceFader = this.fade((byte)1, this.m_currentAmbianceFader, gain, duration);
    }
    
    public void fadeGUI(final float gain, final int duration) {
        this.m_currentGUIFader = this.fade((byte)2, this.m_currentGUIFader, gain, duration);
    }
    
    @Override
    protected boolean onInitialize() {
        WakfuGameCalendar.getInstance().addDayChangeListener(this);
        try {
            this.m_ambianceEnabled = WakfuConfiguration.getInstance().getBoolean("soundAmbianceEnable");
            if (this.m_ambianceEnabled) {
                this.m_soundContainerManager.load(WakfuConfiguration.getContentPath("dynamicSoundAmbianceFile"));
                this.m_soundContainerManager.addListener(new SoundContainerManagerListener() {
                    long m_lastDate = System.currentTimeMillis();
                    
                    @Override
                    public void onUpdate(final long date) {
                        if (date > this.m_lastDate + 10000L) {
                            CharacterInfoZoneComputer.execute();
                            ResourceZoneComputer.execute();
                            AudioMarkerZoneComputer.execute();
                            this.m_lastDate = date;
                        }
                    }
                    
                    @Override
                    public void onEvent(final SoundEvent event) {
                    }
                });
            }
        }
        catch (PropertyException e) {
            WakfuSoundManager.m_logger.warn((Object)e.getMessage());
        }
        ContainerCriterionParameterManager.getInstance().setProvider(new WakfuContainerCriterionParameterProvider());
        if (this.m_initializer != null) {
            this.m_initializer.initSound();
        }
        for (final GameSoundGroup group : GameSoundGroup.values()) {
            group.onBackToLogin();
        }
        GameSoundGroup.MUSIC.getMusicGroup().setDefaultGainMod(WakfuConfiguration.getInstance().getMusicMix());
        this.addGroup(GameSoundGroup.MUSIC.getMusicGroup());
        GameSoundGroup.MUSIC.getFieldSourceGroup().setDefaultGainMod(WakfuConfiguration.getInstance().getMusicMix());
        this.addGroup(GameSoundGroup.MUSIC.getFieldSourceGroup());
        GameSoundGroup.SOUND_AMB_2D.getMusicGroup().setDefaultGainMod(WakfuConfiguration.getInstance().getAmb2dMix());
        this.addGroup(GameSoundGroup.SOUND_AMB_2D.getMusicGroup());
        GameSoundGroup.SOUND_AMB_2D.getDefaultGroup().setDefaultGainMod(WakfuConfiguration.getInstance().getAmb2dMix());
        this.addGroup(GameSoundGroup.SOUND_AMB_2D.getDefaultGroup());
        GameSoundGroup.SOUND_AMB_2D.getFieldSourceGroup().setDefaultGainMod(WakfuConfiguration.getInstance().getAmb2dMix());
        this.addGroup(GameSoundGroup.SOUND_AMB_2D.getFieldSourceGroup());
        GameSoundGroup.SOUND_AMB_3D.getFieldSourceGroup().setDefaultGainMod(WakfuConfiguration.getInstance().getAmb3dMix());
        this.addGroup(GameSoundGroup.SOUND_AMB_3D.getFieldSourceGroup());
        GameSoundGroup.GUI.getDefaultGroup().setDefaultGainMod(WakfuConfiguration.getInstance().getGuiMix());
        this.addGroup(GameSoundGroup.GUI.getDefaultGroup());
        GameSoundGroup.SFX.getDefaultGroup().setDefaultGainMod(WakfuConfiguration.getInstance().getSfxMix());
        this.addGroup(GameSoundGroup.SFX.getDefaultGroup());
        GameSoundGroup.SFX.getFieldSourceGroup().setDefaultGainMod(WakfuConfiguration.getInstance().getSfxMix());
        this.addGroup(GameSoundGroup.SFX.getFieldSourceGroup());
        GameSoundGroup.SOUND_FIGHT.getDefaultGroup().setDefaultGainMod(WakfuConfiguration.getInstance().getFightMix());
        this.addGroup(GameSoundGroup.SOUND_FIGHT.getDefaultGroup());
        GameSoundGroup.SOUND_FIGHT.getFieldSourceGroup().setDefaultGainMod(WakfuConfiguration.getInstance().getFightMix());
        this.addGroup(GameSoundGroup.SOUND_FIGHT.getFieldSourceGroup());
        GameSoundGroup.VOICES.getDefaultGroup().setDefaultGainMod(WakfuConfiguration.getInstance().getVoicesMix());
        this.addGroup(GameSoundGroup.VOICES.getDefaultGroup());
        GameSoundGroup.VOICES.getFieldSourceGroup().setDefaultGainMod(WakfuConfiguration.getInstance().getVoicesMix());
        this.addGroup(GameSoundGroup.VOICES.getFieldSourceGroup());
        GameSoundGroup.FOLEYS.getDefaultGroup().setDefaultGainMod(WakfuConfiguration.getInstance().getFoleysMix());
        this.addGroup(GameSoundGroup.FOLEYS.getDefaultGroup());
        GameSoundGroup.FOLEYS.getFieldSourceGroup().setDefaultGainMod(WakfuConfiguration.getInstance().getFoleysMix());
        this.addGroup(GameSoundGroup.FOLEYS.getFieldSourceGroup());
        GameSoundGroup.PARTICLES.getDefaultGroup().setDefaultGainMod(WakfuConfiguration.getInstance().getParticleMix());
        this.addGroup(GameSoundGroup.PARTICLES.getDefaultGroup());
        GameSoundGroup.PARTICLES.getFieldSourceGroup().setDefaultGainMod(WakfuConfiguration.getInstance().getParticleMix());
        this.addGroup(GameSoundGroup.PARTICLES.getFieldSourceGroup());
        VolumeLinker.DEFAULT_FADEOUT = WakfuConfiguration.getInstance().getFightMixFadeOutTime();
        VolumeLinker.DEFAULT_GAIN = WakfuConfiguration.getInstance().getFightMixTargetGain();
        SoundFunctionsLibrary.getInstance().setDelegate(this.m_soundLibraryDelegate);
        SoundFunctionsLibrary.getInstance().setBarkProvider(this.m_barkManager);
        SoundFunctionsLibrary.getInstance().setGroundSoundProvider(this.m_groundSoundManager);
        return true;
    }
    
    @Override
    public void setAreFiltersSupported(final boolean areFiltersSupported) {
        GameSoundGroup.SOUND_AMB_2D.getDefaultGroup().setEnableLowPassFilter(areFiltersSupported);
        GameSoundGroup.SOUND_AMB_2D.getFieldSourceGroup().setEnableLowPassFilter(areFiltersSupported);
        GameSoundGroup.SOUND_AMB_3D.getFieldSourceGroup().setEnableLowPassFilter(areFiltersSupported);
        GameSoundGroup.VOICES.getDefaultGroup().setEnableLowPassFilter(areFiltersSupported);
        GameSoundGroup.VOICES.getFieldSourceGroup().setEnableLowPassFilter(areFiltersSupported);
        GameSoundGroup.SFX.getDefaultGroup().setEnableLowPassFilter(areFiltersSupported);
        GameSoundGroup.SFX.getFieldSourceGroup().setEnableLowPassFilter(areFiltersSupported);
        GameSoundGroup.FOLEYS.getDefaultGroup().setEnableLowPassFilter(areFiltersSupported);
        GameSoundGroup.FOLEYS.getFieldSourceGroup().setEnableLowPassFilter(areFiltersSupported);
    }
    
    @Override
    protected void onUpdate(final long currentTime) {
        super.onUpdate(currentTime);
        this.m_soundLibraryDelegate.processSoundRequests();
        final int maskKey = WakfuClientInstance.getInstance().getWorldScene().getIsoCamera().getGroupMaskKey();
        if (this.m_currentCameraMaskKey != maskKey) {
            this.onCameraGroupChange(maskKey);
        }
    }
    
    @Override
    protected void onTerminate() {
        if (!this.isRunning()) {
            return;
        }
        this.removeGroup(GameSoundGroup.MUSIC.getMusicGroup());
        this.removeGroup(GameSoundGroup.MUSIC.getFieldSourceGroup());
        this.removeGroup(GameSoundGroup.SOUND_AMB_2D.getMusicGroup());
        this.removeGroup(GameSoundGroup.SOUND_AMB_2D.getDefaultGroup());
        this.removeGroup(GameSoundGroup.SOUND_AMB_2D.getFieldSourceGroup());
        this.removeGroup(GameSoundGroup.SOUND_FIGHT.getDefaultGroup());
        this.removeGroup(GameSoundGroup.SOUND_FIGHT.getFieldSourceGroup());
        this.removeGroup(GameSoundGroup.SOUND_AMB_3D.getFieldSourceGroup());
        this.removeGroup(GameSoundGroup.GUI.getDefaultGroup());
        this.removeGroup(GameSoundGroup.SFX.getDefaultGroup());
        this.removeGroup(GameSoundGroup.SFX.getFieldSourceGroup());
        this.removeGroup(GameSoundGroup.VOICES.getDefaultGroup());
        this.removeGroup(GameSoundGroup.VOICES.getFieldSourceGroup());
        this.removeGroup(GameSoundGroup.FOLEYS.getDefaultGroup());
        this.removeGroup(GameSoundGroup.FOLEYS.getFieldSourceGroup());
        this.removeGroup(GameSoundGroup.PARTICLES.getDefaultGroup());
        this.removeGroup(GameSoundGroup.PARTICLES.getFieldSourceGroup());
        SoundFunctionsLibrary.getInstance().setDelegate(this.m_soundLibraryDelegate);
        LocalPartitionManager.getInstance().removePartitionChangedListener(this);
    }
    
    public void stopWorldSound() {
        if (!this.isRunning()) {
            return;
        }
        try {
            GameSoundGroup.SOUND_AMB_2D.getMusicGroup().stop();
            GameSoundGroup.SOUND_AMB_2D.getDefaultGroup().stop();
            GameSoundGroup.SOUND_AMB_2D.getFieldSourceGroup().stop();
            GameSoundGroup.SOUND_AMB_3D.getFieldSourceGroup().stop();
            GameSoundGroup.SOUND_FIGHT.getDefaultGroup().stop();
            GameSoundGroup.SOUND_FIGHT.getFieldSourceGroup().stop();
            GameSoundGroup.SFX.getDefaultGroup().stop();
            GameSoundGroup.SFX.getFieldSourceGroup().stop();
            GameSoundGroup.VOICES.getDefaultGroup().stop();
            GameSoundGroup.VOICES.getFieldSourceGroup().stop();
            GameSoundGroup.FOLEYS.getDefaultGroup().stop();
            GameSoundGroup.FOLEYS.getFieldSourceGroup().stop();
            GameSoundGroup.PARTICLES.getDefaultGroup().stop();
            GameSoundGroup.PARTICLES.getFieldSourceGroup().stop();
        }
        catch (Exception e) {
            WakfuSoundManager.m_logger.error((Object)"Erreur lors de l'arr\u00eat des sons du monde ", (Throwable)e);
        }
    }
    
    private void applyPlayListChanges() {
        if (!this.m_inWorld || !this.m_needsToApplyPlayList || this.m_isInFight || this.m_isInForcedMusic) {
            return;
        }
        this.m_needsToApplyPlayList = false;
        final boolean day = WakfuGameCalendar.getInstance().isSunShining();
        final PlayListData playListData = PlayListManager.getInstance().get(this.m_currentPlayListId);
        if (playListData == null) {
            this.stopAmbience();
            this.stopMusic();
            return;
        }
        MusicData musicData = day ? playListData.getDayAmbience() : playListData.getNightAmbience();
        if (musicData == null) {
            this.stopAmbience();
        }
        else {
            this.playAmbience(musicData.getMusicId(), musicData.getVolume() / 100.0f);
        }
        final ArrayList<MusicData> musicDataList = day ? playListData.getDayMusics() : playListData.getNightMusics();
        if (musicDataList == null) {
            this.stopMusic();
        }
        else {
            final MusicGroup musicGroup = GameSoundGroup.MUSIC.getMusicGroup();
            musicGroup.loadPlayListData(musicDataList, playListData.isLoopPlaylist());
        }
        musicData = playListData.getFight();
        this.m_musicFightId = ((musicData == null) ? 200200L : musicData.getMusicId());
        this.m_musicFightGain = ((musicData == null) ? 1.0f : (musicData.getVolume() / 100.0f));
        musicData = playListData.getBossFight();
        this.m_musicBossFightId = ((musicData == null) ? this.m_musicFightId : musicData.getMusicId());
        this.m_musicBossFightGain = ((musicData == null) ? this.m_musicFightGain : (musicData.getVolume() / 100.0f));
    }
    
    public void setMusicContinuousMode(final boolean continuous) {
        GameSoundGroup.MUSIC.getMusicGroup().setInContinuousMode(continuous);
        this.m_needsToApplyPlayList = true;
        this.applyPlayListChanges();
    }
    
    public void onEnterWorld() {
        for (final GameSoundGroup group : GameSoundGroup.values()) {
            group.onEnterWorld();
        }
        this.m_inWorld = true;
        final MusicGroup musicGroup = GameSoundGroup.MUSIC.getMusicGroup();
        musicGroup.setFadeOutDuration(8000);
        musicGroup.setFadeInDuration(8000);
        if (this.m_ambianceEnabled) {
            this.m_soundContainerManager.play();
        }
    }
    
    public void onAmbianceZoneTypeChange() {
        if (this.m_ambianceEnabled) {
            this.m_soundContainerManager.play();
        }
    }
    
    public void onBackToLogin() {
        for (final GameSoundGroup group : GameSoundGroup.values()) {
            group.onBackToLogin();
        }
        this.m_inWorld = false;
        final MusicGroup musicGroup = GameSoundGroup.MUSIC.getMusicGroup();
        musicGroup.setFadeOutDuration(1000);
        musicGroup.setFadeInDuration(1000);
        this.m_currentPlayListId = -1;
        this.m_needsToApplyPlayList = false;
        this.m_isInFight = false;
        this.m_isInForcedMusic = false;
        if (this.m_ambianceEnabled) {
            this.m_soundContainerManager.stop();
        }
        final AudioSource source = this.playMusic(200000L);
        if (source == null) {
            this.stopMusic();
        }
    }
    
    @Override
    public void onDayChange(final boolean isDay) {
        this.m_needsToApplyPlayList = true;
        this.applyPlayListChanges();
    }
    
    public void playPlayList(final short playListId) {
        this.playPlayList(playListId, false);
    }
    
    public void playPlayList(final short playListId, final boolean force) {
        if (playListId == this.m_currentPlayListId && !force) {
            return;
        }
        this.m_currentPlayListId = playListId;
        this.m_needsToApplyPlayList = true;
        this.applyPlayListChanges();
    }
    
    public void switchToAlternativeMusic(final boolean alternative) {
        GameSoundGroup.MUSIC.getMusicGroup().switchToAlternatePlayList(alternative);
    }
    
    public void playAmbience(final long ambianceId, final float gain) {
        this.playAmbience(ambianceId, gain, true);
    }
    
    private void playAmbience(final long ambienceId, final float gain, final boolean rememberPrevious) {
        if (!this.isRunning()) {
            return;
        }
        if (ambienceId == 0L) {
            return;
        }
        final MusicGroup ambGroup = GameSoundGroup.SOUND_AMB_2D.getMusicGroup();
        if (rememberPrevious) {
            this.m_previousAmbiance = ambienceId;
            this.m_previousAmbianceGain = gain;
        }
        if (!this.m_isInFight) {
            ambGroup.crossFade(ambienceId, gain);
        }
    }
    
    public void stopAmbience() {
        if (!this.isRunning()) {
            return;
        }
        final MusicGroup group = GameSoundGroup.SOUND_AMB_2D.getMusicGroup();
        group.fadeAndStop(8000.0f);
    }
    
    @Nullable
    public AudioSource playMusic(final long musicId) {
        return this.playMusic(musicId, 1.0f);
    }
    
    @Nullable
    public AudioSource playMusic(final long musicId, final float gain) {
        if (!this.m_isInFight && musicId != 0L) {
            return GameSoundGroup.MUSIC.getMusicGroup().crossFade(musicId, gain);
        }
        return null;
    }
    
    private void stopMusic() {
        if (!this.isRunning()) {
            return;
        }
        final MusicGroup group = GameSoundGroup.MUSIC.getMusicGroup();
        if (group.getPlayListState() != MusicGroup.PlayListState.NONE) {
            group.loadPlayListData(null);
        }
        if (!this.m_isInFight) {
            try {
                group.fadeAndStop();
            }
            catch (Exception e) {
                WakfuSoundManager.m_logger.warn((Object)"Probl\u00e8me \u00e0 l'arr\u00eat du groupe de musique.");
            }
        }
    }
    
    public void playGUISound(final long soundId, final boolean loop, final int delay) {
        if (!this.isRunning()) {
            return;
        }
        if (delay == -1) {
            this.playGUISound(soundId, loop);
        }
        else {
            ProcessScheduler.getInstance().schedule(new GUISoundDelay(soundId, loop), delay, 1);
        }
    }
    
    @Nullable
    public AudioSource playGUISound(final long soundId) {
        if (!this.isRunning()) {
            return null;
        }
        return this.playGUISound(soundId, false);
    }
    
    @Nullable
    public AudioSource playGUISound(final long soundId, final boolean loop) {
        return this.playSound(GameSoundGroup.GUI.getDefaultGroup(), soundId, loop);
    }
    
    @Nullable
    public AudioSource playGuiSoundAndFadeMusic(final long soundId, final float gainMod, final int fadeOutDuration, final int fadeInDuration, final int delay) {
        final MusicGroup group = GameSoundGroup.MUSIC.getMusicGroup();
        final float gain = group.getInnerGain();
        group.fade(gain * gainMod, fadeOutDuration);
        ProcessScheduler.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
                group.fade(gain, fadeInDuration);
            }
        }, delay, 1);
        return this.playGUISound(soundId);
    }
    
    public void playGuiSoundWithDelayAndFadeMusic(final long soundId, final float gainMod, final int fadeOutDuration, final int fadeInDuration, final int delay) {
        final MusicGroup group = GameSoundGroup.MUSIC.getMusicGroup();
        final float gain = group.getInnerGain();
        group.fade(gain * gainMod, fadeOutDuration);
        ProcessScheduler.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
                group.fade(gain, fadeInDuration);
            }
        }, delay, 1);
        this.playGUISound(soundId, false, fadeOutDuration);
    }
    
    @Nullable
    public AudioSource playSFXSound(final long sounId) {
        return this.playSFXSound(sounId, false);
    }
    
    @Nullable
    public AudioSource playSFXSound(final long sounId, final boolean loop) {
        return this.playSound(GameSoundGroup.SFX.getDefaultGroup(), sounId, loop);
    }
    
    @Nullable
    private AudioSource playSound(final AudioSourceGroup asg, final long soundId, final boolean loop) {
        if (!this.isRunning()) {
            return null;
        }
        return asg.addSource(soundId, true, true, loop, -1L);
    }
    
    public void stopGUISound() {
        if (!this.isRunning()) {
            return;
        }
        try {
            GameSoundGroup.GUI.getDefaultGroup().stop();
        }
        catch (Exception e) {
            WakfuSoundManager.m_logger.warn((Object)("Probl\u00e8me lors de l'arr\u00eat des sons de l'interface : " + e.getMessage()));
        }
    }
    
    public void reset() {
        this.m_isInForcedMusic = false;
        if (this.m_isInFight) {
            this.m_isInFight = false;
            final MusicGroup ambianceGroup = GameSoundGroup.SOUND_AMB_2D.getMusicGroup();
            ambianceGroup.setFadeInDuration(4000);
            GameSoundGroup.SOUND_AMB_3D.getFieldSourceGroup().setMute(WakfuClientInstance.getInstance().getGamePreferences().getBooleanValue(KeyPreferenceStoreEnum.AMBIANCE_SOUNDS_MUTE_PREFERENCE_KEY));
        }
        final MusicGroup musicGroup = GameSoundGroup.MUSIC.getMusicGroup();
        musicGroup.setFadeOutDuration(8000);
        musicGroup.setFadeInDuration(8000);
    }
    
    public boolean isEnableInventoryEventSounds() {
        return this.m_enableInventoryEventSounds;
    }
    
    public void setEnableInventoryEventSounds(final boolean enableInventoryEventSounds) {
        this.m_enableInventoryEventSounds = enableInventoryEventSounds;
    }
    
    public XulorSoundManagementDelegate getXulorSoundManagementDelegate() {
        return this.m_xulorDelegate;
    }
    
    public void startLoading(final int soundId) {
        if (!this.isRunning()) {
            return;
        }
        if (this.m_loadingSource == null) {
            this.m_loadingSource = this.playGUISound(soundId, true);
        }
    }
    
    public void startLoading() {
        this.startLoading(600001);
    }
    
    public void stopLoading() {
        if (!this.isRunning()) {
            return;
        }
        if (this.m_loadingSource != null) {
            this.m_loadingSource.fadeOutAndStop(0.01f);
            this.m_loadingSource = null;
        }
    }
    
    public void alert() {
        if (!this.isRunning()) {
            return;
        }
        this.playGUISound(600011L);
    }
    
    public void windowFadeIn() {
        if (!this.isRunning()) {
            return;
        }
        final AudioSource source = this.playGUISound(600012L);
        if (source != null) {
            final float sourceGain = source.getGain();
            source.setGain(sourceGain * 0.5f);
        }
    }
    
    public void windowFadeOut() {
        if (!this.isRunning()) {
            return;
        }
        final AudioSource source = this.playGUISound(600013L);
        if (source != null) {
            final float sourceGain = source.getGain();
            source.setGain(sourceGain * 0.5f);
        }
    }
    
    public void equipItem(final EquipmentPosition pos) {
        if (!this.isRunning() || !this.m_enableInventoryEventSounds) {
            return;
        }
        switch (pos) {
            case ACCESSORY: {
                this.playGUISound(600037L);
                break;
            }
            case FIRST_WEAPON:
            case SECOND_WEAPON: {
                this.playGUISound(600038L);
                break;
            }
            case NECK: {
                this.playGUISound(600036L);
                break;
            }
            case LEGS: {
                this.playGUISound(600028L);
                break;
            }
            case ARMS: {
                this.playGUISound(600029L);
                break;
            }
            default: {
                this.playGUISound(600030L);
                break;
            }
        }
        this.blockInventoryEventSounds();
    }
    
    public void dropItem() {
        if (!this.isRunning() || !this.m_enableInventoryEventSounds) {
            return;
        }
        this.playGUISound(600043L);
        this.blockInventoryEventSounds();
    }
    
    public void storeItem() {
        if (!this.isRunning() || !this.m_enableInventoryEventSounds) {
            return;
        }
        this.playGUISound(600040L);
        this.blockInventoryEventSounds();
    }
    
    private void blockInventoryEventSounds() {
        this.m_enableInventoryEventSounds = false;
        ProcessScheduler.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
                WakfuSoundManager.this.m_enableInventoryEventSounds = true;
            }
        }, 1000L, 1);
    }
    
    public void onWakfuStasisUpdate() {
        this.switchToAlternativeMusic(WakfuGlobalZoneManager.getInstance().getZoneEquilibrium() < 0.0f);
    }
    
    public synchronized void startForcedMusic(final long fileId) {
        this.m_isInForcedMusic = true;
        this.m_forcedMusicId = fileId;
        if (this.m_isInFight) {
            return;
        }
        final MusicGroup musicGroup = GameSoundGroup.MUSIC.getMusicGroup();
        musicGroup.setFadeInDuration(1000);
        musicGroup.setFadeOutDuration(1000);
        musicGroup.pauseMainMusic(true);
        musicGroup.crossFade(this.m_forcedMusicId, 1.0f);
    }
    
    public synchronized void stopForcedMusic() {
        this.m_isInForcedMusic = false;
        if (!this.m_isInFight) {
            final MusicGroup musicGroup = GameSoundGroup.MUSIC.getMusicGroup();
            musicGroup.setFadeInDuration(2000);
            musicGroup.setFadeOutDuration(1000);
            musicGroup.pauseMainMusic(false);
            musicGroup.setFadeOutDuration(8000);
            musicGroup.setFadeInDuration(8000);
            this.applyPlayListChanges();
        }
    }
    
    public void playFightTaunt() {
        if (!this.isRunning()) {
            return;
        }
        this.playGUISound(600067L);
    }
    
    public synchronized void enterFight(final Fight fight) {
        if (!this.isRunning()) {
            return;
        }
        this.m_isInFight = true;
        if (this.m_ambianceEnabled) {
            this.m_soundContainerManager.stop();
        }
        GameSoundGroup.SOUND_AMB_3D.getFieldSourceGroup().setMute(true);
        GameSoundGroup.MUSIC.getFieldSourceGroup().setMute(true);
        long musicFightId = this.m_musicFightId;
        for (final CharacterInfo fighter : fight.getFighters()) {
            if (fighter instanceof NonPlayerCharacter) {
                final NonPlayerCharacter monster = (NonPlayerCharacter)fighter;
                if (monster.getProtector() != null) {
                    musicFightId = this.m_musicBossFightId;
                    break;
                }
                if (fighter.hasProperty(WorldPropertyType.BOSS)) {
                    musicFightId = this.m_musicBossFightId;
                    break;
                }
                continue;
            }
        }
        final MusicGroup musicGroup = GameSoundGroup.MUSIC.getMusicGroup();
        musicGroup.setFadeInDuration(1500);
        musicGroup.setFadeOutDuration(1000);
        if (!this.m_isInForcedMusic) {
            musicGroup.pauseMainMusic(true);
        }
        if (musicFightId != 0L) {
            musicGroup.crossFade(musicFightId, 0.4f * this.m_musicFightGain);
        }
    }
    
    public void enterAction() {
        if (!this.isRunning()) {
            return;
        }
        final MusicGroup musicGroup = GameSoundGroup.MUSIC.getMusicGroup();
        final AudioSource source = musicGroup.getMainMusic();
        if (source != null) {
            source.fade(this.m_musicFightGain, 1000.0f);
        }
    }
    
    public void endFight(final boolean victory) {
        if (!this.isRunning()) {
            return;
        }
        if (!this.m_isInFight) {
            return;
        }
        final MusicGroup musicGroup = GameSoundGroup.MUSIC.getMusicGroup();
        musicGroup.setFadeOutDuration(1000);
        musicGroup.setFadeInDuration(1000);
        if (victory) {
            musicGroup.crossFade(290998L, 1.0f);
        }
        else {
            musicGroup.crossFade(290999L, 1.0f);
        }
    }
    
    public synchronized void exitFight() {
        if (!this.isRunning()) {
            return;
        }
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        if (localPlayer == null) {
            return;
        }
        final boolean isInFight = localPlayer.getCurrentOrObservedFight() != null;
        if (isInFight) {
            return;
        }
        final MusicGroup ambianceGroup = GameSoundGroup.SOUND_AMB_2D.getMusicGroup();
        ambianceGroup.setFadeInDuration(4000);
        this.playAmbience(this.m_previousAmbiance, this.m_previousAmbianceGain);
        GameSoundGroup.SOUND_AMB_3D.getFieldSourceGroup().setMute(WakfuClientInstance.getInstance().getGamePreferences().getBooleanValue(KeyPreferenceStoreEnum.AMBIANCE_SOUNDS_MUTE_PREFERENCE_KEY));
        GameSoundGroup.MUSIC.getFieldSourceGroup().setMute(WakfuClientInstance.getInstance().getGamePreferences().getBooleanValue(KeyPreferenceStoreEnum.MUSIC_MUTE_PREFERENCE_KEY));
        final MusicGroup musicGroup = GameSoundGroup.MUSIC.getMusicGroup();
        if (!this.m_isInForcedMusic) {
            musicGroup.pauseMainMusic(false);
            musicGroup.setFadeOutDuration(8000);
            musicGroup.setFadeInDuration(8000);
        }
        else {
            musicGroup.crossFade(this.m_forcedMusicId, 0.5f);
        }
        this.m_isInFight = false;
        this.applyPlayListChanges();
        if (this.m_ambianceEnabled) {
            this.m_soundContainerManager.play();
            WeatherEffectManager.INSTANCE.playCurrentSoundEvent();
            AudioMarkerZoneComputer.launchLocalizedAudioMarkers();
        }
    }
    
    public final void waitForMapLoad() {
        final FieldSourceGroup group = GameSoundGroup.SOUND_AMB_3D.getFieldSourceGroup();
        group.setListenerReady(false);
    }
    
    public final void stopWaitingForMapLoad() {
        final FieldSourceGroup group = GameSoundGroup.SOUND_AMB_3D.getFieldSourceGroup();
        group.setListenerReady(true);
    }
    
    @Override
    public final void partitionChanged(final PathMobile target, final LocalPartition oldPartition, final LocalPartition newPartition) {
        if (!this.isRunning()) {
            return;
        }
    }
    
    public void setSoundSourceFlavor(final String key) {
        this.m_initializer = SoundSourceType.getFromKey(key);
    }
    
    public void setMusicVolume(final float volume) {
        if (!this.isRunning()) {
            return;
        }
        this.setGain(GameSoundGroup.MUSIC.getMusicGroup(), volume);
        this.setGain(GameSoundGroup.MUSIC.getFieldSourceGroup(), volume);
    }
    
    private void setGain(final AudioSourceGroup group, final float volume) {
        group.setMaxGain(volume);
        group.setGain(volume);
    }
    
    public float getMusicVolume() {
        return GameSoundGroup.MUSIC.getMusicGroup().getMaxGain();
    }
    
    public void setMusicMute(final boolean mute) {
        if (!this.isRunning()) {
            return;
        }
        GameSoundGroup.MUSIC.getMusicGroup().setMute(mute);
        GameSoundGroup.MUSIC.getFieldSourceGroup().setMute(mute);
    }
    
    public boolean isMusicMute() {
        return GameSoundGroup.MUSIC.getMusicGroup().isMute();
    }
    
    public void setUiSoundsVolume(final float volume) {
        if (!this.isRunning()) {
            return;
        }
        this.setGain(GameSoundGroup.GUI.getDefaultGroup(), volume);
    }
    
    public void setAmbianceSoundsVolume(final float volume) {
        if (!this.isRunning()) {
            return;
        }
        this.setGain(GameSoundGroup.SOUND_AMB_2D.getMusicGroup(), volume);
        this.setGain(GameSoundGroup.SOUND_AMB_2D.getDefaultGroup(), volume);
        this.setGain(GameSoundGroup.SOUND_AMB_2D.getFieldSourceGroup(), volume);
        this.setGain(GameSoundGroup.SOUND_AMB_3D.getFieldSourceGroup(), volume);
        this.setGain(GameSoundGroup.SOUND_FIGHT.getDefaultGroup(), volume);
        this.setGain(GameSoundGroup.SOUND_FIGHT.getFieldSourceGroup(), volume);
        this.setGain(GameSoundGroup.SFX.getDefaultGroup(), volume);
        this.setGain(GameSoundGroup.SFX.getFieldSourceGroup(), volume);
        this.setGain(GameSoundGroup.VOICES.getDefaultGroup(), volume);
        this.setGain(GameSoundGroup.VOICES.getFieldSourceGroup(), volume);
        this.setGain(GameSoundGroup.FOLEYS.getDefaultGroup(), volume);
        this.setGain(GameSoundGroup.FOLEYS.getFieldSourceGroup(), volume);
        this.setGain(GameSoundGroup.PARTICLES.getDefaultGroup(), volume);
        this.setGain(GameSoundGroup.PARTICLES.getFieldSourceGroup(), volume);
    }
    
    public float getSoundsVolume() {
        return GameSoundGroup.SOUND_AMB_2D.getDefaultGroup().getMaxGain();
    }
    
    public void setUiSoundsMute(final boolean mute) {
        if (!this.isRunning()) {
            return;
        }
        GameSoundGroup.GUI.getDefaultGroup().setMute(mute);
    }
    
    public boolean isUiSoundsMute() {
        return GameSoundGroup.GUI.getDefaultGroup().isMute();
    }
    
    public void setAmbianceSoundsMute(final boolean mute) {
        if (!this.isRunning()) {
            return;
        }
        GameSoundGroup.SOUND_AMB_2D.getMusicGroup().setMute(mute);
        GameSoundGroup.SOUND_AMB_2D.getDefaultGroup().setMute(mute);
        GameSoundGroup.SOUND_AMB_2D.getFieldSourceGroup().setMute(mute);
        GameSoundGroup.SOUND_FIGHT.getDefaultGroup().setMute(mute);
        GameSoundGroup.SOUND_FIGHT.getFieldSourceGroup().setMute(mute);
        GameSoundGroup.SOUND_AMB_3D.getFieldSourceGroup().setMute(mute);
        GameSoundGroup.SFX.getDefaultGroup().setMute(mute);
        GameSoundGroup.SFX.getFieldSourceGroup().setMute(mute);
        GameSoundGroup.VOICES.getDefaultGroup().setMute(mute);
        GameSoundGroup.VOICES.getFieldSourceGroup().setMute(mute);
        GameSoundGroup.FOLEYS.getDefaultGroup().setMute(mute);
        GameSoundGroup.FOLEYS.getFieldSourceGroup().setMute(mute);
        GameSoundGroup.PARTICLES.getDefaultGroup().setMute(mute);
        GameSoundGroup.PARTICLES.getFieldSourceGroup().setMute(mute);
    }
    
    public boolean isAmbianceSoundsMute() {
        return GameSoundGroup.SOUND_AMB_2D.getMusicGroup().isMute();
    }
    
    @Override
    public PositionedSound addWorldSound(final IAmbienceSound sound, final int x, final int y, final int z) {
        if (!this.isRunning()) {
            return null;
        }
        if (sound == null) {
            return null;
        }
        final long prefix = SoundGroupUtils.getSoundPrefix(sound.getFileId());
        GameSoundGroup group = GameSoundGroup.getGameSourceGroupFromPrefix(prefix);
        if (group == null || group.getFieldSourceGroup() == null) {
            group = GameSoundGroup.SOUND_AMB_3D;
        }
        final FieldSourceGroup posGroup = group.getFieldSourceGroup();
        if (!posGroup.canPrepareSounds()) {
            return null;
        }
        AudioStreamProvider asp;
        try {
            asp = posGroup.getHelper().fromId(sound.getFileId());
        }
        catch (IOException e2) {
            WakfuSoundManager.m_logger.debug((Object)("Impossible de charger le son d'id " + sound.getFileId()));
            return null;
        }
        if (asp == null) {
            WakfuSoundManager.m_logger.debug((Object)("Impossible de charger le son d'id " + sound.getFileId()));
            return null;
        }
        PositionedSound psound;
        try {
            final int groupId = WakfuClientInstance.getInstance().getWorldScene().getGroupId(x, y, z);
            psound = posGroup.addSource(asp, sound.getMaxGain(), new StaticPositionProvider(x, y, z, false, groupId), sound.getRefDistance(), sound.getMaxDistance(), sound.getRollOffFactor(), sound.getMinTimeBeforeLoop(), sound.getMaxTimeBeforeLoop(), true, false, false, 200.0f, -1L);
        }
        catch (Exception e) {
            WakfuSoundManager.m_logger.debug((Object)"Exception lev\u00e9e lors de la cr\u00e9ation d'une source positionn\u00e9e", (Throwable)e);
            return null;
        }
        return psound;
    }
    
    @Override
    public void setListener(final ObservedListener listener) {
        if (listener == null) {
            WakfuSoundManager.m_logger.warn((Object)"setListener(null), ne devrait pas arriver");
        }
        if (!this.isRunning()) {
            return;
        }
        super.setListener(listener);
        GameSoundGroup.MUSIC.getFieldSourceGroup().setListener(listener);
        GameSoundGroup.SOUND_AMB_2D.getFieldSourceGroup().setListener(listener);
        GameSoundGroup.SOUND_AMB_3D.getFieldSourceGroup().setListener(listener);
        GameSoundGroup.SOUND_FIGHT.getFieldSourceGroup().setListener(listener);
        GameSoundGroup.SFX.getFieldSourceGroup().setListener(listener);
        GameSoundGroup.VOICES.getFieldSourceGroup().setListener(listener);
        GameSoundGroup.PARTICLES.getFieldSourceGroup().setListener(listener);
        GameSoundGroup.FOLEYS.getFieldSourceGroup().setListener(listener);
        GameSoundGroup.SOUND_AMB_2D.getDefaultGroup().setListener(listener);
        GameSoundGroup.SOUND_FIGHT.getDefaultGroup().setListener(listener);
        GameSoundGroup.SFX.getDefaultGroup().setListener(listener);
        GameSoundGroup.VOICES.getDefaultGroup().setListener(listener);
        GameSoundGroup.PARTICLES.getDefaultGroup().setListener(listener);
        GameSoundGroup.FOLEYS.getDefaultGroup().setListener(listener);
    }
    
    public void initReverbPreset(final String sndPath) throws Exception {
        if (!this.isRunning()) {
            return;
        }
        final XMLDocumentAccessor accessor = new XMLDocumentAccessor();
        final XMLDocumentContainer container = new XMLDocumentContainer();
        accessor.open(sndPath);
        accessor.read(container, new DocumentEntryParser[0]);
        accessor.close();
        final DocumentEntry entry = container.getEntryByName("reverbs");
        this.m_reverbPresets = ReverbParameter.createListFromXML(entry);
    }
    
    @Override
    protected ReverbParameter getReverbParameter(final int id) {
        return this.m_reverbPresets.get(id);
    }
    
    @Override
    protected void applyReverb(final int effectSlot) {
        GameSoundGroup.MUSIC.getFieldSourceGroup().applyReverb(effectSlot);
        GameSoundGroup.SOUND_AMB_2D.getDefaultGroup().applyReverb(effectSlot);
        GameSoundGroup.SOUND_AMB_2D.getFieldSourceGroup().applyReverb(effectSlot);
        GameSoundGroup.SOUND_AMB_3D.getFieldSourceGroup().applyReverb(effectSlot);
        GameSoundGroup.SOUND_FIGHT.getDefaultGroup().applyReverb(effectSlot);
        GameSoundGroup.SOUND_FIGHT.getFieldSourceGroup().applyReverb(effectSlot);
        GameSoundGroup.VOICES.getDefaultGroup().applyReverb(effectSlot);
        GameSoundGroup.VOICES.getFieldSourceGroup().applyReverb(effectSlot);
        GameSoundGroup.SFX.getDefaultGroup().applyReverb(effectSlot);
        GameSoundGroup.FOLEYS.getDefaultGroup().applyReverb(effectSlot);
        GameSoundGroup.FOLEYS.getFieldSourceGroup().applyReverb(effectSlot);
    }
    
    public void initLowPassPreset(final String sndPath) throws Exception {
        if (!this.isRunning()) {
            return;
        }
        final XMLDocumentAccessor accessor = new XMLDocumentAccessor();
        final XMLDocumentContainer container = new XMLDocumentContainer();
        accessor.open(sndPath);
        accessor.read(container, new DocumentEntryParser[0]);
        accessor.close();
        final DocumentEntry entry = container.getEntryByName("lowpasses");
        this.m_lowPassPresets = LowPassParameter.createListFromXML(entry);
        this.setLowPassFilter(0);
    }
    
    @Override
    protected LowPassParameter getLowPassParameter(final int id) {
        if (this.m_lowPassPresets == null || this.m_lowPassPresets.size() == 0) {
            return LowPassParameter.DEFAULT;
        }
        return this.m_lowPassPresets.get(id);
    }
    
    public void reloadRollOffPresets() {
        try {
            final String sndPath = WakfuConfiguration.getContentPath("rollOffPresetFile");
            getInstance().initRollOffPreset(sndPath);
        }
        catch (Exception ex) {
            WakfuSoundManager.m_logger.error((Object)"impossible d'initialiser WakfuSoundManager.ROLLOFF_PRESET_FILE", (Throwable)ex);
        }
    }
    
    public void initRollOffPreset(final String rollOffPresetsPath) throws Exception {
        if (!this.isRunning()) {
            return;
        }
        final XMLDocumentAccessor accessor = new XMLDocumentAccessor();
        final XMLDocumentContainer container = new XMLDocumentContainer();
        accessor.open(rollOffPresetsPath);
        accessor.read(container, new DocumentEntryParser[0]);
        accessor.close();
        final DocumentEntry entry = container.getEntryByName("rollOffs");
        this.m_rollOffPresets = RollOffParameter.createListFromXML(entry);
    }
    
    @Override
    public RollOffParameter getRollOffParameter(final int id) {
        if (this.m_rollOffPresets == null || this.m_rollOffPresets.size() == 0) {
            return RollOffParameter.DEFAULT;
        }
        final RollOffParameter param = this.m_rollOffPresets.get(id);
        if (param != null) {
            return param;
        }
        return this.m_rollOffPresets.getQuickValue(0);
    }
    
    public void initBarks(final String path) throws Exception {
        if (!this.isRunning()) {
            return;
        }
        this.m_barkManager.loadFromXML(path);
    }
    
    public void reloadBarks() throws Exception {
        if (!this.isRunning()) {
            return;
        }
        this.m_barkManager.reload();
    }
    
    public void initGrounds(final String path) throws Exception {
        if (!this.isRunning()) {
            return;
        }
        this.m_groundSoundManager.loadFromXML(path);
    }
    
    public void reloadGrounds() throws Exception {
        if (!this.isRunning()) {
            return;
        }
        this.m_groundSoundManager.reload();
    }
    
    public void onEvent(final SoundEvent event) {
        if (this.m_ambianceEnabled && !this.m_isInFight) {
            this.m_soundContainerManager.onEvent(event);
        }
    }
    
    public void onAudioMarkerSpawn(final long id, final AudioMarkerType type, final ObservedSource source) {
        if (this.m_ambianceEnabled && !this.m_isInFight) {
            this.m_soundContainerManager.onAudioMarkerSpawn(id, source, type);
        }
    }
    
    public void onAudioMarkerDespawn(final long id) {
        this.m_soundContainerManager.onAudioMarkerDespawn(id);
    }
    
    @Override
    protected boolean isSoundCacheActivated() {
        return true;
    }
    
    public void addSoundContainerManagerListener(final SoundContainerManagerListener l) {
        this.m_soundContainerManager.addListener(l);
    }
    
    public void removeSoundContainerManagerListener(final SoundContainerManagerListener l) {
        this.m_soundContainerManager.removeListener(l);
    }
    
    public SoundContainerManager getSoundContainerManager() {
        return this.m_soundContainerManager;
    }
    
    static {
        m_instance = new WakfuSoundManager();
    }
    
    public final class GUISoundDelay implements Runnable
    {
        private long m_soundId;
        private boolean m_loop;
        
        public GUISoundDelay(final long soundId, final boolean loop) {
            super();
            this.m_soundId = soundId;
            this.m_loop = loop;
        }
        
        @Override
        public void run() {
            WakfuSoundManager.this.playGUISound(this.m_soundId, this.m_loop);
        }
    }
    
    private class Fader implements Runnable
    {
        private static final byte MUSIC = 0;
        private static final byte AMBIANCE = 1;
        private static final byte GUI = 2;
        private final byte m_type;
        private final float m_startingRatio;
        private final float m_destRatio;
        private final int m_numFrames;
        private int m_currentFrame;
        private float m_currentRatio;
        
        private Fader(final byte type, final float startingRatio, final float destRatio, final int numFrames) {
            super();
            this.m_type = type;
            this.m_startingRatio = startingRatio;
            this.m_destRatio = destRatio;
            this.m_numFrames = numFrames;
        }
        
        @Override
        public void run() {
            this.m_currentRatio = MathHelper.lerp(this.m_startingRatio, this.m_destRatio, ++this.m_currentFrame / this.m_numFrames);
            switch (this.m_type) {
                case 0: {
                    GameSoundGroup.MUSIC.getDefaultGroup().setGainModRatioToDefault(this.m_currentRatio);
                    GameSoundGroup.MUSIC.getMusicGroup().setGainModRatioToDefault(this.m_currentRatio);
                    GameSoundGroup.MUSIC.getFieldSourceGroup().setGainModRatioToDefault(this.m_currentRatio);
                    break;
                }
                case 1: {
                    GameSoundGroup.SOUND_AMB_2D.getMusicGroup().setGainModRatioToDefault(this.m_currentRatio);
                    GameSoundGroup.SOUND_AMB_2D.getDefaultGroup().setGainModRatioToDefault(this.m_currentRatio);
                    GameSoundGroup.SOUND_AMB_2D.getFieldSourceGroup().setGainModRatioToDefault(this.m_currentRatio);
                    GameSoundGroup.SOUND_AMB_3D.getFieldSourceGroup().setGainModRatioToDefault(this.m_currentRatio);
                    GameSoundGroup.SFX.getDefaultGroup().setGainModRatioToDefault(this.m_currentRatio);
                    GameSoundGroup.SFX.getFieldSourceGroup().setGainModRatioToDefault(this.m_currentRatio);
                    GameSoundGroup.SOUND_FIGHT.getDefaultGroup().setGainModRatioToDefault(this.m_currentRatio);
                    GameSoundGroup.SOUND_FIGHT.getFieldSourceGroup().setGainModRatioToDefault(this.m_currentRatio);
                    GameSoundGroup.VOICES.getDefaultGroup().setGainModRatioToDefault(this.m_currentRatio);
                    GameSoundGroup.VOICES.getFieldSourceGroup().setGainModRatioToDefault(this.m_currentRatio);
                    GameSoundGroup.FOLEYS.getDefaultGroup().setGainModRatioToDefault(this.m_currentRatio);
                    GameSoundGroup.FOLEYS.getFieldSourceGroup().setGainModRatioToDefault(this.m_currentRatio);
                    GameSoundGroup.PARTICLES.getDefaultGroup().setGainModRatioToDefault(this.m_currentRatio);
                    GameSoundGroup.PARTICLES.getFieldSourceGroup().setGainModRatioToDefault(this.m_currentRatio);
                    break;
                }
                case 2: {
                    GameSoundGroup.GUI.getDefaultGroup().setGainModRatioToDefault(this.m_currentRatio);
                    break;
                }
            }
            if (this.m_currentFrame == this.m_numFrames) {
                switch (this.m_type) {
                    case 0: {
                        WakfuSoundManager.this.m_currentMusicFader = null;
                        break;
                    }
                    case 1: {
                        WakfuSoundManager.this.m_currentAmbianceFader = null;
                        break;
                    }
                    case 2: {
                        WakfuSoundManager.this.m_currentGUIFader = null;
                        break;
                    }
                }
            }
        }
    }
}
