package com.ankamagames.wakfu.common.game.characteristics.skill;

import com.ankamagames.wakfu.common.game.characteristics.craft.*;

final class EcosystemSkillCharacteristic implements SkillImproveCharacteristic
{
    private final EcosystemActionType m_actionType;
    private final ResourceType m_resourceType;
    private int m_efficiencyPercentage;
    
    EcosystemSkillCharacteristic(final EcosystemActionType actionType, final ResourceType resourceType) {
        super();
        this.m_efficiencyPercentage = 0;
        this.m_actionType = actionType;
        this.m_resourceType = resourceType;
    }
    
    public EcosystemActionType getActionType() {
        return this.m_actionType;
    }
    
    public ResourceType getResourceType() {
        return this.m_resourceType;
    }
    
    @Override
    public int getEfficiencyPercentage() {
        return this.m_efficiencyPercentage;
    }
    
    public void setEfficiencyPercentage(final int efficiencyPercentage) {
        if (efficiencyPercentage > 100) {
            this.m_efficiencyPercentage = 100;
        }
        if (efficiencyPercentage < 0) {
            this.m_efficiencyPercentage = 0;
        }
        this.m_efficiencyPercentage = efficiencyPercentage;
    }
}
