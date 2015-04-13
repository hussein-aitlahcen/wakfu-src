package com.ankamagames.framework.graphics.engine.transformer;

import com.ankamagames.framework.kernel.core.maths.*;

public abstract class Transformer
{
    protected final Matrix44 m_matrix;
    
    protected Transformer() {
        super();
        this.m_matrix = Matrix44.Factory.newInstance();
    }
    
    public abstract Matrix44 getMatrix();
    
    public abstract void setIdentity();
    
    public abstract float getTranslationX();
    
    public abstract float getTranslationY();
    
    public abstract float getScaleX();
    
    public abstract float getScaleY();
    
    public abstract void setTranslation(final float p0, final float p1, final float p2);
    
    public abstract void setScale(final float p0, final float p1, final float p2);
}
