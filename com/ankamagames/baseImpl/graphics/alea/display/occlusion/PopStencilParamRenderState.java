package com.ankamagames.baseImpl.graphics.alea.display.occlusion;

import com.ankamagames.framework.graphics.engine.*;
import javax.media.opengl.*;
import com.ankamagames.framework.graphics.engine.states.*;

public class PopStencilParamRenderState extends RenderStates
{
    public static final PopStencilParamRenderState INSTANCE;
    
    @Override
    public void apply(final Renderer renderer) {
        final GL gl = renderer.getDevice();
        StencilStateManager.getInstance().popStencilParam(gl);
    }
    
    static {
        INSTANCE = new PopStencilParamRenderState();
    }
}
