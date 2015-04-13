package com.ankamagames.framework.graphics.engine;

import com.ankamagames.framework.graphics.engine.states.*;

public interface Renderable
{
    void draw(Renderer p0);
    
    boolean visible();
    
    boolean fill(VertexBufferPCT p0);
    
    void applyStates(RenderStateManager p0);
    
    int getMode();
    
    void render(Renderer p0);
}
