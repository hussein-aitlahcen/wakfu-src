package com.ankamagames.wakfu.client.ui.component.worldEditor.tools;

import org.apache.log4j.*;
import com.ankamagames.wakfu.common.game.havenWorld.definition.entry.*;
import com.ankamagames.wakfu.client.ui.component.worldEditor.utils.*;
import com.ankamagames.wakfu.client.core.world.havenWorld.*;
import com.ankamagames.wakfu.common.game.world.havenWorld.validator.*;
import com.ankamagames.wakfu.common.game.world.havenWorld.*;
import com.ankamagames.wakfu.client.core.world.havenWorld.buildings.*;
import com.ankamagames.wakfu.common.game.havenWorld.*;
import com.ankamagames.wakfu.client.ui.component.worldEditor.utils.modif.*;
import java.util.*;
import com.ankamagames.wakfu.common.game.havenWorld.definition.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.wakfu.common.game.world.havenWorld.items.*;

public class InsertBuilding extends Tool
{
    private static final Logger m_logger;
    private final BuildingCatalogEntry m_havenCatalogEntry;
    private static long UID_GENERATOR;
    
    public InsertBuilding(final BuildingCatalogEntry catalogEntry) {
        super(new DisplayOptions(true, true));
        this.m_havenCatalogEntry = catalogEntry;
    }
    
    @Override
    public ItemLayer getWorkingLayer() {
        return ItemLayer.BUILDING;
    }
    
    @Override
    protected void execute(final int screenX, final int screenY) {
        final AbstractBuildingDefinition buildingDefinition = BuildingDefinitionHelper.getFirstBuildingFor(this.m_havenCatalogEntry);
        final Point2i cell = this.m_worldEditor.getCellCoordFromMouse(screenX, screenY);
        final Point2i cellOffset = this.m_worldEditor.getHavenWorldImages().getBuildingCellOffset(buildingDefinition.getEditorGroupId());
        final int cellX = cell.getX() + cellOffset.getX();
        final int cellY = cell.getY() + cellOffset.getY();
        final InsertBuildingValidator validator = new InsertBuildingValidator(this.m_worldEditor.getWorkingHavenWorld(), ClientPartitionPatchLibrary.INSTANCE);
        final BuildingStruct info = new BuildingStruct(InsertBuilding.UID_GENERATOR, buildingDefinition.getId(), 0, (short)cellX, (short)cellY);
        validator.validate(info);
        final BuildingItem item = this.m_worldEditor.createBuilding(info);
        (this.m_modification = new AddBuildingModification(item)).addErrors(validator.getErrors());
    }
    
    @Override
    protected void onApply() {
        --InsertBuilding.UID_GENERATOR;
    }
    
    static {
        m_logger = Logger.getLogger((Class)InsertBuilding.class);
        InsertBuilding.UID_GENERATOR = -1L;
    }
}
