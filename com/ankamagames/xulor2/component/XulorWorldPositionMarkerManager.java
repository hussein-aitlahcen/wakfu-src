package com.ankamagames.xulor2.component;

import java.util.*;
import com.ankamagames.baseImpl.graphics.game.worldPositionManager.*;
import com.ankamagames.framework.graphics.engine.entity.*;
import com.ankamagames.xulor2.graphics.*;
import com.ankamagames.framework.graphics.engine.transformer.*;
import com.ankamagames.xulor2.appearance.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.baseImpl.graphics.isometric.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.xulor2.layout.*;
import com.ankamagames.xulor2.util.*;

public class XulorWorldPositionMarkerManager extends Widget implements RenderProcessHandler, ValuePointManager
{
    private static final XulorWorldPositionMarkerManager m_instance;
    public static final String TAG = "WorldPositionMarker";
    public static int m_positionAps;
    private static String m_particlePath;
    private float m_screenIsoCenterX;
    private float m_screenIsoCenterY;
    private EntityGroup m_group;
    private ArrayList<ValuePoint> m_points;
    private final ArrayList<XulorParticleSystem> m_particles;
    private static final Quaternion quat;
    
    private XulorWorldPositionMarkerManager() {
        super();
        this.m_points = new ArrayList<ValuePoint>();
        this.m_particles = new ArrayList<XulorParticleSystem>();
    }
    
    public static XulorWorldPositionMarkerManager getInstance() {
        return XulorWorldPositionMarkerManager.m_instance;
    }
    
    public static void setParticlePath(final String path) {
        XulorWorldPositionMarkerManager.m_particlePath = path;
    }
    
    @Override
    protected void addInnerMeshes() {
        super.addInnerMeshes();
        this.m_entity.addChild(this.m_group);
    }
    
    @Override
    public void addPoint(final ValuePoint point) {
        this.m_points.add(point);
        final XulorParticleSystem particle = XulorParticleSystemFactory.getInstance().getFreeParticleSystem(String.format(XulorWorldPositionMarkerManager.m_particlePath, point.getArrowApsId()), 0);
        particle.getTransformer().addTransformer(new TransformerSRT());
        this.m_particles.add(particle);
    }
    
    @Override
    public void removePoint(final ValuePoint point) {
        if (this.m_points.remove(point)) {
            final XulorParticleSystem particleSystem = this.m_particles.remove(this.m_particles.size() - 1);
            particleSystem.stopAndKill();
            particleSystem.removeReference();
        }
    }
    
    @Override
    public String getTag() {
        return "WorldPositionMarker";
    }
    
    @Override
    public boolean isAppearanceCompatible(final DecoratorAppearance appearance) {
        return true;
    }
    
    private int getOnScreenX(final float x, final float y, final int textureWidth, final int textureHeight) {
        final int contentHeight = this.getAppearance().getContentHeight() - textureHeight;
        final int contentWidth = this.getAppearance().getContentWidth() - textureWidth;
        final float screenRatio = contentHeight / contentWidth;
        final float xi = x - contentWidth / 2;
        final float yi = y - contentHeight / 2;
        final float a = yi / xi;
        if (Math.abs(a) < screenRatio) {
            return (xi > 0.0f) ? contentWidth : 0;
        }
        return (int)(Math.signum(xi) * contentHeight / 2.0f / Math.abs(a)) + contentWidth / 2;
    }
    
    private int getOnScreenY(final float x, final float y, final int textureWidth, final int textureHeight) {
        final int contentHeight = this.getAppearance().getContentHeight() - textureHeight;
        final int contentWidth = this.getAppearance().getContentWidth() - textureWidth;
        final float screenRatio = contentHeight / contentWidth;
        final float xi = x - contentWidth / 2;
        final float yi = y - contentHeight / 2;
        final float a = yi / xi;
        if (Math.abs(a) > screenRatio) {
            return (yi > 0.0f) ? contentHeight : 0;
        }
        return (int)(Math.signum(yi) * contentWidth / 2.0f * Math.abs(a)) + contentHeight / 2;
    }
    
    private void displayPoint(final ValuePoint mapPoint, final XulorParticleSystem particle, final float halfScreenX, final float halfScreenY) {
        final float xScreenItem = mapPoint.getScreenX() - this.m_screenIsoCenterX;
        final float yScreenItem = mapPoint.getScreenY() - this.m_screenIsoCenterY;
        final int x = this.getOnScreenX((int)xScreenItem + halfScreenX, (int)yScreenItem + halfScreenY, 50, 50);
        final int y = this.getOnScreenY((int)xScreenItem + halfScreenX, (int)yScreenItem + halfScreenY, 50, 50);
        final TransformerSRT transformer = (TransformerSRT)particle.getTransformer().getTransformer(0);
        final float x2 = x - halfScreenX;
        final float y2 = y - halfScreenY;
        final float angle = this.angle(x2, y2) - 3.1415927f;
        XulorWorldPositionMarkerManager.quat.set(Vector3.AXIS_Z, angle);
        transformer.setTranslation(x + 25, y + 25, 0.0f);
        transformer.setRotation(XulorWorldPositionMarkerManager.quat);
        particle.getTransformer().setToUpdate();
        this.m_group.addChild(particle);
    }
    
    private float angle(float xa, final float ya) {
        if (xa == 0.0f && ya < 0.0f) {
            xa = -1.0E-4f;
        }
        final float norm = Vector2.length(xa, ya);
        final float cosX = ya / norm;
        final float sinX = -xa / norm;
        final double acos = Math.acos(cosX);
        return (float)(Math.signum(sinX) * acos);
    }
    
    public void clear() {
        this.m_points.clear();
        for (int i = this.m_particles.size() - 1; i >= 0; --i) {
            final XulorParticleSystem particleSystem = this.m_particles.remove(0);
            particleSystem.stopAndKill();
            particleSystem.removeReference();
        }
    }
    
    @Override
    public void process(final IsoWorldScene scene, final int deltaTime) {
        final float halfScreenX = this.m_appearance.getContentWidth() / 2.0f;
        final float halfScreenY = this.m_appearance.getContentHeight() / 2.0f;
        this.m_group.removeAllChildren();
        for (int i = this.m_points.size() - 1; i >= 0; --i) {
            final ValuePoint valuePoint = this.m_points.get(i);
            final XulorParticleSystem particleSystem = this.m_particles.get(i);
            this.displayPoint(valuePoint, particleSystem, halfScreenX, halfScreenY);
            particleSystem.update(deltaTime / 1000.0f);
            particleSystem.prepareParticlesBeforeRendering(this.m_group);
        }
    }
    
    @Override
    public void prepareBeforeRendering(final IsoWorldScene scene, final float centerScreenIsoWorldX, final float centerScreenIsoWorldY) {
        this.m_screenIsoCenterX = scene.isoToScreenX(centerScreenIsoWorldX, centerScreenIsoWorldY);
        this.m_screenIsoCenterY = scene.isoToScreenY(centerScreenIsoWorldX, centerScreenIsoWorldY);
    }
    
    @Override
    public void onCheckIn() {
        super.onCheckIn();
        this.m_group.removeReference();
        this.m_group = null;
        this.clear();
    }
    
    @Override
    public void onCheckOut() {
        super.onCheckOut();
        final DecoratorAppearance app = DecoratorAppearance.checkOut();
        app.setWidget(this);
        this.add(app);
        final StaticLayoutData sld = new StaticLayoutData();
        sld.onCheckOut();
        sld.setSize(new Dimension(100.0f, 100.0f));
        this.add(sld);
        this.m_nonBlocking = true;
        assert this.m_group == null;
        this.m_group = EntityGroup.Factory.newPooledInstance();
        this.m_group.m_owner = this;
        this.m_group.getTransformer().addTransformer(new TransformerSRT());
    }
    
    static {
        m_instance = new XulorWorldPositionMarkerManager();
        quat = new Quaternion();
    }
}
