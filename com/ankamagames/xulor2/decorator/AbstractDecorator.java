package com.ankamagames.xulor2.decorator;

import com.ankamagames.xulor2.decorator.mesh.*;
import com.ankamagames.framework.graphics.engine.entity.*;
import java.awt.*;

public abstract class AbstractDecorator extends AbstractStateAppearanceElement implements Decorator
{
    protected boolean m_dirty;
    
    @Override
    public boolean isDirty() {
        return this.m_dirty;
    }
    
    @Override
    public abstract AbstractDecoratorMesh getMesh();
    
    @Override
    public abstract Entity getEntity();
    
    @Override
    public void updateDecorator(final Dimension size, final Insets margin, final Insets border, final Insets padding) {
        this.getMesh().updateVertex(size, margin, border, padding);
    }
    
    @Override
    public void onCheckOut() {
        super.onCheckOut();
        this.m_dirty = false;
    }
}
