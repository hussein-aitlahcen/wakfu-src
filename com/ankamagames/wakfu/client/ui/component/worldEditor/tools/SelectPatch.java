package com.ankamagames.wakfu.client.ui.component.worldEditor.tools;

import org.apache.log4j.*;
import com.ankamagames.wakfu.client.ui.component.worldEditor.utils.modif.*;
import com.ankamagames.wakfu.common.game.world.havenWorld.items.*;

public class SelectPatch extends Tool
{
    private static final Logger m_logger;
    
    @Override
    public ItemLayer getWorkingLayer() {
        return ItemLayer.GROUND;
    }
    
    @Override
    protected void execute(final int screenX, final int screenY) {
        final PatchItem item = this.m_worldEditor.getGroundUnderMouse(screenX, screenY);
        if (item != null) {
            this.m_worldEditor.highlightEntity(item);
            this.m_modification = new SelectPatchModification(item);
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)SelectPatch.class);
    }
}
