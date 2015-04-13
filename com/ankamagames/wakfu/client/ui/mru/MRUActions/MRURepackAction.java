package com.ankamagames.wakfu.client.ui.mru.MRUActions;

import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.core.game.interactiveElement.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.interactiveElement.*;
import com.ankamagames.baseImpl.client.proxyclient.base.game.interactiveElement.*;
import com.ankamagames.wakfu.client.core.game.exchange.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.client.core.game.interactiveElement.itemizable.*;
import com.ankamagames.wakfu.client.ui.mru.*;

public class MRURepackAction extends AbstractMRUAction
{
    @Override
    public MRUActions tag() {
        return MRUActions.REPACK_ITEMIZABLE_ELEMENT_ACTION;
    }
    
    @Override
    public AbstractMRUAction getCopy() {
        return new MRURepackAction();
    }
    
    @Override
    public void run() {
        if (!this.isRunnable()) {
            return;
        }
        WakfuGameEntity.getInstance().getLocalPlayer().cancelCurrentOccupation(false, true);
        if (this.m_source instanceof WakfuClientMapInteractiveElement) {
            final ClientMapInteractiveElement clientMapInteractiveElement = (WakfuClientMapInteractiveElement)this.m_source;
            clientMapInteractiveElement.fireAction(InteractiveElementAction.REPACK, WakfuGameEntity.getInstance().getLocalPlayer());
        }
        else {
            MRURepackAction.m_logger.error((Object)("Cannot REPACK a non-itemizable element of type " + this.getClass().getName()));
        }
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
        if (itemizableInfo.getOwnerId() != localplayer.getId() && !MRUActionUtils.canManageInHavenWorld(itemizableInfo.getOwnerId())) {
            return false;
        }
        this.setEnabled(itemizableInfo.canBeRepacked());
        return true;
    }
    
    @Override
    public String getTranslatorKey() {
        if (this.isEnabled()) {
            return "repack";
        }
        return "repack.impossible";
    }
    
    @Override
    protected int getGFXId() {
        return MRUGfxConstants.BAG_DIMENSIONAL.m_id;
    }
}
