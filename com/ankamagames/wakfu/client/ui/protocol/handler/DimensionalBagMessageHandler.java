package com.ankamagames.wakfu.client.ui.protocol.handler;

import org.apache.log4j.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.core.game.exchange.*;
import com.ankamagames.framework.kernel.events.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.wakfu.client.sound.*;
import com.ankamagames.baseImpl.client.proxyclient.base.chat.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.baseImpl.graphics.game.interactiveElement.*;
import com.ankamagames.baseImpl.graphics.isometric.*;
import com.ankamagames.baseImpl.graphics.isometric.maskableLayer.*;
import com.ankamagames.wakfu.client.core.game.interactiveElement.*;
import com.ankamagames.wakfu.client.core.game.pvp.*;
import com.ankamagames.baseImpl.client.proxyclient.base.game.interactiveElement.*;
import com.ankamagames.wakfu.client.core.game.actor.*;
import java.util.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.personalSpace.*;
import com.ankamagames.wakfu.client.core.world.*;
import com.ankamagames.wakfu.client.core.game.dimensionalBag.*;
import com.ankamagames.wakfu.client.core.nation.*;
import com.ankamagames.wakfu.client.core.game.protector.*;
import com.ankamagames.wakfu.common.game.protector.*;
import com.ankamagames.wakfu.common.game.nation.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.interactiveElement.*;

public class DimensionalBagMessageHandler implements UIMessageHandler
{
    private static final int IE_DIMENSIONAL_BAG_TEMPLATE_ID = 59;
    private static final Logger m_logger;
    
    @Override
    public boolean onMessage(final Message message) {
        switch (message.getId()) {
            case 17013: {
                this.execute();
                return false;
            }
            default: {
                return true;
            }
        }
    }
    
    public void execute() {
        final WakfuGameEntity gameEntity = WakfuGameEntity.getInstance();
        final LocalPlayerCharacter localPlayer = gameEntity.getLocalPlayer();
        final CharacterActor localActor = localPlayer.getActor();
        if (localPlayer.isOnFight()) {
            return;
        }
        if (localPlayer.isWaitingForResult()) {
            return;
        }
        if (ClientTradeHelper.INSTANCE.isTradeRunning()) {
            return;
        }
        if (!localPlayer.cancelCurrentOccupation(false, true)) {
            return;
        }
        if (gameEntity.hasFrame(UIDimensionalBagInteractionFrame.getInstance())) {
            gameEntity.removeFrame(UIDimensionalBagInteractionFrame.getInstance());
        }
        final DimensionalBagView visiting = localPlayer.getVisitingDimentionalBag();
        if (visiting != null) {
            localActor.setDirection(Direction8.SOUTH_EAST);
            WakfuSoundManager.getInstance().playGUISound(600066L);
            localPlayer.setWaitingForResult(true);
            this.sendLeaveBagRequest();
            return;
        }
        if (!this.positionAllowed()) {
            ChatManager.getInstance().pushMessage(WakfuTranslator.getInstance().getString("bag.forbiddenAtThisPosition"), 3);
            return;
        }
        if (!this.nationAllowed()) {
            ChatManager.getInstance().pushMessage(WakfuTranslator.getInstance().getString("bag.forbiddenInEnnemyTerritory"), 3);
            return;
        }
        if (Xulor.getInstance().isLoaded("exchangeDialog")) {
            gameEntity.removeFrame(UIExchangeFrame.getInstance());
        }
        if (Xulor.getInstance().isLoaded("recycleDialog")) {
            gameEntity.removeFrame(UIRecycleFrame.getInstance());
        }
        if (Xulor.getInstance().isLoaded("protectorManagementDialog")) {
            gameEntity.removeFrame(UIProtectorManagementFrame.getInstance());
        }
        if (Xulor.getInstance().isLoaded("weatherInfoDialog")) {
            gameEntity.removeFrame(UIWeatherInfoFrame.getInstance());
        }
        if (gameEntity.hasFrame(UIExchangeInvitationFrame.getInstance())) {
            gameEntity.removeFrame(UIExchangeInvitationFrame.getInstance());
        }
        UIMRUFrame.getInstance().closeCurrentMRU();
        final DimensionalBagInteractiveElement dbIE = ((InteractiveElementFactory<DimensionalBagInteractiveElement, C>)WakfuClientInteractiveElementFactory.getInstance()).createInteractiveElement(59L);
        if (dbIE != null) {
            dbIE.setId(0L);
            dbIE.setPosition(localPlayer.getActor().getWorldCoordinates());
            dbIE.setWorld(localPlayer.getInstanceId());
            dbIE.setOldState((short)1);
            dbIE.setState((short)2);
            dbIE.setVisible(true);
            dbIE.setSelectable(false);
            dbIE.notifyViews();
            for (final ClientInteractiveElementView view : dbIE.getViews()) {
                if (view instanceof ClientInteractiveAnimatedElementSceneView) {
                    final ClientInteractiveAnimatedElementSceneView clientView = (ClientInteractiveAnimatedElementSceneView)view;
                    AnimatedElementSceneViewManager.getInstance().addElement(clientView);
                    clientView.setDeltaZ(LayerOrder.OBJECT_LOOTED.getDeltaZ());
                    MaskableHelper.setUndefined(clientView);
                }
            }
            LocalPartitionManager.getInstance().addInteractiveElement(dbIE);
        }
        else {
            DimensionalBagMessageHandler.m_logger.error((Object)"[ItemAction] Impossible de spawn visuellement le havre-sac");
        }
        localActor.setDirection(Direction8.SOUTH_EAST);
        localActor.stopMoving();
        this.sendEnterOwnBagRequest();
        final PlayerCharacter player = WakfuGameEntity.getInstance().getLocalPlayer();
        if (!player.getCitizenComportment().getPvpState().isActive()) {
            WakfuSoundManager.getInstance().playGUISound(600065L);
            localPlayer.setWaitingForResult(true);
            return;
        }
        PvpInteractionManager.INSTANCE.startInteraction(new PvpInteractionHandler() {
            @Override
            public void onFinish() {
                WakfuSoundManager.getInstance().playGUISound(600065L);
                localPlayer.setWaitingForResult(true);
                DimensionalBagMessageHandler.this.sendEnterOwnBagRequest();
            }
            
            @Override
            public void onCancel() {
                LocalPartitionManager.getInstance().removeInteractiveElement(dbIE);
            }
        });
    }
    
    void sendEnterOwnBagRequest() {
        final PSEnterRequestMessage msg = new PSEnterRequestMessage();
        msg.setWorldObjectId(0L);
        WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(msg);
    }
    
    private void sendLeaveBagRequest() {
        final PSLeaveRequestMessage msg = new PSLeaveRequestMessage();
        WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(msg);
    }
    
    private boolean positionAllowed() {
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        final WorldInfoManager.WorldInfo worldInfo = WorldInfoManager.getInstance().getInfo(localPlayer.getInstanceId());
        return DimensionalBagPlacementPolicy.checkPosition(localPlayer.getPositionConst(), worldInfo);
    }
    
    private boolean nationAllowed() {
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        final Territory currentTerritory = localPlayer.getCurrentTerritory();
        if (currentTerritory == null) {
            return true;
        }
        final ProtectorBase protector = currentTerritory.getProtector();
        final CitizenComportment comportment = localPlayer.getCitizenComportment();
        return protector == null || CitizenAuthorizationRules.getInstance().canEnterBag(comportment, protector.getCurrentNationId());
    }
    
    static {
        m_logger = Logger.getLogger((Class)DimensionalBagMessageHandler.class);
    }
}
