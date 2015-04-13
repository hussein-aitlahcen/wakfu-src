package com.ankamagames.wakfu.client.ui.mru.MRUActions;

import com.ankamagames.wakfu.client.core.game.interactiveElement.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.interactiveElement.*;
import com.ankamagames.wakfu.client.sound.*;
import com.ankamagames.wakfu.client.core.game.exchange.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.client.core.game.interactiveElement.itemizable.*;
import com.ankamagames.wakfu.client.ui.mru.*;

public class MRURotateAction extends AbstractMRUAction
{
    @Override
    public MRUActions tag() {
        return MRUActions.ROTATE_ITEMIZABLE_ELEMENT_ACTION;
    }
    
    @Override
    public AbstractMRUAction getCopy() {
        return new MRURotateAction();
    }
    
    @Override
    public void run() {
        if (!this.isRunnable()) {
            return;
        }
        final WakfuClientMapInteractiveElement itemizableElement = (WakfuClientMapInteractiveElement)this.m_source;
        itemizableElement.fireAction(InteractiveElementAction.ROTATE, WakfuGameEntity.getInstance().getLocalPlayer());
        WakfuSoundManager.getInstance().playGUISound(600044L);
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
        return "rotate";
    }
    
    @Override
    protected int getGFXId() {
        return MRUGfxConstants.ROTATION.m_id;
    }
}
