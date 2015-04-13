package com.ankamagames.framework.graphics.engine.geometry;

import com.ankamagames.framework.kernel.core.common.*;
import com.ankamagames.framework.graphics.engine.states.*;
import com.ankamagames.framework.graphics.engine.material.*;
import com.ankamagames.framework.graphics.engine.*;

public abstract class Geometry extends MemoryObject
{
    protected BlendModes m_source;
    protected BlendModes m_destination;
    
    public final void setBlendFunc(final BlendModes source, final BlendModes destination) {
        this.m_source = source;
        this.m_destination = destination;
    }
    
    public final BlendModes getSourceBlend() {
        return this.m_source;
    }
    
    public final BlendModes getDestinationBlend() {
        return this.m_destination;
    }
    
    public abstract void setColor(final float p0, final float p1, final float p2, final float p3);
    
    public void applyMaterial(final Material material) {
    }
    
    public abstract void update(final float p0);
    
    public abstract void render(final Renderer p0);
    
    @Override
    protected void checkout() {
        this.setBlendFunc(BlendModes.SrcAlpha, BlendModes.InvSrcAlpha);
    }
}
