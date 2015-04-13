package com.ankamagames.xulor2.component.mesh.mapHelper;

import com.ankamagames.framework.graphics.engine.entity.*;
import org.apache.log4j.*;
import com.ankamagames.framework.graphics.engine.texture.*;
import com.ankamagames.xulor2.component.mesh.*;

public abstract class MapDisplayer<T extends Entity>
{
    private static final Logger m_logger;
    private T m_mapBackgroundEntity;
    
    public final T getEntity() {
        return this.m_mapBackgroundEntity;
    }
    
    public final void init() {
        this.m_mapBackgroundEntity = this.createEntity();
        ((MapDisplayer)(this.m_mapBackgroundEntity.m_owner = this)).addTransformations();
    }
    
    protected abstract void addTransformations();
    
    protected abstract T createEntity();
    
    public void clear() {
        this.m_mapBackgroundEntity.removeReference();
        this.m_mapBackgroundEntity = null;
    }
    
    public void setZoneMaskTexture(final Texture texture) {
    }
    
    public abstract void updateGeometry(final MapOverlayMesh p0);
    
    static {
        m_logger = Logger.getLogger((Class)MapDisplayer.class);
    }
}
