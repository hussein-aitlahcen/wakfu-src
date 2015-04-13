package com.ankamagames.framework.graphics.engine.particleSystem.lightColorHelper;

import org.apache.log4j.*;
import com.ankamagames.framework.graphics.engine.*;

public class ImmediateColor implements ColorHelper
{
    private static final Logger m_logger;
    private static final ImmediateColor m_instance;
    
    public static ImmediateColor getInstance() {
        return ImmediateColor.m_instance;
    }
    
    @Override
    public void applyImmediate(final VertexBufferPCT vb, final float[] particleGeomColor, final int length) {
        vb.addColors(particleGeomColor, length);
    }
    
    @Override
    public void applyDelayed(final float[] colors) {
    }
    
    static {
        m_logger = Logger.getLogger((Class)ImmediateColor.class);
        m_instance = new ImmediateColor();
    }
}
