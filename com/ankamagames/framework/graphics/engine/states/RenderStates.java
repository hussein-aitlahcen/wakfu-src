package com.ankamagames.framework.graphics.engine.states;

import com.ankamagames.framework.graphics.engine.*;

public class RenderStates
{
    public static final RenderStates m_instance;
    
    public void apply(final Renderer renderer) {
    }
    
    static {
        m_instance = new RenderStates();
    }
}
