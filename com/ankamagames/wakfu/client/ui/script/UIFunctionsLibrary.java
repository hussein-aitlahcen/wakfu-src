package com.ankamagames.wakfu.client.ui.script;

import org.apache.log4j.*;
import com.ankamagames.xulor2.decorator.*;
import org.jetbrains.annotations.*;
import gnu.trove.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.dialog.*;
import org.keplerproject.luajava.*;
import com.ankamagames.framework.kernel.events.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.core.landMarks.*;
import com.ankamagames.wakfu.client.core.preferences.*;
import com.ankamagames.baseImpl.graphics.core.*;
import com.ankamagames.wakfu.client.core.game.notificationSystem.*;
import com.ankamagames.wakfu.client.ui.protocol.message.notificationMessage.*;
import com.ankamagames.wakfu.common.game.time.calendar.*;
import com.ankamagames.wakfu.client.core.game.challenge.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.*;
import com.ankamagames.wakfu.client.core.game.fight.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.client.ui.systemMessage.*;
import com.ankamagames.framework.kernel.core.common.message.scheduler.clock.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.ui.mru.*;
import com.ankamagames.baseImpl.graphics.alea.animatedElement.*;
import com.ankamagames.baseImpl.graphics.game.interactiveElement.*;
import com.ankamagames.wakfu.common.game.skill.*;
import com.ankamagames.wakfu.client.ui.mru.MRUActions.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.baseImpl.graphics.ui.*;
import com.ankamagames.wakfu.client.ui.*;
import com.ankamagames.xulor2.layout.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.xulor2.tween.*;
import com.ankamagames.xulor2.appearance.*;
import com.ankamagames.xulor2.util.alignment.*;
import com.ankamagames.framework.graphics.engine.particleSystem.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.wakfu.client.alea.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.client.ui.protocol.message.shortcut.*;
import com.ankamagames.wakfu.client.core.game.item.referenceItem.*;
import com.ankamagames.wakfu.common.game.shortcut.*;
import java.util.*;
import com.ankamagames.wakfu.client.core.game.shortcut.*;
import com.ankamagames.baseImpl.graphics.ui.event.*;
import com.ankamagames.xulor2.util.*;
import com.ankamagames.baseImpl.graphics.ui.shortcuts.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.wakfu.client.core.game.miniMap.*;
import com.ankamagames.wakfu.client.*;
import com.ankamagames.baseImpl.client.proxyclient.base.chat.*;
import com.ankamagames.wakfu.client.core.protector.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.wakfu.client.core.game.wakfu.*;
import com.ankamagames.wakfu.client.core.game.pet.*;
import com.ankamagames.xulor2.core.event.*;
import com.ankamagames.framework.script.*;
import com.ankamagames.baseImpl.graphics.alea.display.*;
import com.ankamagames.baseImpl.common.clientAndServer.world.topology.*;
import com.ankamagames.framework.kernel.core.common.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.wakfu.client.ui.protocol.message.tutorial.*;
import com.ankamagames.wakfu.client.ui.protocol.message.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.*;

public class UIFunctionsLibrary extends JavaFunctionsLibrary
{
    static final Logger m_logger;
    private static final LuaScriptParameterDescriptor[] START_INTERACTIVE_DIALOGS_LUA_SCRIPT_PARAMETER_DESCRIPTORS;
    private static final LuaScriptParameterDescriptor[] ADD_LANDMARK_NOTE_RETURN_LUA_SCRIPT_PARAMETER_DESCRIPTORS;
    private static final LuaScriptParameterDescriptor[] DISPLAY_BUTTON_PARAMS;
    private static final LuaScriptParameterDescriptor[] ADD_LANDMARK_NOTE_LUA_SCRIPT_PARAMETER_DESCRIPTORS;
    private static final LuaScriptParameterDescriptor[] DISPLAY_TUTORIAL_MESSAGE_LUA_SCRIPT_PARAMETER_DESCRIPTORS;
    private static final LuaScriptParameterDescriptor[] DISPLAY_CHALLENGE_VAR_UPDATE_LUA_SCRIPT_PARAMETER_DESCRIPTORS;
    private static final LuaScriptParameterDescriptor[] REMOVE_LAND_MARK_LUA_SCRIPT_PARAMETER_DESCRIPTORS;
    private static final LuaScriptParameterDescriptor[] DISPLAY_FRESCO_LUA_SCRIPT_PARAMETER_DESCRIPTORS;
    private static final LuaScriptParameterDescriptor[] START_COUNTDOWN_LUA_SCRIPT_PARAMETER_DESCRIPTORS;
    private static final LuaScriptParameterDescriptor[] HIGHLIGHT_FIGHTER_IN_TIMELINE_LUA_SCRIPT_PARAMETER_DESCRIPTORS;
    private static final LuaScriptParameterDescriptor[] DISPLAY_COUNTDOWN_LUA_SCRIPT_PARAMETER_DESCRIPTORS;
    private static final LuaScriptParameterDescriptor[] SYSTEM_MESSAGE_LUA_SCRIPT_PARAMETER_DESCRIPTORS;
    private static final LuaScriptParameterDescriptor[] OPEN_MRU_WITH_ACTION_LUA_SCRIPT_PARAMETER_DESCRIPTORS;
    private static final LuaScriptParameterDescriptor[] OPEN_MRU_LUA_SCRIPT_PARAMETER_DESCRIPTORS;
    private static final LuaScriptParameterDescriptor[] MANAGE_SKILL_MRU_LUA_SCRIPT_PARAMETER_DESCRIPTORS;
    private static final LuaScriptParameterDescriptor[] MANAGE_MRU_LUA_SCRIPT_PARAMETER_DESCRIPTORS;
    private static final LuaScriptParameterDescriptor[] ADD_MRU_LUA_SCRIPT_PARAMETER_DESCRIPTORS;
    private static final LuaScriptParameterDescriptor[] REMOVE_MRU_LUA_SCRIPT_PARAMETER_DESCRIPTORS;
    private static final LuaScriptParameterDescriptor[] SET_TRAINING_FIGHT_PARAMETERS_LUA_SCRIPT_PARAMETER_DESCRIPTORS;
    private static final LuaScriptParameterDescriptor[] SET_TRAINING_FIGHT_BATTLEGROUND_CENTER_LUA_SCRIPT_PARAMETER_DESCRIPTORS;
    private static final LuaScriptParameterDescriptor[] SET_TRAINING_FIGHT_FIGHT_START_POSITIONS_LUA_SCRIPT_PARAMETER_DESCRIPTORS;
    private static final LuaScriptParameterDescriptor[] MANAGE_FRAME_LUA_SCRIPT_PARAMETER_DESCRIPTORS;
    private static final LuaScriptParameterDescriptor[] ADD_EVENT_LISTENER_LUA_SCRIPT_PARAMETER_DESCRIPTORS;
    private static final LuaScriptParameterDescriptor[] REMOVE_EVENT_LISTENER_LUA_SCRIPT_PARAMETER_DESCRIPTORS;
    private static final LuaScriptParameterDescriptor[] CLICK_BUTTON_LUA_SCRIPT_PARAMETER_DESCRIPTORS;
    private static final LuaScriptParameterDescriptor[] ENABLE_DRAG_N_DROP_LUA_SCRIPT_PARAMETER_DESCRIPTORS;
    private static final LuaScriptParameterDescriptor[] ENABLE_EVENT_LUA_SCRIPT_PARAMETER_DESCRIPTORS;
    private static final LuaScriptParameterDescriptor[] CHARACTER_DISPLAYER_OPEN_LUA_SCRIPT_PARAMETER_DESCRIPTORS;
    private static final LuaScriptParameterDescriptor[] CHARACTER_DISPLAYER_CHANGE_LINKAGE_LUA_SCRIPT_PARAMETER_DESCRIPTORS;
    private static final LuaScriptParameterDescriptor[] CHARACTER_DISPLAYER_CLOSE_LUA_SCRIPT_PARAMETER_DESCRIPTORS;
    private static final LuaScriptParameterDescriptor[] LOAD_DIALOG_LUA_SCRIPT_PARAMETER_DESCRIPTORS;
    private static final LuaScriptParameterDescriptor[] UNLOAD_DIALOG_LUA_SCRIPT_PARAMETER_DESCRIPTORS;
    private static final LuaScriptParameterDescriptor[] SET_WINDOW_MOVABLE_LUA_SCRIPT_PARAMETER_DESCRIPTORS;
    private static final LuaScriptParameterDescriptor[] SET_WIDGET_ENABLED_LUA_SCRIPT_PARAMETER_DESCRIPTORS;
    private static final LuaScriptParameterDescriptor[] SET_PROPERTY_BOOLEAN_VALUE_LUA_SCRIPT_PARAMETER_DESCRIPTORS;
    private static final LuaScriptParameterDescriptor[] SET_PROPERTY_STRING_VALUE_LUA_SCRIPT_PARAMETER_DESCRIPTORS;
    private static final LuaScriptParameterDescriptor[] REMOVE_PROPERTY_LUA_SCRIPT_PARAMETER_DESCRIPTORS;
    private static final LuaScriptParameterDescriptor[] REMOVE_PROPERTY_CLIENT_LUA_SCRIPT_PARAMETER_DESCRIPTORS;
    private static final LuaScriptParameterDescriptor[] GET_PROPERTY_VALUE_INT_LUA_SCRIPT_PARAMETER_DESCRIPTORS;
    private static final LuaScriptParameterDescriptor[] SET_WIDGET_VISIBILITY_LUA_SCRIPT_PARAMETER_DESCRIPTORS;
    private static final LuaScriptParameterDescriptor[] SET_WIDGET_BLOCKABILITY_LUA_SCRIPT_PARAMETER_DESCRIPTORS;
    private static final LuaScriptParameterDescriptor[] ADD_LIST_COLOR_TWEEN_LUA_SCRIPT_PARAMETER_DESCRIPTORS;
    private static final LuaScriptParameterDescriptor[] REMOVE_LIST_COLOR_TWEEN_LUA_SCRIPT_PARAMETER_DESCRIPTORS;
    private static final LuaScriptParameterDescriptor[] ADD_COLOR_TWEEN_LUA_SCRIPT_PARAMETER_DESCRIPTORS;
    private static final LuaScriptParameterDescriptor[] REMOVE_COLOR_TWEEN_LUA_SCRIPT_PARAMETER_DESCRIPTORS;
    private static final LuaScriptParameterDescriptor[] ADD_PARTICLE_LUA_SCRIPT_PARAMETER_DESCRIPTORS;
    private static final LuaScriptParameterDescriptor[] ADD_PARTICLE_RETURN_LUA_SCRIPT_PARAMETER_DESCRIPTORS;
    private static final LuaScriptParameterDescriptor[] REMOVE_PARTICLE_LUA_SCRIPT_PARAMETER_DESCRIPTORS;
    private static final LuaScriptParameterDescriptor[] CLEAR_PARTICLES_LUA_SCRIPT_PARAMETER_DESCRIPTORS;
    private static final LuaScriptParameterDescriptor[] CHANGE_CURSOR_LUA_SCRIPT_PARAMETER_DESCRIPTORS;
    private static final LuaScriptParameterDescriptor[] CHANGE_CURSOR_RETURN_LUA_SCRIPT_PARAMETER_DESCRIPTORS;
    private static final LuaScriptParameterDescriptor[] DISPLAY_WIDE_SCREEN_BAND_LUA_SCRIPT_PARAMETER_DESCRIPTORS;
    private static final LuaScriptParameterDescriptor[] ADD_EQUIPMENT_SHORTCUT_LUA_SCRIPT_PARAMETER_DESCRIPTORS;
    private static final LuaScriptParameterDescriptor[] ADD_EMOTE_SHORTCUT_LUA_SCRIPT_PARAMETER_DESCRIPTORS;
    private static final LuaScriptParameterDescriptor[] REMOVE_SHORTCUT_WITH_ID_LUA_SCRIPT_PARAMETER_DESCRIPTORS;
    private static final LuaScriptParameterDescriptor[] ADD_SPELL_SHORTCUT_LUA_SCRIPT_PARAMETER_DESCRIPTORS;
    private static final LuaScriptParameterDescriptor[] ADD_ITEM_SHORTCUT_LUA_SCRIPT_PARAMETER_DESCRIPTORS;
    private static final LuaScriptParameterDescriptor[] HAS_SHORTCUT_LUA_SCRIPT_RESULT_DESCRIPTORS;
    private static final LuaScriptParameterDescriptor[] HAS_SHORTCUT_LUA_SCRIPT_PARAMETER_DESCRIPTORS;
    private static final LuaScriptParameterDescriptor[] HAS_WORLD_SHORTCUT_LUA_SCRIPT_RESULT_DESCRIPTORS;
    private static final LuaScriptParameterDescriptor[] HAS_WORLD_SHORTCUT_LUA_SCRIPT_PARAMETER_DESCRIPTORS;
    private static final LuaScriptParameterDescriptor[] HAS_FIGHT_SHORTCUT_LUA_SCRIPT_RESULT_DESCRIPTORS;
    private static final LuaScriptParameterDescriptor[] HAS_FIGHT_SHORTCUT_LUA_SCRIPT_PARAMETER_DESCRIPTORS;
    private static final LuaScriptParameterDescriptor[] SET_SHORTCUT_USABLE_LUA_SCRIPT_PARAMETER_DESCRIPTORS;
    private static final LuaScriptParameterDescriptor[] ADD_DIALOG_UNLOADED_LISTENER_LUA_SCRIPT_PARAMETER_DESCRIPTORS;
    private static final LuaScriptParameterDescriptor[] GET_WINDOW_SIZE_LUA_SCRIPT_PARAMETER_DESCRIPTORS;
    private static final LuaScriptParameterDescriptor[] REMOVE_DIALOG_UNLOADED_LISTENER_LUA_SCRIPT_PARAMETER_DESCRIPTORS;
    private static final LuaScriptParameterDescriptor[] ADD_DIALOG_LOADED_LISTENER_LUA_SCRIPT_PARAMETER_DESCRIPTORS;
    private static final LuaScriptParameterDescriptor[] REMOVE_DIALOG_LOADED_LISTENER_LUA_SCRIPT_PARAMETER_DESCRIPTORS;
    private static final LuaScriptParameterDescriptor[] SET_SHORTCUT_ENABLED_LUA_SCRIPT_PARAMETER_DESCRIPTORS;
    private static final LuaScriptParameterDescriptor[] SET_UI_VISIBLE_LUA_SCRIPT_PARAMETER_DESCRIPTORS;
    private static final LuaScriptParameterDescriptor[] WRITE_IN_CHAT_LUA_SCRIPT_PARAMETER_DESCRIPTORS;
    private static final LuaScriptParameterDescriptor[] WRITE_IN_EMOTE_CHAT_LUA_SCRIPT_PARAMETER_DESCRIPTORS;
    private static final LuaScriptParameterDescriptor[] FOLLOW_ON_MINIMAP_LUA_SCRIPT_PARAMETER_DESCRIPTORS;
    private static final LuaScriptParameterDescriptor[] REMOVE_FROM_MINIMAP_LUA_SCRIPT_PARAMETER_DESCRIPTORS;
    private static final LuaScriptParameterDescriptor[] WRITE_BLOOPS_IN_CHAT_LUA_SCRIPT_PARAMETER_DESCRIPTORS;
    private static final LuaScriptParameterDescriptor[] WRITE_UNLOCALIZED_IN_CHAT_LUA_SCRIPT_PARAMETER_DESCRIPTORS;
    private static final LuaScriptParameterDescriptor[] DISPLAY_SMILEY_LUA_SCRIPT_PARAMETER_DESCRIPTORS;
    private static final LuaScriptParameterDescriptor[] PUSH_STATIC_PROTECTOR_LUA_SCRIPT_PARAMETER_DESCRIPTORS;
    private static final LuaScriptParameterDescriptor[] SET_STATIC_PROTECTOR_INTERVAL_LUA_SCRIPT_PARAMETER_DESCRIPTORS;
    private static final LuaScriptParameterDescriptor[] DISPLAY_SPLASH_SCREEN_IMAGE_LUA_SCRIPT_PARAMETER_DESCRIPTORS;
    private static final LuaScriptParameterDescriptor[] IS_BAR_LOCKED_MODE_LUA_SCRIPT_PARAMETER_DESCRIPTORS;
    private static final LuaScriptParameterDescriptor[] IS_DIALOG_LOADED_LUA_SCRIPT_PARAMETER_DESCRIPTORS;
    private static final LuaScriptParameterDescriptor[] IS_DIALOG_LOADED_RETURN_LUA_SCRIPT_PARAMETER_DESCRIPTORS;
    private static final LuaScriptParameterDescriptor[] PUSH_PET_MESSAGE_LUA_SCRIPT_PARAMETER_DESCRIPTORS;
    private static final LuaScriptParameterDescriptor[] DISPLAY_BACKGROUND_LUA_SCRIPT_PARAMETER_DESCRIPTORS;
    private static final LuaScriptParameterDescriptor[] DUNGEON_RESULT_SPLASH_TEXT_LUA_SCRIPT_PARAMETER_DESCRIPTORS;
    private static final LuaScriptParameterDescriptor[] SPLASH_TEXT_LUA_SCRIPT_PARAMETER_DESCRIPTORS;
    private static final LuaScriptParameterDescriptor[] ADD_SPELL_SELECTION_LISTENER_LUA_SCRIPT_PARAMETER_DESCRIPTORS;
    private static final LuaScriptParameterDescriptor[] REMOVE_SPELL_SELECTION_LISTENER_LUA_SCRIPT_PARAMETER_DESCRIPTORS;
    private static final LuaScriptParameterDescriptor[] ADD_LOD_LISTENER_LUA_SCRIPT_PARAMETER_DESCRIPTORS;
    private static final LuaScriptParameterDescriptor[] GET_LOD_LUA_SCRIPT_PARAMETER_DESCRIPTORS;
    private static final LuaScriptParameterDescriptor[] ADD_COMPASS_LUA_SCRIPT_PARAMETER_DESCRIPTORS;
    private static final LuaScriptParameterDescriptor[] ADD_COMPASS_RETURN_LUA_SCRIPT_PARAMETER_DESCRIPTORS;
    private static final LuaScriptParameterDescriptor[] REMOVE_COMPASS_LUA_SCRIPT_PARAMETER_DESCRIPTORS;
    private static final LuaScriptParameterDescriptor[] OPEN_REWARDS_DIALOG_LUA_SCRIPT_PARAMETER_DESCRIPTORS;
    private static final LuaScriptParameterDescriptor[] OPEN_INFO_DIALOG_LUA_SCRIPT_PARAMETER_DESCRIPTORS;
    private static final LuaScriptParameterDescriptor[] ADD_REWARD_LUA_SCRIPT_PARAMETER_DESCRIPTORS;
    private static final LuaScriptParameterDescriptor[] LOAD_TUTORIAL_LUA_SCRIPT_PARAMETER_DESCRIPTORS;
    private static final LuaScriptParameterDescriptor[] ACTIVATE_FOLLOW_ACHIEVEMENT_LUA_SCRIPT_PARAMETER_DESCRIPTORS;
    private static final THashSet<String> m_properties;
    private static final UIFunctionsLibrary m_instance;
    private static int m_interfaceIdGenerator;
    private static int m_particleIdGenerator;
    final TIntObjectHashMap<ParticleDecorator> m_particles;
    
    @Override
    public final String getName() {
        return "UI";
    }
    
    @Override
    public String getDescription() {
        return "NO Description<br/>Please Dev... implement me!";
    }
    
    public static UIFunctionsLibrary getInstance() {
        return UIFunctionsLibrary.m_instance;
    }
    
    private UIFunctionsLibrary() {
        super();
        this.m_particles = new TIntObjectHashMap<ParticleDecorator>();
    }
    
    @Override
    public JavaFunctionEx[] createFunctions(final LuaState luaState) {
        return new JavaFunctionEx[] { new DisplayFresco(luaState), new AddEventListener(luaState), new RemoveEventListener(luaState), new EnableEvent(luaState), new AddColorTween(luaState), new RemoveColorTween(luaState), new AddListColorTween(luaState), new RemoveListColorTween(luaState), new AddParticle(luaState), new RemoveParticle(luaState), new ClearParticles(luaState), new ClickButton(luaState), new LoadDialog(luaState), new UnloadDialog(luaState), new ChangeCursor(luaState), new ManageFrame(luaState), new OpenMRU(luaState), new OpenMRUWithAction(luaState), new ManageSkillMRU(luaState), new ManageMRU(luaState), new AddMRU(luaState), new SetTrainingFightParameters(luaState), new SetTrainingFightBattleGroundCenter(luaState), new SetTrainingFightStartPositions(luaState), new DisplayWideScreenBand(luaState), new CharacterDisplayerOpen(luaState), new CharacterDisplayerClose(luaState), new CharacterDisplayerChangeLinkage(luaState), new AddEquipmentShortcut(luaState), new AddEmoteShortcut(luaState), new AddSpellShortcut(luaState), new SetShortcutUsable(luaState), new OpenAllBags(luaState), new CloseAllBags(luaState), new EnableDragNDrop(luaState), new SetWindowMovable(luaState), new AddDialogLoadedListener(luaState), new RemoveDialogLoadedListener(luaState), new AddDialogUnloadedListener(luaState), new RemoveDialogUnloadedListener(luaState), new GetWindowSize(luaState), new SetWidgetEnabled(luaState), new SetWidgetVisibility(luaState), new RemovePropertyClient(luaState), new SetShortcutEnabled(luaState), new CloseMRU(luaState), new WriteBloopsInChat(luaState), new WriteInChat(luaState), new RemoveMRU(luaState), new RemoveShortCutWithId(luaState), new DisplayCountDown(luaState), new SetWidgetBlockability(luaState), new SystemMessage(luaState), new FollowOnMiniMap(luaState), new RemoveFromMiniMap(luaState), new SetUIVisible(luaState), new DisplaySplashScreenImage(luaState), new GetPropertyValueInt(luaState), new IsBarLockedMode(luaState), new IsDialogLoaded(luaState), new AddItemShortcut(luaState), new SetPropertyBooleanValue(luaState), new SetPropertyStringValue(luaState), new RemoveProperty(luaState), new PushPetMessage(luaState), new SetNextPetMessage(luaState), new HasFightShortcut(luaState), new HasWorldShortcut(luaState), new HasShortcut(luaState), new WriteInEmoteChat(luaState), new WriteUnlocalisedInChat(luaState), new DisplaySmiley(luaState), new DisplayFlyingText(luaState), new HighlightFighterInTimeline(luaState), new DisplayBackground(luaState), new DisplaySplashText(luaState), new StopDungeonLadderTimer(luaState), new DisplayDungeonLadderTimer(luaState), new DisplayDungeonLadderMonsterCount(luaState), new UnloadDungeonStatut(luaState), new IncrementDungeonLadderMonsterCount(luaState), new DisplayDungeonResultSplashText(luaState), new AddSpellSelectionListener(luaState), new RemoveSpellSelectionListener(luaState), new PushStaticProtector(luaState), new AddLODListener(luaState), new GetLOD(luaState), new DisplayChallengeVarUpdateFlyingImage(luaState), new StartCountdown(luaState), new StopScenarioCountdown(luaState), new AddLandMarkNote(luaState), new RemoveLandMarkNote(luaState), new StartInteractiveDialog(luaState), new SetStaticProtectorInterval(luaState), new AddCompass(luaState), new RemoveCompass(luaState), new DisplayTutorialMessage(luaState), new OpenRewardsDialog(luaState), new AddReward(luaState), new OpenInfoDialog(luaState), new LoadTutorialMessageDialog(luaState), new UnloadTutorialMessageDialog(luaState), new ActivateFollowAchievement(luaState), new DisplayButton(luaState), new HideButton(luaState) };
    }
    
    @Nullable
    @Override
    public JavaFunctionEx[] createGlobalFunctions(final LuaState luaState) {
        return null;
    }
    
    private static String getDialogCharacterName(final int id) {
        return "characterDialog" + id;
    }
    
    public void clear() {
        final TIntObjectIterator<ParticleDecorator> iter = this.m_particles.iterator();
        for (int i = this.m_particles.size(); i > 0; --i) {
            iter.advance();
            iter.value().setTimeToLive(0);
        }
        this.m_particles.clear();
        UIFunctionsLibrary.m_interfaceIdGenerator = 0;
        UIFunctionsLibrary.m_particleIdGenerator = 0;
        UIFunctionsLibrary.m_properties.forEach(new TObjectProcedure<String>() {
            @Override
            public boolean execute(final String object) {
                PropertiesProvider.getInstance().removeProperty(object);
                return true;
            }
        });
    }
    
    static {
        m_logger = Logger.getLogger((Class)UIFunctionsLibrary.class);
        START_INTERACTIVE_DIALOGS_LUA_SCRIPT_PARAMETER_DESCRIPTORS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("dialogId", "Id du dialogue", LuaScriptParameterType.INTEGER, false), new LuaScriptParameterDescriptor("sourceType", "1 pour protecteur, 2 pour NPC", LuaScriptParameterType.INTEGER, false), new LuaScriptParameterDescriptor("sourceId", "L'id de la source (pour le protecteur, il ne s'agit pas du groupe de monstre associ?)", LuaScriptParameterType.LONG, false) };
        ADD_LANDMARK_NOTE_RETURN_LUA_SCRIPT_PARAMETER_DESCRIPTORS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("landMarkId", "Id de la note", LuaScriptParameterType.INTEGER, false) };
        DISPLAY_BUTTON_PARAMS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("callFunction", "Fonction \u00e0 appeler lors du clic sur le bouton", LuaScriptParameterType.STRING, false), new LuaScriptParameterDescriptor("callbackOnEscape", "Appuyer sur Echap appelle la callback", LuaScriptParameterType.BOOLEAN, false), new LuaScriptParameterDescriptor("tradKey", "Cl\u00e9 de traduction \u00e0 afficher dans le bouton", LuaScriptParameterType.STRING, true) };
        ADD_LANDMARK_NOTE_LUA_SCRIPT_PARAMETER_DESCRIPTORS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("translationKey", "Cl? de traduction", LuaScriptParameterType.STRING, false), new LuaScriptParameterDescriptor("gfxId", "Id de l'icone", LuaScriptParameterType.INTEGER, false), new LuaScriptParameterDescriptor("worldX", "Position x", LuaScriptParameterType.INTEGER, false), new LuaScriptParameterDescriptor("worldY", "Position y", LuaScriptParameterType.INTEGER, false) };
        DISPLAY_TUTORIAL_MESSAGE_LUA_SCRIPT_PARAMETER_DESCRIPTORS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("titleTranslationKey", "Clef de traduction du titre", LuaScriptParameterType.STRING, false), new LuaScriptParameterDescriptor("textTranslationKey", "Clef de traduction du texte", LuaScriptParameterType.STRING, false) };
        DISPLAY_CHALLENGE_VAR_UPDATE_LUA_SCRIPT_PARAMETER_DESCRIPTORS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("positive", "true affiche un +1, false affiche un -1", LuaScriptParameterType.BOOLEAN, true) };
        REMOVE_LAND_MARK_LUA_SCRIPT_PARAMETER_DESCRIPTORS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("landMarkId", "Id de la note", LuaScriptParameterType.INTEGER, false) };
        DISPLAY_FRESCO_LUA_SCRIPT_PARAMETER_DESCRIPTORS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("frescoId", "Id de la fresque", LuaScriptParameterType.INTEGER, false) };
        START_COUNTDOWN_LUA_SCRIPT_PARAMETER_DESCRIPTORS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("duration", "Dur?e du compte ? rebours", LuaScriptParameterType.INTEGER, false), new LuaScriptParameterDescriptor("startDate", "Date de d?part du compte ? rebours (date courante si non renseign?)", LuaScriptParameterType.LONG, true) };
        HIGHLIGHT_FIGHTER_IN_TIMELINE_LUA_SCRIPT_PARAMETER_DESCRIPTORS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("fighterId", "Id du fighter", LuaScriptParameterType.INTEGER, false), new LuaScriptParameterDescriptor("activated", "s?lectionne si true, d?selectionne si false", LuaScriptParameterType.BOOLEAN, false) };
        DISPLAY_COUNTDOWN_LUA_SCRIPT_PARAMETER_DESCRIPTORS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("duration", "Dur?e du d?compte (en secondes)", LuaScriptParameterType.NUMBER, false), new LuaScriptParameterDescriptor("decrement", "Interval entre deux 'tic' de countdown", LuaScriptParameterType.NUMBER, true) };
        SYSTEM_MESSAGE_LUA_SCRIPT_PARAMETER_DESCRIPTORS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("message", "Message ? afficher", LuaScriptParameterType.STRING, true), new LuaScriptParameterDescriptor("duration", "Dur?e d'affichage du message", LuaScriptParameterType.NUMBER, false) };
        OPEN_MRU_WITH_ACTION_LUA_SCRIPT_PARAMETER_DESCRIPTORS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("elementId", "Id de l'?l?ment interactif sur lequel on ouvre le MRU", LuaScriptParameterType.LONG, false), new LuaScriptParameterDescriptor("eventId", "Id de l'?v?nement LUA g?n?r? par l'activation du MRU", LuaScriptParameterType.INTEGER, false), new LuaScriptParameterDescriptor("gfxId", "gfxId de l'action de MRU", LuaScriptParameterType.INTEGER, false) };
        OPEN_MRU_LUA_SCRIPT_PARAMETER_DESCRIPTORS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("interactiveElementId", "Id de l'?l?ment interactif sur lequel on ouvre le MRU", LuaScriptParameterType.NUMBER, false) };
        MANAGE_SKILL_MRU_LUA_SCRIPT_PARAMETER_DESCRIPTORS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("skillVisualId", "Id de l'action", LuaScriptParameterType.NUMBER, false), new LuaScriptParameterDescriptor("usable", "Utilisable ou non", LuaScriptParameterType.BOOLEAN, false) };
        MANAGE_MRU_LUA_SCRIPT_PARAMETER_DESCRIPTORS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("actionId", "Id de l'action", LuaScriptParameterType.NUMBER, false), new LuaScriptParameterDescriptor("usable", "Utilisable ou non", LuaScriptParameterType.BOOLEAN, false) };
        ADD_MRU_LUA_SCRIPT_PARAMETER_DESCRIPTORS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("charaCcterId", "Id du personnage concern?", LuaScriptParameterType.LONG, false), new LuaScriptParameterDescriptor("actionId", "Id de l'action MRU", LuaScriptParameterType.NUMBER, false), new LuaScriptParameterDescriptor("onlyOnce", "Sp?cifie qu'on ajoutera pas le MRU s'il existe d?j? un avec le m?me id d'action sur ce personnage", LuaScriptParameterType.BOOLEAN, true) };
        REMOVE_MRU_LUA_SCRIPT_PARAMETER_DESCRIPTORS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("characterId", "Id du personnage concern?", LuaScriptParameterType.LONG, false), new LuaScriptParameterDescriptor("actionId", "Id de l'action MRU concern?e", LuaScriptParameterType.NUMBER, false) };
        SET_TRAINING_FIGHT_PARAMETERS_LUA_SCRIPT_PARAMETER_DESCRIPTORS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("trainingActionIndex", "Id de l'action MRU concern?e", LuaScriptParameterType.INTEGER, false), new LuaScriptParameterDescriptor("shouldTeleport", "Indique si les fighters doivent ?tre t?l?port?s dans le combat ou pas", LuaScriptParameterType.BOOLEAN, false), new LuaScriptParameterDescriptor("fightType", "Indique le type de combat (pvp=1,pve=2,tuto=3,etc..cf FightModel)", LuaScriptParameterType.INTEGER, false), new LuaScriptParameterDescriptor("hasBorders", "Indique si le combat doit avoir des bordures", LuaScriptParameterType.BOOLEAN, false), new LuaScriptParameterDescriptor("battlegroundType", "Indique le type de bordure du combat", LuaScriptParameterType.INTEGER, false), new LuaScriptParameterDescriptor("bgParams", "Pattern du battleground", LuaScriptParameterType.BLOOPS, true) };
        SET_TRAINING_FIGHT_BATTLEGROUND_CENTER_LUA_SCRIPT_PARAMETER_DESCRIPTORS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("trainingActionIndex", "Id de l'action MRU concern?e", LuaScriptParameterType.INTEGER, false), new LuaScriptParameterDescriptor("bgX", "x de la position centre du battleground", LuaScriptParameterType.INTEGER, false), new LuaScriptParameterDescriptor("bgY", "y de la position centre du battleground", LuaScriptParameterType.INTEGER, false), new LuaScriptParameterDescriptor("bgZ", "z de la position centre du battleground", LuaScriptParameterType.INTEGER, false) };
        SET_TRAINING_FIGHT_FIGHT_START_POSITIONS_LUA_SCRIPT_PARAMETER_DESCRIPTORS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("trainingActionIndex", "Id de l'action MRU concern?e", LuaScriptParameterType.INTEGER, false), new LuaScriptParameterDescriptor("attackerX", "x de la position de d?part des attaquants", LuaScriptParameterType.INTEGER, false), new LuaScriptParameterDescriptor("attackerY", "y de la position de d?part des attaquants", LuaScriptParameterType.INTEGER, false), new LuaScriptParameterDescriptor("attackerZ", "z de la position de d?part des attaquants", LuaScriptParameterType.INTEGER, false), new LuaScriptParameterDescriptor("defenderX", "x de la position de d?part des d?fenseurs", LuaScriptParameterType.INTEGER, false), new LuaScriptParameterDescriptor("defenderY", "y de la position de d?part des d?fenseurs", LuaScriptParameterType.INTEGER, false), new LuaScriptParameterDescriptor("defenderZ", "z de la position de d?part des d?fenseurs", LuaScriptParameterType.INTEGER, false) };
        MANAGE_FRAME_LUA_SCRIPT_PARAMETER_DESCRIPTORS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("frame", "Frame ? activer", LuaScriptParameterType.STRING, false), new LuaScriptParameterDescriptor("push", "Active ou desactive la frame", LuaScriptParameterType.BOOLEAN, false) };
        ADD_EVENT_LISTENER_LUA_SCRIPT_PARAMETER_DESCRIPTORS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("dialog", "Nom du dialogue", LuaScriptParameterType.STRING, false), new LuaScriptParameterDescriptor("id", "Nom du widget", LuaScriptParameterType.STRING, false), new LuaScriptParameterDescriptor("eventType", "Type d'?v?nement", LuaScriptParameterType.STRING, false), new LuaScriptParameterDescriptor("funcName", "Fonction ? appeler", LuaScriptParameterType.STRING, false), new LuaScriptParameterDescriptor("params", "Param?tres de la fonction ? appeler", LuaScriptParameterType.BLOOPS, true) };
        REMOVE_EVENT_LISTENER_LUA_SCRIPT_PARAMETER_DESCRIPTORS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("dialog", "Nom du dialogue", LuaScriptParameterType.STRING, false), new LuaScriptParameterDescriptor("id", "Nom du widget", LuaScriptParameterType.STRING, false), new LuaScriptParameterDescriptor("eventType", "Type d'evenement", LuaScriptParameterType.STRING, false), new LuaScriptParameterDescriptor("funcName", "Fonction qui devait ?tre appel?e", LuaScriptParameterType.STRING, false) };
        CLICK_BUTTON_LUA_SCRIPT_PARAMETER_DESCRIPTORS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("dialog", "Nom du dialoque", LuaScriptParameterType.STRING, false), new LuaScriptParameterDescriptor("id", "Nom du widget", LuaScriptParameterType.STRING, false), new LuaScriptParameterDescriptor("button", "Bouton de la souris enfonc?", LuaScriptParameterType.NUMBER, true), new LuaScriptParameterDescriptor("clickCount", "Nombre de click", LuaScriptParameterType.NUMBER, true) };
        ENABLE_DRAG_N_DROP_LUA_SCRIPT_PARAMETER_DESCRIPTORS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("dialog", "Nom du dialogue", LuaScriptParameterType.STRING, false), new LuaScriptParameterDescriptor("id", "Nom du widget", LuaScriptParameterType.STRING, false), new LuaScriptParameterDescriptor("enable", "Activation", LuaScriptParameterType.BOOLEAN, false) };
        ENABLE_EVENT_LUA_SCRIPT_PARAMETER_DESCRIPTORS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("dialog", "Nom du dialogue", LuaScriptParameterType.STRING, false), new LuaScriptParameterDescriptor("id", "Nom du wdget", LuaScriptParameterType.STRING, false), new LuaScriptParameterDescriptor("eventType", "Type d'?v?nement", LuaScriptParameterType.STRING, false), new LuaScriptParameterDescriptor("enable", "Activation", LuaScriptParameterType.BOOLEAN, false) };
        CHARACTER_DISPLAYER_OPEN_LUA_SCRIPT_PARAMETER_DESCRIPTORS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("fileName", "Nom du fichier ? ouvrir", LuaScriptParameterType.STRING, false), new LuaScriptParameterDescriptor("linkageName", "Nom de l'anim ? jouer dans l'interface", LuaScriptParameterType.STRING, false), new LuaScriptParameterDescriptor("align", "Alignement de la fenetre", LuaScriptParameterType.STRING, false), new LuaScriptParameterDescriptor("screenXoffset", "D?calage en pixel vers la droite", LuaScriptParameterType.INTEGER, false), new LuaScriptParameterDescriptor("screenYoffset", "D?calage en pixel vers le haut", LuaScriptParameterType.INTEGER, false) };
        CHARACTER_DISPLAYER_CHANGE_LINKAGE_LUA_SCRIPT_PARAMETER_DESCRIPTORS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("id", "Id de la fenetre d'interface", LuaScriptParameterType.INTEGER, false), new LuaScriptParameterDescriptor("animName", "Nom de l'animation ? jouer", LuaScriptParameterType.STRING, false) };
        CHARACTER_DISPLAYER_CLOSE_LUA_SCRIPT_PARAMETER_DESCRIPTORS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("id", "Id de la fenetre d'interface", LuaScriptParameterType.INTEGER, false) };
        LOAD_DIALOG_LUA_SCRIPT_PARAMETER_DESCRIPTORS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("dialog", "Nom de la fenetre ? ouvrir", LuaScriptParameterType.STRING, false) };
        UNLOAD_DIALOG_LUA_SCRIPT_PARAMETER_DESCRIPTORS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("dialogName", "Nom du dialogue", LuaScriptParameterType.STRING, false) };
        SET_WINDOW_MOVABLE_LUA_SCRIPT_PARAMETER_DESCRIPTORS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("dialogName", "Nom du dialogue", LuaScriptParameterType.STRING, false), new LuaScriptParameterDescriptor("movable", "V?rouill?", LuaScriptParameterType.BOOLEAN, false) };
        SET_WIDGET_ENABLED_LUA_SCRIPT_PARAMETER_DESCRIPTORS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("dialogName", "Nom du dialogue", LuaScriptParameterType.STRING, false), new LuaScriptParameterDescriptor("widgetId", "Nom du widget", LuaScriptParameterType.STRING, false), new LuaScriptParameterDescriptor("enabled", "Activation", LuaScriptParameterType.BOOLEAN, false) };
        SET_PROPERTY_BOOLEAN_VALUE_LUA_SCRIPT_PARAMETER_DESCRIPTORS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("propertyName", "Nom de la propri?t?", LuaScriptParameterType.STRING, false), new LuaScriptParameterDescriptor("value", "Valeur", LuaScriptParameterType.BOOLEAN, false) };
        SET_PROPERTY_STRING_VALUE_LUA_SCRIPT_PARAMETER_DESCRIPTORS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("propertyName", "Nom de la propri?t?", LuaScriptParameterType.STRING, false), new LuaScriptParameterDescriptor("value", "Valeur", LuaScriptParameterType.STRING, false) };
        REMOVE_PROPERTY_LUA_SCRIPT_PARAMETER_DESCRIPTORS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("propertyName", "Nom de la propri?t?", LuaScriptParameterType.STRING, false) };
        REMOVE_PROPERTY_CLIENT_LUA_SCRIPT_PARAMETER_DESCRIPTORS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("dialogName", "Nom du dialogue", LuaScriptParameterType.STRING, false), new LuaScriptParameterDescriptor("widgetId", "Nom du wodget", LuaScriptParameterType.STRING, false), new LuaScriptParameterDescriptor("propertyName", "Nom de la propri?t? concern?e", LuaScriptParameterType.STRING, false), new LuaScriptParameterDescriptor("local", "True s'il s'agit d'une propri?t? locale (propre ? un dialog)", LuaScriptParameterType.BOOLEAN, true) };
        GET_PROPERTY_VALUE_INT_LUA_SCRIPT_PARAMETER_DESCRIPTORS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("dialogName", "Nom du dialogue", LuaScriptParameterType.STRING, false), new LuaScriptParameterDescriptor("propertyName", "Nom de la propri?t?", LuaScriptParameterType.STRING, false), new LuaScriptParameterDescriptor("field", "Champ de la propri?t?", LuaScriptParameterType.STRING, false), new LuaScriptParameterDescriptor("local", "True s'il s'agit d'une propri?t? locale (propre ? un dialog)", LuaScriptParameterType.BOOLEAN, true) };
        SET_WIDGET_VISIBILITY_LUA_SCRIPT_PARAMETER_DESCRIPTORS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("dialogName", "Nom du dialogue", LuaScriptParameterType.STRING, false), new LuaScriptParameterDescriptor("widgetId", "Nom du widget", LuaScriptParameterType.STRING, false), new LuaScriptParameterDescriptor("visible", "Visibilit?", LuaScriptParameterType.BOOLEAN, false) };
        SET_WIDGET_BLOCKABILITY_LUA_SCRIPT_PARAMETER_DESCRIPTORS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("dialogName", "Nom du dialogue", LuaScriptParameterType.STRING, false), new LuaScriptParameterDescriptor("widgetId", "Nom du widget", LuaScriptParameterType.STRING, false), new LuaScriptParameterDescriptor("blocking", "true bloque, false ne bloque pas", LuaScriptParameterType.BOOLEAN, false) };
        ADD_LIST_COLOR_TWEEN_LUA_SCRIPT_PARAMETER_DESCRIPTORS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("dialogName", "Nom du dialog", LuaScriptParameterType.STRING, false), new LuaScriptParameterDescriptor("listId", "Id de la liste", LuaScriptParameterType.STRING, false), new LuaScriptParameterDescriptor("subId", "Id de l'?l?ment concern? dans la liste", LuaScriptParameterType.STRING, false), new LuaScriptParameterDescriptor("duration", "Dur?e de tween", LuaScriptParameterType.NUMBER, false), new LuaScriptParameterDescriptor("repeat", "Nombre de r?p?titions du tween (-1 pour infinie)", LuaScriptParameterType.NUMBER, false), new LuaScriptParameterDescriptor("r1", "Valeur rouge de d?part (blanc vers gris si non renseign?)", LuaScriptParameterType.NUMBER, true), new LuaScriptParameterDescriptor("g1", "Valeur vert de d?part (blanc vers gris si non renseign?)", LuaScriptParameterType.NUMBER, true), new LuaScriptParameterDescriptor("b1", "Valeur bleu de d?part (blanc vers gris si non renseign?)", LuaScriptParameterType.NUMBER, true), new LuaScriptParameterDescriptor("a1", "Valeur alpha de d?part (blanc vers gris si non renseign?)", LuaScriptParameterType.NUMBER, true), new LuaScriptParameterDescriptor("r2", "Valeur rouge de fin (blanc vers gris si non renseign?)", LuaScriptParameterType.NUMBER, true), new LuaScriptParameterDescriptor("g2", "Valeur vert de fin (blanc vers gris si non renseign?)", LuaScriptParameterType.NUMBER, true), new LuaScriptParameterDescriptor("b2", "Valeur bleu de fin (blanc vers gris si non renseign?)", LuaScriptParameterType.NUMBER, true), new LuaScriptParameterDescriptor("a2", "Valeur alpha de fin (blanc vers gris si non renseign?)", LuaScriptParameterType.NUMBER, true) };
        REMOVE_LIST_COLOR_TWEEN_LUA_SCRIPT_PARAMETER_DESCRIPTORS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("dialogName", "Nom du dialog", LuaScriptParameterType.STRING, false), new LuaScriptParameterDescriptor("widgetId", "Id de la liste", LuaScriptParameterType.STRING, false), new LuaScriptParameterDescriptor("subId", "Id de l'?l?ment concern? dans la liste", LuaScriptParameterType.STRING, false) };
        ADD_COLOR_TWEEN_LUA_SCRIPT_PARAMETER_DESCRIPTORS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("dialogName", "Nom du dialogue", LuaScriptParameterType.STRING, false), new LuaScriptParameterDescriptor("widgetId", "Nom du widget", LuaScriptParameterType.STRING, false), new LuaScriptParameterDescriptor("duration", "Temps du cycle du tween (en ms)", LuaScriptParameterType.NUMBER, false), new LuaScriptParameterDescriptor("repeat", "Nombre de cycles du tween (-1 = infini)", LuaScriptParameterType.NUMBER, false), new LuaScriptParameterDescriptor("r1", "Teinte 1 rouge", LuaScriptParameterType.NUMBER, true), new LuaScriptParameterDescriptor("g1", "Teinte 1 vert", LuaScriptParameterType.NUMBER, true), new LuaScriptParameterDescriptor("b1", "Teinte 1 bleu", LuaScriptParameterType.NUMBER, true), new LuaScriptParameterDescriptor("a1", "Teinte 1 alpha", LuaScriptParameterType.NUMBER, true), new LuaScriptParameterDescriptor("r2", "Teinte 2 rouge", LuaScriptParameterType.NUMBER, true), new LuaScriptParameterDescriptor("g2", "Teinte 2 vert", LuaScriptParameterType.NUMBER, true), new LuaScriptParameterDescriptor("b2", "Teinte 2 bleu", LuaScriptParameterType.NUMBER, true), new LuaScriptParameterDescriptor("a2", "Teinte 2 alpha", LuaScriptParameterType.NUMBER, true) };
        REMOVE_COLOR_TWEEN_LUA_SCRIPT_PARAMETER_DESCRIPTORS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("dialogName", "Nom du dialogue", LuaScriptParameterType.STRING, false), new LuaScriptParameterDescriptor("widgetId", "Nom du widget", LuaScriptParameterType.STRING, false) };
        ADD_PARTICLE_LUA_SCRIPT_PARAMETER_DESCRIPTORS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("dialogName", "Nom du dialogue", LuaScriptParameterType.STRING, false), new LuaScriptParameterDescriptor("widgetId", "Nom du widget", LuaScriptParameterType.STRING, false), new LuaScriptParameterDescriptor("particleFileName", "Nom du fichier de particule", LuaScriptParameterType.STRING, false), new LuaScriptParameterDescriptor("posX", "Position x", LuaScriptParameterType.NUMBER, false), new LuaScriptParameterDescriptor("posY", "Position y", LuaScriptParameterType.NUMBER, false), new LuaScriptParameterDescriptor("followBorder", "Active le systeme autours du widget", LuaScriptParameterType.BOOLEAN, false), new LuaScriptParameterDescriptor("alignment", "Alignement du systeme", LuaScriptParameterType.STRING, true), new LuaScriptParameterDescriptor("level", "Niveau du systeme", LuaScriptParameterType.NUMBER, true) };
        ADD_PARTICLE_RETURN_LUA_SCRIPT_PARAMETER_DESCRIPTORS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("particleId", "Id de l'APS", LuaScriptParameterType.INTEGER, false) };
        REMOVE_PARTICLE_LUA_SCRIPT_PARAMETER_DESCRIPTORS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("particleId", "Id de l'APS", LuaScriptParameterType.INTEGER, false), new LuaScriptParameterDescriptor("time", "Temps avant disparition (en ms)", LuaScriptParameterType.INTEGER, true) };
        CLEAR_PARTICLES_LUA_SCRIPT_PARAMETER_DESCRIPTORS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("dialogId", "Id de l'interface", LuaScriptParameterType.STRING, false), new LuaScriptParameterDescriptor("widgetId", "Id du widget", LuaScriptParameterType.STRING, false) };
        CHANGE_CURSOR_LUA_SCRIPT_PARAMETER_DESCRIPTORS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("cursorState", "Etat du curseur", LuaScriptParameterType.STRING, false) };
        CHANGE_CURSOR_RETURN_LUA_SCRIPT_PARAMETER_DESCRIPTORS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("cursorState", "Ancien ?tat du curseur", LuaScriptParameterType.STRING, false) };
        DISPLAY_WIDE_SCREEN_BAND_LUA_SCRIPT_PARAMETER_DESCRIPTORS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("show/hide", "Affiche ou masque les bandes", LuaScriptParameterType.BOOLEAN, false), new LuaScriptParameterDescriptor("percentOfScreen", "Pourcentage de l'?cran en noir", LuaScriptParameterType.INTEGER, true), new LuaScriptParameterDescriptor("duration", "Temps de transition (en ms)", LuaScriptParameterType.INTEGER, true) };
        ADD_EQUIPMENT_SHORTCUT_LUA_SCRIPT_PARAMETER_DESCRIPTORS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("referenceType", "Le type de param?tre qui d?finira l'objet (Pos,UID, refid)", LuaScriptParameterType.STRING, false), new LuaScriptParameterDescriptor("itemReference", "Suivant refType, soit la position, soit l'UID, soit l'id de reference", LuaScriptParameterType.LONG, false), new LuaScriptParameterDescriptor("position", "Position ? laquelle le raccourci doit ?tre ajout? dans la barre", LuaScriptParameterType.INTEGER, true) };
        ADD_EMOTE_SHORTCUT_LUA_SCRIPT_PARAMETER_DESCRIPTORS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("emoteId", "Id de l'emote concern?e", LuaScriptParameterType.INTEGER, false), new LuaScriptParameterDescriptor("position", "Position dans la barre de raccourci d'item courante (premi?re place libre si non renseign?)", LuaScriptParameterType.INTEGER, true) };
        REMOVE_SHORTCUT_WITH_ID_LUA_SCRIPT_PARAMETER_DESCRIPTORS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("referenceId", "Id r?f?rent de l'?l?ment qu'on souhaite enlever", LuaScriptParameterType.INTEGER, false), new LuaScriptParameterDescriptor("shortcutType", "Typde de la barre de raccourci concern?e (0: spell, 2: item, 3: slot)", LuaScriptParameterType.INTEGER, false) };
        ADD_SPELL_SHORTCUT_LUA_SCRIPT_PARAMETER_DESCRIPTORS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("referenceType", "Le type de param?tre qui d?finira l'objet (UID, refid)", LuaScriptParameterType.STRING, false), new LuaScriptParameterDescriptor("itemReference", "Suivant refType, soit la position, soit l'UID, soit l'id de reference", LuaScriptParameterType.LONG, false), new LuaScriptParameterDescriptor("position", "Position ? laquelle le raccourci doit ?tre ajout? dans la barre", LuaScriptParameterType.INTEGER, true) };
        ADD_ITEM_SHORTCUT_LUA_SCRIPT_PARAMETER_DESCRIPTORS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("referenceType", "Le type de r?f?rence d'item en param2 (uid=l'unique id de l'item,refId=le refId de l'item)", LuaScriptParameterType.STRING, false), new LuaScriptParameterDescriptor("itemReference", "Id ou refId de l'item", LuaScriptParameterType.LONG, false), new LuaScriptParameterDescriptor("position", "Position dans la barre courante (premi?re position livre si non renseign?)", LuaScriptParameterType.INTEGER, true) };
        HAS_SHORTCUT_LUA_SCRIPT_RESULT_DESCRIPTORS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("hasShortcut", "true si on trouve un raccourci, false sinon", LuaScriptParameterType.BOOLEAN, false) };
        HAS_SHORTCUT_LUA_SCRIPT_PARAMETER_DESCRIPTORS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("referenceId", "Id r?f?rent de l'?l?ment qu'on souhaite v?rifier", LuaScriptParameterType.INTEGER, true), new LuaScriptParameterDescriptor("shortcutType", "Typde de la barre de raccourci concern?e (0: spell, 2: item, 3: slot)", LuaScriptParameterType.INTEGER, true) };
        HAS_WORLD_SHORTCUT_LUA_SCRIPT_RESULT_DESCRIPTORS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("hasShortcut", "true si on trouve un raccourci, false sinon", LuaScriptParameterType.BOOLEAN, false) };
        HAS_WORLD_SHORTCUT_LUA_SCRIPT_PARAMETER_DESCRIPTORS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("referenceId", "Id r?f?rent de l'?l?ment qu'on souhaite v?rifier", LuaScriptParameterType.INTEGER, true), new LuaScriptParameterDescriptor("shortcutType", "Typde de la barre de raccourci concern?e (0: spell, 2: item, 3: slot)", LuaScriptParameterType.INTEGER, true) };
        HAS_FIGHT_SHORTCUT_LUA_SCRIPT_RESULT_DESCRIPTORS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("hasShortcut", "true si on trouve un raccourci, false sinon", LuaScriptParameterType.BOOLEAN, false) };
        HAS_FIGHT_SHORTCUT_LUA_SCRIPT_PARAMETER_DESCRIPTORS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("referenceId", "Id r?f?rent de l'?l?ment qu'on souhaite v?rifier", LuaScriptParameterType.INTEGER, true), new LuaScriptParameterDescriptor("shortcutType", "Typde de la barre de raccourci concern?e (0: spell, 2: item, 3: slot)", LuaScriptParameterType.INTEGER, true) };
        SET_SHORTCUT_USABLE_LUA_SCRIPT_PARAMETER_DESCRIPTORS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("usable", "Active si true, d?sactive si false", LuaScriptParameterType.BOOLEAN, false), new LuaScriptParameterDescriptor("shortCutBarType", "Type de barre de raccourci concern?e (HANDS, HANDS_AND_FIGHT, FIGHT ou WORLD, toutes les barres si non renseign?)", LuaScriptParameterType.STRING, true), new LuaScriptParameterDescriptor("barIndex", "Indice de la barre concern?e", LuaScriptParameterType.INTEGER, true), new LuaScriptParameterDescriptor("position", "Position dans la barre du raccourci concern?", LuaScriptParameterType.INTEGER, true) };
        ADD_DIALOG_UNLOADED_LISTENER_LUA_SCRIPT_PARAMETER_DESCRIPTORS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("dialog", "Nom du dialogue", LuaScriptParameterType.STRING, false), new LuaScriptParameterDescriptor("funcName", "Fonction ? appeler", LuaScriptParameterType.STRING, false), new LuaScriptParameterDescriptor("params", "Param?tres de la fonction ? appaler", LuaScriptParameterType.BLOOPS, true) };
        GET_WINDOW_SIZE_LUA_SCRIPT_PARAMETER_DESCRIPTORS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("dialog", "Nom du dialogue", LuaScriptParameterType.STRING, false) };
        REMOVE_DIALOG_UNLOADED_LISTENER_LUA_SCRIPT_PARAMETER_DESCRIPTORS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("dialog", "Nom du dialogue", LuaScriptParameterType.STRING, false) };
        ADD_DIALOG_LOADED_LISTENER_LUA_SCRIPT_PARAMETER_DESCRIPTORS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("dialog", "Nom du dialogue", LuaScriptParameterType.STRING, false), new LuaScriptParameterDescriptor("funcName", "Fonction a appeler", LuaScriptParameterType.STRING, false), new LuaScriptParameterDescriptor("params", "Parametres de la fonction a appeler", LuaScriptParameterType.BLOOPS, true) };
        REMOVE_DIALOG_LOADED_LISTENER_LUA_SCRIPT_PARAMETER_DESCRIPTORS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("dialog", "Nom du dialogue", LuaScriptParameterType.STRING, false) };
        SET_SHORTCUT_ENABLED_LUA_SCRIPT_PARAMETER_DESCRIPTORS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("enabled", "Activation", LuaScriptParameterType.BOOLEAN, false), new LuaScriptParameterDescriptor("groupName", "Nom du groupe", LuaScriptParameterType.STRING, true), new LuaScriptParameterDescriptor("name", null, LuaScriptParameterType.STRING, true) };
        SET_UI_VISIBLE_LUA_SCRIPT_PARAMETER_DESCRIPTORS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("visible", "true visible, false invisible", LuaScriptParameterType.BOOLEAN, false) };
        WRITE_IN_CHAT_LUA_SCRIPT_PARAMETER_DESCRIPTORS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("message", "La clef de traduction suivie d'?ventuels param?tres", LuaScriptParameterType.BLOOPS, true) };
        WRITE_IN_EMOTE_CHAT_LUA_SCRIPT_PARAMETER_DESCRIPTORS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("message", "La clef de traduction", LuaScriptParameterType.STRING, false), new LuaScriptParameterDescriptor("params", "Les param?tres ?ventuels du message", LuaScriptParameterType.BLOOPS, true) };
        FOLLOW_ON_MINIMAP_LUA_SCRIPT_PARAMETER_DESCRIPTORS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("x", "x de la position du marqueur dans le monde", LuaScriptParameterType.INTEGER, false), new LuaScriptParameterDescriptor("y", "y de la position du marqueur dans le monde", LuaScriptParameterType.INTEGER, false), new LuaScriptParameterDescriptor("z", "z de la position du marqueur dans le monde", LuaScriptParameterType.INTEGER, false), new LuaScriptParameterDescriptor("pointId", "Obsol?te", LuaScriptParameterType.INTEGER, false) };
        REMOVE_FROM_MINIMAP_LUA_SCRIPT_PARAMETER_DESCRIPTORS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("pointId", "Obsol?te", LuaScriptParameterType.INTEGER, false) };
        WRITE_BLOOPS_IN_CHAT_LUA_SCRIPT_PARAMETER_DESCRIPTORS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("message", "Un ou plusieurs messages s?par?s par des virgules", LuaScriptParameterType.BLOOPS, true) };
        WRITE_UNLOCALIZED_IN_CHAT_LUA_SCRIPT_PARAMETER_DESCRIPTORS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("message", "Le message", LuaScriptParameterType.STRING, false), new LuaScriptParameterDescriptor("mobileId", "L'id du mobile concern?", LuaScriptParameterType.LONG, false), new LuaScriptParameterDescriptor("params", "Param?tres ?ventuels du message", LuaScriptParameterType.BLOOPS, true) };
        DISPLAY_SMILEY_LUA_SCRIPT_PARAMETER_DESCRIPTORS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("smileyId", "Id du smiley", LuaScriptParameterType.INTEGER, false), new LuaScriptParameterDescriptor("mobileId", "Id du mobile concern?", LuaScriptParameterType.LONG, false), new LuaScriptParameterDescriptor("familyId", "S'il s'agit d'un smiley de monstre, id de la famille de monstre", LuaScriptParameterType.INTEGER, true) };
        PUSH_STATIC_PROTECTOR_LUA_SCRIPT_PARAMETER_DESCRIPTORS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("protectorId", "L'id du protecteur statique", LuaScriptParameterType.INTEGER, false) };
        SET_STATIC_PROTECTOR_INTERVAL_LUA_SCRIPT_PARAMETER_DESCRIPTORS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("monster", null, LuaScriptParameterType.BOOLEAN, false), new LuaScriptParameterDescriptor("familyId", null, LuaScriptParameterType.INTEGER, false), new LuaScriptParameterDescriptor("minValue", null, LuaScriptParameterType.INTEGER, false), new LuaScriptParameterDescriptor("maxValue", null, LuaScriptParameterType.INTEGER, false) };
        DISPLAY_SPLASH_SCREEN_IMAGE_LUA_SCRIPT_PARAMETER_DESCRIPTORS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("iconUrl", "Url de l'image ? afficher", LuaScriptParameterType.STRING, false) };
        IS_BAR_LOCKED_MODE_LUA_SCRIPT_PARAMETER_DESCRIPTORS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("result", "true si l'interface est verouill?e, false sinon", LuaScriptParameterType.BOOLEAN, false) };
        IS_DIALOG_LOADED_LUA_SCRIPT_PARAMETER_DESCRIPTORS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("dialogName", "Nom du dialog", LuaScriptParameterType.STRING, false) };
        IS_DIALOG_LOADED_RETURN_LUA_SCRIPT_PARAMETER_DESCRIPTORS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("result", "True si le dialog ouvert, false sinon", LuaScriptParameterType.BOOLEAN, false) };
        PUSH_PET_MESSAGE_LUA_SCRIPT_PARAMETER_DESCRIPTORS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("messageParamCount", "Nombre de param?tres dans le message", LuaScriptParameterType.INTEGER, false), new LuaScriptParameterDescriptor("message", "Clef de traduction du message", LuaScriptParameterType.BLOOPS, false), new LuaScriptParameterDescriptor("blocking", "Le message ne peut pas se fermer au clic", LuaScriptParameterType.BOOLEAN, false), new LuaScriptParameterDescriptor("displayTime", "Dur?e d'affichage du message (dynamique en fonction du message si non renseign?)", LuaScriptParameterType.INTEGER, true), new LuaScriptParameterDescriptor("funcName", "Fonctione ? appeler quand on ferme la bulle de dialogue", LuaScriptParameterType.STRING, true), new LuaScriptParameterDescriptor("params", "Les param?tres ?ventuels du message", LuaScriptParameterType.BLOOPS, true) };
        DISPLAY_BACKGROUND_LUA_SCRIPT_PARAMETER_DESCRIPTORS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("displayId", "Id du background", LuaScriptParameterType.INTEGER, false) };
        DUNGEON_RESULT_SPLASH_TEXT_LUA_SCRIPT_PARAMETER_DESCRIPTORS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("position", "Position du groupe de joueurs sur le podium", LuaScriptParameterType.INTEGER, false) };
        SPLASH_TEXT_LUA_SCRIPT_PARAMETER_DESCRIPTORS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("text", "Clef de traduction du message", LuaScriptParameterType.STRING, false), new LuaScriptParameterDescriptor("params", "Les param?tres ?ventuels du message", LuaScriptParameterType.BLOOPS, true) };
        ADD_SPELL_SELECTION_LISTENER_LUA_SCRIPT_PARAMETER_DESCRIPTORS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("blocking", "True emp?che la s?lection effective du sort, false sinon", LuaScriptParameterType.BOOLEAN, false), new LuaScriptParameterDescriptor("funcName", "Nom de la fonction ? appeler quand un sort est s?lectionn?", LuaScriptParameterType.STRING, false), new LuaScriptParameterDescriptor("params", "Parma?tres ?ventuels de la fonction appel?e quand un sort est s?lectionn?", LuaScriptParameterType.BLOOPS, true) };
        REMOVE_SPELL_SELECTION_LISTENER_LUA_SCRIPT_PARAMETER_DESCRIPTORS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("funcName", "Nom de la fonction qui ?coute la s?lection d'un sort", LuaScriptParameterType.STRING, false) };
        ADD_LOD_LISTENER_LUA_SCRIPT_PARAMETER_DESCRIPTORS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("funcName", "La fonction ? appeler quand le niveau de d?tail du jeu change", LuaScriptParameterType.STRING, false) };
        GET_LOD_LUA_SCRIPT_PARAMETER_DESCRIPTORS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("lodLevel", "Le niveau de d?tails actuel du jeu (0,1 ou 2)", LuaScriptParameterType.INTEGER, false) };
        ADD_COMPASS_LUA_SCRIPT_PARAMETER_DESCRIPTORS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("Nom", "Nom (inutilis?)", LuaScriptParameterType.STRING, false), new LuaScriptParameterDescriptor("x", "Position x", LuaScriptParameterType.NUMBER, false), new LuaScriptParameterDescriptor("y", "Position y", LuaScriptParameterType.NUMBER, false), new LuaScriptParameterDescriptor("z", "Position z", LuaScriptParameterType.NUMBER, true), new LuaScriptParameterDescriptor("type", "Le type de boussole (pour l'icone). Valeurs accept?es : 0 (personnage), 1 (havre-sac), 2 (autres), 3 (challenges), 6 (protecteurs)", LuaScriptParameterType.NUMBER, true) };
        ADD_COMPASS_RETURN_LUA_SCRIPT_PARAMETER_DESCRIPTORS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("compassId", "Id de la boussole", LuaScriptParameterType.LONG, false) };
        REMOVE_COMPASS_LUA_SCRIPT_PARAMETER_DESCRIPTORS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("id", "Id de la boussole", LuaScriptParameterType.LONG, false) };
        OPEN_REWARDS_DIALOG_LUA_SCRIPT_PARAMETER_DESCRIPTORS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("translationKey", "Clef de traduction du texte ? afficher", LuaScriptParameterType.STRING, true), new LuaScriptParameterDescriptor("params", "Les param?tres ?ventuels du message", LuaScriptParameterType.BLOOPS, true) };
        OPEN_INFO_DIALOG_LUA_SCRIPT_PARAMETER_DESCRIPTORS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("titleTranslationKey", "Clef de trad du titre du popup", LuaScriptParameterType.STRING, false), new LuaScriptParameterDescriptor("translationKey", "Clef de trad du texte du popup", LuaScriptParameterType.STRING, false), new LuaScriptParameterDescriptor("params", "Params du texte du popup", LuaScriptParameterType.BLOOPS, true) };
        ADD_REWARD_LUA_SCRIPT_PARAMETER_DESCRIPTORS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("item", "RefId de l'item offert", LuaScriptParameterType.NUMBER, false), new LuaScriptParameterDescriptor("translationKey", "clef de traduction du texte de la r?compense", LuaScriptParameterType.STRING, true), new LuaScriptParameterDescriptor("params", "Les param?tres ?ventuels du texte", LuaScriptParameterType.BLOOPS, true) };
        LOAD_TUTORIAL_LUA_SCRIPT_PARAMETER_DESCRIPTORS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("iconName", "Nom de l'image ? afficher (cf dossier tutorial dans les assets)", LuaScriptParameterType.STRING, false), new LuaScriptParameterDescriptor("titleTranslationKey", "clef de traduction du titre du tuto", LuaScriptParameterType.STRING, false), new LuaScriptParameterDescriptor("descriptionTranslationKey", "clef de traduction du texte du tuto", LuaScriptParameterType.STRING, false), new LuaScriptParameterDescriptor("type", "Type de message d'info : (0=tuto, 1=tips, ...)", LuaScriptParameterType.INTEGER, true), new LuaScriptParameterDescriptor("params", "Les param?tres ?ventuels du texte", LuaScriptParameterType.BLOOPS, true) };
        ACTIVATE_FOLLOW_ACHIEVEMENT_LUA_SCRIPT_PARAMETER_DESCRIPTORS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("activate", "Active ou desactive le dialog", LuaScriptParameterType.BOOLEAN, false) };
        m_properties = new THashSet<String>();
        m_instance = new UIFunctionsLibrary();
    }
    
    private static class StartInteractiveDialog extends JavaFunctionEx
    {
        StartInteractiveDialog(final LuaState luaState) {
            super(luaState);
        }
        
        @Override
        public String getName() {
            return "startInteractiveDialog";
        }
        
        @Override
        public String getDescription() {
            return "Affiche une fen?tre de dialogue interactive";
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getParameterDescriptors() {
            return UIFunctionsLibrary.START_INTERACTIVE_DIALOGS_LUA_SCRIPT_PARAMETER_DESCRIPTORS;
        }
        
        @Nullable
        @Override
        public final LuaScriptParameterDescriptor[] getResultDescriptors() {
            return null;
        }
        
        public void run(final int paramCount) throws LuaException {
            final int dialogId = this.getParamInt(0);
            final byte sourceType = (byte)this.getParamInt(1);
            final long sourceId = this.getParamLong(2);
            final StartDialogRequestMessage msg = new StartDialogRequestMessage();
            msg.setDialogId(dialogId);
            msg.setSourceId(sourceId);
            msg.setSourceType(sourceType);
            WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(msg);
        }
    }
    
    private static class DisplayButton extends JavaFunctionEx
    {
        DisplayButton(final LuaState luaState) {
            super(luaState);
        }
        
        @Override
        public String getName() {
            return "displayButton";
        }
        
        @Override
        public String getDescription() {
            return "Affiche un bouton avec la cl\u00e9 de trad en question";
        }
        
        @Nullable
        @Override
        public LuaScriptParameterDescriptor[] getParameterDescriptors() {
            return UIFunctionsLibrary.DISPLAY_BUTTON_PARAMS;
        }
        
        @Nullable
        @Override
        public LuaScriptParameterDescriptor[] getResultDescriptors() {
            return null;
        }
        
        @Override
        protected void run(final int paramCount) throws LuaException {
            final String callFunction = this.getParamString(0);
            final boolean callBackOnEscape = this.getParamBool(1);
            final String tradKey = (paramCount > 2) ? this.getParamString(2) : "scenario.endKinematic";
            UIDisplayButtonFrame.INSTANCE.setTradKey(tradKey);
            UIDisplayButtonFrame.INSTANCE.setScript(this.getScriptObject());
            UIDisplayButtonFrame.INSTANCE.setCallFunction(callFunction);
            UIDisplayButtonFrame.INSTANCE.setCallbackOnEscape(callBackOnEscape);
            if (!WakfuGameEntity.getInstance().hasFrame(UIDisplayButtonFrame.INSTANCE)) {
                WakfuGameEntity.getInstance().pushFrame(UIDisplayButtonFrame.INSTANCE);
            }
        }
    }
    
    private static class HideButton extends JavaFunctionEx
    {
        HideButton(final LuaState luaState) {
            super(luaState);
        }
        
        @Override
        public String getName() {
            return "hideButton";
        }
        
        @Override
        public String getDescription() {
            return "D\u00e9pushe la frame du DisplayButton";
        }
        
        @Nullable
        @Override
        public LuaScriptParameterDescriptor[] getParameterDescriptors() {
            return null;
        }
        
        @Nullable
        @Override
        public LuaScriptParameterDescriptor[] getResultDescriptors() {
            return null;
        }
        
        @Override
        protected void run(final int paramCount) throws LuaException {
            if (WakfuGameEntity.getInstance().hasFrame(UIDisplayButtonFrame.INSTANCE)) {
                WakfuGameEntity.getInstance().removeFrame(UIDisplayButtonFrame.INSTANCE);
            }
        }
    }
    
    private static class AddLandMarkNote extends JavaFunctionEx
    {
        AddLandMarkNote(final LuaState luaState) {
            super(luaState);
        }
        
        @Override
        public String getName() {
            return "addLandMarkNote";
        }
        
        @Override
        public String getDescription() {
            return "Ajoute une note personnelle";
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getParameterDescriptors() {
            return UIFunctionsLibrary.ADD_LANDMARK_NOTE_LUA_SCRIPT_PARAMETER_DESCRIPTORS;
        }
        
        @Override
        public final LuaScriptParameterDescriptor[] getResultDescriptors() {
            return UIFunctionsLibrary.ADD_LANDMARK_NOTE_RETURN_LUA_SCRIPT_PARAMETER_DESCRIPTORS;
        }
        
        public void run(final int paramCount) throws LuaException {
            final String text = WakfuTranslator.getInstance().getString(this.getParamString(0));
            final int gfxId = this.getParamInt(1);
            final int worldX = this.getParamInt(2);
            final int worldY = this.getParamInt(3);
            final LandMarkNote note = MapManager.getInstance().getLandMarkHandler().addNote(worldX, worldY, gfxId, text);
            this.addReturnValue(note.getId());
        }
    }
    
    private static class RemoveLandMarkNote extends JavaFunctionEx
    {
        RemoveLandMarkNote(final LuaState luaState) {
            super(luaState);
        }
        
        @Override
        public String getName() {
            return "removeLandMarkNote";
        }
        
        @Override
        public String getDescription() {
            return "Retire une note personnelle";
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getParameterDescriptors() {
            return UIFunctionsLibrary.REMOVE_LAND_MARK_LUA_SCRIPT_PARAMETER_DESCRIPTORS;
        }
        
        @Nullable
        @Override
        public final LuaScriptParameterDescriptor[] getResultDescriptors() {
            return null;
        }
        
        public void run(final int paramCount) throws LuaException {
            final int landMarkId = this.getParamInt(0);
            if (!LandMarkNoteManager.getInstance().removeNote(landMarkId)) {
                UIFunctionsLibrary.m_logger.warn((Object)("Tentative de suppression d'un landmark d'id " + landMarkId + " non trouv?."));
            }
        }
    }
    
    private static class DisplayTutorialMessage extends JavaFunctionEx
    {
        DisplayTutorialMessage(final LuaState luaState) {
            super(luaState);
        }
        
        @Override
        public String getName() {
            return "displayTutorialMessage";
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getParameterDescriptors() {
            return UIFunctionsLibrary.DISPLAY_TUTORIAL_MESSAGE_LUA_SCRIPT_PARAMETER_DESCRIPTORS;
        }
        
        @Override
        public String getDescription() {
            return "Utilise le syst?me d'affichage popup pour afficher un message titr?";
        }
        
        @Nullable
        @Override
        public final LuaScriptParameterDescriptor[] getResultDescriptors() {
            return null;
        }
        
        public void run(final int paramCount) throws LuaException {
            if (!WakfuClientInstance.getInstance().getGamePreferences().getBooleanValue(WakfuKeyPreferenceStoreEnum.TIPS_ACTIVATED)) {
                return;
            }
            final String title = WakfuTranslator.getInstance().getString(this.getParamString(0));
            final String message = WakfuTranslator.getInstance().getString(this.getParamString(1));
            final UINotificationMessage msg = new UINotificationMessage(title, message, NotificationMessageType.TUTORIAL);
            Worker.getInstance().pushMessage(msg);
        }
    }
    
    private static class DisplayChallengeVarUpdateFlyingImage extends JavaFunctionEx
    {
        DisplayChallengeVarUpdateFlyingImage(final LuaState luaState) {
            super(luaState);
        }
        
        @Override
        public String getName() {
            return "displayChallengeVarUpdateFlyingImage";
        }
        
        @Override
        public String getDescription() {
            return "Affiche l'image volante d'update de variable de challenge";
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getParameterDescriptors() {
            return UIFunctionsLibrary.DISPLAY_CHALLENGE_VAR_UPDATE_LUA_SCRIPT_PARAMETER_DESCRIPTORS;
        }
        
        @Nullable
        @Override
        public final LuaScriptParameterDescriptor[] getResultDescriptors() {
            return null;
        }
        
        public void run(final int paramCount) throws LuaException {
            boolean positive = true;
            if (paramCount >= 1) {
                positive = this.getParamBool(0);
            }
            AreaChallengeInformation.getInstance().varUpdated(positive);
        }
    }
    
    private static class DisplayFresco extends JavaFunctionEx
    {
        DisplayFresco(final LuaState luaState) {
            super(luaState);
        }
        
        @Override
        public String getName() {
            return "displayFresco";
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getParameterDescriptors() {
            return UIFunctionsLibrary.DISPLAY_FRESCO_LUA_SCRIPT_PARAMETER_DESCRIPTORS;
        }
        
        @Override
        public String getDescription() {
            return "Affiche une fr?sque background manipulable horizontalement ? la souris";
        }
        
        @Nullable
        @Override
        public final LuaScriptParameterDescriptor[] getResultDescriptors() {
            return null;
        }
        
        public void run(final int paramCount) throws LuaException {
            final int frescoId = this.getParamInt(0);
            UIFrescoFrame.getInstance().setFrescoId(frescoId);
            WakfuGameEntity.getInstance().pushFrame(UIFrescoFrame.getInstance());
        }
    }
    
    private static class StartCountdown extends JavaFunctionEx
    {
        StartCountdown(final LuaState luaState) {
            super(luaState);
        }
        
        @Override
        public String getName() {
            return "startCountdown";
        }
        
        @Override
        public String getDescription() {
            return "Affiche un compte ? rebours g?n?rique";
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getParameterDescriptors() {
            return UIFunctionsLibrary.START_COUNTDOWN_LUA_SCRIPT_PARAMETER_DESCRIPTORS;
        }
        
        @Nullable
        @Override
        public final LuaScriptParameterDescriptor[] getResultDescriptors() {
            return null;
        }
        
        public void run(final int paramCount) throws LuaException {
            final int duration = this.getParamInt(0);
            final GameDateConst startDate = (paramCount >= 2) ? GameDate.fromLong(this.getParamLong(1)) : WakfuGameCalendar.getInstance().getDate();
            AllAroundCountdown.getInstance().start(duration, startDate);
        }
    }
    
    private static class StopScenarioCountdown extends JavaFunctionEx
    {
        StopScenarioCountdown(final LuaState luaState) {
            super(luaState);
        }
        
        @Override
        public String getName() {
            return "stopCountdown";
        }
        
        @Override
        public String getDescription() {
            return "Arr?te et efface le compte ? rebours g?n?rique";
        }
        
        @Nullable
        @Override
        public LuaScriptParameterDescriptor[] getParameterDescriptors() {
            return null;
        }
        
        @Nullable
        @Override
        public final LuaScriptParameterDescriptor[] getResultDescriptors() {
            return null;
        }
        
        public void run(final int paramCount) throws LuaException {
            AllAroundCountdown.getInstance().stop();
        }
    }
    
    private static class HighlightFighterInTimeline extends JavaFunctionEx
    {
        HighlightFighterInTimeline(final LuaState luaState) {
            super(luaState);
        }
        
        @Override
        public String getName() {
            return "highlightFighterInTimeline";
        }
        
        @Override
        public String getDescription() {
            return "S?lectionne un personnage dans la timeLine";
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getParameterDescriptors() {
            return UIFunctionsLibrary.HIGHLIGHT_FIGHTER_IN_TIMELINE_LUA_SCRIPT_PARAMETER_DESCRIPTORS;
        }
        
        @Nullable
        @Override
        public final LuaScriptParameterDescriptor[] getResultDescriptors() {
            return null;
        }
        
        public void run(final int paramCount) throws LuaException {
            final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
            final Fight currentFight = localPlayer.getCurrentFight();
            final CharacterInfo characterInfo = CharacterInfoManager.getInstance().getCharacter(this.getParamLong(0));
            if (characterInfo == null) {
                return;
            }
            if (currentFight != null && currentFight.containsFighter(characterInfo)) {
                currentFight.getTimeline().highlightFighter(characterInfo, this.getParamBool(1));
            }
        }
    }
    
    private static class DisplayCountDown extends JavaFunctionEx
    {
        private int m_currentTime;
        private int m_decrement;
        private final CountDownHandler m_handler;
        
        DisplayCountDown(final LuaState luaState) {
            super(luaState);
            this.m_decrement = 1;
            this.m_handler = new CountDownHandler();
        }
        
        @Override
        public String getName() {
            return "displayCountdown";
        }
        
        @Override
        public String getDescription() {
            return "Affiche un d?compte";
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getParameterDescriptors() {
            return UIFunctionsLibrary.DISPLAY_COUNTDOWN_LUA_SCRIPT_PARAMETER_DESCRIPTORS;
        }
        
        @Nullable
        @Override
        public final LuaScriptParameterDescriptor[] getResultDescriptors() {
            return null;
        }
        
        private void nextCountdown() {
            WakfuSystemMessageManager.getInstance().showMessage(new SystemMessageData(WakfuSystemMessageManager.SystemMessageType.FIGHT_INFO, String.valueOf(this.m_currentTime), this.m_currentTime * 1000));
            this.m_currentTime -= this.m_decrement;
        }
        
        public void run(final int paramCount) throws LuaException {
            this.m_currentTime = this.getParamInt(0);
            this.m_decrement = ((paramCount == 2) ? this.getParamInt(1) : 1);
            this.nextCountdown();
            MessageScheduler.getInstance().addClock(this.m_handler, this.m_decrement * 1000, -1, this.m_currentTime / this.m_decrement + 1);
        }
        
        private class CountDownHandler implements MessageHandler
        {
            @Override
            public boolean onMessage(final Message message) {
                DisplayCountDown.this.nextCountdown();
                return true;
            }
            
            @Override
            public long getId() {
                return 1L;
            }
            
            @Override
            public void setId(final long id) {
            }
        }
    }
    
    private static class SystemMessage extends JavaFunctionEx
    {
        SystemMessage(final LuaState luaState) {
            super(luaState);
        }
        
        @Override
        public String getName() {
            return "systemMessage";
        }
        
        @Override
        public String getDescription() {
            return "Affiche un message admin NON LOCALIS? sur l'?cran";
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getParameterDescriptors() {
            return UIFunctionsLibrary.SYSTEM_MESSAGE_LUA_SCRIPT_PARAMETER_DESCRIPTORS;
        }
        
        @Nullable
        @Override
        public final LuaScriptParameterDescriptor[] getResultDescriptors() {
            return null;
        }
        
        public void run(final int paramCount) throws LuaException {
            final String msg = this.getParamString(0);
            final int duration = (paramCount >= 2) ? this.getParamInt(1) : 5000;
            WakfuSystemMessageManager.getInstance().showMessage(new SystemMessageData(WakfuSystemMessageManager.SystemMessageType.ADMIN_INFO, msg, duration));
        }
    }
    
    private static class OpenMRUWithAction extends JavaFunctionEx
    {
        OpenMRUWithAction(final LuaState luaState) {
            super(luaState);
        }
        
        @Override
        public String getName() {
            return "openMRUWithAction";
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getParameterDescriptors() {
            return UIFunctionsLibrary.OPEN_MRU_WITH_ACTION_LUA_SCRIPT_PARAMETER_DESCRIPTORS;
        }
        
        @Override
        public String getDescription() {
            return "Ouvre une MRU qui lance un script sur un ?l?ment interactif du jeu";
        }
        
        @Nullable
        @Override
        public final LuaScriptParameterDescriptor[] getResultDescriptors() {
            return null;
        }
        
        public void run(final int paramCount) throws LuaException {
            final MRU mru = new MRU();
            final long elemId = this.getParamLong(0);
            final int eventId = this.getParamInt(1);
            final int gfxId = this.getParamInt(2);
            final ClientInteractiveAnimatedElementSceneView elem = AnimatedElementSceneViewManager.getInstance().getElement(elemId);
            if (elem == null) {
                UIFunctionsLibrary.m_logger.warn((Object)("impossible de trouver l'interactiveElement d'id " + elemId));
                return;
            }
            final MRUScriptAction action = (MRUScriptAction)MRUActions.SCRIPT_ACTION.getModel().getCopy();
            action.setGfxId(gfxId);
            action.setEventId(eventId);
            final AbstractMRUAction[] actions = { action };
            mru.add(actions, elem);
            if (mru.isDisplayable()) {
                mru.display();
            }
        }
    }
    
    private static class OpenMRU extends JavaFunctionEx
    {
        OpenMRU(final LuaState luaState) {
            super(luaState);
        }
        
        @Override
        public String getName() {
            return "openMRU";
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getParameterDescriptors() {
            return UIFunctionsLibrary.OPEN_MRU_LUA_SCRIPT_PARAMETER_DESCRIPTORS;
        }
        
        @Override
        public String getDescription() {
            return "Ouvre un MRU non d?fini sur un ?l?ment interactif du jeu";
        }
        
        @Nullable
        @Override
        public final LuaScriptParameterDescriptor[] getResultDescriptors() {
            return null;
        }
        
        public void run(final int paramCount) throws LuaException {
            final MRU mru = new MRU();
            final long id = this.getParamLong(0);
            final ClientInteractiveAnimatedElementSceneView elem = AnimatedElementSceneViewManager.getInstance().getElement(id);
            if (elem == null) {
                UIFunctionsLibrary.m_logger.warn((Object)("impossible de trouver l'interactiveElement d'id " + id));
                return;
            }
            UIMRUFrame.addToMRU(mru, elem);
            if (mru.isDisplayable()) {
                mru.display();
            }
        }
    }
    
    private static class ManageSkillMRU extends JavaFunctionEx
    {
        ManageSkillMRU(final LuaState luaState) {
            super(luaState);
        }
        
        @Override
        public String getName() {
            return "manageSkillMRU";
        }
        
        @Override
        public String getDescription() {
            return "Active/Desactive une action MRU";
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getParameterDescriptors() {
            return UIFunctionsLibrary.MANAGE_SKILL_MRU_LUA_SCRIPT_PARAMETER_DESCRIPTORS;
        }
        
        @Nullable
        @Override
        public final LuaScriptParameterDescriptor[] getResultDescriptors() {
            return null;
        }
        
        public void run(final int paramCount) throws LuaException {
            final int visualId = this.getParamInt(0);
            final ActionVisual visual = ActionVisualManager.getInstance().get(visualId);
            if (visual == null) {
                UIFunctionsLibrary.m_logger.error((Object)("[GD] Mauvais param?tre, le visuel " + visualId + " n'existe pas"));
                return;
            }
            visual.setEnabled(this.getParamBool(1));
        }
    }
    
    private static class ManageMRU extends JavaFunctionEx
    {
        ManageMRU(final LuaState luaState) {
            super(luaState);
        }
        
        @Override
        public String getName() {
            return "manageMRU";
        }
        
        @Override
        public String getDescription() {
            return "Active/D?sactive des actions du MRU en fonction de leur ID";
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getParameterDescriptors() {
            return UIFunctionsLibrary.MANAGE_MRU_LUA_SCRIPT_PARAMETER_DESCRIPTORS;
        }
        
        @Nullable
        @Override
        public final LuaScriptParameterDescriptor[] getResultDescriptors() {
            return null;
        }
        
        public void run(final int paramCount) throws LuaException {
            final int actionId = this.getParamInt(0);
            final MRUActions[] arr$;
            final MRUActions[] actions = arr$ = MRUActions.values();
            for (final MRUActions action : arr$) {
                if (action.getActionId() == actionId) {
                    action.setUsable(this.getParamBool(1));
                    break;
                }
            }
        }
    }
    
    private static class CloseMRU extends JavaFunctionEx
    {
        CloseMRU(final LuaState luaState) {
            super(luaState);
        }
        
        @Override
        public String getName() {
            return "closeMRU";
        }
        
        @Override
        public String getDescription() {
            return "Ferme le MRU courant s'il est affich?";
        }
        
        @Nullable
        @Override
        public LuaScriptParameterDescriptor[] getParameterDescriptors() {
            return null;
        }
        
        @Nullable
        @Override
        public final LuaScriptParameterDescriptor[] getResultDescriptors() {
            return null;
        }
        
        public void run(final int paramCount) throws LuaException {
            UIFunctionsLibrary.m_logger.info((Object)"Fermeture de tous les MRU");
            UIMRUFrame.getInstance().closeCurrentMRU();
        }
    }
    
    private static class AddMRU extends JavaFunctionEx
    {
        AddMRU(final LuaState luaState) {
            super(luaState);
        }
        
        @Override
        public String getName() {
            return "addMRU";
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getParameterDescriptors() {
            return UIFunctionsLibrary.ADD_MRU_LUA_SCRIPT_PARAMETER_DESCRIPTORS;
        }
        
        @Override
        public String getDescription() {
            return "Ajoute un MRU d'action d?finie ? un personnage";
        }
        
        @Nullable
        @Override
        public final LuaScriptParameterDescriptor[] getResultDescriptors() {
            return null;
        }
        
        public void run(final int paramCount) throws LuaException {
            final long characterId = this.getParamLong(0);
            final int actionId = this.getParamInt(1);
            final CharacterInfo character = CharacterInfoManager.getInstance().getCharacter(characterId);
            final AbstractMRUAction[] abCharActions = character.getMRUActions();
            final MRUActions[] charActions = new MRUActions[abCharActions.length];
            for (int i = 0; i < abCharActions.length; ++i) {
                charActions[i] = abCharActions[i].tag();
            }
            MRUActions action = null;
            final MRUActions[] actions = MRUActions.values();
            for (int j = 0; j < actions.length; ++j) {
                if (actions[j].getActionId() == actionId) {
                    action = actions[j];
                    break;
                }
            }
            boolean alreadyAdded = false;
            if (paramCount < 3 || this.getParamBool(2)) {
                for (int k = 0; k < charActions.length; ++k) {
                    if (action != null) {
                        if (charActions[k].getActionId() == action.getModel().tag().getActionId()) {
                            alreadyAdded = true;
                            break;
                        }
                    }
                }
            }
            if (!alreadyAdded) {
                character.addMRUAction(action);
            }
        }
    }
    
    private static class RemoveMRU extends JavaFunctionEx
    {
        RemoveMRU(final LuaState luaState) {
            super(luaState);
        }
        
        @Override
        public String getName() {
            return "removeMRU";
        }
        
        @Override
        public String getDescription() {
            return "Enl?ve un MRU d'action sp?cifi?e au personnage sp?cifi?";
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getParameterDescriptors() {
            return UIFunctionsLibrary.REMOVE_MRU_LUA_SCRIPT_PARAMETER_DESCRIPTORS;
        }
        
        @Nullable
        @Override
        public final LuaScriptParameterDescriptor[] getResultDescriptors() {
            return null;
        }
        
        public void run(final int paramCount) throws LuaException {
            final long characterId = this.getParamLong(0);
            final int actionId = this.getParamInt(1);
            final CharacterInfo character = CharacterInfoManager.getInstance().getCharacter(characterId);
            if (character != null) {
                character.removeMRUAction(actionId);
            }
        }
    }
    
    private static class SetTrainingFightParameters extends JavaFunctionEx
    {
        SetTrainingFightParameters(final LuaState luaState) {
            super(luaState);
        }
        
        @Override
        public String getName() {
            return "setTrainingParameters";
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getParameterDescriptors() {
            return UIFunctionsLibrary.SET_TRAINING_FIGHT_PARAMETERS_LUA_SCRIPT_PARAMETER_DESCRIPTORS;
        }
        
        @Override
        public String getDescription() {
            return "Sp?cifie les param?tres d'un combat d'entra?nement en les fournissant ? l'action MRU qui correspond";
        }
        
        @Nullable
        @Override
        public final LuaScriptParameterDescriptor[] getResultDescriptors() {
            return null;
        }
        
        public void run(final int paramCount) throws LuaException {
            final int actionIndex = this.getParamInt(0);
            MRUCastTrainingFightAction actions;
            if (actionIndex == MRUActions.CHARACTER_CAST_TRAINING_FIGHT_ACTION.getActionId()) {
                actions = (MRUCastTrainingFightAction)MRUActions.CHARACTER_CAST_TRAINING_FIGHT_ACTION.getModel();
            }
            else {
                if (actionIndex != MRUActions.CHARACTER_CAST_TRAINING_FIGHT_ACTION_2.getActionId()) {
                    UIFunctionsLibrary.m_logger.error((Object)"Il n'y a que 2 actions diff?rentes de combat d'entrainement");
                    return;
                }
                actions = (MRUCastTrainingFightAction)MRUActions.CHARACTER_CAST_TRAINING_FIGHT_ACTION_2.getModel();
            }
            actions.setTeleportFighters(this.getParamBool(1));
            actions.setFightType(this.getParamInt(2));
            actions.setWithBorders(this.getParamBool(3));
            actions.setBattleGroundType((byte)this.getParamInt(4));
            final LuaValue[] params = this.getParams(5, paramCount);
            int[] bgParams = null;
            if (params.length > 0) {
                bgParams = new int[params.length];
                for (int i = 0; i < params.length; ++i) {
                    if (params[i].getType() == LuaScriptParameterType.INTEGER || params[i].getType() == LuaScriptParameterType.NUMBER) {
                        bgParams[i] = (int)params[i].getValue();
                    }
                    else {
                        UIFunctionsLibrary.m_logger.error((Object)"Les param?tres de battleground doivent imp?rativement ?tre de type nombre");
                        bgParams[i] = 0;
                    }
                }
            }
            actions.setBgParams(bgParams);
        }
    }
    
    private static class SetTrainingFightBattleGroundCenter extends JavaFunctionEx
    {
        SetTrainingFightBattleGroundCenter(final LuaState luaState) {
            super(luaState);
        }
        
        @Override
        public String getName() {
            return "setBattlegroundCenter";
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getParameterDescriptors() {
            return UIFunctionsLibrary.SET_TRAINING_FIGHT_BATTLEGROUND_CENTER_LUA_SCRIPT_PARAMETER_DESCRIPTORS;
        }
        
        @Override
        public String getDescription() {
            return "Sp?cifie la position du centre du battleground d'un combat d'entra?nement";
        }
        
        @Nullable
        @Override
        public final LuaScriptParameterDescriptor[] getResultDescriptors() {
            return null;
        }
        
        public void run(final int paramCount) throws LuaException {
            final int actionIndex = this.getParamInt(0);
            MRUCastTrainingFightAction actions;
            if (actionIndex == MRUActions.CHARACTER_CAST_TRAINING_FIGHT_ACTION.getActionId()) {
                actions = (MRUCastTrainingFightAction)MRUActions.CHARACTER_CAST_TRAINING_FIGHT_ACTION.getModel();
            }
            else {
                if (actionIndex != MRUActions.CHARACTER_CAST_TRAINING_FIGHT_ACTION_2.getActionId()) {
                    UIFunctionsLibrary.m_logger.error((Object)"Il n'y a que 2 actions diff?rentes de combat d'entrainement");
                    return;
                }
                actions = (MRUCastTrainingFightAction)MRUActions.CHARACTER_CAST_TRAINING_FIGHT_ACTION_2.getModel();
            }
            actions.setBattlegroundCenter(new Point3(this.getParamInt(1), this.getParamInt(2), (short)this.getParamInt(3)));
        }
    }
    
    private static class SetTrainingFightStartPositions extends JavaFunctionEx
    {
        SetTrainingFightStartPositions(final LuaState luaState) {
            super(luaState);
        }
        
        @Override
        public String getName() {
            return "setStartPositions";
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getParameterDescriptors() {
            return UIFunctionsLibrary.SET_TRAINING_FIGHT_FIGHT_START_POSITIONS_LUA_SCRIPT_PARAMETER_DESCRIPTORS;
        }
        
        @Override
        public String getDescription() {
            return "Sp?cifie la position des attaquants et d?fenseurs d'un combat d'entra?nement";
        }
        
        @Nullable
        @Override
        public final LuaScriptParameterDescriptor[] getResultDescriptors() {
            return null;
        }
        
        public void run(final int paramCount) throws LuaException {
            final int actionIndex = this.getParamInt(0);
            MRUCastTrainingFightAction actions;
            if (actionIndex == MRUActions.CHARACTER_CAST_TRAINING_FIGHT_ACTION.getActionId()) {
                actions = (MRUCastTrainingFightAction)MRUActions.CHARACTER_CAST_TRAINING_FIGHT_ACTION.getModel();
            }
            else {
                if (actionIndex != MRUActions.CHARACTER_CAST_TRAINING_FIGHT_ACTION_2.getActionId()) {
                    UIFunctionsLibrary.m_logger.error((Object)"Il n'y a que 2 actions diff?rentes de combat d'entrainement");
                    return;
                }
                actions = (MRUCastTrainingFightAction)MRUActions.CHARACTER_CAST_TRAINING_FIGHT_ACTION_2.getModel();
            }
            actions.setAttackerPos(new Point3(this.getParamInt(1), this.getParamInt(2), (short)this.getParamInt(3)));
            actions.setDefenderPos(new Point3(this.getParamInt(4), this.getParamInt(5), (short)this.getParamInt(6)));
        }
    }
    
    private static class ManageFrame extends JavaFunctionEx
    {
        ManageFrame(final LuaState luaState) {
            super(luaState);
        }
        
        @Override
        public String getName() {
            return "manageFrame";
        }
        
        @Override
        public String getDescription() {
            return "Active ou d?sactive des frames, donc des types d'interactions entre l'utilisateur et le jeu.";
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getParameterDescriptors() {
            return UIFunctionsLibrary.MANAGE_FRAME_LUA_SCRIPT_PARAMETER_DESCRIPTORS;
        }
        
        @Nullable
        @Override
        public final LuaScriptParameterDescriptor[] getResultDescriptors() {
            return null;
        }
        
        public void run(final int paramCount) throws LuaException {
            final String frame = this.getParamString(0);
            MessageFrame msgFrame;
            if ("FightItemUse".equalsIgnoreCase(frame)) {
                msgFrame = UIFightItemUseInteractionFrame.getInstance();
            }
            else if ("ControlCenter".equalsIgnoreCase(frame)) {
                msgFrame = UIControlCenterContainerFrame.getInstance();
            }
            else if ("Chat".equalsIgnoreCase(frame)) {
                msgFrame = UIChatFrame.getInstance();
            }
            else if ("Shortcut".equalsIgnoreCase(frame)) {
                msgFrame = UIShortcutBarFrame.getInstance();
            }
            else if ("World".equalsIgnoreCase(frame)) {
                msgFrame = UIWorldInteractionFrame.getInstance();
            }
            else if ("FightMovementFrame".equalsIgnoreCase(frame)) {
                msgFrame = UIFightMovementFrame.getInstance();
            }
            else if ("Flea".equalsIgnoreCase(frame)) {
                msgFrame = UIBrowseFleaFrame.getInstance();
            }
            else if ("Equipment".equalsIgnoreCase(frame)) {
                msgFrame = UIEquipmentFrame.getInstance();
            }
            else if ("CharacterSheet".equalsIgnoreCase(frame)) {
                msgFrame = UICharacterSheetFrame.getInstance();
            }
            else if ("CharacterBookSpellDetails".equalsIgnoreCase(frame)) {
                msgFrame = UISpellsPageFrame.getInstance();
            }
            else if ("CharacterSpells".equalsIgnoreCase(frame)) {
                msgFrame = UISpellsPageFrame.getInstance();
            }
            else if ("TemporaryInventory".equalsIgnoreCase(frame)) {
                msgFrame = UITemporaryInventoryFrame.getInstance();
            }
            else if ("FightEndFrame".equalsIgnoreCase(frame)) {
                msgFrame = UIFightEndFrame.getInstance();
            }
            else if ("SystemBar".equalsIgnoreCase(frame)) {
                msgFrame = UISystemBarFrame.getInstance();
            }
            else if ("MRU".equalsIgnoreCase(frame)) {
                msgFrame = UIMRUFrame.getInstance();
            }
            else if ("Menu".equalsIgnoreCase(frame)) {
                msgFrame = UIMenuFrame.getInstance();
            }
            else if ("UITimelineFrame".equalsIgnoreCase(frame)) {
                msgFrame = UITimelineFrame.getInstance();
            }
            else {
                msgFrame = ("Protector".equalsIgnoreCase(frame) ? UIProtectorFrame.getInstance() : null);
            }
            final boolean push = this.getParamBool(1);
            UIFunctionsLibrary.m_logger.info((Object)(push ? ("On remet la frame " + frame) : ("On enl?ve la frame " + frame)));
            if (msgFrame != null) {
                if (push) {
                    if (!WakfuGameEntity.getInstance().hasFrame(msgFrame)) {
                        WakfuGameEntity.getInstance().pushFrame(msgFrame);
                    }
                    else {
                        this.writeError(UIFunctionsLibrary.m_logger, " On essaie de pousser une frame qui est d?j? l?. C'est soit un bug, soit une erreur dans un sc?nario.");
                    }
                }
                else {
                    WakfuGameEntity.getInstance().removeFrame(msgFrame);
                }
            }
            else {
                this.writeError(UIFunctionsLibrary.m_logger, "Cette frame n'est pas support?e par manageFrame " + frame);
            }
        }
    }
    
    private static class AddEventListener extends JavaFunctionEx
    {
        AddEventListener(final LuaState luaState) {
            super(luaState);
        }
        
        @Override
        public String getName() {
            return "addEventListener";
        }
        
        @Override
        public String getDescription() {
            return "Permet d'ajouter une fonction sur un ?v?nement sur un widget. On ne peut pas enregistrer 2 fois la m?me fonction pour un m?me type d'?v?nement sur un widget d'un dialogue";
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getParameterDescriptors() {
            return UIFunctionsLibrary.ADD_EVENT_LISTENER_LUA_SCRIPT_PARAMETER_DESCRIPTORS;
        }
        
        @Nullable
        @Override
        public final LuaScriptParameterDescriptor[] getResultDescriptors() {
            return null;
        }
        
        public void run(final int paramCount) throws LuaException {
            final String dialogName = this.getParamString(0);
            final ElementMap map = Xulor.getInstance().getEnvironment().getElementMap(dialogName);
            if (map == null) {
                this.writeError(UIFunctionsLibrary.m_logger, "Dialogue inconnu " + dialogName);
                return;
            }
            final String controlId = this.getParamString(1);
            final EventDispatcher ed = map.getElement(controlId);
            if (ed == null) {
                this.writeError(UIFunctionsLibrary.m_logger, "ElementDispatcher inconnu " + controlId + " dans le dialog " + dialogName);
                return;
            }
            final String eventTypeName = this.getParamString(2);
            final boolean clickOnceAndDbl = "MOUSE_CLICKED_AND_DOUBLE_CLICKED".equals(eventTypeName);
            final Events eventType = clickOnceAndDbl ? Events.MOUSE_CLICKED : Events.valueOf(eventTypeName);
            if (eventType == null) {
                this.writeError(UIFunctionsLibrary.m_logger, "Type d'evnement inconnu " + eventTypeName + " pour le dialog " + dialogName);
                return;
            }
            final String funcName = this.getParamString(3);
            final LuaValue[] params = this.getParams(4, paramCount);
            final LuaScript script = this.getScriptObject();
            EventListener el = (EventListener)ScriptUIEventManager.getInstance().eventListenerAlreadyExist(script, dialogName, controlId, eventTypeName, funcName);
            if (el == null) {
                el = new LuaCallback(script, funcName, params);
                ScriptUIEventManager.getInstance().putEventListener(script, dialogName, controlId, eventTypeName, funcName, el);
                ed.addEventListener(eventType, el, false);
                if (clickOnceAndDbl) {
                    ed.addEventListener(Events.MOUSE_DOUBLE_CLICKED, el, false);
                }
            }
            else {
                ((LuaCallback)el).setParams(params);
                final ArrayList<EventListener> listenerArrayList = ed.getListeners(eventType, false);
                if (listenerArrayList == null || !listenerArrayList.contains(el)) {
                    ed.addEventListener(eventType, el, false);
                    if (clickOnceAndDbl) {
                        ed.addEventListener(Events.MOUSE_DOUBLE_CLICKED, el, false);
                    }
                }
            }
        }
    }
    
    private static class RemoveEventListener extends JavaFunctionEx
    {
        RemoveEventListener(final LuaState luaState) {
            super(luaState);
        }
        
        @Override
        public String getName() {
            return "removeEventListener";
        }
        
        @Override
        public String getDescription() {
            return "Permet de retirer une fonction sur un ?v?nement sur un widget.";
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getParameterDescriptors() {
            return UIFunctionsLibrary.REMOVE_EVENT_LISTENER_LUA_SCRIPT_PARAMETER_DESCRIPTORS;
        }
        
        @Nullable
        @Override
        public final LuaScriptParameterDescriptor[] getResultDescriptors() {
            return null;
        }
        
        public void run(final int paramCount) throws LuaException {
            final String dialogName = this.getParamString(0);
            final ElementMap map = Xulor.getInstance().getEnvironment().getElementMap(dialogName);
            if (map == null) {
                this.writeError(UIFunctionsLibrary.m_logger, "Dialogue inconnu " + dialogName);
                return;
            }
            final String controlId = this.getParamString(1);
            final EventDispatcher ed = map.getElement(controlId);
            if (ed == null) {
                this.writeError(UIFunctionsLibrary.m_logger, "ElementDispatcher inconnu " + controlId + " dans le dialog " + dialogName);
                return;
            }
            final String eventTypeName = this.getParamString(2);
            final boolean clickOnceAndDbl = "MOUSE_CLICKED_AND_DOUBLE_CLICKED".equals(eventTypeName);
            final Events eventType = clickOnceAndDbl ? Events.MOUSE_CLICKED : Events.valueOf(eventTypeName);
            if (eventType == null) {
                this.writeError(UIFunctionsLibrary.m_logger, "Type d'evnement inconnu " + eventTypeName + " pour le dialog " + dialogName);
                return;
            }
            final String funcName = this.getParamString(3);
            final LuaScript script = this.getScriptObject();
            final EventListener el = (EventListener)ScriptUIEventManager.getInstance().removeEventListener(script, dialogName, controlId, eventTypeName, funcName);
            if (el == null) {
                this.writeError(UIFunctionsLibrary.m_logger, "Le Listener a d?j? ?t? enlev?");
                return;
            }
            ed.removeEventListener(eventType, el, false);
            if (clickOnceAndDbl) {
                ed.removeEventListener(Events.MOUSE_DOUBLE_CLICKED, el, false);
            }
        }
    }
    
    private static class ClickButton extends JavaFunctionEx
    {
        ClickButton(final LuaState luaState) {
            super(luaState);
        }
        
        @Override
        public String getName() {
            return "clickButton";
        }
        
        @Override
        public String getDescription() {
            return "Simule un clique sur un bouton";
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getParameterDescriptors() {
            return UIFunctionsLibrary.CLICK_BUTTON_LUA_SCRIPT_PARAMETER_DESCRIPTORS;
        }
        
        @Nullable
        @Override
        public final LuaScriptParameterDescriptor[] getResultDescriptors() {
            return null;
        }
        
        public void run(final int paramCount) throws LuaException {
            final ElementMap map = Xulor.getInstance().getEnvironment().getElementMap(this.getParamString(0));
            if (map == null) {
                return;
            }
            final EventDispatcher ed = map.getElement(this.getParamString(1));
            if (ed instanceof Button) {
                if (paramCount > 2) {
                    int button = 0;
                    switch (this.getParamInt(2)) {
                        case 2: {
                            button = 2;
                            break;
                        }
                        case 3: {
                            button = 3;
                            break;
                        }
                        default: {
                            button = 1;
                            break;
                        }
                    }
                    ((Button)ed).performClick(button, (paramCount == 4) ? this.getParamInt(3) : 1, 0);
                }
                else {
                    ((Button)ed).performClick();
                }
            }
        }
    }
    
    private static class EnableDragNDrop extends JavaFunctionEx
    {
        EnableDragNDrop(final LuaState luaState) {
            super(luaState);
        }
        
        @Override
        public String getName() {
            return "enableDragNDrop";
        }
        
        @Override
        public String getDescription() {
            return "Permet de d?sactiver/r?activer le dragNDrop sur une List ou un RenderableContainer";
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getParameterDescriptors() {
            return UIFunctionsLibrary.ENABLE_DRAG_N_DROP_LUA_SCRIPT_PARAMETER_DESCRIPTORS;
        }
        
        @Nullable
        @Override
        public final LuaScriptParameterDescriptor[] getResultDescriptors() {
            return null;
        }
        
        public void run(final int paramCount) throws LuaException {
            final ElementMap map = Xulor.getInstance().getEnvironment().getElementMap(this.getParamString(0));
            if (map == null) {
                return;
            }
            final EventDispatcher ed = map.getElement(this.getParamString(1));
            if (ed != null) {
                if (ed instanceof List) {
                    ((List)ed).setEnableDND(this.getParamBool(2));
                }
                else if (ed instanceof RenderableContainer) {
                    ((RenderableContainer)ed).setEnableDND(this.getParamBool(2));
                }
                else {
                    this.writeError(UIFunctionsLibrary.m_logger, this.getParamString(0) + '.' + this.getParamString(1) + " n'est pas du bon type !");
                }
            }
            else {
                this.writeError(UIFunctionsLibrary.m_logger, this.getParamString(0) + '.' + this.getParamString(1) + " est introuvable !");
            }
        }
    }
    
    private static class EnableEvent extends JavaFunctionEx
    {
        EnableEvent(final LuaState luaState) {
            super(luaState);
        }
        
        @Override
        public String getName() {
            return "enableEvent";
        }
        
        @Override
        public String getDescription() {
            return "Permet de d?sactiver/r?activer des ?v?nements sur un widget.";
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getParameterDescriptors() {
            return UIFunctionsLibrary.ENABLE_EVENT_LUA_SCRIPT_PARAMETER_DESCRIPTORS;
        }
        
        @Nullable
        @Override
        public LuaScriptParameterDescriptor[] getResultDescriptors() {
            return null;
        }
        
        @Override
        protected void run(final int paramCount) throws LuaException {
            final String dialogName = this.getParamString(0);
            final ElementMap map = Xulor.getInstance().getEnvironment().getElementMap(dialogName);
            if (map == null) {
                this.writeError(UIFunctionsLibrary.m_logger, "Dialogue inconnu " + dialogName);
                return;
            }
            final String controlId = this.getParamString(1);
            final EventDispatcher ed = map.getElement(controlId);
            if (ed == null) {
                this.writeError(UIFunctionsLibrary.m_logger, "ElementDispatcher inconnu " + controlId + " dans le dialog " + dialogName);
                return;
            }
            final String eventTypeName = this.getParamString(2);
            final Events eventType = Events.valueOf(eventTypeName);
            if (eventType == null) {
                this.writeError(UIFunctionsLibrary.m_logger, "Type d'evenement inconnu " + eventTypeName + " pour le dialog " + dialogName);
                return;
            }
            ed.enableEvent(eventType, this.getParamBool(3));
        }
    }
    
    private static class CharacterDisplayerOpen extends JavaFunctionEx
    {
        CharacterDisplayerOpen(final LuaState luaState) {
            super(luaState);
        }
        
        @Override
        public String getName() {
            return "characterDisplayerOpen";
        }
        
        @Override
        public String getDescription() {
            return "Affiche une fenetre d'interface dans laquelle on place un perso";
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getParameterDescriptors() {
            return UIFunctionsLibrary.CHARACTER_DISPLAYER_OPEN_LUA_SCRIPT_PARAMETER_DESCRIPTORS;
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getResultDescriptors() {
            return new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("id", null, LuaScriptParameterType.INTEGER, false) };
        }
        
        @Override
        protected void run(final int paramCount) throws LuaException {
            final int id = ++UIFunctionsLibrary.m_interfaceIdGenerator;
            final String dialogName = getDialogCharacterName(id);
            final Widget widget = (Widget)Xulor.getInstance().load(dialogName, Dialogs.getDialogPath("emissaryTutoDialog"), (short)10000);
            final String linkage = this.getParamString(1);
            final String filePath = this.getParamString(0) + ".anm";
            final String animName = linkage.substring(2);
            final int direction = Integer.parseInt(linkage.substring(0, 1));
            PropertiesProvider.getInstance().setLocalPropertyValue("filePath", filePath, dialogName);
            PropertiesProvider.getInstance().setLocalPropertyValue("animName", animName, dialogName);
            PropertiesProvider.getInstance().setLocalPropertyValue("direction", direction, dialogName);
            final Alignment17 align = Alignment17.valueOf(this.getParamString(2));
            final int xoffset = this.getParamInt(3);
            final int yoffset = this.getParamInt(4);
            final StaticLayoutData sld = new StaticLayoutData();
            sld.onCheckOut();
            sld.setAlign(align);
            sld.setXOffset(xoffset);
            sld.setYOffset(yoffset);
            widget.add(sld);
            this.addReturnValue(id);
        }
    }
    
    private static class CharacterDisplayerChangeLinkage extends JavaFunctionEx
    {
        CharacterDisplayerChangeLinkage(final LuaState luaState) {
            super(luaState);
        }
        
        @Override
        public String getName() {
            return "characterDisplayerChangeLinkage";
        }
        
        @Override
        public String getDescription() {
            return "Change l'animation d'un perso dans une interface";
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getParameterDescriptors() {
            return UIFunctionsLibrary.CHARACTER_DISPLAYER_CHANGE_LINKAGE_LUA_SCRIPT_PARAMETER_DESCRIPTORS;
        }
        
        @Nullable
        @Override
        public LuaScriptParameterDescriptor[] getResultDescriptors() {
            return null;
        }
        
        @Override
        protected void run(final int paramCount) throws LuaException {
            final int id = this.getParamInt(0);
            final String linkage = this.getParamString(1);
            final String dialogName = getDialogCharacterName(id);
            final String animName = linkage.substring(2);
            final int direction = Integer.parseInt(linkage.substring(0, 1));
            PropertiesProvider.getInstance().setLocalPropertyValue("animName", animName, dialogName);
            PropertiesProvider.getInstance().setLocalPropertyValue("direction", direction, dialogName);
        }
    }
    
    private static class CharacterDisplayerClose extends JavaFunctionEx
    {
        CharacterDisplayerClose(final LuaState luaState) {
            super(luaState);
        }
        
        @Override
        public String getName() {
            return "characterDisplayerClose";
        }
        
        @Override
        public String getDescription() {
            return "Ferme une fenetre d'interface dans laquelle on place un perso";
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getParameterDescriptors() {
            return UIFunctionsLibrary.CHARACTER_DISPLAYER_CLOSE_LUA_SCRIPT_PARAMETER_DESCRIPTORS;
        }
        
        @Nullable
        @Override
        public LuaScriptParameterDescriptor[] getResultDescriptors() {
            return null;
        }
        
        @Override
        protected void run(final int paramCount) throws LuaException {
            final int id = this.getParamInt(0);
            Xulor.getInstance().unload(getDialogCharacterName(id));
        }
    }
    
    private static class LoadDialog extends JavaFunctionEx
    {
        LoadDialog(final LuaState luaState) {
            super(luaState);
        }
        
        @Override
        public String getName() {
            return "loadDialog";
        }
        
        @Override
        public String getDescription() {
            return "Ouvre une fen?tre de dialogue si elle n'est pas ouverte";
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getParameterDescriptors() {
            return UIFunctionsLibrary.LOAD_DIALOG_LUA_SCRIPT_PARAMETER_DESCRIPTORS;
        }
        
        @Nullable
        @Override
        public final LuaScriptParameterDescriptor[] getResultDescriptors() {
            return null;
        }
        
        @Override
        protected void run(final int paramCount) throws LuaException {
            final String dialogName = this.getParamString(0);
            String fileName = null;
            short modalLevel = -1;
            if (dialogName.equals("emissaryTutoDialog")) {
                fileName = "emissaryTutoDialog.xml";
                modalLevel = 10000;
            }
            else if (dialogName.equals("fleaDialog")) {
                fileName = "fleaDialog.xml";
                modalLevel = 10000;
            }
            else if (dialogName.equals("verticalFollowedAchievementsDialog")) {
                fileName = "verticalFollowedAchievementsDialog.xml";
                modalLevel = 10000;
            }
            else {
                this.writeError(UIFunctionsLibrary.m_logger, "Ce dialogue n'est pas pris en charge");
            }
            assert fileName != null && modalLevel > 0;
            if (Xulor.getInstance().isLoaded(dialogName)) {
                UIFunctionsLibrary.m_logger.info((Object)("dialog " + dialogName + " d?j? ouvert"));
                return;
            }
            Xulor.getInstance().load(dialogName, fileName, modalLevel);
        }
    }
    
    private static class UnloadDialog extends JavaFunctionEx
    {
        UnloadDialog(final LuaState luaState) {
            super(luaState);
        }
        
        @Override
        public String getName() {
            return "unloadDialog";
        }
        
        @Override
        public String getDescription() {
            return "Supprime un dialogue";
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getParameterDescriptors() {
            return UIFunctionsLibrary.UNLOAD_DIALOG_LUA_SCRIPT_PARAMETER_DESCRIPTORS;
        }
        
        @Nullable
        @Override
        public final LuaScriptParameterDescriptor[] getResultDescriptors() {
            return null;
        }
        
        @Override
        protected void run(final int paramCount) throws LuaException {
            final String dialogName = this.getParamString(0);
            Xulor.getInstance().unload(dialogName);
        }
    }
    
    private static class SetWindowMovable extends JavaFunctionEx
    {
        SetWindowMovable(final LuaState luaState) {
            super(luaState);
        }
        
        @Override
        public String getName() {
            return "setWindowMovable";
        }
        
        @Override
        public String getDescription() {
            return "Permet de verrouiller ou d?verrouiller une fen?tre de l'interface.";
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getParameterDescriptors() {
            return UIFunctionsLibrary.SET_WINDOW_MOVABLE_LUA_SCRIPT_PARAMETER_DESCRIPTORS;
        }
        
        @Nullable
        @Override
        public final LuaScriptParameterDescriptor[] getResultDescriptors() {
            return null;
        }
        
        @Override
        protected void run(final int paramCount) throws LuaException {
            final String dialogName = this.getParamString(0);
            final boolean movable = this.getParamBool(1);
            final EventDispatcher eventDispatcher = Xulor.getInstance().getLoadedElement(dialogName);
            if (eventDispatcher instanceof Window) {
                final Window w = (Window)eventDispatcher;
                w.setMovable(movable);
            }
            else {
                UIFunctionsLibrary.m_logger.error((Object)("Impossible de trouver la fen?tre '" + dialogName + "' pour la rendre movable=" + movable));
            }
        }
    }
    
    private static class SetWidgetEnabled extends JavaFunctionEx
    {
        SetWidgetEnabled(final LuaState luaState) {
            super(luaState);
        }
        
        @Override
        public String getName() {
            return "setWidgetEnabled";
        }
        
        @Override
        public String getDescription() {
            return "Permet d'activer ou d?sactiver un widget (par exempel un bouton) dans une interface.";
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getParameterDescriptors() {
            return UIFunctionsLibrary.SET_WIDGET_ENABLED_LUA_SCRIPT_PARAMETER_DESCRIPTORS;
        }
        
        @Nullable
        @Override
        public final LuaScriptParameterDescriptor[] getResultDescriptors() {
            return null;
        }
        
        @Override
        protected void run(final int paramCount) throws LuaException {
            final String dialogName = this.getParamString(0);
            final String widgetId = this.getParamString(1);
            final boolean enabled = this.getParamBool(2);
            final ElementMap map = Xulor.getInstance().getEnvironment().getElementMap(dialogName);
            if (map == null) {
                this.writeError(UIFunctionsLibrary.m_logger, "Dialogue inconnu " + dialogName);
                return;
            }
            final EventDispatcher ed = map.getElement(widgetId);
            if (ed == null) {
                this.writeError(UIFunctionsLibrary.m_logger, "ElementDispatcher inconnu " + widgetId + " dans le dialog " + dialogName);
                return;
            }
            if (!(ed instanceof Widget)) {
                this.writeError(UIFunctionsLibrary.m_logger, "le widget n'est pas du type Widget");
                return;
            }
            final Widget w = (Widget)ed;
            w.setEnabled(enabled);
        }
    }
    
    private static class SetPropertyBooleanValue extends JavaFunctionEx
    {
        SetPropertyBooleanValue(final LuaState luaState) {
            super(luaState);
        }
        
        @Override
        public String getName() {
            return "setPropertyBooleanValue";
        }
        
        @Override
        public String getDescription() {
            return "Applique une valeure bool?enne ? une propri?t? xulor";
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getParameterDescriptors() {
            return UIFunctionsLibrary.SET_PROPERTY_BOOLEAN_VALUE_LUA_SCRIPT_PARAMETER_DESCRIPTORS;
        }
        
        @Nullable
        @Override
        public final LuaScriptParameterDescriptor[] getResultDescriptors() {
            return null;
        }
        
        public void run(final int paramCount) throws LuaException {
            final String propertyName = this.getParamString(0);
            final boolean value = this.getParamBool(1);
            UIFunctionsLibrary.m_properties.add(propertyName);
            Property property = PropertiesProvider.getInstance().getProperty(propertyName);
            if (property == null) {
                property = new Property(propertyName, null);
                property.setDispatchValueEvents(true);
                PropertiesProvider.getInstance().addProperty(property);
            }
            property.setValue(value, false);
        }
    }
    
    private static class SetPropertyStringValue extends JavaFunctionEx
    {
        SetPropertyStringValue(final LuaState luaState) {
            super(luaState);
        }
        
        @Override
        public String getName() {
            return "setPropertyStringValue";
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getParameterDescriptors() {
            return UIFunctionsLibrary.SET_PROPERTY_STRING_VALUE_LUA_SCRIPT_PARAMETER_DESCRIPTORS;
        }
        
        @Override
        public String getDescription() {
            return "Applique une valeure String ? une propri?t? xulor";
        }
        
        @Nullable
        @Override
        public final LuaScriptParameterDescriptor[] getResultDescriptors() {
            return null;
        }
        
        public void run(final int paramCount) throws LuaException {
            final String propertyName = this.getParamString(0);
            final String value = this.getParamString(1);
            UIFunctionsLibrary.m_properties.add(propertyName);
            Property property = PropertiesProvider.getInstance().getProperty(propertyName);
            if (property == null) {
                property = new Property(propertyName, null);
                property.setDispatchValueEvents(true);
                PropertiesProvider.getInstance().addProperty(property);
            }
            property.setValue(value, false);
        }
    }
    
    private static class RemoveProperty extends JavaFunctionEx
    {
        RemoveProperty(final LuaState luaState) {
            super(luaState);
        }
        
        @Override
        public String getName() {
            return "removeProperty";
        }
        
        @Override
        public String getDescription() {
            return "Supprime une propri?t? Xulor";
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getParameterDescriptors() {
            return UIFunctionsLibrary.REMOVE_PROPERTY_LUA_SCRIPT_PARAMETER_DESCRIPTORS;
        }
        
        @Nullable
        @Override
        public final LuaScriptParameterDescriptor[] getResultDescriptors() {
            return null;
        }
        
        public void run(final int paramCount) throws LuaException {
            final String propertyName = this.getParamString(0);
            UIFunctionsLibrary.m_properties.remove(propertyName);
            PropertiesProvider.getInstance().removeProperty(propertyName);
        }
    }
    
    private static class RemovePropertyClient extends JavaFunctionEx
    {
        RemovePropertyClient(final LuaState luaState) {
            super(luaState);
        }
        
        @Override
        public String getName() {
            return "removePropertyClient";
        }
        
        @Override
        public String getDescription() {
            return "Supprime un ?l?ment d'interface sp?cifi? de la liste des clients d'une propri?t?";
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getParameterDescriptors() {
            return UIFunctionsLibrary.REMOVE_PROPERTY_CLIENT_LUA_SCRIPT_PARAMETER_DESCRIPTORS;
        }
        
        @Nullable
        @Override
        public final LuaScriptParameterDescriptor[] getResultDescriptors() {
            return null;
        }
        
        @Override
        protected void run(final int paramCount) throws LuaException {
            final String dialogName = this.getParamString(0);
            final String widgetId = this.getParamString(1);
            final String propertyName = this.getParamString(2);
            boolean local = false;
            if (paramCount > 3) {
                local = this.getParamBool(3);
            }
            final ElementMap map = Xulor.getInstance().getEnvironment().getElementMap(dialogName);
            if (map == null) {
                this.writeError(UIFunctionsLibrary.m_logger, "ElementMap inconnue " + dialogName);
                return;
            }
            final Property property = local ? PropertiesProvider.getInstance().getProperty(propertyName, dialogName) : PropertiesProvider.getInstance().getProperty(propertyName);
            if (property == null) {
                this.writeError(UIFunctionsLibrary.m_logger, "Propri?t?e inconnue " + propertyName);
                return;
            }
            final EventDispatcher ed = map.getElement(widgetId);
            if (ed == null) {
                this.writeError(UIFunctionsLibrary.m_logger, "ElementDispatcher inconnu " + widgetId + " dans le dialog " + dialogName);
                return;
            }
            property.removePropertyClient(ed);
        }
    }
    
    private static class GetPropertyValueInt extends JavaFunctionEx
    {
        GetPropertyValueInt(final LuaState luaState) {
            super(luaState);
        }
        
        @Override
        public String getName() {
            return "getPropertyValueInt";
        }
        
        @Override
        public String getDescription() {
            return "Fourni la valeur enti?re d'une propri?t? sp?cifi?e";
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getParameterDescriptors() {
            return UIFunctionsLibrary.GET_PROPERTY_VALUE_INT_LUA_SCRIPT_PARAMETER_DESCRIPTORS;
        }
        
        @Override
        public final LuaScriptParameterDescriptor[] getResultDescriptors() {
            return new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("propertyValue", null, LuaScriptParameterType.INTEGER, false) };
        }
        
        @Override
        protected void run(final int paramCount) throws LuaException {
            final String dialogName = this.getParamString(0);
            final String propertyName = this.getParamString(1);
            final String fieldName = this.getParamString(2);
            boolean local = false;
            if (paramCount > 3) {
                local = this.getParamBool(3);
            }
            final ElementMap map = Xulor.getInstance().getEnvironment().getElementMap(dialogName);
            if (map == null) {
                this.writeError(UIFunctionsLibrary.m_logger, "ElementMap inconnue " + dialogName);
                return;
            }
            final Property property = local ? PropertiesProvider.getInstance().getProperty(propertyName, dialogName) : PropertiesProvider.getInstance().getProperty(propertyName);
            if (property == null) {
                this.writeError(UIFunctionsLibrary.m_logger, "Propri?t?e inconnue " + propertyName);
                return;
            }
            final int value = (property.getFieldObjectValue(fieldName) == null) ? property.getInt() : property.getFieldIntValue(fieldName);
            this.addReturnValue(value);
        }
    }
    
    private static class SetWidgetVisibility extends JavaFunctionEx
    {
        SetWidgetVisibility(final LuaState luaState) {
            super(luaState);
        }
        
        @Override
        public String getName() {
            return "setWidgetVisibility";
        }
        
        @Override
        public String getDescription() {
            return "Permet de rendre visible ou invisible un widget";
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getParameterDescriptors() {
            return UIFunctionsLibrary.SET_WIDGET_VISIBILITY_LUA_SCRIPT_PARAMETER_DESCRIPTORS;
        }
        
        @Nullable
        @Override
        public final LuaScriptParameterDescriptor[] getResultDescriptors() {
            return null;
        }
        
        @Override
        protected void run(final int paramCount) throws LuaException {
            final String dialogName = this.getParamString(0);
            final String widgetId = this.getParamString(1);
            final boolean visible = this.getParamBool(2);
            final ElementMap map = Xulor.getInstance().getEnvironment().getElementMap(dialogName);
            if (map == null) {
                this.writeError(UIFunctionsLibrary.m_logger, "Dialogue inconnu " + dialogName);
                return;
            }
            final EventDispatcher ed = map.getElement(widgetId);
            if (ed == null) {
                this.writeError(UIFunctionsLibrary.m_logger, "ElementDispatcher inconnu " + widgetId + " dans le dialog " + dialogName);
                return;
            }
            if (!(ed instanceof Widget)) {
                this.writeError(UIFunctionsLibrary.m_logger, "le widget n'est pas du type Widget");
                return;
            }
            final Widget w = (Widget)ed;
            w.setVisible(visible);
        }
    }
    
    private static class SetWidgetBlockability extends JavaFunctionEx
    {
        SetWidgetBlockability(final LuaState luaState) {
            super(luaState);
        }
        
        @Override
        public String getName() {
            return "setWidgetBlockability";
        }
        
        @Override
        public String getDescription() {
            return "Sp?cifie si un widget sp?cifi? bloque ou non les interactions de clic de souris";
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getParameterDescriptors() {
            return UIFunctionsLibrary.SET_WIDGET_BLOCKABILITY_LUA_SCRIPT_PARAMETER_DESCRIPTORS;
        }
        
        @Nullable
        @Override
        public final LuaScriptParameterDescriptor[] getResultDescriptors() {
            return null;
        }
        
        @Override
        protected void run(final int paramCount) throws LuaException {
            final String dialogName = this.getParamString(0);
            final String widgetId = this.getParamString(1);
            final boolean blocking = this.getParamBool(2);
            final ElementMap map = Xulor.getInstance().getEnvironment().getElementMap(dialogName);
            if (map == null) {
                this.writeError(UIFunctionsLibrary.m_logger, "Dialogue inconnu " + dialogName);
                return;
            }
            final EventDispatcher ed = map.getElement(widgetId);
            if (ed == null) {
                this.writeError(UIFunctionsLibrary.m_logger, "ElementDispatcher inconnu " + widgetId + " dans le dialog " + dialogName);
                return;
            }
            if (!(ed instanceof Widget)) {
                this.writeError(UIFunctionsLibrary.m_logger, "le widget n'est pas du type Widget");
                return;
            }
            final Widget w = (Widget)ed;
            w.setNonBlocking(!blocking);
        }
    }
    
    private static class OpenAllBags extends JavaFunctionEx
    {
        OpenAllBags(final LuaState luaState) {
            super(luaState);
        }
        
        @Override
        public String getName() {
            return "openAllBags";
        }
        
        @Override
        public String getDescription() {
            return "Ouvre la fen?tre d'inventaires";
        }
        
        @Nullable
        @Override
        public LuaScriptParameterDescriptor[] getParameterDescriptors() {
            return null;
        }
        
        @Nullable
        @Override
        public final LuaScriptParameterDescriptor[] getResultDescriptors() {
            return null;
        }
        
        @Override
        protected void run(final int paramCount) throws LuaException {
            UIEquipmentFrame.getInstance().openEquipment();
        }
    }
    
    private static class CloseAllBags extends JavaFunctionEx
    {
        CloseAllBags(final LuaState luaState) {
            super(luaState);
        }
        
        @Override
        public String getName() {
            return "closeAllBags";
        }
        
        @Override
        public String getDescription() {
            return "Ferme la fen?tre des inventaires";
        }
        
        @Nullable
        @Override
        public LuaScriptParameterDescriptor[] getParameterDescriptors() {
            return null;
        }
        
        @Nullable
        @Override
        public final LuaScriptParameterDescriptor[] getResultDescriptors() {
            return null;
        }
        
        @Override
        protected void run(final int paramCount) throws LuaException {
            WakfuGameEntity.getInstance().removeFrame(UIEquipmentFrame.getInstance());
        }
    }
    
    private static class AddListColorTween extends JavaFunctionEx
    {
        AddListColorTween(final LuaState luaState) {
            super(luaState);
        }
        
        @Override
        public String getName() {
            return "addListColorTween";
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getParameterDescriptors() {
            return UIFunctionsLibrary.ADD_LIST_COLOR_TWEEN_LUA_SCRIPT_PARAMETER_DESCRIPTORS;
        }
        
        @Override
        public String getDescription() {
            return "Ajoute un colorTween aux ?l?ments d'une liste";
        }
        
        @Nullable
        @Override
        public final LuaScriptParameterDescriptor[] getResultDescriptors() {
            return null;
        }
        
        @Override
        protected void run(final int paramCount) throws LuaException {
            final String dialogName = this.getParamString(0);
            final ElementMap map = Xulor.getInstance().getEnvironment().getElementMap(dialogName);
            if (map == null) {
                this.writeError(UIFunctionsLibrary.m_logger, "Dialogue inconnu " + dialogName);
                return;
            }
            final String controlId = this.getParamString(1);
            final EventDispatcher ed = map.getElement(controlId);
            if (ed == null) {
                this.writeError(UIFunctionsLibrary.m_logger, "EventDispatcher inconnu " + controlId + " dans le dialog " + dialogName);
                return;
            }
            if (!(ed instanceof List)) {
                this.writeError(UIFunctionsLibrary.m_logger, "l'EventDispatcher n'est pas du type Widget");
                return;
            }
            final List list = (List)ed;
            final String subId = this.getParamString(2);
            final int duration = this.getParamInt(3);
            final int repeat = this.getParamInt(4);
            final Color c1 = new Color();
            final Color c2 = new Color();
            if (paramCount == 13) {
                c1.setFromFloat((float)this.getParamDouble(5), (float)this.getParamDouble(6), (float)this.getParamDouble(7), (float)this.getParamDouble(8));
                c2.setFromFloat((float)this.getParamDouble(9), (float)this.getParamDouble(10), (float)this.getParamDouble(11), (float)this.getParamDouble(12));
            }
            else {
                c1.set(Color.WHITE.get());
                c2.set(Color.LIGHT_GRAY.get());
            }
            final ArrayList<RenderableContainer> renderables = list.getRenderables();
            for (int i = 0, size = renderables.size(); i < size; ++i) {
                final RenderableContainer renderable = renderables.get(i);
                if (renderable != null) {
                    final ElementMap innerElementMap = renderable.getInnerElementMap();
                    if (innerElementMap != null) {
                        final Widget w = (Widget)innerElementMap.getElement(subId);
                        if (w != null) {
                            final DecoratorAppearance app = w.getAppearance();
                            app.addTween(new ModulationColorTween(c1, c2, app, 0, duration, repeat, TweenFunction.PROGRESSIVE));
                        }
                    }
                }
            }
        }
    }
    
    private static class RemoveListColorTween extends JavaFunctionEx
    {
        RemoveListColorTween(final LuaState luaState) {
            super(luaState);
        }
        
        @Override
        public String getName() {
            return "removeListColorTween";
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getParameterDescriptors() {
            return UIFunctionsLibrary.REMOVE_LIST_COLOR_TWEEN_LUA_SCRIPT_PARAMETER_DESCRIPTORS;
        }
        
        @Override
        public String getDescription() {
            return "Supprime un colorTween des ?l?ments d'une liste";
        }
        
        @Nullable
        @Override
        public final LuaScriptParameterDescriptor[] getResultDescriptors() {
            return null;
        }
        
        @Override
        protected void run(final int paramCount) throws LuaException {
            final String dialogName = this.getParamString(0);
            final ElementMap map = Xulor.getInstance().getEnvironment().getElementMap(dialogName);
            if (map == null) {
                this.writeError(UIFunctionsLibrary.m_logger, "Dialogue inconnu " + dialogName);
                return;
            }
            final String controlId = this.getParamString(1);
            final EventDispatcher ed = map.getElement(controlId);
            if (ed == null) {
                this.writeError(UIFunctionsLibrary.m_logger, "EventDispatcher inconnu " + controlId + " dans le dialog " + dialogName);
                return;
            }
            if (!(ed instanceof List)) {
                this.writeError(UIFunctionsLibrary.m_logger, "l'EventDispatcher n'est pas du type Widget");
                return;
            }
            final String subId = this.getParamString(2);
            final List list = (List)ed;
            final ArrayList<RenderableContainer> renderables = list.getRenderables();
            for (int i = 0, size = renderables.size(); i < size; ++i) {
                final RenderableContainer renderable = renderables.get(i);
                if (renderable != null) {
                    final ElementMap innerElementMap = renderable.getInnerElementMap();
                    if (innerElementMap != null) {
                        final Widget w = (Widget)innerElementMap.getElement(subId);
                        if (w != null) {
                            w.getAppearance().removeTweensOfType(ModulationColorTween.class);
                        }
                    }
                }
            }
        }
    }
    
    private static class AddColorTween extends JavaFunctionEx
    {
        AddColorTween(final LuaState luaState) {
            super(luaState);
        }
        
        @Override
        public String getName() {
            return "addColorTween";
        }
        
        @Override
        public String getDescription() {
            return "Ajoute un tween de changement de couleur de modulation sur le Widget pass? en param?tre.";
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getParameterDescriptors() {
            return UIFunctionsLibrary.ADD_COLOR_TWEEN_LUA_SCRIPT_PARAMETER_DESCRIPTORS;
        }
        
        @Nullable
        @Override
        public final LuaScriptParameterDescriptor[] getResultDescriptors() {
            return null;
        }
        
        @Override
        protected void run(final int paramCount) throws LuaException {
            final String dialogName = this.getParamString(0);
            final ElementMap map = Xulor.getInstance().getEnvironment().getElementMap(dialogName);
            if (map == null) {
                this.writeError(UIFunctionsLibrary.m_logger, "Dialogue inconnu " + dialogName);
                return;
            }
            final String controlId = this.getParamString(1);
            final EventDispatcher ed = map.getElement(controlId);
            if (ed == null) {
                this.writeError(UIFunctionsLibrary.m_logger, "EventDispatcher inconnu " + controlId + " dans le dialog " + dialogName);
                return;
            }
            if (!(ed instanceof Widget)) {
                this.writeError(UIFunctionsLibrary.m_logger, "l'EventDispatcher n'est pas du type Widget");
                return;
            }
            final int duration = this.getParamInt(2);
            final int repeat = this.getParamInt(3);
            final Color c1 = new Color();
            final Color c2 = new Color();
            if (paramCount == 12) {
                c1.setFromFloat((float)this.getParamDouble(4), (float)this.getParamDouble(5), (float)this.getParamDouble(6), (float)this.getParamDouble(7));
                c2.setFromFloat((float)this.getParamDouble(8), (float)this.getParamDouble(9), (float)this.getParamDouble(10), (float)this.getParamDouble(11));
            }
            else {
                c1.set(Color.WHITE.get());
                c2.set(Color.LIGHT_GRAY.get());
            }
            final DecoratorAppearance app = ((Widget)ed).getAppearance();
            app.addTween(new ModulationColorTween(c1, c2, app, 0, duration, repeat, TweenFunction.PROGRESSIVE));
        }
    }
    
    private static class RemoveColorTween extends JavaFunctionEx
    {
        RemoveColorTween(final LuaState luaState) {
            super(luaState);
        }
        
        @Override
        public String getName() {
            return "removeColorTween";
        }
        
        @Override
        public String getDescription() {
            return "Retire les ColorTween sur le widget pass? en param?tre";
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getParameterDescriptors() {
            return UIFunctionsLibrary.REMOVE_COLOR_TWEEN_LUA_SCRIPT_PARAMETER_DESCRIPTORS;
        }
        
        @Nullable
        @Override
        public final LuaScriptParameterDescriptor[] getResultDescriptors() {
            return null;
        }
        
        @Override
        protected void run(final int paramCount) throws LuaException {
            final String dialogName = this.getParamString(0);
            final ElementMap map = Xulor.getInstance().getEnvironment().getElementMap(dialogName);
            if (map == null) {
                this.writeError(UIFunctionsLibrary.m_logger, "Dialogue inconnu " + dialogName);
                return;
            }
            final String controlId = this.getParamString(1);
            final EventDispatcher ed = map.getElement(controlId);
            if (ed == null) {
                this.writeError(UIFunctionsLibrary.m_logger, "EventDispatcher inconnu " + controlId + " dans le dialog " + dialogName);
                return;
            }
            if (!(ed instanceof Widget)) {
                this.writeError(UIFunctionsLibrary.m_logger, "l'EventDispatcher n'est pas du type Widget");
                return;
            }
            ((Widget)ed).getAppearance().removeTweensOfType(ModulationColorTween.class);
        }
    }
    
    private static class AddParticle extends JavaFunctionEx
    {
        AddParticle(final LuaState luaState) {
            super(luaState);
        }
        
        @Override
        public String getName() {
            return "addParticle";
        }
        
        @Override
        public String getDescription() {
            return "Ajoute un syst?me de particule sur un ?l?ment d'interface";
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getParameterDescriptors() {
            return UIFunctionsLibrary.ADD_PARTICLE_LUA_SCRIPT_PARAMETER_DESCRIPTORS;
        }
        
        @Override
        public final LuaScriptParameterDescriptor[] getResultDescriptors() {
            return UIFunctionsLibrary.ADD_PARTICLE_RETURN_LUA_SCRIPT_PARAMETER_DESCRIPTORS;
        }
        
        @Override
        protected void run(final int paramCount) throws LuaException {
            final String dialogName = this.getParamString(0);
            final ElementMap map = Xulor.getInstance().getEnvironment().getElementMap(dialogName);
            if (map == null) {
                this.writeError(UIFunctionsLibrary.m_logger, "Dialogue inconnu " + dialogName);
                this.addReturnNilValue();
                return;
            }
            final String controlId = this.getParamString(1);
            final EventDispatcher ed = map.getElement(controlId);
            if (ed == null) {
                this.writeError(UIFunctionsLibrary.m_logger, "ElementDispatcher inconnu " + controlId + " dans le dialog " + dialogName);
                this.addReturnNilValue();
                return;
            }
            if (!(ed instanceof Widget)) {
                this.writeError(UIFunctionsLibrary.m_logger, "le widget n'est pas du type Widget");
                this.addReturnNilValue();
                return;
            }
            final String particleFile = this.getParamString(2);
            final ParticleDecorator decorator = new ParticleDecorator();
            final int particleId = ++UIFunctionsLibrary.m_particleIdGenerator;
            decorator.onCheckOut();
            decorator.setFile(particleFile);
            decorator.setX(this.getParamInt(3));
            decorator.setY(this.getParamInt(4));
            decorator.setFollowBorders(this.getParamBool(5));
            Alignment9 align = Alignment9.CENTER;
            int level = ParticleSystemFactory.SYSTEM_WITHOUT_LEVEL;
            if (paramCount > 6) {
                if (this.getParam(8).isString()) {
                    align = Alignment9.valueOf(this.getParamString(6));
                }
                else if (this.getParam(8).isNumber()) {
                    level = this.getParamInt(6);
                }
            }
            if (paramCount > 7 && this.getParam(9).isNumber()) {
                level = this.getParamInt(7);
            }
            if (level != ParticleSystemFactory.SYSTEM_WITHOUT_LEVEL) {
                decorator.setLevel(level);
            }
            decorator.setAlignment(align);
            ((Widget)ed).getAppearance().add(decorator);
            UIFunctionsLibrary.getInstance().m_particles.put(particleId, decorator);
            this.addReturnValue(particleId);
        }
    }
    
    private static class RemoveParticle extends JavaFunctionEx
    {
        RemoveParticle(final LuaState luaState) {
            super(luaState);
        }
        
        @Override
        public String getName() {
            return "removeParticle";
        }
        
        @Override
        public String getDescription() {
            return "Enl?ve un syst?me de particule sur un ?l?ment d'interface";
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getParameterDescriptors() {
            return UIFunctionsLibrary.REMOVE_PARTICLE_LUA_SCRIPT_PARAMETER_DESCRIPTORS;
        }
        
        @Nullable
        @Override
        public final LuaScriptParameterDescriptor[] getResultDescriptors() {
            return null;
        }
        
        @Override
        protected void run(final int paramCount) throws LuaException {
            final int id = this.getParamInt(0);
            final ParticleDecorator particleDecorator = UIFunctionsLibrary.getInstance().m_particles.remove(id);
            if (particleDecorator != null) {
                try {
                    particleDecorator.setTimeToLive((paramCount == 2) ? this.getParamInt(1) : 100);
                }
                catch (Exception e) {
                    UIFunctionsLibrary.m_logger.error((Object)("Exception lev?e dans le removeParticle(" + id + ")"), (Throwable)e);
                }
            }
            else {
                UIFunctionsLibrary.m_logger.warn((Object)("Impossible de trouver la particle " + id + " pour la supprimer"));
            }
        }
    }
    
    private static class ClearParticles extends JavaFunctionEx
    {
        ClearParticles(final LuaState luaState) {
            super(luaState);
        }
        
        @Override
        public String getName() {
            return "clearParticles";
        }
        
        @Override
        public String getDescription() {
            return "Enl?ve tous les syst?mes de particule sur un ?l?ment d'interface";
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getParameterDescriptors() {
            return UIFunctionsLibrary.CLEAR_PARTICLES_LUA_SCRIPT_PARAMETER_DESCRIPTORS;
        }
        
        @Nullable
        @Override
        public final LuaScriptParameterDescriptor[] getResultDescriptors() {
            return null;
        }
        
        @Override
        protected void run(final int paramCount) throws LuaException {
            final String dialogId = this.getParamString(0);
            final String widgetId = this.getParamString(1);
            final ElementMap elementMap = Xulor.getInstance().getEnvironment().getElementMap(dialogId);
            if (elementMap == null) {
                UIFunctionsLibrary.m_logger.error((Object)("[ClearParticles] Impossible de retrouver l'interface d'id=" + dialogId));
                return;
            }
            final Widget element = (Widget)elementMap.getElement(widgetId);
            if (element == null) {
                UIFunctionsLibrary.m_logger.error((Object)("[ClearParticles] Impossible de retrouver le widget d'id=" + widgetId + " dans l'interface d'id=" + dialogId));
                return;
            }
            final ArrayList<ParticleDecorator> particleDecorators = new ArrayList<ParticleDecorator>();
            for (final EventDispatcher eventDispatcher : element.getAppearance().getChildren()) {
                if (eventDispatcher instanceof ParticleDecorator) {
                    particleDecorators.add((ParticleDecorator)eventDispatcher);
                }
            }
            for (final ParticleDecorator ed : particleDecorators) {
                ed.setTimeToLive(100);
            }
        }
    }
    
    private static class ChangeCursor extends JavaFunctionEx
    {
        ChangeCursor(final LuaState luaState) {
            super(luaState);
        }
        
        @Override
        public String getName() {
            return "changeCursor";
        }
        
        @Override
        public String getDescription() {
            return "Change le curseur de la souris.";
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getParameterDescriptors() {
            return UIFunctionsLibrary.CHANGE_CURSOR_LUA_SCRIPT_PARAMETER_DESCRIPTORS;
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getResultDescriptors() {
            return UIFunctionsLibrary.CHANGE_CURSOR_RETURN_LUA_SCRIPT_PARAMETER_DESCRIPTORS;
        }
        
        @Override
        protected void run(final int paramCount) throws LuaException {
            final String state = this.getParamString(0);
            CursorFactory.CursorType cursorType = CursorFactory.CursorType.valueOf(state);
            if (cursorType == null) {
                if ("SPELL".equals(state)) {
                    cursorType = CursorFactory.CursorType.CUSTOM1;
                }
                else {
                    this.writeError(UIFunctionsLibrary.m_logger, "Type de curseur inconnu " + state);
                }
            }
            CursorFactory.getInstance().unlock();
            CursorFactory.getInstance().show(cursorType, true);
            final String lastState = "DEFAULT";
            this.addReturnValue("DEFAULT");
        }
    }
    
    private static class DisplayWideScreenBand extends JavaFunctionEx
    {
        DisplayWideScreenBand(final LuaState luaState) {
            super(luaState);
        }
        
        @Override
        public String getName() {
            return "displayWideScreenBand";
        }
        
        @Override
        public String getDescription() {
            return "Affiche/Masque les bandes noires pour faire un aspect cin?ma";
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getParameterDescriptors() {
            return UIFunctionsLibrary.DISPLAY_WIDE_SCREEN_BAND_LUA_SCRIPT_PARAMETER_DESCRIPTORS;
        }
        
        @Nullable
        @Override
        public LuaScriptParameterDescriptor[] getResultDescriptors() {
            return null;
        }
        
        @Override
        protected void run(final int paramCount) throws LuaException {
            final WakfuWorldScene scene = (WakfuWorldScene)WakfuClientInstance.getInstance().getWorldScene();
            scene.setMaskShow(this.getParamBool(0));
            if (paramCount > 1) {
                scene.setMaskMaxHeight(this.getParamInt(1));
                if (paramCount > 2) {
                    scene.setMaskSpeed(1000.0f / this.getParamInt(2));
                }
            }
        }
    }
    
    private static class AddEquipmentShortcut extends JavaFunctionEx
    {
        AddEquipmentShortcut(final LuaState luaState) {
            super(luaState);
        }
        
        @Override
        public String getName() {
            return "addEquipmentShortcut";
        }
        
        @Override
        public String getDescription() {
            return "Ajoute un lien vers un objet ?quipe dans la barre de raccourcis active.";
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getParameterDescriptors() {
            return UIFunctionsLibrary.ADD_EQUIPMENT_SHORTCUT_LUA_SCRIPT_PARAMETER_DESCRIPTORS;
        }
        
        @Nullable
        @Override
        public final LuaScriptParameterDescriptor[] getResultDescriptors() {
            return null;
        }
        
        @Override
        protected void run(final int paramCount) throws LuaException {
            final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
            final String referenceType = this.getParamString(0);
            final long itemRef = this.getParamLong(1);
            short position = -1;
            boolean bPositionFixed = paramCount > 2;
            final ShortcutBar currentShortcutBar = localPlayer.getShortcutBarManager().getSelectedItemsShortcutBar();
            if (bPositionFixed) {
                position = (short)this.getParamInt(2);
                if (!currentShortcutBar.isPositionFree(position)) {
                    bPositionFixed = false;
                }
            }
            if (!bPositionFixed) {
                position = currentShortcutBar.getFirstFreeIndex();
            }
            if (position == -1) {
                return;
            }
            Item item;
            if ("pos".equalsIgnoreCase(referenceType)) {
                item = ((ArrayInventoryWithoutCheck<Item, R>)localPlayer.getEquipmentInventory()).getFromPosition((short)itemRef);
                if (item == null) {
                    return;
                }
            }
            else if ("uid".equalsIgnoreCase(referenceType)) {
                item = ((ArrayInventoryWithoutCheck<Item, R>)localPlayer.getEquipmentInventory()).getWithUniqueId(itemRef);
                if (item == null) {
                    return;
                }
            }
            else {
                if (!"refId".equalsIgnoreCase(referenceType)) {
                    UIFunctionsLibrary.m_logger.warn((Object)("Type de r?f?rence (uid, ref id, position, ...) inconnu pour ajouter un equipment : " + referenceType));
                    return;
                }
                item = ((ArrayInventoryWithoutCheck<Item, R>)localPlayer.getEquipmentInventory()).getFirstWithReferenceId((int)itemRef);
                if (item == null) {
                    return;
                }
            }
            final short pos = localPlayer.getEquipmentInventory().getPosition(item.getUniqueId());
            if (currentShortcutBar.getFirstWithReferenceId(item.getReferenceId()) == null) {
                final ShortCutItem shortcut = ShortCutItem.checkOut(ShortCutType.EQUIPMENT_SLOT, -pos - 1, item.getReferenceId(), item.getGfxId());
                final UIShortcutMessage message = new UIShortcutMessage();
                message.setItem(shortcut);
                message.setForce(true);
                message.setShorcutBarNumber(localPlayer.getShortcutBarManager().getSelectedItemsShortcutBarNumber());
                message.setPosition(position);
                message.setBooleanValue(false);
                message.setId(16700);
                Worker.getInstance().pushMessage(message);
            }
        }
    }
    
    private static class AddEmoteShortcut extends JavaFunctionEx
    {
        AddEmoteShortcut(final LuaState luaState) {
            super(luaState);
        }
        
        @Override
        public String getName() {
            return "addEmoteShortcut";
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getParameterDescriptors() {
            return UIFunctionsLibrary.ADD_EMOTE_SHORTCUT_LUA_SCRIPT_PARAMETER_DESCRIPTORS;
        }
        
        @Override
        public String getDescription() {
            return "Ajoute l'emote sp?cifi?e dans la barre de raccourci d'item courante";
        }
        
        @Nullable
        @Override
        public final LuaScriptParameterDescriptor[] getResultDescriptors() {
            return null;
        }
        
        @Override
        protected void run(final int paramCount) throws LuaException {
            final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
            final int emoteId = this.getParamInt(0);
            short position = -1;
            boolean bPositionFixed = paramCount > 2;
            final ShortcutBar currentShortcutBar = localPlayer.getShortcutBarManager().getSelectedItemsShortcutBar();
            if (bPositionFixed) {
                position = (short)this.getParamInt(1);
                if (!currentShortcutBar.isPositionFree(position)) {
                    bPositionFixed = false;
                }
            }
            if (!bPositionFixed) {
                position = currentShortcutBar.getFirstFreeIndex();
            }
            if (position == -1) {
                return;
            }
            final short pos = localPlayer.getEquipmentInventory().getPosition(emoteId);
            if (currentShortcutBar.getFirstWithReferenceId(emoteId) == null) {
                final ShortCutItem shortcut = ShortCutItem.checkOut(ShortCutType.EMOTE, -pos - 1, emoteId, emoteId);
                final UIShortcutMessage message = new UIShortcutMessage();
                message.setItem(shortcut);
                message.setForce(true);
                message.setShorcutBarNumber(localPlayer.getShortcutBarManager().getSelectedItemsShortcutBarNumber());
                message.setPosition(position);
                message.setBooleanValue(false);
                message.setId(16700);
                Worker.getInstance().pushMessage(message);
            }
        }
    }
    
    private static class RemoveShortCutWithId extends JavaFunctionEx
    {
        @Override
        public String getName() {
            return "removeShortcut";
        }
        
        RemoveShortCutWithId(final LuaState state) {
            super(state);
        }
        
        @Override
        public String getDescription() {
            return "Enl?ve le raccourci sp?cifi? des barres de raccourcis";
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getParameterDescriptors() {
            return UIFunctionsLibrary.REMOVE_SHORTCUT_WITH_ID_LUA_SCRIPT_PARAMETER_DESCRIPTORS;
        }
        
        @Nullable
        @Override
        public LuaScriptParameterDescriptor[] getResultDescriptors() {
            return null;
        }
        
        @Override
        protected void run(final int paramCount) throws LuaException {
            final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
            final int shortCutRefId = this.getParamInt(0);
            final ShortCutType type = ShortCutType.getFromId((byte)this.getParamInt(1));
            localPlayer.getShortcutBarManager().removeShortcutItemWithId(shortCutRefId, type, true);
        }
    }
    
    private static class AddSpellShortcut extends JavaFunctionEx
    {
        AddSpellShortcut(final LuaState luaState) {
            super(luaState);
        }
        
        @Override
        public String getName() {
            return "addSpellShortcut";
        }
        
        @Override
        public String getDescription() {
            return "Ajoute un lien vers un sort dans la barre de raccourcis active.";
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getParameterDescriptors() {
            return UIFunctionsLibrary.ADD_SPELL_SHORTCUT_LUA_SCRIPT_PARAMETER_DESCRIPTORS;
        }
        
        @Nullable
        @Override
        public final LuaScriptParameterDescriptor[] getResultDescriptors() {
            return null;
        }
        
        @Override
        protected void run(final int paramCount) throws LuaException {
            final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
            final String referenceType = this.getParamString(0);
            final long itemRef = this.getParamLong(1);
            short position = -1;
            boolean bPositionFixed = paramCount > 2;
            final ShortcutBar bar = localPlayer.getShortcutBarManager().getSpellsBar((byte)0);
            if (bPositionFixed) {
                position = (short)this.getParamInt(2);
                if (!bar.isPositionFree(position)) {
                    bPositionFixed = false;
                }
            }
            if (!bPositionFixed) {
                position = bar.getFirstFreeIndex();
            }
            if (position == -1) {
                return;
            }
            Object item;
            if ("uid".equalsIgnoreCase(referenceType)) {
                item = localPlayer.getSpellInventory().getWithUniqueId(itemRef);
                if (item == null) {
                    return;
                }
            }
            else {
                if (!"refId".equalsIgnoreCase(referenceType)) {
                    UIFunctionsLibrary.m_logger.warn((Object)("Type de r?f?rence (uid, ref id) inconnu pour ajouter un sort : " + referenceType));
                    return;
                }
                item = localPlayer.getSpellInventory().getFirstWithReferenceId((int)itemRef);
                if (item == null) {
                    return;
                }
            }
            final UIShortcutMessage message = new UIShortcutMessage();
            message.setItem(item);
            message.setForce(true);
            message.setShorcutBarNumber(localPlayer.getShortcutBarManager().getShortcutBarNumber(bar));
            message.setPosition(position);
            message.setBooleanValue(false);
            message.setId(16700);
            Worker.getInstance().pushMessage(message);
        }
    }
    
    private static class AddItemShortcut extends JavaFunctionEx
    {
        AddItemShortcut(final LuaState luaState) {
            super(luaState);
        }
        
        @Override
        public String getName() {
            return "AddItemShortcut";
        }
        
        @Override
        public String getDescription() {
            return "Ajoute un raccourci d'item donn? aux barres de raccourci";
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getParameterDescriptors() {
            return UIFunctionsLibrary.ADD_ITEM_SHORTCUT_LUA_SCRIPT_PARAMETER_DESCRIPTORS;
        }
        
        @Nullable
        @Override
        public final LuaScriptParameterDescriptor[] getResultDescriptors() {
            return null;
        }
        
        @Override
        protected void run(final int paramCount) throws LuaException {
            final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
            final String referenceType = this.getParamString(0);
            final long itemRef = this.getParamLong(1);
            boolean bPositionFixed = paramCount > 2;
            Item item;
            if ("uid".equalsIgnoreCase(referenceType)) {
                item = localPlayer.getBags().getItemFromInventories(itemRef);
            }
            else {
                if (!"refId".equalsIgnoreCase(referenceType)) {
                    UIFunctionsLibrary.m_logger.warn((Object)("Type de r?f?rence (uid, ref id) inconnu pour ajouter un item : " + referenceType));
                    return;
                }
                final Collection<Item> items = localPlayer.getBags().getAllWithReferenceId((int)itemRef);
                item = items.toArray(new Item[items.size()])[0];
            }
            if (item == null) {
                return;
            }
            final ShortcutBarManager shortcutBarManager = localPlayer.getShortcutBarManager();
            final ShortCutBarType shortCutBarType = shortcutBarManager.getCurrentBarType();
            final ReferenceItem referenceItem = (ReferenceItem)item.getReferenceItem();
            if ((item.isUsableInWorld() && shortCutBarType == ShortCutBarType.WORLD) || (shortCutBarType == ShortCutBarType.FIGHT && (referenceItem.getReferenceItemDisplayer().isEquipable() || item.isUsableInFight()))) {
                final ShortcutBar bar = (shortCutBarType == ShortCutBarType.FIGHT) ? shortcutBarManager.getSelectedSpellsShortcutBar() : ((shortCutBarType == ShortCutBarType.WORLD) ? shortcutBarManager.getSelectedItemsShortcutBar() : null);
                short position = -1;
                if (bPositionFixed) {
                    position = (short)this.getParamInt(2);
                    if (!bar.isPositionFree(position)) {
                        bPositionFixed = false;
                    }
                }
                final UIShortcutMessage message = new UIShortcutMessage();
                message.setItem(item);
                message.setForce(true);
                message.setShorcutBarNumber(shortcutBarManager.getShortcutBarNumber(bar));
                message.setPosition(bPositionFixed ? position : -1);
                message.setBooleanValue(false);
                message.setId(16700);
                Worker.getInstance().pushMessage(message);
                return;
            }
            if (!item.isUsableInFight() && !item.isUsableInWorld() && !referenceItem.getReferenceItemDisplayer().isEquipable()) {
                return;
            }
            final UIShortcutMessage message2 = new UIShortcutMessage();
            message2.setItem(item);
            message2.setForce(true);
            message2.setShorcutBarNumber(-1);
            message2.setPosition(-1);
            message2.setBooleanValue(false);
            message2.setId(16700);
            Worker.getInstance().pushMessage(message2);
        }
    }
    
    private static class HasFightShortcut extends JavaFunctionEx
    {
        HasFightShortcut(final LuaState luaState) {
            super(luaState);
        }
        
        @Override
        public String getName() {
            return "hasFightShortcut";
        }
        
        @Override
        public String getDescription() {
            return "Indique si le joueur a au moins un raccourci de combat";
        }
        
        @Nullable
        @Override
        public LuaScriptParameterDescriptor[] getParameterDescriptors() {
            return UIFunctionsLibrary.HAS_FIGHT_SHORTCUT_LUA_SCRIPT_PARAMETER_DESCRIPTORS;
        }
        
        @Override
        public final LuaScriptParameterDescriptor[] getResultDescriptors() {
            return UIFunctionsLibrary.HAS_FIGHT_SHORTCUT_LUA_SCRIPT_RESULT_DESCRIPTORS;
        }
        
        @Override
        protected void run(final int paramCount) throws LuaException {
            int refId = -1;
            ShortCutType type = null;
            if (paramCount == 2) {
                refId = this.getParamInt(0);
                type = ShortCutType.getFromId((byte)this.getParamInt(1));
            }
            final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
            final ShortcutBarManager shortcutBarManager = localPlayer.getShortcutBarManager();
            final ArrayList<ShortcutBar> spellsBars = shortcutBarManager.getSpellsBars(false);
            for (int i = spellsBars.size() - 1; i >= 0; --i) {
                final ShortcutBar bar = spellsBars.get(i);
                if (refId != -1) {
                    for (final ShortCutItem sci : bar.getAllWithReferenceId(refId)) {
                        if (sci.getType() == type) {
                            this.addReturnValue(true);
                            return;
                        }
                    }
                }
                else if (!bar.isEmpty()) {
                    this.addReturnValue(true);
                    return;
                }
            }
            this.addReturnValue(false);
        }
    }
    
    private static class HasWorldShortcut extends JavaFunctionEx
    {
        HasWorldShortcut(final LuaState luaState) {
            super(luaState);
        }
        
        @Override
        public String getName() {
            return "hasWorldShortcut";
        }
        
        @Override
        public String getDescription() {
            return "Indique si le joueur a au moins un raccourci de monde";
        }
        
        @Nullable
        @Override
        public LuaScriptParameterDescriptor[] getParameterDescriptors() {
            return UIFunctionsLibrary.HAS_WORLD_SHORTCUT_LUA_SCRIPT_PARAMETER_DESCRIPTORS;
        }
        
        @Override
        public final LuaScriptParameterDescriptor[] getResultDescriptors() {
            return UIFunctionsLibrary.HAS_WORLD_SHORTCUT_LUA_SCRIPT_RESULT_DESCRIPTORS;
        }
        
        @Override
        protected void run(final int paramCount) throws LuaException {
            int refId = -1;
            ShortCutType type = null;
            if (paramCount == 2) {
                refId = this.getParamInt(0);
                type = ShortCutType.getFromId((byte)this.getParamInt(1));
            }
            final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
            final ShortcutBarManager shortcutBarManager = localPlayer.getShortcutBarManager();
            final ArrayList<ShortcutBar> worldBars = shortcutBarManager.getItemsBars(false);
            for (int i = worldBars.size() - 1; i >= 0; --i) {
                final ShortcutBar bar = worldBars.get(i);
                if (refId != -1) {
                    for (final ShortCutItem sci : bar.getAllWithReferenceId(refId)) {
                        if (sci.getType() == type) {
                            this.addReturnValue(true);
                            return;
                        }
                    }
                }
                else if (!bar.isEmpty()) {
                    this.addReturnValue(true);
                    return;
                }
            }
            this.addReturnValue(false);
        }
    }
    
    private static class HasShortcut extends JavaFunctionEx
    {
        HasShortcut(final LuaState luaState) {
            super(luaState);
        }
        
        @Override
        public String getName() {
            return "hasShortcut";
        }
        
        @Nullable
        @Override
        public LuaScriptParameterDescriptor[] getParameterDescriptors() {
            return UIFunctionsLibrary.HAS_SHORTCUT_LUA_SCRIPT_PARAMETER_DESCRIPTORS;
        }
        
        @Override
        public String getDescription() {
            return "Indique si le joueur a au moins un raccourci";
        }
        
        @Override
        public final LuaScriptParameterDescriptor[] getResultDescriptors() {
            return UIFunctionsLibrary.HAS_SHORTCUT_LUA_SCRIPT_RESULT_DESCRIPTORS;
        }
        
        @Override
        protected void run(final int paramCount) throws LuaException {
            int refId = -1;
            ShortCutType type = null;
            if (paramCount == 2) {
                refId = this.getParamInt(0);
                type = ShortCutType.getFromId((byte)this.getParamInt(1));
            }
            final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
            final ShortcutBarManager shortcutBarManager = localPlayer.getShortcutBarManager();
            final ArrayList<ShortcutBar> worldBars = shortcutBarManager.getItemsBars(false);
            if (this.checkBars(refId, type, worldBars)) {
                return;
            }
            final ArrayList<ShortcutBar> spellsBars = shortcutBarManager.getSpellsBars(false);
            if (this.checkBars(refId, type, spellsBars)) {
                return;
            }
            this.addReturnValue(false);
        }
        
        private boolean checkBars(final int refId, final ShortCutType type, final ArrayList<ShortcutBar> worldBars) throws LuaException {
            for (int i = worldBars.size() - 1; i >= 0; --i) {
                final ShortcutBar bar = worldBars.get(i);
                if (refId != -1) {
                    for (final ShortCutItem sci : bar.getAllWithReferenceId(refId)) {
                        if (sci.getType() == type) {
                            this.addReturnValue(true);
                            return true;
                        }
                    }
                }
                else if (!bar.isEmpty()) {
                    this.addReturnValue(true);
                    return true;
                }
            }
            return false;
        }
    }
    
    private static class SetShortcutUsable extends JavaFunctionEx
    {
        SetShortcutUsable(final LuaState luaState) {
            super(luaState);
        }
        
        @Override
        public String getName() {
            return "setShortcutUsable";
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getParameterDescriptors() {
            return UIFunctionsLibrary.SET_SHORTCUT_USABLE_LUA_SCRIPT_PARAMETER_DESCRIPTORS;
        }
        
        @Override
        public String getDescription() {
            return "Active/d?sactive le ou les raccourcis de la ou des barres de raccourcis sp?cifi?es";
        }
        
        @Nullable
        @Override
        public final LuaScriptParameterDescriptor[] getResultDescriptors() {
            return null;
        }
        
        @Override
        protected void run(final int paramCount) throws LuaException {
            if (paramCount < 1) {
                return;
            }
            final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
            final ShortcutBarManager shortcutBarManager = localPlayer.getShortcutBarManager();
            final boolean usable = this.getParamBool(0);
            if (paramCount == 1) {
                for (final ShortcutBar shortcutBar : shortcutBarManager.getSpellsBars(false)) {
                    shortcutBar.setShortcutsUsable(usable);
                }
                for (final ShortcutBar shortcutBar : shortcutBarManager.getItemsBars(false)) {
                    shortcutBar.setShortcutsUsable(usable);
                }
            }
            else {
                final String type = this.getParamString(1);
                final ShortcutType shortcutBarType = ShortcutType.valueOf(type);
                if (shortcutBarType == null) {
                    UIFunctionsLibrary.m_logger.error((Object)("Impossible de retrouver le type de barre de raccourci suivant : " + type));
                    return;
                }
                if (paramCount == 2) {
                    if (shortcutBarType.isFight()) {
                        for (final ShortcutBar shortcutBar2 : shortcutBarManager.getSpellsBars(false)) {
                            shortcutBar2.setShortcutsUsable(usable);
                        }
                    }
                    if (shortcutBarType.isWorld()) {
                        for (final ShortcutBar shortcutBar2 : shortcutBarManager.getItemsBars(false)) {
                            shortcutBar2.setShortcutsUsable(usable);
                        }
                    }
                    if (shortcutBarType.isHands()) {
                        shortcutBarManager.getLeftHandWeaponShortcut().setUsable(usable);
                        shortcutBarManager.getRightHandWeaponShortcut().setUsable(usable);
                    }
                }
                else {
                    final byte barIndex = (byte)this.getParamInt(2);
                    if (paramCount == 3) {
                        if (shortcutBarType.isFight()) {
                            shortcutBarManager.getSpellsBar(barIndex).setShortcutsUsable(usable);
                        }
                        if (shortcutBarType.isWorld()) {
                            shortcutBarManager.getOutFightBar(barIndex).setShortcutsUsable(usable);
                        }
                    }
                    else if (paramCount == 4) {
                        final short position = (short)this.getParamInt(3);
                        if (shortcutBarType.isFight()) {
                            shortcutBarManager.getSpellsBar(barIndex).getFromPosition(position).setUsable(usable);
                        }
                        if (shortcutBarType.isWorld()) {
                            shortcutBarManager.getOutFightBar(barIndex).getFromPosition(position).setUsable(usable);
                        }
                    }
                }
            }
        }
        
        private enum ShortcutType
        {
            HANDS(true, false, false), 
            HANDS_AND_FIGHT(true, true, false), 
            FIGHT(false, true, false), 
            WORLD(false, false, true);
            
            private final boolean m_hands;
            private final boolean m_fight;
            private final boolean m_world;
            
            private ShortcutType(final boolean hands, final boolean fight, final boolean world) {
                this.m_hands = hands;
                this.m_fight = fight;
                this.m_world = world;
            }
            
            public boolean isHands() {
                return this.m_hands;
            }
            
            public boolean isFight() {
                return this.m_fight;
            }
            
            public boolean isWorld() {
                return this.m_world;
            }
        }
    }
    
    private static class AddDialogUnloadedListener extends JavaFunctionEx
    {
        AddDialogUnloadedListener(final LuaState luaState) {
            super(luaState);
        }
        
        @Override
        public String getName() {
            return "addDialogUnloadedListener";
        }
        
        @Override
        public String getDescription() {
            return "Permet de demander l'?x?cution d'un callback LUA lors de la fermeture d'une interface Xulor. Une fois ce callback appel?, il sera automatiquement d?senregistr?.";
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getParameterDescriptors() {
            return UIFunctionsLibrary.ADD_DIALOG_UNLOADED_LISTENER_LUA_SCRIPT_PARAMETER_DESCRIPTORS;
        }
        
        @Nullable
        @Override
        public final LuaScriptParameterDescriptor[] getResultDescriptors() {
            return null;
        }
        
        @Override
        protected void run(final int paramCount) throws LuaException {
            final String dialog = this.getParamString(0);
            final String funcName = this.getParamString(1);
            final LuaValue[] params = this.getParams(2, paramCount);
            final LuaScript script = this.getScriptObject();
            final DialogUnloadListener listener = new DialogUnloadListener() {
                private final LuaCallback m_callback = new LuaCallback(script, funcName, params);
                
                @Override
                public void dialogUnloaded(final String id) {
                    if (id.equals(dialog)) {
                        this.m_callback.run();
                        ScriptUIEventManager.getInstance().removeDialogUnloadListener(dialog);
                    }
                }
            };
            ScriptUIEventManager.getInstance().addDialogUnloadListener(dialog, listener);
        }
    }
    
    private static class RemoveDialogUnloadedListener extends JavaFunctionEx
    {
        RemoveDialogUnloadedListener(final LuaState luaState) {
            super(luaState);
        }
        
        @Override
        public String getName() {
            return "removeDialogUnloadedListener";
        }
        
        @Override
        public String getDescription() {
            return "Annule l'effet d'un UI.addDialogUnloadedListener";
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getParameterDescriptors() {
            return UIFunctionsLibrary.REMOVE_DIALOG_UNLOADED_LISTENER_LUA_SCRIPT_PARAMETER_DESCRIPTORS;
        }
        
        @Nullable
        @Override
        public final LuaScriptParameterDescriptor[] getResultDescriptors() {
            return null;
        }
        
        @Override
        protected void run(final int paramCount) throws LuaException {
            final String dialog = this.getParamString(0);
            ScriptUIEventManager.getInstance().removeDialogUnloadListener(dialog);
        }
    }
    
    private static class AddDialogLoadedListener extends JavaFunctionEx
    {
        AddDialogLoadedListener(final LuaState luaState) {
            super(luaState);
        }
        
        @Override
        public String getName() {
            return "addDialogLoadedListener";
        }
        
        @Override
        public String getDescription() {
            return "Permet de demander l'?x?cution d'un callback LUA lors de l'ouverture d'une interface Xulor.Une fois ce callback appel?, il sera automatiquement d?senregistr?.";
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getParameterDescriptors() {
            return UIFunctionsLibrary.ADD_DIALOG_LOADED_LISTENER_LUA_SCRIPT_PARAMETER_DESCRIPTORS;
        }
        
        @Nullable
        @Override
        public final LuaScriptParameterDescriptor[] getResultDescriptors() {
            return null;
        }
        
        @Override
        protected void run(final int paramCount) throws LuaException {
            final String dialog = this.getParamString(0);
            final String funcName = this.getParamString(1);
            final LuaValue[] params = this.getParams(2, paramCount);
            final LuaScript script = this.getScriptObject();
            final DialogLoadListener listener = new DialogLoadListener() {
                private final LuaCallback m_callback = new LuaCallback(script, funcName, params);
                
                @Override
                public void dialogLoaded(final String id) {
                    if (id.equals(dialog)) {
                        this.m_callback.run();
                        ScriptUIEventManager.getInstance().removeDialogLoadListener(dialog);
                    }
                }
            };
            ScriptUIEventManager.getInstance().addDialogLoadListener(dialog, listener);
        }
    }
    
    private static class RemoveDialogLoadedListener extends JavaFunctionEx
    {
        RemoveDialogLoadedListener(final LuaState luaState) {
            super(luaState);
        }
        
        @Override
        public String getName() {
            return "removeDialogLoadedListener";
        }
        
        @Override
        public String getDescription() {
            return "Annule l'effet d'un UI.addDialogLoadedListener";
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getParameterDescriptors() {
            return UIFunctionsLibrary.REMOVE_DIALOG_LOADED_LISTENER_LUA_SCRIPT_PARAMETER_DESCRIPTORS;
        }
        
        @Nullable
        @Override
        public final LuaScriptParameterDescriptor[] getResultDescriptors() {
            return null;
        }
        
        @Override
        protected void run(final int paramCount) throws LuaException {
            final String dialog = this.getParamString(0);
            ScriptUIEventManager.getInstance().removeDialogLoadListener(dialog);
        }
    }
    
    private static class GetWindowSize extends JavaFunctionEx
    {
        GetWindowSize(final LuaState luaState) {
            super(luaState);
        }
        
        @Override
        public String getName() {
            return "getWindowSize";
        }
        
        @Override
        public String getDescription() {
            return "Fourni la taille d'une fen?tre sp?cifi?e";
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getParameterDescriptors() {
            return UIFunctionsLibrary.GET_WINDOW_SIZE_LUA_SCRIPT_PARAMETER_DESCRIPTORS;
        }
        
        @Override
        public final LuaScriptParameterDescriptor[] getResultDescriptors() {
            return new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("x", null, LuaScriptParameterType.NUMBER, false), new LuaScriptParameterDescriptor("y", null, LuaScriptParameterType.NUMBER, false) };
        }
        
        @Override
        protected void run(final int paramCount) throws LuaException {
            final String dialog = this.getParamString(0);
            final EventDispatcher eventDispatcher = Xulor.getInstance().getLoadedElement(dialog);
            if (eventDispatcher instanceof Window) {
                final Window window = (Window)eventDispatcher;
                final Dimension dimension = window.getPrefSize();
                this.addReturnValue(dimension.width);
                this.addReturnValue(dimension.height);
            }
            else {
                this.addReturnValue(0);
                this.addReturnValue(0);
                UIFunctionsLibrary.m_logger.error((Object)("Impossible de r?cup?rer la taille de la fen?tre '" + dialog + "' qui n'existe pas"));
            }
        }
    }
    
    private static class SetShortcutEnabled extends JavaFunctionEx
    {
        SetShortcutEnabled(final LuaState luaState) {
            super(luaState);
        }
        
        @Override
        public String getName() {
            return "setShortcutEnabled";
        }
        
        @Override
        public String getDescription() {
            return "Permet d'activer/d?sactiver l'ensemble, un groupe ou un unique raccourci (voir le fichier config/client/content/data/shortcuts.xml). Si aucune id n'est pr?cis?e dans le raccourci, vous pouvez en rajouter une.";
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getParameterDescriptors() {
            return UIFunctionsLibrary.SET_SHORTCUT_ENABLED_LUA_SCRIPT_PARAMETER_DESCRIPTORS;
        }
        
        @Nullable
        @Override
        public final LuaScriptParameterDescriptor[] getResultDescriptors() {
            return null;
        }
        
        @Override
        protected void run(final int paramCount) throws LuaException {
            final boolean enabled = this.getParamBool(0);
            final String groupName = (paramCount >= 2) ? this.getParamString(1) : null;
            final String name = (paramCount >= 3) ? this.getParamString(2) : null;
            if (paramCount == 1) {
                ShortcutManager.getInstance().setAllShortcutsEnabled(enabled);
            }
            else if (paramCount == 2) {
                ShortcutManager.getInstance().enableGroup(groupName, enabled);
            }
            else if (paramCount == 3) {
                ShortcutManager.getInstance().enableShortcut(name, enabled);
            }
        }
    }
    
    private static class SetUIVisible extends JavaFunctionEx
    {
        SetUIVisible(final LuaState luaState) {
            super(luaState);
        }
        
        @Override
        public String getName() {
            return "setUIVisible";
        }
        
        @Override
        public String getDescription() {
            return "Rend visible/invisible les ?l?ments d'interface qui ne font pas partis des layers permanents (Upper, Bulles de dialogue, Monde(overheads),etc...)";
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getParameterDescriptors() {
            return UIFunctionsLibrary.SET_UI_VISIBLE_LUA_SCRIPT_PARAMETER_DESCRIPTORS;
        }
        
        @Nullable
        @Override
        public final LuaScriptParameterDescriptor[] getResultDescriptors() {
            return null;
        }
        
        @Override
        protected void run(final int paramCount) throws LuaException {
            final boolean visible = this.getParamBool(0);
            final LayeredContainer layeredContainer = MasterRootContainer.getInstance().getLayeredContainer();
            final TObjectProcedure<Widget> procedure = new TObjectProcedure<Widget>() {
                @Override
                public boolean execute(final Widget w) {
                    w.setVisible(visible);
                    return true;
                }
            };
            layeredContainer.forEachWidgetNotInLayers(procedure, 26000, 25000, 25999, -40000);
        }
    }
    
    private static class WriteInChat extends JavaFunctionEx
    {
        WriteInChat(final LuaState luaState) {
            super(luaState);
        }
        
        @Override
        public String getName() {
            return "writeLocalisedInChat";
        }
        
        @Override
        public String getDescription() {
            return "?crit dans le chat d'information de jeu le message li? ? la clef de traduction sp?cifi?e";
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getParameterDescriptors() {
            return UIFunctionsLibrary.WRITE_IN_CHAT_LUA_SCRIPT_PARAMETER_DESCRIPTORS;
        }
        
        @Nullable
        @Override
        public LuaScriptParameterDescriptor[] getResultDescriptors() {
            return null;
        }
        
        @Override
        protected void run(final int paramCount) throws LuaException {
            if (paramCount < 1) {
                UIFunctionsLibrary.m_logger.warn((Object)"On utilise writeLocalisedInChat sans argument !");
                return;
            }
            String message;
            if (paramCount == 1) {
                message = WakfuTranslator.getInstance().getString(this.getParamString(0));
            }
            else {
                final String[] params = new String[paramCount - 1];
                for (int i = 1; i < paramCount; ++i) {
                    final String param = this.getParamForcedAsString(i);
                    params[i - 1] = param;
                }
                message = WakfuTranslator.getInstance().getString(this.getParamString(0), (Object[])params);
            }
            final int chatPipe = 4;
            ChatManager.getInstance().pushMessage(message, 4);
        }
    }
    
    private static class WriteInEmoteChat extends JavaFunctionEx
    {
        WriteInEmoteChat(final LuaState luaState) {
            super(luaState);
        }
        
        @Override
        public String getName() {
            return "writeEmoteChat";
        }
        
        @Override
        public String getDescription() {
            return "[DEPRECATED] Affiche un message chat localis? dans le chat d'emote";
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getParameterDescriptors() {
            return UIFunctionsLibrary.WRITE_IN_EMOTE_CHAT_LUA_SCRIPT_PARAMETER_DESCRIPTORS;
        }
        
        @Nullable
        @Override
        public LuaScriptParameterDescriptor[] getResultDescriptors() {
            return null;
        }
        
        @Override
        protected void run(final int paramCount) throws LuaException {
            if (paramCount < 1) {
                UIFunctionsLibrary.m_logger.warn((Object)"On utilise writeEmoteChat sans argument !");
                return;
            }
            final String msg = this.getParamString(0);
            String message;
            if (paramCount == 1) {
                message = WakfuTranslator.getInstance().getString(msg);
            }
            else {
                final Object[] params = new Object[paramCount - 1];
                for (int i = 1; i < paramCount; ++i) {
                    final Object param = this.getParamString(i);
                    params[i - 1] = param;
                }
                message = WakfuTranslator.getInstance().getString(msg, params);
            }
            final int chatPipe = 1;
            ChatManager.getInstance().pushMessage(message, 1);
        }
    }
    
    private static class FollowOnMiniMap extends JavaFunctionEx
    {
        FollowOnMiniMap(final LuaState luaState) {
            super(luaState);
        }
        
        @Override
        public final String getName() {
            return "miniMapMarker";
        }
        
        @Override
        public String getDescription() {
            return "Applique le marqueur de boussole sur la map et la minimap ? une position sp?cifi?e";
        }
        
        @Override
        public final LuaScriptParameterDescriptor[] getParameterDescriptors() {
            return UIFunctionsLibrary.FOLLOW_ON_MINIMAP_LUA_SCRIPT_PARAMETER_DESCRIPTORS;
        }
        
        @Nullable
        @Override
        public final LuaScriptParameterDescriptor[] getResultDescriptors() {
            return null;
        }
        
        public final void run(final int paramCount) throws LuaException {
            final int x = this.getParamInt(0);
            final int y = this.getParamInt(1);
            final int z = this.getParamInt(2);
            MapManager.getInstance().setUniqueCompassPoint(x, y, z, WakfuGameEntity.getInstance().getLocalPlayer().getInstanceId(), "", this, DisplayableMapPointIconFactory.COMPASS_POINT_ICON, WakfuClientConstants.MINI_MAP_POINT_COLOR_COMPASS_DEFAULT);
        }
    }
    
    private static class RemoveFromMiniMap extends JavaFunctionEx
    {
        RemoveFromMiniMap(final LuaState luaState) {
            super(luaState);
        }
        
        @Override
        public String getDescription() {
            return "Enl?ve le marqueur de boussole de la map et de la miniMap";
        }
        
        @Override
        public final String getName() {
            return "removeMiniMapMarker";
        }
        
        @Override
        public final LuaScriptParameterDescriptor[] getParameterDescriptors() {
            return UIFunctionsLibrary.REMOVE_FROM_MINIMAP_LUA_SCRIPT_PARAMETER_DESCRIPTORS;
        }
        
        @Nullable
        @Override
        public final LuaScriptParameterDescriptor[] getResultDescriptors() {
            return null;
        }
        
        public final void run(final int paramCount) {
            MapManager.getInstance().removeUniqueCompass();
        }
    }
    
    private static class WriteBloopsInChat extends JavaFunctionEx
    {
        WriteBloopsInChat(final LuaState luaState) {
            super(luaState);
        }
        
        @Override
        public final String getName() {
            return "writeInChat";
        }
        
        @Override
        public String getDescription() {
            return "[DEPRECATED] ?crit des informations NON LOCALIS?ES dans le chat d'information de jeu";
        }
        
        @Override
        public final LuaScriptParameterDescriptor[] getParameterDescriptors() {
            return UIFunctionsLibrary.WRITE_BLOOPS_IN_CHAT_LUA_SCRIPT_PARAMETER_DESCRIPTORS;
        }
        
        @Nullable
        @Override
        public final LuaScriptParameterDescriptor[] getResultDescriptors() {
            return null;
        }
        
        public final void run(final int paramCount) throws LuaException {
            final StringBuilder builder = new StringBuilder();
            for (int i = 0; i < paramCount; ++i) {
                final String param = this.getParamForcedAsString(i);
                builder.append(" ").append((param != null) ? param : null);
            }
            final int chatPipe = 4;
            ChatManager.getInstance().pushMessage(builder.toString(), 4);
            UIFunctionsLibrary.m_logger.warn((Object)("On utilise writeInChat pour : " + (Object)builder + ", pr?f?rer writeLocalisedInChat"));
        }
    }
    
    private static class WriteUnlocalisedInChat extends JavaFunctionEx
    {
        WriteUnlocalisedInChat(final LuaState luaState) {
            super(luaState);
        }
        
        @Override
        public final String getName() {
            return "writeUnlocalisedInChat";
        }
        
        @Override
        public String getDescription() {
            return "[DEPRECATED] ?crit un message NON LOCALIS? dans le chat, fait parler un personnage";
        }
        
        @Override
        public final LuaScriptParameterDescriptor[] getParameterDescriptors() {
            return UIFunctionsLibrary.WRITE_UNLOCALIZED_IN_CHAT_LUA_SCRIPT_PARAMETER_DESCRIPTORS;
        }
        
        @Nullable
        @Override
        public final LuaScriptParameterDescriptor[] getResultDescriptors() {
            return null;
        }
        
        public final void run(final int paramCount) throws LuaException {
            if (paramCount < 2) {
                UIFunctionsLibrary.m_logger.warn((Object)"On utilise writeEmoteChat sans argument !");
                return;
            }
            final String msg = this.getParamString(0);
            final int sourceId = this.getParamInt(1);
            String message;
            if (paramCount == 2) {
                message = msg;
            }
            else {
                final Object[] params = new Object[paramCount - 1];
                for (int i = 1; i < paramCount; ++i) {
                    final Object param = this.getParamString(i);
                    params[i - 1] = param;
                }
                message = String.format(msg, params);
            }
            final ChatMessage chatMsg = new ChatMessage(sourceId, message);
            chatMsg.setPipeDestination(1);
            ChatManager.getInstance().pushMessage(chatMsg);
        }
    }
    
    private static class DisplaySmiley extends JavaFunctionEx
    {
        DisplaySmiley(final LuaState luaState) {
            super(luaState);
        }
        
        @Override
        public final String getName() {
            return "displaySmiley";
        }
        
        @Override
        public String getDescription() {
            return "Affiche le smiley sp?cifi? au-dessus d'un mobile";
        }
        
        @Override
        public final LuaScriptParameterDescriptor[] getParameterDescriptors() {
            return UIFunctionsLibrary.DISPLAY_SMILEY_LUA_SCRIPT_PARAMETER_DESCRIPTORS;
        }
        
        @Nullable
        @Override
        public final LuaScriptParameterDescriptor[] getResultDescriptors() {
            return null;
        }
        
        public final void run(final int paramCount) throws LuaException {
            if (paramCount < 2) {
                UIFunctionsLibrary.m_logger.warn((Object)"On utilise displaySmiley sans argument !");
                return;
            }
            final int familyId = (paramCount > 2) ? this.getParamInt(2) : -1;
            UIChatFrame.getInstance().displayEmoteIcon(this.getParamInt(0), this.getParamLong(1), familyId);
        }
    }
    
    private static class PushStaticProtector extends JavaFunctionEx
    {
        PushStaticProtector(final LuaState luaState) {
            super(luaState);
        }
        
        @Override
        public String getName() {
            return "pushStaticProtector";
        }
        
        @Override
        public String getDescription() {
            return "Initialise et affiche les interfaces de protecteur avec les informations d'un protecteur donn? (static)";
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getParameterDescriptors() {
            return UIFunctionsLibrary.PUSH_STATIC_PROTECTOR_LUA_SCRIPT_PARAMETER_DESCRIPTORS;
        }
        
        @Nullable
        @Override
        public LuaScriptParameterDescriptor[] getResultDescriptors() {
            return null;
        }
        
        @Override
        protected void run(final int paramCount) throws LuaException {
            if (paramCount < 1) {
                UIFunctionsLibrary.m_logger.warn((Object)"On utilise pushStaticProtector sans argument !");
                return;
            }
            final int protectorId = this.getParamInt(0);
            final UIProtectorFrame uiProtectorFrame = UIProtectorFrame.getInstance();
            if (StaticProtectorView.INSTANCE.getProtectorId() != -1) {
                UIFunctionsLibrary.m_logger.warn((Object)"Attention, on push un protecteur static au-dessus d'un autre protecteur !");
            }
            StaticProtectorView.INSTANCE.setProtectorId(protectorId);
            if (!WakfuGameEntity.getInstance().hasFrame(uiProtectorFrame)) {
                WakfuGameEntity.getInstance().pushFrame(uiProtectorFrame);
            }
        }
    }
    
    private static class SetStaticProtectorInterval extends JavaFunctionEx
    {
        SetStaticProtectorInterval(final LuaState luaState) {
            super(luaState);
        }
        
        @Override
        public String getName() {
            return "setStaticProtectorInterval";
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getParameterDescriptors() {
            return UIFunctionsLibrary.SET_STATIC_PROTECTOR_INTERVAL_LUA_SCRIPT_PARAMETER_DESCRIPTORS;
        }
        
        @Nullable
        @Override
        public LuaScriptParameterDescriptor[] getResultDescriptors() {
            return null;
        }
        
        @Override
        protected void run(final int paramCount) throws LuaException {
            if (paramCount < 4) {
                UIFunctionsLibrary.m_logger.warn((Object)"On utilise setStaticProtectorInterval avec trop peu d'arguments !");
                return;
            }
            if (StaticProtectorView.INSTANCE.getProtectorId() == -1) {
                UIFunctionsLibrary.m_logger.error((Object)"On ajoute un interval de volont? au protecteur static qui n'existe pas");
                return;
            }
            final boolean monster = this.getParamBool(0);
            final int familyId = this.getParamInt(1);
            final int minValue = this.getParamInt(2);
            final int maxValue = this.getParamInt(3);
            if (maxValue < minValue) {
                UIFunctionsLibrary.m_logger.error((Object)"On ajoute un interval de volont? dont la borne max est inf?rieure ? la borne min au protecteur static !");
                return;
            }
            final WakfuEcosystemFamilyInfo wakfuEcosystemFamilyInfo = monster ? WakfuMonsterZoneManager.getInstance().getMonsterFamilyInfo(familyId) : WakfuResourceZoneManager.getInstance().getResourceFamilyInfo(familyId);
            if (wakfuEcosystemFamilyInfo == null) {
                UIFunctionsLibrary.m_logger.error((Object)"Impossible de retrouver la famille de ressource pour laquelle on cherche ? ajouter un interval de volont? de mdc");
                return;
            }
            final Interval interval = new Interval(minValue, maxValue);
            wakfuEcosystemFamilyInfo.setProtectorInterval(interval);
            StaticProtectorView.INSTANCE.addProtectorInterval(familyId, interval);
        }
    }
    
    private static class DisplaySplashScreenImage extends JavaFunctionEx
    {
        DisplaySplashScreenImage(final LuaState luaState) {
            super(luaState);
        }
        
        @Override
        public String getName() {
            return "displaySplashScreenImage";
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getParameterDescriptors() {
            return UIFunctionsLibrary.DISPLAY_SPLASH_SCREEN_IMAGE_LUA_SCRIPT_PARAMETER_DESCRIPTORS;
        }
        
        @Override
        public String getDescription() {
            return "Affiche une image au centre de l'?cran (non modale)";
        }
        
        @Nullable
        @Override
        public LuaScriptParameterDescriptor[] getResultDescriptors() {
            return null;
        }
        
        @Override
        protected void run(final int paramCount) throws LuaException {
            try {
                if (paramCount < 1) {
                    UIFunctionsLibrary.m_logger.warn((Object)"On utilise displaySplashScreenImage sans argument !");
                    return;
                }
                Xulor.getInstance().load("splashScreenDialog", Dialogs.getDialogPath("splashScreenDialog"), 1L, (short)15000);
                PropertiesProvider.getInstance().setPropertyValue("splashScreenIconUrl", this.getParamString(0));
            }
            catch (Exception e) {
                UIFunctionsLibrary.m_logger.error((Object)"Exception lev?e lors det l'ajout d'un message de personnage sp?cial", (Throwable)e);
            }
        }
    }
    
    private static class IsBarLockedMode extends JavaFunctionEx
    {
        IsBarLockedMode(final LuaState luaState) {
            super(luaState);
        }
        
        @Override
        public String getName() {
            return "isBarLockedMode";
        }
        
        @Override
        public String getDescription() {
            return "Indique si le controlCenter (barres, coeur,...) est verouill?e ou non";
        }
        
        @Nullable
        @Override
        public LuaScriptParameterDescriptor[] getParameterDescriptors() {
            return null;
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getResultDescriptors() {
            return UIFunctionsLibrary.IS_BAR_LOCKED_MODE_LUA_SCRIPT_PARAMETER_DESCRIPTORS;
        }
        
        @Override
        protected void run(final int paramCount) throws LuaException {
            this.addReturnValue(WakfuClientInstance.getInstance().getGamePreferences().getBooleanValue(WakfuKeyPreferenceStoreEnum.BARS_LOCKED_MODE_KEY));
        }
    }
    
    private static class IsDialogLoaded extends JavaFunctionEx
    {
        IsDialogLoaded(final LuaState luaState) {
            super(luaState);
        }
        
        @Override
        public String getName() {
            return "isDialogLoaded";
        }
        
        @Override
        public String getDescription() {
            return "Indique si l'interface sp?cifi?e est ouverte ou non";
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getParameterDescriptors() {
            return UIFunctionsLibrary.IS_DIALOG_LOADED_LUA_SCRIPT_PARAMETER_DESCRIPTORS;
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getResultDescriptors() {
            return UIFunctionsLibrary.IS_DIALOG_LOADED_RETURN_LUA_SCRIPT_PARAMETER_DESCRIPTORS;
        }
        
        @Override
        protected void run(final int paramCount) throws LuaException {
            this.addReturnValue(Xulor.getInstance().isLoaded(this.getParamString(0)));
        }
    }
    
    private static class PushPetMessage extends JavaFunctionEx
    {
        PushPetMessage(final LuaState luaState) {
            super(luaState);
        }
        
        @Override
        public String getName() {
            return "pushPetMessage";
        }
        
        @Override
        public String getDescription() {
            return "[Obsol?te] Ouvre une bulle de dialogue G?lutin au dessus du joueur";
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getParameterDescriptors() {
            return UIFunctionsLibrary.PUSH_PET_MESSAGE_LUA_SCRIPT_PARAMETER_DESCRIPTORS;
        }
        
        @Nullable
        @Override
        public LuaScriptParameterDescriptor[] getResultDescriptors() {
            return null;
        }
        
        @Override
        protected void run(final int paramCount) throws LuaException {
            try {
                if (paramCount < 1) {
                    UIFunctionsLibrary.m_logger.warn((Object)"On utilise PushPetMessage sans argument !");
                    return;
                }
                final int messageParamCount = this.getParamInt(0);
                String message;
                if (messageParamCount == 0) {
                    message = WakfuTranslator.getInstance().getString(this.getParamString(1));
                }
                else {
                    final String[] params = new String[messageParamCount - 1];
                    for (int i = 1; i < params.length; ++i) {
                        final String param = this.getParamForcedAsString(i);
                        params[i - 1] = param;
                    }
                    message = WakfuTranslator.getInstance().getString(this.getParamString(1), (Object[])params);
                }
                final Boolean blocking = paramCount == 2 || this.getParamBool(2);
                if (paramCount < 4) {
                    UITutorialFrame.getInstance().pushPetMessage(message, blocking, 0L, PetEmotion.NEUTRAL);
                    return;
                }
                int paramIndex = 3;
                final LuaValue[] values = this.getParams(paramIndex, paramCount);
                if (values[0].getType() == LuaScriptParameterType.NUMBER) {
                    UITutorialFrame.getInstance().pushPetMessage(message, blocking, this.getParamInt(paramIndex), PetEmotion.NEUTRAL);
                    ++paramIndex;
                }
                else {
                    UITutorialFrame.getInstance().pushPetMessage(message, blocking, 0L, PetEmotion.NEUTRAL);
                }
                final String funcName = this.getParamString(paramIndex);
                final LuaValue[] params2 = this.getParams(paramIndex + 1, paramCount);
                final LuaScript script = this.getScriptObject();
                final String dialogName = "petBubbleDialog";
                EventListener el = (EventListener)ScriptUIEventManager.getInstance().eventListenerAlreadyExist(script, "petBubbleDialog", null, null, funcName);
                if (el == null) {
                    el = new LuaCallback(script, funcName, params2) {
                        @Override
                        public boolean run(final Event event) {
                            return this.run();
                        }
                    };
                    ScriptUIEventManager.getInstance().putEventListener(script, "petBubbleDialog", null, null, funcName, el);
                    UITutorialFrame.getInstance().setPetDialogEndEventListener(el);
                }
                else {
                    ((LuaCallback)el).setParams(params2);
                    final EventListener listener = UITutorialFrame.getInstance().getListener();
                    if (listener == null || listener != el) {
                        UITutorialFrame.getInstance().setPetDialogEndEventListener(el);
                    }
                }
            }
            catch (Exception e) {
                UIFunctionsLibrary.m_logger.error((Object)"Exception lev?e lors de l'ajout d'un message de familier", (Throwable)e);
            }
        }
    }
    
    private static class SetNextPetMessage extends JavaFunctionEx
    {
        SetNextPetMessage(final LuaState luaState) {
            super(luaState);
        }
        
        @Override
        public String getName() {
            return "setNextPetMessage";
        }
        
        @Override
        public String getDescription() {
            return "[Obsol?te] Fais passer le message de g?lutin courant";
        }
        
        @Nullable
        @Override
        public LuaScriptParameterDescriptor[] getParameterDescriptors() {
            return null;
        }
        
        @Nullable
        @Override
        public LuaScriptParameterDescriptor[] getResultDescriptors() {
            return null;
        }
        
        @Override
        protected void run(final int paramCount) throws LuaException {
            UITutorialFrame.getInstance().setNextPetMessage();
        }
    }
    
    private static class DisplayBackground extends JavaFunctionEx
    {
        DisplayBackground(final LuaState luaState) {
            super(luaState);
        }
        
        @Override
        public String getName() {
            return "displayBackground";
        }
        
        @Override
        public String getDescription() {
            return "Ouvre l'interface d'affichage background (Affiche, St?le, Livre, etc..)";
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getParameterDescriptors() {
            return UIFunctionsLibrary.DISPLAY_BACKGROUND_LUA_SCRIPT_PARAMETER_DESCRIPTORS;
        }
        
        @Nullable
        @Override
        public LuaScriptParameterDescriptor[] getResultDescriptors() {
            return null;
        }
        
        @Override
        protected void run(final int paramCount) throws LuaException {
            final UIBackgroundDisplayFrame displayFrame = UIBackgroundDisplayFrame.getInstance();
            displayFrame.loadBackgroundDisplay(this.getParamInt(0));
            WakfuGameEntity.getInstance().pushFrame(displayFrame);
        }
    }
    
    private static class DisplayDungeonLadderMonsterCount extends JavaFunctionEx
    {
        DisplayDungeonLadderMonsterCount(final LuaState luaState) {
            super(luaState);
        }
        
        @Override
        public String getName() {
            return "displayDungeonLadderMonsterCount";
        }
        
        @Override
        public String getDescription() {
            return "[DONJON SHUKRUTE] Initialise et affiche le compteur de monstres du donjon";
        }
        
        @Nullable
        @Override
        public LuaScriptParameterDescriptor[] getParameterDescriptors() {
            return null;
        }
        
        @Nullable
        @Override
        public LuaScriptParameterDescriptor[] getResultDescriptors() {
            return null;
        }
        
        @Override
        protected void run(final int paramCount) throws LuaException {
            UIDungeonLadderFrame.getInstance().displayDungeonLadderMonsterCount();
        }
    }
    
    private static class UnloadDungeonStatut extends JavaFunctionEx
    {
        UnloadDungeonStatut(final LuaState luaState) {
            super(luaState);
        }
        
        @Override
        public String getName() {
            return "unloadDungeonStatut";
        }
        
        @Override
        public String getDescription() {
            return "[DONJON SHUKRUTE] Fais dispara?tre les affichages du donjon";
        }
        
        @Nullable
        @Override
        public LuaScriptParameterDescriptor[] getParameterDescriptors() {
            return null;
        }
        
        @Nullable
        @Override
        public LuaScriptParameterDescriptor[] getResultDescriptors() {
            return null;
        }
        
        @Override
        protected void run(final int paramCount) throws LuaException {
            UIDungeonLadderFrame.getInstance().unloadDungeonStatut();
        }
    }
    
    private static class IncrementDungeonLadderMonsterCount extends JavaFunctionEx
    {
        IncrementDungeonLadderMonsterCount(final LuaState luaState) {
            super(luaState);
        }
        
        @Override
        public String getName() {
            return "incrementDungeonLadderMonsterCount";
        }
        
        @Override
        public String getDescription() {
            return "[DONJON SHUKRUTE] Incr?mente le compteur de monstres du donjon";
        }
        
        @Nullable
        @Override
        public LuaScriptParameterDescriptor[] getParameterDescriptors() {
            return null;
        }
        
        @Nullable
        @Override
        public LuaScriptParameterDescriptor[] getResultDescriptors() {
            return null;
        }
        
        @Override
        protected void run(final int paramCount) throws LuaException {
            UIDungeonLadderFrame.getInstance().incrementDungeonLadderMonsterCount();
        }
    }
    
    private static class DisplayDungeonLadderTimer extends JavaFunctionEx
    {
        DisplayDungeonLadderTimer(final LuaState luaState) {
            super(luaState);
        }
        
        @Override
        public String getName() {
            return "displayDungeonLadderTimer";
        }
        
        @Override
        public String getDescription() {
            return "[DONJON SHUKRUTE] Affiche et d?marre le chrono du donjon";
        }
        
        @Nullable
        @Override
        public LuaScriptParameterDescriptor[] getParameterDescriptors() {
            return null;
        }
        
        @Nullable
        @Override
        public LuaScriptParameterDescriptor[] getResultDescriptors() {
            return null;
        }
        
        @Override
        protected void run(final int paramCount) throws LuaException {
            UIDungeonLadderFrame.getInstance().displayDungeonLadderTimer();
        }
    }
    
    private static class StopDungeonLadderTimer extends JavaFunctionEx
    {
        StopDungeonLadderTimer(final LuaState luaState) {
            super(luaState);
        }
        
        @Override
        public String getName() {
            return "stopDungeonLadderTimer";
        }
        
        @Override
        public String getDescription() {
            return "[DONJON SHUKRUTE] Arr?te le chrono du donjon";
        }
        
        @Nullable
        @Override
        public LuaScriptParameterDescriptor[] getParameterDescriptors() {
            return null;
        }
        
        @Nullable
        @Override
        public LuaScriptParameterDescriptor[] getResultDescriptors() {
            return null;
        }
        
        @Override
        protected void run(final int paramCount) throws LuaException {
            UIDungeonLadderFrame.getInstance().stopDungeonLadderTimer();
        }
    }
    
    private static class DisplaySplashText extends JavaFunctionEx
    {
        DisplaySplashText(final LuaState luaState) {
            super(luaState);
        }
        
        @Override
        public String getName() {
            return "displaySplashText";
        }
        
        @Override
        public String getDescription() {
            return "[DONJON SHUKRUTE] Affiche un message splash (trace de pneu + particule..)";
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getParameterDescriptors() {
            return UIFunctionsLibrary.SPLASH_TEXT_LUA_SCRIPT_PARAMETER_DESCRIPTORS;
        }
        
        @Nullable
        @Override
        public LuaScriptParameterDescriptor[] getResultDescriptors() {
            return null;
        }
        
        @Override
        protected void run(final int paramCount) throws LuaException {
            final String msg = this.getParamString(0);
            String message;
            if (paramCount == 1) {
                message = WakfuTranslator.getInstance().getString(msg);
            }
            else {
                final Object[] params = new Object[paramCount - 1];
                for (int i = 1; i < paramCount; ++i) {
                    final Object param = this.getParamString(i);
                    params[i - 1] = param;
                }
                message = WakfuTranslator.getInstance().getString(msg, params);
            }
            UIDungeonLadderFrame.getInstance().displaySplashText(message);
        }
    }
    
    private static class DisplayDungeonResultSplashText extends JavaFunctionEx
    {
        DisplayDungeonResultSplashText(final LuaState luaState) {
            super(luaState);
        }
        
        @Override
        public String getName() {
            return "displayDungeonResultSplashText";
        }
        
        @Override
        public String getDescription() {
            return "[DONJON SHUKRUTE] Affiche le r?sultat du donjon en fonction de la position sur le podium du groupe de joueur";
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getParameterDescriptors() {
            return UIFunctionsLibrary.DUNGEON_RESULT_SPLASH_TEXT_LUA_SCRIPT_PARAMETER_DESCRIPTORS;
        }
        
        @Nullable
        @Override
        public LuaScriptParameterDescriptor[] getResultDescriptors() {
            return null;
        }
        
        @Override
        protected void run(final int paramCount) throws LuaException {
            if (paramCount < 1) {
                UIFunctionsLibrary.m_logger.warn((Object)"On utilise addSpellSelectionListener sans argument !");
                return;
            }
            UIDungeonLadderFrame.getInstance().displayDungeonResultSplashText((short)this.getParamInt(0));
        }
    }
    
    private static class AddSpellSelectionListener extends JavaFunctionEx
    {
        AddSpellSelectionListener(final LuaState luaState) {
            super(luaState);
        }
        
        @Override
        public String getName() {
            return "addSpellSelectionListener";
        }
        
        @Override
        public String getDescription() {
            return "Ajoute une ?coute sur la s?lection d'un sort";
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getParameterDescriptors() {
            return UIFunctionsLibrary.ADD_SPELL_SELECTION_LISTENER_LUA_SCRIPT_PARAMETER_DESCRIPTORS;
        }
        
        @Nullable
        @Override
        public LuaScriptParameterDescriptor[] getResultDescriptors() {
            return null;
        }
        
        @Override
        protected void run(final int paramCount) throws LuaException {
            if (paramCount < 1) {
                UIFunctionsLibrary.m_logger.warn((Object)"On utilise addSpellSelectionListener sans argument !");
                return;
            }
            final Boolean blocking = this.getParamBool(0);
            final String funcName = this.getParamString(1);
            final LuaValue[] params = this.getParams(2, paramCount);
            final LuaScript script = this.getScriptObject();
            SpellSelectedListener el = (SpellSelectedListener)ScriptUIEventManager.getInstance().eventListenerAlreadyExist(script, null, null, null, funcName);
            if (el == null) {
                el = new SpellSelectedListener(script, funcName, params, blocking);
                ScriptUIEventManager.getInstance().putEventListener(script, null, null, null, funcName, el);
                UIFightTurnFrame.getInstance().setSpellSelectedListener(el);
            }
            else {
                el.setParams(params);
                final SpellSelectedListener listener = UIFightTurnFrame.getInstance().getSpellSelectedListener();
                if (listener == null || listener != el) {
                    UIFightTurnFrame.getInstance().setSpellSelectedListener(el);
                }
            }
        }
    }
    
    private static class RemoveSpellSelectionListener extends JavaFunctionEx
    {
        RemoveSpellSelectionListener(final LuaState luaState) {
            super(luaState);
        }
        
        @Override
        public String getName() {
            return "removeSpellSelectionListener";
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getParameterDescriptors() {
            return UIFunctionsLibrary.REMOVE_SPELL_SELECTION_LISTENER_LUA_SCRIPT_PARAMETER_DESCRIPTORS;
        }
        
        @Override
        public String getDescription() {
            return "Supprime l'?coute de la s?lection d'un sort par une fonction script";
        }
        
        @Nullable
        @Override
        public LuaScriptParameterDescriptor[] getResultDescriptors() {
            return null;
        }
        
        @Override
        protected void run(final int paramCount) throws LuaException {
            if (paramCount < 1) {
                UIFunctionsLibrary.m_logger.warn((Object)"On utilise removeSpellSelectionListener sans argument !");
                return;
            }
            final String funcName = this.getParamString(0);
            final LuaScript script = this.getScriptObject();
            ScriptUIEventManager.getInstance().removeEventListener(script, null, null, null, funcName);
            UIFightTurnFrame.getInstance().setSpellSelectedListener(null);
        }
    }
    
    private static class AddLODListener extends JavaFunctionEx
    {
        AddLODListener(final LuaState luaState) {
            super(luaState);
        }
        
        @Override
        public String getName() {
            return "addLODListener";
        }
        
        @Override
        public String getDescription() {
            return "Ajoute une ?coute sur le changement de niveau de d?tails du jeu";
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getParameterDescriptors() {
            return UIFunctionsLibrary.ADD_LOD_LISTENER_LUA_SCRIPT_PARAMETER_DESCRIPTORS;
        }
        
        @Nullable
        @Override
        public LuaScriptParameterDescriptor[] getResultDescriptors() {
            return null;
        }
        
        @Override
        protected void run(final int paramCount) throws LuaException {
            if (paramCount < 1) {
                UIFunctionsLibrary.m_logger.warn((Object)"On utilise addLODListener sans argument !");
                return;
            }
            final String funcName = this.getParamString(0);
            final LuaScript script = this.getScriptObject();
            WakfuClientInstance.getInstance().addLODChangeListener(new LODChangeListener() {
                @Override
                public void onLODChange(final int newLevel) {
                    script.runFunction(funcName, new LuaValue[] { new LuaValue(newLevel) }, new LuaTable[0]);
                }
            });
        }
    }
    
    private static class GetLOD extends JavaFunctionEx
    {
        GetLOD(final LuaState luaState) {
            super(luaState);
        }
        
        @Override
        public String getName() {
            return "getLOD";
        }
        
        @Override
        public String getDescription() {
            return "Fourni le niveau de d?tails actuel du jeu";
        }
        
        @Nullable
        @Override
        public LuaScriptParameterDescriptor[] getParameterDescriptors() {
            return null;
        }
        
        @Override
        public final LuaScriptParameterDescriptor[] getResultDescriptors() {
            return UIFunctionsLibrary.GET_LOD_LUA_SCRIPT_PARAMETER_DESCRIPTORS;
        }
        
        @Override
        protected void run(final int paramCount) throws LuaException {
            this.addReturnValue(DisplayedScreenWorld.getInstance().getLodLevel());
        }
    }
    
    private static class AddCompass extends JavaFunctionEx
    {
        AddCompass(final LuaState luaState) {
            super(luaState);
        }
        
        @Override
        public String getName() {
            return "addCompass";
        }
        
        @Override
        public String getDescription() {
            return "Cr?e une boussole";
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getParameterDescriptors() {
            return UIFunctionsLibrary.ADD_COMPASS_LUA_SCRIPT_PARAMETER_DESCRIPTORS;
        }
        
        @Override
        public final LuaScriptParameterDescriptor[] getResultDescriptors() {
            return UIFunctionsLibrary.ADD_COMPASS_RETURN_LUA_SCRIPT_PARAMETER_DESCRIPTORS;
        }
        
        @Override
        protected void run(final int paramCount) throws LuaException {
            final String name = WakfuTranslator.getInstance().getString(this.getParamString(0));
            final int x = this.getParamInt(1);
            final int y = this.getParamInt(2);
            short z = (short)((paramCount >= 4) ? ((short)this.getParamInt(3)) : 0);
            z = TopologyMapManager.getNearestZ(x, y, z);
            if (z == -32768) {
                z = 0;
            }
            int type = 2;
            if (paramCount >= 5) {
                type = this.getParamInt(4);
            }
            final long id = GUIDGenerator.getGUID();
            MapManager.getInstance().addCompassPointAndPositionMarker(type, x, y, z, WakfuGameEntity.getInstance().getLocalPlayer().getInstanceId(), null, name, true);
            this.addReturnValue(id);
        }
    }
    
    private static class RemoveCompass extends JavaFunctionEx
    {
        RemoveCompass(final LuaState luaState) {
            super(luaState);
        }
        
        @Override
        public String getName() {
            return "removeCompass";
        }
        
        @Override
        public String getDescription() {
            return "Retire la boussole";
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getParameterDescriptors() {
            return UIFunctionsLibrary.REMOVE_COMPASS_LUA_SCRIPT_PARAMETER_DESCRIPTORS;
        }
        
        @Nullable
        @Override
        public final LuaScriptParameterDescriptor[] getResultDescriptors() {
            return null;
        }
        
        @Override
        protected void run(final int paramCount) throws LuaException {
            MapManager.getInstance().removeCompassPointAndPositionMarker();
        }
    }
    
    private static class OpenRewardsDialog extends JavaFunctionEx
    {
        OpenRewardsDialog(final LuaState luaState) {
            super(luaState);
        }
        
        @Override
        public String getName() {
            return "openRewardsDialog";
        }
        
        @Override
        public String getDescription() {
            return "[DONJON SHUKRUTE] Affiche l'interface de r?compenses avec un texte donn?";
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getParameterDescriptors() {
            return UIFunctionsLibrary.OPEN_REWARDS_DIALOG_LUA_SCRIPT_PARAMETER_DESCRIPTORS;
        }
        
        @Nullable
        @Override
        public final LuaScriptParameterDescriptor[] getResultDescriptors() {
            return null;
        }
        
        public void run(final int paramCount) throws LuaException {
            String desc = null;
            final String key = this.getParamString(0);
            if (paramCount >= 1) {
                if (paramCount == 1) {
                    desc = WakfuTranslator.getInstance().getString(key);
                }
                else {
                    final String[] params = new String[paramCount - 1];
                    for (int i = 1; i < paramCount; ++i) {
                        final String param = this.getParamForcedAsString(i);
                        params[i - 1] = param;
                    }
                    desc = WakfuTranslator.getInstance().getString(desc, (Object[])params);
                }
            }
            final UIShukruteRewardFrame.ShukruteReward reward = new UIShukruteRewardFrame.ShukruteReward(desc);
            UIShukruteRewardFrame.getInstance().loadShukruteRewards(reward);
        }
    }
    
    private static class OpenInfoDialog extends JavaFunctionEx
    {
        OpenInfoDialog(final LuaState luaState) {
            super(luaState);
        }
        
        @Override
        public String getName() {
            return "openInfoDialog";
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getParameterDescriptors() {
            return UIFunctionsLibrary.OPEN_INFO_DIALOG_LUA_SCRIPT_PARAMETER_DESCRIPTORS;
        }
        
        @Nullable
        @Override
        public final LuaScriptParameterDescriptor[] getResultDescriptors() {
            return null;
        }
        
        @Override
        public String getDescription() {
            return "Ouvre un popup semblable ? celui des donjons shukrutes, mais sans ic?ne challenge et avec un titre param?tr?";
        }
        
        public void run(final int paramCount) throws LuaException {
            String desc = null;
            final String title = WakfuTranslator.getInstance().getString(this.getParamString(0));
            if (paramCount >= 1) {
                final String translationKey = this.getParamString(1);
                if (paramCount == 1) {
                    desc = WakfuTranslator.getInstance().getString(translationKey);
                }
                else {
                    final String[] params = new String[paramCount - 1];
                    for (int i = 1; i < paramCount; ++i) {
                        final String param = this.getParamForcedAsString(i);
                        params[i - 1] = param;
                    }
                    desc = WakfuTranslator.getInstance().getString(translationKey, (Object[])params);
                }
            }
            final UIInfoDialogFrame.InfoView infoView = new UIInfoDialogFrame.InfoView(title, desc);
            UIInfoDialogFrame.getInstance().loadInfoDialog(infoView);
        }
    }
    
    private static class AddReward extends JavaFunctionEx
    {
        AddReward(final LuaState luaState) {
            super(luaState);
        }
        
        @Override
        public String getName() {
            return "addReward";
        }
        
        @Override
        public String getDescription() {
            return "[DONJON SHUKRUTE] Ajoute une r?compense ? l'interface de r?compenses du donjon";
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getParameterDescriptors() {
            return UIFunctionsLibrary.ADD_REWARD_LUA_SCRIPT_PARAMETER_DESCRIPTORS;
        }
        
        @Nullable
        @Override
        public final LuaScriptParameterDescriptor[] getResultDescriptors() {
            return null;
        }
        
        public void run(final int paramCount) throws LuaException {
            String desc = null;
            if (paramCount >= 2) {
                final String key = this.getParamString(1);
                if (paramCount == 2) {
                    desc = WakfuTranslator.getInstance().getString(key);
                }
                else {
                    final String[] params = new String[paramCount - 2];
                    for (int i = 2; i < paramCount; ++i) {
                        final String param = this.getParamForcedAsString(i);
                        params[i - 2] = param;
                    }
                    desc = WakfuTranslator.getInstance().getString(desc, (Object[])params);
                }
            }
            final UIShukruteRewardFrame.ShukruteRewardItem rewardItem = new UIShukruteRewardFrame.ShukruteRewardItem(this.getParamInt(0), desc);
            UIShukruteRewardFrame.getInstance().addReward(rewardItem);
        }
    }
    
    private static class LoadTutorialMessageDialog extends JavaFunctionEx
    {
        LoadTutorialMessageDialog(final LuaState luaState) {
            super(luaState);
        }
        
        @Override
        public String getName() {
            return "loadTutorialDialog";
        }
        
        @Override
        public String getDescription() {
            return "Ouvre l'interface de tutorial";
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getParameterDescriptors() {
            return UIFunctionsLibrary.LOAD_TUTORIAL_LUA_SCRIPT_PARAMETER_DESCRIPTORS;
        }
        
        @Nullable
        @Override
        public final LuaScriptParameterDescriptor[] getResultDescriptors() {
            return null;
        }
        
        public void run(final int paramCount) throws LuaException {
            if (paramCount < 3) {
                UIFunctionsLibrary.m_logger.warn((Object)"On utilise LoadTutorialDialog avec trop peu d'arguments !");
                return;
            }
            int currentIndex = 0;
            final String paramString = this.getParamString(currentIndex++);
            final String iconName = "null".equals(paramString.toLowerCase()) ? null : paramString;
            final String title = WakfuTranslator.getInstance().getString(this.getParamString(currentIndex++));
            final String key = this.getParamString(currentIndex++);
            int type;
            if (paramCount == currentIndex) {
                type = 0;
            }
            else {
                try {
                    type = this.getParamInt(currentIndex);
                    ++currentIndex;
                }
                catch (LuaException e) {
                    type = 0;
                }
            }
            String desc = null;
            if (paramCount - currentIndex == 0) {
                desc = WakfuTranslator.getInstance().getString(key);
            }
            else {
                final String[] params = new String[paramCount - currentIndex];
                for (int i = currentIndex; i < paramCount; ++i) {
                    final String param = this.getParamForcedAsString(i);
                    params[i - currentIndex] = param;
                }
                desc = WakfuTranslator.getInstance().getString(desc, (Object[])params);
            }
            final Message msg = new UITutorialMessage(title, desc, iconName, type);
            Worker.getInstance().pushMessage(msg);
        }
    }
    
    private static class UnloadTutorialMessageDialog extends JavaFunctionEx
    {
        UnloadTutorialMessageDialog(final LuaState luaState) {
            super(luaState);
        }
        
        @Override
        public String getName() {
            return "unloadTutorialDialog";
        }
        
        @Override
        public String getDescription() {
            return "Ferme l'interface de tutorial";
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getParameterDescriptors() {
            return null;
        }
        
        @Nullable
        @Override
        public final LuaScriptParameterDescriptor[] getResultDescriptors() {
            return null;
        }
        
        public void run(final int paramCount) throws LuaException {
            final Message message = new UIMessage();
            message.setId(19149);
            Worker.getInstance().pushMessage(message);
        }
    }
    
    private static class ActivateFollowAchievement extends JavaFunctionEx
    {
        ActivateFollowAchievement(final LuaState luaState) {
            super(luaState);
        }
        
        @Override
        public String getName() {
            return "activateFollowAchievement";
        }
        
        @Override
        public String getDescription() {
            return "Active/D\u00e9sactive le suivi des qu\u00eates";
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getParameterDescriptors() {
            return UIFunctionsLibrary.ACTIVATE_FOLLOW_ACHIEVEMENT_LUA_SCRIPT_PARAMETER_DESCRIPTORS;
        }
        
        @Nullable
        @Override
        public final LuaScriptParameterDescriptor[] getResultDescriptors() {
            return null;
        }
        
        public void run(final int paramCount) throws LuaException {
            if (paramCount < 1) {
                return;
            }
            PropertiesProvider.getInstance().setPropertyValue("followAchievementsEnabled", this.getParamBool(0));
        }
    }
}
