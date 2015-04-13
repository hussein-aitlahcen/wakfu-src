package com.ankamagames.wakfu.client.ui.mru.MRUActions;

import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.framework.kernel.events.*;
import com.ankamagames.wakfu.client.core.game.protector.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.client.core.game.exchange.*;
import com.ankamagames.wakfu.common.account.subscription.*;
import com.ankamagames.wakfu.common.game.nation.*;
import com.ankamagames.wakfu.common.game.nation.impl.*;
import com.ankamagames.baseImpl.graphics.alea.mobile.*;
import com.ankamagames.wakfu.client.ui.mru.*;

public class MRUPreviousVoteListAction extends AbstractMRUAction implements MobileEndPathListener
{
    @Override
    public MRUActions tag() {
        return MRUActions.PREVIOUS_VOTE_LIST_ACTION;
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
            PropertiesProvider.getInstance().setPropertyValue("voteList", true);
            WakfuGameEntity.getInstance().pushFrame(UIVoteInformationFrame.getInstance());
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
        if (!(this.m_source instanceof NonPlayerCharacter)) {
            return false;
        }
        final Protector protector = ((NonPlayerCharacter)this.m_source).getProtector();
        if (protector == null) {
            return false;
        }
        final CitizenComportment citizen = localPlayer.getCitizenComportment();
        final int protectorCurrentNationId = protector.getCurrentNationId();
        if (protectorCurrentNationId == 0 || protectorCurrentNationId == 34 || protectorCurrentNationId != citizen.getNationId()) {
            return false;
        }
        final Nation nation = protector.getCurrentNation();
        if (nation == null) {
            return false;
        }
        if (nation.isVoteRunning()) {
            return false;
        }
        final LocalPlayerCharacter player = WakfuGameEntity.getInstance().getLocalPlayer();
        return player.hasSubscriptionRight(SubscriptionRight.ACCESS_TO_ELECTION);
    }
    
    @Override
    public String getLabel() {
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        final AbstractCitizenComportement citizen = (AbstractCitizenComportement)localPlayer.getCitizenComportment();
        return super.getLabel();
    }
    
    @Override
    public String getTranslatorKey() {
        return "voteList";
    }
    
    @Override
    public AbstractMRUAction getCopy() {
        return new MRUPreviousVoteListAction();
    }
    
    @Override
    public void pathEnded(final PathMobile mobile, final int x, final int y, final short altitude) {
        mobile.removeEndPositionListener(this);
        WakfuGameEntity.getInstance().pushFrame(UIVoteInformationFrame.getInstance());
    }
    
    @Override
    protected int getGFXId() {
        return MRUGfxConstants.LIST.m_id;
    }
}
