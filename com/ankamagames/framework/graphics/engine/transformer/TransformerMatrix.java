package com.ankamagames.framework.graphics.engine.transformer;

import com.ankamagames.framework.kernel.core.maths.*;

public final class TransformerMatrix extends Transformer
{
    @Override
    public Matrix44 getMatrix() {
        return this.m_matrix;
    }
    
    @Override
    public void setIdentity() {
        this.m_matrix.setIdentity();
    }
    
    @Override
    public float getTranslationX() {
        return this.m_matrix.getBuffer()[12];
    }
    
    @Override
    public float getTranslationY() {
        return this.m_matrix.getBuffer()[13];
    }
    
    @Override
    public float getScaleX() {
        return this.m_matrix.getBuffer()[0];
    }
    
    @Override
    public float getScaleY() {
        return this.m_matrix.getBuffer()[5];
    }
    
    @Override
    public void setTranslation(final float x, final float y, final float z) {
        final float[] buffer = this.m_matrix.getBuffer();
        buffer[12] = x;
        buffer[13] = y;
        buffer[14] = z;
    }
    
    @Override
    public void setScale(final float x, final float y, final float z) {
        final float[] buffer = this.m_matrix.getBuffer();
        buffer[0] = x;
        buffer[5] = y;
        buffer[10] = z;
    }
}
