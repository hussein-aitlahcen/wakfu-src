package com.ankamagames.framework.graphics.engine.entity;

import com.ankamagames.framework.kernel.core.common.*;
import com.ankamagames.framework.graphics.engine.fx.effets.*;
import com.ankamagames.framework.graphics.engine.transformer.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.framework.graphics.engine.fx.*;
import java.util.*;
import com.ankamagames.framework.graphics.engine.*;
import com.ankamagames.framework.graphics.engine.states.*;
import com.ankamagames.framework.graphics.image.*;

public abstract class Entity extends MemoryObject implements Renderable
{
    private static final RenderStates m_defaultPreRenderState;
    private static final RenderStates m_defaultPostRenderState;
    protected static Effect m_lastEffect;
    public long m_zOrder;
    public float m_cellX;
    public float m_cellY;
    public float m_cellZ;
    public float m_renderRadius;
    public float m_height;
    public int m_userFlag1;
    public int m_minX;
    public int m_maxX;
    public int m_minY;
    public int m_maxY;
    public Object m_owner;
    protected RenderStates m_preRenderStates;
    protected RenderStates m_postRenderStates;
    protected Effect m_effect;
    protected int m_techniqueCRC;
    protected EffectParams m_effectParams;
    private EntityGroup m_parent;
    private boolean m_isVisible;
    private BatchTransformer m_batchTransformer;
    public static Hidder DEBUG_HIDDER;
    
    public void setRenderRadius(final byte renderRadius) {
        this.m_renderRadius = renderRadius;
    }
    
    public boolean isVisible() {
        return this.m_isVisible;
    }
    
    public void setVisible(final boolean visible) {
        this.m_isVisible = visible;
    }
    
    public final Entity getParent() {
        return this.m_parent;
    }
    
    public final BatchTransformer getTransformer() {
        return this.m_batchTransformer;
    }
    
    @Override
    public void render(final Renderer renderer) {
        if (this.isDebugHidden() || !this.m_isVisible) {
            return;
        }
        if (this.getNumReferences() < 0) {
            return;
        }
        if (this.m_effect != null) {
            Entity.m_lastEffect = this.m_effect;
            this.m_effect.selectTechnique(this.m_techniqueCRC);
            this.m_effect.render(renderer, this, this.m_effectParams);
        }
        else {
            if (Entity.m_lastEffect != null) {
                Entity.m_lastEffect.reset();
                Entity.m_lastEffect = null;
            }
            this.renderWithoutEffect(renderer);
        }
    }
    
    public abstract void update(final float p0);
    
    public abstract void renderWithoutEffect(final Renderer p0);
    
    public final Matrix44 getMatrix() {
        return this.getTransformer().getMatrix();
    }
    
    public RenderStates getPostRenderStates() {
        return this.m_postRenderStates;
    }
    
    public RenderStates getPreRenderStates() {
        return this.m_preRenderStates;
    }
    
    public final void setPreRenderStates(final RenderStates renderStates) {
        this.m_preRenderStates = renderStates;
    }
    
    public final void setPostRenderStates(final RenderStates renderStates) {
        this.m_postRenderStates = renderStates;
    }
    
    public final Effect getEffect() {
        return this.m_effect;
    }
    
    public final EffectParams getEffectParams() {
        return this.m_effectParams;
    }
    
    public final int getTechniqueCRC() {
        return this.m_techniqueCRC;
    }
    
    public void setEffect(final Effect effect, final int techniqueCRC, final EffectParams params) {
        this.m_effect = effect;
        this.m_techniqueCRC = techniqueCRC;
        this.m_effectParams = params;
    }
    
    public final void removeEffectForWorld() {
        this.setEffect(EffectManager.getInstance().getBaseEffect(), FxConstants.TRANSFORM_TECHNIQUE, FxConstants.COLOR_SCALE_FOR_WORLD_PARAMS);
    }
    
    public final void removeEffectForUI() {
        this.setEffect(EffectManager.getInstance().getBaseEffect(), FxConstants.TRANSFORM_TECHNIQUE, FxConstants.COLOR_SCALE_FOR_UI_PARAMS);
    }
    
    protected final boolean useFixedPipeline() {
        return this.m_effect == null || this.m_effect.useFixedPipeline();
    }
    
    @Override
    protected void checkout() {
        this.m_zOrder = 0L;
        this.m_cellX = 0.0f;
        this.m_cellY = 0.0f;
        this.m_cellZ = 0.0f;
        this.m_renderRadius = 0.0f;
        this.m_height = 0.0f;
        this.m_userFlag1 = 0;
        this.m_minX = Integer.MAX_VALUE;
        this.m_maxX = Integer.MIN_VALUE;
        this.m_minY = Integer.MAX_VALUE;
        this.m_maxY = Integer.MIN_VALUE;
        this.m_preRenderStates = Entity.m_defaultPreRenderState;
        this.m_postRenderStates = Entity.m_defaultPostRenderState;
        this.m_techniqueCRC = FxConstants.TRANSFORM_TECHNIQUE;
        assert this.m_batchTransformer == null;
        this.m_batchTransformer = BatchTransformer.Factory.newPooledInstance();
        this.m_isVisible = true;
    }
    
    @Override
    protected void checkin() {
        this.m_preRenderStates = null;
        this.m_postRenderStates = null;
        this.m_effect = null;
        this.m_effectParams = null;
        if (this.m_parent != null) {
            this.m_parent.removeChild(this);
            this.m_parent = null;
        }
        this.m_batchTransformer.removeReference();
        this.m_batchTransformer = null;
    }
    
    protected final void setParent(final EntityGroup entity) {
        this.m_parent = entity;
    }
    
    protected final BatchTransformer getBatchTransformer() {
        return this.m_batchTransformer;
    }
    
    public ArrayList<Entity> getChildList() {
        return null;
    }
    
    @Override
    public boolean fill(final VertexBufferPCT vertexBuffer) {
        return true;
    }
    
    @Override
    public void applyStates(final RenderStateManager stateManager) {
    }
    
    @Override
    public int getMode() {
        return 7;
    }
    
    @Override
    public boolean visible() {
        return this.m_isVisible && this.getNumReferences() >= 0;
    }
    
    @Override
    public void draw(final Renderer renderer) {
        throw new UnsupportedOperationException("");
    }
    
    public final void setColor(final Color color) {
        this.setColor(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
    }
    
    public abstract void setColor(final float p0, final float p1, final float p2, final float p3);
    
    private boolean isDebugHidden() {
        return Entity.DEBUG_HIDDER != null && Entity.DEBUG_HIDDER.isHidden(this.m_owner);
    }
    
    static {
        m_defaultPreRenderState = new RenderStates();
        m_defaultPostRenderState = new RenderStates();
        Entity.m_lastEffect = null;
        Entity.DEBUG_HIDDER = null;
    }
    
    public interface Hidder
    {
        boolean isHidden(Object p0);
    }
}
