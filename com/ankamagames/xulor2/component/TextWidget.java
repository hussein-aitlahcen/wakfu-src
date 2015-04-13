package com.ankamagames.xulor2.component;

import com.ankamagames.xulor2.component.text.builder.*;
import java.util.*;
import com.ankamagames.xulor2.component.mesh.*;
import com.ankamagames.framework.graphics.engine.entity.*;
import com.ankamagames.xulor2.appearance.*;
import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.framework.kernel.core.translator.*;
import com.ankamagames.xulor2.util.alignment.*;
import com.ankamagames.xulor2.util.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.framework.graphics.engine.transformer.*;
import com.ankamagames.framework.graphics.engine.states.*;
import com.ankamagames.framework.graphics.engine.geometry.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.xulor2.core.converter.*;
import com.ankamagames.framework.java.util.*;
import com.ankamagames.framework.graphics.engine.text.*;
import com.ankamagames.xulor2.tween.*;

public abstract class TextWidget extends Widget implements ModulationColorClient, FontClient, ColorClient, Alignment9Client
{
    public static final String TEXT_COLOR = "text";
    private static final double EPSILON = 0.001;
    private TextBuilder m_textBuilder;
    protected Entity3D m_textEntity;
    protected boolean m_orientationIsDirty;
    protected String m_rawText;
    private static final Quaternion m_staticRotationQuaternion;
    private final ArrayList<TimedCharDisplayListener> m_timedCharDisplayListeners;
    private float m_zoom;
    private float m_innerZoom;
    private boolean m_enableAutoZoomShrink;
    private boolean m_enableAutoZoomShrinkInit;
    public static final int ALIGN_HASH;
    public static final int JUSTIFY_HASH;
    public static final int FONT_HASH;
    public static final int BRIGHTEN_COLOR_HASH;
    public static final int DARKEN_COLOR_HASH;
    public static final int HORIZONTAL_ALIGNMENT_HASH;
    public static final int VERTICAL_ALIGNMENT_HASH;
    public static final int MAX_WIDTH_HASH;
    public static final int MIN_WIDTH_HASH;
    public static final int MULTILINE_HASH;
    public static final int ORIENTATION_HASH;
    public static final int TEXT_HASH;
    public static final int ENABLE_SHRINKING_HASH;
    public static final int USE_HIGH_CONTRAST_HASH;
    public static final int DISPLAY_CHAR_DELAY_HASH;
    public static final int ZOOM_HASH;
    public static final int ENABLE_AUTO_ZOOM_SHRINK_HASH;
    
    public TextWidget() {
        super();
        this.m_orientationIsDirty = true;
        this.m_rawText = null;
        this.m_timedCharDisplayListeners = new ArrayList<TimedCharDisplayListener>();
        this.m_enableAutoZoomShrink = false;
        this.m_enableAutoZoomShrinkInit = false;
    }
    
    public void addTimedCharDisplayListener(final TimedCharDisplayListener timedCharDisplayListener) {
        if (!this.m_timedCharDisplayListeners.contains(timedCharDisplayListener)) {
            this.m_timedCharDisplayListeners.add(timedCharDisplayListener);
        }
    }
    
    public void removeTimedCharDisplayListener(final TimedCharDisplayListener timedCharDisplayListener) {
        if (this.m_timedCharDisplayListeners.contains(timedCharDisplayListener)) {
            this.m_timedCharDisplayListeners.remove(timedCharDisplayListener);
        }
    }
    
    public void displayAllTextImmediately() {
        final GLTextGeometry textGeometry = (GLTextGeometry)this.m_textEntity.getGeometry(0);
        textGeometry.setCurrentTime(-1L);
    }
    
    @Override
    protected void addInnerMeshes() {
        super.addInnerMeshes();
        this.m_entity.addChild(this.m_textEntity);
    }
    
    @Override
    public boolean isAppearanceCompatible(final DecoratorAppearance appearance) {
        return true;
    }
    
    public TextBuilder getTextBuilder() {
        return this.m_textBuilder;
    }
    
    protected void setTextBuilder(final TextBuilder textBuilder) {
        this.m_textBuilder = textBuilder;
        final GLTextGeometry textGeometry = (GLTextGeometry)this.m_textEntity.getGeometry(0);
        textGeometry.setTextBuilder(textBuilder);
    }
    
    @Override
    public void setModulationColor(final Color c) {
        final GLTextGeometry textGeometry = (GLTextGeometry)this.m_textEntity.getGeometry(0);
        textGeometry.setModulationColor(c);
    }
    
    @Override
    public Color getModulationColor() {
        final GLTextGeometry textGeometry = (GLTextGeometry)this.m_textEntity.getGeometry(0);
        return textGeometry.getModulationColor();
    }
    
    public String getText() {
        return this.m_textBuilder.getRawText();
    }
    
    public boolean isTextReduced() {
        return this.m_textBuilder.isTextReduced();
    }
    
    public void setText(final String text) {
        this.setText((Object)text);
    }
    
    public void setText(final Object text) {
        String rawText;
        if (text == null) {
            rawText = "";
        }
        else if (text instanceof Number) {
            rawText = Translator.getInstance().formatNumber(((Number)text).doubleValue());
        }
        else {
            rawText = String.valueOf(text);
        }
        if (this.m_rawText != null || !this.m_textBuilder.rawTextEquals(rawText)) {
            this.m_rawText = rawText;
            this.setNeedsToPreProcess();
        }
    }
    
    public void setZoomTween(final float zoom, final int duration) {
        this.removeTweensOfType(TextZoomTween.class);
        this.addTween(new TextZoomTween(Float.valueOf(this.m_zoom), Float.valueOf(zoom), this, 0, duration, TweenFunction.PROGRESSIVE));
    }
    
    public void setTextImmediate(final String text) {
        this.setText(text);
        this.applySetText();
    }
    
    public void setBrightenColor(final boolean brightenColor) {
        final GLTextGeometry geom = (GLTextGeometry)this.m_textEntity.getGeometry(0);
        geom.setBrightenColor(brightenColor);
    }
    
    public boolean getBrightenColor() {
        final GLTextGeometry geom = (GLTextGeometry)this.m_textEntity.getGeometry(0);
        return geom.isBrightenColor();
    }
    
    public void setDarkenColor(final boolean darkenColor) {
        final GLTextGeometry geom = (GLTextGeometry)this.m_textEntity.getGeometry(0);
        geom.setDarkenColor(darkenColor);
    }
    
    public boolean getDarkenColor() {
        final GLTextGeometry geom = (GLTextGeometry)this.m_textEntity.getGeometry(0);
        return geom.isDarkenColor();
    }
    
    public void setUseHighContrast(final boolean use) {
        this.m_textBuilder.setUseHighContrast(use);
    }
    
    public boolean getUseHighContrast() {
        return this.m_textBuilder.isUseHighContrast();
    }
    
    @Override
    public void setColor(final Color c, final String name) {
        if (name == null || name.equalsIgnoreCase("text")) {
            this.m_textBuilder.setDefaultColor(c);
        }
    }
    
    public boolean getEnableAutoZoomShrink() {
        return this.m_enableAutoZoomShrink;
    }
    
    public void setEnableAutoZoomShrink(final boolean enableAutoZoomShrink) {
        this.m_enableAutoZoomShrink = enableAutoZoomShrink;
        this.m_enableAutoZoomShrinkInit = true;
    }
    
    @Override
    public void setAlign(final Alignment9 align) {
        if (align.isNorth()) {
            this.setVerticalAlignment(Alignment5.NORTH);
        }
        else if (align.isSouth()) {
            this.setVerticalAlignment(Alignment5.SOUTH);
        }
        else {
            this.setVerticalAlignment(Alignment5.CENTER);
        }
        if (align.isWest()) {
            this.setHorizontalAlignment(Alignment5.WEST);
        }
        else if (align.isEast()) {
            this.setHorizontalAlignment(Alignment5.EAST);
        }
        else {
            this.setHorizontalAlignment(Alignment5.CENTER);
        }
    }
    
    public void setHorizontalAlignment(final Alignment5 alignment) {
        this.m_textBuilder.setDefaultHorizontalAlignment(alignment);
    }
    
    public void setVerticalAlignment(final Alignment5 alignment) {
        this.m_textBuilder.setVerticalAlignment(alignment);
    }
    
    public void setOrientation(final Orientation orientation) {
        this.m_textBuilder.setOrientation(orientation);
        this.m_orientationIsDirty = true;
        this.setNeedsToPostProcess();
    }
    
    public Orientation getOrientation() {
        return this.m_textBuilder.getOrientation();
    }
    
    public boolean isEnableAWTFont() {
        return this.getTextBuilder().getDocument().isEnableAWTFont();
    }
    
    public void setEnableAWTFont(final boolean enableAWTFont) {
        this.getTextBuilder().getDocument().setEnableAWTFont(enableAWTFont);
    }
    
    public void setMultiline(final boolean multiline) {
        this.m_textBuilder.setMultiline(multiline);
    }
    
    public boolean getMultiline() {
        return this.m_textBuilder.isMultiline();
    }
    
    public void setEnableShrinking(final boolean enableShrinking) {
        this.m_textBuilder.setEnableShrinking(enableShrinking);
    }
    
    public boolean getEnableShrinking() {
        return this.m_textBuilder.isEnableShrinking();
    }
    
    public void setMinWidth(final int width) {
        this.m_textBuilder.setMinWidth(width);
    }
    
    public int getMinWidth() {
        return this.m_textBuilder.getMinWidth();
    }
    
    public void setMaxWidth(final int width) {
        this.m_textBuilder.setMaxWidth(width);
    }
    
    public int getMaxWidth() {
        return this.m_textBuilder.getMaxWidth();
    }
    
    @Override
    public void setFont(final TextRenderer renderer) {
        this.m_textBuilder.setDefaultTextRenderer(renderer);
    }
    
    @Override
    public void setSize(final int width, final int height) {
        this.setTextWidgetSize(width, height, false);
    }
    
    public void setTextWidgetSize(final int width, final int height, final boolean updateMinWidth) {
        super.setSize(width, height);
        final Dimension d = this.m_appearance.getContentSize();
        if (this.m_enableAutoZoomShrink) {
            final Dimension minSize = this.m_textBuilder.getUnconstrainedMinSize();
            final float ratio = Math.min(d.width / minSize.width, d.height / minSize.height);
            if (ratio < 1.0f && Math.abs(this.m_innerZoom - ratio) > 0.001) {
                this.m_innerZoom = ratio;
                this.setNeedsToPostProcess();
            }
            else if (ratio >= 1.0f && Math.abs(this.m_innerZoom - 1.0f) > 0.001) {
                this.m_innerZoom = 1.0f;
                this.setNeedsToPostProcess();
            }
        }
        final float zoom = this.getAppliedZoom();
        if (updateMinWidth) {
            this.m_textBuilder.setMinWidth((int)(d.width / zoom));
        }
        this.m_textBuilder.setSize((int)Math.ceil(d.width / zoom), (int)Math.ceil(d.height / zoom));
    }
    
    private float getAppliedZoom() {
        return (this.m_zoom != 1.0f) ? this.m_zoom : this.m_innerZoom;
    }
    
    public void setZoom(final float zoom) {
        this.m_zoom = zoom;
        if (this.m_containerParent != null) {
            this.m_containerParent.invalidateMinSize();
        }
        this.setNeedsToPostProcess();
    }
    
    @Override
    public Dimension getContentMinSize() {
        final Dimension minSize = this.m_textBuilder.getMinSize();
        minSize.setWidth((int)(minSize.width * this.m_zoom));
        minSize.setHeight((int)(minSize.height * this.m_zoom));
        return minSize;
    }
    
    public void setDisplayCharDelay(final long displayCharDuration) {
        this.m_textBuilder.setCharsDelay(displayCharDuration);
    }
    
    public void setJustify(final boolean justify) {
        this.m_textBuilder.setJustify(justify);
    }
    
    protected void applySetText() {
        if (this.m_rawText != null) {
            this.m_textBuilder.setRawText(this.m_rawText);
            this.resetGeometryProgressTextTimer();
            this.m_rawText = null;
        }
    }
    
    public void resetGeometryProgressTextTimer() {
        ((GLTextGeometry)this.m_textEntity.getGeometry(0)).resetTextProgressionTimer();
    }
    
    @Override
    public void validate() {
        super.validate();
        if (this.m_textBuilder.isNeedToReflow()) {
            this.m_textBuilder.reflow();
        }
        if (this.m_textBuilder.isNeedToReflowSelection()) {
            this.m_textBuilder.reflowSelection();
        }
    }
    
    @Override
    public void invalidate() {
        super.invalidate();
        this.m_orientationIsDirty = true;
        this.setNeedsToPostProcess();
    }
    
    @Override
    public boolean preProcess(final int deltaTime) {
        this.applySetText();
        if (this.m_textBuilder.isNeedToComputeMinSize()) {
            this.m_textBuilder.computeMinSize();
            this.m_orientationIsDirty = true;
            this.setNeedsToPostProcess();
            if (this.m_containerParent != null) {
                this.m_containerParent.invalidateMinSize();
            }
        }
        return super.preProcess(deltaTime);
    }
    
    @Override
    public boolean middleProcess(final int deltaTime) {
        final boolean ret = super.middleProcess(deltaTime);
        if (this.m_textBuilder.isNeedToReflow() || this.m_textBuilder.isNeedToReflowSelection()) {
            this.invalidate();
        }
        return ret;
    }
    
    @Override
    public boolean postProcess(final int deltaTime) {
        final boolean ret = super.postProcess(deltaTime);
        if (!this.m_orientationIsDirty) {
            return ret;
        }
        final Dimension size = this.m_textBuilder.getSize();
        float left = 0.0f;
        float top = 0.0f;
        float angle = 0.0f;
        switch (this.getOrientation()) {
            case NORTH: {
                left = this.m_appearance.getLeftInset() + size.width;
                top = this.m_appearance.getBottomInset();
                angle = 1.5707964f;
                break;
            }
            case EAST: {
                left = this.m_appearance.getLeftInset();
                top = this.m_appearance.getBottomInset();
                angle = 0.0f;
                break;
            }
            case SOUTH: {
                left = this.m_appearance.getLeftInset();
                top = this.m_appearance.getBottomInset() + size.height;
                angle = -1.5707964f;
                break;
            }
            case WEST: {
                left = this.m_appearance.getLeftInset();
                top = this.m_appearance.getBottomInset() + size.height;
                angle = 3.1415927f;
                break;
            }
            default: {
                assert false : "We should never end here";
                break;
            }
        }
        final float zoom = this.getAppliedZoom();
        this.m_textEntity.getTransformer().setScale(0, zoom, zoom);
        final TransformerSRT transformer = (TransformerSRT)this.m_textEntity.getTransformer().getTransformer(1);
        transformer.setTranslation(left, top, 0.0f);
        TextWidget.m_staticRotationQuaternion.set(Vector3.AXIS_Z, angle);
        transformer.setRotation(TextWidget.m_staticRotationQuaternion);
        this.m_textEntity.getTransformer().setTransformer(1, transformer);
        this.m_orientationIsDirty = false;
        return ret;
    }
    
    public void onTimedCharDisplayedEnd() {
        for (int i = this.m_timedCharDisplayListeners.size() - 1; i >= 0; --i) {
            this.m_timedCharDisplayListeners.get(i).onTimedCharDisplayedEnd();
        }
    }
    
    @Override
    public void onCheckIn() {
        super.onCheckIn();
        if (this.m_textBuilder != null) {
            this.m_textBuilder.clean();
            this.m_textBuilder = null;
        }
        this.m_timedCharDisplayListeners.clear();
        this.m_textEntity.setPreRenderStates(null);
        this.m_textEntity.setPostRenderStates(null);
        this.m_textEntity.removeReference();
        this.m_textEntity = null;
    }
    
    @Override
    public void onCheckOut() {
        super.onCheckOut();
        assert this.m_textEntity == null;
        this.m_textEntity = Entity3D.Factory.newPooledInstance();
        TransformerSRT transformer = new TransformerSRT();
        transformer.setIdentity();
        this.m_textEntity.getTransformer().addTransformer(transformer);
        transformer = new TransformerSRT();
        transformer.setIdentity();
        this.m_textEntity.getTransformer().addTransformer(transformer);
        final GLTextGeometry geometry = GLTextGeometry.Factory.newPooledInstance();
        this.m_textEntity.addGeometry(geometry);
        geometry.removeReference();
        this.m_zoom = 1.0f;
        this.m_innerZoom = 1.0f;
        this.m_enableAutoZoomShrink = false;
        this.m_enableAutoZoomShrinkInit = false;
    }
    
    @Override
    public void copyElement(final BasicElement t) {
        final TextWidget e = (TextWidget)t;
        super.copyElement(e);
        e.setText(this.m_textBuilder.getRawText());
        if (this.m_rawText != null) {
            e.m_rawText = this.m_rawText;
        }
        e.setJustify(this.m_textBuilder.isJustify());
        e.setMaxWidth(this.m_textBuilder.getMaxWidth());
        e.setMinWidth(this.m_textBuilder.getMinWidth());
        e.setEnableShrinking(this.getEnableShrinking());
        e.setMultiline(this.getMultiline());
        e.setOrientation(this.getOrientation());
        e.setUseHighContrast(this.getUseHighContrast());
        e.setBrightenColor(this.getBrightenColor());
        e.setDarkenColor(this.getDarkenColor());
        e.setZoom(this.m_zoom);
        if (this.m_enableAutoZoomShrinkInit) {
            e.setEnableAutoZoomShrink(this.m_enableAutoZoomShrink);
        }
        e.setEnableAWTFont(this.isEnableAWTFont());
    }
    
    @Override
    public String toString() {
        return super.toString() + ((this.m_textBuilder != null) ? (" : " + this.getText()) : "");
    }
    
    @Override
    public boolean setXMLAttribute(final int hash, final String value, final ConverterLibrary cl) {
        if (hash == TextWidget.ALIGN_HASH) {
            this.setAlign(Alignment9.value(value));
        }
        else if (hash == TextWidget.JUSTIFY_HASH) {
            this.setJustify(PrimitiveConverter.getBoolean(value));
        }
        else if (hash == TextWidget.FONT_HASH) {
            this.setFont(cl.convertToFont(value));
        }
        else if (hash == TextWidget.HORIZONTAL_ALIGNMENT_HASH) {
            this.setHorizontalAlignment(Alignment5.value(value));
        }
        else if (hash == TextWidget.BRIGHTEN_COLOR_HASH) {
            this.setBrightenColor(PrimitiveConverter.getBoolean(value));
        }
        else if (hash == TextWidget.DARKEN_COLOR_HASH) {
            this.setDarkenColor(PrimitiveConverter.getBoolean(value));
        }
        else if (hash == TextWidget.VERTICAL_ALIGNMENT_HASH) {
            this.setVerticalAlignment(Alignment5.value(value));
        }
        else if (hash == TextWidget.MAX_WIDTH_HASH) {
            this.setMaxWidth(PrimitiveConverter.getInteger(value));
        }
        else if (hash == TextWidget.MIN_WIDTH_HASH) {
            this.setMinWidth(PrimitiveConverter.getInteger(value));
        }
        else if (hash == TextWidget.MULTILINE_HASH) {
            this.setMultiline(PrimitiveConverter.getBoolean(value));
        }
        else if (hash == TextWidget.ORIENTATION_HASH) {
            this.setOrientation(Orientation.value(value));
        }
        else if (hash == TextWidget.TEXT_HASH) {
            this.setText(cl.convertToString(value, this.m_elementMap));
        }
        else if (hash == TextWidget.USE_HIGH_CONTRAST_HASH) {
            this.setUseHighContrast(PrimitiveConverter.getBoolean(value));
        }
        else if (hash == TextWidget.ENABLE_SHRINKING_HASH) {
            this.setEnableShrinking(PrimitiveConverter.getBoolean(value));
        }
        else if (hash == TextWidget.ZOOM_HASH) {
            this.setZoom(PrimitiveConverter.getFloat(value));
        }
        else if (hash == TextWidget.DISPLAY_CHAR_DELAY_HASH) {
            this.setDisplayCharDelay(PrimitiveConverter.getLong(value));
        }
        else {
            if (hash != TextWidget.ENABLE_AUTO_ZOOM_SHRINK_HASH) {
                return super.setXMLAttribute(hash, value, cl);
            }
            this.setEnableAutoZoomShrink(PrimitiveConverter.getBoolean(value));
        }
        return true;
    }
    
    @Override
    public boolean setPropertyAttribute(final int hash, final Object value) {
        if (hash == TextWidget.TEXT_HASH) {
            this.setText(value);
        }
        else if (hash == TextWidget.ALIGN_HASH) {
            this.setAlign((Alignment9)value);
        }
        else if (hash == TextWidget.JUSTIFY_HASH) {
            this.setJustify(PrimitiveConverter.getBoolean(value));
        }
        else if (hash == TextWidget.FONT_HASH) {
            this.setFont((TextRenderer)value);
        }
        else if (hash == TextWidget.BRIGHTEN_COLOR_HASH) {
            this.setBrightenColor(PrimitiveConverter.getBoolean(value));
        }
        else if (hash == TextWidget.DARKEN_COLOR_HASH) {
            this.setDarkenColor(PrimitiveConverter.getBoolean(value));
        }
        else if (hash == TextWidget.HORIZONTAL_ALIGNMENT_HASH) {
            this.setHorizontalAlignment((Alignment5)value);
        }
        else if (hash == TextWidget.VERTICAL_ALIGNMENT_HASH) {
            this.setVerticalAlignment((Alignment5)value);
        }
        else if (hash == TextWidget.MAX_WIDTH_HASH) {
            this.setMaxWidth(PrimitiveConverter.getInteger(value));
        }
        else if (hash == TextWidget.MIN_WIDTH_HASH) {
            this.setMinWidth(PrimitiveConverter.getInteger(value));
        }
        else if (hash == TextWidget.MULTILINE_HASH) {
            this.setMultiline(PrimitiveConverter.getBoolean(value));
        }
        else if (hash == TextWidget.ORIENTATION_HASH) {
            this.setOrientation((Orientation)value);
        }
        else if (hash == TextWidget.ENABLE_SHRINKING_HASH) {
            this.setEnableShrinking(PrimitiveConverter.getBoolean(value));
        }
        else if (hash == TextWidget.USE_HIGH_CONTRAST_HASH) {
            this.setUseHighContrast(PrimitiveConverter.getBoolean(value));
        }
        else if (hash == TextWidget.ZOOM_HASH) {
            this.setZoom(PrimitiveConverter.getFloat(value));
        }
        else if (hash == TextWidget.DISPLAY_CHAR_DELAY_HASH) {
            this.setDisplayCharDelay(PrimitiveConverter.getLong(value));
        }
        else {
            if (hash != TextWidget.ENABLE_AUTO_ZOOM_SHRINK_HASH) {
                return super.setPropertyAttribute(hash, value);
            }
            this.setEnableAutoZoomShrink(PrimitiveConverter.getBoolean(value));
        }
        return true;
    }
    
    static {
        m_staticRotationQuaternion = new Quaternion();
        ALIGN_HASH = "align".hashCode();
        JUSTIFY_HASH = "justify".hashCode();
        FONT_HASH = "font".hashCode();
        BRIGHTEN_COLOR_HASH = "brightenColor".hashCode();
        DARKEN_COLOR_HASH = "darkenColor".hashCode();
        HORIZONTAL_ALIGNMENT_HASH = "horizontalAlignment".hashCode();
        VERTICAL_ALIGNMENT_HASH = "verticalAlignment".hashCode();
        MAX_WIDTH_HASH = "maxWidth".hashCode();
        MIN_WIDTH_HASH = "minWidth".hashCode();
        MULTILINE_HASH = "multiline".hashCode();
        ORIENTATION_HASH = "orientation".hashCode();
        TEXT_HASH = "text".hashCode();
        ENABLE_SHRINKING_HASH = "enableShrinking".hashCode();
        USE_HIGH_CONTRAST_HASH = "useHighContrast".hashCode();
        DISPLAY_CHAR_DELAY_HASH = "displayCharDelay".hashCode();
        ZOOM_HASH = "zoom".hashCode();
        ENABLE_AUTO_ZOOM_SHRINK_HASH = "enableAutoZoomShrink".hashCode();
    }
    
    public static class TextZoomTween extends AbstractWidgetTween<Float>
    {
        public TextZoomTween(final Float a, final Float b, final TextWidget w, final int delay, final int duration, final TweenFunction function) {
            super(a, b, w, delay, duration, function);
        }
        
        @Override
        public TextWidget getWidget() {
            return (TextWidget)super.getWidget();
        }
        
        @Override
        public boolean process(final int deltaTime) {
            if (!super.process(deltaTime)) {
                return false;
            }
            if (this.m_function != null) {
                final float zoom = this.m_function.compute((float)this.m_a, (float)this.m_b, this.m_elapsedTime, this.m_duration);
                this.getWidget().setZoom(zoom);
            }
            return true;
        }
        
        @Override
        public void onEnd() {
            this.getWidget().setZoom((float)this.m_b);
        }
    }
    
    public interface TimedCharDisplayListener
    {
        void onTimedCharDisplayedEnd();
    }
}
