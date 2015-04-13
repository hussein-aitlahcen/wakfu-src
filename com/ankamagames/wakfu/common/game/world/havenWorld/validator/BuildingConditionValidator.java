package com.ankamagames.wakfu.common.game.world.havenWorld.validator;

import com.ankamagames.wakfu.common.game.havenWorld.*;
import com.ankamagames.wakfu.common.game.world.havenWorld.items.*;
import com.ankamagames.wakfu.common.game.world.havenWorld.validator.conflict.*;

public class BuildingConditionValidator extends BaseBuildingConditionValidator
{
    public BuildingConditionValidator(final HavenWorldDataProvider havenWorldDataProvider) {
        super(havenWorldDataProvider);
    }
    
    @Override
    protected void validateMaxQuantity(final AbstractBuildingStruct info) {
        if (!BuildingValidationHelper.checkMaxQuantity(info, (HavenWorldDataProvider)this.m_dataProvider, false)) {
            this.addError(new BuildMaxQuantityReached(new BuildingItem(info)));
        }
    }
}
