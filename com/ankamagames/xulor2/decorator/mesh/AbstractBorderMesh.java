package com.ankamagames.xulor2.decorator.mesh;

import org.apache.log4j.*;
import java.awt.*;

public abstract class AbstractBorderMesh extends AbstractDecoratorMesh
{
    private static Logger m_logger;
    protected Insets m_insets;
    
    public AbstractBorderMesh() {
        super();
        this.m_insets = new Insets(0, 0, 0, 0);
    }
    
    public Insets getInsets() {
        return this.m_insets;
    }
    
    public void setInsets(final Insets insets) {
        this.m_insets.top = insets.top;
        this.m_insets.bottom = insets.bottom;
        this.m_insets.left = insets.left;
        this.m_insets.right = insets.right;
    }
    
    @Override
    public abstract void updateVertex(final Dimension p0, final Insets p1, final Insets p2, final Insets p3);
    
    static {
        AbstractBorderMesh.m_logger = Logger.getLogger((Class)AbstractBorderMesh.class);
    }
}
