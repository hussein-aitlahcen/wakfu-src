package com.ankamagames.wakfu.client.ui.component.worldEditor.tools;

import com.ankamagames.wakfu.common.game.world.havenWorld.items.*;

public class DoNothing extends Tool
{
    @Override
    public ItemLayer getWorkingLayer() {
        return null;
    }
    
    @Override
    protected void execute(final int screenX, final int screenY) {
    }
}
