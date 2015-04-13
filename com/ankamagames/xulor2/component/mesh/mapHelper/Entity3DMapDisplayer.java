package com.ankamagames.xulor2.component.mesh.mapHelper;

import org.apache.log4j.*;
import com.ankamagames.xulor2.component.mesh.*;
import com.ankamagames.framework.graphics.engine.states.*;
import com.ankamagames.framework.graphics.engine.texture.*;
import com.ankamagames.framework.graphics.engine.entity.*;

public class Entity3DMapDisplayer extends MapDisplayer<Entity3D>
{
    private static final Logger m_logger;
    
    @Override
    protected Entity3D createEntity() {
        return Entity3D.Factory.newPooledInstance();
    }
    
    @Override
    protected void addTransformations() {
        PixmapMapDisplayer.addTransformations(((MapDisplayer<Entity>)this).getEntity());
    }
    
    @Override
    public void updateGeometry(final MapOverlayMesh mapMesh) {
        final Entity3D entity = this.getEntity();
        entity.clear();
        final Pixmap pixmap = mapMesh.getPixmap();
        if (pixmap == null) {
            return;
        }
        final int width = mapMesh.getWidth();
        final int height = mapMesh.getHeight();
        EntityConstructor.addGeometry(-width / 2, height / 2, width, height, pixmap, mapMesh.getColor(), entity);
        for (int i = 0; i < entity.getNumGeometries(); ++i) {
            entity.getGeometry(i).setBlendFunc(BlendModes.One, BlendModes.InvSrcAlpha);
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)Entity3DMapDisplayer.class);
    }
}
