package com.ankamagames.wakfu.client.core.game.group.party.member;

import com.ankamagames.wakfu.common.game.group.member.*;
import com.ankamagames.baseImpl.client.proxyclient.base.chat.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;

public final class PartyMemberModelClientListener implements PlayerMemberModelListener
{
    public static final int NO_LEVEL = -1;
    private int m_level;
    
    public PartyMemberModelClientListener() {
        super();
        this.m_level = -1;
    }
    
    @Override
    public void onSetLevel(final PartyMemberModel memberModel) {
        if (this.m_level == -1) {
            this.m_level = memberModel.getLevel();
            return;
        }
        if (this.m_level < memberModel.getLevel() && WakfuGameEntity.getInstance().getLocalPlayer().getId() != memberModel.getCharacterId()) {
            ChatManager.getInstance().pushMessage(WakfuTranslator.getInstance().getString("infoPop.levelUp", memberModel.getLevel(), memberModel.getName()), 4);
        }
        this.m_level = memberModel.getLevel();
    }
    
    @Override
    public void onSetDead(final PartyMemberModel memberModel) {
        this.fireStateUpdate(memberModel);
    }
    
    @Override
    public void onSetInFight(final PartyMemberModel memberModel) {
        this.fireStateUpdate(memberModel);
    }
    
    private void fireStateUpdate(final PartyMemberModel memberModel) {
        UIPartyFrame.getInstance().fireMemberStateUpdate(memberModel.getCharacterId());
    }
    
    @Override
    public void onSetCurrentHp(final PartyMemberModel memberModel) {
    }
    
    @Override
    public void onSetMaxHp(final PartyMemberModel memberModel) {
    }
}
