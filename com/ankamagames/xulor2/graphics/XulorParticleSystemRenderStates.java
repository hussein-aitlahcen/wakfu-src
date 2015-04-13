package com.ankamagames.xulor2.graphics;

import com.ankamagames.framework.graphics.engine.*;
import com.ankamagames.framework.graphics.engine.states.*;

public class XulorParticleSystemRenderStates extends RenderStates
{
    private boolean m_useParentScissor;
    private boolean m_preRenderStates;
    
    public XulorParticleSystemRenderStates() {
        super();
        this.m_preRenderStates = true;
    }
    
    @Override
    public void apply(final Renderer renderer) {
        if (!this.m_useParentScissor) {
            RenderStateManager.getInstance().enableScissor(!this.m_preRenderStates);
        }
    }
    
    public final void setUseParentScissor(final boolean useParentScissor) {
        this.m_useParentScissor = useParentScissor;
    }
}
