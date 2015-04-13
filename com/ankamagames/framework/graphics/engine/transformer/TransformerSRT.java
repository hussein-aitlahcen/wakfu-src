package com.ankamagames.framework.graphics.engine.transformer;

import com.ankamagames.framework.kernel.core.maths.*;

public final class TransformerSRT extends Transformer
{
    private final Vector4 m_scale;
    private final Vector4 m_translation;
    private final Quaternion m_rotation;
    private final Matrix44 m_additionalTransform;
    private boolean m_needUpdate;
    
    public TransformerSRT() {
        super();
        this.m_scale = new Vector4();
        this.m_rotation = new Quaternion();
        this.m_translation = new Vector4();
        this.m_additionalTransform = Matrix44.getIdentity();
        this.setIdentity();
    }
    
    @Override
    public void setIdentity() {
        this.m_scale.set(1.0f, 1.0f, 1.0f, 1.0f);
        this.m_translation.set(0.0f, 0.0f, 0.0f, 1.0f);
        this.m_rotation.setIdentity();
        this.m_matrix.setIdentity();
        this.m_additionalTransform.setIdentity();
        this.m_needUpdate = false;
    }
    
    @Override
    public Matrix44 getMatrix() {
        this.update();
        return this.m_matrix;
    }
    
    @Override
    public float getTranslationX() {
        return this.m_translation.m_x;
    }
    
    @Override
    public float getTranslationY() {
        return this.m_translation.m_y;
    }
    
    @Override
    public float getScaleX() {
        return this.m_scale.m_x;
    }
    
    @Override
    public float getScaleY() {
        return this.m_scale.m_y;
    }
    
    public void setTranslation(final Vector4 t) {
        this.m_translation.set(t);
        this.m_needUpdate = true;
    }
    
    @Override
    public void setTranslation(final float x, final float y, final float z) {
        this.m_translation.set(x, y, z);
        this.m_needUpdate = true;
    }
    
    public void translate(final Vector4 t) {
        this.m_translation.setAdd(t);
        this.m_needUpdate = true;
    }
    
    public void translate(final float x, final float y, final float z) {
        this.m_translation.setAdd(x, y, z, 0.0f);
        this.m_needUpdate = true;
    }
    
    public Vector4 getTranslation() {
        return new Vector4(this.m_translation);
    }
    
    public Vector4 getTranslationConst() {
        return this.m_translation;
    }
    
    public void setRotation(final Quaternion q) {
        this.m_rotation.set(q);
        this.m_needUpdate = true;
    }
    
    public void rotate(final Quaternion q) {
        this.m_rotation.setMupltiply(q);
        this.m_needUpdate = true;
    }
    
    public Quaternion getRotation() {
        return new Quaternion(this.m_rotation);
    }
    
    public Quaternion getRotationConst() {
        return this.m_rotation;
    }
    
    public void setScale(final Vector4 s) {
        this.m_scale.set(s);
        this.m_needUpdate = true;
    }
    
    @Override
    public void setScale(final float x, final float y, final float z) {
        this.m_scale.set(x, y, z);
        this.m_needUpdate = true;
    }
    
    public void scale(final Vector4 s) {
        this.m_scale.setAdd(s);
        this.m_needUpdate = true;
    }
    
    public Vector4 getScale() {
        return new Vector4(this.m_scale);
    }
    
    public Vector4 getScaleConst() {
        return this.m_scale;
    }
    
    public Matrix44 getAdditionalTransform() {
        return this.m_additionalTransform;
    }
    
    public void setToUpdate() {
        this.m_needUpdate = true;
    }
    
    private void update() {
        if (!this.m_needUpdate) {
            return;
        }
        this.m_matrix.set(this.m_rotation);
        if (!this.m_additionalTransform.isIdentity()) {
            this.m_matrix.setMultiply(this.m_additionalTransform);
        }
        this.m_matrix.setTranslation(this.m_translation);
        this.m_matrix.setScale(this.m_scale);
        this.m_needUpdate = false;
    }
    
    public void set(final TransformerSRT t) {
        this.m_rotation.set(t.m_rotation);
        this.m_scale.set(t.m_scale);
        this.m_translation.set(t.m_translation);
        this.m_matrix.set(t.m_matrix);
        this.m_additionalTransform.set(t.m_additionalTransform);
        this.m_needUpdate = t.m_needUpdate;
    }
}
