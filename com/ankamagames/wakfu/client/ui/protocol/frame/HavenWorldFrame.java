package com.ankamagames.wakfu.client.ui.protocol.frame;

import com.ankamagames.framework.kernel.events.*;
import com.ankamagames.baseImpl.graphics.alea.mobile.*;
import com.ankamagames.wakfu.client.core.havenWorld.view.*;
import com.ankamagames.framework.kernel.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.wakfu.client.ui.actions.*;
import com.ankamagames.wakfu.common.game.havenWorld.*;

public abstract class HavenWorldFrame implements MessageFrame, MobileStartPathListener
{
    protected HavenWorld m_world;
    
    public void setWorld(final HavenWorld world) {
        this.m_world = world;
        HavenWorldViewManager.INSTANCE.refreshElements(world);
    }
    
    public HavenWorld getWorld() {
        return this.m_world;
    }
    
    @Override
    public void onFrameRemove(final FrameHandler frameHandler, final boolean isAboutToBeRemoved) {
        if (!isAboutToBeRemoved) {
            Xulor.getInstance().removeActionClass("wakfu.havenWorld");
        }
    }
    
    @Override
    public void onFrameAdd(final FrameHandler frameHandler, final boolean isAboutToBeAdded) {
        if (!isAboutToBeAdded) {
            Xulor.getInstance().putActionClass("wakfu.havenWorld", HavenWorldDialogsActions.class);
        }
    }
    
    public abstract Building getSelectedBuilding();
}
