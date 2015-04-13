package com.ankamagames.wakfu.client.core;

import com.ankamagames.wakfu.client.core.account.*;
import java.nio.*;
import com.ankamagames.wakfu.client.network.security.*;
import com.ankamagames.framework.kernel.events.*;
import com.ankamagames.wakfu.client.core.world.*;
import com.ankamagames.framework.kernel.utils.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.wakfu.client.core.game.item.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.proxy.*;
import com.ankamagames.wakfu.client.core.preferences.*;
import com.ankamagames.baseImpl.graphics.core.*;
import com.ankamagames.wakfu.client.core.game.fight.*;
import com.ankamagames.wakfu.client.core.game.ressource.*;
import com.ankamagames.wakfu.client.core.game.gift.*;
import com.ankamagames.wakfu.client.core.game.miniMap.*;
import com.ankamagames.framework.ai.pathfinder.deadreckoning.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.guild.*;
import com.ankamagames.wakfu.client.core.game.eventsCalendar.*;
import com.ankamagames.wakfu.client.core.game.companion.*;
import com.ankamagames.baseImpl.graphics.alea.mobile.*;
import com.ankamagames.wakfu.client.core.game.IsoWorldTarget.*;
import com.ankamagames.wakfu.client.chat.*;
import com.ankamagames.wakfu.client.network.protocol.frame.*;
import com.ankamagames.framework.kernel.core.common.message.scheduler.clock.*;
import com.ankamagames.wakfu.client.core.game.challenge.*;
import com.ankamagames.wakfu.client.sound.*;
import com.ankamagames.wakfu.common.game.time.calendar.*;
import com.ankamagames.wakfu.client.sound.ambiance2D.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.*;
import com.ankamagames.baseImpl.graphics.alea.display.lights.*;
import com.ankamagames.wakfu.client.alea.graphics.*;
import com.ankamagames.wakfu.client.core.action.*;
import com.ankamagames.wakfu.client.alea.graphics.fightView.*;
import com.ankamagames.wakfu.common.game.nation.*;
import com.ankamagames.wakfu.client.core.nation.*;
import com.ankamagames.wakfu.common.game.nation.event.*;
import com.ankamagames.wakfu.client.core.game.craft.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.common.game.hero.*;
import com.ankamagames.wakfu.client.core.game.exchange.*;
import com.ankamagames.wakfu.client.core.game.achievements.ui.*;
import com.ankamagames.wakfu.client.core.game.almanach.*;
import com.ankamagames.wakfu.client.core.krosmoz.collection.*;
import com.ankamagames.wakfu.client.core.havenWorld.view.*;
import com.ankamagames.wakfu.client.core.game.achievements.*;
import com.ankamagames.wakfu.client.core.game.soap.auth.*;
import com.ankamagames.wakfu.client.core.game.pvp.*;
import com.ankamagames.wakfu.client.*;
import com.ankamagames.wakfu.client.core.game.synchronizing.*;
import com.ankamagames.framework.kernel.core.common.message.synchronizing.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.occupation.*;
import com.ankamagames.wakfu.client.core.companion.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.wakfu.common.constants.*;
import com.ankamagames.wakfu.client.ui.*;
import com.ankamagames.wakfu.client.ui.progress.*;
import com.ankamagames.xulor2.core.netEnabled.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.network.protocol.message.connection.clientToServer.*;
import com.ankamagames.xulor2.core.messagebox.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.baseImpl.graphics.alea.animatedElement.*;
import com.ankamagames.wakfu.common.datas.Breed.*;
import com.ankamagames.framework.kernel.core.maths.*;
import org.jetbrains.annotations.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.google.common.base.*;
import io.netty.channel.*;
import com.ankamagames.wakfu.client.core.game.actor.*;
import gnu.trove.*;

public class WakfuGameEntity extends GameEntity
{
    private static final WakfuGameEntity m_instance;
    private LocalAccountInformations m_localAccount;
    private LocalPlayerCharacter m_localPlayer;
    private LoginPhase m_currentLoginPhase;
    private MessageBoxControler m_messageBoxControler;
    
    public static WakfuGameEntity getInstance() {
        return WakfuGameEntity.m_instance;
    }
    
    public static byte[] getEncryptedLoginAndPassword(final long salt) {
        final byte[] login = StringUtils.toUTF8(getInstance().getLogin());
        final byte loginLength = (byte)login.length;
        final byte[] password = StringUtils.toUTF8(getInstance().getPassword());
        final byte passwordLength = (byte)password.length;
        final ByteBuffer bb = ByteBuffer.allocate(9 + loginLength + 1 + passwordLength);
        bb.putLong(salt);
        bb.put(loginLength);
        bb.put(login);
        bb.put(passwordLength);
        bb.put(password);
        final byte[] rawData = bb.array();
        return ConnectionEncryptionManager.INSTANCE.crypt(rawData);
    }
    
    public LocalPlayerCharacter getLocalPlayer() {
        if (this.hasFrame(UIFightTurnFrame.getInstance())) {
            final CharacterInfo concernedFighter = UIFightTurnFrame.getInstance().getConcernedFighter();
            if (concernedFighter != null && concernedFighter.isLocalPlayer()) {
                return (LocalPlayerCharacter)concernedFighter;
            }
        }
        return this.m_localPlayer;
    }
    
    public void setLocalPlayer(final LocalPlayerCharacter localPlayer) {
        if (this.m_localPlayer != null) {
            this.m_localPlayer.finishCurrentOccupation();
            this.m_localPlayer.getActor().removePositionListener(LocalPartitionManager.getInstance());
            this.m_localPlayer.removeCharacterImageGenerator();
        }
        this.m_localPlayer = localPlayer;
        StringFormatter.setGender((this.m_localPlayer == null) ? StringFormatter.DEFAULT_GENDER : this.m_localPlayer.getSex());
        StringFormatter.setName((this.m_localPlayer == null) ? StringFormatter.DEFAULT_NAME : this.m_localPlayer.getName());
        StringFormatter.setBreedName((this.m_localPlayer == null) ? StringFormatter.DEFAULT_NAME : this.m_localPlayer.getBreedInfo().getName());
        final String nationName = (this.m_localPlayer != null) ? WakfuTranslator.getInstance().getString(39, this.m_localPlayer.getCitizenComportment().getNationId(), new Object[0]) : null;
        StringFormatter.setNationName((this.m_localPlayer == null) ? StringFormatter.DEFAULT_NAME : nationName);
        PropertiesProvider.getInstance().setPropertyValue("localPlayer", localPlayer);
        PropertiesProvider.getInstance().setPropertyValue("isInGame", localPlayer != null);
        if (localPlayer != null) {
            FloorItemManager.getInstance().initialize();
            localPlayer.getActor().addPositionListener(LocalPartitionManager.getInstance());
        }
    }
    
    public void setObservedCharacter(final CharacterInfo info) {
        PropertiesProvider.getInstance().setPropertyValue("observedCharacter", info);
    }
    
    public LocalAccountInformations getLocalAccount() {
        return this.m_localAccount;
    }
    
    public void setLocalAccount(final LocalAccountInformations localAccount) {
        this.m_localAccount = localAccount;
        PropertiesProvider.getInstance().setPropertyValue("localAccount", localAccount);
    }
    
    @Override
    public void setProxyGroup(final ProxyGroup proxyGroup) {
        super.setProxyGroup(proxyGroup);
        WakfuClientConfigurationManager.getInstance().setWorldName(proxyGroup.getName());
    }
    
    @Override
    public void setServerId(final int serverId) {
        super.setServerId(serverId);
        WakfuClientInstance.getInstance().getGamePreferences().setValue(WakfuKeyPreferenceStoreEnum.LAST_SERVER_ID_KEY, serverId);
    }
    
    public void setCurrentLoginPhase(final LoginPhase currentLoginPhase) {
        this.m_currentLoginPhase = currentLoginPhase;
    }
    
    public LoginPhase getCurrentLoginPhase() {
        return this.m_currentLoginPhase;
    }
    
    @Override
    public void partialCleanUp() {
        super.partialCleanUp();
        FightManager.getInstance().cleanUp();
        ResourceManager.getInstance().removeAllResources();
        GiftManager.getInstance().clear();
        MapManager.getInstance().removeAll();
        DeadReckoningVectorsManager.removeAllManagers();
        SelfGuildInformationHelper.INSTANCE.cleanUp();
        UIEventsCalendarFrame.getInstance().reset();
        CharacterEventsCalendar.getInstance().clear();
        HeroesDisplayer.INSTANCE.clear();
        final LocalPlayerCharacter localPlayer = this.m_localPlayer;
        if (localPlayer != null) {
            ChatWindowManager.getInstance().saveChatConfiguration();
            ChatWindowManager.getInstance().clean();
            final AbstractOccupation occupation = localPlayer.getCurrentOccupation();
            if (occupation != null) {
                occupation.cancel(true, false);
            }
            localPlayer.getRunningEffectManager().removeManager();
            localPlayer.removeAdditionalAppearance();
            localPlayer.getCraftHandler().clear();
        }
        MobileManager.getInstance().removeAllMobiles();
        InteractiveIsoWorldTargetManager.getInstance().removeAll();
        LocalPartitionManager.getInstance().clear();
        ChallengeManager.getInstance().clean();
        WakfuUserGroupManager.getInstance().clear();
        long clockId = NetWorldFrame.getInstance().getShortcutClockId();
        if (clockId > 0L) {
            MessageScheduler.getInstance().removeClock(clockId);
        }
        clockId = NetWorldFrame.getInstance().getHealthBarClockId();
        if (clockId > 0L) {
            MessageScheduler.getInstance().removeClock(clockId);
        }
        AllAroundCountdown.getInstance().stop();
        WakfuSoundManager.getInstance().stopWorldSound();
        WakfuGameCalendar.getInstance().removeEventListener(TimeEventListener.INSTANCE);
        WakfuGameCalendar.getInstance().removeEventListener(SunLightModifier.INSTANCE);
        WakfuGameCalendar.getInstance().removeEventListener(SunShadowManager.getInstance());
        WorldActionGroupManager.getInstance().clear();
        FightVisibilityManager.getInstance().reset();
        final TIntObjectIterator<Nation> it = NationManager.INSTANCE.nationsIterator();
        while (it.hasNext()) {
            it.advance();
            it.value().unregisterJusticeEventHandler(CNationJusticeEventHandler.INSTANCE);
        }
        CNationJusticeEventHandler.INSTANCE.clean();
        CraftDisplayer.INSTANCE.clear();
        CharacterInfoManager.getInstance().removeAllCharacters();
        HeroesManager.INSTANCE.clear();
        HeroesLeaderManager.INSTANCE.clear();
        ClientTradeHelper.INSTANCE.setCurrentTrade(null);
        AchievementsViewManager.INSTANCE.cleanUp();
        QuestTimeManager.INSTANCE.stop();
        AlmanachView.INSTANCE.clear();
        KrosmozCollectionView.INSTANCE.clear();
        HavenWorldViewManager.INSTANCE.clear();
        QuestConfigManager.INSTANCE.saveConfig();
        AuthentificationManager.INSTANCE.clear();
        PvpLadderEntryView.clear();
        UIPartySearchFrame.cleanPartyRequester();
        SWFWrapper.INSTANCE.unload();
        BarrierManager.INSTANCE.setBarrier(SynchroBarriers.LOCAL_PLAYER_LOADED);
        this.setLocalPlayer(null);
        this.setObservedCharacter(null);
    }
    
    @Override
    public void removeAllFrames() {
        super.removeAllFrames();
        WakfuGameEntity.m_instance.removeFrame(UIEquipmentFrame.getInstance());
        UITimelineFrame.getInstance().clearDialogs();
        WakfuGameEntity.m_instance.pushFrame(UIMessageCatcherFrame.INSTANCE);
        WakfuGameEntity.m_instance.pushFrame(NetCompanionFrame.INSTANCE);
    }
    
    @Override
    public void cleanUp() {
        super.cleanUp();
    }
    
    public boolean connect() {
        return this.connect(WakfuClientInstance.getInstance().getProxy());
    }
    
    @Override
    public void onQueueFinished() {
    }
    
    @Override
    public void onQueryResult(final int queryResultCode) {
        WakfuGameEntity.m_logger.info((Object)("queryResultCode : " + queryResultCode));
    }
    
    @Override
    public void onInvalidClientVersion(final byte[] neededVersion) {
        this.pushFrame(UIAuthentificationFrame.getInstance());
        Xulor.getInstance().msgBox(WakfuTranslator.getInstance().getString("logon.invalidClientVersion", Version.format(Version.INTERNAL_VERSION), Version.format(neededVersion)), WakfuMessageBoxConstants.getMessageBoxIconUrl(1), 1027L, 1, 2);
        WakfuProgressMonitorManager.getInstance().done();
        NetEnabledWidgetManager.INSTANCE.setGroupEnabled("loginLock", true);
        this.disconnectFromServer("Invalid Client Version");
    }
    
    @Override
    protected void onConnectionToProxyFaild() {
        this.pushFrame(UIAuthentificationFrame.getInstance());
        WakfuProgressMonitorManager.getInstance().done();
        NetEnabledWidgetManager.INSTANCE.setGroupEnabled("loginLock", true);
        Xulor.getInstance().msgBox(WakfuTranslator.getInstance().getString("logon.noProxyAvailable"), WakfuMessageBoxConstants.getMessageBoxIconUrl(1), 1027L, 1, 1);
    }
    
    @Override
    protected void onLogonRequest() {
        if (this.m_currentLoginPhase == null) {
            WakfuGameEntity.m_logger.error((Object)"Aucune phase de login n'a \u00e9t\u00e9 \u00e9tablie !");
            return;
        }
        WakfuProgressMonitorManager.getInstance().getProgressMonitor(true).beginTask(WakfuTranslator.getInstance().getString("logon.progress"), 0);
        final ClientVersionMessage versionMsg = new ClientVersionMessage();
        this.getNetworkEntity().sendMessage(versionMsg);
        final ClientPublicKeyRequestMessage publicKeyRequestMessage = new ClientPublicKeyRequestMessage(this.m_currentLoginPhase.getServerId());
        this.getNetworkEntity().sendMessage(publicKeyRequestMessage);
    }
    
    @Override
    protected void onLogoffRequest() {
        if (this.m_messageBoxControler != null) {
            this.m_messageBoxControler.cleanUpAndRemoveQuick();
        }
        this.m_messageBoxControler = Xulor.getInstance().msgBox(WakfuTranslator.getInstance().getString("question.disconnect"), WakfuMessageBoxConstants.getMessageBoxIconUrl(0), 2073L, 102, 1);
        if (this.m_messageBoxControler != null) {
            this.m_messageBoxControler.addEventListener(new MessageBoxEventListener() {
                @Override
                public void messageBoxClosed(final int type, final String userEntry) {
                    if (type == 8) {
                        WakfuGameEntity.this.removeFrame(UIMenuFrame.getInstance());
                        final LocalPlayerCharacter localPlayer = WakfuGameEntity.this.getLocalPlayer();
                        if (localPlayer != null && (localPlayer.getCurrentOccupation() == null || localPlayer.getCurrentOccupation().getOccupationTypeId() != 4)) {
                            final AnimationEndedListener listener = new AnimationEndedListener() {
                                @Override
                                public void animationEnded(final AnimatedElement element) {
                                    element.removeAnimationEndedListener(this);
                                    WakfuGameEntity.this.startDisconnection();
                                }
                            };
                            WakfuGameEntity.this.playByeEmote(localPlayer, listener);
                        }
                        else {
                            WakfuGameEntity.this.startDisconnection();
                        }
                    }
                }
            });
        }
    }
    
    public void playByeEmote(final CharacterInfo localPlayer, final AnimationEndedListener listener) {
        final CharacterActor actor = localPlayer.getActor();
        actor.addAnimationEndedListener(listener);
        actor.setDirection(Direction8.SOUTH_EAST);
        String animation;
        if (localPlayer.getBreed() != AvatarBreed.SOUL) {
            final String[] emotes = { "AnimEmote-Coucou", "AnimEmote-Huss-Boucle", "AnimEmoteMarket-LeverMain" };
            animation = emotes[MathHelper.random(emotes.length)];
        }
        else {
            animation = "AnimEmote-Coucou";
        }
        if (actor.containsAnimation(animation)) {
            actor.setAnimation(animation);
        }
        else {
            listener.animationEnded(actor);
        }
    }
    
    private void startDisconnection() {
        WakfuProgressMonitorManager.getInstance().getProgressMonitor(true).beginTask(WakfuTranslator.getInstance().getString("disconnection.progress"), 0);
        this.disconnectFromServer("LogOff");
    }
    
    @Override
    protected void onGotoWorldSelectionRequest() {
        this.disconnectFromServer("Going to World Selection");
    }
    
    @Override
    protected void onQuitRequest() {
        final MessageBoxControler messageBoxControler = Xulor.getInstance().msgBox(WakfuTranslator.getInstance().getString("question.quit"), WakfuMessageBoxConstants.getMessageBoxIconUrl(0), 2073L, 102, 1);
        messageBoxControler.addEventListener(new MessageBoxEventListener() {
            @Override
            public void messageBoxClosed(final int type, final String userEntry) {
                if (type == 8) {
                    WakfuGameEntity.this.disconnectFromServer("Quit Request From Client");
                    WakfuClientInstance.getInstance().getAppUI().close();
                }
            }
        });
    }
    
    public void disconnectFromServer(@Nullable final String debugReason) {
        if (this.m_localPlayer != null) {
            this.m_localPlayer.getShortcutBarManager().sendShortcutsUpdateMessageIfNeeded();
            final long clockId = NetWorldFrame.getInstance().getShortcutClockId();
            if (clockId > 0L) {
                MessageScheduler.getInstance().removeClock(clockId);
            }
        }
        if (this.getNetworkEntity() != null) {
            WakfuGameEntity.m_logger.info((Object)("Sending DisconnectionMessage to Servers. Reason : {" + debugReason + '}'));
            final DisconnectionNotificationMessage message = new DisconnectionNotificationMessage();
            this.getNetworkEntity().sendMessage(message);
            this.getNetworkEntity().closeConnection();
        }
    }
    
    @Override
    public Optional<ChannelFuture> sendMessage(final Message message) {
        throw new UnsupportedOperationException("Pas d'envoi de message \u00ef¿½ l'aide de cette entit\u00ef¿½ : voir : getNetworkEntity()");
    }
    
    @Override
    public boolean onMessage(final Message message) {
        final boolean notProcessed = super.onMessage(message);
        AutoLogin.peekAtMessage(message);
        return notProcessed;
    }
    
    public static void checkMob(final Actor actor) {
        if (WakfuGameEntity.m_instance.m_localPlayer == null) {
            return;
        }
        final Actor localActor = WakfuGameEntity.m_instance.m_localPlayer.getActor();
        if (actor == localActor) {
            return;
        }
        final int dx = actor.getWorldCellX() - localActor.getWorldCellX();
        final int dy = actor.getWorldCellY() - localActor.getWorldCellY();
        if (Math.abs(dx) > 54 || Math.abs(dy) > 54) {
            WakfuGameEntity.m_logger.error((Object)("acteur (" + actor.getId() + ") se d\u00e9pla\u00e7ant trop loin (probl\u00e8me avec PartitionIntersectorCache?) " + "camera=" + localActor.getWorldCoordinates() + " acteurPos=" + actor.getWorldCoordinates()));
        }
    }
    
    static {
        m_instance = new WakfuGameEntity();
    }
}
