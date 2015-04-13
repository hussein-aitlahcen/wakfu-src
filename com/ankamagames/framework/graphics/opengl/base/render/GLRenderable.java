package com.ankamagames.framework.graphics.opengl.base.render;

import javax.media.opengl.*;

public interface GLRenderable
{
    void init(GLAutoDrawable p0);
    
    void setFrustumSize(int p0, int p1);
    
    void process(int p0);
    
    void display(GL p0);
}
