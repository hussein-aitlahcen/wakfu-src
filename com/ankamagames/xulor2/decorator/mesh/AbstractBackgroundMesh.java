package com.ankamagames.xulor2.decorator.mesh;

import org.apache.log4j.*;
import java.awt.*;

public abstract class AbstractBackgroundMesh extends AbstractDecoratorMesh
{
    private static Logger m_logger;
    
    @Override
    public abstract void updateVertex(final Dimension p0, final Insets p1, final Insets p2, final Insets p3);
    
    static {
        AbstractBackgroundMesh.m_logger = Logger.getLogger((Class)AbstractBackgroundMesh.class);
    }
}
