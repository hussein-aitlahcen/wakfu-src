package com.ankamagames.wakfu.client.core.game.fight;

import com.ankamagames.framework.reflect.*;
import com.ankamagames.framework.graphics.engine.material.*;
import com.ankamagames.wakfu.common.game.fight.handler.*;
import com.ankamagames.wakfu.common.game.xp.modifications.*;
import com.ankamagames.wakfu.client.core.game.item.*;
import com.ankamagames.wakfu.client.core.game.fight.time.*;
import com.ankamagames.wakfu.common.game.fight.bombCombination.*;
import com.ankamagames.wakfu.common.game.fight.microbotCombination.*;
import org.jetbrains.annotations.*;
import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.framework.kernel.events.*;
import com.ankamagames.wakfu.client.sound.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.wakfu.client.core.script.*;
import com.ankamagames.framework.script.events.*;
import com.ankamagames.wakfu.client.core.action.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.wakfu.client.*;
import com.ankamagames.baseImpl.graphics.isometric.*;
import com.ankamagames.baseImpl.graphics.alea.display.*;
import com.ankamagames.wakfu.common.game.fight.*;
import com.ankamagames.wakfu.client.network.protocol.frame.*;
import com.ankamagames.wakfu.client.core.game.events.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.baseImpl.common.clientAndServer.global.group.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.wakfu.common.game.companion.*;
import com.ankamagames.wakfu.client.core.game.events.events.*;
import java.util.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.fight.*;
import com.ankamagames.wakfu.common.game.fight.lock.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.baseImpl.client.proxyclient.base.chat.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.time.TurnBased.timeevents.*;
import com.ankamagames.wakfu.common.game.effect.runningEffect.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effectArea.*;
import com.ankamagames.framework.ai.targetfinder.*;
import com.ankamagames.wakfu.client.alea.*;
import com.ankamagames.wakfu.client.alea.highlightingCells.*;
import com.ankamagames.wakfu.client.core.effectArea.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.baseImpl.graphics.*;
import com.ankamagames.wakfu.common.game.effectArea.*;
import com.ankamagames.wakfu.client.ui.protocol.message.effectArea.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.core.effectArea.graphics.*;
import com.ankamagames.baseImpl.graphics.ui.protocol.message.*;
import com.ankamagames.wakfu.client.alea.graphics.fightView.*;
import gnu.trove.*;
import com.ankamagames.wakfu.common.game.fight.protagonists.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.wakfu.common.game.effect.*;
import com.ankamagames.wakfu.client.core.effect.manager.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.fight.*;
import com.ankamagames.wakfu.client.core.game.interactiveElement.*;
import com.ankamagames.wakfu.common.game.spell.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.time.TurnBased.*;
import com.ankamagames.baseImpl.graphics.isometric.lights.*;
import org.apache.log4j.*;
import com.ankamagames.wakfu.common.game.fight.time.*;
import com.ankamagames.framework.kernel.core.common.message.scheduler.clock.*;
import com.ankamagames.framework.kernel.core.common.*;

public class Fight extends AbstractFight<CharacterInfo> implements FieldProvider, FightInfo
{
    public static final String[] FIELDS;
    private List<Long> m_winners;
    private List<Long> m_losers;
    private final ArrayList<FightEndListener> m_fightEndListeners;
    private FightCountdown m_countdown;
    private long m_startTime;
    private final HashSet<CharacterInfo> m_fightersReady;
    protected static final Material m_highLightColor;
    private final ClockHandler m_turnEndRequestHandler;
    private final List<FightListener<CharacterInfo>> m_listeners;
    private TByteHashSet m_lockedTeams;
    private BombCombinationDisplay m_bombCombinationDisplay;
    private boolean m_hideExternalCharacter;
    private PlayerXpModificationCollection m_playerXpModifications;
    private final List<CharacterInfo> m_fightersToDespawnAtEndOfFight;
    FightCellLightModifier m_cellLightModifier;
    private final HashMap<Long, FloorItem> m_items;
    boolean m_isEnding;
    
    protected static Material getHighLightMaterial() {
        Fight.m_highLightColor.setSpecularColor(0.2f, 0.2f, 0.2f);
        return Fight.m_highLightColor;
    }
    
    public PlayerXpModificationCollection getPlayerXpModifications() {
        return this.m_playerXpModifications;
    }
    
    public void setPlayerXpModifications(final PlayerXpModificationCollection playerXpModifications) {
        this.m_playerXpModifications = playerXpModifications;
    }
    
    public Fight(final int id, final FightModel model, final FightMap fightMap, final TByteHashSet lockedTeams, final FightStatus fightStatus) {
        this(id, model, fightMap, lockedTeams);
        this.m_status = fightStatus;
    }
    
    public Fight(final int id, final FightModel model, final FightMap fightMap, final TByteHashSet lockedTeams) {
        super(id, model, fightMap);
        this.m_fightersReady = new HashSet<CharacterInfo>(0);
        this.m_listeners = new ArrayList<FightListener<CharacterInfo>>();
        this.m_fightersToDespawnAtEndOfFight = new ArrayList<CharacterInfo>();
        this.m_items = new HashMap<Long, FloorItem>();
        this.m_timeline = new TimelineFactory(this).createTimeline();
        this.m_listeners.add(new TimelineFightListener((Timeline)this.m_timeline));
        this.m_cellLightModifier = new FightCellLightModifier(fightMap);
        this.m_turnEndRequestHandler = new ClockHandler();
        this.m_fightEndListeners = new ArrayList<FightEndListener>();
        this.m_effectExecutionListener = new FightEffectExecutionListener(this);
        this.m_lockedTeams = lockedTeams;
        this.m_bombCombinationDisplay = new BombCombinationDisplay(id);
        this.m_bombSpecialEffectComputer.setListener(this.m_bombCombinationDisplay);
    }
    
    @Override
    public void setBattlegroundBorderEffectArea(final AbstractBattlegroundBorderEffectArea battlegroundBorderEffectArea) {
        super.setBattlegroundBorderEffectArea(battlegroundBorderEffectArea);
        this.m_cellLightModifier.hideBorder(battlegroundBorderEffectArea.isInvisible());
    }
    
    public boolean consernLocalPlayer() {
        return this.m_protagonists.containsFighter((F)WakfuGameEntity.getInstance().getLocalPlayer());
    }
    
    public int getDuration() {
        final int duration = (int)(System.currentTimeMillis() - this.m_startTime) / 60000;
        if (duration >= 0) {
            return duration;
        }
        return 0;
    }
    
    @Override
    public Timeline getTimeline() {
        return (Timeline)super.getTimeline();
    }
    
    @Override
    protected void initializeMicrobotManager(final MicrobotManager manager) {
    }
    
    public void addCountdown() {
        this.m_countdown = new FightCountdown();
    }
    
    public boolean isLocked(final byte teamId) {
        return this.m_lockedTeams != null && this.m_lockedTeams.contains(teamId);
    }
    
    @Override
    public long getNextFreeEffectUserId(final byte effectUserType) {
        throw new UnsupportedOperationException("Le client ne doit pas donner un Id d'effectuser");
    }
    
    @Override
    public FightManagerBase getFightManager() {
        return null;
    }
    
    @Override
    public void onActionStart() {
        super.onActionStart();
        this.m_startTime = System.currentTimeMillis();
        for (int i = 0; i < this.m_listeners.size(); ++i) {
            try {
                this.m_listeners.get(i).onFightStart();
            }
            catch (Exception e) {
                Fight.m_logger.error((Object)"Exception levee", (Throwable)e);
            }
        }
    }
    
    @Override
    public void onFighterStartTurn(@NotNull final CharacterInfo fighter) {
        super.onFighterStartTurn(fighter);
        final int duration = this.getDurationForFighterCurrentTurn(fighter.getId()) / 1000;
        this.startCountdown(duration);
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        localPlayer.updateCharacteristicViews(FighterCharacteristicType.AP, FighterCharacteristicType.HP, FighterCharacteristicType.MP, FighterCharacteristicType.WP);
        if (useFighterTurnFrame(fighter, localPlayer)) {
            WakfuGameEntity.getInstance().removeFrame(UIFightOutTurnFrame.getInstance());
            if (!fighter.isControlledByAI()) {
                WakfuGameEntity.getInstance().pushFrame(UIFightTurnFrame.getInstance());
            }
            WakfuSoundManager.getInstance().playGUISound(600077L);
            EffectAreaSpecificDisplay.INSTANCE.onStartTurn(fighter);
            this.startTurnClock(fighter.getId());
        }
        else {
            WakfuSoundManager.getInstance().playGUISound(600078L);
            final Property property = PropertiesProvider.getInstance().getProperty("itemDetail", "equipmentDialog");
            if (property != null) {
                final ElementMap map = Xulor.getInstance().getEnvironment().getElementMap("equipmentDialog");
                if (map != null) {
                    PropertiesProvider.getInstance().firePropertyValueChanged("itemDetail", "usableNow", map);
                }
            }
        }
        ScriptEventManager.getInstance().fireEvent(new FightTurnStartedScriptEvent(fighter.getId()));
        FightActionGroupManager.getInstance().executePendingGroup(this);
        for (int i = 0; i < this.m_listeners.size(); ++i) {
            try {
                this.m_listeners.get(i).onFighterStartTurn(fighter);
            }
            catch (Exception e) {
                Fight.m_logger.error((Object)"Exception levee", (Throwable)e);
            }
        }
    }
    
    public void startCountdown(final int duration) {
        if (this.m_countdown != null) {
            this.m_countdown.start(duration);
            PropertiesProvider.getInstance().setPropertyValue("totalCountdown", duration);
        }
    }
    
    public static boolean useFighterTurnFrame(final CharacterInfo fighter, final LocalPlayerCharacter localPlayer) {
        return fighter != null && fighter.isControlledByLocalPlayer();
    }
    
    @Override
    public void addFighter(final CharacterInfo fighter, final byte teamId, final boolean liveOnlyThisFight) {
        if (!(fighter instanceof NonPlayerCharacter)) {
            super.addFighter(fighter, teamId, liveOnlyThisFight);
            return;
        }
        final NonPlayerCharacter npc = (NonPlayerCharacter)fighter;
        final long companionControllerId = npc.getCompanionControllerId();
        final CharacterInfo character = CharacterInfoManager.getInstance().getCharacter(companionControllerId);
        if (character == null) {
            super.addFighter(fighter, teamId, liveOnlyThisFight);
            return;
        }
        this.addFighter(fighter, teamId, liveOnlyThisFight, character);
    }
    
    public void startTurnClock(final long fighterId) {
        this.m_turnEndRequestHandler.startClock(this.getDurationForFighterCurrentTurn(fighterId), fighterId);
    }
    
    private void expireTurnClock() {
        if (!this.m_turnEndRequestHandler.hasClock()) {
            return;
        }
        this.m_turnEndRequestHandler.cancelClock();
    }
    
    public void requestEndTurn(final CharacterInfo fighter) {
        if (!this.m_turnEndRequestHandler.hasClock()) {
            return;
        }
        this.m_turnEndRequestHandler.cancelClock();
        this.notifyEndTurn(fighter);
    }
    
    void notifyEndTurn(final CharacterInfo fighter) {
        WakfuGameEntity.getInstance().removeFrame(UIFightTurnFrame.getInstance());
        final long fighterId = fighter.getId();
        final short tableTurn = this.getTimeline().getCurrentTableturn();
        final Message message = new FighterEndTurnRequestMessage(fighterId, tableTurn);
        WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(message);
    }
    
    public void registerLocalFightHandler(@NotNull final FightListener listener) {
        if (!this.m_listeners.contains(listener)) {
            this.m_listeners.add(listener);
        }
    }
    
    public void unRegisterLocalFightHandler(@NotNull final FightListener listener) {
        this.m_listeners.remove(listener);
    }
    
    public void addFighterToDespawnAtEndOfFight(final CharacterInfo character) {
        if (!this.m_fightersToDespawnAtEndOfFight.contains(character)) {
            this.m_fightersToDespawnAtEndOfFight.add(character);
        }
    }
    
    @Override
    public void onFighterEndTurn(@NotNull final CharacterInfo fighter) {
        if (this.m_countdown != null) {
            this.m_countdown.stop();
        }
        this.expireTurnClock();
        super.onFighterEndTurn(fighter);
        EffectAreaSpecificDisplay.INSTANCE.onEndTurn(fighter);
        for (int i = 0; i < this.m_listeners.size(); ++i) {
            try {
                this.m_listeners.get(i).onFighterEndTurn(fighter);
            }
            catch (Exception e) {
                Fight.m_logger.error((Object)"Exception levee", (Throwable)e);
            }
        }
        if (WakfuGameEntity.getInstance().hasFrame(UIFightTurnFrame.getInstance())) {
            WakfuGameEntity.getInstance().removeFrame(UIFightTurnFrame.getInstance());
        }
        if (!WakfuGameEntity.getInstance().hasFrame(UIFightOutTurnFrame.getInstance())) {
            WakfuGameEntity.getInstance().pushFrame(UIFightOutTurnFrame.getInstance());
        }
    }
    
    @Override
    protected void onPlacementStart(final int remainingTime) {
        if (this.consernLocalPlayer()) {
            StartPointDisplayer.getInstance().display(this);
            WakfuGameEntity.getInstance().pushFrame(UIFightPlacementFrame.getInstance());
            final AleaIsoCamera camera = WakfuClientInstance.getInstance().getWorldScene().getIsoCamera();
            camera.setTrackingTarget(WakfuClientInstance.getGameEntity().getLocalPlayer().getActor());
            final int duration = (remainingTime + 600) / 1000;
            if (this.m_countdown != null) {
                this.m_countdown.start(duration);
            }
            PropertiesProvider.getInstance().setPropertyValue("totalCountdown", duration);
            for (int i = 0; i < this.m_listeners.size(); ++i) {
                try {
                    this.m_listeners.get(i).onPlacementStart();
                }
                catch (Exception e) {
                    Fight.m_logger.error((Object)"Exception levee", (Throwable)e);
                }
            }
        }
    }
    
    @Override
    public void onPlacementEnd() {
        if (this.consernLocalPlayer()) {
            StartPointDisplayer.getInstance().clear();
            final AleaIsoCamera camera = WakfuClientInstance.getInstance().getWorldScene().getIsoCamera();
            camera.setTrackingTarget(WakfuClientInstance.getGameEntity().getLocalPlayer().getActor());
            WakfuGameEntity.getInstance().removeFrame(UIFightPlacementFrame.getInstance());
            this.m_countdown.stop();
            for (int i = 0; i < this.m_listeners.size(); ++i) {
                try {
                    this.m_listeners.get(i).onPlacementEnd();
                }
                catch (Exception e) {
                    Fight.m_logger.error((Object)"Exception levee", (Throwable)e);
                }
            }
        }
    }
    
    @Override
    public void onFighterMove(final CharacterInfo f, final List<int[]> path, final FightMovementType fightMovementType) {
    }
    
    @Override
    public void onFightEnded() {
        super.onFightEnded();
        this.m_turnEndRequestHandler.cancelClock();
        if (this.m_countdown != null) {
            this.m_countdown.stop();
            this.m_countdown = null;
            this.m_startTime = 0L;
        }
        this.m_fightersReady.clear();
        this.m_items.clear();
        WakfuGameEntity.getInstance().getLocalPlayer().updateShortcutBars();
        WakfuGameEntity.getInstance().getLocalPlayer().getSpellInventoryManager().updateSpellsField();
        for (int i = 0, n = this.m_fightersToDespawnAtEndOfFight.size(); i < n; ++i) {
            final CharacterInfo characterInfo = this.m_fightersToDespawnAtEndOfFight.get(i);
            NetActorsFrame.getInstance().finalDespawn(false, characterInfo);
        }
        final Iterator<FightListener<CharacterInfo>> it = this.m_listeners.iterator();
        while (it.hasNext()) {
            try {
                it.next().onFightEnded();
            }
            catch (Exception e) {
                Fight.m_logger.error((Object)"Exception levee", (Throwable)e);
            }
        }
    }
    
    @Override
    public void destroyFight() {
        FightManager.getInstance().destroyFight(this);
        this.m_listeners.clear();
        this.m_bombCombinationDisplay.removeParticles();
        super.destroyFight();
    }
    
    @Override
    public void onFighterJoinFight(final CharacterInfo characterInfo) {
        super.onFighterJoinFight(characterInfo);
        if (characterInfo.isLocalPlayer()) {
            final Collection<CharacterInfo> opponents = this.getFightersNotInTeam(characterInfo.getTeamId());
            CharacterInfo opponent = null;
            for (final CharacterInfo info : opponents) {
                if (!this.isLeader(info)) {
                    continue;
                }
                opponent = info;
                break;
            }
            if (opponent != null) {
                if (opponent instanceof NonPlayerCharacter) {
                    ClientGameEventManager.INSTANCE.fireEvent(new ClientEventFightStarted(opponent.getBreedId(), ((NonPlayerCharacter)opponent).getGroupId()));
                }
                else if (opponent instanceof PlayerCharacter) {
                    ClientGameEventManager.INSTANCE.fireEvent(new ClientEventFightStarted(opponent.getBreedId(), ((PlayerCharacter)opponent).getGroupId(GroupType.PARTY)));
                }
                else {
                    ClientGameEventManager.INSTANCE.fireEvent(new ClientEventFightStarted(opponent.getBreedId(), 0L));
                }
            }
            PropertiesProvider.getInstance().setPropertyValue("currentFightLocked", this.isLocked(this.getTeamId(characterInfo)));
        }
        else if (characterInfo instanceof NonPlayerCharacter) {
            final NonPlayerCharacter npc = (NonPlayerCharacter)characterInfo;
            final long accountId = WakfuGameEntity.getInstance().getLocalAccount().getAccountId();
            final long companionId = npc.getCompanionId();
            if (companionId > 0L && CompanionManager.INSTANCE.getCompanion(accountId, companionId) != null) {
                ClientGameEventManager.INSTANCE.fireEvent(new ClientEventCompanionEnterFight());
            }
            else {
                npc.changeToSpellAttackIfNecessary();
            }
        }
        for (int i = 0; i < this.m_listeners.size(); ++i) {
            try {
                this.m_listeners.get(i).onFighterJoinFight(characterInfo);
            }
            catch (Exception e) {
                Fight.m_logger.error((Object)"Exception levee", (Throwable)e);
            }
        }
    }
    
    @Override
    protected void addFighterToMapObstacles(final BasicCharacterInfo f) {
        if (f.getObstacleId() != -1 && this.m_fightMap.isInMap(f.getWorldCellX(), f.getWorldCellY())) {
            this.m_fightMap.assignObstacleWithId(f);
        }
    }
    
    @Override
    protected void onFighterOutOfPlay(final CharacterInfo characterInfo) {
        super.onFighterOutOfPlay(characterInfo);
        characterInfo.despawnFromFight(characterInfo, this.getFighters());
        for (int i = 0; i < this.m_listeners.size(); ++i) {
            try {
                this.m_listeners.get(i).onFighterOutOfPlay(characterInfo);
            }
            catch (Exception e) {
                Fight.m_logger.error((Object)"Exception levee", (Throwable)e);
            }
        }
    }
    
    @Override
    public void onControllerRemovedFromFight(final CharacterInfo controller) {
        for (int i = 0; i < this.m_listeners.size(); ++i) {
            try {
                this.m_listeners.get(i).onFighterRemovedFromFight(controller);
            }
            catch (Exception e) {
                Fight.m_logger.error((Object)"Exception levee", (Throwable)e);
            }
        }
        controller.setSpawnInMyFight(false);
        super.onControllerRemovedFromFight(controller);
    }
    
    @Override
    public void onControllerWinFight(final CharacterInfo controller) {
        super.onControllerWinFight(controller);
        for (int i = 0; i < this.m_listeners.size(); ++i) {
            try {
                this.m_listeners.get(i).onFighterWinFight(controller);
            }
            catch (Exception e) {
                Fight.m_logger.error((Object)"Exception levee", (Throwable)e);
            }
        }
    }
    
    @Override
    public void onControllerLoseFight(final CharacterInfo controller) {
        super.onControllerLoseFight(controller);
        for (int i = 0; i < this.m_listeners.size(); ++i) {
            try {
                this.m_listeners.get(i).onFighterLoseFight(controller);
            }
            catch (Exception e) {
                Fight.m_logger.error((Object)"Exception levee", (Throwable)e);
            }
        }
    }
    
    public void lockFight(final FightLockModification lockModification) {
        final long requestorId = lockModification.getRequestorId();
        PropertiesProvider.getInstance().setPropertyValue("currentFightLocked", lockModification.isClosed());
        final CharacterInfo requestor = this.getFighterFromId(requestorId);
        if (requestor == null) {
            Fight.m_logger.error((Object)String.format("Joueur #%d \u00e0 l'origine du verrouillage du combat id=%d inconnu", requestorId, this.getId()));
        }
        String infoMessage;
        if (lockModification.isAutoLock()) {
            infoMessage = WakfuTranslator.getInstance().getString("fight.autoLocked");
        }
        else {
            final String infoMessageKey = lockModification.isClosed() ? "fight.lockedBy" : "fight.unlockedBy";
            infoMessage = WakfuTranslator.getInstance().getString(infoMessageKey, (requestor == null) ? null : requestor.getName());
        }
        final ChatMessage chatMessage = new ChatMessage(infoMessage);
        chatMessage.setPipeDestination(4);
        ChatManager.getInstance().pushMessage(chatMessage);
    }
    
    @Override
    public void handleRunningEffectDeactivationEvent(final RunningEffectDeactivationEvent event) {
        final WakfuRunningEffect runningEffect = (WakfuRunningEffect)event.getRunningEffect();
        if (runningEffect == null) {
            return;
        }
        final EffectUser target = runningEffect.getTarget();
        if (target != null && target instanceof CharacterInfo) {
            ((CharacterInfo)target).getRunningEffectFieldProvider().removeRunningEffectProvider(runningEffect);
        }
        if (!runningEffect.unapplicationMustBeNotified()) {
            super.handleRunningEffectDeactivationEvent(event);
        }
    }
    
    @Override
    public void onEffectAreaApplication(final BasicEffectArea area, final Target applicant) {
    }
    
    @Override
    public void onEffectAreaUnapplication(final BasicEffectArea area, final Target unapplicant) {
    }
    
    @Override
    public void onEffectAreaAdded(final BasicEffectArea area) {
        super.onEffectAreaAdded(area);
        if (!this.shouldDisplayAreaForLocalPlayer(area)) {
            return;
        }
        StaticEffectAreaDisplayer.getInstance().pushStaticEffectArea(area, this);
        ((WakfuWorldScene)WakfuClientInstance.getInstance().getWorldScene()).addHighlightCellProvidersToUpdate(StaticEffectAreaDisplayer.getInstance());
        if (area.getType() == EffectAreaType.HOUR.getTypeId() && WakfuGameEntity.getInstance().hasFrame(UIFightMovementFrame.getInstance())) {
            UIFightMovementFrame.getInstance().updatePathSelection();
        }
    }
    
    @Override
    public boolean shouldDisplayAreaForLocalPlayer(final BasicEffectArea area) {
        if (area.getType() == EffectAreaType.TRAP.getTypeId()) {
            final TrapEffectArea trap = (TrapEffectArea)area;
            if (this.dontDisplayTrap(trap)) {
                return false;
            }
        }
        return (!area.isActiveProperty(EffectAreaPropertyType.INVISIBLE_FOR_ENEMIES) || WakfuGameEntity.getInstance().getLocalPlayer().getTeamId() == area.getTeamId()) && (area.getType() != EffectAreaType.ENUTROF_DEPOSIT.getTypeId() || WakfuGameEntity.getInstance().getLocalPlayer() == area.getOwner()) && (area.getType() != EffectAreaType.HOUR.getTypeId() || WakfuGameEntity.getInstance().getLocalPlayer() == area.getOwner());
    }
    
    public boolean dontDisplayTrap(final TrapEffectArea trap) {
        if (!trap.isInvisible()) {
            return false;
        }
        final EffectUser owner = trap.getOwner();
        if (owner == null) {
            return true;
        }
        final CharacterInfo trapOwner = (CharacterInfo)this.m_protagonists.getFighterById(owner.getId());
        if (trapOwner == null) {
            return true;
        }
        final byte trapOwnerTeamId = this.m_protagonists.getTeamId(trapOwner);
        final byte localPlayerTeamId = this.m_protagonists.getTeamId(WakfuGameEntity.getInstance().getLocalPlayer());
        return trapOwnerTeamId != localPlayerTeamId;
    }
    
    @Override
    public void onEffectAreaRemoved(final BasicEffectArea area) {
        super.onEffectAreaRemoved(area);
        if (area instanceof GraphicalAreaProvider) {
            final GraphicalArea graphicalArea = ((GraphicalAreaProvider)area).getGraphicalArea();
            UIOverHeadInfosFrame.getInstance().hideOverHead(graphicalArea.getAnimatedElement());
        }
        this.uncarryAreaIfNecessary(area);
        StaticEffectAreaDisplayer.getInstance().removeStaticEffectArea(area);
        WakfuGameEntity.getInstance().getLocalPlayer().getShortcutBarManager().updateShorctutBarUsability();
        final AbstractUIMessage effectAreaMsg = new UIEffectAreaMessage((AbstractEffectArea)area);
        effectAreaMsg.setId(18106);
        effectAreaMsg.setBooleanValue(false);
        Worker.getInstance().pushMessage(effectAreaMsg);
    }
    
    @Override
    public void onEffectAreaPositionChanged(final BasicEffectArea area) {
        FightVisibilityManager.getInstance().onBasicEffectAreaTeleported(this, area);
    }
    
    @Override
    public void appendFieldValue(final String fieldName, final Object value) {
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        return null;
    }
    
    @Override
    public String[] getFields() {
        return Fight.FIELDS;
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
    
    @Override
    public void removeItem(final long itemId) {
        final FloorItem floorItem = this.m_items.remove(itemId);
        if (floorItem != null) {
            floorItem.unspawn();
        }
    }
    
    @Override
    public void revealInvisibleCharacterForAll(final CharacterInfo invisible) {
    }
    
    @Override
    public Iterator<FloorItem> getItems() {
        return this.m_items.values().iterator();
    }
    
    @Override
    public void onTableTurnBegin() {
        super.onTableTurnBegin();
        for (int i = 0; i < this.m_listeners.size(); ++i) {
            try {
                this.m_listeners.get(i).onTableTurnStart();
            }
            catch (Exception e) {
                Fight.m_logger.error((Object)"Exception levee", (Throwable)e);
            }
        }
    }
    
    @Override
    public void onTableTurnEnd() {
        super.onTableTurnEnd();
        for (int i = 0; i < this.m_listeners.size(); ++i) {
            try {
                this.m_listeners.get(i).onTableTurnEnd();
            }
            catch (Exception e) {
                Fight.m_logger.error((Object)"Exception levee", (Throwable)e);
            }
        }
    }
    
    public void addItem(final FloorItem floorItem) {
        this.m_items.put(floorItem.getId(), floorItem);
    }
    
    public void removeAllItems() {
        this.m_items.clear();
    }
    
    public FloorItem getItem(final long id) {
        return this.m_items.get(id);
    }
    
    public void setLosers(final List<Long> losers) {
        this.m_losers = losers;
    }
    
    public void setWinners(final List<Long> winners) {
        this.m_winners = winners;
    }
    
    public boolean hasLost(final long id) {
        return this.m_losers != null && this.m_losers.contains(id);
    }
    
    public boolean hasWon(final long id) {
        return this.m_winners != null && this.m_winners.contains(id);
    }
    
    @Override
    public boolean isEnding() {
        return super.isEnding() || this.m_isEnding;
    }
    
    @Override
    public void putFighterBackInPlay(final CharacterInfo characterInfo) {
        super.putFighterBackInPlay(characterInfo);
    }
    
    @Override
    public boolean checkFightEnd() {
        if (!this.isCreatedAndInitialized()) {
            return false;
        }
        final TByteHashSet teamsLeftCollector = new TByteHashSet();
        final boolean enoughTeamsLeft = this.enoughTeamsLeft(teamsLeftCollector);
        if (enoughTeamsLeft) {
            return false;
        }
        for (final byte team : teamsLeftCollector) {
            this.onTeamWin(team);
        }
        return true;
    }
    
    private boolean enoughTeamsLeft(final TByteHashSet teamsLeftCollector) {
        final Iterator<CharacterInfo> inPlayFighters = (Iterator<CharacterInfo>)this.m_protagonists.getFighters(ProtagonistFilter.inPlay()).iterator();
        boolean enoughTeamsLeft = false;
        while (inPlayFighters.hasNext()) {
            final CharacterInfo characterInfo = inPlayFighters.next();
            teamsLeftCollector.add(characterInfo.getTeamId());
            if (teamsLeftCollector.size() >= this.getMinTeam()) {
                enoughTeamsLeft = true;
                break;
            }
        }
        return enoughTeamsLeft;
    }
    
    @Override
    public AbstractEffectManager<WakfuEffect> getEffectManager() {
        return EffectManager.getInstance();
    }
    
    public void displayHighLightTeam(final boolean selected) {
        final Iterator<CharacterInfo> it = this.getFighters().iterator();
        if (selected) {
            while (it.hasNext()) {
                final CharacterInfo fighter = it.next();
                fighter.getActor().colorize(getHighLightMaterial());
            }
        }
    }
    
    public void askForRecoveryProcess() {
        Fight.m_logger.error((Object)"ASK FOR FIGHT RECOVERY PROCESS");
        this.getTimeline().stop();
        final FightRecoveryRequestMessage msg = new FightRecoveryRequestMessage();
        msg.setFightId(this.getId());
        WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(msg);
    }
    
    public void recoverFromDatas(final byte[] serializedDatas) {
        Fight.m_logger.error((Object)"PROCESSING FIGHT RECOVERY");
        this.fromBuild(serializedDatas);
        this.getTimeline().start();
    }
    
    public boolean addFighterReady(final CharacterInfo fighter) {
        return this.m_fightersReady.add(fighter);
    }
    
    public boolean removeFighterReady(final CharacterInfo fighter) {
        return this.m_fightersReady.remove(fighter);
    }
    
    public boolean containsFighterReady(final CharacterInfo fighter) {
        return this.m_fightersReady.contains(fighter);
    }
    
    public boolean isHideExternalCharacter() {
        return this.m_hideExternalCharacter;
    }
    
    public void setHideExternalCharacter(final boolean hideExternalCharacter) {
        this.m_hideExternalCharacter = hideExternalCharacter;
    }
    
    public boolean isAllOtherPlayerCharacterReady(final CharacterInfo player) {
        for (final CharacterInfo character : this.getFighters()) {
            if (character instanceof PlayerCharacter && character != player && !this.m_fightersReady.contains(character)) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public FightCellLightModifier getCellLightModifier() {
        return this.m_cellLightModifier;
    }
    
    public long getLeaderId(final byte teamId) {
        return this.getLeader(teamId).getId();
    }
    
    public void tryPutAdditionalTarget(@NotNull final FightObstacle fightObstacle) {
        if (fightObstacle instanceof DestructibleMachine) {
            final DestructibleMachine destructible = (DestructibleMachine)fightObstacle;
            this.putAdditionnalTarget(destructible);
        }
    }
    
    public Collection<CharacterInfo> getFightersWithoutLeader() {
        return (Collection<CharacterInfo>)this.m_protagonists.getFighters(ProtagonistFilter.not(ProtagonistFilter.leaderOfTeam()));
    }
    
    public boolean containsFighter(final CharacterInfo fighter) {
        return this.m_protagonists.containsFighter((F)fighter);
    }
    
    @Override
    public void endFight() {
        this.m_isEnding = true;
        for (int i = 0, size = this.m_listeners.size(); i < size; ++i) {
            try {
                this.m_listeners.get(i).onFightEnd();
            }
            catch (RuntimeException e) {
                Fight.m_logger.error((Object)"Exception levee", (Throwable)e);
            }
        }
        super.endFight();
        this.fireEndFight();
    }
    
    private void fireEndFight() {
        for (int i = 0, size = this.m_fightEndListeners.size(); i < size; ++i) {
            this.m_fightEndListeners.get(i).onFightEnd(this);
        }
        this.m_fightEndListeners.clear();
    }
    
    public void addFightEndListener(final FightEndListener fightEndListener) {
        if (!this.m_fightEndListeners.contains(fightEndListener)) {
            this.m_fightEndListeners.add(fightEndListener);
        }
    }
    
    public void onSpellCast(final BasicCharacterInfo caster, final AbstractSpell spell) {
        for (int i = 0; i < this.m_listeners.size(); ++i) {
            try {
                this.m_listeners.get(i).onFighterCastSpell((CharacterInfo)caster, spell);
            }
            catch (Exception e) {
                Fight.m_logger.error((Object)"Exception levee", (Throwable)e);
            }
        }
    }
    
    @Override
    public BasicCharacterInfo getCharacterInfoAtPosition(final int posx, final int posy) {
        final BasicCharacterInfo info = super.getCharacterInfoAtPosition(posx, posy);
        if (info != null && info.isInvisible()) {
            return null;
        }
        return info;
    }
    
    static {
        FIELDS = new String[0];
        m_highLightColor = Material.Factory.newInstance();
    }
    
    private class ClockHandler implements ClockUser
    {
        @NotNull
        private final SequentialClockHandler m_innerHandler;
        private long m_currentFighterId;
        
        private ClockHandler() {
            super();
            (this.m_innerHandler = new SequentialClockHandler(new CurrentTimeMillisTimeProvider(), MessageScheduler.getInstance())).setHandler(this);
        }
        
        @Override
        public void onClockEnd(final int clockType) {
            if (this.m_currentFighterId == 0L) {
                return;
            }
            final long fighterId = this.m_currentFighterId;
            this.m_currentFighterId = 0L;
            final CharacterInfo fighter = Fight.this.getFighterFromId(fighterId);
            if (fighter == null) {
                final String message = "[TURNS] Tour expir\u00e9 pour le fighter #" + fighterId + ", or celui-ci est absent";
                Fight.m_logger.warn((Object)message);
                return;
            }
            Fight.this.notifyEndTurn(fighter);
        }
        
        @Override
        public ClockUser getClockUser() {
            return this;
        }
        
        public void cancelClock() {
            this.m_currentFighterId = 0L;
            this.m_innerHandler.cancelClock();
        }
        
        public void startClock(final int duration, final long fighterId) {
            this.m_currentFighterId = fighterId;
            this.m_innerHandler.startClock(duration, 65535);
        }
        
        public boolean hasClock() {
            return this.m_innerHandler.hasClock();
        }
    }
}
