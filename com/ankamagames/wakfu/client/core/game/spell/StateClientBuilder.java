package com.ankamagames.wakfu.client.core.game.spell;

import com.ankamagames.wakfu.common.game.spell.*;

public final class StateClientBuilder extends StateBuilder<StateClient>
{
    public StateClientBuilder(final StateClient state) {
        super(state);
    }
    
    public void setDisplayCasterName(final boolean displayCasterName) {
        ((StateClient)this.m_state).m_displayCasterName = displayCasterName;
    }
    
    public void setShowInTimeline(final boolean showInTimeline) {
        ((StateClient)this.m_state).m_showInTimeline = showInTimeline;
    }
    
    public void setGfxId(final int gfxId) {
        ((StateClient)this.m_state).m_gfxId = gfxId;
    }
}
