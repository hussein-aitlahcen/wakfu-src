package com.ankamagames.wakfu.common.game.interactiveElements.param;

import gnu.trove.*;
import com.ankamagames.wakfu.common.game.chaos.*;

public final class IEZaapOnlyOutParameter extends IEParameter
{
    private final int m_guildCost;
    private final int m_visitorCost;
    private final TIntArrayList m_actions;
    
    public IEZaapOnlyOutParameter(final int paramId, final int visualId, final ChaosInteractiveCategory chaosCategory, final int chaosCollectorId, final int guildCost, final int visitorCost) {
        super(paramId, visualId, chaosCategory, chaosCollectorId);
        this.m_actions = new TIntArrayList();
        this.m_guildCost = guildCost;
        this.m_visitorCost = visitorCost;
    }
    
    public int getGuildCost() {
        return this.m_guildCost;
    }
    
    public int getVisitorCost() {
        return this.m_visitorCost;
    }
    
    public void addAction(final int actionId) {
        this.m_actions.add(actionId);
    }
    
    public TIntArrayList getActions() {
        return this.m_actions;
    }
}
