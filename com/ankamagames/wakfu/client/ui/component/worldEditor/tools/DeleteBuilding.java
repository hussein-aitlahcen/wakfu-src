package com.ankamagames.wakfu.client.ui.component.worldEditor.tools;

import org.apache.log4j.*;
import com.ankamagames.wakfu.client.ui.component.worldEditor.utils.modif.*;
import com.ankamagames.wakfu.common.game.world.havenWorld.items.*;

public class DeleteBuilding extends Tool
{
    private static final Logger m_logger;
    
    @Override
    public ItemLayer getWorkingLayer() {
        return ItemLayer.BUILDING;
    }
    
    @Override
    protected void execute(final int screenX, final int screenY) {
        final BuildingItem building = this.m_worldEditor.getBuildingUnderMouse(screenX, screenY);
        this.m_modification = new DeleteBuildingModification(building);
        this.m_worldEditor.markAsRemoved(building);
    }
    
    static {
        m_logger = Logger.getLogger((Class)DeleteBuilding.class);
    }
}
