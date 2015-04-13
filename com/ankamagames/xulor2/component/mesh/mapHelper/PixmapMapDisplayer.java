package com.ankamagames.xulor2.component.mesh.mapHelper;

import org.apache.log4j.*;
import com.ankamagames.framework.graphics.engine.entity.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.framework.graphics.engine.transformer.*;
import com.ankamagames.xulor2.component.mesh.*;
import com.ankamagames.framework.graphics.engine.states.*;
import com.ankamagames.framework.graphics.engine.texture.*;

public class PixmapMapDisplayer extends MapDisplayer<EntitySprite>
{
    private static final Logger m_logger;
    
    @Override
    protected EntitySprite createEntity() {
        return EntitySprite.Factory.newPooledInstance();
    }
    
    @Override
    protected void addTransformations() {
        addTransformations(((MapDisplayer<Entity>)this).getEntity());
    }
    
    public static void addTransformations(final Entity entity) {
        final TransformerSRT rotation = new TransformerSRT();
        rotation.setRotation(new Quaternion(Vector3.AXIS_Z, -0.7853982f));
        rotation.setScale(0.5f * MathHelper.SQRT_2, MathHelper.SQRT_2, 1.0f);
        final TransformerSRT skew = new TransformerSRT();
        skew.setRotation(new Quaternion(Vector3.AXIS_X, -1.0471976f));
        entity.getTransformer().addTransformer(rotation);
        entity.getTransformer().addTransformer(skew);
    }
    
    @Override
    public void setZoneMaskTexture(final Texture texture) {
        this.getEntity().setTexture2(texture);
    }
    
    @Override
    public void updateGeometry(final MapOverlayMesh mapMesh) {
        final Pixmap pixmap = mapMesh.getPixmap();
        if (pixmap == null) {
            return;
        }
        final int width = mapMesh.getWidth();
        final int height = mapMesh.getHeight();
        EntityConstructor.addGeometry(-width / 2, height / 2, width, height, pixmap, mapMesh.getColor(), this.getEntity());
        this.getEntity().getGeometry().setBlendFunc(BlendModes.One, BlendModes.InvSrcAlpha);
    }
    
    static {
        m_logger = Logger.getLogger((Class)PixmapMapDisplayer.class);
    }
}
