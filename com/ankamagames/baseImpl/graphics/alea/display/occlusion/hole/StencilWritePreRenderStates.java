package com.ankamagames.baseImpl.graphics.alea.display.occlusion.hole;

import com.ankamagames.framework.graphics.engine.*;
import javax.media.opengl.*;
import com.ankamagames.framework.graphics.engine.states.*;

class StencilWritePreRenderStates extends RenderStates
{
    public static final StencilWritePreRenderStates m_instance;
    
    @Override
    public void apply(final Renderer renderer) {
        final GL gl = renderer.getDevice();
        final StencilStateManager.StencilParam params = new StencilStateManager.StencilParam();
        params.setEnable(true);
        params.setMask(1);
        params.setFunc(512, 1, -1);
        params.setOp(7681);
        params.setColorMask(false);
        StencilStateManager.getInstance().pushStencil(gl, params);
    }
    
    static {
        m_instance = new StencilWritePreRenderStates();
    }
}
