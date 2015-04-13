package com.ankamagames.wakfu.client.core.game.characterInfo.occupation;

import com.ankamagames.wakfu.common.game.characterInfo.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.common.game.skill.*;
import com.ankamagames.wakfu.common.datas.*;

public abstract class AbstractOccupation implements BasicOccupation
{
    protected LocalPlayerCharacter m_localPlayer;
    
    protected AbstractOccupation() {
        super();
        this.m_localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
    }
    
    @Override
    public boolean cancel() {
        return this.cancel(false, true);
    }
    
    public boolean onPlayerMove() {
        return this.cancel(false, false);
    }
    
    protected boolean isOwnerEquippedForAction(final ActionVisual visual) {
        return visual.isValidFor(this.m_localPlayer);
    }
    
    public abstract boolean cancel(final boolean p0, final boolean p1);
    
    public boolean canPlayEmote() {
        return false;
    }
}
