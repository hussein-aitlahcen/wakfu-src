package com.ankamagames.wakfu.client.ui.mru.MRUActions;

import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.core.game.protector.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.protector.*;
import com.ankamagames.wakfu.client.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.framework.kernel.events.*;
import com.ankamagames.wakfu.client.core.game.exchange.*;
import com.ankamagames.wakfu.common.game.nation.*;
import com.ankamagames.wakfu.client.core.nation.*;
import com.ankamagames.wakfu.common.game.nation.government.*;
import com.ankamagames.wakfu.client.ui.mru.*;

public class MRUManageProtectorAction extends AbstractMRUAction
{
    @Override
    public MRUActions tag() {
        return MRUActions.MANAGE_PROTECTOR_ACTION;
    }
    
    @Override
    public void run() {
        if (!this.isRunnable()) {
            return;
        }
        final NonPlayerCharacter npc = (NonPlayerCharacter)this.m_source;
        final Protector protector = npc.getProtector();
        if (protector != null) {
            final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
            if (!localPlayer.cancelCurrentOccupation(false, true)) {
                return;
            }
            this.loadProtectorManagementFrame();
        }
    }
    
    private void requestProtectorManagementData() {
        final NonPlayerCharacter npc = (NonPlayerCharacter)this.m_source;
        final Protector protector = npc.getProtector();
        if (protector != null) {
            final ProtectorManagementRequestMessage message = new ProtectorManagementRequestMessage();
            message.setProtectorId(protector.getId());
            WakfuClientInstance.getInstance();
            WakfuClientInstance.getGameEntity().getNetworkEntity().sendMessage(message);
        }
        else {
            MRUManageProtectorAction.m_logger.error((Object)("Tentative de management d'un protecteur inconnu source=" + this.m_source));
        }
    }
    
    private void loadProtectorManagementFrame() {
        if (!WakfuGameEntity.getInstance().hasFrame(UIProtectorManagementFrame.getInstance())) {
            WakfuGameEntity.getInstance().pushFrame(UIProtectorManagementFrame.getInstance());
        }
    }
    
    @Override
    public boolean isRunnable() {
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        if (localPlayer.isOnFight()) {
            return false;
        }
        if (ClientTradeHelper.INSTANCE.isTradeRunning()) {
            return false;
        }
        final CitizenComportment citizenComportment = localPlayer.getCitizenComportment();
        if (!(this.m_source instanceof NonPlayerCharacter)) {
            return false;
        }
        final Protector protector = ((NonPlayerCharacter)this.m_source).getProtector();
        if (protector == null) {
            return false;
        }
        final int protectorCurrentNationId = protector.getCurrentNationId();
        return protectorCurrentNationId != 0 && protectorCurrentNationId == citizenComportment.getNationId();
    }
    
    @Override
    public String getTranslatorKey() {
        final NationGovernment government = NationDisplayer.getInstance().getNation().getGovernment();
        final GovernmentInfo localMember = government.getMember(WakfuGameEntity.getInstance().getLocalPlayer().getId());
        return (localMember != null && NationRankRightChecker.hasRankToManageProtector(localMember.getRank())) ? "manageProtector" : "seeProtector";
    }
    
    @Override
    public AbstractMRUAction getCopy() {
        return new MRUManageProtectorAction();
    }
    
    @Override
    protected int getGFXId() {
        return MRUGfxConstants.FLEA.m_id;
    }
}
