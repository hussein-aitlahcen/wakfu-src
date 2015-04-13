package com.ankamagames.xulor2.component;

import com.ankamagames.xulor2.appearance.*;
import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.xulor2.util.*;
import java.util.*;
import com.ankamagames.xulor2.event.*;
import com.ankamagames.xulor2.core.event.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.xulor2.core.converter.*;
import com.ankamagames.framework.java.util.*;
import com.ankamagames.xulor2.layout.*;

public class Slider extends Container implements PixmapClient
{
    public static final String TAG = "Slider";
    public static final String THEME_HORIZONTAL_BUTTON = "horizontalButton";
    public static final String THEME_VERTICAL_BUTTON = "verticalButton";
    public static final String THEME_HORIZONTAL_SEPARATOR = "horizontalSeparator";
    public static final String THEME_VERTICAL_SEPARATOR = "verticalSeparator";
    private float m_value;
    private float m_unboundedValue;
    private int m_minBound;
    private int m_maxBound;
    private boolean m_valueIsDirty;
    private boolean m_buttonSizeIsDirty;
    private boolean m_fixedValues;
    private int m_numFixedValues;
    private boolean m_readOnly;
    private boolean m_useTween;
    private boolean m_horizontal;
    private float m_sliderSize;
    private float m_jump;
    private Button m_button;
    private int m_lastPressedX;
    private int m_lastPressedY;
    private EventListener m_mousePressedListener;
    private EventListener m_mouseDraggedListener;
    private ArrayList<Image> m_separatorsImages;
    private PixmapElement m_pixmap;
    private Color m_modulationColor;
    private boolean m_pixmapIsDirty;
    private boolean m_numFixedValuesDirty;
    public static final int READ_ONLY_HASH;
    public static final int USE_TWEEN_HASH;
    public static final int HORIZONTAL_HASH;
    public static final int MAX_BOUND_HASH;
    public static final int MIN_BOUND_HASH;
    public static final int JUMP_HASH;
    public static final int SLIDER_SIZE_HASH;
    public static final int VALUE_HASH;
    public static final int NUM_FIXED_VALUES_HASH;
    
    public Slider() {
        super();
        this.m_value = 0.0f;
        this.m_unboundedValue = 0.0f;
        this.m_minBound = 0;
        this.m_maxBound = 1;
        this.m_valueIsDirty = true;
        this.m_buttonSizeIsDirty = true;
        this.m_fixedValues = false;
        this.m_numFixedValues = 0;
        this.m_readOnly = false;
        this.m_useTween = false;
        this.m_horizontal = true;
        this.m_sliderSize = 0.15f;
        this.m_jump = 0.05f;
        this.m_mousePressedListener = null;
        this.m_mouseDraggedListener = null;
        this.m_separatorsImages = new ArrayList<Image>();
        this.m_pixmap = null;
        this.m_modulationColor = null;
        this.m_pixmapIsDirty = false;
        this.m_numFixedValuesDirty = false;
    }
    
    @Override
    public void add(final EventDispatcher e) {
        if (e instanceof Button && this.m_button != e) {
            if (this.m_button != null) {
                this.destroy(this.m_button);
            }
            (this.m_button = (Button)e).setUsePositionTween(this.m_readOnly && this.m_useTween);
        }
        else if (e instanceof PixmapElement) {
            this.setPixmap((PixmapElement)e);
        }
        super.add(e);
    }
    
    @Override
    public String getTag() {
        return "Slider";
    }
    
    public float getValue() {
        return this.convertToBoundedValue(this.m_value);
    }
    
    public void setValue(float value) {
        if (value == this.m_value) {
            return;
        }
        final int minBound = this.m_fixedValues ? 0 : this.m_minBound;
        final int maxBound = this.m_fixedValues ? (this.m_numFixedValues - 1) : this.m_maxBound;
        this.m_unboundedValue = value;
        if (value < minBound) {
            value = minBound;
        }
        else if (value > maxBound) {
            value = maxBound;
        }
        this.m_value = value;
        final SliderMovedEvent e = new SliderMovedEvent(this);
        e.setValue(value);
        this.dispatchEvent(e);
        this.m_valueIsDirty = true;
        this.setNeedsToMiddleProcess();
    }
    
    public void setNumFixedValues(final int numFixedValues) {
        assert numFixedValues >= 0 : "Invalid value. numFixedValues must be >= 0";
        if (numFixedValues == this.m_numFixedValues) {
            return;
        }
        this.m_numFixedValues = numFixedValues;
        this.m_fixedValues = (this.m_numFixedValues != 0);
        this.m_numFixedValuesDirty = true;
        if (this.m_fixedValues) {
            this.setValue(this.m_unboundedValue);
        }
    }
    
    public int getMinBound() {
        return this.m_minBound;
    }
    
    public void setMinBound(final int minBound) {
        this.m_minBound = minBound;
    }
    
    public int getMaxBound() {
        return this.m_maxBound;
    }
    
    public void setMaxBound(final int maxBound) {
        this.m_maxBound = maxBound;
    }
    
    public double getJump() {
        return this.m_jump;
    }
    
    public void setJump(float jump) {
        if (jump < 0.0f) {
            jump = 0.0f;
        }
        else if (jump > 1.0f) {
            jump = 1.0f;
        }
        this.m_jump = jump;
    }
    
    public boolean isHorizontal() {
        return this.m_horizontal;
    }
    
    public void setHorizontal(final boolean horizontal) {
        this.m_horizontal = horizontal;
    }
    
    public double getSliderSize() {
        return this.m_sliderSize;
    }
    
    public void setSliderSize(float size) {
        size = Math.min(1.0f, Math.max(size, 0.01f));
        this.m_sliderSize = size;
        this.m_buttonSizeIsDirty = true;
        this.setNeedsToMiddleProcess();
    }
    
    public Button getButton() {
        return this.m_button;
    }
    
    @Override
    public Widget getWidgetByThemeElementName(final String themeElementName, final boolean compilationMode) {
        if ((this.m_horizontal || compilationMode) && "horizontalButton".equalsIgnoreCase(themeElementName)) {
            return this.m_button;
        }
        if ((!this.m_horizontal || compilationMode) && "verticalButton".equalsIgnoreCase(themeElementName)) {
            return this.m_button;
        }
        return null;
    }
    
    @Override
    public void setEnabled(final boolean enabled) {
        super.setEnabled(enabled);
        this.m_button.setEnabled(enabled);
    }
    
    @Override
    public void setNetEnabled(final boolean netEnabled) {
        super.setNetEnabled(netEnabled);
        this.m_button.setNetEnabled(netEnabled);
    }
    
    public boolean getReadOnly() {
        return this.m_readOnly;
    }
    
    public void setReadOnly(final boolean readOnly) {
        this.m_readOnly = readOnly;
        this.m_button.setUsePositionTween(this.m_readOnly && this.m_useTween);
    }
    
    public void setUseTween(final boolean useTween) {
        this.m_useTween = useTween;
        this.m_button.setUsePositionTween(this.m_readOnly && this.m_useTween);
    }
    
    @Override
    public void setPixmap(final PixmapElement p) {
        if (p != this.m_pixmap) {
            this.m_pixmap = p;
            this.m_pixmapIsDirty = true;
            this.setNeedsToPreProcess();
        }
    }
    
    @Override
    public void setModulationColor(final Color c) {
        if (this.m_modulationColor == c) {
            return;
        }
        this.m_modulationColor = c;
        for (int i = this.m_separatorsImages.size() - 1; i >= 0; --i) {
            this.m_separatorsImages.get(i).setModulationColor(c);
        }
    }
    
    @Override
    public Color getModulationColor() {
        return this.m_modulationColor;
    }
    
    private void updateSeparatorsPosition() {
        if (this.m_numFixedValues <= 1 || !this.m_fixedValues || this.m_separatorsImages.size() <= 0) {
            return;
        }
        if (this.m_horizontal) {
            final int imageWidth = this.m_separatorsImages.get(0).getWidth();
            final int startOffset = this.m_button.getWidth() / 2 - imageWidth / 2;
            final float offSet = (this.getAppearance().getContentWidth() - this.m_button.getWidth()) / (this.m_numFixedValues - 1);
            for (int i = 0; i < this.m_separatorsImages.size(); ++i) {
                final Image image = this.m_separatorsImages.get(i);
                image.setPosition((int)(offSet * i + startOffset), 0);
            }
        }
        else {
            final int imageHeight = this.m_separatorsImages.get(0).getHeight();
            final int startOffset = this.m_button.getHeight() / 2 - imageHeight / 2;
            final float offSet = (this.getAppearance().getContentHeight() - this.m_button.getHeight()) / (this.m_numFixedValues - 1);
            for (int i = 0; i < this.m_separatorsImages.size(); ++i) {
                final Image image = this.m_separatorsImages.get(i);
                image.setPosition(0, (int)(offSet * i + startOffset));
            }
        }
        this.m_numFixedValuesDirty = false;
        this.setNeedsToMiddleProcess();
    }
    
    @Override
    public void invalidate() {
        this.m_buttonSizeIsDirty = true;
        this.m_valueIsDirty = true;
        this.m_numFixedValuesDirty = true;
        this.setNeedsToMiddleProcess();
        super.invalidate();
    }
    
    private float convertToBoundedValue(final float value) {
        final int minBound = this.m_fixedValues ? 0 : this.m_minBound;
        final int maxBound = this.m_fixedValues ? (this.m_numFixedValues - 1) : this.m_maxBound;
        float ret = value * (maxBound - minBound) + minBound;
        if (this.m_fixedValues) {
            ret = Math.round(ret);
        }
        return ret;
    }
    
    private float convertFromBoundedValue(final float value) {
        final int minBound = this.m_fixedValues ? 0 : this.m_minBound;
        final int maxBound = this.m_fixedValues ? (this.m_numFixedValues - 1) : this.m_maxBound;
        return (value - minBound) / (maxBound - minBound);
    }
    
    public void updateButtonSize() {
        final Dimension buttonPrefSize = this.m_button.getPrefSize();
        int buttonHeight;
        int buttonWidth;
        if (this.m_horizontal) {
            buttonHeight = this.getAppearance().getContentHeight();
            buttonWidth = Math.max((int)(this.getAppearance().getContentWidth() * this.m_sliderSize), buttonPrefSize.width);
        }
        else {
            buttonWidth = this.getAppearance().getContentWidth();
            buttonHeight = Math.max((int)(this.getAppearance().getContentHeight() * this.m_sliderSize), buttonPrefSize.height);
        }
        this.m_button.setSize(buttonWidth, buttonHeight);
        if (this.m_separatorsImages != null && this.m_separatorsImages.size() > 0) {
            final Dimension prefSize = this.m_separatorsImages.get(0).getPrefSize();
            final int width = this.m_horizontal ? prefSize.width : buttonWidth;
            final int height = this.m_horizontal ? buttonHeight : prefSize.height;
            for (final Image image : this.m_separatorsImages) {
                image.setSize(width, height);
            }
        }
        this.m_buttonSizeIsDirty = false;
        this.setNeedsToMiddleProcess();
    }
    
    public void updateButtonPosition() {
        int y;
        int x;
        if (this.m_horizontal) {
            y = 0;
            x = (int)(this.convertFromBoundedValue(this.m_value) * Math.max(0, this.getAppearance().getContentWidth() - this.m_button.getWidth()));
        }
        else {
            x = 0;
            y = (int)(this.convertFromBoundedValue(this.m_value) * Math.max(0, this.getAppearance().getContentHeight() - this.m_button.getHeight()));
        }
        this.m_button.setPosition(x, y);
        this.m_valueIsDirty = false;
        this.setNeedsToMiddleProcess();
    }
    
    public void addSliderListeners() {
        this.m_mousePressedListener = new EventListener() {
            @Override
            public boolean run(final Event event) {
                if (Slider.this.m_readOnly) {
                    return false;
                }
                final MouseEvent e = (MouseEvent)event;
                if (event.getTarget() == Slider.this.m_button) {
                    Slider.this.m_lastPressedX = e.getX(Slider.this.m_button) - Slider.this.m_button.getWidth() / 2;
                    Slider.this.m_lastPressedY = e.getY(Slider.this.m_button) - Slider.this.m_button.getHeight() / 2;
                }
                else if (event.getTarget() == event.getCurrentTarget()) {
                    final float delta = Slider.this.m_fixedValues ? (1.0f / Slider.this.m_numFixedValues + 0.001f) : Slider.this.m_jump;
                    if ((Slider.this.m_horizontal && e.getX(e.getTarget()) < Slider.this.m_button.getX()) || (!Slider.this.m_horizontal && e.getY(e.getTarget()) < Slider.this.m_button.getY())) {
                        Slider.this.setValue(Slider.this.convertToBoundedValue(Slider.this.convertFromBoundedValue(Slider.this.m_value) - delta));
                    }
                    else {
                        Slider.this.setValue(Slider.this.convertToBoundedValue(Slider.this.convertFromBoundedValue(Slider.this.m_value) + delta));
                    }
                }
                return false;
            }
        };
        this.addEventListener(Events.MOUSE_PRESSED, this.m_mousePressedListener, false);
        this.m_mouseDraggedListener = new EventListener() {
            @Override
            public boolean run(final Event event) {
                if (Slider.this.m_readOnly || event.getTarget() != Slider.this.m_button) {
                    return false;
                }
                final MouseEvent e = (MouseEvent)event;
                if (Slider.this.m_horizontal) {
                    int x = e.getX(e.getCurrentTarget()) - Slider.this.m_lastPressedX;
                    final float buttonWidthByTwoFloor = (int)Math.floor(Slider.this.m_button.getWidth() / 2.0);
                    final float buttonWidthByTwoCeil = (int)Math.ceil(Slider.this.m_button.getWidth() / 2.0);
                    if (x < buttonWidthByTwoFloor) {
                        x = (int)buttonWidthByTwoFloor;
                    }
                    else if (x > Slider.this.m_size.width - buttonWidthByTwoCeil) {
                        x = Slider.this.m_size.width - (int)buttonWidthByTwoCeil;
                    }
                    final float value = (x - buttonWidthByTwoFloor) / (Slider.this.m_size.width - Slider.this.m_button.getWidth());
                    Slider.this.setValue(Slider.this.convertToBoundedValue(value));
                }
                else {
                    int y = e.getY(e.getCurrentTarget()) - Slider.this.m_lastPressedY;
                    final float buttonHeightByTwoFloor = (float)Math.floor(Slider.this.m_button.getHeight() / 2.0);
                    final float buttonHeightByTwoCeil = (float)Math.ceil(Slider.this.m_button.getHeight() / 2.0);
                    if (y < buttonHeightByTwoFloor) {
                        y = (int)buttonHeightByTwoFloor;
                    }
                    else if (y > Slider.this.m_size.height - buttonHeightByTwoCeil) {
                        y = Slider.this.m_size.height - (int)buttonHeightByTwoCeil;
                    }
                    final float value = (y - buttonHeightByTwoFloor) / (Slider.this.m_size.height - Slider.this.m_button.getHeight());
                    Slider.this.setValue(Slider.this.convertToBoundedValue(value));
                }
                return true;
            }
        };
        this.addEventListener(Events.MOUSE_DRAGGED, this.m_mouseDraggedListener, true);
    }
    
    @Override
    public void onCheckIn() {
        super.onCheckIn();
        this.m_button = null;
        this.m_separatorsImages.clear();
        this.m_buttonSizeIsDirty = false;
        this.m_pixmap = null;
        this.m_numFixedValuesDirty = false;
    }
    
    @Override
    public void onCheckOut() {
        super.onCheckOut();
        final SliderLayout sl = new SliderLayout();
        sl.onCheckOut();
        this.add(sl);
        (this.m_button = new Button()).onCheckOut();
        this.m_button.setCanBeCloned(false);
        this.add(this.m_button);
        this.m_lastPressedX = 0;
        this.m_lastPressedY = 0;
        this.m_nonBlocking = false;
        this.addSliderListeners();
    }
    
    @Override
    public boolean middleProcess(final int deltaTime) {
        final boolean ret = super.middleProcess(deltaTime);
        if (this.m_buttonSizeIsDirty || this.m_valueIsDirty) {
            this.invalidate();
        }
        return ret;
    }
    
    @Override
    public void copyElement(final BasicElement s) {
        final Slider e = (Slider)s;
        super.copyElement(e);
        e.m_value = this.m_value;
        e.m_minBound = this.m_minBound;
        e.m_maxBound = this.m_maxBound;
        e.m_fixedValues = this.m_fixedValues;
        e.m_numFixedValues = this.m_numFixedValues;
        e.m_unboundedValue = this.m_unboundedValue;
        e.m_horizontal = this.m_horizontal;
        e.m_jump = this.m_jump;
        e.m_sliderSize = this.m_sliderSize;
        e.m_separatorsImages = this.m_separatorsImages;
        e.setReadOnly(this.m_readOnly);
        e.setUseTween(this.m_useTween);
        e.setModulationColor(this.m_modulationColor);
        e.removeEventListener(Events.MOUSE_PRESSED, this.m_mousePressedListener, true);
        e.removeEventListener(Events.MOUSE_DRAGGED, this.m_mouseDraggedListener, true);
        e.m_styleIsDirty = true;
        e.setNeedsToPreProcess();
    }
    
    @Override
    public boolean setXMLAttribute(final int hash, final String value, final ConverterLibrary cl) {
        if (hash == Slider.HORIZONTAL_HASH) {
            this.setHorizontal(PrimitiveConverter.getBoolean(value));
        }
        else if (hash == Slider.READ_ONLY_HASH) {
            this.setReadOnly(PrimitiveConverter.getBoolean(value));
        }
        else if (hash == Slider.USE_TWEEN_HASH) {
            this.setUseTween(PrimitiveConverter.getBoolean(value));
        }
        else if (hash == Slider.MAX_BOUND_HASH) {
            this.setMaxBound(PrimitiveConverter.getInteger(value));
        }
        else if (hash == Slider.MIN_BOUND_HASH) {
            this.setMinBound(PrimitiveConverter.getInteger(value));
        }
        else if (hash == Slider.JUMP_HASH) {
            this.setJump(PrimitiveConverter.getFloat(value));
        }
        else if (hash == Slider.SLIDER_SIZE_HASH) {
            this.setSliderSize(PrimitiveConverter.getFloat(value));
        }
        else if (hash == Slider.VALUE_HASH) {
            this.setValue(PrimitiveConverter.getFloat(value));
        }
        else {
            if (hash != Slider.NUM_FIXED_VALUES_HASH) {
                return super.setXMLAttribute(hash, value, cl);
            }
            this.setNumFixedValues(PrimitiveConverter.getInteger(value));
        }
        return true;
    }
    
    @Override
    public boolean setPropertyAttribute(final int hash, final Object value) {
        if (hash == Slider.HORIZONTAL_HASH) {
            this.setHorizontal(PrimitiveConverter.getBoolean(value));
        }
        else if (hash == Slider.READ_ONLY_HASH) {
            this.setReadOnly(PrimitiveConverter.getBoolean(value));
        }
        else if (hash == Slider.USE_TWEEN_HASH) {
            this.setUseTween(PrimitiveConverter.getBoolean(value));
        }
        else if (hash == Slider.MAX_BOUND_HASH) {
            this.setMaxBound(PrimitiveConverter.getInteger(value));
        }
        else if (hash == Slider.MIN_BOUND_HASH) {
            this.setMinBound(PrimitiveConverter.getInteger(value));
        }
        else if (hash == Slider.JUMP_HASH) {
            this.setJump(PrimitiveConverter.getFloat(value));
        }
        else if (hash == Slider.SLIDER_SIZE_HASH) {
            this.setSliderSize(PrimitiveConverter.getFloat(value));
        }
        else if (hash == Slider.VALUE_HASH) {
            this.setValue(PrimitiveConverter.getFloat(value));
        }
        else {
            if (hash != Slider.NUM_FIXED_VALUES_HASH) {
                return super.setPropertyAttribute(hash, value);
            }
            this.setNumFixedValues(PrimitiveConverter.getInteger(value));
        }
        return true;
    }
    
    static {
        READ_ONLY_HASH = "readOnly".hashCode();
        USE_TWEEN_HASH = "useTween".hashCode();
        HORIZONTAL_HASH = "horizontal".hashCode();
        MAX_BOUND_HASH = "maxBound".hashCode();
        MIN_BOUND_HASH = "minBound".hashCode();
        JUMP_HASH = "jump".hashCode();
        SLIDER_SIZE_HASH = "sliderSize".hashCode();
        VALUE_HASH = "value".hashCode();
        NUM_FIXED_VALUES_HASH = "numFixedValues".hashCode();
    }
    
    private class SliderLayout extends AbstractLayoutManager
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
            if (Slider.this.m_pixmapIsDirty) {
                Slider.this.m_pixmapIsDirty = false;
            }
            if (Slider.this.m_numFixedValuesDirty) {
                while (Slider.this.m_numFixedValues < Slider.this.m_separatorsImages.size()) {
                    Slider.this.m_separatorsImages.remove(Slider.this.m_numFixedValues).destroySelfFromParent();
                }
                if (Slider.this.m_numFixedValues > Slider.this.m_separatorsImages.size()) {
                    if (Slider.this.m_separatorsImages.size() == 0) {
                        final Image image = new Image();
                        image.onCheckOut();
                        image.setNonBlocking(true);
                        image.setModulationColor(Slider.this.m_modulationColor);
                        image.add(Slider.this.m_pixmap.cloneElementStructure());
                        this.add(image);
                        Slider.this.m_separatorsImages.add(image);
                    }
                    while (Slider.this.m_numFixedValues > Slider.this.m_separatorsImages.size()) {
                        final Image image = (Image)Slider.this.m_separatorsImages.get(0).cloneElementStructure();
                        this.add(image);
                        Slider.this.m_separatorsImages.add(image);
                    }
                    Slider.this.remove(Slider.this.m_button);
                    this.add(Slider.this.m_button);
                }
                Slider.this.updateSeparatorsPosition();
                Slider.this.m_numFixedValuesDirty = false;
            }
            if (Slider.this.m_buttonSizeIsDirty) {
                Slider.this.updateButtonSize();
            }
            if (Slider.this.m_valueIsDirty) {
                Slider.this.updateButtonPosition();
                Slider.this.updateSeparatorsPosition();
            }
        }
    }
}
