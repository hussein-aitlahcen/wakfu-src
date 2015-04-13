package com.ankamagames.xulor2.component;

import com.ankamagames.xulor2.appearance.*;
import com.ankamagames.xulor2.util.*;
import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.xulor2.util.alignment.*;
import java.awt.*;
import com.ankamagames.framework.kernel.core.maths.*;
import org.jetbrains.annotations.*;
import java.util.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.xulor2.core.converter.*;
import com.ankamagames.framework.java.util.*;
import com.ankamagames.xulor2.component.mesh.*;
import com.ankamagames.xulor2.tween.*;
import com.ankamagames.xulor2.layout.*;

public class ProgressBar extends Container implements ModulationColorClient
{
    public static final String TAG = "progressBar";
    private AbstractProgressBarMesh m_progressBarMesh;
    private boolean m_horizontal;
    private float m_fullCirclePercentage;
    private float m_deltaAngle;
    public static final float DEFAULT_MIN_VALUE = 0.0f;
    public static final float DEFAULT_MAX_VALUE = 1.0f;
    private float m_minBound;
    private float m_maxBound;
    private float m_value;
    private float m_initialValue;
    private float m_visualValue;
    private boolean m_useIncreaseProgressTween;
    private boolean m_useDecreaseProgressTween;
    private ProgressBarDisplayType m_displayType;
    private boolean m_inversed;
    private float[] m_markers;
    private ArrayList<Image> m_markersImages;
    private boolean m_markersValuesDirty;
    private PixmapElement m_pixmap;
    private long m_tweenDuration;
    private TweenFunction m_tweenFunction;
    public static final int HORIZONTAL_HASH;
    public static final int FULL_CIRCLE_PERCENTAGE_HASH;
    public static final int DELTA_ANGLE_HASH;
    public static final int MAX_BOUND_HASH;
    public static final int MIN_BOUND_HASH;
    public static final int DISPLAY_TYPE_HASH;
    public static final int USE_INCREASE_PROGRESS_TWEEN_HASH;
    public static final int USE_DECREASE_PROGRESS_TWEEN_HASH;
    public static final int VALUE_HASH;
    public static final int INITIAL_VALUE_HASH;
    public static final int INVERSED_HASH;
    public static final int MARKERS_HASH;
    public static final int TWEEN_DURATION_HASH;
    public static final int INNER_RADIUS_FACTOR_HASH;
    public static final int OUTER_RADIUS_FACTOR_HASH;
    
    public ProgressBar() {
        super();
        this.m_horizontal = true;
        this.m_fullCirclePercentage = 1.0f;
        this.m_deltaAngle = 1.5707964f;
        this.m_minBound = 0.0f;
        this.m_maxBound = 1.0f;
        this.m_value = 0.0f;
        this.m_initialValue = 0.0f;
        this.m_visualValue = 0.0f;
        this.m_useIncreaseProgressTween = true;
        this.m_useDecreaseProgressTween = true;
        this.m_inversed = false;
        this.m_markers = null;
        this.m_markersImages = new ArrayList<Image>();
        this.m_markersValuesDirty = false;
        this.m_pixmap = null;
        this.m_tweenDuration = -1L;
    }
    
    @Override
    public void add(final EventDispatcher e) {
        if (e instanceof PixmapElement) {
            this.setPixmap((PixmapElement)e);
        }
        super.add(e);
    }
    
    public void setPixmap(final PixmapElement p) {
        if (p != this.m_pixmap) {
            this.m_pixmap = p;
            this.setNeedsToPreProcess();
        }
    }
    
    @Override
    public void invalidate() {
        this.m_markersValuesDirty = true;
        super.invalidate();
    }
    
    @Override
    protected void addInnerMeshes() {
        super.addInnerMeshes();
        this.m_entity.addChild(this.m_progressBarMesh.getEntity());
    }
    
    @Override
    public String getTag() {
        return "progressBar";
    }
    
    @Override
    public boolean isAppearanceCompatible(final DecoratorAppearance appearance) {
        return appearance instanceof ProgressBarAppearance;
    }
    
    @Override
    public ProgressBarAppearance getAppearance() {
        return (ProgressBarAppearance)this.m_appearance;
    }
    
    public boolean getUseIncreaseProgressTween() {
        return this.m_useIncreaseProgressTween;
    }
    
    public void setUseIncreaseProgressTween(final boolean useProgressTween) {
        this.m_useIncreaseProgressTween = useProgressTween;
    }
    
    public boolean getUseDecreaseProgressTween() {
        return this.m_useDecreaseProgressTween;
    }
    
    public void setUseDecreaseProgressTween(final boolean useProgressTween) {
        this.m_useDecreaseProgressTween = useProgressTween;
    }
    
    public void setPixmaps(final PixmapElement northWest, final PixmapElement north, final PixmapElement northEast, final PixmapElement west, final PixmapElement center, final PixmapElement east, final PixmapElement southWest, final PixmapElement south, final PixmapElement southEast) {
        if (this.m_displayType == ProgressBarDisplayType.PLAIN) {
            this.setDisplayType(ProgressBarDisplayType.PIXMAP);
        }
        this.m_progressBarMesh.setPixmaps(northWest.getPixmap(), north.getPixmap(), northEast.getPixmap(), west.getPixmap(), center.getPixmap(), east.getPixmap(), southWest.getPixmap(), south.getPixmap(), southEast.getPixmap());
        final int minWidth = northWest.getPixmap().getWidth() + northEast.getPixmap().getWidth();
        final int minHeight = northWest.getPixmap().getHeight() + southWest.getPixmap().getHeight();
        this.setMinSize(new Dimension(minWidth, minHeight));
    }
    
    @Override
    public void setModulationColor(final Color c) {
        if (this.m_progressBarMesh != null) {
            this.m_progressBarMesh.setModulationColor(c);
        }
        for (int i = this.m_markersImages.size() - 1; i >= 0; --i) {
            this.m_markersImages.get(i).setModulationColor(c);
        }
    }
    
    @Override
    public Color getModulationColor() {
        if (this.m_progressBarMesh != null) {
            return this.m_progressBarMesh.getModulationColor();
        }
        return null;
    }
    
    public void setColor(final Color color, final String name) {
        if (name == null || name.equalsIgnoreCase("progressBar")) {
            this.m_progressBarMesh.setColor(color);
            this.setMinSize(new Dimension(0, 0));
        }
        else if (name.equals("progressBarBorder")) {
            this.m_progressBarMesh.setBorderColor(color);
        }
    }
    
    public void setInnerPosition(final Alignment9 position) {
        if (this.m_progressBarMesh != null) {
            this.m_progressBarMesh.setPosition(position);
        }
    }
    
    public void setInnerBorder(final Insets innerBorder) {
        if (this.m_progressBarMesh != null) {
            this.m_progressBarMesh.setBorder(innerBorder);
        }
        this.setMinSize(new Dimension(innerBorder.left + innerBorder.right, innerBorder.bottom + innerBorder.top));
    }
    
    public float getMinBound() {
        return this.m_minBound;
    }
    
    public void setMinBound(final float minBound) {
        this.m_minBound = minBound;
        if (this.m_value < this.m_minBound) {
            this.setMinBound(this.m_minBound);
        }
        this.updateProgressBarMesh();
    }
    
    public float getMaxBound() {
        return this.m_maxBound;
    }
    
    public void setMaxBound(final float maxBound) {
        this.m_maxBound = maxBound;
        if (this.m_value > this.m_maxBound) {
            this.setValue(this.m_maxBound);
        }
        this.updateProgressBarMesh();
    }
    
    public float getPercentage() {
        return Math.max(0.0f, Math.min(1.0f, (this.m_value - this.m_minBound) / (this.m_maxBound - this.m_minBound)));
    }
    
    private float getVisualPercentage() {
        return Math.max(0.0f, Math.min(1.0f, (this.m_visualValue - this.m_minBound) / (this.m_maxBound - this.m_minBound)));
    }
    
    public float getValue() {
        return this.m_value;
    }
    
    public AbstractTween setValue(float value) {
        value = MathHelper.clamp(value, this.m_minBound, this.m_maxBound);
        if (this.m_value == value) {
            return null;
        }
        final boolean useTween = (this.m_useIncreaseProgressTween && value > this.m_value) || (this.m_useDecreaseProgressTween && value < this.m_value);
        this.removeTweensOfType(ProgressBarTween.class);
        final AbstractTween tw = new ProgressBarTween(this.m_inversed ? (this.m_maxBound - this.m_visualValue) : this.m_visualValue, this.m_inversed ? (this.m_maxBound - value) : value, this, 0, useTween ? ((int)((this.m_tweenDuration != -1L) ? this.m_tweenDuration : 500L)) : 0, this.m_tweenFunction);
        this.addTween(tw);
        this.m_value = value;
        return tw;
    }
    
    public TweenFunction getTweenFunction() {
        return this.m_tweenFunction;
    }
    
    public void setTweenFunction(@NotNull final TweenFunction tweenFunction) {
        this.m_tweenFunction = tweenFunction;
    }
    
    public float getInitialValue() {
        return this.m_initialValue;
    }
    
    public void setInitialValue(final float initialValue) {
        this.m_initialValue = initialValue;
        this.m_visualValue = initialValue;
        this.m_value = this.m_initialValue;
        this.updateProgressBarMesh();
    }
    
    public float getFullCirclePercentage() {
        return this.m_fullCirclePercentage;
    }
    
    public void setFullCirclePercentage(final float fullCirclePercentage) {
        this.m_fullCirclePercentage = fullCirclePercentage;
        if (this.m_progressBarMesh != null) {
            this.m_progressBarMesh.setFullCirclePercentage(fullCirclePercentage);
        }
    }
    
    public float getDeltaAngle() {
        return this.m_deltaAngle;
    }
    
    public void setDeltaAngle(final float deltaAngle) {
        this.m_deltaAngle = deltaAngle;
        if (this.m_progressBarMesh != null) {
            this.m_progressBarMesh.setDeltaAngle(deltaAngle);
        }
    }
    
    public ProgressBarDisplayType getDisplayType() {
        return this.m_displayType;
    }
    
    public void setDisplayType(final ProgressBarDisplayType displayType) {
        if (this.m_displayType != displayType || this.m_progressBarMesh == null) {
            this.m_displayType = displayType;
            Color c = null;
            Color bc = null;
            Color mc = null;
            Insets border = null;
            if (this.m_progressBarMesh != null) {
                c = this.m_progressBarMesh.getColor();
                bc = this.m_progressBarMesh.getBorderColor();
                mc = this.m_progressBarMesh.getModulationColor();
                border = this.m_progressBarMesh.getBorder();
                this.m_progressBarMesh.onCheckIn();
            }
            (this.m_progressBarMesh = this.m_displayType.getNewMesh()).onCheckOut();
            this.m_progressBarMesh.setHorizontal(this.m_horizontal);
            this.m_progressBarMesh.setColor(c);
            this.m_progressBarMesh.setBorderColor(bc);
            this.m_progressBarMesh.setModulationColor(mc);
            if (border != null) {
                this.m_progressBarMesh.setBorder(border);
            }
            this.m_progressBarMesh.setFullCirclePercentage(this.m_fullCirclePercentage);
            this.m_progressBarMesh.setDeltaAngle(this.m_deltaAngle);
            if (this.m_appearance != null) {
                final ProgressBarAppearance app = (ProgressBarAppearance)this.m_appearance;
                app.applyPixmaps();
            }
        }
    }
    
    public boolean isHorizontal() {
        return this.m_horizontal;
    }
    
    public void setHorizontal(final boolean horizontal) {
        this.m_horizontal = horizontal;
        if (this.m_progressBarMesh != null) {
            this.m_progressBarMesh.setHorizontal(horizontal);
        }
    }
    
    public boolean isInversed() {
        return this.m_inversed;
    }
    
    public void setInversed(final boolean inversed) {
        this.m_inversed = inversed;
    }
    
    public void setInnerRadiusFactor(final float radiusFactor) {
        if (this.m_progressBarMesh instanceof CirclePlainProgressBarMesh) {
            ((CirclePlainProgressBarMesh)this.m_progressBarMesh).setInnerRadiusFactor(radiusFactor);
        }
    }
    
    public void setOuterRadiusFactor(final float radiusFactor) {
        if (this.m_progressBarMesh instanceof CirclePlainProgressBarMesh) {
            ((CirclePlainProgressBarMesh)this.m_progressBarMesh).setOuterRadiusFactor(radiusFactor);
        }
    }
    
    public void setMarkers(final float[] markers) {
        if (this.m_markers != null && Arrays.equals(this.m_markers, markers)) {
            return;
        }
        this.m_markers = markers;
        this.invalidate();
    }
    
    public void setTweenDuration(final long tweenDuration) {
        this.m_tweenDuration = tweenDuration;
    }
    
    public AbstractProgressBarMesh getProgressBarMesh() {
        return this.m_progressBarMesh;
    }
    
    @Override
    public void validate() {
        super.validate();
        this.updateProgressBarMesh();
    }
    
    public void updateProgressBarMesh() {
        if (this.m_progressBarMesh != null) {
            final int width = this.m_appearance.getContentWidth();
            final int height = this.m_appearance.getContentHeight();
            float visualPercentage = this.getVisualPercentage();
            int x;
            if (this.m_inversed) {
                x = this.m_appearance.getLeftInset() + (int)(width * visualPercentage) + 1;
                visualPercentage = 1.0f - visualPercentage;
            }
            else {
                x = this.m_appearance.getLeftInset();
            }
            final int y = this.m_appearance.getBottomInset();
            this.m_progressBarMesh.setGeometry(x, y, width, height, visualPercentage);
        }
    }
    
    @Override
    public boolean middleProcess(final int deltaTime) {
        final boolean ret = super.middleProcess(deltaTime);
        if (this.m_markersValuesDirty) {
            this.invalidate();
        }
        return ret;
    }
    
    @Override
    public void copyElement(final BasicElement source) {
        final ProgressBar pb = (ProgressBar)source;
        super.copyElement(source);
        pb.setDisplayType(this.getDisplayType());
        pb.setUseIncreaseProgressTween(this.m_useIncreaseProgressTween);
        pb.setUseDecreaseProgressTween(this.m_useDecreaseProgressTween);
        pb.setHorizontal(this.m_horizontal);
        pb.setValue(this.m_value);
        pb.setInitialValue(this.m_initialValue);
        pb.setMaxBound(this.m_maxBound);
        pb.setMinBound(this.m_minBound);
        pb.setFullCirclePercentage(this.m_fullCirclePercentage);
        pb.setDeltaAngle(this.m_deltaAngle);
        pb.m_pixmap = this.m_pixmap;
        pb.setInversed(this.m_inversed);
        pb.setTweenDuration(this.m_tweenDuration);
        pb.setNeedsToPreProcess();
    }
    
    @Override
    public void onCheckIn() {
        super.onCheckIn();
        this.m_progressBarMesh.onCheckIn();
        this.m_progressBarMesh = null;
        this.m_markersImages.clear();
        this.m_markersValuesDirty = false;
        this.m_pixmap = null;
        this.m_markers = null;
        this.m_tweenDuration = -1L;
    }
    
    @Override
    public void onCheckOut() {
        super.onCheckOut();
        final ProgressBarAppearance pba = new ProgressBarAppearance();
        pba.onCheckOut();
        pba.setWidget(this);
        this.add(pba);
        final ProgressBarLayout pbl = new ProgressBarLayout();
        pbl.onCheckOut();
        this.add(pbl);
        this.m_fullCirclePercentage = 1.0f;
        this.m_deltaAngle = 1.5707964f;
        this.m_horizontal = true;
        this.m_useDecreaseProgressTween = true;
        this.m_useIncreaseProgressTween = true;
        this.m_inversed = false;
        this.setDisplayType(ProgressBarDisplayType.PLAIN);
        this.setNonBlocking(false);
        this.m_tweenFunction = TweenFunction.LINEAR;
    }
    
    @Override
    public boolean setXMLAttribute(final int hash, final String value, final ConverterLibrary cl) {
        if (hash == ProgressBar.HORIZONTAL_HASH) {
            this.setHorizontal(PrimitiveConverter.getBoolean(value));
        }
        else if (hash == ProgressBar.DELTA_ANGLE_HASH) {
            this.setDeltaAngle(PrimitiveConverter.getFloat(value));
        }
        else if (hash == ProgressBar.MAX_BOUND_HASH) {
            this.setMaxBound(PrimitiveConverter.getFloat(value));
        }
        else if (hash == ProgressBar.FULL_CIRCLE_PERCENTAGE_HASH) {
            this.setFullCirclePercentage(PrimitiveConverter.getFloat(value));
        }
        else if (hash == ProgressBar.MIN_BOUND_HASH) {
            this.setMinBound(PrimitiveConverter.getFloat(value));
        }
        else if (hash == ProgressBar.USE_INCREASE_PROGRESS_TWEEN_HASH) {
            this.setUseIncreaseProgressTween(PrimitiveConverter.getBoolean(value));
        }
        else if (hash == ProgressBar.USE_DECREASE_PROGRESS_TWEEN_HASH) {
            this.setUseDecreaseProgressTween(PrimitiveConverter.getBoolean(value));
        }
        else if (hash == ProgressBar.VALUE_HASH) {
            this.setValue(PrimitiveConverter.getFloat(value));
        }
        else if (hash == ProgressBar.INITIAL_VALUE_HASH) {
            this.setInitialValue(PrimitiveConverter.getFloat(value));
        }
        else if (hash == ProgressBar.DISPLAY_TYPE_HASH) {
            this.setDisplayType(ProgressBarDisplayType.value(value));
        }
        else if (hash == ProgressBar.INVERSED_HASH) {
            this.setInversed(PrimitiveConverter.getBoolean(value));
        }
        else if (hash == ProgressBar.INNER_RADIUS_FACTOR_HASH) {
            this.setInnerRadiusFactor(PrimitiveConverter.getFloat(value));
        }
        else if (hash == ProgressBar.OUTER_RADIUS_FACTOR_HASH) {
            this.setOuterRadiusFactor(PrimitiveConverter.getFloat(value));
        }
        else {
            if (hash != ProgressBar.TWEEN_DURATION_HASH) {
                return super.setXMLAttribute(hash, value, cl);
            }
            this.setTweenDuration(PrimitiveConverter.getLong(value));
        }
        return true;
    }
    
    @Override
    public boolean setPropertyAttribute(final int hash, final Object value) {
        if (hash == ProgressBar.MAX_BOUND_HASH) {
            this.setMaxBound(PrimitiveConverter.getFloat(value));
        }
        else if (hash == ProgressBar.MIN_BOUND_HASH) {
            this.setMinBound(PrimitiveConverter.getFloat(value));
        }
        else if (hash == ProgressBar.VALUE_HASH) {
            this.setValue(PrimitiveConverter.getFloat(value));
        }
        else if (hash == ProgressBar.INITIAL_VALUE_HASH) {
            this.setInitialValue(PrimitiveConverter.getFloat(value));
        }
        else if (hash == ProgressBar.INVERSED_HASH) {
            this.setInversed(PrimitiveConverter.getBoolean(value));
        }
        else if (hash == ProgressBar.MARKERS_HASH) {
            if (value != null && !(value instanceof float[])) {
                return false;
            }
            this.setMarkers((float[])value);
        }
        else {
            if (hash != ProgressBar.TWEEN_DURATION_HASH) {
                return super.setPropertyAttribute(hash, value);
            }
            this.setTweenDuration(PrimitiveConverter.getLong(value));
        }
        return true;
    }
    
    static {
        HORIZONTAL_HASH = "horizontal".hashCode();
        FULL_CIRCLE_PERCENTAGE_HASH = "fullCirclePercentage".hashCode();
        DELTA_ANGLE_HASH = "deltaAngle".hashCode();
        MAX_BOUND_HASH = "maxBound".hashCode();
        MIN_BOUND_HASH = "minBound".hashCode();
        DISPLAY_TYPE_HASH = "displayType".hashCode();
        USE_INCREASE_PROGRESS_TWEEN_HASH = "useIncreaseProgressTween".hashCode();
        USE_DECREASE_PROGRESS_TWEEN_HASH = "useDecreaseProgressTween".hashCode();
        VALUE_HASH = "value".hashCode();
        INITIAL_VALUE_HASH = "initialValue".hashCode();
        INVERSED_HASH = "inversed".hashCode();
        MARKERS_HASH = "markers".hashCode();
        TWEEN_DURATION_HASH = "tweenDuration".hashCode();
        INNER_RADIUS_FACTOR_HASH = "innerRadius".hashCode();
        OUTER_RADIUS_FACTOR_HASH = "outerRadius".hashCode();
    }
    
    public enum ProgressBarDisplayType
    {
        PLAIN {
            @Override
            public AbstractProgressBarMesh getNewMesh() {
                return new PlainProgressBarMesh();
            }
        }, 
        PIXMAP {
            @Override
            public AbstractProgressBarMesh getNewMesh() {
                return new PixmapProgressBarMesh();
            }
        }, 
        CIRCLE {
            @Override
            public AbstractProgressBarMesh getNewMesh() {
                return new CirclePlainProgressBarMesh();
            }
        }, 
        HEART {
            @Override
            public AbstractProgressBarMesh getNewMesh() {
                return new HeartPlainProgressBarMesh();
            }
        }, 
        COMPASS {
            @Override
            public AbstractProgressBarMesh getNewMesh() {
                return new CompassProgressBarMesh();
            }
        }, 
        CIRCLEDESAT {
            @Override
            public AbstractProgressBarMesh getNewMesh() {
                return new CircleDesatProgressBarMesh();
            }
        };
        
        public abstract AbstractProgressBarMesh getNewMesh();
        
        public static ProgressBarDisplayType value(final String value) {
            final ProgressBarDisplayType[] arr$;
            final ProgressBarDisplayType[] values = arr$ = values();
            for (final ProgressBarDisplayType a : arr$) {
                if (a.name().equals(value.toUpperCase())) {
                    return a;
                }
            }
            return values[0];
        }
    }
    
    private class ProgressBarTween extends AbstractWidgetTween<Float>
    {
        private boolean m_running;
        
        private ProgressBarTween() {
            super();
            this.m_running = false;
        }
        
        private ProgressBarTween(final float a, final float b, final ProgressBar w, final int delay, final int duration, final TweenFunction function) {
            super(a, b, w, delay, duration, function);
            this.m_running = false;
        }
        
        public void update(final float a, final float b) {
            if (this.m_running) {
                this.m_duration += 500;
                this.m_b = (T)b;
            }
            this.m_running = true;
        }
        
        @Override
        public boolean process(final int deltaTime) {
            if (!super.process(deltaTime)) {
                return false;
            }
            if (this.m_function != null && this.m_a instanceof Float && this.m_b instanceof Float) {
                ProgressBar.this.m_visualValue = this.m_function.compute((float)this.m_a, (float)this.m_b, this.m_elapsedTime, this.m_duration);
                ProgressBar.this.updateProgressBarMesh();
            }
            return true;
        }
        
        @Override
        public void onEnd() {
            ProgressBar.this.m_visualValue = (float)this.m_b;
            ProgressBar.this.updateProgressBarMesh();
            super.onEnd();
        }
        
        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder();
            sb.append("[ProgressBarTween] ").append(this.m_a).append(" -> ").append(this.m_b);
            return sb.toString();
        }
    }
    
    private class ProgressBarLayout extends AbstractLayoutManager
    {
        public boolean canBeCloned() {
            return false;
        }
        
        @Override
        public Dimension getContentMinSize(final Container slider) {
            return new Dimension(0, 0);
        }
        
        @Override
        public Dimension getContentPreferedSize(final Container slider) {
            return new Dimension(0, 0);
        }
        
        @Override
        public void layoutContainer(final Container parent) {
            if (ProgressBar.this.m_markersValuesDirty) {
                if (ProgressBar.this.m_markers != null) {
                    while (ProgressBar.this.m_markers.length < ProgressBar.this.m_markersImages.size()) {
                        ProgressBar.this.m_markersImages.remove(ProgressBar.this.m_markers.length).destroySelfFromParent();
                    }
                    if (ProgressBar.this.m_markers.length > ProgressBar.this.m_markersImages.size()) {
                        if (ProgressBar.this.m_markersImages.size() == 0) {
                            final Image image = new Image();
                            image.onCheckOut();
                            image.setNonBlocking(true);
                            image.setModulationColor(ProgressBar.this.getModulationColor());
                            image.add(ProgressBar.this.m_pixmap.cloneElementStructure());
                            this.add(image);
                            ProgressBar.this.m_markersImages.add(image);
                        }
                        while (ProgressBar.this.m_markers.length > ProgressBar.this.m_markersImages.size()) {
                            final Image image = (Image)ProgressBar.this.m_markersImages.get(0).cloneElementStructure();
                            this.add(image);
                            ProgressBar.this.m_markersImages.add(image);
                        }
                    }
                }
                this.updateMarkersPosition();
                ProgressBar.this.m_markersValuesDirty = false;
            }
        }
        
        private void updateMarkersPosition() {
            if (ProgressBar.this.m_markers == null || ProgressBar.this.m_markers.length == 0 || ProgressBar.this.m_markersImages == null || ProgressBar.this.m_markersImages.size() != ProgressBar.this.m_markers.length) {
                return;
            }
            if (ProgressBar.this.m_horizontal) {
                for (int i = 0; i < ProgressBar.this.m_markersImages.size(); ++i) {
                    final Image image = ProgressBar.this.m_markersImages.get(i);
                    final float marker = ProgressBar.this.m_markers[i];
                    image.setPosition(Math.round(ProgressBar.this.getAppearance().getContentWidth() * marker), ProgressBar.this.getAppearance().getContentHeight() / 2 - image.getHeight() / 2);
                }
            }
            else {
                for (int i = 0; i < ProgressBar.this.m_markersImages.size(); ++i) {
                    final Image image = ProgressBar.this.m_markersImages.get(i);
                    final float marker = ProgressBar.this.m_markers[i];
                    image.setPosition(ProgressBar.this.getAppearance().getContentWidth() / 2 - image.getWidth() / 2, Math.round(ProgressBar.this.getAppearance().getContentHeight() * marker));
                }
            }
            ProgressBar.this.m_markersValuesDirty = false;
            this.setNeedsToMiddleProcess();
        }
    }
}
