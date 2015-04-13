package com.ankamagames.wakfu.common.game.characteristics.skill;

final class CraftSkillCharacteristic implements SkillImproveCharacteristic
{
    private int m_efficiencyPercentage;
    
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
