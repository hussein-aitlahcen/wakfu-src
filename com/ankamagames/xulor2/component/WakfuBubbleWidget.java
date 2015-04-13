package com.ankamagames.xulor2.component;

import com.ankamagames.baseImpl.graphics.alea.mobile.*;
import org.apache.log4j.*;
import com.ankamagames.xulor2.component.bubble.*;
import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.xulor2.util.alignment.*;
import com.ankamagames.baseImpl.graphics.alea.adviser.text.*;
import com.ankamagames.framework.kernel.core.common.collections.*;
import com.ankamagames.baseImpl.graphics.alea.animatedElement.*;
import com.ankamagames.baseImpl.graphics.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.xulor2.*;
import java.util.*;
import com.ankamagames.xulor2.appearance.*;
import com.ankamagames.xulor2.tween.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.baseImpl.graphics.isometric.*;

public class WakfuBubbleWidget extends WatcherContainer implements VisibleChangedListener, TargetPositionListener<PathMobile>
{
    private static final Logger m_logger;
    public static final String TAG = "wakfuBubble";
    private static final int DEFAULT_X_OFFSET = 5;
    private static final int DEFAULT_Y_OFFSET = 60;
    public static final String TEXT_COMPONENT_NAME = "text";
    public static final String CONTAINER_COMPONENT_NAME = "container";
    public static final String COLORED_CONTAINER_COMPONENT_NAME = "coloredContainer";
    public static final String IMAGE_COMPONENT_NAME = "image";
    public static int UID;
    private TextWidget m_textWidget;
    private boolean m_unloadOnClean;
    private boolean m_bubbleIsVisible;
    private boolean m_isDirectionInit;
    private boolean m_toRight;
    private boolean m_shakingBubble;
    private boolean m_fadingBubble;
    private WakfuBubbleModeType m_wakfuBubbleModeType;
    private PathMobile m_bubbleObserver;
    private DistanceEnum m_currentDistance;
    private float m_scale;
    private boolean m_resizingBubble;
    private Color m_color;
    
    public WakfuBubbleWidget() {
        super();
        this.m_bubbleIsVisible = true;
        this.m_isDirectionInit = false;
        this.m_toRight = true;
        this.m_shakingBubble = false;
        this.m_fadingBubble = false;
        this.m_scale = 1.0f;
    }
    
    public void initialize() {
        this.initialize(true, true);
    }
    
    public void initialize(final boolean unloadOnClean, final boolean resizing) {
        this.initialize(true, unloadOnClean, resizing, false);
    }
    
    public void initialize(final boolean unloadOnClean, final boolean resizing, final boolean fading) {
        this.initialize(true, unloadOnClean, resizing, fading);
    }
    
    public void initialize(final boolean toRight, final boolean unloadOnClean, final boolean resizing, final boolean fading) {
        this.m_textWidget = (TextWidget)this.getElementMap().getElement("text");
        if (!this.m_isDirectionInit || this.m_toRight != toRight) {
            this.m_toRight = toRight;
            this.applyDirection();
        }
        this.m_unloadOnClean = unloadOnClean;
        this.m_resizingBubble = resizing;
        this.m_fadingBubble = fading;
    }
    
    protected void applyDirection() {
        if (this.m_wakfuBubbleModeType != null && this.m_wakfuBubbleModeType.getWakfuBubbleStyleTransformer() != null) {
            this.m_wakfuBubbleModeType.getWakfuBubbleStyleTransformer().turn(this);
        }
        this.setAlign(this.m_toRight ? Alignment9.NORTH_WEST : Alignment9.NORTH_EAST);
        if (this.m_target != null) {
            this.screenTargetMoved(this.m_target, this.m_target.getScreenX(), this.m_target.getScreenY(), 0);
        }
        this.invalidate();
        this.m_isDirectionInit = true;
    }
    
    protected void transform() {
        this.m_wakfuBubbleModeType.getWakfuBubbleStyleTransformer().transform(this);
    }
    
    public String setText(final String text) {
        return this.setText(text, 1.0f);
    }
    
    public String setText(String text, final float durationFactor) {
        assert this.m_textWidget != null;
        final ObjectPair<WakfuBubbleModeType, String> objectPair = WakfuBubbleModeType.getWakfuBubbleType(text);
        if (!objectPair.getFirst().equals(this.m_wakfuBubbleModeType)) {
            this.m_wakfuBubbleModeType = objectPair.getFirst();
            this.transform();
            this.applyDirection();
        }
        text = objectPair.getSecond().trim();
        this.m_textWidget.setText(text);
        this.setDuration((int)(TextUtils.getTextDuration(text) * durationFactor));
        if (this.m_fadingBubble) {
            this.fade(true);
        }
        this.updateVisibility();
        this.pushToTop();
        return text;
    }
    
    public void setTarget(final AnimatedElementWithDirection target) {
        this.setTarget((AnimatedElement)target);
        if (target != null) {
            this.checkMobileDirection(target.getDirection());
        }
    }
    
    public void setTarget(final AnimatedElement target) {
        if (target == this.m_target) {
            return;
        }
        if (this.m_target != null && this.m_target instanceof AnimatedElement) {
            ((AnimatedElement)this.m_target).removeVisibleChangedListener(this);
        }
        super.setTarget(target);
        if (target != null) {
            target.addVisibleChangedListener(this);
            this.setTargetIsVisible(target.isVisible());
        }
    }
    
    public void setBubbleObserver(final PathMobile bubbleObserver) {
        (this.m_bubbleObserver = bubbleObserver).addPositionListener(this);
    }
    
    private void checkMobileDirection(final Direction8 direction8) {
        final boolean toRight = isRightDirection(direction8);
        if (toRight != this.m_toRight) {
            this.m_toRight = toRight;
            this.applyDirection();
        }
    }
    
    @Override
    public void onAdviserCleanUp() {
        if (this.m_fadingBubble) {
            this.fade(false);
        }
        else {
            this.cleanUp();
        }
    }
    
    public void cleanUp() {
        this.m_shakingBubble = false;
        this.m_currentDistance = null;
        if (this.m_unloadOnClean) {
            this.unloadWidget();
        }
        else {
            this.setBubbleIsVisible(false);
        }
        if (this.m_bubbleObserver != null) {
            this.m_bubbleObserver.removePositionListener(this);
        }
    }
    
    public void setBubbleIsVisible(final boolean bubbleIsVisible) {
        if (this.m_bubbleIsVisible == bubbleIsVisible) {
            return;
        }
        this.m_bubbleIsVisible = bubbleIsVisible;
        this.updateVisibility();
    }
    
    @Override
    protected void updateVisibility() {
        final boolean visible = this.m_targetIsVisible && this.m_bubbleIsVisible;
        if (this.m_visible == visible) {
            return;
        }
        this.setVisible(visible);
        if (visible) {
            this.pushToTop();
        }
    }
    
    private void pushToTop() {
        final LayeredContainer layeredContainer = this.getParentOfType(LayeredContainer.class);
        if (layeredContainer == null) {
            return;
        }
        layeredContainer.pushToTop(this);
    }
    
    @Override
    public final void invalidate() {
        super.invalidate();
    }
    
    @Override
    public Point2i getComputedPosition(final int x, final int y, final int height) {
        final Point2i position = super.getComputedPosition(x, y, height);
        int newX = position.getX();
        int newY = position.getY();
        final int duration = this.m_watcherContainerAdviser.getDuration();
        if ((this.m_shakingBubble && (duration == -1 || this.m_watcherContainerAdviser.getElapsedLifeTime() < duration / 2)) || (this.m_wakfuBubbleModeType == WakfuBubbleModeType.SCREAM && this.m_watcherContainerAdviser.getElapsedLifeTime() < 500)) {
            newX += MathHelper.random(-3, 3);
            newY += MathHelper.random(-3, 3);
        }
        position.set(newX, newY);
        return position;
    }
    
    @Override
    public void screenTargetMoved(final ScreenTarget target, final int x, final int y, final int height) {
        this.updateVisibility();
        this.checkDistance(this.m_bubbleObserver);
        super.screenTargetMoved(target, x, y, 0);
    }
    
    public void setShakingBubble(final boolean shakingBubble) {
        this.m_shakingBubble = shakingBubble;
    }
    
    public void setDuration(final int duration) {
        this.m_watcherContainerAdviser.setDuration(duration);
    }
    
    public void resetElapsedLifeTime() {
        this.m_watcherContainerAdviser.setElapsedLifeTime(0);
        this.updateVisibility();
    }
    
    public int getAdviserId() {
        return this.m_watcherContainerAdviser.getId();
    }
    
    public boolean isToRight() {
        return this.m_toRight;
    }
    
    public Color getColor() {
        return this.m_color;
    }
    
    public void setColor(final Color color) {
        this.m_color = color;
        this.applyColor();
    }
    
    public void applyColor() {
        final Container container = (Container)this.getElementMap().getElement("coloredContainer");
        container.setVisible(this.m_color != null);
        container.getAppearance().setModulationColor(this.m_color);
    }
    
    public void unloadWidget() {
        if (this.getElementMap() != null) {
            Xulor.getInstance().unload(this.getElementMap().getId());
        }
    }
    
    @Override
    public void onVisibleChanged(final boolean visible, final VisibleChangedCause cause) {
        this.setTargetIsVisible(visible);
    }
    
    public static boolean isRightDirection(final Direction8 direction8) {
        switch (direction8) {
            case NORTH:
            case NORTH_EAST:
            case EAST:
            case SOUTH_EAST:
            case SOUTH: {
                return false;
            }
            case WEST:
            case NORTH_WEST:
            case SOUTH_WEST: {
                return true;
            }
            default: {
                return false;
            }
        }
    }
    
    @Override
    public float getWorldX() {
        if (this.m_target != null) {
            return this.m_target.getWorldX();
        }
        return 0.0f;
    }
    
    @Override
    public float getWorldY() {
        if (this.m_target != null) {
            return this.m_target.getWorldY();
        }
        return 0.0f;
    }
    
    @Override
    public float getAltitude() {
        if (this.m_target != null) {
            return this.m_target.getAltitude();
        }
        return 0.0f;
    }
    
    public int getDuration() {
        return this.m_watcherContainerAdviser.getDuration();
    }
    
    @Override
    public void removedFromWidgetTree() {
        super.removedFromWidgetTree();
        if (this.m_target != null && this.m_target instanceof AnimatedElement) {
            ((AnimatedElement)this.m_target).removeVisibleChangedListener(this);
        }
    }
    
    @Override
    public void onCheckIn() {
        super.onCheckIn();
        this.m_isDirectionInit = false;
    }
    
    private void fade(final boolean fadeIn) {
        final ElementMap map = this.getElementMap();
        final ArrayList<ModulationColorClient> mcc = new ArrayList<ModulationColorClient>();
        if (map == null) {
            return;
        }
        Widget w = (Widget)map.getElement("image");
        if (w != null) {
            mcc.add(w.getAppearance());
        }
        w = (Widget)map.getElement("container");
        if (w != null) {
            mcc.add(w.getAppearance());
        }
        w = (Widget)map.getElement("text");
        if (w != null) {
            mcc.add(w.getAppearance());
        }
        if (w != null) {
            final Color c1 = new Color(fadeIn ? Color.WHITE_ALPHA.get() : Color.WHITE.get());
            final Color c2 = new Color(fadeIn ? Color.WHITE.get() : Color.WHITE_ALPHA.get());
            final ModulationColorListTween tween = new ModulationColorListTween(c1, c2, mcc, 0, 500, 1, TweenFunction.PROGRESSIVE);
            if (!fadeIn) {
                tween.addTweenEventListener(new TweenEventListener() {
                    @Override
                    public void onTweenEvent(final AbstractTween tw, final TweenEvent e) {
                        switch (e) {
                            case TWEEN_ENDED: {
                                final ElementMap map = WakfuBubbleWidget.this.getElementMap();
                                Widget w = (Widget)map.getElement("image");
                                if (w != null) {
                                    w.getAppearance().setModulationColor(Color.WHITE);
                                }
                                w = (Widget)map.getElement("container");
                                if (w != null) {
                                    w.getAppearance().setModulationColor(Color.WHITE);
                                }
                                w = (Widget)map.getElement("text");
                                if (w != null) {
                                    w.getAppearance().setModulationColor(Color.WHITE);
                                }
                                WakfuBubbleWidget.this.cleanUp();
                                tween.removeTweenEventListener(this);
                            }
                            default: {}
                        }
                    }
                });
            }
            w.addTween(tween);
        }
    }
    
    @Override
    public void cellPositionChanged(final PathMobile target, final int worldX, final int worldY, final short altitude) {
        this.checkDistance(target);
    }
    
    private void checkDistance(final PathMobile target) {
        if (target == null || this.m_wakfuBubbleModeType == null) {
            return;
        }
        final int distance = target.getWorldCoordinates().getDistance(this.m_target.getWorldCellX(), this.m_target.getWorldCellY(), this.m_target.getWorldCellAltitude());
        DistanceEnum distanceEnum;
        if (distance > this.m_wakfuBubbleModeType.getFarDistance()) {
            distanceEnum = DistanceEnum.FAR;
        }
        else {
            distanceEnum = DistanceEnum.NORMAL;
        }
        if (distanceEnum != this.m_currentDistance) {
            if (this.m_resizingBubble || this.m_wakfuBubbleModeType == WakfuBubbleModeType.WHISPER) {
                this.applyDistance(distanceEnum);
            }
            this.m_currentDistance = distanceEnum;
        }
    }
    
    private void applyDistance(final DistanceEnum distanceEnum) {
        final TextWidget textWidget = (TextWidget)this.getElementMap().getElement("text");
        if (this.m_currentDistance != null) {
            textWidget.setZoomTween(distanceEnum.getZoomScale(), 500);
        }
        else {
            textWidget.setZoom(distanceEnum.getZoomScale());
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)WakfuBubbleWidget.class);
        WakfuBubbleWidget.UID = Integer.MIN_VALUE;
    }
    
    private enum DistanceEnum
    {
        FAR(0.25f), 
        NORMAL(1.0f);
        
        private float m_zoomScale;
        
        private DistanceEnum(final float zoomScale) {
            this.m_zoomScale = zoomScale;
        }
        
        public float getZoomScale() {
            return this.m_zoomScale;
        }
    }
}
