package com.ankamagames.wakfu.client.ui.mru.MRUActions;

import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.common.datas.specific.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.wakfu.common.game.group.party.*;
import com.ankamagames.framework.text.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.ui.mru.*;

public class MRUInvitJoinPartyAction extends AbstractMRUAction
{
    private String m_disabledReason;
    
    @Override
    public MRUActions tag() {
        return MRUActions.INVIT_TO_JOIN_PARTY;
    }
    
    @Override
    public void run() {
        final PlayerCharacter target = (PlayerCharacter)this.m_source;
        WakfuGameEntity.getInstance().getLocalPlayer().getPartyComportment().inviteSomeone(target.getName());
    }
    
    @Override
    public boolean isEnabled() {
        if (!super.isEnabled()) {
            return false;
        }
        if (!(this.m_source instanceof PlayerCharacter)) {
            return false;
        }
        final PlayerCharacter target = (PlayerCharacter)this.m_source;
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        return localPlayer != null;
    }
    
    @Override
    public boolean isRunnable() {
        if (!(this.m_source instanceof PlayerCharacter)) {
            return false;
        }
        final PlayerCharacter source = (PlayerCharacter)this.m_source;
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        if (localPlayer.isActiveProperty(WorldPropertyType.GROUP_DISABLED)) {
            return false;
        }
        if (source.isActiveProperty(WorldPropertyType.GROUP_DISABLED)) {
            return false;
        }
        if (source.isOnFight()) {
            return false;
        }
        if (localPlayer.getPartyComportment().isInParty()) {
            final PartyModelInterface party = localPlayer.getPartyComportment().getParty();
            if (party.contains(source.getId())) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public String getTranslatorKey() {
        return "invitToJoinGroup";
    }
    
    @Override
    public String getLabel() {
        final TextWidgetFormater sb = new TextWidgetFormater();
        if (!(this.m_source instanceof PlayerCharacter)) {
            return null;
        }
        sb.b().addColor(MRUInvitJoinPartyAction.DEFAULT_TOOLTIP_COLOR);
        sb.append(WakfuTranslator.getInstance().getString("desc.mru." + this.getTranslatorKey(), ((PlayerCharacter)this.m_source).getName()))._b();
        if (!this.isEnabled()) {
            sb.newLine().addColor(this.isEnabled() ? MRUInvitJoinPartyAction.DEFAULT_TOOLTIP_COLOR : MRUInvitJoinPartyAction.NOK_TOOLTIP_COLOR);
            sb.append(WakfuTranslator.getInstance().getString(this.m_disabledReason));
        }
        return sb.finishAndToString();
    }
    
    @Override
    public AbstractMRUAction getCopy() {
        return new MRUInvitJoinPartyAction();
    }
    
    @Override
    protected int getGFXId() {
        return MRUGfxConstants.FRIENDS.m_id;
    }
}
