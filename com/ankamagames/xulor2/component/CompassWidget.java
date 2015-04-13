package com.ankamagames.xulor2.component;

import java.util.*;
import com.ankamagames.baseImpl.graphics.game.worldPositionManager.*;
import com.ankamagames.xulor2.core.converter.*;
import com.ankamagames.xulor2.appearance.*;
import com.ankamagames.framework.graphics.engine.texture.*;
import com.ankamagames.baseImpl.graphics.isometric.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.xulor2.util.*;
import com.ankamagames.xulor2.component.mesh.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.framework.java.util.*;
import com.ankamagames.xulor2.layout.*;

public class CompassWidget extends WatcherContainer implements ValuePointManager, RenderProcessHandler
{
    public static final CompassWidget INSTANCE;
    public static final String TAG = "compass";
    public static final String PROGRESS_BAR = "progressBar";
    public static final String COMPASS_ORIENTATION = "compassOrientation";
    private ProgressBar m_progressBar;
    private Image m_compassTypeImg;
    private Image m_compassOrientationImg;
    private float m_angle;
    private float m_proximity;
    private final ArrayList<ValuePoint> m_valuePoints;
    public static final int ANGLE_HASH;
    public static final int PROXIMITY_HASH;
    
    public CompassWidget() {
        super();
        this.m_valuePoints = new ArrayList<ValuePoint>();
    }
    
    @Override
    public Widget getWidgetByThemeElementName(final String themeElementName, final boolean compilationMode) {
        if (themeElementName.equals("progressBar")) {
            return this.m_progressBar;
        }
        if (themeElementName.equals("compassOrientation")) {
            return this.m_compassOrientationImg;
        }
        return null;
    }
    
    @Override
    public String getTag() {
        return "compass";
    }
    
    private float getEffectiveProximity() {
        final float minAngle = 0.05f;
        final float maxAngleRatio = 0.3f;
        return (1.0f - this.m_proximity) * 0.3f + 0.05f;
    }
    
    private void computeProgressBarPercentage() {
        final float angle = this.m_angle + 6.2831855f * this.getEffectiveProximity() / 2.0f;
        this.m_progressBar.getProgressBarMesh().setDeltaAngle(angle);
        this.m_progressBar.updateProgressBarMesh();
    }
    
    public void setAngle(final float angle) {
        this.m_angle = angle;
        this.m_compassOrientationImg.getImageMesh().setRotation(Vector3.AXIS_Z, angle);
        this.computeProgressBarPercentage();
        this.invalidateMinSize();
        this.setNeedsToPostProcess();
    }
    
    public void setProximity(final float proximity) {
        this.m_proximity = proximity;
        this.m_progressBar.getProgressBarMesh().setFullCirclePercentage(this.getEffectiveProximity());
        this.computeProgressBarPercentage();
    }
    
    @Override
    public void addPoint(final ValuePoint p) {
        this.m_valuePoints.add(p);
        if (!p.isWatcherInit()) {
            p.setWatcherStart(this.getTarget().getWorldCellX(), this.getTarget().getWorldCellY(), this.getTarget().getWorldCellAltitude());
        }
        final Texture texture = ConverterLibrary.getInstance().convertToTexture(p.getIconUrl());
        if (texture == null) {
            CompassWidget.m_logger.error((Object)("On essai d'ajouter un marqueur avec l'url=" + p.getIconUrl() + " mais l'icone n'exite pas !!!"));
            return;
        }
        final PixmapElement pixmapElement = new PixmapElement();
        pixmapElement.onCheckOut();
        pixmapElement.setPixmap(new Pixmap(texture));
        this.m_compassTypeImg.add(pixmapElement);
        this.setVisible(true);
    }
    
    @Override
    public void removePoint(final ValuePoint p) {
        this.m_valuePoints.remove(p);
        this.setVisible(false);
    }
    
    private float angle(float xa, final float ya) {
        if (xa == 0.0f && ya < 0.0f) {
            xa = -1.0E-4f;
        }
        if (xa == 0.0f && ya == 0.0f) {
            return -1.5707964f;
        }
        final float norm = Vector2.length(xa, ya);
        final float cosX = ya / norm;
        final float sinX = -xa / norm;
        final double acos = Math.acos(cosX);
        return (float)(Math.signum(sinX) * acos) - 1.5707964f;
    }
    
    @Override
    public void process(final IsoWorldScene isoWorldScene, final int deltaTime) {
        if (this.m_valuePoints.isEmpty()) {
            return;
        }
        final ValuePoint point = this.m_valuePoints.get(0);
        final float targetX = this.getTarget().getWorldX();
        final float targetY = this.getTarget().getWorldY();
        final float startDist = Vector2.length(point.getWatcherStartX() - point.getStartX(), point.getWatcherStartY() - point.getStartY());
        final float dist = Vector2.length(targetX - point.getX(), targetY - point.getY());
        this.setProximity(1.0f - MathHelper.clamp(dist / (startDist * 1.2f), 0.0f, 1.0f));
        final float targetScreenX = isoWorldScene.isoToScreenX(targetX, targetY);
        final float targetScreenY = isoWorldScene.isoToScreenY(targetX, targetY, this.getTarget().getWorldCellAltitude());
        final int screenX = point.getScreenX();
        final int screenY = point.getScreenY();
        this.setAngle(this.angle(targetScreenX - screenX, targetScreenY - screenY));
    }
    
    @Override
    public void prepareBeforeRendering(final IsoWorldScene isoWorldScene, final float centerScreenIsoWorldX, final float centerScreenIsoWorldY) {
    }
    
    @Override
    public void onCheckIn() {
        super.onCheckIn();
        this.m_progressBar = null;
        this.m_compassOrientationImg = null;
        this.m_compassTypeImg = null;
        this.m_valuePoints.clear();
    }
    
    @Override
    public void onCheckOut() {
        super.onCheckOut();
        final StaticLayoutData sld = new StaticLayoutData();
        sld.onCheckOut();
        sld.setSize(new Dimension(200, 200));
        this.add(sld);
        final CompassLayout l = new CompassLayout();
        l.onCheckOut();
        this.add(l);
        this.setVisible(false);
        this.setYOffset(-130);
        (this.m_progressBar = new ProgressBar()).onCheckOut();
        this.m_progressBar.setDisplayType(ProgressBar.ProgressBarDisplayType.COMPASS);
        this.m_progressBar.setValue(1.0f);
        this.m_progressBar.setNonBlocking(true);
        final CompassProgressBarMesh mesh = (CompassProgressBarMesh)this.m_progressBar.getProgressBarMesh();
        mesh.setInnerRadiusFactor(0.75f);
        mesh.setOuterRadiusFactor(0.8f);
        this.add(this.m_progressBar);
        (this.m_compassOrientationImg = new Image()).onCheckOut();
        this.m_compassOrientationImg.setNonBlocking(true);
        this.add(this.m_compassOrientationImg);
        (this.m_compassTypeImg = new Image()).onCheckOut();
        this.m_compassTypeImg.setNonBlocking(true);
        this.add(this.m_compassTypeImg);
    }
    
    @Override
    public void copyElement(final BasicElement c) {
        super.copyElement(c);
        final CompassWidget w = (CompassWidget)c;
        w.setAngle(this.m_angle);
        w.setProximity(this.m_proximity);
    }
    
    @Override
    public boolean setXMLAttribute(final int hash, final String value, final ConverterLibrary cl) {
        if (hash == CompassWidget.ANGLE_HASH) {
            this.setAngle(PrimitiveConverter.getFloat(value));
        }
        else {
            if (hash != CompassWidget.PROXIMITY_HASH) {
                return super.setXMLAttribute(hash, value, cl);
            }
            this.setProximity(PrimitiveConverter.getFloat(value));
        }
        return true;
    }
    
    @Override
    public boolean setPropertyAttribute(final int hash, final Object value) {
        if (hash == CompassWidget.ANGLE_HASH) {
            this.setAngle(PrimitiveConverter.getFloat(value));
        }
        else {
            if (hash != CompassWidget.PROXIMITY_HASH) {
                return super.setPropertyAttribute(hash, value);
            }
            this.setProximity(PrimitiveConverter.getFloat(value));
        }
        return true;
    }
    
    static {
        INSTANCE = new CompassWidget();
        ANGLE_HASH = "angle".hashCode();
        PROXIMITY_HASH = "proximity".hashCode();
    }
    
    private class CompassLayout extends AbstractLayoutManager
    {
        @Override
        public Dimension getContentPreferedSize(final Container container) {
            final Dimension prefSize = CompassWidget.this.m_compassOrientationImg.getPrefSize();
            return new Dimension(prefSize.width * 3, prefSize.height * 3);
        }
        
        @Override
        public Dimension getContentMinSize(final Container container) {
            return this.getContentPreferedSize(container);
        }
        
        @Override
        public void layoutContainer(final Container parent) {
            final int contentWidth = parent.getAppearance().getContentWidth();
            final int contentHeight = parent.getAppearance().getContentHeight();
            CompassWidget.this.m_progressBar.setPosition(0, 0);
            CompassWidget.this.m_progressBar.setSize(contentWidth, contentHeight);
            CompassWidget.this.m_compassOrientationImg.setSizeToPrefSize();
            CompassWidget.this.m_compassTypeImg.setSizeToPrefSize();
            final int radius = contentWidth / 2 - CompassWidget.this.m_compassOrientationImg.getWidth() / 2;
            final int centerX = contentWidth / 2 + (int)(radius * MathHelper.cosf(CompassWidget.this.m_angle));
            final int centerY = contentHeight / 2 + (int)(radius * MathHelper.sinf(CompassWidget.this.m_angle));
            CompassWidget.this.m_compassOrientationImg.setPosition(centerX - CompassWidget.this.m_compassOrientationImg.getWidth() / 2, centerY - CompassWidget.this.m_compassOrientationImg.getHeight() / 2);
            CompassWidget.this.m_compassTypeImg.setPosition(centerX - CompassWidget.this.m_compassTypeImg.getWidth() / 2, centerY - CompassWidget.this.m_compassTypeImg.getHeight() / 2);
        }
    }
}
