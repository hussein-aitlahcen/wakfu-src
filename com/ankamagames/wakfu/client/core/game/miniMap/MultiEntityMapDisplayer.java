package com.ankamagames.wakfu.client.core.game.miniMap;

import org.apache.log4j.*;
import com.ankamagames.framework.graphics.engine.texture.*;
import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.framework.graphics.engine.entity.*;
import com.ankamagames.xulor2.component.mesh.mapHelper.*;
import com.ankamagames.xulor2.component.mesh.*;
import com.ankamagames.framework.graphics.engine.states.*;

public class MultiEntityMapDisplayer extends MapDisplayer<EntityGroup>
{
    private static final Logger m_logger;
    private EntityGroup m_multiEntity;
    private EntitySprite m_background;
    private Pixmap m_bgPixmap;
    private Color m_bgColor;
    
    @Override
    protected EntityGroup createEntity() {
        this.m_background = EntitySprite.Factory.newPooledInstance();
        this.m_background.m_owner = this;
        this.m_multiEntity = EntityGroup.Factory.newPooledInstance();
        this.m_multiEntity.m_owner = this;
        final EntityGroup root = EntityGroup.Factory.newPooledInstance();
        root.addChild(this.m_background);
        root.addChild(this.m_multiEntity);
        return root;
    }
    
    public EntityGroup getMultiEntity() {
        return this.m_multiEntity;
    }
    
    @Override
    protected void addTransformations() {
        PixmapMapDisplayer.addTransformations(this.m_background);
    }
    
    @Override
    public void updateGeometry(final MapOverlayMesh mapMesh) {
        if (this.m_bgColor != null) {
            EntityConstructor.addGeometry(0, 0, this.m_bgPixmap.getWidth(), this.m_bgPixmap.getHeight(), this.m_bgPixmap, mapMesh.getColor(), this.m_background);
            final int width = mapMesh.getWidth();
            final int height = mapMesh.getHeight();
            this.m_background.setBounds(height / 2, -width / 2, (int)(1.7f * width), (int)(1.7f * height));
            this.m_background.getGeometry().setBlendFunc(BlendModes.One, BlendModes.InvSrcAlpha);
        }
    }
    
    @Override
    public void clear() {
        super.clear();
        this.m_multiEntity = null;
        this.m_background = null;
    }
    
    public void setBackground(final Pixmap pixmap, final Color color) {
        this.m_bgPixmap = pixmap;
        this.m_bgColor = color;
    }
    
    static {
        m_logger = Logger.getLogger((Class)MultiEntityMapDisplayer.class);
    }
}
