package com.ankamagames.wakfu.common.game.characteristics.skill;

import com.ankamagames.wakfu.common.game.characteristics.craft.*;

public interface SkillCharacteristics
{
    int getEcosystemCharacteristicEfficiency(EcosystemActionType p0, ResourceType p1);
    
    void modifyEcosystemCharacteristicEfficiency(EcosystemActionType p0, ResourceType p1, int p2);
    
    void reset();
    
    int getCraftCharacteristicEfficiency(CraftSkillType p0, int p1);
    
    void modifyCraftCharacteristicEfficiency(CraftSkillType p0, int p1, int p2);
}
