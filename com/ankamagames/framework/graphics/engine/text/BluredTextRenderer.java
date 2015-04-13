package com.ankamagames.framework.graphics.engine.text;

import java.awt.*;

public class BluredTextRenderer extends CustomTextRenderer2
{
    private boolean m_blured;
    
    public BluredTextRenderer(final Font arg0, final boolean arg1, final boolean arg2, final int deltaX, final int deltaY) {
        super(arg0, arg1, arg2, deltaX, deltaY);
        this.m_blured = false;
    }
    
    public BluredTextRenderer(final Font arg0, final boolean arg1, final boolean arg2, final RenderDelegate delegate, final int deltaX, final int deltaY) {
        super(arg0, arg1, arg2, delegate, deltaX, deltaY);
        if (delegate instanceof BlurRenderDelegate) {
            this.m_blured = true;
        }
        else {
            this.m_blured = false;
        }
    }
    
    @Override
    public boolean isBlured() {
        return this.m_blured;
    }
}
