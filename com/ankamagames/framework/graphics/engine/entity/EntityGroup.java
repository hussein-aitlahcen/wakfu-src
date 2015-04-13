package com.ankamagames.framework.graphics.engine.entity;

import com.ankamagames.framework.graphics.engine.*;
import java.util.*;
import com.ankamagames.framework.kernel.core.common.*;

public class EntityGroup extends Entity
{
    private static final int DEFAULT_NUM_CHILDREN = 2;
    public static final ObjectFactory Factory;
    protected final ArrayList<Entity> m_childList;
    
    private EntityGroup() {
        super();
        this.m_childList = new ArrayList<Entity>(2);
    }
    
    @Override
    public void update(final float timeIncrement) {
        for (int childListSize = this.m_childList.size(), i = 0; i < childListSize; ++i) {
            this.m_childList.get(i).update(timeIncrement);
        }
    }
    
    @Override
    public void render(final Renderer renderer) {
        if (!this.isVisible()) {
            return;
        }
        this.renderWithoutEffect(renderer);
    }
    
    @Override
    public void renderWithoutEffect(final Renderer renderer) {
        final int size = this.m_childList.size();
        if (size == 0) {
            return;
        }
        this.m_preRenderStates.apply(renderer);
        for (int i = 0; i < size; ++i) {
            this.m_childList.get(i).render(renderer);
        }
        this.m_postRenderStates.apply(renderer);
    }
    
    @Override
    public void setVisible(final boolean visible) {
        super.setVisible(visible);
        for (final Entity child : this.m_childList) {
            child.setVisible(visible);
        }
    }
    
    @Override
    public void setColor(final float r, final float g, final float b, final float a) {
        for (int size = this.m_childList.size(), i = 0; i < size; ++i) {
            this.m_childList.get(i).setColor(r, g, b, a);
        }
    }
    
    @Override
    public final ArrayList<Entity> getChildList() {
        return this.m_childList;
    }
    
    public void setChild(final int i, final Entity child) {
        assert child != null : "It's forbidden to add a null value as a child";
        child.setParent(this);
        final Entity old = this.m_childList.set(i, child);
        child.addReference();
        this.getBatchTransformer().addChild(i, child.getTransformer());
        if (old != null) {
            this.getBatchTransformer().removeChild(old.getTransformer());
            old.removeReference();
        }
    }
    
    public final void addChild(final Entity child) {
        assert child != null : "It's forbidden to add a null value as a child";
        child.setParent(this);
        this.m_childList.add(child);
        child.addReference();
        this.getBatchTransformer().addChild(child.getTransformer());
    }
    
    public final void removeChild(final Entity child) {
        assert child != null : "null value can't be removed from childList";
        child.setParent(null);
        if (this.m_childList.remove(child)) {
            child.removeReference();
        }
        this.getBatchTransformer().removeChild(child.getTransformer());
    }
    
    public final void removeAllChildren() {
        for (int i = this.m_childList.size() - 1; i >= 0; --i) {
            final Entity entity = this.m_childList.get(i);
            entity.setParent(null);
            entity.removeReference();
        }
        this.m_childList.clear();
        this.getBatchTransformer().removeAllChildren();
    }
    
    @Override
    protected void checkin() {
        this.removeAllChildren();
        super.checkin();
    }
    
    public void computeBounds() {
        this.m_minX = Integer.MAX_VALUE;
        this.m_minY = Integer.MAX_VALUE;
        this.m_maxX = Integer.MIN_VALUE;
        this.m_maxY = Integer.MIN_VALUE;
        for (final Entity child : this.m_childList) {
            if (child.m_minX < this.m_minX) {
                this.m_minX = child.m_minX;
            }
            if (child.m_minY < this.m_minY) {
                this.m_minY = child.m_minY;
            }
            if (child.m_maxX > this.m_maxX) {
                this.m_maxX = child.m_maxX;
            }
            if (child.m_maxY > this.m_maxY) {
                this.m_maxY = child.m_maxY;
            }
        }
    }
    
    static {
        Factory = new ObjectFactory();
    }
    
    public static final class ObjectFactory extends MemoryObject.ObjectFactory<EntityGroup>
    {
        public ObjectFactory() {
            super(EntityGroup.class);
        }
        
        @Override
        public EntityGroup create() {
            return new EntityGroup(null);
        }
    }
}
