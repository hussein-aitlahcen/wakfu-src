package com.ankamagames.xulor2.util;

import com.ankamagames.baseImpl.graphics.*;

public class XulorSetWatcherTarget implements XulorLoadUnload
{
    private String m_containerId;
    private ScreenTarget m_target;
    
    public XulorSetWatcherTarget(final String containerId, final ScreenTarget target) {
        super();
        this.m_target = target;
        this.m_containerId = containerId;
    }
    
    public String getContainerId() {
        return this.m_containerId;
    }
    
    public ScreenTarget getTarget() {
        return this.m_target;
    }
}
