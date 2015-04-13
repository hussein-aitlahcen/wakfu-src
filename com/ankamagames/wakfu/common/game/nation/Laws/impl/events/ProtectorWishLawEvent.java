package com.ankamagames.wakfu.common.game.nation.Laws.impl.events;

import com.ankamagames.wakfu.common.game.nation.*;
import com.ankamagames.wakfu.common.game.nation.Laws.*;

public class ProtectorWishLawEvent extends NationLawEvent
{
    private ActionType m_action;
    
    public ProtectorWishLawEvent(final Citizen citizen) {
        super(citizen);
    }
    
    @Override
    public NationLawEventType getEventType() {
        return NationLawEventType.PROTECTOR_WISH_ACTION;
    }
    
    public void setAction(final ActionType action) {
        this.m_action = action;
    }
    
    public ActionType getActionType() {
        return this.m_action;
    }
    
    public enum ActionType
    {
        AGAINST, 
        FOLLOWING;
    }
}
