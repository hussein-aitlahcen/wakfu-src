package com.ankamagames.xulor2.component.mesh;

import org.apache.log4j.*;
import com.ankamagames.xulor2.component.mesh.mapHelper.*;
import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.framework.graphics.engine.geometry.*;
import com.ankamagames.framework.graphics.engine.opengl.*;
import java.awt.*;
import com.ankamagames.framework.graphics.engine.entity.*;
import java.util.*;
import com.ankamagames.xulor2.component.map.*;
import com.ankamagames.baseImpl.graphics.alea.animatedElement.*;
import com.ankamagames.framework.graphics.engine.transformer.*;
import com.ankamagames.framework.graphics.engine.fx.*;
import com.ankamagames.framework.graphics.engine.texture.*;
import gnu.trove.*;

public final class MapMesh extends MapOverlayMesh
{
    private static final Logger m_logger;
    private Entity3D m_mapInformationEntity;
    private Entity3D m_mapDecoratorEntity;
    private EntityGroup m_mapAnimDecorators;
    private MapShader m_shader;
    private Pixmap m_mapBackgroundPixmap;
    private int m_mapBackgroundX;
    private int m_mapBackgroundY;
    private int m_mapBackgroundWidth;
    private int m_mapBackgroundHeight;
    private TIntObjectHashMap<ParentMapZone> m_mapZones;
    private static final short STIPPLE_PATTERN = 3855;
    
    @Override
    public void setMapDisplayer(final MapDisplayer mapDisplayer) {
        super.setMapDisplayer(mapDisplayer);
        this.addTransformations(this.m_mapDisplayer.getEntity());
        this.applyShader();
    }
    
    public void addMapZone(final int index, final ParentMapZone mapZone) {
        this.m_mapZones.put(index, mapZone);
    }
    
    public void removeMapZone(final int index) {
        this.m_mapZones.remove(index);
    }
    
    public void removeAllMapZones() {
        this.m_mapZones.clear();
    }
    
    @Override
    public void setPixmap(final Pixmap p) {
        super.setPixmap(p);
        this.m_shader.setMainTextureDimension(p.getWidth());
    }
    
    public void setMapBackground(final Pixmap p) {
        this.m_mapBackgroundPixmap = p;
    }
    
    public void setMapBackgroundParameters(final int x, final int y, final int width, final int height) {
        this.m_mapBackgroundX = x;
        this.m_mapBackgroundY = y;
        this.m_mapBackgroundWidth = width;
        this.m_mapBackgroundHeight = height;
    }
    
    public void setMapZoneSelected(final Color c, final int index, final boolean selected) {
        if (index < 0 || index >= this.m_mapInformationEntity.getNumGeometries()) {
            return;
        }
        final GeometryMesh geom = (GeometryMesh)this.m_mapInformationEntity.getGeometry(index);
        geom.setColor(c.getRed(), c.getGreen(), c.getBlue(), selected ? 0.6f : 0.3f);
    }
    
    public void setMapZoneLineWidth(final int index, final float lineWidth) {
        if (index < 0 || index >= this.m_mapInformationEntity.getNumGeometries()) {
            return;
        }
        final GLGeometryMesh geom = (GLGeometryMesh)this.m_mapInformationEntity.getGeometry(index);
        geom.setLineWidth(lineWidth);
    }
    
    @Override
    protected void updateEntitiesWithTransformers() {
        super.updateEntitiesWithTransformers();
        this.m_mapInformationEntity.getTransformer().setToUpdate();
        this.m_mapDecoratorEntity.getTransformer().setToUpdate();
        this.m_mapAnimDecorators.getTransformer().setToUpdate();
    }
    
    @Override
    public void updateVertex(final Dimension size, final Insets margin, final Insets border, final Insets padding) {
        super.updateVertex(size, margin, border, padding);
        this.updateDecorator(this.getColor());
    }
    
    @Override
    protected void recomputeCameraPosition(final Dimension size) {
        this.updateCameraTranslation((float)size.getWidth() * 0.5f, (float)size.getHeight() * 0.5f);
    }
    
    @Override
    protected void updateEntity() {
        this.m_mapInformationEntity.clear();
        this.m_mapDecoratorEntity.clear();
        this.m_mapAnimDecorators.removeAllChildren();
        final ArrayList<Entity> childList = this.m_entity.getChildList();
        final Entity mapEntity = this.m_mapDisplayer.getEntity();
        if (childList.isEmpty()) {
            this.m_entity.addChild(mapEntity);
            this.m_entity.addChild(this.m_mapInformationEntity);
            this.m_entity.addChild(this.m_mapDecoratorEntity);
            this.m_entity.addChild(this.m_mapAnimDecorators);
        }
        else if (childList.get(0) != mapEntity) {
            this.m_entity.setChild(0, mapEntity);
        }
    }
    
    private void updateDecorator(final Color color) {
        final TIntObjectIterator<ParentMapZone> it = this.m_mapZones.iterator();
        while (it.hasNext()) {
            it.advance();
            final ParentMapZone mapZone = it.value();
            final Pixmap pixmap = mapZone.getPixmap();
            if (pixmap != null) {
                final int imageWidth = (int)(pixmap.getWidth() / this.m_zoomFactor / 1.5f);
                final int imageHeight = (int)(pixmap.getHeight() / this.m_zoomFactor / 1.5f);
                EntityConstructor.addGeometry(mapZone.getCenterWorldX() - this.m_width / 2, mapZone.getCenterWorldY() + imageHeight + this.m_height / 2, imageWidth, imageHeight, pixmap, color, this.m_mapDecoratorEntity);
            }
            final ArrayList<MapZone> list = mapZone.getChildren();
            for (int i = 0, size = list.size(); i < size; ++i) {
                final MapZone zone = list.get(i);
                final AnimatedElementWithDirection animatedElement = zone.getAnimatedElement();
                if (animatedElement != null) {
                    this.m_mapAnimDecorators.addChild(animatedElement.getEntity());
                }
            }
        }
        if (this.m_mapBackgroundPixmap != null) {
            EntityConstructor.addGeometry(-this.m_width / 2 + this.m_mapBackgroundX, this.m_height / 2 - this.m_mapBackgroundY, this.m_mapBackgroundWidth, this.m_mapBackgroundHeight, this.m_mapBackgroundPixmap, color, this.m_mapDecoratorEntity);
        }
    }
    
    @Override
    public void onCheckIn() {
        super.onCheckIn();
        this.m_mapBackgroundPixmap = null;
        this.m_mapZones.clear();
        this.m_mapZones = null;
        this.m_mapInformationEntity.removeReference();
        this.m_mapInformationEntity = null;
        this.m_mapAnimDecorators.removeReference();
        this.m_mapInformationEntity = null;
        this.m_mapDecoratorEntity.removeReference();
        this.m_mapDecoratorEntity = null;
        this.m_shader = null;
    }
    
    @Override
    public void onCheckOut() {
        super.onCheckOut();
        assert this.m_mapInformationEntity == null;
        assert this.m_mapDecoratorEntity == null;
        assert this.m_mapAnimDecorators == null;
        this.m_mapInformationEntity = Entity3D.Factory.newPooledInstance();
        this.m_mapDecoratorEntity = Entity3D.Factory.newPooledInstance();
        this.m_mapAnimDecorators = EntityGroup.Factory.newPooledInstance();
        this.addTransformations(this.m_mapInformationEntity);
        this.addTransformations(this.m_mapDecoratorEntity);
        this.addTransformations(this.m_mapAnimDecorators);
        this.m_mapZones = new TIntObjectHashMap<ParentMapZone>();
        try {
            this.m_shader = new MapShader();
        }
        catch (Exception e) {
            MapMesh.m_logger.warn((Object)"Probl\u00e8me \u00e0 l'instantiation de MapMesh");
        }
    }
    
    private void addTransformations(final Entity entity) {
        entity.getTransformer().addTransformer(this.m_worldTransformation);
        entity.getTransformer().addTransformer(this.m_cameraTransformation);
    }
    
    private void applyShader() {
        if (this.m_mapDisplayer != null) {
            this.m_mapDisplayer.getEntity().setEffect(this.m_shader.getEffect(), this.m_shader.getTechniqueCRC(), this.m_shader.getParams());
        }
    }
    
    private void unapplyShader() {
        if (this.m_mapDisplayer != null) {
            this.m_mapDisplayer.getEntity().removeEffectForUI();
        }
    }
    
    public void setZoneDisplayEnabled(final boolean enable) {
        if (enable) {
            this.applyShader();
        }
        else {
            this.unapplyShader();
        }
    }
    
    public void setZoneMaskTexture(final Texture texture) {
        if (this.m_shader != null) {
            this.m_shader.setMaskTexture(texture);
        }
        this.m_mapDisplayer.setZoneMaskTexture(texture);
    }
    
    public void setZoneIndexById(final TIntByteHashMap zoneIndexById) {
        if (this.m_shader != null) {
            this.m_shader.setZonesIndexById(zoneIndexById);
        }
    }
    
    public void setColorByZoneId(final TIntObjectHashMap<Color> colorByIndex) {
        if (this.m_shader != null) {
            this.m_shader.setColorsById(colorByIndex);
        }
    }
    
    public void setSelectedZone(final int zoneId) {
        if (this.m_shader != null) {
            this.m_shader.setSelectedZone(zoneId);
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)MapMesh.class);
    }
}
