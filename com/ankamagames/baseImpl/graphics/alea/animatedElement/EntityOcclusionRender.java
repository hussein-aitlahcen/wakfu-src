package com.ankamagames.baseImpl.graphics.alea.animatedElement;

import org.apache.log4j.*;
import com.ankamagames.framework.graphics.engine.entity.*;

public abstract class EntityOcclusionRender
{
    private static final Logger m_logger;
    protected Entity3D m_anmEntity;
    
    public Entity3D getAnmEntity() {
        return this.m_anmEntity;
    }
    
    public void reset() {
        assert this.m_anmEntity != null;
        this.m_anmEntity.removeReference();
        this.m_anmEntity = null;
    }
    
    public EntityGroup createEntity(final AnimatedElement elt) {
        final EntityGroup entity = EntityGroup.Factory.newPooledInstance();
        entity.m_owner = elt;
        assert this.m_anmEntity == null;
        entity.addChild(this.m_anmEntity = elt.createEntity());
        return entity;
    }
    
    public abstract void enableOcclusion(final boolean p0);
    
    static {
        m_logger = Logger.getLogger((Class)EntityOcclusionRender.class);
    }
}
