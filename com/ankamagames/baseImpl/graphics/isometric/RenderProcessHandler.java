package com.ankamagames.baseImpl.graphics.isometric;

public interface RenderProcessHandler<HandlerScene extends IsoWorldScene>
{
    void process(HandlerScene p0, int p1);
    
    void prepareBeforeRendering(HandlerScene p0, float p1, float p2);
}
