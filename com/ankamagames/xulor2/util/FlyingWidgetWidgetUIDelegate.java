package com.ankamagames.xulor2.util;

import com.ankamagames.baseImpl.graphics.alea.adviser.text.flying.*;
import com.ankamagames.xulor2.component.*;

public class FlyingWidgetWidgetUIDelegate implements FlyingWidgetUIDelegate
{
    private final Widget m_widget;
    
    public FlyingWidgetWidgetUIDelegate(final Widget widget) {
        super();
        this.m_widget = widget;
    }
    
    @Override
    public int getScreenX() {
        return this.m_widget.getScreenX() - MasterRootContainer.getInstance().getWidth() / 2;
    }
    
    @Override
    public int getScreenY() {
        return this.m_widget.getScreenY() - MasterRootContainer.getInstance().getHeight() / 2;
    }
    
    @Override
    public int getWidth() {
        return this.m_widget.getWidth();
    }
    
    @Override
    public int getHeight() {
        return this.m_widget.getHeight();
    }
}
