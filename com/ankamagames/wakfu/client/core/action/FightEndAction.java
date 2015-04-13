package com.ankamagames.wakfu.client.core.action;

import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.alea.graphics.fightView.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.wakfu.client.alea.highlightingCells.*;
import com.ankamagames.wakfu.client.core.game.fight.spectator.*;
import com.ankamagames.wakfu.client.core.game.events.*;
import com.ankamagames.wakfu.client.core.game.events.events.*;
import com.ankamagames.wakfu.client.core.world.*;
import com.ankamagames.baseImpl.client.proxyclient.base.game.interactiveElement.*;
import com.ankamagames.wakfu.client.core.game.specifics.*;
import com.ankamagames.baseImpl.graphics.alea.mobile.*;
import com.ankamagames.wakfu.client.core.game.ressource.*;
import com.ankamagames.baseImpl.graphics.alea.animatedElement.*;
import com.ankamagames.baseImpl.graphics.game.interactiveElement.*;
import java.util.*;
import com.ankamagames.wakfu.client.core.game.fight.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.client.core.game.actor.*;
import com.ankamagames.wakfu.client.core.game.hero.*;
import com.ankamagames.wakfu.client.*;
import com.ankamagames.baseImpl.graphics.isometric.*;
import com.ankamagames.baseImpl.graphics.alea.display.*;
import com.ankamagames.baseImpl.graphics.alea.display.lights.*;
import com.ankamagames.framework.kernel.events.*;
import com.ankamagames.wakfu.client.network.protocol.frame.*;
import com.ankamagames.wakfu.client.network.protocol.frame.fight.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.wakfu.client.sound.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.fight.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.common.game.fight.*;
import com.ankamagames.wakfu.client.core.game.item.*;
import gnu.trove.*;

public class FightEndAction extends AbstractFightAction
{
    private final boolean m_flee;
    private boolean m_localVictory;
    private boolean m_isForSpectator;
    private List<Long> m_loosers;
    private List<Long> m_winners;
    private List<Long> m_escapees;
    
    public FightEndAction(final int uniqueId, final int actionType, final int actionId, final int fightId, final boolean isFlee) {
        super(uniqueId, actionType, actionId, fightId);
        this.m_flee = isFlee;
    }
    
    @Override
    protected void runCore() {
        externalFightStatus();
        if (this.getFight() instanceof ExternalFightInfo) {
            final ExternalFightEndAction action = new ExternalFightEndAction(0, 0, 0, this.getFight().getId());
            action.addLoosers(this.m_loosers);
            action.addWinners(this.m_winners);
            action.addEscapees(this.m_escapees);
            action.runCore();
            return;
        }
        if (!(this.getFight() instanceof Fight)) {
            return;
        }
        final Fight fight = (Fight)this.getFight();
        FightEndAction.m_logger.info((Object)("[FIGHT] Fin du combat " + fight.getId()));
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        final boolean hasLost = fight.hasLost(localPlayer.getId());
        if (!this.m_flee) {
            this.transfertItems();
            this.m_localVictory = !hasLost;
        }
        else {
            this.m_localVictory = false;
            fight.fighterAbandonFight(localPlayer.getId());
            fight.onControllerRemovedFromFight(localPlayer);
        }
        FightVisibilityManager.deactivateFightObservationView(fight);
        if (this.m_flee && fight.checkFightEnd()) {
            return;
        }
        PropertiesProvider.getInstance().removeProperty("fight");
        PropertiesProvider.getInstance().setPropertyValue("isInFightCreationOrPlacement", false);
        removeFrames();
        PropertiesProvider.getInstance().setPropertyValue("fight.timeline", null);
        addFrames();
        removeFightCircle(fight);
        StartPointDisplayer.getInstance().clear();
        StaticEffectAreaDisplayer.getInstance().clear();
        recenterCameraOnLocalActor(localPlayer);
        if (!this.m_flee) {
            final Collection<CharacterInfo> allFighters = fight.getAllFighters();
            for (final CharacterInfo charac : allFighters) {
                charac.forceUncarry();
            }
            for (final CharacterInfo charac : allFighters) {
                charac.resetFighterAfterFight();
            }
            fight.endFight();
        }
        else {
            localPlayer.resetFighterAfterFight();
            final ExternalFightInfo info = new ToExternalFightMigrator(fight).migrateFightToExternal();
            FightVisibilityManager.getInstance().onFighterLeaveFight(localPlayer, info.getId());
        }
        if (this.m_isForSpectator) {
            new LeaveSpectatorModeProcedure().execute();
        }
        if (hasLost) {
            ClientGameEventManager.INSTANCE.fireEvent(new ClientEventLoseFight());
        }
        for (final ClientMapInteractiveElement element : LocalPartitionManager.getInstance().getInteractiveElementsFromPartitions()) {
            element.notifyViews();
        }
        FightActionGroupManager.getInstance().removePendingGroups(fight.getId());
        if (WakfuGameEntity.getInstance().getLocalPlayer().getSymbiot() != null) {
            SymbiotView.getInstance().updateLeaderShipCapacity();
        }
        FightVisibilityManager.getInstance().updateFightVisibility();
        IsoSceneLightManager.INSTANCE.forceUpdate();
        MobileManager.getInstance().forceReload();
        ResourceManager.getInstance().forceReload();
        SimpleAnimatedElementManager.getInstance().forceReload();
        AnimatedElementSceneViewManager.getInstance().forceReload();
    }
    
    private static void externalFightStatus() {
        final TIntObjectIterator<FightInfo> itFight = FightManager.getInstance().getFightsIterator();
        while (itFight.hasNext()) {
            itFight.advance();
            final FightInfo fightInfo = itFight.value();
            if (!(fightInfo instanceof ExternalFightInfo)) {
                continue;
            }
            for (final CharacterInfo fighter : fightInfo.getFighters()) {
                fighter.setOnFight(true);
                final CharacterActor actor = fighter.getActor();
                if (fightInfo.getStatus() == AbstractFight.FightStatus.PLACEMENT && fighter instanceof PlayerCharacter) {
                    actor.addCrossSwordParticleSystem((byte)0);
                }
            }
        }
    }
    
    private static void recenterCameraOnLocalActor(final LocalPlayerCharacter localPlayer) {
        final LocalPlayerCharacter leaderCharacter = ClientHeroUtils.getLeaderCharacter();
        if (leaderCharacter == null) {
            FightEndAction.m_logger.error((Object)"Impossible de recentrer la cam\u00e9ra sur le leader, celui-ci est inconnu");
            return;
        }
        final AleaIsoCamera camera = WakfuClientInstance.getInstance().getWorldScene().getIsoCamera();
        camera.setTrackingTarget(leaderCharacter.getActor());
        camera.setDesiredZoomFactor(FightVisibilityManager.getInstance().getZoomFactorBeforeFight());
    }
    
    private static void removeFightCircle(final Fight fight) {
        IsoSceneLightManager.INSTANCE.removeLightingModifier(fight.getCellLightModifier());
    }
    
    private static void addFrames() {
        WakfuGameEntity.getInstance().pushFrame(UIWorldInteractionFrame.getInstance());
        WakfuGameEntity.getInstance().pushFrame(NetNotInFightManagementFrame.getInstance());
    }
    
    private static void removeFrames() {
        try {
            UIFightTurnFrame.getInstance().cleanUp();
            WakfuGameEntity.getInstance().removeFrame(UITimelineFrame.getInstance());
            WakfuGameEntity.getInstance().removeFrame(UITimePointSelectionFrame.getInstance());
            WakfuGameEntity.getInstance().removeFrame(UIFightPlacementFrame.getInstance());
            WakfuGameEntity.getInstance().removeFrame(UIFightTurnFrame.getInstance());
            WakfuGameEntity.getInstance().removeFrame(UIFightEndTurnFrame.getInstance());
            WakfuGameEntity.getInstance().removeFrame(UIFightMovementFrame.getInstance());
            WakfuGameEntity.getInstance().removeFrame(UIFightOutTurnFrame.getInstance());
            WakfuGameEntity.getInstance().removeFrame(UIFightFrame.getInstance());
            WakfuGameEntity.getInstance().removeFrame(NetInFightManagementFrame.getInstance());
            NetInFightManagementFrame.getInstance().associateFight(null);
            WakfuGameEntity.getInstance().removeFrame(UIFightCameraFrame.getInstance());
        }
        catch (Exception e) {
            FightEndAction.m_logger.error((Object)"Exception levee", (Throwable)e);
        }
    }
    
    @Override
    protected void onActionFinished() {
        final FightInfo fight = this.getFight();
        if (fight == null) {
            return;
        }
        final FightModel model = fight.getModel();
        if (model.displayFightResult()) {
            WakfuSoundManager.getInstance().endFight(this.m_localVictory);
        }
        final FightEndedInClientMessage message = new FightEndedInClientMessage(fight.getId());
        WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(message);
    }
    
    private void transfertItems() {
        if (!(this.getFight() instanceof Fight)) {
            return;
        }
        final Fight fight = (Fight)this.getFight();
        FloorItemManager.getInstance().foreachFloorItem(new TObjectProcedure<FloorItem>() {
            @Override
            public boolean execute(final FloorItem floorItem) {
                if (floorItem.getCurrentFightId() == fight.getId()) {
                    floorItem.setRemainingTicksInPhase(-1L);
                    floorItem.setCurrentFightId(0);
                    FloorItemManager.getInstance().resetLockAndPhase(floorItem);
                }
                return true;
            }
        });
        fight.removeAllItems();
    }
    
    public void setIsForSpectator(final boolean forSpectator) {
        this.m_isForSpectator = forSpectator;
    }
    
    public void setForExternal(final List<Long> escapees, final List<Long> winnerTeamMates, final List<Long> looserTeamMates) {
        this.m_escapees = escapees;
        this.m_winners = winnerTeamMates;
        this.m_loosers = looserTeamMates;
    }
}
