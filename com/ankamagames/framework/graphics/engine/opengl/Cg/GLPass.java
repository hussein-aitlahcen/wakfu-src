package com.ankamagames.framework.graphics.engine.opengl.Cg;

import com.ankamagames.framework.graphics.engine.fx.*;
import com.ankamagames.framework.graphics.engine.*;
import com.ankamagames.framework.graphics.engine.entity.*;
import com.sun.opengl.cg.*;
import com.ankamagames.framework.graphics.engine.states.*;

public class GLPass extends Pass
{
    private static CGpass PREVIOUS_PASS;
    private final CGpass m_pass;
    
    public GLPass(final CGpass cgPass) {
        super();
        this.m_pass = cgPass;
    }
    
    @Override
    public final void render(final Renderer renderer, final Entity entity) {
        if (GLPass.PREVIOUS_PASS != this.m_pass) {
            this.reset();
            CgGL.cgSetPassState(this.m_pass);
            GLPass.PREVIOUS_PASS = this.m_pass;
        }
        entity.renderWithoutEffect(renderer);
    }
    
    @Override
    public final void reset() {
        if (GLPass.PREVIOUS_PASS == null) {
            return;
        }
        CgGL.cgResetPassState(GLPass.PREVIOUS_PASS);
        RenderStateManager.getInstance().reset();
        GLPass.PREVIOUS_PASS = null;
    }
}
