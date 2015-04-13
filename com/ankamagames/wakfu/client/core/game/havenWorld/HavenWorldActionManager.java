package com.ankamagames.wakfu.client.core.game.havenWorld;

import java.util.*;
import com.ankamagames.wakfu.common.game.havenWorld.action.*;
import gnu.trove.*;

public class HavenWorldActionManager
{
    private final ArrayList<HavenWorldAction> m_actions;
    
    public HavenWorldActionManager() {
        super();
        this.m_actions = new ArrayList<HavenWorldAction>();
    }
    
    public boolean addAction(final HavenWorldAction action) {
        return !this.m_actions.contains(action) && this.m_actions.add(action);
    }
    
    public boolean forEachAction(final TObjectProcedure<HavenWorldAction> procedure) {
        for (int i = 0; i < this.m_actions.size(); ++i) {
            if (!procedure.execute(this.m_actions.get(i))) {
                return false;
            }
        }
        return true;
    }
}
