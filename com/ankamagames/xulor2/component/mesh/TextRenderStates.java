package com.ankamagames.xulor2.component.mesh;

import com.ankamagames.xulor2.component.*;
import com.ankamagames.framework.graphics.engine.*;
import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.xulor2.graphics.*;
import com.ankamagames.framework.graphics.engine.states.*;

public class TextRenderStates extends RenderStates
{
    private final Widget m_widget;
    private boolean m_preRender;
    
    public TextRenderStates(final Widget w) {
        super();
        this.m_preRender = true;
        this.m_widget = w;
    }
    
    @Override
    public void apply(final Renderer renderer) {
        if (this.m_widget.getContainer() == null) {
            return;
        }
        if (this.m_preRender) {
            final PooledRectangle rectangle = PooledRectangle.checkout(this.m_widget.getScreenX() + this.m_widget.getAppearance().getLeftInset(), this.m_widget.getScreenY() + this.m_widget.getAppearance().getBottomInset(), this.m_widget.getAppearance().getContentWidth(), this.m_widget.getAppearance().getContentHeight());
            Xulor.getInstance().getScene().scale(rectangle);
            Graphics.getInstance().pushScissor(rectangle);
            final PooledRectangle r = Graphics.getInstance().getScissor();
            RenderStateManager.getInstance().enableScissor(true);
            RenderStateManager.getInstance().setScissorRect(r.getX(), r.getY(), r.getWidth() + 1, r.getHeight() + 1);
            RenderStateManager.getInstance().applyStates(renderer);
        }
        else {
            Graphics.getInstance().popScissor();
            final PooledRectangle r2 = Graphics.getInstance().getScissor();
            if (r2 != null) {
                RenderStateManager.getInstance().enableScissor(true);
                RenderStateManager.getInstance().setScissorRect(r2.getX(), r2.getY(), r2.getWidth() + 1, r2.getHeight() + 1);
                RenderStateManager.getInstance().applyStates(renderer);
            }
        }
        this.m_preRender = !this.m_preRender;
    }
}
