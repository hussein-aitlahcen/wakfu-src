package com.ankamagames.xulor2.component.mesh;

import com.ankamagames.framework.graphics.engine.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.xulor2.graphics.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.framework.graphics.engine.states.*;

public class ContainerRenderStates extends RenderStates
{
    private final Widget m_widget;
    private boolean m_preRender;
    
    public ContainerRenderStates(final Widget w) {
        super();
        this.m_preRender = true;
        this.m_widget = w;
    }
    
    @Override
    public void apply(final Renderer renderer) {
        final Container container = this.m_widget.getContainer();
        if (container == null) {
            return;
        }
        if (this.m_preRender) {
            if (container.needsScissor()) {
                this.m_widget.setScreenPosition(this.m_widget.getScreenX(), this.m_widget.getScreenY());
                container.setScreenPosition(container.getScreenX(), container.getScreenY());
                final PooledRectangle contRect = container.getScissor(this.m_widget);
                Xulor.getInstance().getScene().scale(contRect);
                Graphics.getInstance().pushScissor(contRect);
                final PooledRectangle widgetRect = this.m_widget.getComputedScissor();
                if (widgetRect != null) {
                    Xulor.getInstance().getScene().scale(widgetRect);
                    Graphics.getInstance().pushScissor(widgetRect);
                }
                this.applyScissor(renderer);
            }
        }
        else if (container.needsScissor()) {
            this.m_widget.setScreenPosition(-1, -1);
            container.setScreenPosition(-1, -1);
            Graphics.getInstance().popScissor();
            if (this.m_widget.getScissor() != null) {
                Graphics.getInstance().popScissor();
            }
            this.applyScissor(renderer);
        }
        this.m_preRender = !this.m_preRender;
    }
    
    private void applyScissor(final Renderer renderer) {
        final PooledRectangle r = Graphics.getInstance().getScissor();
        if (r != null) {
            final RenderStateManager stateManager = RenderStateManager.getInstance();
            stateManager.enableScissor(true);
            stateManager.setScissorRect(r.getX(), r.getY(), r.getWidth() + 1, r.getHeight() + 1);
        }
    }
}
