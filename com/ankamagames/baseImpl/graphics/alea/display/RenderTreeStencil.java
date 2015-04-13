package com.ankamagames.baseImpl.graphics.alea.display;

import com.ankamagames.framework.kernel.core.common.*;
import org.apache.log4j.*;
import com.ankamagames.framework.graphics.engine.entity.*;
import com.ankamagames.framework.graphics.engine.*;
import java.util.*;
import javax.media.opengl.*;
import com.ankamagames.framework.graphics.engine.states.*;

public class RenderTreeStencil extends MemoryObject implements RenderTreeInterface
{
    public static final ObjectFactory Factory;
    protected static final Logger m_logger;
    private Entity m_entity;
    private final ArrayList<RenderTreeStencil> m_mask;
    private final ArrayList<RenderTreeStencil> m_over;
    private final ArrayList<RenderTreeStencil> m_maskChild;
    private final ArrayList<Entity> m_entitiesAfter;
    private static short m_stencilValue;
    private RenderTreeStencil m_child;
    private final ArrayList<Entity> m_unsortedEntities;
    
    public RenderTreeStencil() {
        super();
        this.m_mask = new ArrayList<RenderTreeStencil>(1);
        this.m_over = new ArrayList<RenderTreeStencil>(1);
        this.m_maskChild = new ArrayList<RenderTreeStencil>(1);
        this.m_entitiesAfter = new ArrayList<Entity>();
        this.m_unsortedEntities = new ArrayList<Entity>();
    }
    
    @Override
    public void clear() {
        this.m_unsortedEntities.clear();
        this.clearRecurse();
    }
    
    @Override
    public void push(final Entity entity, final int level) {
        if (this.m_entity == null) {
            this.m_entity = entity;
            return;
        }
        final RenderTreeStencil renderTree = RenderTreeStencil.Factory.newPooledInstance();
        renderTree.m_entity = entity;
        this.push(renderTree);
    }
    
    @Override
    public void pushAtEnd(final Entity entity) {
        this.m_unsortedEntities.add(entity);
    }
    
    @Override
    public void render(final Renderer renderer) {
        RenderStateManager.getInstance().enableScissor(false);
        renderer.updateMatrix();
        this.renderRecurse(renderer);
        for (int numElements = this.m_unsortedEntities.size(), entityIndex = 0; entityIndex < numElements; ++entityIndex) {
            renderer.draw(this.m_unsortedEntities.get(entityIndex));
        }
    }
    
    @Override
    public void getAddEntities(final HashSet<Entity> entities) {
        entities.addAll((Collection<?>)this.m_entitiesAfter);
        entities.addAll((Collection<?>)this.m_unsortedEntities);
        if (this.m_child != null) {
            this.m_child.getAddEntities(entities);
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
    
    private void clearRecurse() {
        this.m_mask.clear();
        this.m_over.clear();
        this.m_maskChild.clear();
        this.m_entitiesAfter.clear();
        this.m_entity = null;
        this.m_unsortedEntities.clear();
        if (this.m_child != null) {
            this.m_child.clearRecurse();
            this.m_child.removeReference();
            this.m_child = null;
        }
        RenderTreeStencil.m_stencilValue = 254;
    }
    
    private void renderRecurse(final Renderer renderer) {
        if (this.m_entity == null) {
            return;
        }
        final GL gl = renderer.getDevice();
        final int maskSize = this.m_mask.size();
        final StencilStateManager stencilManager = StencilStateManager.getInstance();
        if (maskSize != 0) {
            StencilStateManager.StencilParam params = new StencilStateManager.StencilParam();
            params.setMask(-2);
            params.setEnable(true);
            params.setOp(7681);
            params.setFunc(512, 2, -1);
            params.setColorMask(false);
            stencilManager.pushStencil(gl, params);
            for (int i = 0; i < maskSize; ++i) {
                final RenderTreeStencil renderTree = this.m_mask.get(i);
                renderer.draw(renderTree.m_entity);
                for (int maskChildsCount = renderTree.m_maskChild.size(), childIndex = 0; childIndex < maskChildsCount; ++childIndex) {
                    final RenderTreeStencil childTree = renderTree.m_maskChild.get(childIndex);
                    renderer.draw(childTree.m_entity);
                }
            }
            params = new StencilStateManager.StencilParam();
            params.setEnable(true);
            params.setMask(0);
            params.setFunc(517, 2, -1);
            params.setOp(7680);
            params.setColorMask(true);
            stencilManager.pushStencil(gl, params);
            renderer.draw(this.m_entity);
            stencilManager.popStencilParam(gl, 2);
        }
        else {
            final int overSize = this.m_over.size();
            if (overSize != 0) {
                renderer.draw(this.m_entity);
                final int maskChildsCount2 = this.m_maskChild.size();
                StencilStateManager.StencilParam params2 = new StencilStateManager.StencilParam();
                params2.setEnable(true);
                params2.setMask(-2);
                params2.setOp(7681);
                params2.setFunc(512, RenderTreeStencil.m_stencilValue, -1);
                params2.setColorMask(false);
                stencilManager.pushStencil(gl, params2);
                renderer.draw(this.m_entity);
                for (int childIndex2 = 0; childIndex2 < maskChildsCount2; ++childIndex2) {
                    renderer.draw(this.m_maskChild.get(childIndex2).m_entity);
                }
                params2 = new StencilStateManager.StencilParam();
                params2.setEnable(false);
                params2.setMask(-2);
                params2.setOp(7681);
                params2.setFunc(512, 0, -1);
                params2.setColorMask(false);
                stencilManager.pushStencil(gl, params2);
                for (int overIndex = 0; overIndex < overSize; ++overIndex) {
                    int startIndex = -1;
                    final RenderTreeStencil over = this.m_over.get(overIndex);
                    final int overMaskSize = over.m_mask.size();
                    for (int maskIndex = 0; maskIndex < overMaskSize; ++maskIndex) {
                        if (over.m_mask.get(maskIndex) == this) {
                            startIndex = maskIndex + 1;
                            break;
                        }
                    }
                    if (startIndex != -1) {
                        for (int maskIndex = startIndex; maskIndex < overMaskSize; ++maskIndex) {
                            final RenderTreeStencil maskTree = over.m_mask.get(maskIndex);
                            renderer.draw(maskTree.m_entity);
                            for (int maskTreeChildsCount = maskTree.m_maskChild.size(), childIndex3 = 0; childIndex3 < maskTreeChildsCount; ++childIndex3) {
                                renderer.draw(maskTree.m_maskChild.get(childIndex3).m_entity);
                            }
                        }
                    }
                }
                params2 = new StencilStateManager.StencilParam();
                params2.setEnable(true);
                params2.setMask(0);
                params2.setOp(7680);
                params2.setFunc(514, RenderTreeStencil.m_stencilValue, -2);
                params2.setColorMask(true);
                stencilManager.pushStencil(gl, params2);
                for (int j = 0; j < overSize; ++j) {
                    final RenderTreeStencil renderTree2 = this.m_over.get(j);
                    renderer.draw(renderTree2.m_entity);
                }
                RenderTreeStencil.m_stencilValue -= 2;
                if (RenderTreeStencil.m_stencilValue <= 2) {
                    RenderTreeStencil.m_stencilValue = 254;
                }
                stencilManager.popStencilParam(gl, 3);
            }
            else {
                renderer.draw(this.m_entity);
            }
        }
        for (int numEntities = this.m_entitiesAfter.size(), entityIndex = 0; entityIndex < numEntities; ++entityIndex) {
            this.m_entitiesAfter.get(entityIndex).render(renderer);
        }
        if (this.m_child != null) {
            this.m_child.renderRecurse(renderer);
        }
    }
    
    private void push(final RenderTreeStencil renderTree) {
        final Entity entity = renderTree.m_entity;
        if (this.m_entity.m_renderRadius > 1.0f) {
            if (entity.m_renderRadius <= 1.0f) {
                if (isInRenderRadius(entity, this.m_entity, this.m_entity.m_renderRadius) && isUnder(entity, this.m_entity)) {
                    for (int maskSize = this.m_mask.size(), i = 0; i < maskSize; ++i) {
                        final RenderTreeStencil cell = this.m_mask.get(i);
                        if (cell.m_entity.m_cellX == entity.m_cellX) {
                            if (cell.m_entity.m_cellY == entity.m_cellY) {
                                renderTree.m_maskChild.addAll(cell.m_maskChild);
                                renderTree.m_maskChild.add(cell);
                                cell.m_maskChild.clear();
                                cell.m_over.remove(this);
                                this.m_mask.remove(i);
                                break;
                            }
                        }
                    }
                    renderTree.m_over.add(this);
                    this.m_mask.add(renderTree);
                }
            }
        }
        if (this.m_child == null) {
            if (entity.m_renderRadius > 1.0f || renderTree.m_mask.size() != 0 || renderTree.m_over.size() != 0) {
                this.m_child = renderTree;
            }
            else {
                this.m_entitiesAfter.add(entity);
                renderTree.removeReference();
            }
        }
        else {
            this.m_child.push(renderTree);
        }
    }
    
    private static boolean isInRenderRadius(final Entity entity, final Entity entity2, final float renderRadius) {
        return Math.abs(entity.m_cellX - entity2.m_cellX) < renderRadius && Math.abs(entity.m_cellY - entity2.m_cellY) < renderRadius;
    }
    
    private static boolean isUnder(final Entity entity, final Entity entity2) {
        if (entity.m_cellX == entity2.m_cellX && entity.m_cellY == entity2.m_cellY) {
            return entity.m_cellZ < entity2.m_cellZ + entity2.m_height;
        }
        return entity.m_cellZ + entity.m_height <= entity2.m_cellZ;
    }
    
    public Entity getEntity() {
        return this.m_entity;
    }
    
    public RenderTreeStencil getChild() {
        return this.m_child;
    }
    
    public ArrayList<RenderTreeStencil> getMaskChild() {
        return this.m_maskChild;
    }
    
    public ArrayList<RenderTreeStencil> getOver() {
        return this.m_over;
    }
    
    public ArrayList<RenderTreeStencil> getMask() {
        return this.m_mask;
    }
    
    public ArrayList<Entity> getEntitiesAfter() {
        return this.m_entitiesAfter;
    }
    
    static {
        Factory = new ObjectFactory();
        m_logger = Logger.getLogger((Class)RenderTreeStencil.class);
        RenderTreeStencil.m_stencilValue = 1;
    }
    
    public static final class ObjectFactory extends MemoryObject.ObjectFactory<RenderTreeStencil>
    {
        public ObjectFactory() {
            super(RenderTreeStencil.class);
        }
        
        @Override
        public RenderTreeStencil create() {
            return new RenderTreeStencil();
        }
    }
}
