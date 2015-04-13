package com.ankamagames.wakfu.client.ui.mru.MRUActions;

import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.client.core.world.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.wakfu.client.core.game.interactiveElement.*;
import com.ankamagames.wakfu.client.core.game.interactiveElement.itemizable.*;
import com.ankamagames.framework.kernel.events.*;
import com.ankamagames.wakfu.client.core.game.exchange.*;
import com.ankamagames.wakfu.client.ui.mru.*;

public class MRUMoveAction extends AbstractMRUAction
{
    @Override
    public MRUActions tag() {
        return MRUActions.MOVE_ITEMIZABLE_ELEMENT_ACTION;
    }
    
    @Override
    public AbstractMRUAction getCopy() {
        return new MRUMoveAction();
    }
    
    private boolean isInOwnDimensionalBag() {
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        return localPlayer.getVisitingDimentionalBag() == localPlayer.getOwnedDimensionalBag();
    }
    
    private boolean isInHavenWorld() {
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        final short instanceId = localPlayer.getInstanceId();
        final WorldInfoManager.WorldInfo info = WorldInfoManager.getInstance().getInfo(instanceId);
        return info.isHavenWorld();
    }
    
    private UIInteractionFrame getInteractionFrame() {
        if (this.isInOwnDimensionalBag()) {
            return UIDimensionalBagInteractionFrame.getInstance();
        }
        return UIHavenWorldInteractionFrame.getInstance();
    }
    
    @Override
    public void run() {
        if (!this.isRunnable()) {
            return;
        }
        WakfuGameEntity.getInstance().getLocalPlayer().cancelCurrentOccupation(false, true);
        final UIInteractionFrame frame = this.getInteractionFrame();
        final WakfuClientMapInteractiveElement element = (WakfuClientMapInteractiveElement)this.m_source;
        final ItemizableInfo itemizableInfo = element.getOrCreateItemizableInfo();
        frame.setElement(itemizableInfo);
        frame.setOnValidate(new Runnable() {
            @Override
            public void run() {
                itemizableInfo.getDragInfo().validateDrag();
            }
        });
        frame.setOnCancel(new Runnable() {
            @Override
            public void run() {
                itemizableInfo.getDragInfo().cancelDrag();
            }
        });
        WakfuGameEntity.getInstance().pushFrame(frame);
    }
    
    @Override
    public boolean isRunnable() {
        final LocalPlayerCharacter localplayer = WakfuGameEntity.getInstance().getLocalPlayer();
        if (localplayer.isWaitingForResult() || localplayer.isOnFight() || ClientTradeHelper.INSTANCE.isTradeRunning()) {
            return false;
        }
        if (!(this.m_source instanceof WakfuClientMapInteractiveElement)) {
            return false;
        }
        final ItemizableInfo itemizableInfo = ((WakfuClientMapInteractiveElement)this.m_source).getItemizableInfo();
        return itemizableInfo.getOwnerId() == localplayer.getId() || MRUActionUtils.canManageInHavenWorld(itemizableInfo.getOwnerId());
    }
    
    @Override
    public String getTranslatorKey() {
        return "move";
    }
    
    @Override
    protected int getGFXId() {
        return MRUGfxConstants.HAND.m_id;
    }
}
