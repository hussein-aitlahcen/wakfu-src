package com.ankamagames.framework.graphics.engine.transformer;

import com.ankamagames.framework.kernel.core.common.*;
import com.ankamagames.framework.kernel.core.maths.*;
import java.util.*;

public class BatchTransformer extends MemoryObject
{
    public static final ObjectFactory Factory;
    private static final Matrix44 m_intermediateMatrix;
    private final ArrayList<Transformer> m_transformerList;
    private Matrix44 m_matrix;
    private boolean m_needUpdate;
    private final ArrayList<BatchTransformer> m_children;
    private BatchTransformer m_parent;
    
    private BatchTransformer() {
        super();
        this.m_transformerList = new ArrayList<Transformer>(2);
        this.m_children = new ArrayList<BatchTransformer>(1);
    }
    
    public void setIdentity(final int index) {
        this.getTransformer(index).setIdentity();
        this.setToUpdate();
    }
    
    public void setTranslation(final int index, final float tx, final float ty) {
        final Transformer transformer = this.getTransformer(index);
        if (transformer.getTranslationX() == tx && transformer.getTranslationY() == ty) {
            return;
        }
        transformer.setTranslation(tx, ty, 0.0f);
        this.setToUpdate();
    }
    
    public void setScale(final int index, final float sx, final float sy) {
        final Transformer transformer = this.getTransformer(index);
        if (transformer.getScaleX() == sx && transformer.getScaleY() == sy) {
            return;
        }
        transformer.setScale(sx, sy, 1.0f);
        this.setToUpdate();
    }
    
    public final void addChild(final BatchTransformer child) {
        this.addChild(this.m_children.size(), child);
    }
    
    public final void addChild(final int index, final BatchTransformer child) {
        assert child != null : "Child can't be null";
        child.addReference();
        child.setParent(this);
        this.m_children.add(index, child);
    }
    
    public final void removeChild(final BatchTransformer child) {
        assert child != null : "Child can't be null";
        child.setParent(null);
        if (this.m_children.remove(child)) {
            child.removeReference();
        }
    }
    
    public final void removeChild(final int index) {
        assert index > 0 && index < this.m_children.size() : "Index out of bound";
        this.removeChild(this.m_children.get(index));
    }
    
    public final void removeAllChildren() {
        for (int childrenSize = this.m_children.size(), i = 0; i < childrenSize; ++i) {
            final BatchTransformer batchTransformer = this.m_children.get(i);
            batchTransformer.setParent(null);
            batchTransformer.removeReference();
        }
        this.m_children.clear();
    }
    
    public final void addTransformer(final Transformer transformer) {
        this.m_transformerList.add(transformer);
        this.setToUpdate();
    }
    
    public final void addTransformer(final int index, final Transformer transformer) {
        this.m_transformerList.add(index, transformer);
        this.setToUpdate();
    }
    
    public final Transformer getTransformer(final int index) {
        return this.m_transformerList.get(index);
    }
    
    public final void setTransformer(final int index, final Transformer transformer) {
        this.m_transformerList.set(index, transformer);
        this.setToUpdate();
    }
    
    public final void clear() {
        this.m_parent = null;
        this.removeAllChildren();
        this.m_transformerList.clear();
        this.setToUpdate();
    }
    
    public final Matrix44 getMatrix() {
        if (!this.m_needUpdate) {
            return this.m_matrix;
        }
        final int numTransformers = this.m_transformerList.size();
        if (numTransformers == 0) {
            this.m_matrix.setIdentity();
        }
        else if (numTransformers > 4) {
            this.m_matrix.setMultiply(this.m_transformerList.get(numTransformers - 2).getMatrix(), this.m_transformerList.get(numTransformers - 1).getMatrix());
            for (int i = numTransformers - 3; i >= 0; --i) {
                BatchTransformer.m_intermediateMatrix.set(this.m_matrix);
                this.m_matrix.setMultiply(this.m_transformerList.get(i).getMatrix(), BatchTransformer.m_intermediateMatrix);
            }
        }
        else {
            switch (numTransformers) {
                case 1: {
                    this.m_matrix.set(this.m_transformerList.get(0).getMatrix());
                    break;
                }
                case 2: {
                    this.m_matrix.setMultiply(this.m_transformerList.get(0).getMatrix(), this.m_transformerList.get(1).getMatrix());
                    break;
                }
                case 3: {
                    this.m_matrix.setMultiply(this.m_transformerList.get(0).getMatrix(), this.m_transformerList.get(1).getMatrix(), this.m_transformerList.get(2).getMatrix());
                    break;
                }
                case 4: {
                    this.m_matrix.setMultiply(this.m_transformerList.get(0).getMatrix(), this.m_transformerList.get(1).getMatrix(), this.m_transformerList.get(2).getMatrix(), this.m_transformerList.get(3).getMatrix());
                    break;
                }
            }
        }
        if (this.m_parent != null) {
            if (this.m_matrix.isIdentity()) {
                this.m_matrix.set(this.m_parent.getMatrix());
            }
            else {
                this.m_matrix.setMultiply(this.m_parent.getMatrix());
            }
        }
        this.setUpToDate();
        return this.m_matrix;
    }
    
    @Override
    protected void checkout() {
        if (this.m_matrix == null) {
            this.m_matrix = Matrix44.Factory.newPooledInstance();
        }
        this.setToUpdate();
    }
    
    @Override
    protected void checkin() {
        final int childrenCount = this.m_children.size();
        this.removeAllChildren();
        if (childrenCount > 1) {
            this.m_children.trimToSize();
        }
        final int transformerCount = this.m_transformerList.size();
        this.m_transformerList.clear();
        if (transformerCount > 2) {
            this.m_transformerList.trimToSize();
        }
        this.m_parent = null;
        this.m_matrix.removeReference();
        this.m_matrix = null;
    }
    
    private void setParent(final BatchTransformer parent) {
        this.m_parent = parent;
        this.setToUpdate();
    }
    
    public void setToUpdate() {
        if (this.m_needUpdate) {
            return;
        }
        this.m_needUpdate = true;
        for (int size = this.m_children.size(), i = 0; i < size; ++i) {
            this.m_children.get(i).setToUpdate();
        }
    }
    
    private void setUpToDate() {
        this.m_needUpdate = false;
        assert !this.m_parent.m_needUpdate;
    }
    
    static {
        Factory = new ObjectFactory();
        m_intermediateMatrix = Matrix44.Factory.newInstance();
    }
    
    public static final class ObjectFactory extends MemoryObject.ObjectFactory<BatchTransformer>
    {
        public ObjectFactory() {
            super(BatchTransformer.class);
        }
        
        @Override
        public BatchTransformer create() {
            return new BatchTransformer(null);
        }
    }
}
