package com.ankamagames.baseImpl.graphics.alea.display.occlusion.hole;

import org.apache.log4j.*;
import com.ankamagames.baseImpl.graphics.alea.animatedElement.*;
import com.ankamagames.framework.graphics.engine.states.*;
import com.ankamagames.baseImpl.graphics.alea.display.occlusion.*;
import com.ankamagames.framework.graphics.engine.entity.*;
import com.ankamagames.framework.graphics.engine.fx.*;
import com.ankamagames.baseImpl.graphics.isometric.camera.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.framework.graphics.engine.fx.effets.*;
import java.util.*;
import com.ankamagames.baseImpl.graphics.alea.display.*;

public class HoleOcclusionProcess extends OcclusionProcess
{
    private static final Logger m_logger;
    public static final HoleOcclusionProcess INSTANCE;
    private final Vector4 m_occludeePosition;
    private final Vector4 m_occuldeeScreenPosition;
    private final HashMap<Entity, float[]> m_occludersBounds;
    
    private HoleOcclusionProcess() {
        super();
        this.m_occludeePosition = new Vector4();
        this.m_occuldeeScreenPosition = new Vector4();
        this.m_occludersBounds = new HashMap<Entity, float[]>();
    }
    
    @Override
    public void reset() {
        super.reset();
        this.m_occludersBounds.clear();
    }
    
    @Override
    public EntityOcclusionRender createRenderer() {
        return new EntityHoleOcclusionRender();
    }
    
    @Override
    protected void onProcessed(final int firstOccluder, final Entity entity) {
        final int numOccluders = this.m_occluders.size();
        if (numOccluders - firstOccluder <= 0) {
            return;
        }
        final Effect baseEffect = EffectManager.getInstance().getBaseEffect();
        final boolean useFixedPipeline = !baseEffect.isTechniqueValide(FxConstants.ALPHAMASK_TECHNIQUE);
        if (!useFixedPipeline) {
            final int entityMaxX = entity.m_maxX;
            final int entityMaxY = entity.m_maxY;
            final int entityMinX = entity.m_minX;
            final int entityMinY = entity.m_minY;
            for (int j = firstOccluder; j < numOccluders; ++j) {
                final Entity occluder = this.m_occluders.get(j);
                float[] rect = this.m_occludersBounds.get(occluder);
                if (rect == null) {
                    rect = new float[4];
                    this.m_occludersBounds.put(occluder, rect);
                    rect[0] = entityMinX;
                    rect[1] = entityMaxX;
                    rect[2] = entityMinY;
                    rect[3] = entityMaxY;
                }
                else {
                    if (entityMinX < rect[0]) {
                        rect[0] = entityMinX;
                    }
                    if (entityMaxX > rect[1]) {
                        rect[1] = entityMaxX;
                    }
                    if (entityMinY < rect[2]) {
                        rect[2] = entityMinY;
                    }
                    if (entityMaxY > rect[3]) {
                        rect[3] = entityMaxY;
                    }
                }
            }
        }
        else {
            for (int i = firstOccluder; i < numOccluders; ++i) {
                final Entity occluder2 = this.m_occluders.get(i);
                occluder2.setPreRenderStates(StencilTestPreRenderStates.m_instance);
                occluder2.setPostRenderStates(PopStencilParamRenderState.INSTANCE);
                if (occluder2 instanceof EntityGroup) {
                    final ArrayList<Entity> childList = occluder2.getChildList();
                    for (int childIndex = 0; childIndex < childList.size(); ++childIndex) {
                        final Entity child = childList.get(childIndex);
                        child.setPreRenderStates(StencilTestPreRenderStates.m_instance);
                        child.setPostRenderStates(PopStencilParamRenderState.INSTANCE);
                    }
                }
                else {
                    occluder2.setPreRenderStates(StencilTestPreRenderStates.m_instance);
                    occluder2.setPostRenderStates(PopStencilParamRenderState.INSTANCE);
                }
            }
        }
    }
    
    @Override
    public void compute(final Matrix44 cameraMatrix, final AbstractCamera isoCamera) {
        if (this.m_occludersBounds.isEmpty()) {
            return;
        }
        final Effect baseEffect = EffectManager.getInstance().getBaseEffect();
        final int screenWidth = isoCamera.getScreenWidth();
        final int screenHeight = isoCamera.getScreenHeight();
        final float zoomFactor = isoCamera.getZoomFactor();
        final float widthFactor = zoomFactor / screenWidth;
        final float heightFactor = zoomFactor / screenHeight;
        for (final Map.Entry<Entity, float[]> entry : this.m_occludersBounds.entrySet()) {
            final Entity occluder = entry.getKey();
            final float[] rect = entry.getValue();
            float radiusX = rect[1] - rect[0] - 1.0f;
            if (radiusX < 48.0f) {
                radiusX = 48.0f;
            }
            final float radiusY = rect[3] - rect[2] - 1.0f;
            final float screenRadiusX = radiusX * widthFactor * MathHelper.SQRT_2;
            final float screenRadiusY = radiusY * heightFactor * MathHelper.SQRT_2;
            final float centerX = (rect[0] + rect[1]) / 2.0f;
            final float centerY = (rect[2] + rect[3]) / 2.0f;
            this.m_occludeePosition.set(centerX, centerY, 0.0f, 1.0f);
            cameraMatrix.transformPoint(this.m_occludeePosition, this.m_occuldeeScreenPosition);
            final Vector4 occuldeeScreenPosition = this.m_occuldeeScreenPosition;
            occuldeeScreenPosition.m_x *= 2.0f / screenWidth;
            final Vector4 occuldeeScreenPosition2 = this.m_occuldeeScreenPosition;
            occuldeeScreenPosition2.m_y *= 2.0f / screenHeight;
            final EffectParams params = new EffectParams(FxConstants.ALPHAMASK_VARS);
            params.setVector2("gAlphaMaskRadius", screenRadiusX, screenRadiusY);
            params.setVector2("gAlphaMaskPos", this.m_occuldeeScreenPosition.m_x, this.m_occuldeeScreenPosition.m_y);
            if (occluder instanceof EntityGroup) {
                final ArrayList<Entity> childList = occluder.getChildList();
                for (int childIndex = 0; childIndex < childList.size(); ++childIndex) {
                    final Entity child = childList.get(childIndex);
                    child.setEffect(baseEffect, FxConstants.ANM_ALPHAMASK_TECHNIQUE, params);
                }
            }
            else {
                occluder.setEffect(baseEffect, FxConstants.ALPHAMASK_TECHNIQUE, params);
            }
        }
    }
    
    @Override
    public void onDone(final AleaWorldScene scene) {
    }
    
    static {
        m_logger = Logger.getLogger((Class)HoleOcclusionProcess.class);
        INSTANCE = new HoleOcclusionProcess();
    }
}
