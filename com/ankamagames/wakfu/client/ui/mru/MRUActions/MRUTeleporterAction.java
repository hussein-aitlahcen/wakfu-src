package com.ankamagames.wakfu.client.ui.mru.MRUActions;

import com.ankamagames.wakfu.common.game.interactiveElements.param.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.baseImpl.common.clientAndServer.account.*;
import com.ankamagames.wakfu.common.account.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.interactiveElement.*;
import com.ankamagames.baseImpl.client.proxyclient.base.game.interactiveElement.*;
import com.ankamagames.wakfu.client.core.game.interactiveElement.*;

public class MRUTeleporterAction extends MRUActionWithCost
{
    private short m_linkedInstanceId;
    private IETeleporterParameter.Exit m_exit;
    
    @Override
    public MRUTeleporterAction getCopy() {
        return new MRUTeleporterAction();
    }
    
    @Override
    public boolean isUsable() {
        if (!this.hasValidExitFor(WakfuGameEntity.getInstance().getLocalPlayer())) {
            this.m_disabledReason.add(1);
        }
        return this.m_disabledReason.isEmpty() && super.isUsable();
    }
    
    private boolean hasValidExitFor(final LocalPlayerCharacter localPlayer) {
        if (this.m_linkedInstanceId <= 0) {
            MRUTeleporterAction.m_logger.error((Object)"Teleporter sans instance li\u00e9e");
            return false;
        }
        final WakfuAccountInformationHolder holder = new WakfuAccountInformationHolder() {
            @Override
            public short getInstanceId() {
                return MRUTeleporterAction.this.m_linkedInstanceId;
            }
            
            @Override
            public WakfuAccountInformationHandler getAccountInformationHandler() {
                return localPlayer.getAccountInformationHandler();
            }
        };
        return WakfuAccountPermissionContext.SUBSCRIBER_ZONE.hasPermission(holder);
    }
    
    @Override
    protected void doAction(final InteractiveElementAction action, final InteractiveElementUser localPlayer, final ClientMapInteractiveElement interactiveElement) {
        ((Teleporter)interactiveElement).use(localPlayer, this.m_exit);
    }
    
    public void setLinkedInstanceId(final short destinationInstanceId) {
        this.m_linkedInstanceId = destinationInstanceId;
    }
    
    public void setExit(final IETeleporterParameter.Exit action) {
        this.m_exit = action;
    }
    
    public IETeleporterParameter.Exit getExit() {
        return this.m_exit;
    }
}
