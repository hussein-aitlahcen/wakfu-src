package com.ankamagames.wakfu.client.ui.component.worldEditor.tools;

import org.apache.log4j.*;
import com.ankamagames.wakfu.client.ui.component.worldEditor.utils.*;
import com.ankamagames.wakfu.common.game.world.havenWorld.items.*;
import com.ankamagames.wakfu.client.core.world.havenWorld.*;
import com.ankamagames.wakfu.common.game.world.havenWorld.validator.*;
import com.ankamagames.wakfu.common.game.world.havenWorld.*;
import com.ankamagames.wakfu.client.core.world.havenWorld.buildings.*;
import com.ankamagames.wakfu.client.ui.component.worldEditor.utils.modif.*;
import java.util.*;
import com.ankamagames.wakfu.common.game.havenWorld.definition.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.wakfu.common.game.havenWorld.*;

public class MoveBuilding extends Tool
{
    private static final Logger m_logger;
    private final BuildingItem m_building;
    private final int m_cellOriginX;
    private final int m_cellOriginY;
    
    public MoveBuilding(final BuildingItem building) {
        super(new DisplayOptions(false, true));
        this.m_building = building;
        this.m_cellOriginX = this.m_building.getCell().getX();
        this.m_cellOriginY = this.m_building.getCell().getY();
    }
    
    @Override
    public ItemLayer getWorkingLayer() {
        return ItemLayer.BUILDING;
    }
    
    @Override
    protected void execute(final int screenX, final int screenY) {
        if (this.m_building == null) {
            MoveBuilding.m_logger.error((Object)"pas de batiment a d\u00e9placer");
            return;
        }
        final AbstractBuildingDefinition buildingDefinition = this.m_building.getBuildingInfo().getDefinition();
        final Point2i cell = this.m_worldEditor.getCellCoordFromMouse(screenX, screenY);
        final Point2i cellOffset = this.m_worldEditor.getHavenWorldImages().getBuildingCellOffset(buildingDefinition.getEditorGroupId());
        final int cellX = cell.getX() + cellOffset.getX();
        final int cellY = cell.getY() + cellOffset.getY();
        final InsertBuildingValidator validator = new InsertBuildingValidator(this.m_worldEditor.getWorkingHavenWorld(), ClientPartitionPatchLibrary.INSTANCE);
        final AbstractBuildingStruct info = new BuildingStruct(this.m_building.getUid(), buildingDefinition.getId(), this.m_building.getBuildingInfo().getItemId(), (short)cellX, (short)cellY);
        validator.validate(info);
        final BuildingItem item = this.m_worldEditor.moveBuilding(info);
        (this.m_modification = new MoveBuildingModification(item, (short)this.m_cellOriginX, (short)this.m_cellOriginY)).addErrors(validator.getErrors());
    }
    
    @Override
    protected void onApply() {
        super.onApply();
    }
    
    static {
        m_logger = Logger.getLogger((Class)MoveBuilding.class);
    }
}
