package com.ankamagames.wakfu.client.ui.component.worldEditor.utils.modif;

import org.apache.log4j.*;
import com.ankamagames.wakfu.client.core.world.havenWorld.*;
import com.ankamagames.wakfu.client.ui.component.worldEditor.*;
import com.ankamagames.wakfu.common.game.world.havenWorld.items.*;
import com.ankamagames.wakfu.client.core.world.havenWorld.buildings.*;
import com.ankamagames.wakfu.common.game.havenWorld.*;

public class MoveBuildingModification extends Modification<BuildingItem>
{
    private static final Logger m_logger;
    private final short m_cellOriginX;
    private final short m_cellOriginY;
    
    public MoveBuildingModification(final BuildingItem item, final short originCellX, final short originCellY) {
        super(item);
        this.m_cellOriginX = originCellX;
        this.m_cellOriginY = originCellY;
    }
    
    @Override
    public Type getType() {
        return Type.MOVE;
    }
    
    @Override
    public void apply(final HavenWorldTopology world) {
        world.addBuilding(((BuildingItem)this.m_item).getBuildingInfo());
    }
    
    @Override
    public void unapply(final WorldEditor worldEditor) {
        worldEditor.remove(this.m_item);
        final AbstractBuildingStruct info = ((BuildingItem)this.m_item).getBuildingInfo();
        worldEditor.createBuilding(new BuildingStruct(info.getBuildingUid(), info.getBuildingDefinitionId(), info.getItemId(), this.m_cellOriginX, this.m_cellOriginY));
    }
    
    @Override
    public void onSuccess(final WorldEditor editor) {
        final AbstractBuildingStruct info = ((BuildingItem)this.m_item).getBuildingInfo();
        editor.createBuilding(new BuildingStruct(info.getBuildingUid(), info.getBuildingDefinitionId(), info.getItemId(), info.getCellX(), info.getCellY()));
    }
    
    static {
        m_logger = Logger.getLogger((Class)MoveBuildingModification.class);
    }
}
