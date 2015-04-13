package com.ankamagames.wakfu.client.network.protocol.frame;

import com.ankamagames.framework.kernel.events.*;
import com.ankamagames.wakfu.client.*;
import com.ankamagames.wakfu.client.core.game.item.*;
import com.ankamagames.wakfu.client.sound.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.wakfu.client.core.game.eventsCalendar.*;
import java.io.*;
import com.ankamagames.wakfu.client.core.game.events.*;
import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.wakfu.common.game.nation.event.*;
import com.ankamagames.wakfu.client.core.game.synchronizing.*;
import com.ankamagames.framework.kernel.core.common.message.synchronizing.*;
import com.ankamagames.wakfu.client.core.game.achievements.*;
import com.ankamagames.wakfu.client.core.game.background.gazette.*;
import com.ankamagames.framework.kernel.core.common.message.scheduler.clock.*;
import com.ankamagames.baseImpl.graphics.core.*;
import com.ankamagames.wakfu.client.chat.*;
import com.ankamagames.wakfu.client.core.contentInitializer.*;
import com.ankamagames.wakfu.common.game.hero.*;
import com.ankamagames.wakfu.common.configuration.*;
import com.ankamagames.wakfu.common.game.nation.*;
import com.ankamagames.wakfu.client.core.game.companion.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.actor.*;
import com.ankamagames.wakfu.client.core.game.fight.spectator.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.baseImpl.graphics.core.partitions.*;
import com.ankamagames.wakfu.client.core.world.havenWorld.*;
import com.ankamagames.baseImpl.graphics.alea.*;
import com.ankamagames.wakfu.client.core.world.*;
import com.ankamagames.wakfu.client.core.protector.*;
import java.nio.*;
import com.ankamagames.wakfu.common.game.nation.protector.*;
import com.ankamagames.wakfu.client.core.game.protector.*;
import com.ankamagames.wakfu.client.core.game.challenge.*;
import com.ankamagames.wakfu.common.constants.*;
import com.ankamagames.wakfu.common.game.time.calendar.*;
import com.ankamagames.wakfu.client.sound.ambiance2D.*;
import com.ankamagames.framework.fileFormat.properties.*;
import com.ankamagames.wakfu.client.core.companion.*;
import com.ankamagames.wakfu.client.network.protocol.frame.guild.*;
import com.ankamagames.baseImpl.graphics.ui.shortcuts.*;
import com.ankamagames.wakfu.client.core.taxes.*;
import com.ankamagames.wakfu.common.game.tax.*;
import com.ankamagames.wakfu.client.alea.graphics.*;
import com.ankamagames.baseImpl.graphics.alea.display.lights.*;
import com.ankamagames.wakfu.client.core.landMarks.*;
import com.ankamagames.wakfu.client.core.world.river.*;
import com.ankamagames.baseImpl.graphics.isometric.maskableLayer.*;
import com.ankamagames.wakfu.client.core.game.wakfu.*;
import com.ankamagames.wakfu.client.core.nation.*;
import com.ankamagames.wakfu.client.core.weather.*;
import com.ankamagames.baseImpl.graphics.alea.mobile.*;
import com.ankamagames.baseImpl.graphics.isometric.*;
import com.ankamagames.wakfu.client.core.game.miniMap.*;
import com.ankamagames.baseImpl.graphics.alea.environment.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.wakfu.client.core.game.actor.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.framework.graphics.engine.fadeManager.*;
import com.ankamagames.wakfu.client.ui.progress.*;
import com.ankamagames.wakfu.client.ui.actions.*;
import com.ankamagames.wakfu.client.core.game.achievements.ui.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.gift.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.personalSpace.*;
import com.ankamagames.wakfu.client.core.game.dimensionalBag.*;
import com.ankamagames.wakfu.client.core.game.embeddedTutorial.*;
import com.ankamagames.wakfu.common.game.effect.runningEffect.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.worldaction.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.wakfu.common.game.effect.*;
import com.ankamagames.framework.kernel.core.common.collections.*;
import com.ankamagames.wakfu.client.core.action.world.*;
import com.ankamagames.framework.script.*;
import com.ankamagames.framework.script.libraries.scriptedAction.*;
import com.ankamagames.framework.script.action.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.merchant.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.fight.*;
import com.ankamagames.wakfu.client.network.protocol.frame.fight.*;
import com.ankamagames.wakfu.client.core.action.*;
import org.apache.commons.lang3.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.chaos.*;
import com.ankamagames.wakfu.client.core.game.notificationSystem.*;
import com.ankamagames.wakfu.client.ui.protocol.message.notificationMessage.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.nation.*;
import com.ankamagames.wakfu.common.game.characterInfo.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.monster.*;
import com.ankamagames.baseImpl.client.proxyclient.base.chat.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.*;
import com.ankamagames.wakfu.common.game.interactiveElements.param.*;
import com.ankamagames.wakfu.common.game.interactiveElements.param.type.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.map.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.recustom.*;
import com.ankamagames.wakfu.client.network.protocol.message.world.serverToClient.*;
import com.ankamagames.wakfu.client.ui.*;
import com.ankamagames.wakfu.client.ui.protocol.message.*;
import com.ankamagames.wakfu.client.core.preferences.*;
import com.ankamagames.framework.preferences.*;
import com.ankamagames.wakfu.client.core.game.fight.*;
import java.util.*;
import com.ankamagames.wakfu.common.game.pet.*;
import com.ankamagames.wakfu.common.game.item.referenceItem.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.*;
import com.ankamagames.wakfu.common.game.protector.*;
import com.ankamagames.wakfu.common.game.travel.*;
import com.ankamagames.wakfu.common.game.travel.infos.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.xulor2.core.messagebox.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.global.clientToServer.group.*;
import com.ankamagames.wakfu.client.core.game.events.events.*;
import com.ankamagames.wakfu.client.alea.*;
import com.ankamagames.baseImpl.graphics.alea.display.worldTransition.*;
import com.ankamagames.baseImpl.graphics.alea.display.*;
import com.ankamagames.baseImpl.client.core.world.*;
import com.ankamagames.framework.kernel.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import org.apache.log4j.*;
import gnu.trove.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.*;
import com.ankamagames.baseImpl.common.clientAndServer.utils.*;

public class NetWorldFrame implements MessageFrame
{
    private static final Logger m_logger;
    private static final NetWorldFrame m_instance;
    private long m_shortcutClockId;
    private long m_healthBarClockId;
    private boolean m_waitForFirstCharacterInfoMessage;
    
    public static NetWorldFrame getInstance() {
        return NetWorldFrame.m_instance;
    }
    
    public long getShortcutClockId() {
        return this.m_shortcutClockId;
    }
    
    public long getHealthBarClockId() {
        return this.m_healthBarClockId;
    }
    
    @Override
    public boolean onMessage(final Message message) {
        final WakfuGameEntity wge = WakfuGameEntity.getInstance();
        final WakfuClientInstance clientInstance = WakfuClientInstance.getInstance();
        switch (message.getId()) {
            case 4098: {
                final CharacterInformationMessage msg = (CharacterInformationMessage)message;
                final byte[] serializedCharacterInfo = msg.getSerializedCharacterInfo();
                ItemUIDsManager.getInstance().removeAll();
                for (int i = 0; i < msg.getReservedIds().length; ++i) {
                    ItemUIDsManager.getInstance().addUId(msg.getReservedIds()[i]);
                }
                WakfuSoundManager.getInstance().setEnableInventoryEventSounds(false);
                PropertiesProvider.getInstance().setPropertyValue("isAlive", true);
                final LocalPlayerCharacter localPlayer = new LocalPlayerCharacter();
                localPlayer.beginRefreshDisplayEquipment();
                localPlayer.fromBuild(serializedCharacterInfo);
                WakfuSoundManager.getInstance().setEnableInventoryEventSounds(true);
                ClientEventsCalendarView.getInstance().setCalendar(CharacterEventsCalendar.getInstance());
                WakfuClientConfigurationManager.getInstance().setCharacterName(localPlayer.getName());
                final WakfuGamePreferences wakfuGamePreferences = clientInstance.getGamePreferences();
                ClientGameEventManager.INSTANCE.setToDefaultActiveValue();
                final PreferenceStore store = new CharacterPreferenceStore(localPlayer.getName());
                wakfuGamePreferences.setCharacterPreferenceStore(store);
                try {
                    store.load();
                }
                catch (IOException e) {
                    NetWorldFrame.m_logger.warn((Object)"Probl\u00e8me au chargement du PreferenceStore", (Throwable)e);
                }
                ClientGameEventPreferencesManager.setActiveValueFromPreferences();
                localPlayer.getOwnedDimensionalBag().getWallet().setListener(localPlayer);
                wge.setLocalPlayer(localPlayer);
                HeroesManager.INSTANCE.addHero(localPlayer.getClientId(), localPlayer);
                PropertiesProvider.getInstance().setPropertyValue("heroesPartyIsFull", false);
                final int hpValue = localPlayer.getCharacteristic((CharacteristicType)FighterCharacteristicType.HP).unboundedValue();
                localPlayer.initialize();
                final TIntObjectIterator<Nation> it = NationManager.INSTANCE.nationsIterator();
                while (it.hasNext()) {
                    it.advance();
                    it.value().registerPoliticEventHandler(new CNationPoliticEventHandler(it.value()));
                    it.value().registerJusticeEventHandler(CNationJusticeEventHandler.INSTANCE);
                }
                PlayerSynchronizator.INSTANCE.notifyPlayerLoaded(localPlayer.getId());
                BarrierManager.INSTANCE.removeBarrier(SynchroBarriers.LOCAL_PLAYER_LOADED);
                localPlayer.setHpValue(hpValue);
                wge.setObservedCharacter(localPlayer);
                localPlayer.getCharacteristicViewProvider().loadFromConfiguration();
                QuestConfigManager.INSTANCE.loadConfig();
                WakfuSoundManager.getInstance().onEnterWorld();
                GazetteManager.INSTANCE.init();
                HeroesDisplayer.INSTANCE.init();
                ShortcutBarDialogActions.m_shortCutsActive = true;
                this.m_shortcutClockId = MessageScheduler.getInstance().addClock(new MessageHandler() {
                    @Override
                    public boolean onMessage(final Message message) {
                        if (!(message instanceof ClockMessage)) {
                            return true;
                        }
                        if (wge.getLocalPlayer() != null) {
                            wge.getLocalPlayer().getShortcutBarManager().sendShortcutsUpdateMessageIfNeeded();
                        }
                        return false;
                    }
                    
                    @Override
                    public long getId() {
                        return 1L;
                    }
                    
                    @Override
                    public void setId(final long id) {
                    }
                }, 300000L, 159159160, -1);
                this.m_healthBarClockId = MessageScheduler.getInstance().addClock(new MessageHandler() {
                    @Override
                    public boolean onMessage(final Message message) {
                        if (wge.getLocalPlayer() != null) {
                            wge.getLocalPlayer().onHealthRegenTick(((ClockMessage)message).getTimeStamp());
                        }
                        return false;
                    }
                    
                    @Override
                    public long getId() {
                        return 1L;
                    }
                    
                    @Override
                    public void setId(final long id) {
                    }
                }, 4000L, 159159161);
                PropertiesProvider.getInstance().setPropertyValue("isInDimensionalBag", false);
                PropertiesProvider.getInstance().setPropertyValue("hasOccupation", false);
                try {
                    if (wakfuGamePreferences.getBooleanValue(WakfuKeyPreferenceStoreEnum.NEED_FIRST_LAUNCH_CHARACTER_UPDATE)) {
                        ChatWindowManager.getInstance().cleanAndDeletePreferences();
                        wakfuGamePreferences.setValue(WakfuKeyPreferenceStoreEnum.NEED_FIRST_LAUNCH_CHARACTER_UPDATE, false);
                    }
                    ChatInitializer.initializeChatFromPreferences();
                }
                catch (Exception e2) {
                    NetWorldFrame.m_logger.error((Object)("Erreur \u00e0 l'initialisation du chat, impossible de le charger : " + e2.getMessage()));
                }
                localPlayer.endRefreshDisplayEquipment();
                HeroesLeaderManager.INSTANCE.putLeader(localPlayer.getClientId(), localPlayer.getId());
                HeroesManager.INSTANCE.addHeroToParty(localPlayer.getClientId(), localPlayer.getId());
                final boolean heroesEnabled = SystemConfiguration.INSTANCE.getBooleanValue(SystemConfigurationType.HEROES_ENABLED);
                PropertiesProvider.getInstance().setPropertyValue("heroesEnabled", heroesEnabled);
                final ShortCharacterView view = new PlayerCompanionViewShort(localPlayer);
                final List<ShortCharacterView> value = new ArrayList<ShortCharacterView>();
                value.add(0, view);
                PropertiesProvider.getInstance().setPropertyValue("mainCharacterViewForTemplate", value);
                return false;
            }
            case 4128: {
                final ActorLeaveInstanceMessage msg2 = (ActorLeaveInstanceMessage)message;
                final Fight observedFight = WakfuGameEntity.getInstance().getLocalPlayer().getObservedFight();
                if (observedFight != null) {
                    FightActionGroupManager.getInstance().executeAllActionInPendingGroup(observedFight.getId());
                    final LeaveSpectatorModeProcedure leaveSpectatorModeProcedure = new LeaveSpectatorModeProcedure();
                    leaveSpectatorModeProcedure.execute();
                }
                this.leaveWorld(wge);
                return false;
            }
            case 4100: {
                final CharacterEnterWorldMessage msg3 = (CharacterEnterWorldMessage)message;
                final int lastWorld = MapManagerHelper.getWorldId();
                Xulor.getInstance().closeAllMRU();
                WakfuClientInstance.getInstance().removeAllLODChangeListener();
                LocalPartitionManager.getInstance().clear();
                if (WakfuGameEntity.getInstance().hasFrame(UIMapFrame.getInstance())) {
                    WakfuGameEntity.getInstance().removeFrame(UIMapFrame.getInstance());
                }
                MapManagerHelper.clear();
                final CharacterInfo staticCharacterInfo = DefaultCharacterInfo.getStaticInstance();
                staticCharacterInfo.getSerializer().extractPartFromBuild(staticCharacterInfo.getSerializer().getIdPart(), msg3.getSerializedCharacterInfo());
                staticCharacterInfo.getSerializer().extractPartFromBuild(staticCharacterInfo.getSerializer().getIdentityPart(), msg3.getSerializedCharacterInfo());
                staticCharacterInfo.getSerializer().extractPartFromBuild(staticCharacterInfo.getSerializer().getPositionPart(), msg3.getSerializedCharacterInfo());
                final short instanceId = staticCharacterInfo.getInstanceId();
                LocalPartitionManager.getInstance().addPartitionListener(wge.getLocalPlayer());
                WakfuClientConfigurationManager.getInstance().setInstanceId(instanceId);
                final WorldInfoManager.WorldInfo worldInfo = WorldInfoManager.getInstance().getInfo(instanceId);
                final boolean isInHavenWorld = HavenWorldManager.INSTANCE.onEnterWorld(instanceId);
                if (!isInHavenWorld) {
                    MapManagerHelper.setWorldId(instanceId, DefaultMapLoader.getInstance());
                }
                PropertiesProvider.getInstance().setPropertyValue("isInHavenWorld", isInHavenWorld);
                WakfuAmbianceListener.getInstance().setWorldId(instanceId);
                final Point3 position = staticCharacterInfo.getPosition();
                StaticProtectorView.INSTANCE.setProtectorId(-1);
                ProtectorManager.INSTANCE.clear();
                final ArrayList<byte[]> serializedProtectors = msg3.getSerializedProtectors();
                for (final byte[] serializedProtector : serializedProtectors) {
                    final Protector protector = ProtectorSerializer.INSTANCE.fromBuild(ByteBuffer.wrap(serializedProtector));
                    ProtectorManager.INSTANCE.registerProtector(protector);
                }
                final ArrayList<NationProtectorInfo> nationProtectorInfos = msg3.getNationProtectorInfos();
                for (int j = nationProtectorInfos.size() - 1; j >= 0; --j) {
                    final NationProtectorInfo info = nationProtectorInfos.get(j);
                    if (info.getNation() != null) {
                        if (info.getNation().getProtectorInfoManager().hasProtector(info.getId())) {
                            NationProtectorInfoManager.updateProtectorChaos(info.getId(), info.isInChaos());
                            NationProtectorInfoManager.updateProtectorSatisfaction(info.getId(), info.getCurrentSatisfaction());
                        }
                        else {
                            NationProtectorInfoManager.registerNewProtector(info);
                        }
                    }
                }
                TerritoriesView.INSTANCE.onInstanceChanged(instanceId);
                if (lastWorld != instanceId) {
                    AreaChallengeInformation.getInstance().setCurrentZoneStatus(ChallengeStatus.NO_CHALLENGES_IN_LIST);
                    AreaChallengeInformation.getInstance().updateStatus();
                }
                SunLightModifier.INSTANCE.resetLight();
                WakfuGameCalendar.getInstance().removeEventListener(SunLightModifier.INSTANCE);
                WakfuGameCalendar.getInstance().removeEventListener(SunShadowManager.getInstance());
                SunShadowManager.getInstance().setShadowEnabled(false);
                final boolean isExterior = worldInfo.m_isExterior;
                if (isExterior) {
                    SunLightModifier.INSTANCE.setSunCycleDuration(86400);
                    SunLightModifier.INSTANCE.forceSetDayTime(WakfuGameCalendar.getInstance());
                    WakfuGameCalendar.getInstance().addEventListener(SunLightModifier.INSTANCE);
                    WakfuGameCalendar.getInstance().addEventListener(SunShadowManager.getInstance());
                    SunShadowManager.getInstance().setShadowEnabled(true);
                }
                WakfuGameCalendar.getInstance().addEventListener(TimeEventListener.INSTANCE);
                PropertiesProvider.getInstance().setPropertyValue("wakfuEcosystemEnabled", false);
                PropertiesProvider.getInstance().setPropertyValue("isInExterior", isExterior);
                PropertiesProvider.getInstance().setPropertyValue("isInWakfuStasisInstance", worldInfo.m_wakfuStasis);
                PropertiesProvider.getInstance().setPropertyValue("player.displayStates", true);
                PropertiesProvider.getInstance().setPropertyValue("osamodasSymbiotCreatureCapturedState", false);
                PropertiesProvider.getInstance().setPropertyValue("havenWorld", null);
                PropertiesProvider.getInstance().setPropertyValue("exchange.itemTrade", null);
                PropertiesProvider.getInstance().setPropertyValue("isInDungeon", worldInfo.isDungeon());
                boolean activateStuffPreview;
                try {
                    activateStuffPreview = WakfuConfiguration.getInstance().getBoolean("activateStuffPreview");
                }
                catch (PropertyException e3) {
                    activateStuffPreview = false;
                }
                PropertiesProvider.getInstance().setPropertyValue("stuffPreviewActivated", activateStuffPreview);
                if (!wge.hasFrame(UIItemManagementFrame.getInstance())) {
                    wge.pushFrame(UIItemManagementFrame.getInstance());
                }
                if (!wge.hasFrame(UITutorialFrame.getInstance())) {
                    wge.pushFrame(UITutorialFrame.getInstance());
                }
                if (!wge.hasFrame(NetActorsFrame.getInstance())) {
                    wge.pushFrame(NetActorsFrame.getInstance());
                }
                if (!wge.hasFrame(NetInteractiveDialogFrame.INSTANCE)) {
                    wge.pushFrame(NetInteractiveDialogFrame.INSTANCE);
                }
                if (!wge.hasFrame(NetNotInFightManagementFrame.getInstance())) {
                    wge.pushFrame(NetNotInFightManagementFrame.getInstance());
                }
                if (!wge.hasFrame(NetOutFightManagementFrame.getInstance())) {
                    wge.pushFrame(NetOutFightManagementFrame.getInstance());
                }
                if (!wge.hasFrame(UIWorldInteractionFrame.getInstance())) {
                    wge.pushFrame(UIWorldInteractionFrame.getInstance());
                }
                if (!wge.hasFrame(UIMRUFrame.getInstance())) {
                    wge.pushFrame(UIMRUFrame.getInstance());
                }
                if (!wge.hasFrame(UIMasterWorldFrame.getInstance())) {
                    wge.pushFrame(UIMasterWorldFrame.getInstance());
                }
                if (!wge.hasFrame(UIShortcutBarFrame.getInstance())) {
                    wge.pushFrame(UIShortcutBarFrame.getInstance());
                }
                if (!wge.hasFrame(UIChallengeFrame.getInstance())) {
                    wge.pushFrame(UIChallengeFrame.getInstance());
                }
                if (!wge.hasFrame(UIChatFrame.getInstance())) {
                    wge.pushFrame(UIChatFrame.getInstance());
                }
                if (!wge.hasFrame(UIOverHeadInfosFrame.getInstance())) {
                    wge.pushFrame(UIOverHeadInfosFrame.getInstance());
                }
                if (!wge.hasFrame(NetExchangeInvitationFrame.getInstance())) {
                    wge.pushFrame(NetExchangeInvitationFrame.getInstance());
                }
                if (!wge.hasFrame(NetOccupationFrame.getInstance())) {
                    wge.pushFrame(NetOccupationFrame.getInstance());
                }
                if (!wge.hasFrame(NetEcosystemFrame.getInstance())) {
                    wge.pushFrame(NetEcosystemFrame.getInstance());
                }
                if (!wge.hasFrame(NetProtectorFrame.INSTANCE)) {
                    wge.pushFrame(NetProtectorFrame.INSTANCE);
                }
                if (!wge.hasFrame(UIGroupFrame.getInstance())) {
                    wge.pushFrame(UIGroupFrame.getInstance());
                }
                if (!wge.hasFrame(NetGroupFrame.getInstance())) {
                    wge.pushFrame(NetGroupFrame.getInstance());
                }
                if (!wge.hasFrame(NetEventsCalendarFrame.getInstance())) {
                    wge.pushFrame(NetEventsCalendarFrame.getInstance());
                }
                if (!wge.hasFrame(NetCompanionFrame.INSTANCE)) {
                    wge.pushFrame(NetCompanionFrame.INSTANCE);
                }
                if (!wge.hasFrame(UIProtectorFrame.getInstance())) {
                    wge.pushFrame(UIProtectorFrame.getInstance());
                }
                if (!wge.hasFrame(UISpellDescriptionFrame.getInstance())) {
                    wge.pushFrame(UISpellDescriptionFrame.getInstance());
                }
                if (!wge.hasFrame(UIStateDetailFrame.getInstance())) {
                    wge.pushFrame(UIStateDetailFrame.getInstance());
                }
                if (!wge.hasFrame(UIEffectAreaDetailFrame.getInstance())) {
                    wge.pushFrame(UIEffectAreaDetailFrame.getInstance());
                }
                if (!wge.hasFrame(UIAchievementsFrame.getInstance())) {
                    wge.pushFrame(UIAchievementsFrame.getInstance());
                }
                if (!wge.hasFrame(UISystemBarFrame.getInstance())) {
                    wge.pushFrame(UISystemBarFrame.getInstance());
                }
                if (!wge.hasFrame(NetCraftFrame.INSTANCE)) {
                    wge.pushFrame(NetCraftFrame.INSTANCE);
                }
                if (!wge.hasFrame(UINotificationPanelFrame.getInstance())) {
                    wge.pushFrame(UINotificationPanelFrame.getInstance());
                }
                if (!wge.hasFrame(UIPetFrame.getInstance())) {
                    wge.pushFrame(UIPetFrame.getInstance());
                }
                if (!wge.hasFrame(NetPetFrame.INSTANCE)) {
                    wge.pushFrame(NetPetFrame.INSTANCE);
                }
                if (!wge.hasFrame(NetItemXpFrame.INSTANCE)) {
                    wge.pushFrame(NetItemXpFrame.INSTANCE);
                }
                if (!wge.hasFrame(NetInventoryFrame.INSTANCE)) {
                    wge.pushFrame(NetInventoryFrame.INSTANCE);
                }
                if (!wge.hasFrame(NetGuildFrame.INSTANCE)) {
                    wge.pushFrame(NetGuildFrame.INSTANCE);
                }
                if (!wge.hasFrame(NetGuildLadderFrame.INSTANCE)) {
                    wge.pushFrame(NetGuildLadderFrame.INSTANCE);
                }
                if (!wge.hasFrame(NetGemsFrame.INSTANCE)) {
                    wge.pushFrame(NetGemsFrame.INSTANCE);
                }
                if (!wge.hasFrame(NetArcadeDungeonFrame.INSTANCE)) {
                    wge.pushFrame(NetArcadeDungeonFrame.INSTANCE);
                }
                if (!wge.hasFrame(UIXPGainFrame.getInstance())) {
                    wge.pushFrame(UIXPGainFrame.getInstance());
                }
                if (!wge.hasFrame(NetPartySearchFrame.INSTANCE)) {
                    wge.pushFrame(NetPartySearchFrame.INSTANCE);
                }
                if (!wge.hasFrame(NetHeroesFrame.INSTANCE)) {
                    wge.pushFrame(NetHeroesFrame.INSTANCE);
                }
                if (!wge.hasFrame(UIControlCenterContainerFrame.getInstance())) {
                    wge.pushFrame(UIControlCenterContainerFrame.getInstance());
                }
                UICompanionsEmbeddedFrame.clearStaticData();
                ShortcutManager.getInstance().setAllShortcutsEnabled(true);
                TaxManager.INSTANCE.addTaxHandler(DefaultTaxHandler.INSTANCE);
                final AleaWorldScene scene = clientInstance.getWorldScene();
                scene.getIsoCamera().setUserZoomLocked(false);
                if (isExterior) {
                    IsoSceneLightManager.INSTANCE.addLightingModifier(SunLightModifier.INSTANCE);
                    IsoSceneLightManager.INSTANCE.addLightingModifier(ResourceShadowCast.INSTANCE);
                    IsoSceneLightManager.INSTANCE.addLightingModifier(ClampLightLitModifier.getInstance());
                }
                else {
                    IsoSceneLightManager.INSTANCE.removeLightingModifier(ClampLightLitModifier.getInstance());
                }
                final LocalPlayerCharacter localPlayer2 = WakfuGameEntity.getInstance().getLocalPlayer();
                final CharacterActor localActor = localPlayer2.getActor();
                localActor.fireAndRemoveRemainingAnimationEndedListener();
                LandMarkNoteManager.getInstance().loadNotes();
                WorldPositionMarkerManager.getInstance().removeAll();
                WakfuAmbianceListener.getInstance().clear();
                SoundPartitionManager.INSTANCE.clear();
                WakfuSoundManager.getInstance().waitForMapLoad();
                MaskableHelper.setUndefined(localActor);
                MapManagerHelper.initializeSceneCamera(scene, position.getX(), position.getY(), position.getZ());
                LocalPartitionManager.getInstance().initialize(staticCharacterInfo.getWorldCellX(), staticCharacterInfo.getWorldCellY(), true);
                WakfuAmbianceListener.getInstance().onCreateScene(LocalPartitionManager.getInstance().getCenterPartitionWorldX(), LocalPartitionManager.getInstance().getCenterPartitionWorldY());
                WakfuGlobalZoneManager.getInstance().setWorldInfo(worldInfo);
                WakfuNationEnemyListener.INSTANCE.initialize();
                if (localPlayer2 != null) {
                    localPlayer2.beginRefreshDisplayEquipment();
                    final CharacterInfo character = CharacterInfoManager.getInstance().getCharacter(localPlayer2.getId());
                    if (character != null && character != localPlayer2) {
                        CharacterInfoManager.getInstance().removeCharacter(character.getId());
                    }
                    CharacterInfoManager.getInstance().addAndSpawnCharacter(localPlayer2);
                    localActor.setVisible(true);
                    localActor.forceReloadAnimation();
                    localActor.removePositionListener(LocalPartitionManager.getInstance());
                    localPlayer2.setPosition(staticCharacterInfo.getPosition());
                    localActor.addPositionListener(LocalPartitionManager.getInstance());
                    localPlayer2.setDirection(staticCharacterInfo.getDirection());
                    localActor.addPositionListener(WeatherEffectsVisibilityManager.INSTANCE);
                    localActor.setEmitter(true);
                    localActor.onPositionChanged(localPlayer2);
                    localPlayer2.setInstanceId(instanceId);
                    localActor.reloadHmiActions();
                    MobileManager.getInstance().addMobile(localActor);
                    scene.setCameraTarget(localActor);
                    scene.alignCameraOnTrackingTarget();
                    MapManager.getInstance().initProperty();
                    PaperMapManager.getInstance().load(instanceId);
                    WakfuSoundManager.getInstance().stopWaitingForMapLoad();
                    final int hp = localPlayer2.getCharacteristic((CharacteristicType)FighterCharacteristicType.HP).unboundedValue();
                    localPlayer2.reloadBuffs(localPlayer2.getEffectContext());
                    localPlayer2.getCharacteristic((CharacteristicType)FighterCharacteristicType.HP).set(hp);
                    if (instanceId == 9) {
                        final DimensionalBagView dimensionalBag = localPlayer2.getVisitingDimentionalBag();
                        DisplayedScreenWorld.getInstance().addReloadListener(dimensionalBag);
                        DisplayedScreenWorld.getInstance().addMapLoadListener(dimensionalBag);
                        dimensionalBag.initializeToEnter();
                        worldInfo.setParallax(dimensionalBag.getBackgroundMapId());
                        AmbianceDimensionalBag.getInstance().enterInBag(dimensionalBag);
                    }
                    AmbianceDimensionalBag.getInstance().applyAmbiance(localActor);
                    scene.setForceUpdateDisplayCell(true);
                    final FadeListener onMapLoaded = new FadeListener() {
                        @Override
                        public void onFadeInEnd() {
                            scene.getIsoCamera().resetMaskKey();
                            GroupLayerManager.getInstance().clear();
                            GroupLayerManager.getInstance().process(scene, 300);
                        }
                        
                        @Override
                        public void onFadeOutEnd() {
                            FadeManager.getInstance().removeListener(this);
                            localPlayer2.setWaitingForResult(false);
                        }
                        
                        @Override
                        public void onFadeInStart() {
                        }
                        
                        @Override
                        public void onFadeOutStart() {
                            scene.setBackgoundColor(worldInfo.m_backGroundColor);
                            NetWorldFrame.setParallax(clientInstance.getBackgroundWorldScene(), worldInfo);
                            try {
                                localActor.onCellChanged(position.getX(), position.getY(), position.getZ());
                            }
                            catch (Exception e) {
                                NetWorldFrame.m_logger.error((Object)"Exception levee", (Throwable)e);
                            }
                        }
                    };
                    if (FadeManager.getInstance().isFadeIn()) {
                        FadeManager.getInstance().addListener(onMapLoaded);
                    }
                    else {
                        onMapLoaded.onFadeInEnd();
                        onMapLoaded.onFadeOutStart();
                        onMapLoaded.onFadeOutEnd();
                    }
                }
                else {
                    NetWorldFrame.m_logger.error((Object)"Il n'y a aucun localPlayer de d\u00e9fini, il n'est pas possible d'entrer dans le monde !");
                }
                scene.setInitialized(true);
                final Item petHolder = ((ArrayInventoryWithoutCheck<Item, R>)localPlayer2.getEquipmentInventory()).getFromPosition(EquipmentPosition.PET.m_id);
                if (petHolder != null && petHolder.hasPet()) {
                    final Pet pet = petHolder.getPet();
                    localPlayer2.createPetMobileView(pet.getDefinition().getId(), pet.getColorItemRefId(), pet.getEquippedRefItemId(), pet.getSleepRefItemId(), pet.getHealth());
                }
                final Item mountHolder = ((ArrayInventoryWithoutCheck<Item, R>)localPlayer2.getEquipmentInventory()).getFromPosition(EquipmentPosition.MOUNT.m_id);
                if (mountHolder != null && mountHolder.hasPet()) {
                    final Pet mount = mountHolder.getPet();
                    localPlayer2.createMountMobileView(mount.getDefinition().getId(), mount.getColorItemRefId(), mount.getEquippedRefItemId(), mount.getSleepRefItemId(), mount.getHealth());
                }
                localPlayer2.setCanMoveAndInteract(true);
                localPlayer2.removeAdditionalAppearance();
                localPlayer2.updateAdditionalAppearance();
                EmbeddedTutorialManager.getInstance().setEnabled(true);
                WakfuProgressMonitorManager.getInstance().done();
                Xulor.getInstance().putActionClass("wakfu.windowStick", WindowStickActions.class);
                AchievementsViewManager.INSTANCE.onPlayerEnterWorld();
                ClientGameEventManager.INSTANCE.fireEvent(new ClientEventEnterInstance(lastWorld, instanceId));
                final GiftInventoryRequestMessage netMessage = new GiftInventoryRequestMessage();
                netMessage.setLocale(WakfuTranslator.getInstance().getLanguage().getActualLocale());
                WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(netMessage);
                localPlayer2.endRefreshDisplayEquipment();
                if (this.m_waitForFirstCharacterInfoMessage) {
                    this.m_waitForFirstCharacterInfoMessage = false;
                    this.askForPartyInfo();
                }
                return false;
            }
            case 4106: {
                final String userMsg = WakfuTranslator.getInstance().getString("player.unstuck");
                this.pushChatInfoMessage(userMsg);
                return false;
            }
            case 10004: {
                final PSEnterResultMessage msg4 = (PSEnterResultMessage)message;
                NetWorldFrame.m_logger.debug((Object)("PERSONAL_SPACE_ENTER_RESULT_MESSAGE (success=" + msg4.isSuccessful() + ")"));
                final LocalPlayerCharacter localPlayer3 = wge.getLocalPlayer();
                if (msg4.isSuccessful()) {
                    DimensionalBagView dimensionalBag2;
                    if (msg4.isYourDimBag()) {
                        dimensionalBag2 = localPlayer3.getOwnedDimensionalBag();
                        dimensionalBag2.setFleaAllowed(msg4.isFleaAllowed());
                        dimensionalBag2.setOnMarket(msg4.isOnMarket());
                        dimensionalBag2.setPartitionNationId(msg4.getPartitionNationId());
                    }
                    else {
                        dimensionalBag2 = new DimensionalBagView();
                        dimensionalBag2.fromRaw(msg4.getSerializedPersonalSpace());
                    }
                    DimensionalBagFromInstanceManager.INSTANCE.setFromInstanceId(localPlayer3.getInstanceId());
                    PropertiesProvider.getInstance().setPropertyValue("isInDimensionalBag", true);
                    localPlayer3.setVisitingDimentionalBag(dimensionalBag2);
                    wge.pushFrame(NetDimensionalBagFrame.getInstance());
                    wge.removeFrame(UIProtectorViewFrame.getInstance());
                    if (msg4.isYourDimBag()) {
                        EmbeddedTutorialManager.getInstance().launchTutorial(TutorialEvent.DIMENSIONNAL_ENTER, null);
                    }
                }
                else {
                    localPlayer3.getActor().setAnimation("AnimStatique");
                    if (msg4.isYourDimBag()) {
                        LocalPartitionManager.getInstance().removeInteractiveElement(0L);
                    }
                    wge.getLocalPlayer().setWaitingForResult(false);
                }
                return false;
            }
            case 10006: {
                NetWorldFrame.m_logger.debug((Object)"PERSONAL_SPACE_LEAVE_RESULT_MESSAGE");
                final LocalPlayerCharacter localPlayer4 = wge.getLocalPlayer();
                DisplayedScreenWorld.getInstance().removeReloadListener(localPlayer4.getVisitingDimentionalBag());
                DisplayedScreenWorld.getInstance().removeMapLoadListener(localPlayer4.getVisitingDimentionalBag());
                wge.removeFrame(UIDimensionalBagRoomManagerFrame.getInstance());
                wge.removeFrame(UIDimensionalBagInteractionFrame.getInstance());
                wge.removeFrame(UIManageFleaFrame.getInstance());
                wge.removeFrame(UIBrowseFleaFrame.getInstance());
                wge.removeFrame(UIDimensionalBagRoomAdministrationFrame.getInstance());
                if (Xulor.getInstance().isLoaded("exchangeDialog")) {
                    wge.removeFrame(UIExchangeFrame.getInstance());
                }
                if (wge.hasFrame(UIExchangeInvitationFrame.getInstance())) {
                    wge.removeFrame(UIExchangeInvitationFrame.getInstance());
                }
                UIMRUFrame.getInstance().closeCurrentMRU();
                if (Xulor.getInstance().isLoaded("dimensionalBagFleaHistoryDialog")) {
                    Xulor.getInstance().unload("dimensionalBagFleaHistoryDialog");
                }
                AmbianceDimensionalBag.getInstance().goOut();
                PropertiesProvider.getInstance().setPropertyValue("isInDimensionalBag", false);
                wge.removeFrame(NetDimensionalBagFrame.getInstance());
                return false;
            }
            case 6322: {
                final RunningEffectWorldApplicationMessage msg5 = (RunningEffectWorldApplicationMessage)message;
                final int runningEffectId = msg5.getRunningEffectId();
                final WakfuRunningEffect runningEffect = RunningEffectConstants.getInstance().getObjectFromId(runningEffectId);
                if (runningEffect != null) {
                    final RunningEffect<WakfuEffect, WakfuEffectContainer> re = runningEffect.newInstance();
                    re.setContext(WakfuGameEntity.getInstance().getLocalPlayer().getInstanceEffectContext());
                    re.fromBuild(msg5.getSerializedRunningEffect());
                    final CharacterInfo target = CharacterInfoManager.getInstance().getCharacter(msg5.getTargetId());
                    target.getActor().applyOnApplicationHMIAction((WakfuRunningEffect)re, false);
                    target.getRunningEffectManager().storeEffect(re);
                }
                return false;
            }
            case 6324: {
                final RunningEffectWorldUnapplicationMessage msg6 = (RunningEffectWorldUnapplicationMessage)message;
                final long targetId = msg6.getTargetId();
                final long runningEffectUid = msg6.getRunningEffectUid();
                final CharacterInfo characterInfo = CharacterInfoManager.getInstance().getCharacter(targetId);
                if (characterInfo != null) {
                    final RunningEffect runningEffect2 = characterInfo.getRunningEffectManager().getRunningEffectFromUID(runningEffectUid);
                    if (runningEffect2 != null) {
                        characterInfo.onEffectUnApplication(runningEffect2);
                    }
                    else {
                        NetWorldFrame.m_logger.error((Object)("R\u00e9ception d'un RunningEffectWorldUnapplicationMessage pour un runningEffect d'uid=" + runningEffectUid + " que le characterInfo=[" + characterInfo + "] ne poss\u00e8de pas"));
                    }
                }
                else {
                    NetWorldFrame.m_logger.error((Object)("R\u00e9ception d'un RunningEffectWorldUnapplicationMessage pour un characterinfo inconnu d'id=" + targetId));
                }
                return false;
            }
            case 6320: {
                final RunningEffectWorldActionMessage msg7 = (RunningEffectWorldActionMessage)message;
                final int actionTypeId = msg7.getWorldActionType().getId();
                final StaticRunningEffect<WakfuEffect, WakfuEffectContainer> staticEffect = ((Constants<StaticRunningEffect<WakfuEffect, WakfuEffectContainer>>)RunningEffectConstants.getInstance()).getObjectFromId(msg7.getRunningEffectId());
                if (staticEffect == null) {
                    NetWorldFrame.m_logger.error((Object)("Impossible d'instancier un runningEffect :" + msg7.getRunningEffectId() + " inconnu"));
                    return false;
                }
                if (msg7.getSerializedTargetsWithCorrespondingAction() != null) {
                    for (final ObjectTriplet<Integer, Integer, byte[]> serializedTargetWithCorrespondingAction : msg7.getSerializedTargetsWithCorrespondingAction()) {
                        final byte[] serializedTarget = serializedTargetWithCorrespondingAction.getThird();
                        final long targetId2 = ByteBuffer.wrap(serializedTarget).getLong();
                        final CharacterInfo character2 = CharacterInfoManager.getInstance().getCharacter(targetId2);
                        final SpellEffectWorldAction spellEffectAction = new SpellEffectWorldAction(msg7.getUniqueId(), actionTypeId, serializedTargetWithCorrespondingAction.getFirst(), staticEffect, msg7.getSerializedRunningEffect(), msg7.isTriggered(), serializedTarget);
                        spellEffectAction.setTriggerActionUniqueId(serializedTargetWithCorrespondingAction.getSecond());
                        spellEffectAction.addJavaFunctionsLibrary(new ScriptedActionFunctionsLibrary(spellEffectAction.getGroup()));
                        if (character2 == null || !character2.isOnFight()) {
                            WorldActionGroupManager.getInstance().addAction(spellEffectAction);
                        }
                        else {
                            FightActionGroupManager.getInstance().addActionToPendingGroup(character2.getCurrentFightId(), spellEffectAction);
                        }
                    }
                }
                else {
                    final SpellEffectWorldAction spellEffectAction2 = new SpellEffectWorldAction(msg7.getUniqueId(), actionTypeId, msg7.getActionId(), staticEffect, msg7.getSerializedRunningEffect(), msg7.isTriggered(), null);
                    spellEffectAction2.setTriggerActionUniqueId(msg7.getTriggeringActionUniqueId());
                    WorldActionGroupManager.getInstance().addAction(spellEffectAction2);
                    spellEffectAction2.addJavaFunctionsLibrary(new ScriptedActionFunctionsLibrary(spellEffectAction2.getGroup()));
                }
                WorldActionGroupManager.getInstance().executePendingGroup();
                return false;
            }
            case 6300: {
                WorldActionGroupManager.getInstance().executePendingGroup();
                return false;
            }
            case 5242: {
                final OfflineFleaTransactionSummaryMessage msg8 = (OfflineFleaTransactionSummaryMessage)message;
                final String userMsg2 = WakfuTranslator.getInstance().getString("offlineFlea.transactionSummary", msg8.getNumberOfTransactions(), msg8.getKamasEarned());
                this.pushChatInfoMessage(userMsg2);
                return false;
            }
            case 5244: {
                final ItemSoldNotificationMessage msg9 = (ItemSoldNotificationMessage)message;
                final AbstractReferenceItem referenceItem = ReferenceItemManager.getInstance().getReferenceItem(msg9.getItemRefId());
                final String itemName = referenceItem.getName();
                final String userMsg3 = WakfuTranslator.getInstance().getString("onlineFlea.transaction", msg9.getQuantity(), itemName, msg9.getTotalKamas());
                this.pushChatInfoMessage(userMsg3);
                return false;
            }
            case 2061: {
                final WhoisResponseMessage msg10 = (WhoisResponseMessage)message;
                String userMsg2 = null;
                switch (msg10.getStatusCode()) {
                    case 0: {
                        userMsg2 = msg10.getMessage();
                        break;
                    }
                    case 1: {
                        userMsg2 = WakfuTranslator.getInstance().getString("error.chat.userNotFound", "");
                        break;
                    }
                    default: {
                        return false;
                    }
                }
                this.pushChatInfoMessage(userMsg2);
                return false;
            }
            case 4214: {
                final PlayerXpModificationMessage xpModificationMessage = (PlayerXpModificationMessage)message;
                if (WakfuGameEntity.getInstance().hasFrame(NetInFightManagementFrame.getInstance()) && xpModificationMessage.isFightXp()) {
                    return true;
                }
                final PlayerXpModificationAction playerXpModificationAction = PlayerXpModificationAction.buildFromMessage(xpModificationMessage);
                WorldActionGroupManager.getInstance().addAction(playerXpModificationAction);
                final SkillOrSpellXpModificationAction[] arr$;
                final SkillOrSpellXpModificationAction[] skillOrSpellXpModificationActions = arr$ = SkillOrSpellXpModificationAction.buildFromMessage(xpModificationMessage);
                for (final SkillOrSpellXpModificationAction skillOrSpellXpModificationAction : arr$) {
                    WorldActionGroupManager.getInstance().addAction(skillOrSpellXpModificationAction);
                }
                WorldActionGroupManager.getInstance().executePendingGroup();
                return false;
            }
            case 15001: {
                final TimeResultMessage msg11 = (TimeResultMessage)message;
                final GameDate d = msg11.getDate();
                final int days = d.getDay();
                final int months = d.getMonth();
                final int rpYear = d.getYear() - 1042;
                final String years = String.valueOf(d.getYear());
                final int hours = d.getHours();
                final int minutes = d.getMinutes();
                final boolean isDay = msg11.isDay();
                final float percentage = msg11.getPhasePercentage();
                final StringBuffer str = new StringBuffer();
                if (percentage < 10.0f) {
                    str.append(isDay ? WakfuTranslator.getInstance().getString("calendar.day.starting") : WakfuTranslator.getInstance().getString("calendar.night.starting"));
                }
                else if (percentage > 90.0f) {
                    str.append(isDay ? WakfuTranslator.getInstance().getString("calendar.day.ending") : WakfuTranslator.getInstance().getString("calendar.night.ending"));
                }
                else {
                    str.append(isDay ? WakfuTranslator.getInstance().getString("calendar.day.during") : WakfuTranslator.getInstance().getString("calendar.night.during"));
                }
                str.append(", ");
                str.append(WakfuTranslator.getInstance().getString("calendar.timeNotice", days, WakfuTranslator.getInstance().getString("calendar.month." + months), (months > 2 || (months == 2 && days == 29)) ? rpYear : (rpYear - 1), (days < 10) ? ("0" + days) : days, (months < 10) ? ("0" + months) : months, StringUtils.substring(years, 2, years.length()), (hours < 10) ? ("0" + hours) : hours, (minutes < 10) ? ("0" + minutes) : minutes));
                this.pushChatInfoMessage(str.toString());
                return false;
            }
            case 9301: {
                final TerritoryChaosMessage msg12 = (TerritoryChaosMessage)message;
                final AbstractTerritory territory = TerritoryManager.INSTANCE.getTerritory(msg12.getTerritoryId());
                if (territory == null) {
                    NetWorldFrame.m_logger.warn((Object)"On re\u00e7oit un update de chaos de territoire pour un territoire non charg\u00e9.");
                    return false;
                }
                if (msg12.isInChaos()) {
                    territory.getChaosHandler().startChaos();
                }
                else {
                    territory.getChaosHandler().endChaos();
                }
                return false;
            }
            case 9305: {
                final InteractiveElementChaosMessage msg13 = (InteractiveElementChaosMessage)message;
                String ieTypeString = "";
                final TShortIntIterator it2 = msg13.getIeTypes().iterator();
                while (it2.hasNext()) {
                    it2.advance();
                    final int ieTypeId = it2.key();
                    ieTypeString += WakfuTranslator.getInstance().getString(103, ieTypeId, new Object[0]);
                    ieTypeString = ieTypeString + WakfuTranslator.getInstance().getString("colon") + it2.value();
                    if (it2.hasNext()) {
                        ieTypeString += ", ";
                    }
                    else {
                        ieTypeString += ".";
                    }
                }
                final UINotificationMessage uiNotificationMessage = new UINotificationMessage(WakfuTranslator.getInstance().getString("notification.IEDestroyedTitle"), WakfuTranslator.getInstance().getString("notification.IEDestroyedText", ieTypeString), NotificationMessageType.PROTECTOR_CHALLENGE);
                Worker.getInstance().pushMessage(uiNotificationMessage);
                return false;
            }
            case 20002: {
                final ClientCharacterUpdateMessage msg14 = (ClientCharacterUpdateMessage)message;
                final long characterId = msg14.getCharacterId();
                final byte[] data = msg14.getSerializedParts();
                final LocalPlayerCharacter player = WakfuGameEntity.getInstance().getLocalPlayer();
                if (player == null) {
                    PlayerSynchronizator.INSTANCE.addSynchroHandler(characterId, new PlayerSynchroHandler() {
                        @Override
                        public void onPlayerLoaded() {
                            final LocalPlayerCharacter player = WakfuGameEntity.getInstance().getLocalPlayer();
                            player.fromBuild(data);
                        }
                    });
                }
                else {
                    final int previousNationId = player.getNationId();
                    player.fromBuild(data);
                    final int newNationId = player.getNationId();
                    if (newNationId != previousNationId) {
                        ClientGameEventManager.INSTANCE.fireEvent(new ClientEventNationChanged(previousNationId, newNationId));
                    }
                }
                return false;
            }
            case 15405: {
                final MonsterSpeakMessage msg15 = (MonsterSpeakMessage)message;
                final long monsterId = msg15.getMonsterId();
                final CharacterInfo fighter = CharacterInfoManager.getInstance().getCharacter(monsterId);
                final String name = fighter.getName();
                final String sentence = WakfuTranslator.getInstance().getString(msg15.getSentenceKey());
                final ChatMessage chatMessage = new ChatMessage(name, monsterId, sentence);
                chatMessage.setPipeDestination(1);
                ChatManager.getInstance().pushMessage(chatMessage);
                return false;
            }
            case 15802: {
                final TravelLoadingMessage msg16 = (TravelLoadingMessage)message;
                final TravelType travelType = msg16.getTravelType();
                final long machineId = msg16.getMachineId();
                final long linkId = msg16.getLinkId();
                TravelLoadingInfo loadingInfo = null;
                switch (travelType) {
                    case BOAT: {
                        final BoatLink link = TravelInfoManager.INSTANCE.getBoatLink(linkId);
                        loadingInfo = link.getLoading();
                        break;
                    }
                    case CANNON: {
                        final CannonInfo info2 = TravelInfoManager.INSTANCE.getInfo(travelType, machineId);
                        final CannonLink link2 = info2.getDrop((int)linkId);
                        loadingInfo = link2.getLoading();
                        break;
                    }
                    case ZAAP: {
                        final ZaapInfo info3 = TravelInfoManager.INSTANCE.getInfo(travelType, machineId);
                        loadingInfo = info3.getLoading();
                        break;
                    }
                    case DRAGO: {
                        final DragoInfo info4 = TravelInfoManager.INSTANCE.getInfo(travelType, machineId);
                        loadingInfo = info4.getLoading();
                        break;
                    }
                    default: {
                        NetWorldFrame.m_logger.error((Object)("Impossible de r\u00e9cup\u00e9rer le LOADING correspondant au travel " + travelType));
                        return false;
                    }
                }
                setLoadingTransition(loadingInfo);
                return false;
            }
            case 15804: {
                final TeleporterLoadingMessage msg17 = (TeleporterLoadingMessage)message;
                final int parameterId = msg17.getParameterId();
                final int linkId2 = msg17.getLinkId();
                final IETeleporterParameter param = (IETeleporterParameter)IEParametersManager.INSTANCE.getParam(IETypes.TELEPORTER, parameterId);
                final IETeleporterParameter.Exit exit = param.getExit(linkId2);
                final TravelLoadingInfo loadingInfo2 = exit.getLoading();
                setLoadingTransition(loadingInfo2);
                return false;
            }
            case 15962: {
                final LandMarkLearnedMessage msg18 = (LandMarkLearnedMessage)message;
                HeroesManager.INSTANCE.getHero(msg18.getCharacterId()).getMapHandler().learnLandMark(msg18.getLandMarkId());
                return false;
            }
            case 15776: {
                final RecustomPrepareMessage msg19 = (RecustomPrepareMessage)message;
                final short recustomType = msg19.getRecustomType();
                if (recustomType != 127) {
                    WakfuProgressMonitorManager.getInstance().getProgressMonitor(true).beginTask(WakfuTranslator.getInstance().getString("recustom.enterTheDressingRoom"), 0);
                }
                else {
                    WakfuProgressMonitorManager.getInstance().getProgressMonitor(true).beginTask(WakfuTranslator.getInstance().getString("charactersLoading.progress"), 0);
                }
                return false;
            }
            case 2065: {
                final RecustomMessage msg20 = (RecustomMessage)message;
                final LocalPlayerCharacter localPlayer5 = WakfuGameEntity.getInstance().getLocalPlayer();
                final PlayerCharacter copy = localPlayer5.getGraphicalCopy();
                WakfuClientInstance.getInstance().partialCleanUp();
                WakfuGameEntity.getInstance().pushFrame(NetSystemNotificationsAndPingFrame.getInstance());
                WakfuGameEntity.getInstance().pushFrame(UISystemBarFrame.getInstance());
                WakfuGameEntity.getInstance().pushFrame(NetChatFrame.getInstance());
                NetCharacterCreationFrame.getInstance().setCharacterId(msg20.getCharacterId());
                NetCharacterCreationFrame.getInstance().setCharacterName(msg20.getCharacterName());
                NetCharacterCreationFrame.getInstance().setCreationType(NetCharacterCreationFrame.CreationType.RECUSTOM);
                NetCharacterCreationFrame.getInstance().setRecustomType(msg20.getRecustomType());
                NetCharacterCreationFrame.getInstance().setSource(msg20.getSource());
                NetCharacterCreationFrame.getInstance().setModel(copy);
                WakfuGameEntity.getInstance().pushFrame(NetCharacterCreationFrame.getInstance());
                return false;
            }
            case 15706: {
                final PopupModerationMessage msg21 = (PopupModerationMessage)message;
                final String receivedMessage = msg21.getMessage();
                final MessageBoxControler controler = Xulor.getInstance().msgBox(receivedMessage, WakfuMessageBoxConstants.getMessageBoxIconUrl(1), 17411L, 102, 2);
                controler.addEventListener(new MessageBoxEventListener() {
                    @Override
                    public void messageBoxClosed(final int type, final String userEntry) {
                        final UIMessage privateChat = new UIMessage();
                        privateChat.setId(19061);
                        privateChat.setStringValue(msg21.getModerator());
                        Worker.getInstance().pushMessage(privateChat);
                    }
                });
                final ChatMessage chatMessage2 = new ChatMessage(receivedMessage);
                chatMessage2.setPipeDestination(3);
                ChatManager.getInstance().pushMessage(chatMessage2);
                break;
            }
        }
        return true;
    }
    
    private void askForPartyInfo() {
        final PartyInfoRequestMessage partyInfoRequestMessage = new PartyInfoRequestMessage();
        WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(partyInfoRequestMessage);
    }
    
    private void leaveWorld(final WakfuGameEntity wge) {
        final LocalPlayerCharacter localPlayer = wge.getLocalPlayer();
        final short instanceId = localPlayer.getInstanceId();
        ClientGameEventManager.INSTANCE.fireEvent(new ClientEventLeaveInstance(instanceId));
        final int lastWorld = MapManagerHelper.getWorldId();
        NetWorldFrame.m_logger.info((Object)("on quitte le monde " + lastWorld));
        WakfuSceneFader.getInstance().sceneLoadingTransition(lastWorld);
        localPlayer.removePetMobile();
        localPlayer.removeMountMobile();
        final CharacterActor localActor = (localPlayer == null) ? null : localPlayer.getActor();
        if (localActor != null) {
            MobileManager.getInstance().detachMobile(localActor);
        }
        LocalPartitionManager.getInstance().clear();
        UISitOccupationFrame.getInstance().clear();
        WakfuClientInstance.getInstance().cleanOnExitWorld();
    }
    
    private void pushChatInfoMessage(final String userMsg) {
        final ChatMessage chatMsg = new ChatMessage(userMsg);
        chatMsg.setPipeDestination(4);
        ChatManager.getInstance().getChatPipe(4).pushMessage(chatMsg);
    }
    
    private static void setLoadingTransition(final TravelLoadingInfo loadingInfo) {
        final LoadingTransitionManager loading = LoadingTransitionManager.getInstance();
        loading.setAnmName(loadingInfo.getAnimationName());
        loading.setMinTransitionDuration(loadingInfo.getMinDuration());
        loading.setFadeInDuration(loadingInfo.getFadeInDuration());
        loading.setFadeOutDuration(loadingInfo.getFadeOutDuration());
    }
    
    public static void setParallax(final ParallaxWorldScene[] backgroundScene, final WorldInfoManager.WorldInfo worldInfo) {
        final int count = (worldInfo == null) ? 0 : worldInfo.getParallaxCount();
        final AleaWorldSceneWithParallax scene = WakfuClientInstance.getInstance().getWorldScene();
        scene.cleanParallaxes();
        if (count == 0) {
            return;
        }
        if (count > backgroundScene.length) {
            NetWorldFrame.m_logger.warn((Object)"trop de parallax");
        }
        for (int i = 0; i < count; ++i) {
            final ParallaxInfo parallaxInfo = worldInfo.getParallaxInfo(i);
            backgroundScene[i].setParallax(parallaxInfo);
            scene.addParallax(backgroundScene[i]);
        }
    }
    
    @Override
    public long getId() {
        return 1L;
    }
    
    @Override
    public void setId(final long id) {
    }
    
    @Override
    public void onFrameAdd(final FrameHandler frameHandler, final boolean isAboutToBeAdded) {
        WakfuGameEntity.getInstance().pushFrame(NetItemsFrame.getInstance());
        WakfuGameEntity.getInstance().pushFrame(NetScriptedEventsFrame.getInstance());
        WakfuGameEntity.getInstance().pushFrame(NetGiftFrame.INSTANCE);
        WakfuGameEntity.getInstance().pushFrame(NetDungeonFrame.INSTANCE);
        WakfuGameEntity.getInstance().pushFrame(UIWorldFrame.getInstance());
    }
    
    @Override
    public void onFrameRemove(final FrameHandler frameHandler, final boolean isAboutToBeRemoved) {
        PropertiesProvider.getInstance().removeProperty("player.displayStates");
        PropertiesProvider.getInstance().removeProperty("isInExterior");
        PropertiesProvider.getInstance().removeProperty("isInWakfuStasisInstance");
        WakfuGameEntity.getInstance().removeFrame(NetItemsFrame.getInstance());
        WakfuGameEntity.getInstance().removeFrame(NetScriptedEventsFrame.getInstance());
        WakfuGameEntity.getInstance().removeFrame(UIWorldFrame.getInstance());
    }
    
    public void setWaitForFirstCharacterInfoMessage(final boolean waitForFirstCharacterInfoMessage) {
        this.m_waitForFirstCharacterInfoMessage = waitForFirstCharacterInfoMessage;
    }
    
    static {
        m_logger = Logger.getLogger((Class)NetWorldFrame.class);
        m_instance = new NetWorldFrame();
        NetWorldFrame.m_logger.setLevel(Level.TRACE);
    }
}
