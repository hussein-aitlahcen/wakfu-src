package com.ankamagames.framework.graphics.engine.fx;

import com.ankamagames.framework.graphics.engine.*;
import com.ankamagames.framework.graphics.engine.entity.*;

public abstract class Technique extends Annotated
{
    protected Pass[] m_passes;
    
    protected Technique(final String name) {
        super(name);
    }
    
    public final void render(final Renderer renderer, final Entity entity) {
        for (int i = 0, m_passesLength = this.m_passes.length; i < m_passesLength; ++i) {
            this.m_passes[i].render(renderer, entity);
        }
    }
    
    public final void reset() {
        for (int i = 0, m_passesLength = this.m_passes.length; i < m_passesLength; ++i) {
            this.m_passes[i].reset();
        }
    }
}
