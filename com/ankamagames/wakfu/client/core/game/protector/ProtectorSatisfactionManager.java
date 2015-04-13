package com.ankamagames.wakfu.client.core.game.protector;

import com.ankamagames.wakfu.common.game.protector.*;
import com.ankamagames.wakfu.client.core.game.wakfu.*;
import com.ankamagames.framework.kernel.core.maths.*;

public class ProtectorSatisfactionManager extends AbstractProtectorSatisfactionManager
{
    public ProtectorSatisfactionManager(final ProtectorBase protector) {
        super(protector);
    }
    
    @Override
    protected void updateGlobalSatisfaction() {
        final ProtectorSatisfactionLevel globalSatisfaction = ProtectorSatisfactionLevel.fromTargets(this.m_resourceSatisfaction + this.m_monsterSatisfaction, this.m_monsterTargets.size() + this.m_resourceTargets.size());
        if (globalSatisfaction != this.m_globalSatisfaction) {
            this.m_globalSatisfaction = globalSatisfaction;
            WakfuGlobalZoneManager.getInstance().fireProtectorSatisfactionLevel();
        }
    }
    
    @Override
    public void setGlobalSatisfaction(final ProtectorSatisfactionLevel globalSatisfaction) {
        super.setGlobalSatisfaction(globalSatisfaction);
        WakfuGlobalZoneManager.getInstance().fireProtectorSatisfactionLevel();
    }
    
    public Interval getMonsterInterval(final int familyId) {
        return this.getMonsterTargets().get(familyId);
    }
    
    public Interval getResourceInterval(final int familyId) {
        return this.getResourceTargets().get(familyId);
    }
}
