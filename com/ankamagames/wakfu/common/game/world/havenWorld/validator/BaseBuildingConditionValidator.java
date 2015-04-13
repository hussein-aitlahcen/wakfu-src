package com.ankamagames.wakfu.common.game.world.havenWorld.validator;

import com.ankamagames.wakfu.common.game.havenWorld.*;
import com.ankamagames.wakfu.common.game.world.havenWorld.items.*;
import gnu.trove.*;
import com.ankamagames.wakfu.common.game.world.havenWorld.validator.conflict.*;
import com.ankamagames.wakfu.common.game.havenWorld.procedure.*;

public abstract class BaseBuildingConditionValidator extends ModificationValidator<HavenWorldDataProvider>
{
    public BaseBuildingConditionValidator(final HavenWorldDataProvider dataProvider) {
        super(dataProvider);
    }
    
    public boolean validate(final AbstractBuildingStruct info) {
        if (info.isTemporary()) {
            this.validateKama(info);
            this.validateResources(info);
            this.validateMaxQuantity(info);
        }
        this.validateWorkers(info);
        this.validateConditions(info);
        return !this.hasErrors();
    }
    
    protected abstract void validateMaxQuantity(final AbstractBuildingStruct p0);
    
    private void validateResources(final AbstractBuildingStruct info) {
        final int missingResources = BuildingValidationHelper.getMissingResources(info, (HavenWorldDataProvider)this.m_dataProvider);
        if (missingResources > 0) {
            this.addError(new MissingResources(new BuildingItem(info), missingResources));
        }
    }
    
    private void validateKama(final AbstractBuildingStruct info) {
        final long missingKamas = BuildingValidationHelper.getMissingKamas(info, (HavenWorldDataProvider)this.m_dataProvider);
        if (missingKamas > 0L) {
            this.addError(new MissingKama(new BuildingItem(info), missingKamas));
        }
    }
    
    private void validateWorkers(final AbstractBuildingStruct info) {
        final int missingWorkers = BuildingValidationHelper.getMissingWorkers(info, (HavenWorldDataProvider)this.m_dataProvider);
        if (missingWorkers > 0) {
            this.addError(new MissingWorker(new BuildingItem(info), missingWorkers));
        }
    }
    
    private void validateConditions(final AbstractBuildingStruct info) {
        final MissingBuildingCountComputer2 computer = BuildingValidationHelper.checkConditions(info, (HavenWorldDataProvider)this.m_dataProvider);
        computer.forEachMissingEntry(new TIntIntProcedure() {
            @Override
            public boolean execute(final int buildingType, final int quantity) {
                BaseBuildingConditionValidator.this.addError(new MissingBuilding(new BuildingItem(info), quantity, buildingType));
                return true;
            }
        });
    }
}
