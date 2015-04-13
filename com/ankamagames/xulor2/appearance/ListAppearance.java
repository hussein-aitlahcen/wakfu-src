package com.ankamagames.xulor2.appearance;

import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.xulor2.component.*;

public class ListAppearance extends DecoratorAppearance implements ColorClient
{
    public static final String TAG = "ListAppearance";
    public static final String SELECTION_COLOR = "selection";
    private Color m_mouseOverColor;
    
    @Override
    public void add(final EventDispatcher e) {
        super.add(e);
        if (e instanceof ColorElement) {
            this.setColor(((ColorElement)e).getColor(), null);
        }
    }
    
    @Override
    public String getTag() {
        return "ListAppearance";
    }
    
    @Override
    public void setColor(final Color c, final String name) {
        if (name == null || name.equalsIgnoreCase("selection")) {
            this.m_mouseOverColor = c;
            if (this.m_widget instanceof List) {
                ((List)this.m_widget).setMouseOverColor(this.m_mouseOverColor);
                ((List)this.m_widget).setSelectedColor(this.m_mouseOverColor);
            }
        }
        else {
            super.setColor(c, name);
        }
    }
    
    @Override
    public void onCheckIn() {
        super.onCheckIn();
        this.m_mouseOverColor = null;
    }
}
