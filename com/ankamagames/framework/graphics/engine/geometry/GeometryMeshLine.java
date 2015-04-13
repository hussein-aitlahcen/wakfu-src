package com.ankamagames.framework.graphics.engine.geometry;

public abstract class GeometryMeshLine extends GeometryMesh
{
    protected float m_lineWidth;
    protected boolean m_enableLineStipple;
    
    protected GeometryMeshLine() {
        super();
        this.m_lineWidth = 1.0f;
    }
    
    public float getLineWidth() {
        return this.m_lineWidth;
    }
    
    public void setLineWidth(final float lineWidth) {
        this.m_lineWidth = lineWidth;
    }
    
    public boolean isEnableLineStipple() {
        return this.m_enableLineStipple;
    }
    
    public void setEnableLineStipple(final boolean enableLineStipple) {
        this.m_enableLineStipple = enableLineStipple;
    }
    
    @Override
    protected void checkout() {
        super.checkout();
        this.m_lineWidth = 1.0f;
        this.m_enableLineStipple = false;
    }
}
