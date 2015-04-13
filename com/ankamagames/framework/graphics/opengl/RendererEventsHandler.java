package com.ankamagames.framework.graphics.opengl;

import javax.media.opengl.*;

public interface RendererEventsHandler
{
    void onInit(GLAutoDrawable p0);
    
    void onDisplay(GLAutoDrawable p0);
    
    void onReshape(GLAutoDrawable p0, int p1, int p2, int p3, int p4);
    
    void onDisplayChanged(GLAutoDrawable p0, boolean p1, boolean p2);
}
