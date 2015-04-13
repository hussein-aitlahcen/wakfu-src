package com.ankamagames.baseImpl.graphics.alea.display;

import com.ankamagames.framework.kernel.core.common.*;
import com.ankamagames.framework.graphics.engine.entity.*;
import com.ankamagames.framework.graphics.engine.*;
import java.util.*;

public class RenderTree extends MemoryObject implements RenderTreeInterface
{
    public static final ObjectFactory Factory;
    private ArrayList<Entity> m_entitiesBefore;
    private ArrayList<Entity> m_entitiesAfter;
    private RenderTree m_before;
    private RenderTree m_after;
    private Entity m_entity;
    private boolean m_isMultiCell;
    private float m_minCellX;
    private float m_minCellY;
    private final ArrayList<Entity> m_unsortedEntities;
    private static int m_lastLevel;
    private static final boolean DEBUG = true;
    
    private RenderTree() {
        super();
        this.m_entitiesBefore = new ArrayList<Entity>(0);
        this.m_entitiesAfter = new ArrayList<Entity>(0);
        this.m_unsortedEntities = new ArrayList<Entity>();
    }
    
    @Override
    public void clear() {
        this.m_unsortedEntities.clear();
        if (this.m_before != null) {
            this.m_before.removeReference();
            this.m_before = null;
        }
        if (this.m_after != null) {
            this.m_after.removeReference();
            this.m_after = null;
        }
        this.m_entitiesBefore.clear();
        this.m_entitiesAfter.clear();
        this.m_entity = null;
    }
    
    @Override
    public void push(final Entity entity, final int level) {
        if (this.m_entity == null) {
            this.m_entity = entity;
            this.m_isMultiCell = this.isMultiCell(this.m_entity);
            this.m_minCellX = this.m_entity.m_cellX;
            this.m_minCellY = this.m_entity.m_cellY;
            this.m_minCellX = Float.MAX_VALUE;
            this.m_minCellY = Float.MAX_VALUE;
            return;
        }
        if (!this.m_isMultiCell) {
            if (this.m_after == null) {
                if (!this.isMultiCell(entity)) {
                    this.m_entitiesAfter.add(entity);
                }
                else {
                    if (this.m_after == null) {
                        this.m_after = RenderTree.Factory.newPooledInstance();
                    }
                    this.m_after.push(entity, level + 1);
                }
            }
            else {
                this.m_after.push(entity, level + 1);
            }
            return;
        }
        if (this.canPushBefore(entity)) {
            if (this.m_before == null) {
                this.m_before = RenderTree.Factory.newPooledInstance();
            }
            this.m_before.push(entity, level + 1);
        }
        else {
            if (this.m_after == null) {
                this.m_after = RenderTree.Factory.newPooledInstance();
            }
            this.m_after.push(entity, level + 1);
        }
    }
    
    public final void toArrayList(final ArrayList<Entity> entityList, final int level) {
        for (int i = 0; i < this.m_entitiesBefore.size(); ++i) {
            entityList.add(this.m_entitiesBefore.get(i));
        }
        if (this.m_before != null) {
            this.m_before.toArrayList(entityList, level);
        }
        if (this.m_entity != null) {
            entityList.add(this.m_entity);
        }
        for (int i = 0; i < this.m_entitiesAfter.size(); ++i) {
            entityList.add(this.m_entitiesAfter.get(i));
        }
        if (level > RenderTree.m_lastLevel) {
            return;
        }
        if (this.m_after != null) {
            this.m_after.toArrayList(entityList, level + 1);
        }
    }
    
    @Override
    public void pushAtEnd(final Entity entity) {
        this.m_unsortedEntities.add(entity);
    }
    
    @Override
    public void render(final Renderer renderer) {
        this.renderRecurse(renderer);
        for (int numElements = this.m_unsortedEntities.size(), entityIndex = 0; entityIndex < numElements; ++entityIndex) {
            renderer.draw(this.m_unsortedEntities.get(entityIndex));
        }
    }
    
    @Override
    public void getAddEntities(final HashSet<Entity> entities) {
        entities.addAll((Collection<?>)this.m_entitiesAfter);
        entities.addAll((Collection<?>)this.m_entitiesBefore);
        entities.addAll((Collection<?>)this.m_unsortedEntities);
        if (this.m_before != null) {
            this.m_before.getAddEntities(entities);
        }
        if (this.m_after != null) {
            this.m_after.getAddEntities(entities);
        }
        if (this.m_entity != null) {
            entities.add(this.m_entity);
        }
    }
    
    @Override
    protected void checkout() {
    }
    
    @Override
    protected void checkin() {
        this.clear();
    }
    
    private void renderRecurse(final Renderer renderer) {
        for (int numEntitiesBefore = this.m_entitiesBefore.size(), i = 0; i < numEntitiesBefore; ++i) {
            this.m_entitiesBefore.get(i).render(renderer);
        }
        if (this.m_before != null) {
            this.m_before.renderRecurse(renderer);
        }
        if (this.m_entity != null) {
            this.m_entity.render(renderer);
        }
        for (int numEntitiesAfter = this.m_entitiesAfter.size(), j = 0; j < numEntitiesAfter; ++j) {
            this.m_entitiesAfter.get(j).render(renderer);
        }
        if (this.m_after != null) {
            this.m_after.renderRecurse(renderer);
        }
    }
    
    private boolean isMultiCell(final Entity entity) {
        return entity.m_renderRadius > 1.0f;
    }
    
    private boolean canPushBefore(final Entity entity) {
        return entity.m_cellX < this.m_entity.m_cellX || entity.m_cellY < this.m_entity.m_cellY;
    }
    
    static {
        Factory = new ObjectFactory();
        RenderTree.m_lastLevel = 1000;
    }
    
    public static final class ObjectFactory extends MemoryObject.ObjectFactory<RenderTree>
    {
        public ObjectFactory() {
            super(RenderTree.class);
        }
        
        @Override
        public RenderTree create() {
            return new RenderTree(null);
        }
    }
}
