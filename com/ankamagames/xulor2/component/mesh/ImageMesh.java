package com.ankamagames.xulor2.component.mesh;

import org.apache.log4j.*;
import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.framework.graphics.engine.texture.*;
import com.ankamagames.xulor2.appearance.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.xulor2.util.*;
import java.awt.*;
import com.ankamagames.framework.kernel.utils.*;
import com.ankamagames.framework.graphics.engine.test.*;
import com.ankamagames.framework.graphics.engine.fx.effets.*;
import com.ankamagames.framework.graphics.engine.*;
import com.ankamagames.framework.graphics.engine.fx.*;
import com.ankamagames.framework.graphics.engine.geometry.*;
import com.ankamagames.framework.graphics.engine.opengl.*;
import com.ankamagames.framework.graphics.engine.transformer.*;
import com.ankamagames.framework.graphics.engine.entity.*;
import com.ankamagames.framework.kernel.core.common.*;

public final class ImageMesh
{
    private static final Logger m_logger;
    private Color m_modulationColor;
    private String m_shader;
    private Pixmap m_pixmap;
    private int m_x;
    private int m_y;
    private int m_height;
    private int m_width;
    private WidgetShape m_shape;
    private boolean m_pixmapsInitialized;
    private boolean m_flipHorizontaly;
    private boolean m_flipVerticaly;
    private EntitySprite m_entity;
    private final Quaternion m_quat;
    static int m_numImageMesh;
    
    public ImageMesh() {
        super();
        this.m_shape = WidgetShape.RECTANGLE;
        this.m_quat = new Quaternion();
    }
    
    public int getX() {
        return this.m_x;
    }
    
    public void setX(final int x) {
        this.m_x = x;
    }
    
    public int getY() {
        return this.m_y;
    }
    
    public void setY(final int y) {
        this.m_y = y;
    }
    
    public int getHeight() {
        return this.m_height;
    }
    
    public void setHeight(final int height) {
        this.m_height = height;
    }
    
    public int getWidth() {
        return this.m_width;
    }
    
    public void setWidth(final int width) {
        this.m_width = width;
    }
    
    public WidgetShape getShape() {
        return this.m_shape;
    }
    
    public void setShape(final WidgetShape shape) {
        this.m_shape = shape;
    }
    
    public void setPixmap(final Pixmap p) {
        this.m_pixmap = p;
        if (this.m_pixmap != null) {
            this.m_pixmapsInitialized = true;
        }
    }
    
    public Pixmap getPixmap() {
        return this.m_pixmap;
    }
    
    public boolean isPixmapInitialized() {
        return this.m_pixmapsInitialized;
    }
    
    public void setRotation(final Vector3 axis, final float angle) {
        final TransformerSRT srt = (TransformerSRT)this.m_entity.getTransformer().getTransformer(1);
        this.m_quat.set(axis, angle);
        srt.setRotation(this.m_quat);
        if (axis == Vector3.AXIS_Y || axis == Vector3.AXIS_X) {
            final float translationX = srt.getTranslation().getX();
            final float translationY = srt.getTranslation().getY();
            srt.setTranslation(translationX, translationY, -(this.getWidth() / 2));
        }
        this.m_entity.getTransformer().setTransformer(1, srt);
    }
    
    public void setModulationColor(final Color modulationColor) {
        if (modulationColor == this.m_modulationColor) {
            return;
        }
        if (modulationColor != null) {
            this.m_entity.setColor(modulationColor.getRed(), modulationColor.getGreen(), modulationColor.getBlue(), modulationColor.getAlpha());
        }
        else {
            this.m_entity.setColor(1.0f, 1.0f, 1.0f, 1.0f);
        }
        this.m_modulationColor = modulationColor;
    }
    
    public Color getModulationColor() {
        return this.m_modulationColor;
    }
    
    public boolean isFlipHorizontaly() {
        return this.m_flipHorizontaly;
    }
    
    public void setFlipHorizontaly(final boolean flipHorizontaly) {
        this.m_flipHorizontaly = flipHorizontaly;
    }
    
    public boolean isFlipVerticaly() {
        return this.m_flipVerticaly;
    }
    
    public void setFlipVerticaly(final boolean flipVerticaly) {
        this.m_flipVerticaly = flipVerticaly;
    }
    
    public String getShader() {
        return this.m_shader;
    }
    
    public void setShader(final String shader) {
        this.m_shader = shader;
        this.applyShader();
    }
    
    public void updateVertex(final Dimension size, final Insets margin, final Insets border, final Insets padding) {
        if (this.m_pixmap == null || this.m_pixmap.getTexture() == null) {
            this.m_entity.setVisible(false);
            return;
        }
        this.m_entity.setVisible(true);
        final int left = margin.left + border.left + padding.left;
        final int bottom = margin.bottom + border.bottom + padding.bottom;
        this.m_entity.setBounds(bottom + this.m_height + this.m_y, left + this.m_x, this.m_width, this.m_height);
        if (this.m_modulationColor != null) {
            this.m_entity.setColor(this.m_modulationColor);
        }
        else {
            this.m_entity.setColor(1.0f, 1.0f, 1.0f, 1.0f);
        }
        this.m_pixmap.computeCoordinates();
        this.m_entity.setTexture(this.m_pixmap.getTexture());
        this.m_entity.getTransformer().setTranslation(0, -this.m_width / 2, -this.m_height / 2);
        this.m_entity.getTransformer().setTranslation(1, this.m_width / 2, this.m_height / 2);
        if (this.m_flipHorizontaly) {
            if (this.m_flipVerticaly) {
                this.m_entity.getGeometry().setTextureCoordinates(this.m_pixmap.getBottom(), this.m_pixmap.getRight(), this.m_pixmap.getTop(), this.m_pixmap.getLeft(), this.m_pixmap.getRotation());
            }
            else {
                this.m_entity.getGeometry().setTextureCoordinates(this.m_pixmap.getTop(), this.m_pixmap.getRight(), this.m_pixmap.getBottom(), this.m_pixmap.getLeft(), this.m_pixmap.getRotation());
            }
        }
        else if (this.m_flipVerticaly) {
            this.m_entity.getGeometry().setTextureCoordinates(this.m_pixmap.getBottom(), this.m_pixmap.getLeft(), this.m_pixmap.getTop(), this.m_pixmap.getRight(), this.m_pixmap.getRotation());
        }
        else {
            this.m_entity.getGeometry().setTextureCoordinates(this.m_pixmap.getTop(), this.m_pixmap.getLeft(), this.m_pixmap.getBottom(), this.m_pixmap.getRight(), this.m_pixmap.getRotation());
        }
        this.applyShader();
    }
    
    private void applyShader() {
        if (StringUtils.isEmptyOrNull(this.m_shader)) {
            if (HardwareFeatureManager.INSTANCE.isShaderSupported()) {
                this.m_entity.setEffect(null, 0, null);
            }
            else {
                this.m_entity.removeEffectForUI();
            }
            return;
        }
        final int technic = Engine.getTechnic(this.m_shader);
        final Effect uiEffect = EffectManager.getInstance().getUiEffect();
        if (uiEffect != null && uiEffect.isTechniqueValide(technic)) {
            this.m_entity.setEffect(uiEffect, technic, FxConstants.COLOR_SCALE_FOR_UI_PARAMS);
        }
        else {
            ImageMesh.m_logger.error((Object)("technic invalide " + this.m_shader));
        }
    }
    
    public void onCheckIn() {
        this.m_pixmap = null;
        this.m_modulationColor = null;
        this.m_shader = null;
        this.m_entity.removeReference();
        this.m_entity = null;
    }
    
    public void onCheckOut() {
        assert this.m_entity == null;
        this.m_entity = EntitySprite.Factory.newPooledInstance();
        this.m_entity.m_owner = this;
        final GeometrySprite geom = ((MemoryObject.ObjectFactory<GeometrySprite>)GLGeometrySprite.Factory).newPooledInstance();
        this.m_entity.setGeometry(geom);
        geom.removeReference();
        final BatchTransformer transformer = this.m_entity.getTransformer();
        transformer.addTransformer(new TransformerSRT());
        transformer.addTransformer(new TransformerSRT());
    }
    
    public Entity getEntity() {
        return this.m_entity;
    }
    
    public Quaternion getQuat() {
        return this.m_quat;
    }
    
    public void setScale(final float scaleX, final float scaleY, final float scaleZ) {
        final TransformerSRT srt = (TransformerSRT)this.m_entity.getTransformer().getTransformer(1);
        srt.setScale(scaleX, scaleY, scaleZ);
        this.m_entity.getTransformer().setTransformer(1, srt);
    }
    
    static {
        m_logger = Logger.getLogger((Class)ImageMesh.class);
    }
}
