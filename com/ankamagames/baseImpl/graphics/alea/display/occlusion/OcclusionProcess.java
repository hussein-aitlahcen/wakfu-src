package com.ankamagames.baseImpl.graphics.alea.display.occlusion;

import java.util.*;
import com.ankamagames.framework.graphics.engine.entity.*;
import com.ankamagames.framework.graphics.engine.states.*;
import com.ankamagames.baseImpl.graphics.alea.animatedElement.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.baseImpl.graphics.isometric.camera.*;
import com.ankamagames.baseImpl.graphics.alea.display.*;
import com.ankamagames.baseImpl.graphics.alea.display.occlusion.hole.*;

public abstract class OcclusionProcess
{
    protected final ArrayList<Entity> m_occluders;
    private static OcclusionProcess m_currentProcessor;
    
    public OcclusionProcess() {
        super();
        this.m_occluders = new ArrayList<Entity>(128);
    }
    
    public void reset() {
        for (int i = 0, size = this.m_occluders.size(); i < size; ++i) {
            final Entity entity = this.m_occluders.get(i);
            if (entity.getNumReferences() >= 0) {
                if (entity instanceof EntityGroup) {
                    final ArrayList<Entity> childList = entity.getChildList();
                    for (int childIndex = 0; childIndex < childList.size(); ++childIndex) {
                        final Entity child = childList.get(childIndex);
                        child.removeEffectForWorld();
                        entity.setPreRenderStates(RenderStates.m_instance);
                        entity.setPostRenderStates(RenderStates.m_instance);
                    }
                }
                else {
                    entity.removeEffectForWorld();
                    entity.setPreRenderStates(RenderStates.m_instance);
                    entity.setPostRenderStates(RenderStates.m_instance);
                }
            }
        }
        this.m_occluders.clear();
    }
    
    public void selectOccluder(final ArrayList<Entity> entities) {
        for (int numEntities = entities.size(), i = 0; i < numEntities; ++i) {
            final Entity entity = entities.get(i);
            if ((entity.m_userFlag1 & 0x1) != 0x0) {
                final int entityMaxX = entity.m_maxX;
                final int entityMaxY = entity.m_maxY;
                final int entityMinX = entity.m_minX;
                final int entityMinY = entity.m_minY;
                final float cellXY = entity.m_cellX + entity.m_cellY;
                final float minOccluderZ = entity.m_cellZ + entity.m_height * 0.666f;
                final int firstOccluder = this.m_occluders.size();
                for (int j = i + 1; j < numEntities; ++j) {
                    final Entity occluder = entities.get(j);
                    if ((occluder.m_userFlag1 & 0x2) != 0x0) {
                        if (occluder.m_minX < entityMaxX) {
                            if (occluder.m_minY < entityMaxY) {
                                if (occluder.m_maxX > entityMinX) {
                                    if (occluder.m_maxY > entityMinY) {
                                        if (occluder.m_cellZ + occluder.m_height > minOccluderZ) {
                                            if (occluder.m_cellX + occluder.m_cellY > cellXY) {
                                                this.addOccluder(entity, occluder);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                this.onProcessed(firstOccluder, entity);
            }
        }
    }
    
    public abstract EntityOcclusionRender createRenderer();
    
    protected abstract void onProcessed(final int p0, final Entity p1);
    
    public void compute(final Matrix44 cameraMatrix, final AbstractCamera isoCamera) {
    }
    
    public abstract void onDone(final AleaWorldScene p0);
    
    protected void addOccluder(final Entity entity, final Entity occluder) {
        this.m_occluders.add(occluder);
    }
    
    public static OcclusionProcess getCurrent() {
        return OcclusionProcess.m_currentProcessor;
    }
    
    public static void setCurrent(final OcclusionProcess instance) {
        OcclusionProcess.m_currentProcessor = instance;
    }
    
    static {
        OcclusionProcess.m_currentProcessor = HoleOcclusionProcess.INSTANCE;
    }
}
