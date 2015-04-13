package com.ankamagames.wakfu.common.game.aptitudeNewVersion.listener;

import com.ankamagames.wakfu.common.game.aptitudeNewVersion.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;

public final class ReloadEffectsOnLevelUpdate implements AptitudeBonusInventoryListener
{
    private final BasicCharacterInfo m_owner;
    
    public ReloadEffectsOnLevelUpdate(final BasicCharacterInfo owner) {
        super();
        this.m_owner = owner;
    }
    
    @Override
    public void onLevelChanged(final int bonusId, final short level) {
        this.m_owner.reloadNewAptitudeEffects(this.m_owner.getOwnContext());
    }
}
