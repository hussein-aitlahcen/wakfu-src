package com.ankamagames.baseImpl.graphics.alea.display.occlusion.hole;

import org.apache.log4j.*;
import com.ankamagames.baseImpl.graphics.alea.animatedElement.*;
import com.ankamagames.framework.graphics.engine.opengl.*;
import com.ankamagames.framework.graphics.engine.*;
import com.ankamagames.framework.graphics.engine.material.*;
import com.ankamagames.framework.graphics.engine.geometry.*;
import com.ankamagames.framework.graphics.engine.states.*;
import com.ankamagames.baseImpl.graphics.alea.display.occlusion.*;
import com.ankamagames.framework.graphics.engine.entity.*;
import com.ankamagames.framework.graphics.engine.fx.*;

class EntityHoleOcclusionRender extends EntityOcclusionRender
{
    private static final Logger m_logger;
    private Entity3D m_maskEntity;
    
    @Override
    public void reset() {
        super.reset();
        this.m_maskEntity.removeReference();
        this.m_maskEntity = null;
    }
    
    @Override
    public EntityGroup createEntity(final AnimatedElement elt) {
        final EntityGroup entity = super.createEntity(elt);
        final GLGeometrySprite maskAlphaGeom = GLGeometrySprite.Factory.newPooledInstance();
        final int visualHeight = 48 + elt.getVisualHeight() * 8;
        maskAlphaGeom.setBounds(visualHeight - 16, -24.0f, 48, visualHeight);
        maskAlphaGeom.setColor(0.5f, 0.5f, 0.5f, 1.0f);
        maskAlphaGeom.setTextureCoordinates(0.0f, 0.0f, 1.0f, 1.0f);
        assert this.m_maskEntity == null;
        (this.m_maskEntity = Entity3D.Factory.newPooledInstance()).addTexturedGeometry(maskAlphaGeom, Engine.getInstance().m_maskAlpha, Material.WHITE_NO_SPECULAR);
        maskAlphaGeom.removeReference();
        this.m_maskEntity.setEffect(EffectManager.getInstance().getBaseEffect(), FxConstants.TRANSFORM_TECHNIQUE, FxConstants.COLOR_SCALE_FOR_WORLD_PARAMS);
        this.m_maskEntity.setPreRenderStates(StencilWritePreRenderStates.m_instance);
        this.m_maskEntity.setPostRenderStates(PopStencilParamRenderState.INSTANCE);
        this.m_maskEntity.setVisible(false);
        entity.addChild(this.m_maskEntity);
        return entity;
    }
    
    @Override
    public void enableOcclusion(final boolean alphaMaskEnabled) {
        if (alphaMaskEnabled) {
            final Effect baseEffect = EffectManager.getInstance().getBaseEffect();
            this.m_maskEntity.setVisible(!baseEffect.isTechniqueValide(FxConstants.ALPHAMASK_TECHNIQUE));
        }
        else {
            this.m_maskEntity.setVisible(false);
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)EntityHoleOcclusionRender.class);
    }
}
