package com.ankamagames.wakfu.common.game.world.havenWorld.validator;

import org.apache.log4j.*;
import com.ankamagames.wakfu.common.game.world.havenWorld.validator.conflict.*;
import gnu.trove.*;
import com.ankamagames.wakfu.common.game.havenWorld.*;
import java.util.*;
import com.ankamagames.wakfu.common.game.world.havenWorld.items.*;

public class WorldValidator extends ModificationValidator<HavenWorldDataProvider>
{
    private static final Logger m_logger;
    
    public WorldValidator(final HavenWorldDataProvider havenWorldProvider) {
        super(havenWorldProvider);
    }
    
    public boolean validate() {
        this.removeAllErrors();
        final ArrayList<ConstructionError> errors = validate((HavenWorldDataProvider)this.m_dataProvider);
        return errors.isEmpty();
    }
    
    public boolean validateRecurs() {
        this.removeAllErrors();
        final HavenWorldDataProvider havenWorldProvider = new HavenWorldDataProvider((HavenWorldDataProvider)this.m_dataProvider);
        boolean changed = true;
        while (changed) {
            final ArrayList<ConstructionError> errors = validate(havenWorldProvider);
            if (errors.isEmpty()) {
                break;
            }
            changed = this.updateWorld(havenWorldProvider, errors);
            for (int i = 0, size = errors.size(); i < size; ++i) {
                this.addError(errors.get(i));
            }
        }
        return !this.hasErrors();
    }
    
    private static ArrayList<ConstructionError> validate(final HavenWorldDataProvider havenWorldProvider) {
        final ArrayList<ConstructionError> worldErrors = new ArrayList<ConstructionError>();
        havenWorldProvider.forEachBuilding(new TObjectProcedure<AbstractBuildingStruct>() {
            @Override
            public boolean execute(final AbstractBuildingStruct object) {
                final BaseBuildingConditionValidator validator = new BuildingConditionValidator(havenWorldProvider);
                validator.validate(object);
                for (final ConstructionError buildingError : validator.getErrors()) {
                    if (!worldErrors.contains(buildingError)) {
                        worldErrors.add(buildingError);
                    }
                }
                return true;
            }
        });
        return worldErrors;
    }
    
    private boolean updateWorld(final HavenWorldDataProvider havenWorldProvider, final ArrayList<ConstructionError> errors) {
        boolean changed = false;
        for (final ConstructionError error : errors) {
            final BuildingItem item = (BuildingItem)error.getItem();
            changed |= havenWorldProvider.removeBuilding(item.getBuildingInfo());
        }
        return changed;
    }
    
    static {
        m_logger = Logger.getLogger((Class)WorldValidator.class);
    }
}
