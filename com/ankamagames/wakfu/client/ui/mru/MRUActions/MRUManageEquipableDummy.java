package com.ankamagames.wakfu.client.ui.mru.MRUActions;

import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.wakfu.client.core.game.interactiveElement.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.client.ui.mru.*;
import com.ankamagames.baseImpl.graphics.alea.mobile.*;

public final class MRUManageEquipableDummy extends EndPathListenerMRUAction
{
    @Override
    public MRUActions tag() {
        return MRUActions.MANAGE_EQUIPABLE_DUMMY;
    }
    
    @Override
    protected void startUse() {
        final EquipableDummy dummy = (EquipableDummy)this.m_source;
        UIEquipableDummyFrame.getInstance().setEquipableDummy(dummy);
    }
    
    @Override
    public boolean isRunnable() {
        if (!(this.m_source instanceof EquipableDummy)) {
            return false;
        }
        final WakfuClientMapInteractiveElement dummy = (WakfuClientMapInteractiveElement)this.m_source;
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        final long ownerId = dummy.getItemizableInfo().getOwnerId();
        return ownerId == localPlayer.getId() || MRUActionUtils.canManageInHavenWorld(ownerId);
    }
    
    @Override
    public String getTranslatorKey() {
        return "manageEquipableDummy";
    }
    
    @Override
    public AbstractMRUAction getCopy() {
        return new MRUManageEquipableDummy();
    }
    
    @Override
    protected int getGFXId() {
        return MRUGfxConstants.FLEA.m_id;
    }
}
