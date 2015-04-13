package com.ankamagames.wakfu.common.game.world.havenWorld.validator;

import com.ankamagames.wakfu.common.game.havenWorld.*;
import com.ankamagames.wakfu.common.game.world.havenWorld.items.*;
import com.ankamagames.wakfu.common.game.world.havenWorld.validator.conflict.*;

public class BuildingEvolveConditionValidator extends BaseBuildingConditionValidator
{
    public BuildingEvolveConditionValidator(final HavenWorldDataProvider dataProvider) {
        super(dataProvider);
    }
    
    @Override
    protected void validateMaxQuantity(final AbstractBuildingStruct info) {
        if (!BuildingValidationHelper.checkMaxQuantity(info, (HavenWorldDataProvider)this.m_dataProvider, true)) {
            this.addError(new BuildMaxQuantityReached(new BuildingItem(info)));
        }
    }
}
