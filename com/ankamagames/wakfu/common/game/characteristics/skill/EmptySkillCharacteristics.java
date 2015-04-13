package com.ankamagames.wakfu.common.game.characteristics.skill;

import com.ankamagames.wakfu.common.game.characteristics.craft.*;

public final class EmptySkillCharacteristics implements SkillCharacteristics
{
    public static EmptySkillCharacteristics INSTANCE;
    
    @Override
    public int getEcosystemCharacteristicEfficiency(final EcosystemActionType actionType, final ResourceType resourceType) {
        return 0;
    }
    
    @Override
    public void modifyEcosystemCharacteristicEfficiency(final EcosystemActionType actionType, final ResourceType resourceType, final int efficiencyModification) {
    }
    
    @Override
    public void reset() {
    }
    
    @Override
    public int getCraftCharacteristicEfficiency(final CraftSkillType craftSkillType, final int jobType) {
        return 0;
    }
    
    @Override
    public void modifyCraftCharacteristicEfficiency(final CraftSkillType craftSkillType, final int jobType, final int value) {
    }
    
    static {
        EmptySkillCharacteristics.INSTANCE = new EmptySkillCharacteristics();
    }
}
