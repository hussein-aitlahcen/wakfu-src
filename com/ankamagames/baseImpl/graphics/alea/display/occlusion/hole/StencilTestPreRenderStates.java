package com.ankamagames.baseImpl.graphics.alea.display.occlusion.hole;

import com.ankamagames.framework.graphics.engine.*;
import javax.media.opengl.*;
import com.ankamagames.framework.graphics.engine.states.*;

class StencilTestPreRenderStates extends RenderStates
{
    public static final StencilTestPreRenderStates m_instance;
    
    @Override
    public void apply(final Renderer renderer) {
        final GL gl = renderer.getDevice();
        final StencilStateManager.StencilParam params = new StencilStateManager.StencilParam();
        params.setEnable(true);
        params.setMask(0);
        params.setFunc(517, 1, 1);
        params.setOp(7680);
        StencilStateManager.getInstance().pushStencil(gl, params);
    }
    
    static {
        m_instance = new StencilTestPreRenderStates();
    }
}
